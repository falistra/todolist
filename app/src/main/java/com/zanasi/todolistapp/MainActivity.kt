package com.zanasi.todolistapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), UpdateAndDelete {

    private lateinit  var database: DatabaseReference
    var toDOList : MutableList<ToDoModel>? = null
    private lateinit var adapter : ToDoAdapter
    private var listViewItem : ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val actionBar = supportActionBar

        // providing title for the ActionBar
        actionBar!!.title =  getString(R.string.app_name)

        // providing subtitle for the ActionBar
        // actionBar.subtitle = "   Design a custom Action Bar"

        // adding icon in the ActionBar
        actionBar.setIcon(R.drawable.app_logo_foreground)

        actionBar.setDisplayUseLogoEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        listViewItem = findViewById(R.id.item_listView)
        database = FirebaseDatabase.getInstance().reference
        val addBtn : Button = findViewById(R.id.addItem)

        addBtn.setOnClickListener {
            val intent = Intent(this, ItemInsert::class.java).apply {
            }
            startActivity(intent)
        }

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

    // method to inflate the options menu when
    // the user opens the menu for the first time
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // methods to control the operations that will
    // happen when user clicks on the action buttons
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addItem -> {
                val intent = Intent(this, ItemInsert::class.java).apply {
                }
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
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

    override fun onItemInfo(itemUID: String) {
        database.child("todo").child(itemUID).get()
            .addOnSuccessListener {
                Log.i("firebase", "Ricevuto valore ${it.child("itemDataText")}")
                val data: Map<String, String> = it.getValue() as HashMap<String, String>
                val bundle = Bundle()
                for (key in data.keys) {
                    bundle.putSerializable(key, data.get(key))
                }

                val intent = Intent(this, ItemInfo::class.java).apply {
                    }
                intent.putExtras(bundle)
                startActivity(intent)
            }
            .addOnFailureListener{
                Log.e("firebase", "Errore nel ricevere i dati", it)
            }
        }
}