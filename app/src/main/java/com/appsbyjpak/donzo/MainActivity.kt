package com.appsbyjpak.donzo

import android.content.Context
import android.os.Bundle
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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout


class MainActivity : AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var navigationTodoLists: ListView
    private lateinit var closeDrawerButton: ImageView
    private lateinit var addTodoListItem: EditText
    private lateinit var addTodoListButton: Button
    var todoLists = ArrayList<String>()
    var todoListAdapter: ArrayAdapter<String>? = null
    var activeTodoList: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Custom Action bar
        val toolbar: Toolbar = findViewById(R.id.todo_toolbar)
        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
            setDisplayShowTitleEnabled(false)
        }

        // Navigation Menu
        mDrawerLayout = findViewById(R.id.drawer_layout)

        closeDrawerButton = findViewById<ImageView>(R.id.close_drawer_button)
        closeDrawerButton.setOnClickListener {
            mDrawerLayout.closeDrawer(Gravity.LEFT)
        }

        navigationTodoLists = findViewById(R.id.nav_todo_lists)

        todoLists = arrayListOf("TODO List 1", "TODO List 2")
        todoListAdapter = ArrayAdapter(this,
                R.layout.navigation_list,
                R.id.todo_list_item,
                todoLists)

        navigationTodoLists.adapter = todoListAdapter

        if (activeTodoList == null) {
            navigationTodoLists.setItemChecked(0, true)
            activeTodoList = navigationTodoLists.adapter.getItem(navigationTodoLists.checkedItemPosition).toString()
            toolbar.findViewById<TextView>(R.id.toolbar_title).text = activeTodoList
        }

        navigationTodoLists.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            mDrawerLayout.closeDrawers()
            navigationTodoLists.setItemChecked(position, true)
            activeTodoList = navigationTodoLists.adapter.getItem(navigationTodoLists.checkedItemPosition).toString()
            toolbar.findViewById<TextView>(R.id.toolbar_title).text = activeTodoList
        }

        addTodoListButton = findViewById<Button>(R.id.add_new_list_button)
        addTodoListItem = findViewById<EditText>(R.id.add_todo_list_item)
        addTodoListButton.setOnClickListener {
            addTodoListItem.visibility = VISIBLE
            addTodoListItem.requestFocus()
            showKeyboard()
            addTodoListItem.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (addTodoListItem.text.toString().isNotEmpty()) {
                        todoListAdapter?.add(addTodoListItem.text.toString())
                        todoListAdapter?.notifyDataSetChanged()
                        addTodoListItem.visibility = GONE
                        addTodoListItem.text.clear()
                    }
                    hideKeyboard()
                }
                false
            }
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

    private fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }
}