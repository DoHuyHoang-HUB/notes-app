package com.example.notes.ui.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.databinding.ItemContainerNoteBinding
import com.example.notes.entities.Note

class NotesListAdapter(

): ListAdapter<Note, NotesListAdapter.NotesViewHolder>(DiffCallback) {

    companion object DiffCallback: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NotesViewHolder(ItemContainerNoteBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    class NotesViewHolder(private val binding: ItemContainerNoteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.textTitle.text = note.title
            binding.textDateTime.text = note.dateTime
            binding.textSubtitle.text = note.subtitle
            if (note.color != null) {
                (binding.layoutNote.background as GradientDrawable).setColor(Color.parseColor(note.color))
            } else {
                (binding.layoutNote.background as GradientDrawable).setColor(Color.parseColor("#333333"))
            }
        }
    }

}