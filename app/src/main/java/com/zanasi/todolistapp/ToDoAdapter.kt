package com.zanasi.todolistapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class ToDoAdapter (context: Context,toDoList:MutableList<ToDoModel>) : BaseAdapter() {

    private val inflater : LayoutInflater = LayoutInflater.from(context)
    private var itemList = toDoList
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val UID : String = itemList[position].UID as String
        val itemTextData = itemList[position].itemDataText as String
        val done : Boolean = itemList[position].done as Boolean

        val view : View
        val viewHolder : ListViewHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.row_itemslayout,parent,false)
            viewHolder = ListViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ListViewHolder
        }

        viewHolder.textLabel.text = itemTextData
        viewHolder.isDone.isChecked = done
        if (viewHolder.isDone.isChecked) {
            viewHolder.back.setBackgroundColor(Color.parseColor("#32CD32"))
        }

        viewHolder.isDone.setOnClickListener {
            updateAndDelete.modifyItem(UID,!done)
            viewHolder.back.setBackgroundColor(Color.parseColor("#32CD32"))
        }
        viewHolder.isDeleted.setOnClickListener {
            updateAndDelete.onItemDelete(UID)
        }

        return view
    }

    private class ListViewHolder(row:View?) {
        val textLabel : TextView = row!!.findViewById(R.id.item_textView) as TextView
        val isDone : CheckBox = row!!.findViewById(R.id.checkbox) as CheckBox
        val isDeleted : ImageButton = row!!.findViewById(R.id.close) as ImageButton
        val back : RelativeLayout = row!!.findViewById(R.id.itemlayout) as RelativeLayout
    }


}