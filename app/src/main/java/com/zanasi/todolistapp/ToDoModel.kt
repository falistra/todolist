package com.zanasi.todolistapp

import android.app.job.JobParameters
import android.app.job.JobService

class ToDoModel : Comparable<ToDoModel> {
    companion object Factory {
        fun creaToDoItem() : ToDoModel = ToDoModel()
    }
    var UID : String? = null
    var itemDataText: String? = null
    var itemTime: String? = null
    var itemDate: String? = null
    // var itemWhoText: String? = null
    // var itemWhyText: String? = null
    // var itemWhereText: String? = null
    var done: Boolean? = false

    override fun compareTo(other: ToDoModel): Int {
        // da implementare itemDate
        return this.itemDate!!.compareTo(other.itemDate!!)
    }
}

class ToDoModelList(listItems: MutableList<ToDoModel>?)  {
    val listItems = listItems
    fun get() : MutableList<ToDoModel>? {
        return listItems
    }

}

class MyJobScheduler : JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        TODO("Not yet implemented")
    }
}