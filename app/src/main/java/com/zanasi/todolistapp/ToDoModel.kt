package com.zanasi.todolistapp

import android.app.job.JobParameters
import android.app.job.JobService
import android.service.autofill.Validators.and
import android.util.Log
import android.widget.Toast

class ToDoModel : Comparable<ToDoModel> {
    companion object Factory {
        fun creaToDoItem(): ToDoModel = ToDoModel()
    }

    var UID: String? = null
    var itemDataText: String? = null
    var itemTime: String? = null
    var itemDate: String? = null

    // var itemWhoText: String? = null
    // var itemWhyText: String? = null
    // var itemWhereText: String? = null
    var done: Boolean? = false

    override fun compareTo(other: ToDoModel): Int {
        // da implementare itemDate
        if ((this.itemDate != null) and (other.itemDate == null)) return 1
        if ((this.itemDate == null) and (other.itemDate != null)) return -1
        // da perfezionare ...
        return 0
    }

}

class ToDoModelList() {
    var listItems: MutableList<ToDoModel>? = null
    var adapter: ToDoAdapter? = null

    constructor(listItems: MutableList<ToDoModel>?, adapter: ToDoAdapter?) : this() {
        this.listItems = listItems
        this.adapter = adapter
    }

    fun get(): MutableList<ToDoModel>? {
        return listItems
    }

    fun sort() {
        listItems!!.sort()
    }


}
