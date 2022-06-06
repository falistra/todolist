package com.zanasi.todolistapp

interface UpdateAndDelete {
    fun modifyItem(itemUID: String,isDone:Boolean)
    fun onItemDelete(itemUID: String)
    fun onItemInfo(itemUID: String)
}