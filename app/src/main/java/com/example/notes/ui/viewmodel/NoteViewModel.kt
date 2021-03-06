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
    val dateNow = getNow()

    private val _selectedNoteColor = MutableLiveData<String>()
    val selectedNoteColor: LiveData<String> = _selectedNoteColor

    private val _selectedImagePath = MutableLiveData<String?>()

    private val _webURL = MutableLiveData<String?>()
    val webURL: LiveData<String?> = _webURL

    init {
        resetNote()
    }

    fun getAllNote(searchText: String): LiveData<List<Note>> {
        return noteDao.getAllNotes("%$searchText%").asLiveData()
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
    ) {
        val note = Note(
            title = title,
            dateTime = dateTime,
            subtitle = subtitle,
            noteText = noteText,
            imagePath = _selectedImagePath.value,
            color = _selectedNoteColor.value,
            webLink = _webURL.value
        )
        insertNote(note)
    }

    fun retrieveNote(id: Int): LiveData<Note> {
        return noteDao.getNote(id).asLiveData()
    }

    fun updateNote(
        id: Int,
        title: String,
        dateTime: String,
        subtitle: String,
        noteText: String,
    ) {
        val note = Note(
            id = id,
            dateTime = dateTime,
            title = title,
            subtitle = subtitle,
            noteText = noteText,
            imagePath = _selectedImagePath.value,
            color = _selectedNoteColor.value,
            webLink = _webURL.value
        )
        updateNote(note)
    }

    private fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteNote(note)
        }
    }

    fun setSelectedNoteColor(selectedNoteColor: String) {
        _selectedNoteColor.value = selectedNoteColor
    }

    fun setSelectedImagePath(selectedImagePath: String?) {
        _selectedImagePath.value = selectedImagePath
    }

    fun setWebURL(webUrl: String?) {
        _webURL.value = webUrl
    }

    private fun getNow(): String {
        val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
        val calendar = Calendar.getInstance()
        return formatter.format(calendar.time)
    }

    fun resetNote() {
        _selectedNoteColor.value = "#333333"
        _selectedImagePath.value = null
        _webURL.value = null
    }

}

class NoteViewModelFactory(private val noteDao: NoteDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}