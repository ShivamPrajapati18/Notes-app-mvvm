package com.example.notesapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.R
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
    lateinit var noteList: ArrayList<Notes>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteDao= NoteDatabase.getInstance(applicationContext).getNoteDao()
        noteRepository= NoteRepository(noteDao)
        noteViewModel= ViewModelProvider(this, NoteViewModelFactory(noteRepository))[NoteViewModel::class.java]
        noteList= ArrayList()
        setSupportActionBar(binding.toolbar)
        /*Recycler View Implementation*/
        binding.noteRv.layoutManager=LinearLayoutManager(this)
        notesAdapter= NotesAdapter(this,this)
        binding.noteRv.adapter=notesAdapter
        /*getting list of notes from viewModel*/
        noteViewModel.getNote().observe(this) {
            it?.let {
                notesAdapter.updateItem(it as ArrayList<Notes>)
                noteList=it
            }
        }
        /*handling floating action button*/
        binding.floatingActionButton.setOnClickListener {
            val intent=Intent(this, SetTheNote::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        val menuItem= menu?.findItem(R.id.app_bar_search)
        val searchView= menuItem?.actionView as SearchView
        searchView.queryHint="Search Note"
        searchView.setOnQueryTextListener(object :OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    filterData(it)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterData(it)
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun filterData(text: String) {
        val newNoteList= ArrayList(noteList.filter {
                it.title.contains(text, true)
            })
        notesAdapter.filterData(newNoteList)
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