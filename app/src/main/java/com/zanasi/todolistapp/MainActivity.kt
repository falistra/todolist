package com.zanasi.todolistapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), UpdateAndDelete {

    // "lateinit" perche' inizializzata dopo in "onCreate"
    private lateinit  var database: DatabaseReference

    // il contenitore dei dati
    var toDOList : MutableList<ToDoModel>? = null

    private lateinit var adapter : ToDoAdapter

    // il widget che mostra la lista dei dati
    private var listViewItem : ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get di un riferimento a un'istanza di FireBase
        // e con questa set della variabile globale "database" (= visibile da tutte le activities)
        // dichiarata in MyApplication
        (this.application as MyApplication).database = FirebaseDatabase.getInstance().reference
        database = (this.application as MyApplication).database!!

        setContentView(R.layout.activity_main)

        // set della barra in cima all'activity
        val actionBar = supportActionBar
        actionBar!!.title =  getString(R.string.app_name)
        // actionBar.subtitle = "   sottotitolo"
        actionBar.setIcon(R.drawable.app_logo_foreground)
        actionBar.setDisplayUseLogoEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        // la ListView che contiene la lista degli Item
        // ListView è un ViewGroup che visualizza
        // un elenco di elementi scorrevoli verticalmente.
        // Gli elementi dell'elenco vengono inseriti automaticamente
        // nell'elenco utilizzando un adapter,
        // e ogni elemento viene convertito in una riga in ListView.

        listViewItem = findViewById(R.id.item_listView)

        // connessione al database Firebase

        // il bottone per aggiungere un item
        val addBtn : Button = findViewById(R.id.addItem)
        // al click del bottone si passa all'activity ItemInsert
        addBtn.setOnClickListener {
            val intent = Intent(this, ItemInsert::class.java)
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
                Toast.makeText(applicationContext,getString(R.string.noItemAdd),Toast.LENGTH_LONG).show()
            }

        })

    }

    // metodo per riempire il menu quando l'utente lo apre
    // per la prima volta
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // metodo che associa azioni alle voci del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addItem -> {
                val intent = Intent(this, ItemInsert::class.java)
                startActivity(intent)
            }
            R.id.info -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.info))
                builder.setMessage(R.string.app_name)

                builder.setPositiveButton(R.string.ok) { dialog, which ->
                    Toast.makeText(applicationContext,
                        R.string.ok, Toast.LENGTH_SHORT).show()
                }
                builder.show()
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
                val toDoItemData = ToDoModel.creaToDoItem()
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

                val intent = Intent(this, ItemInfo::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            .addOnFailureListener{
                Log.e("firebase", "Errore nel ricevere i dati", it)
            }
        }
}
