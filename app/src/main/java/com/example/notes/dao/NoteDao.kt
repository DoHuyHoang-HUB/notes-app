package com.example.notes.dao

import androidx.room.*
import com.example.notes.entities.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("select * from notes order by id desc")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Query("select * from notes where id = :id")
    fun getNote(id: Int): Flow<Note>

}