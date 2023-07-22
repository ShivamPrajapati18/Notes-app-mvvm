package com.example.notesapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.adapter.DeleteClickedInterface
import com.example.notesapp.adapter.NoteClickedInterface
import com.example.notesapp.adapter.NotesAdapter
import com.example.notesapp.dao.NoteDao
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.model.Notes
import com.example.notesapp.repository.NoteRepository
import com.example.notesapp.viewmodel.NoteViewModel
import com.example.notesapp.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity(), DeleteClickedInterface, NoteClickedInterface {
    lateinit var binding: ActivityMainBinding
    lateinit var noteViewModel: NoteViewModel
    lateinit var noteRepository: NoteRepository
    lateinit var noteDao: NoteDao
    lateinit var notesAdapter: NotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteDao= NoteDatabase.getInstance(applicationContext).getNoteDao()
        noteRepository= NoteRepository(noteDao)
        noteViewModel=ViewModelProvider(this, NoteViewModelFactory(noteRepository)).get(
            NoteViewModel::class.java)
        /*Recycler View Implementation*/
        binding.noteRv.layoutManager=LinearLayoutManager(this)
        notesAdapter= NotesAdapter(this,this)
        binding.noteRv.adapter=notesAdapter
        /*getting list of notes from viewModel*/
        noteViewModel.getNote().observe(this) {
            it?.let {
                notesAdapter.updateItem(it as ArrayList<Notes>)
            }
        }
        /*handling floating action button*/
        binding.floatingActionButton.setOnClickListener {
            val intent=Intent(this, SetTheNote::class.java)
            startActivity(intent)
        }

    }
    /*deleting the node*/
    override fun deleteClicked(noteList: Notes) {
        noteViewModel.deleteNote(noteList)
    }
    /*when note clicked*/
    override fun noteClicked(noteList: Notes) {
        /*passing the data of existing of data through intent*/
        val intent=Intent(this, SetTheNote::class.java)
        intent.putExtra("isEdit","true")
        intent.putExtra("title",noteList.title)
        intent.putExtra("description",noteList.description)
        intent.putExtra("time",noteList.timestamp)
        intent.putExtra("id",noteList.id)
        startActivity(intent)
    }
}