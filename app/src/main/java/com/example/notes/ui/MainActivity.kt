package com.example.notes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notes.databinding.ActivityMainBinding

private const val REQUEST_CODE_ADD_NOTE = 1

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageAddNoteMain = binding.imageAddNoteMain
        imageAddNoteMain.setOnClickListener {
            startActivityForResult(Intent(applicationContext, CreateNoteActivity::class.java), REQUEST_CODE_ADD_NOTE)
        }
    }
}