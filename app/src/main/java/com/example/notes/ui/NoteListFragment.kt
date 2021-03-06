package com.example.notes.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.R
import com.example.notes.databinding.FragmentNoteListBinding
import com.example.notes.ui.adapter.NotesListAdapter
import com.example.notes.ui.viewmodel.NoteViewModel
import com.example.notes.ui.viewmodel.NoteViewModelFactory

class NoteListFragment : Fragment(), SearchView.OnQueryTextListener {

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

        val adapter = NotesListAdapter { note ->
            val action = NoteListFragmentDirections
                .actionNoteListFragmentToCreateNoteFragment(note.id)
            findNavController().navigate(action)
            binding.inputSearch.text = null
        }

        binding.notesRecyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        viewModel.getAllNote("").observe(this.viewLifecycleOwner) { notes ->
            notes.let {
                adapter.submitList(it)
            }
        }

        binding.apply {
            notesRecyclerview.adapter = adapter
            imageAddNoteMain.setOnClickListener { openCreateNoteFragment() }
            inputSearch.addTextChangedListener {
                viewModel.getAllNote(inputSearch.text.toString()).observe(viewLifecycleOwner) { notes ->
                    notes.let {
                        adapter.submitList(it)
                    }
                }
            }
        }

    }

    private fun openCreateNoteFragment() {
        findNavController().navigate(R.id.action_noteListFragment_to_createNoteFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("Not yet implemented")
    }
}