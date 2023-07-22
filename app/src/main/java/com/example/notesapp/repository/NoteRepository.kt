package com.example.notesapp.repository

import androidx.lifecycle.LiveData
import com.example.notesapp.model.Notes
import com.example.notesapp.dao.NoteDao

class NoteRepository(private val noteDao: NoteDao) {
    val allNote:LiveData<List<Notes>>
        get() {
            return noteDao.getNote()
        }

    suspend fun insertNote(notes: Notes){
        noteDao.insert(notes)
    }
    suspend fun updateNote(notes: Notes){
        noteDao.update(notes)
    }
    suspend fun deleteNote(notes: Notes){
        noteDao.delete(notes)
    }
}