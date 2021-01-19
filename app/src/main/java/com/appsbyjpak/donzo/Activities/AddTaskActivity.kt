package com.appsbyjpak.donzo.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.appsbyjpak.donzo.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class AddTaskActivity : AppCompatActivity() {

    var todoLists = ArrayList<String>()
    private lateinit var closeButton: ImageView
    private lateinit var listSpinner: Spinner
    private lateinit var taskTitleLayout: TextInputLayout
    private lateinit var taskTitleInput: TextInputEditText
    private lateinit var taskCategory: AutoCompleteTextView
    private lateinit var taskDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        closeButton = findViewById<ImageView>(R.id.close_add_task_button)
        closeButton.setOnClickListener{
            finish()
        }

        todoLists = arrayListOf("TODO List 1", "TODO List 2")
        listSpinner = findViewById(R.id.add_task_selected_list)
        ArrayAdapter(
                this,
                R.layout.list_selector_spinner,
                todoLists
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            listSpinner.adapter = adapter
        }

        taskTitleLayout = findViewById(R.id.add_task_title)
        taskTitleInput = findViewById(R.id.add_task_title_input)
        taskCategory = findViewById(R.id.add_task_category)

        val autoCategories = arrayListOf("category1", "category2", "category3", "hi", "yo")
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, autoCategories)
        taskCategory.setAdapter(adapter)

        val repeatCheckbox = findViewById<CheckBox>(R.id.add_task_repeat_checkbox)
        val repeatSelector = findViewById<LinearLayout>(R.id.repeat_selector_layout)
        repeatCheckbox.setOnClickListener{
            if (repeatCheckbox.isChecked) {
                Handler(Looper.getMainLooper()).postDelayed({
                    repeatSelector.visibility = View.VISIBLE
                }, 100)
            }
            else {
                Handler(Looper.getMainLooper()).postDelayed({
                    repeatSelector.visibility = View.INVISIBLE
                }, 100)
            }
        }

        val numArray = Array(100) { i -> (i+1) * 1 }
        val numberSelectorSpinner = findViewById<Spinner>(R.id.repeat_selector_number)
        ArrayAdapter(
                this,
                R.layout.repeat_selector_spinner,
                numArray
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            numberSelectorSpinner.adapter = adapter
        }

        val durationSelectorSpinner = findViewById<Spinner>(R.id.repeat_selector_duration)
        ArrayAdapter.createFromResource(
                this,
                R.array.durations,
                R.layout.repeat_selector_spinner
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            durationSelectorSpinner.adapter = adapter
        }

        taskDescription = findViewById(R.id.add_task_description)

        val saveButton = findViewById<Button>(R.id.save_add_task_button)
        saveButton.setOnClickListener{
            if (TextUtils.isEmpty(taskTitleInput.text)) {
                taskTitleLayout.error = "Task Title is required"
            } else {
                //prepare result for SeeView activity
                val intent = Intent()
                intent.putExtra("taskTitle", taskTitleInput.text.toString())
                intent.putExtra("taskCategory", taskCategory.text.toString())
                //set the result, it will be passed to onActivityResult() in SeeView activity
                //set the result, it will be passed to onActivityResult() in SeeView activity
                setResult(RESULT_OK, intent)
                finish() }
        }
    }
}