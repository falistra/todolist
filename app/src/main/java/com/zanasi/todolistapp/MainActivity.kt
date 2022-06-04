package com.zanasi.todolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
// import android.widget.Adapter
// import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
// import androidx.appcompat.app.AlertDialog
// import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), UpdateAndDelete {

    private lateinit  var database: DatabaseReference
    var toDOList : MutableList<ToDoModel>? = null
    private lateinit var adapter : ToDoAdapter
    private var listViewItem : ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // val fab = findViewById<android.view.View>(R.id.fab) as FloatingActionButton
        listViewItem = findViewById(R.id.item_listView)

        database = FirebaseDatabase.getInstance().reference
/*
        fab.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            val textEditText = EditText(this)
            alertDialog.setMessage("")
            alertDialog.setTitle("Add TODO Item")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Add") { dialog, _ ->
                val todoItemData = ToDoModel.createList()
                todoItemData.itemDataText = textEditText.text.toString()
                todoItemData.done = false
                val newItemData = database.child("todo").push()
                todoItemData.UID = newItemData.key
                newItemData.setValue(todoItemData)
                dialog.dismiss()
                Toast.makeText(this,"item saved",Toast.LENGTH_LONG).show()
            }

            alertDialog.show()
        }
        */

        toDOList = mutableListOf()

        adapter = ToDoAdapter(this,toDOList!!)
        listViewItem!!.adapter = adapter
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toDOList!!.clear()
                addItemToList(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,"No item added",Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun addItemToList (snapshot: DataSnapshot) {
        val items = snapshot.children.iterator()
        if (items.hasNext()) {
            val toDoIndexedValue = items.next()
            val itemsIterator = toDoIndexedValue.children.iterator()
            while (itemsIterator.hasNext()) {
                val currentItem = itemsIterator.next()
                val toDoItemData = ToDoModel.createList()
                val map = currentItem.value as HashMap<*, *>

                toDoItemData.UID = currentItem.key
                toDoItemData.done = map["done"] as Boolean?
                toDoItemData.itemDataText = map["itemDataText"] as String?
                toDOList!!.add(toDoItemData)
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun modifyItem(itemUID: String, isDone: Boolean) {
        val itemReference = database.child("todo").child(itemUID)
        itemReference.child("done").setValue(isDone)
    }

    override fun onItemDelete(itemUID: String) {
        val itemReference = database.child("todo").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }
}