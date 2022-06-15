package com.zanasi.todolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ItemInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_info)
        val extras : Bundle? = getIntent().getExtras()
        val what = extras?.getString("itemDataText")
        var quandoData = extras?.getString("itemDate").toString()
        // if (quandoData.isNullOrEmpty()) quandoData = ""
        var quandoOra = extras?.getString("itemTime").toString()
        // if (quandoOra.isNullOrEmpty()) quandoOra = ""

        val info_data_textView: TextView = findViewById(R.id.info_data_textView)
        info_data_textView.setText(what.toString())

        val infoQuando: TextView = findViewById(R.id.infoQuandoTextView)
        infoQuando.text = "$quandoOra $quandoData"
    }
}