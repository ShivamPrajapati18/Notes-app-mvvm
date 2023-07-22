package com.example.notesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.model.Notes
import com.example.notesapp.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository: NoteRepository): ViewModel() {

    fun getNote():LiveData<List<Notes>>{
        return noteRepository.allNote
    }

    fun insertNote(notes: Notes){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.insertNote(notes)
        }
    }
    fun updateNote(notes: Notes){
        viewModelScope.launch(Dispatchers.IO){
            noteRepository.updateNote(notes)
        }
    }
    fun deleteNote(notes: Notes){
        viewModelScope.launch (Dispatchers.IO){
            noteRepository.deleteNote(notes)
        }
    }
}