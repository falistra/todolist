package com.zanasi.todolistapp

import android.app.Application
import com.google.firebase.database.*

class MyApplication : Application() {
    // database verrà inizializzato nella MainActivity .
    // E' messo qui per avere un UNICO (nel senso di fatto una volta sola)
    // accesso da tutte le activities
    var database: DatabaseReference? = null
        get() = field                     // getter
        set(value) { field = value }      // setter

}