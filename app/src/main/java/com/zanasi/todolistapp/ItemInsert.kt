package com.zanasi.todolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ItemInsert : AppCompatActivity() {

    private lateinit  var database: DatabaseReference
    var toDOList : MutableList<ToDoModel>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_insert)

        val bottoneAdd: Button = findViewById(R.id.addItemConfirm)
        bottoneAdd.setOnClickListener {
            val what : EditText = findViewById(R.id.editText_what)

            if (what.text.toString().isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.noWhatIns), Toast.LENGTH_SHORT).show()
            }
            else {
                val todoItemData = ToDoModel.createList()

                // recupero il ref al DB da MyApplication
                database = (this.application as MyApplication).database!!

                todoItemData.itemDataText = what.text.toString()

                // eventuali altri dati , legati al singolo item
/*
            val who : EditText = findViewById(R.id.editText_who)
            todoItemData.itemWhoText = who.text.toString()

            val why : EditText = findViewById(R.id.editText_why)
            todoItemData.itemWhyText = why.text.toString()

            val where : EditText = findViewById(R.id.editText_where)
            todoItemData.itemWhereText = where.text.toString()
*/
                val newItemData = database.child("todo").push()
                todoItemData.UID = newItemData.key
                newItemData.setValue(todoItemData)
                onBackPressed()
            }
        }
    }

/*
    bottoneAdd.setOnClickListener {
        finishActivity(0)
    }
*/
}