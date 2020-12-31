package com.appsbyjpak.donzo.Activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.appsbyjpak.donzo.R

class AddTaskActivity : AppCompatActivity() {

    var todoLists = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val closeButton = findViewById<ImageView>(R.id.close_add_task_button)
        closeButton.setOnClickListener{
            finish()
        }

        todoLists = arrayListOf("TODO List 1", "TODO List 2")
        val listSpinner = findViewById<Spinner>(R.id.add_task_selected_list)
        ArrayAdapter(this,
            R.layout.list_selector_spinner,
            todoLists
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            listSpinner.adapter = adapter
        }

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
        ArrayAdapter(this,
                R.layout.repeat_selector_spinner,
                numArray
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            numberSelectorSpinner.adapter = adapter
        }


        val durationSelectorSpinner = findViewById<Spinner>(R.id.repeat_selector_duration)
        ArrayAdapter.createFromResource(this,
                R.array.durations,
                R.layout.repeat_selector_spinner
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            durationSelectorSpinner.adapter = adapter
        }

        val saveButton = findViewById<Button>(R.id.save_add_task_button)
        saveButton.setOnClickListener{
            finish()
        }
    }
}