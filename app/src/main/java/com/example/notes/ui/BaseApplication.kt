package com.example.notes.ui

import android.app.Application
import com.example.notes.database.NotesDatabase

class BaseApplication: Application() {
    val database: NotesDatabase by lazy { NotesDatabase.getDatabase(this) }
}