package com.example.notesapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.NoteRvSampleBinding
import com.example.notesapp.model.Notes

class NotesAdapter(private val listner1: NoteClickedInterface,
                   private val listner2: DeleteClickedInterface
): RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    private val noteList=ArrayList<Notes>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=NoteRvSampleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int =noteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem=noteList[position]
        holder.binding.noteTitle.text=currentItem.title
        holder.binding.timestamp.text=currentItem.timestamp
        holder.itemView.setOnClickListener {
            listner1.noteClicked(currentItem)
        }
        holder.binding.delete.setOnClickListener {
            listner2.deleteClicked(currentItem)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItem(notes: ArrayList<Notes>) {
        noteList.clear()
        noteList.addAll(notes)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: NoteRvSampleBinding): RecyclerView.ViewHolder(binding.root)
}

interface NoteClickedInterface{
    fun noteClicked(noteList: Notes)
}
interface DeleteClickedInterface{
    fun deleteClicked(noteList: Notes)
}
