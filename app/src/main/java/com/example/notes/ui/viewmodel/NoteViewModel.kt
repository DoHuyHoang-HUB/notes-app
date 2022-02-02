package com.example.notes.ui.viewmodel

import androidx.lifecycle.*
import com.example.notes.dao.NoteDao
import com.example.notes.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(
    private val noteDao: NoteDao
): ViewModel() {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes().asLiveData()

    private fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insertNote(note)
        }
    }

    fun addNote(
        title: String,
        dateTime: String,
        subtitle: String,
        noteText: String,
        imagePath: String,
        color: String,
        webLink: String
    ) {
        val note = Note(
            title = title,
            dateTime = dateTime,
            subtitle = subtitle,
            noteText = noteText,
            imagePath = imagePath,
            color = color,
            webLink = webLink
        )
        insertNote(note)
    }
}

class NoteViewModelFactory(private val noteDao: NoteDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}