package com.example.notes.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.FragmentCreateNoteBinding
import com.example.notes.ui.viewmodel.NoteViewModel
import com.example.notes.ui.viewmodel.NoteViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CreateNoteFragment : Fragment() {

    companion object {
        const val REQUEST_CODE_STORAGE_PERMISSION = 1
        const val REQUEST_CODE_SELECT_IMAGE = 2
    }

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

        binding.apply {
            miscellaneous.viewModel = viewModel
            miscellaneous.createNoteFragment = this@CreateNoteFragment
            textDateTime.text = viewModel.dateNow
            (viewSubtitleIndicator.background as GradientDrawable).setColor(Color.parseColor(viewModel.selectedNoteColor.value))
            layoutBack.setOnClickListener { onBackPressed() }
            imageSave.setOnClickListener { saveNote() }
            miscellaneous.textMiscellaneous.setOnClickListener { bottomSheetState(BottomSheetBehavior.from(miscellaneous.layoutMiscellaneous)) }
        }
    }

    fun setSubtitleIndicatorColor(selectedNoteColor: String) {
        viewModel.setSelectedNoteColor(selectedNoteColor)
        (binding.viewSubtitleIndicator.background as GradientDrawable).setColor(Color.parseColor(viewModel.selectedNoteColor.value))
    }

    private fun onBackPressed() {
        activity?.onBackPressed()
        viewModel.resetNote()
    }

    private fun saveNote() {
        if(isValidEntry()) {
            viewModel.addNote(
                binding.inputNoteTitle.text.toString(),
                viewModel.dateNow,
                binding.inputNoteSubtitle.text.toString(),
                binding.inputNote.text.toString(),
                null
            )
            Toast.makeText(context, "Save successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_createNoteFragment_to_noteListFragment)
            viewModel.resetNote()
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

    private fun bottomSheetState(bottomSheetBehavior: BottomSheetBehavior<LinearLayout>) {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    fun requestPermissionsStorage() {
        BottomSheetBehavior.from(binding.miscellaneous.layoutMiscellaneous).state = BottomSheetBehavior.STATE_COLLAPSED
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_STORAGE_PERMISSION
            )
        } else {
            selectImage()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(activity?.packageManager!!) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                selectImage()
            } else {
                Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImageUri = data.data
                if (selectedImageUri != null) {
                    binding.apply {
                        imageNote.setImageURI(selectedImageUri)
                        imageNote.visibility = View.VISIBLE
                    }
                    viewModel.setSelectedImagePath(getPathFromUri(selectedImageUri))
                }
            }
        }
    }

    private fun getPathFromUri(contentUri: Uri): String {
        var filePath = ""
        val cursor = activity?.contentResolver?.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path.toString()
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }
}