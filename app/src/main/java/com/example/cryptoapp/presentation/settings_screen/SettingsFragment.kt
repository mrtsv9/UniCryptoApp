package com.example.cryptoapp.presentation.settings_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentSettingsBinding
import com.example.cryptoapp.presentation.base.BaseFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private lateinit var toolbar: Toolbar
    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2
    private val viewModel: SettingsViewModel by viewModel()
    private var URI: Uri? = null
    private var fileName: String? = null
    private var photoBitmap: Bitmap? = null

    val PREFS_NAME = "MY_PREFS"

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate

    @SuppressLint("ResourceAsColor")
    override fun setup() {

        val sharedPref: SharedPreferences =
            requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()

        toolbar = binding.settingToolbar
        toolbar.title = "Settings"

        val typedValue = TypedValue()
        val theme = context?.theme
        theme?.resolveAttribute(R.attr.colorOnBackground, typedValue, true)
        @ColorInt val colorWhite = typedValue.data
        toolbar.setTitleTextColor(colorWhite)

        binding.etFirstName.setText(sharedPref.getString("name", null))
        binding.etLastName.setText(sharedPref.getString("last_name", null))
        binding.etDateOfBirth.setText(sharedPref.getString("date", null))

        lifecycleScope.launch {

            val sharedURI = sharedPref.getString("uri", null)

            val photoList = loadPhotosFromInternalStorage()
            if (photoList.isNotEmpty()) {
                binding.ivPersonPhoto.setImageBitmap(photoList[0].bmp)
            } else if (!sharedURI.isNullOrEmpty()) {
                binding.ivPersonPhoto.load(sharedURI) {
                    crossfade(true)
                    crossfade(1000)
                }
            }

        }

        binding.ivTakePhoto.setOnClickListener {

            val pictureDialog = context?.let { context -> AlertDialog.Builder(context) }
            pictureDialog?.setTitle("Select Action")
            val pictureDialogItem = arrayOf(
                "Select photo from Gallery",
                "Capture photo from Camera"
            )
            pictureDialog?.setItems(pictureDialogItem) { dialog, which ->

                when (which) {
                    0 -> galleryCheckPermission()
                    1 -> cameraCheckPermission()
                }
            }

            pictureDialog?.show()
        }

        binding.btnSelectDate.setOnClickListener {
            val datePickerFragment = DatePickerFragment()

            val supportFragmentManager = requireActivity().supportFragmentManager

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    binding.etDateOfBirth.setText(date)
                }
            }

            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }

        binding.ivSave.setOnClickListener {
            if (validateTextFields(binding.etFirstName) && validateTextFields(binding.etLastName)) {

                editor.putString("name", binding.etFirstName.text.toString())
                editor.putString("last_name", binding.etLastName.text.toString())
                editor.putString("date", binding.etDateOfBirth.text.toString())
                if (URI != null) {
                    editor.putString("uri", URI.toString())
                } else {
                    editor.putString("filename", fileName)
                }
                editor.commit()

                if (photoBitmap != null) {
                    savePhotoToInternalStorage(fileName!!, photoBitmap!!)
                }

                makeText(binding.root, "Successfully added!", null)
            } else makeText(binding.root, "Error!", null)

        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

    }

    private fun validateTextFields(et: TextInputEditText): Boolean {
        if (et.text?.length!! < 1 || et.text?.length!! > 20) {
            return false
        }
        return true
    }

    private fun gallery() {
        if (fileName != null) deletePhotoFromInternalStorage(fileName)
        deletePhotoFromInternalStorage(fileName)
        deletePhotoFromInternalStorage(fileName)
        photoBitmap = null
        fileName = null

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun camera() {
        if (fileName != null) deletePhotoFromInternalStorage(fileName)
        photoBitmap = null
        URI = null

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun cameraCheckPermission() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            camera()
        } else {
            showRotationalDialogForPermission()
        }

    }

    private fun galleryCheckPermission() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            gallery()
        } else {
            showRotationalDialogForPermission()
        }

    }

    private fun showRotationalDialogForPermission() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(
                    "It looks like you have turned off permissions"
                            + "required for this feature. It can be enable under App settings!!!"
                )

                .setPositiveButton("Go TO SETTINGS") { _, _ ->

                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts(
                            "package",
                            (activity as? MainActivity)?.packageName,
                            null
                        )
                        intent.data = uri
                        startActivity(intent)

                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }

                .setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val sharedPref: SharedPreferences =
            requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {

                CAMERA_REQUEST_CODE -> {
                    editor.putString("uri", null)
                    val bitmap = data?.extras?.get("data") as Bitmap

                    if (fileName == null) {
                        fileName = UUID.randomUUID().toString()
                        photoBitmap = bitmap
                    } else if (fileName != null) {
                        deletePhotoFromInternalStorage(fileName)
                        fileName = UUID.randomUUID().toString()
                        photoBitmap = bitmap
                    }

                    lifecycleScope.launch {
                        val photoList = loadPhotosFromInternalStorage()
                        if (photoList.isNotEmpty()) {
                            binding.ivPersonPhoto.setImageBitmap(photoList[0].bmp)
                        }

                    }
                    // using coroutine image loader (coil)
                    binding.ivPersonPhoto.load(bitmap) {
                        crossfade(true)
                        crossfade(1000)
                    }
                }

                GALLERY_REQUEST_CODE -> {
                    deletePhotoFromInternalStorage(sharedPref.getString("filename", null))
                    deletePhotoFromInternalStorage(fileName)

                    if (fileName != null) deletePhotoFromInternalStorage(fileName)
                    URI = data?.data

                    binding.ivPersonPhoto.load(data?.data) {
                        crossfade(true)
                        crossfade(1000)
                    }

                }
            }

        }

    }

    private fun savePhotoToInternalStorage(filename: String, bmp: Bitmap): Boolean {

        lifecycleScope.launch {
            loadPhotosFromInternalStorage().forEach {
                (activity as? MainActivity)?.deleteFile(it.name)
            }
        }

        return try {
            (activity as? MainActivity)?.openFileOutput(
                "$filename.jpg",
                AppCompatActivity.MODE_PRIVATE
            ).use { stream ->
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("Couldn't save bitmap.")
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private suspend fun loadPhotosFromInternalStorage(): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = (activity as? MainActivity)?.filesDir?.listFiles()
            files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            } ?: listOf()
        }
    }

    private fun deletePhotoFromInternalStorage(filename: String?): Boolean? {
        return try {
            (activity as? MainActivity)?.deleteFile("$filename.jpg")
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}