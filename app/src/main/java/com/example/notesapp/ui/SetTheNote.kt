package com.example.notesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.databinding.ActivitySetTheNoteBinding
import com.example.notesapp.model.Notes
import com.example.notesapp.repository.NoteRepository
import com.example.notesapp.viewmodel.NoteViewModel
import com.example.notesapp.viewmodel.NoteViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date

class SetTheNote : AppCompatActivity() {
    private lateinit var binding: ActivitySetTheNoteBinding
    private lateinit var isEdit: String
    private var id:Int=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySetTheNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val noteDao= NoteDatabase.getInstance(applicationContext).getNoteDao()
        val noteRepository = NoteRepository(noteDao)
        val noteViewModel=ViewModelProvider(this, NoteViewModelFactory(noteRepository)).get(
            NoteViewModel::class.java)
        /* checking if note exists or note and changing the text of the note*/
        isEdit= intent.getStringExtra("isEdit").toString()
        val title=intent.getStringExtra("title")
        val description=intent.getStringExtra("description")
        id=intent.getIntExtra("id",-1)
        if (isEdit.equals("true")){
            binding.titletext.setText(title)
            binding.description.setText(description)
            binding.button.text="Update Notes"
        }else{
            binding.button.text="Save Note"
        }

        binding.button.setOnClickListener {
            /* getting the data which user put on the edit text*/
            val noteTitle=binding.titletext.text.toString()
            val desc=binding.description.text.toString()
            val dateFormate=SimpleDateFormat("dd MMM yyyy hh:mm a")
            val time=dateFormate.format(Date())
            if (isEdit.equals("true")){
                /* checking the is note title and description is not empty*/
                if (noteTitle.isNotEmpty()&&desc.isNotEmpty()) {
                    val updateNode = Notes(noteTitle, desc, time)
                    updateNode.id = id
                    noteViewModel.updateNote(updateNode)
                    Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    this.finish()
                }
                else
                {
                    Toast.makeText(this, "Enter Some Text", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                /* if note is not exits in room and checking the note tile is and description empty or not*/
                if (noteTitle.isNotEmpty()&&desc.isNotEmpty()){
                    val note = Notes(
                        title = noteTitle,
                        description = desc,
                        timestamp = time
                    )
                    noteViewModel.insertNote(note)
                    Toast.makeText(this, "Insert Successfully", Toast.LENGTH_SHORT).show()
                    this.finish()
                }
                else{
                    Toast.makeText(this, "Enter Some Text", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}