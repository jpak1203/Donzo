package com.appsbyjpak.donzo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.VISIBLE
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.setPadding
import androidx.drawerlayout.widget.DrawerLayout
import com.appsbyjpak.donzo.Activities.AddTaskActivity
import com.appsbyjpak.donzo.Adapters.NavigationAdapter
import com.appsbyjpak.donzo.Adapters.TodoListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private val REQ_CODE_ADD_VIEW = 0
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var navigationTodoLists: ListView
    private lateinit var closeDrawerButton: ImageView
    private lateinit var addTodoListItem: EditText
    private lateinit var addTodoListButton: Button
    private lateinit var todoItemsLinearLayout: LinearLayout
    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton
    private val db = Firebase.firestore
    private val lists = db.collection("list")
    private val tasks = db.collection("task")


    private lateinit var taskTitle: String
    private lateinit var taskCategory: String
    private var mapOfTasks: HashMap<String, ArrayList<String?>> = HashMap()
    private var todoItemListView: ListView? = null
    private lateinit var  todoItemListAdapter: TodoListAdapter

    var todoLists = ArrayList<String>()
    var todoListAdapter: NavigationAdapter? = null
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbTodoListsArray = arrayListOf<String>()
        lists.orderBy("timeStamp")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    dbTodoListsArray.add(document.data["title"].toString())
                    Log.d("list", "${document.id} => ${document.data}")
                }
                setContentView(R.layout.activity_main)

                if (savedInstanceState?.getInt("activeList") != null) position = savedInstanceState.getInt("activeList")

                // Custom Action bar
                toolbar = findViewById(R.id.main_todo_toolbar)
                setSupportActionBar(toolbar)

                val actionbar: ActionBar? = supportActionBar
                actionbar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
                    setDisplayShowTitleEnabled(false)
                }

                // Navigation Menu
                mDrawerLayout = findViewById(R.id.drawer_layout)
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                closeDrawerButton = findViewById(R.id.close_drawer_button)
                closeDrawerButton.setOnClickListener {
                    mDrawerLayout.closeDrawer(Gravity.LEFT)
                }

                navigationTodoLists = findViewById(R.id.nav_todo_lists)

                todoLists = dbTodoListsArray
                todoListAdapter = NavigationAdapter(this, todoLists, 0)

                navigationTodoLists.adapter = todoListAdapter

                //on first start -- set active list as the first list
                navigationTodoLists.setItemChecked(position, true)
                todoListAdapter?.notifyDataSetChanged()
                toolbar.findViewById<TextView>(R.id.main_toolbar_title).text = navigationTodoLists.adapter.getItem(
                    0
                ).toString()

                navigationTodoLists.onItemClickListener = OnItemClickListener { _, _, position, _ ->
                    this.position = position
                    navigationTodoLists.setItemChecked(position, true)
                    todoListAdapter?.activeTodoListIndex = position
                    todoListAdapter?.notifyDataSetChanged()
                    mDrawerLayout.closeDrawers()
                    toolbar.findViewById<TextView>(R.id.main_toolbar_title).text = navigationTodoLists.adapter.getItem(
                        position
                    ).toString()
                }

                Log.d("list", toolbar.findViewById<TextView>(R.id.main_toolbar_title).text.toString())
                lists.whereEqualTo("title", toolbar.findViewById<TextView>(R.id.main_toolbar_title).text.toString())
                    .get()
                    .addOnSuccessListener { result ->
                        var listId = result.documents[0].reference
                        todoItemsLinearLayout = findViewById(R.id.todo_items)
                        tasks.whereEqualTo("list_id", listId)
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    Log.d("list", "${document.id} => ${document.data}")
                                    taskTitle = document.data["title"].toString()
                                    taskCategory = document.data["category"].toString()
                                    if (taskCategory == "") taskCategory = "Uncategorized"

                                    todoItemListView = findViewById(taskCategory.hashCode())

                                    val colorArray = resources.getStringArray(R.array.todo_colors)
                                    var color = Color.parseColor(colorArray[(0..2).random()])

                                    if (todoItemListView == null) {
                                        mapOfTasks[taskCategory] = arrayListOf(taskTitle)

                                        todoItemListView = ListView(this)
                                        todoItemListView?.id = taskCategory.hashCode()

                                        val todoItemListCategory = TextView(this)
                                        todoItemListCategory.id = taskCategory.hashCode() + "header".hashCode()
                                        todoItemListCategory.text = taskCategory
                                        todoItemListCategory.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                                        todoItemListCategory.gravity = Gravity.CENTER
                                        todoItemListCategory.setTextColor(ContextCompat.getColor(this, R.color.light))
                                        todoItemListCategory.setPadding(10)
                                        todoItemListCategory.setBackgroundColor(color)
                                        todoItemListView?.addHeaderView(todoItemListCategory, null, false)

                                        todoItemListAdapter = TodoListAdapter(this, mapOfTasks[taskCategory]!!, color)
                                        todoItemListView?.adapter = todoItemListAdapter
                                        todoItemsLinearLayout.addView(todoItemListView, 0)
                                    } else {
                                        mapOfTasks[taskCategory]?.add(taskTitle)
                                        color = (todoItemListView?.findViewById<TextView>(taskCategory.hashCode() + "header".hashCode())?.background as ColorDrawable).color
                                        val todoItemListAdapter = TodoListAdapter(this, mapOfTasks[taskCategory]!!, color)
                                        todoItemListView?.adapter = todoItemListAdapter
                                        todoItemListAdapter.notifyDataSetChanged()
                                    }

                                    todoItemListView?.setOnItemClickListener { _, view, _, _ ->
                                        if (view.background == null || (view.background as ColorDrawable).color == Color.TRANSPARENT) {
                                            view.setBackgroundColor(color)
                                            view.background.alpha = 30
                                            view.findViewById<TextView>(R.id.todo_list_category).apply {
                                                paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                                            }
                                        }
                                        else {
                                            view.setBackgroundColor(Color.TRANSPARENT)
                                            view.findViewById<TextView>(R.id.todo_list_category).apply {
                                                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                                            }
                                        }
                                        Log.d("BACKGROUND", view.background.toString())
                                    }
                                }
                            }
                    }

                addTodoListButton = findViewById(R.id.add_new_list_button)
                addTodoListItem = findViewById(R.id.add_todo_list_item)
                addTodoListButton.setOnClickListener {
                    addTodoListItem.visibility = VISIBLE
                    addTodoListItem.requestFocus()
                    showKeyboard()
                    addTodoListItem.setOnEditorActionListener { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (addTodoListItem.text.toString().isNotEmpty()) {
                                val todoList = hashMapOf(
                                    "title" to addTodoListItem.text.toString(),
                                    "archived" to false,
                                    "timeStamp" to Timestamp.now()
                                )
                                lists.add(todoList)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d("list", "DocumentSnapshot added with ID: ${documentReference.id}")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("list", "Error adding document", e)
                                    }

                                dbTodoListsArray.add(addTodoListItem.text.toString())
                                todoListAdapter?.notifyDataSetChanged()
                                addTodoListItem.visibility = GONE
                                addTodoListItem.text.clear()
                            }
                            hideKeyboard()
                        }
                        false
                    }
                }

                fab = findViewById(R.id.fab)
                fab.setOnClickListener {
                    val myIntent = Intent(this, AddTaskActivity::class.java)
                    startActivityForResult(myIntent, REQ_CODE_ADD_VIEW, null);
                }

            }
            .addOnFailureListener { exception ->
                Log.w("list", "Error getting documents.", exception)
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v = currentFocus
        if (v != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
                v is EditText &&
                !v.javaClass.name.startsWith("android.webkit.")) {
            val sourceCoordinates = IntArray(2)
            v.getLocationOnScreen(sourceCoordinates)
            val x = ev.rawX + v.getLeft() - sourceCoordinates[0]
            val y = ev.rawY + v.getTop() - sourceCoordinates[1]
            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                hideKeyboard()
                addTodoListItem.visibility = GONE
                addTodoListItem.text.clear()
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_ADD_VIEW) {
                val listId = lists.document(data?.getStringExtra("listId").toString())

                tasks.whereEqualTo("list_id", listId)
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            taskTitle = document.data["title"].toString()
                            taskCategory = document.data["category"].toString()
                            if (taskCategory == "") taskCategory = "Uncategorized"

                            val colorArray = resources.getStringArray(R.array.todo_colors)
                            var color = Color.parseColor(colorArray[(0..2).random()])

                            todoItemListView = findViewById(taskCategory.hashCode())
                            if (todoItemListView == null) {
                                mapOfTasks[taskCategory] = arrayListOf(taskTitle)

                                todoItemListView = ListView(this)
                                todoItemListView?.id = taskCategory.hashCode()

                                val todoItemListCategory = TextView(this)
                                todoItemListCategory.id = taskCategory.hashCode() + "header".hashCode()
                                todoItemListCategory.text = taskCategory
                                todoItemListCategory.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                                todoItemListCategory.gravity = Gravity.CENTER
                                todoItemListCategory.setTextColor(ContextCompat.getColor(this, R.color.light))
                                todoItemListCategory.setPadding(10)
                                todoItemListCategory.setBackgroundColor(color)
                                todoItemListView?.addHeaderView(todoItemListCategory, null, false)

                                todoItemListAdapter = TodoListAdapter(this, mapOfTasks[taskCategory]!!, color)
                                todoItemListView?.adapter = todoItemListAdapter
                                todoItemsLinearLayout.addView(todoItemListView, 0)
                            } else {
                                if (!mapOfTasks[taskCategory]!!.contains(taskTitle)) mapOfTasks[taskCategory]?.add(taskTitle)
                                todoItemListAdapter.notifyDataSetChanged()
                            }
                        }
                    }
            }
        }
    }

    private fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }
}