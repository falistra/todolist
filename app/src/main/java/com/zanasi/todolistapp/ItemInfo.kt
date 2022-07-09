package com.zanasi.todolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ItemInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_info)
        val extras : Bundle? = intent.extras
        val what = extras?.getString("itemDataText")

        val infodatatextView: TextView = findViewById(R.id.info_data_textView)
        infodatatextView.text = what.toString()

        val infoQuando: TextView = findViewById(R.id.infoQuandoTextView)
        infoQuando.text = extras?.getString("itemDate").toString()
    }
}