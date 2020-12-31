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
import com.appsbyjpak.donzo.Adapters.NavigationAdapter
import com.appsbyjpak.donzo.R


class MainActivity : AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var navigationTodoLists: ListView
    private lateinit var closeDrawerButton: ImageView
    private lateinit var addTodoListItem: EditText
    private lateinit var addTodoListButton: Button
    var todoLists = ArrayList<String>()
    var todoListAdapter: NavigationAdapter? = null

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
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        closeDrawerButton = findViewById(R.id.close_drawer_button)
        closeDrawerButton.setOnClickListener {
            mDrawerLayout.closeDrawer(Gravity.LEFT)
        }

        navigationTodoLists = findViewById(R.id.nav_todo_lists)

        todoLists = arrayListOf("TODO List 1", "TODO List 2")
        todoListAdapter = NavigationAdapter(this, todoLists, 0)

        navigationTodoLists.adapter = todoListAdapter

        //on first start -- set active list as the first list
        navigationTodoLists.setItemChecked(0, true)
        todoListAdapter?.notifyDataSetChanged()
        toolbar.findViewById<TextView>(R.id.toolbar_title).text = navigationTodoLists.adapter.getItem(0).toString()

        navigationTodoLists.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            navigationTodoLists.setItemChecked(position, true)
            todoListAdapter?.activeTodoListIndex = position
            todoListAdapter?.notifyDataSetChanged()
            mDrawerLayout.closeDrawers()
            toolbar.findViewById<TextView>(R.id.toolbar_title).text = navigationTodoLists.adapter.getItem(position).toString()
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
                        todoLists?.add(addTodoListItem.text.toString())
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