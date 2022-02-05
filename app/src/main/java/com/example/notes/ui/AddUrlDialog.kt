package com.example.notes.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.notes.R
import com.example.notes.databinding.LayoutAddUrlBinding
import com.example.notes.ui.viewmodel.NoteViewModel

class AddUrlDialog(
    context: Context,
    private val listener: EnterURLListener
): AlertDialog(context) {

    interface EnterURLListener {
        fun enterURL(binding: LayoutAddUrlBinding, dialog: AlertDialog)
    }

    private var _binding: LayoutAddUrlBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = LayoutAddUrlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(0))

        binding.textAdd.setOnClickListener { listener.enterURL(binding, this) }
        binding.textCancel.setOnClickListener { dismiss() }
    }


}