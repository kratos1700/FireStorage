package com.example.firestorage.ui.compose.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firestorage.R
import com.example.firestorage.databinding.ActivityUploadComposeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@AndroidEntryPoint
class UploadComposeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadComposeBinding

    companion object {
        // esta función estática es para crear un intent que se usará para navegar a esta actividad
        fun create(context: Context) = Intent(context, UploadComposeActivity::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUploadComposeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // aqui se inicializa la vista de composición
        binding.composeView.setContent {
            UploadScreen()

        }

    }


    @Composable
    fun UploadScreen() {

        val uploadViewModel :UploadComposeViewModel by viewModels()

        var uri: Uri? by remember { mutableStateOf(null) } // uri de la foto tomada con la cámara

        var intentCameraLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
                // aquí se recibe el resultado de la cámara
                if (it && uri?.path?.isNotEmpty() == true) {
                          uploadViewModel.uploadSimpleImage(uri!!)
                }

            }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            FloatingActionButton(onClick = {
                uri = generateUri() // se genera la uri para la foto
                intentCameraLauncher.launch(uri!!) // se lanza la cámara


            }, containerColor = colorResource(id = R.color.green)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = "icono camara",
                    tint = Color.White
                )
            }

        }


    }


// Esta funcion es diferenta a la de XML, ya que en compose no se puede usar el metodo createFile() directamente, por lo que se debe de hacer de otra manera
    // en este caso se crea un archivo temporal y se obtiene su URI para poder usarlo en la cámara y subirlo a Firebase Storage después de tomar la foto

    private fun generateUri(): Uri {
        return FileProvider.getUriForFile(
            Objects.requireNonNull(this),
            "com.example.firestorage.provider",
            createFile()
        )
    }

    private fun createFile(): File {

        val name: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + "image"

        return File.createTempFile(name, ".jpg", externalCacheDir)
    }
}

