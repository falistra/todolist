package com.zanasi.todolistapp


class ToDoModel : Comparable<ToDoModel> {
    companion object Factory {
        fun creaToDoItem(): ToDoModel = ToDoModel()
    }

    var UID: String? = null
    var itemDataText: String? = null
    var itemTime: String? = null
    var itemDate: String? = null
    var done: Boolean = false

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

    constructor(listItems: MutableList<ToDoModel>?) : this() {
        this.listItems = listItems
    }

    fun get(): MutableList<ToDoModel>? {
        return listItems
    }

    fun sort() {
        listItems!!.sort()
    }


}
