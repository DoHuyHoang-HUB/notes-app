package com.example.notes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.FragmentNoteListBinding
import com.example.notes.ui.viewmodel.NoteViewModel
import com.example.notes.ui.viewmodel.NoteViewModelFactory

class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
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
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageAddNoteMain.setOnClickListener { openCreateNoteFragment() }
    }

    private fun openCreateNoteFragment() {
        findNavController().navigate(R.id.action_noteListFragment_to_createNoteFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}