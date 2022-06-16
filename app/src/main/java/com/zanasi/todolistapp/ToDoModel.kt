package com.zanasi.todolistapp

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