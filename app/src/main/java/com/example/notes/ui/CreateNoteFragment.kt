package com.example.notes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.FragmentCreateNoteBinding
import com.example.notes.ui.viewmodel.NoteViewModel
import com.example.notes.ui.viewmodel.NoteViewModelFactory

class CreateNoteFragment : Fragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as BaseApplication).database.noteDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textDateTime.text = viewModel.dateNow
        binding.layoutBack.setOnClickListener { onBackPressed() }
    }

    private fun onBackPressed() {
        activity?.onBackPressed()
    }

    private fun addNote() {
        if(isValidEntry()) {
            viewModel.addNote(
                binding.inputNoteTitle.text.toString(),
                viewModel.dateNow,
                binding.inputNoteSubtitle.text.toString(),
                binding.inputNote.text.toString(),
                null,
                null,
                null
            )
        }
    }

    private fun isValidEntry(): Boolean {
        if (binding.inputNoteTitle.text.toString().isBlank()) {
            Toast.makeText(context, "Note title can't be blank!", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.inputNoteSubtitle.text.toString().isBlank() && binding.inputNote.text.toString().isBlank()) {
            Toast.makeText(context, "Note can't be blank!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}