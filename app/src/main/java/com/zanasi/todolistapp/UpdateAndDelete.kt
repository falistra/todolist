package com.zanasi.todolistapp

// implementata in MainActivity
// usata in ToDoAdapter
interface UpdateAndDelete {
    fun modifyItem(itemUID: String,isDone:Boolean)
    fun onItemDelete(itemUID: String)
    fun onItemInfo(itemUID: String)
}