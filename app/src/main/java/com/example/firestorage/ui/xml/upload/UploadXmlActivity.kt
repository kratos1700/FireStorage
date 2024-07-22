package com.example.firestorage.ui.xml.upload

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firestorage.R
import com.example.firestorage.databinding.ActivityUploadXmlBinding
import com.example.firestorage.databinding.DialogImageSelectorBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@AndroidEntryPoint
class UploadXmlActivity : AppCompatActivity() {

    companion object {
        // esta función estática es para crear un intent que se usará para navegar a esta actividad
        fun create(context: Context): Intent = Intent(context, UploadXmlActivity::class.java)
    }

    private lateinit var binding: ActivityUploadXmlBinding

    private val uploadViewModel: UploadXmlViewModel by viewModels()

    private lateinit var uri: Uri  // uri de la foto tomada con la cámara

    // este es el lanzador de la cámara que se encarga de abrir la cámara y tomar la foto y recibir el resultado de la foto tomada
    private var intentCameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            // aquí se recibe el resultado de la cámara
            if (it && uri.path?.isNotEmpty() == true) {
                uploadViewModel.uploadSimpleImage(uri)
            }

        }

    private var intentGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                uploadViewModel.uploadSimpleImage(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUploadXmlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // esto hace que el contenido no se superponga con la barra de estado y de navegación en dispositivos con notch o pantalla curva
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initUi()

    }

    private fun initUi() {
        initListeners()
    }

    private fun initListeners() {

        binding.fabImage.setOnClickListener {
            // takePicture()
            showImageDialog()

        }

    }

    private fun showImageDialog() {
        // aquí se infla el layout del dialogo que se mostrará para seleccionar la imagen de la galería o tomar una foto con la cámara
        val dialogBinding = DialogImageSelectorBinding.inflate(layoutInflater)

        // aquí se configuran los listeners de los botones del dialogo
        val dialog = AlertDialog.Builder(this).apply {
            setView(dialogBinding.root)
        }.create()

        dialogBinding.btnTakePhoto.setOnClickListener {
            takePicture()
            dialog.dismiss() // cerrar el dialogo
        }
        dialogBinding.btnGallery.setOnClickListener {
            getImageFromGallery()
            dialog.dismiss() // cerrar el dialogo
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent) // quitar el fondo del dialogo para que no se vea el borde

        // mostrar el dialogo
        dialog.show()
    }

    private fun getImageFromGallery() {
        // aquí se lanza el intent para seleccionar una imagen de la galería y se recibe el resultado en el callback de la variable intentGalleryLauncher
        intentGalleryLauncher.launch("image/*")
    }


    private fun generateUri() {
        uri = FileProvider.getUriForFile(
            Objects.requireNonNull(this),
            "com.example.firestorage.provider",
            createFile()
        )
    }

    private fun takePicture() {
        generateUri()
        intentCameraLauncher.launch(uri)
    }


    @SuppressLint("SimpleDateFormat")
    private fun createFile(): File {

        val name: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + "image"

        return File.createTempFile(name, ".jpg", externalCacheDir)
    }
}