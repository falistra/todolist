package com.zanasi.todolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference

class ItemInsert : AppCompatActivity() {

    private lateinit  var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_insert)
        val viewWhen : TextView = findViewById(R.id.whenText)
        val viewDate : TextView = findViewById(R.id.dateText)

        val bottoneWhen: Button = findViewById(R.id.whenButton)
        bottoneWhen.setOnClickListener {
            // https://developer.android.com/reference/android/widget/TimePicker
            val setTime = SetTime(viewWhen)
            setTime.show(supportFragmentManager, "timePicker")
        }

        val bottoneDate : Button = findViewById(R.id.dateButton)
        bottoneDate.setOnClickListener {
            val setDate = SetDate(viewDate)
            setDate.show(supportFragmentManager, "datePicker")
        }

        val bottoneAdd: Button = findViewById(R.id.addItemConfirm)
        bottoneAdd.setOnClickListener {
            val what : EditText = findViewById(R.id.editText_what)
            val quandoTime : TextView = viewWhen
            val quandoData : TextView = viewDate

            if (what.text.toString().isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.noWhatIns), Toast.LENGTH_SHORT).show()
            }
            else {
                val todoItemData = ToDoModel.creaToDoItem()

                // recupero il ref al DB da MyApplication
                database = (this.application as MyApplication).database!!

                todoItemData.itemDataText = what.text.toString()
                todoItemData.itemTime = quandoTime.text.toString()
                todoItemData.itemDate = quandoData.text.toString()

                val newItemData = database.child("todo").push()
                todoItemData.UID = newItemData.key
                newItemData.setValue(todoItemData)
                onBackPressed()
            }
        }
    }

}