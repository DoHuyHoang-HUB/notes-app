package com.example.notes.ui.viewmodel

import androidx.lifecycle.*
import com.example.notes.dao.NoteDao
import com.example.notes.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NoteViewModel(
    private val noteDao: NoteDao
): ViewModel() {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes().asLiveData()

    val dateNow = getNow()

    private val _selectedNoteColor = MutableLiveData<String>()
    val selectedNoteColor: LiveData<String> = _selectedNoteColor

    init {
        _selectedNoteColor.value = "#333333"
    }

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
        imagePath: String?,
        color: String?,
        webLink: String?
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

    fun setSelectedNoteColor(selectedNoteColor: String) {
        _selectedNoteColor.value = selectedNoteColor
    }

    private fun getNow(): String {
        val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
        val calendar = Calendar.getInstance()
        return formatter.format(calendar.time)
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