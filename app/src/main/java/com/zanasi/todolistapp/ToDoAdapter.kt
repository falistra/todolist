package com.zanasi.todolistapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


// classe che estende BaseAdapter
// serve in MainActivity per "adattare" la "struttura dati" toDoList MutableList<ToDoModel>
// in una lista di View che verranno inserite lenna ListView

// BaseAdapter e' astratta e e' richiesta l'implementazione dei seguenti metodi

// int getCount() -> la dimensione di toDoList
// Object getItem(int position) -> indexing di toDoList
// long getItemId(int position) -> cast da int a long

// View getView(int position, View convertView, ViewGroup parent) ->
// metodo che fa il grosso del lavoro per popolare con i dati  una View
// a partire da R.layout.row_itemslayout
// Inoltre definisce i Listeners come chiamate a metodi
// di un interfaccia UpdateAndDelete che verra' implementata nel Main

// vedi https://guides.codepath.com/android/Using-a-BaseAdapter-with-ListView


class ToDoAdapter (context: Context, toDoList:MutableList<ToDoModel>) : BaseAdapter() {
    private val inflater : LayoutInflater = LayoutInflater.from(context)


    // la struttura che contiene i dati
    private var itemList = toDoList

    // qui context (dove viene inserita la View)
    // e' visto come (as) l'implementazione dell'interfaccia UpdateAndDelete
    private var updateAndDelete : UpdateAndDelete = context as UpdateAndDelete

    override fun getCount(): Int {
        return  itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    // essenzialmente restituisce (se convertView == null) una view da
    // view = inflater.inflate(R.layout.row_itemslayout,parent,false)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val UID : String = itemList[position].UID as String
        val itemTextData = itemList[position].itemDataText as String
        val done : Boolean = itemList[position].done as Boolean

        val view : View
        // usa la classe accessoria (private) ListViewHolder definita sotto
        // e' solo una scelta di comodita' e chiarezza
        val viewHolder : ListViewHolder

        if (convertView == null) {
            // se la view e' da convertire
            view = inflater.inflate(R.layout.row_itemslayout,parent,false)
            // associa (.tag) alla view un viewHolder
            viewHolder = ListViewHolder(view)
            view.tag = viewHolder
        } else {
            // se la view e' gia' convertita
            view = convertView
            // recupera il viewHolder
            viewHolder = view.tag as ListViewHolder
        }

        // imposta gli attributi del viewHolder
        viewHolder.textLabel.text = itemTextData
        viewHolder.isDone.isChecked = done
        // se "fatto" cambia lo sfondo
        if (viewHolder.isDone.isChecked) {
            viewHolder.back.setBackgroundColor(R.drawable.done)
        } else {
            // altrimenti ripristina lo sfondo a "non fatto"
            viewHolder.back.setBackgroundResource(R.drawable.custom)
        }

        // associa i Listeners ai componenti (bottoni) del viewHolder
        // al bottone a sinistra (checkbox "fatto")
        viewHolder.isDone.setOnClickListener {
            updateAndDelete.modifyItem(UID,!done)
            if (viewHolder.isDone.isChecked) {
                viewHolder.back.setBackgroundColor(R.drawable.done)
            } else {
                // altrimenti ripristina lo sfondo a "non fatto"
                viewHolder.back.setBackgroundResource(R.drawable.custom)
            }
        }
        // al bottone a destra di cancellazione dell'Item (dalla lista e dal db)
        viewHolder.isDeleted.setOnClickListener {
            updateAndDelete.onItemDelete(UID)
        }
        // al bottone "lente" per info
        viewHolder.isInfo.setOnClickListener {
            updateAndDelete.onItemInfo(UID)
        }

        return view
    }

    // classe privata usata sopra per gestire i Listeners
    // dei diversi componenti della view
    // Viene create durante la convertview e associata a view.tag
    private class ListViewHolder(row:View?) {
        val textLabel : TextView = row!!.findViewById(R.id.item_textView) as TextView
        val isDone : CheckBox = row!!.findViewById(R.id.checkbox) as CheckBox
        val isDeleted : ImageButton = row!!.findViewById(R.id.close) as ImageButton
        val isInfo : ImageButton = row!!.findViewById(R.id.info) as ImageButton
        val back : RelativeLayout = row!!.findViewById(R.id.itemlayout) as RelativeLayout
    }

}