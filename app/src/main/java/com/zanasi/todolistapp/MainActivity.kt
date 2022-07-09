package com.zanasi.todolistapp

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
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
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), UpdateAndDelete {

    // "lateinit" perche' inizializzata dopo in "onCreate"
    private lateinit var database: DatabaseReference

    // il contenitore dei dati
    private var toDOList: MutableList<ToDoModel> = mutableListOf()
    lateinit var toDoModelList: ToDoModelList

    // adatta un item/data ad essere un item/view
    private lateinit var adapter: ToDoAdapter

    // il widget che mostra la lista dei dati
    private var listViewItem: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get di un riferimento a un'istanza di FireBase
        // e, con questa, set della variabile globale "database"
        // (= visibile da tutte le activities)
        // dichiarata in MyApplication
        (this.application as MyApplication).database = FirebaseDatabase.getInstance().reference
        database = (this.application as MyApplication).database!!

        // set del layout associato
        setContentView(R.layout.activity_main)

        // set della barra in cima all'activity
        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.app_name)
        // actionBar.subtitle = "   sottotitolo"
        actionBar.setIcon(R.drawable.app_logo_foreground)
        actionBar.setDisplayUseLogoEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        // ==================================================================
        // il bottone per aggiungere un item
        //
        val addBtn: Button = findViewById(R.id.addItem)
        // al click del bottone si passa all'activity ItemInsert
        addBtn.setOnClickListener {
            val intent = Intent(this, ItemInsert::class.java)
            startActivity(intent)
        }


        // ==================================================================
        // la ListView che contiene la lista degli Items
        // ListView è un ViewGroup che visualizza
        // un elenco di elementi scorrevoli verticalmente.
        // Gli elementi dell'elenco vengono inseriti automaticamente
        // nell'elenco utilizzando un adapter,
        // e ogni elemento viene convertito in una "riga" in ListView.
        listViewItem = findViewById(R.id.item_listView)
        // l'adapter fa il lavoro di adattare un item della toDOList
        // dentro la listViewItem
        adapter = ToDoAdapter(this, toDOList)
        listViewItem!!.adapter = adapter
        toDoModelList = ToDoModelList(toDOList)

        // se un item viene aggiunto al db allora la lista
        // viene aggiornata : addItemToList
        // e ripulita degli item "done"
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toDoModelList.get()!!.clear()
                addItemToList(snapshot)
                clearList()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, getString(R.string.noItemAdd), Toast.LENGTH_LONG)
                    .show()
            }
        })

        // ==========================================================================
        // schedulazione di un job
        val component = ComponentName(this, JobSchedulato::class.java)
        val jobInfo = JobInfo.Builder(1, component)
            .setMinimumLatency(5000)
            .build()
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)


    }

    // metodo per riempire il menu quando l'utente lo apre  per la prima volta
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

                builder.setPositiveButton(R.string.ok) { _, _ ->
                    Toast.makeText(
                        applicationContext,
                        R.string.ok, Toast.LENGTH_SHORT
                    ).show()
                }
                builder.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addItemToList(snapshot: DataSnapshot) {

        val items = snapshot.children.iterator()
        if (items.hasNext()) {
            val toDoIndexedValue = items.next()
            val itemsIterator = toDoIndexedValue.children.iterator()
            while (itemsIterator.hasNext()) {
                val currentItem = itemsIterator.next()
                val toDoItemData = ToDoModel.creaToDoItem()
                val map = currentItem.value as HashMap<*, *>
                toDoItemData.UID = currentItem.key
                toDoItemData.done = map["done"] as Boolean
                toDoItemData.itemDataText = map["itemDataText"] as String?
                toDoModelList.get()!!.add(toDoItemData)
            }
        }
        adapter.notifyDataSetChanged()

    }

    fun clearList() {
        // la pulizia degli Items (data) viene fatta in un thread separato.
        // Poiche si deve operare su la List View definita nel main (UI) Thread,
        // questo viene fatto in runOnUiThread.
        // Il thread viene definito in due modi
        // 1: con la funzione thread
        thread {
            synchronized(this) {
                Thread.sleep(2000)
                runOnUiThread {
                    for (item in toDoModelList.listItems!!) {
                        if (item.done) {
                            val itemReference = database.child("todo").child(item.UID!!)
                            itemReference.removeValue()
                        }
                    }
                    toDoModelList.listItems!!.sort()
                    adapter.notifyDataSetChanged()
                }
            }
        }
        // 2: con una sub-class di Thread che definisce un object singleton
        // che implementa in metodo run()
        /*
        val thread = object : Thread() {
            override fun run() {
                synchronized(this) {
                    sleep(2000)
                    runOnUiThread {
                        for (item in toDoModelList.listItems!!) {
                            if (item.done == true) {
                                val itemReference = database.child("todo").child(item.UID!!)
                                itemReference.removeValue()
                            }
                        }
                        toDoModelList.listItems!!.sort()
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
        thread.start()

         */
    }

    // ====== metodi dell'interfaccia ==========================
    // implementazione del metodo modifyItem, in quanto la classe
    // MainActivity implementa l'interfaccia UpdateAndDelete
    override fun modifyItem(itemUID: String, isDone: Boolean) {
        // si ottiene dal database un ref ai dati di un solo item
        // a partire dall'ID univoco "itemUID"
        // Attenzione!! : senza Listener
        val itemReference = database.child("todo").child(itemUID)
        itemReference.child("done").setValue(isDone)
    }

    // implementazione del metodo modifyItem, in quanto la classe
    // MainActivity implementa l'interfaccia UpdateAndDelete
    override fun onItemDelete(itemUID: String) {
        val itemReference = database.child("todo").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }

    // implementazione del metodo modifyItem, in quanto la classe
    // MainActivity implementa l'interfaccia UpdateAndDelete
    override fun onItemInfo(itemUID: String) {
        // chiedo l'item ad DB usando l'ID univoco itemID

        database.child("todo").child(itemUID).get()
            .addOnSuccessListener {
                // quando e se l'ho ricevuto in "it"
                // map dei valori di it in un Bundle
                Log.i("firebase", "Ricevuto valore ${it.child("itemDataText")}")
                @Suppress("UNCHECKED_CAST")
                val data: Map<String, String> = it.value as Map<String, String>
                val bundle = Bundle()
                for (key in data.keys) {
                    bundle.putSerializable(key, data[key])
                }
                // istanza dell'intent esplicita verso l'activity ItemInfo
                val intent = Intent(this, ItemInfo::class.java)
                // aggingo all'inten il bundle
                intent.putExtras(bundle)
                // attivo l'activity di info
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.e("firebase", "Errore nel ricevere i dati", it)
            }
    }
}

// definizione di un job schedulato in background ... fatto solo a scopo di esercizio
// una sua istanza viene schedulata sopra nella onCreate dell'activity
class JobSchedulato : JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {
        Toast.makeText(applicationContext, "Job Cancelled ", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        Toast.makeText(applicationContext, getString(R.string.sortStart), Toast.LENGTH_SHORT).show()
        Toast.makeText(applicationContext, getString(R.string.sortEnded), Toast.LENGTH_SHORT).show()
        return false
    }
}
