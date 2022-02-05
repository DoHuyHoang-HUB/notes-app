package com.example.notes.ui

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.notes.databinding.LayoutDeleteNoteBinding

class DeleteAlertDialog(
    context: Context,
    private val listenerClick: () -> Unit
): AlertDialog(context) {

    private var _binding: LayoutDeleteNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = LayoutDeleteNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(0))

        binding.textDeleteNote.setOnClickListener {
            listenerClick()
            dismiss()
        }
        binding.textCancel.setOnClickListener { dismiss() }
    }
}