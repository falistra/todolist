package com.zanasi.todolistapp

class ToDoModel {
    companion object Factory {
        fun createList() : ToDoModel = ToDoModel()
    }
    var UID : String? = null
    var itemDataText: String? = null
    var itemTime: String? = null
    var itemDate: String? = null
    var itemWhoText: String? = null
    var itemWhyText: String? = null
    var itemWhereText: String? = null
    var done: Boolean? = false
}