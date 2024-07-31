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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.AsyncImage
import com.example.firestorage.R
import com.example.firestorage.databinding.ActivityUploadComposeBinding
import com.example.firestorage.ui.compose.list.ListComposeActivity
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


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UploadScreen() {

        val uploadViewModel: UploadComposeViewModel by viewModels()

        var uri: Uri? by remember { mutableStateOf(null) } // uri de la foto tomada con la cámara

        var showImageDialog: Boolean by remember { mutableStateOf(false) } // variable para mostrar el dialogo de selección de imagen

        var resultUri: Uri? by remember { mutableStateOf(null) } // uri de la imagen subida a Firebase Storage  y descargada de nuevo para mostrarla en la vista de composición

        val loading: Boolean by uploadViewModel.isLoading.collectAsState() // variable para mostrar un indicador de carga en la vista de composición
        var userTitle: String by remember { mutableStateOf("") } // variable para el título de la imagen subida

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        // este es el lanzador de la cámara que se encarga de abrir la cámara y tomar la foto y recibir el resultado de la foto tomada
        val intentCameraLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
                // aquí se recibe el resultado de la cámara
                if (it && uri?.path?.isNotEmpty() == true) {
                    uploadViewModel.uploadAndGetImage(uri!!) { newUri ->
                        userTitle = ""
                        focusManager.clearFocus()
                        resultUri = newUri
                    } // aquí se sube la imagen a Firebase Storage y se obtiene la url de descarga de la imagen subida
                }

            }

        val intentGalleryLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
                if (it?.path?.isNotEmpty() == true) {
                    uploadViewModel.uploadAndGetImage(it) { newUri ->
                        resultUri = newUri
                    } // aquí se sube la imagen a Firebase Storage y se obtiene la url de descarga de la imagen subida
                }
            }

        if (showImageDialog) {
            Dialog(onDismissRequest = { showImageDialog = false }) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        // Botón para seleccionar una imagen de la camara
                        OutlinedButton(
                            onClick = {
                                uri = generateUri(userTitle) // se genera la uri para la foto
                                intentCameraLauncher.launch(uri!!) // se lanza la cámara
                                showImageDialog = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .align(Alignment.CenterHorizontally),
                            border = BorderStroke(2.dp, colorResource(id = R.color.green))
                        ) {

                            Text(text = "Camera", color = colorResource(id = R.color.green))

                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botón para seleccionar una imagen de la galería
                        OutlinedButton(
                            onClick = {
                                intentGalleryLauncher.launch("image/*") // se lanza la galería
                                showImageDialog = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .align(Alignment.CenterHorizontally),
                            border = BorderStroke(2.dp, colorResource(id = R.color.green))
                        ) {

                            Text(text = "Gallery", color = colorResource(id = R.color.green))

                        }
                    }

                }

            }
        }

        Column(Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(36.dp))
            Card(
                elevation = CardDefaults.cardElevation(36.dp),
                shape = RoundedCornerShape(12),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 36.dp)
            ) {

                if (resultUri != null) {

                    AsyncImage(
                        model = resultUri,
                        contentDescription = "imagen de subida del user",
                        contentScale = ContentScale.Crop
                    )
                }

                if (loading) { // si hay una carga en progreso se muestra un indicador de carga en la vista de composición
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            Modifier.size(50.dp),
                            color = colorResource(id = R.color.green)
                        )
                        Text(text = "Loading...", color = Color.Black)
                    }
                }
                if (!loading && resultUri == null) { // si no hay imagen subida se muestra un texto en la vista de composición
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_upload_image),
                            contentDescription = "placeholder de imagen",
                            modifier = Modifier.size(100.dp),
                            tint = colorResource(id = R.color.green)
                        )

                    }
                }


            }
            Spacer(modifier = Modifier.weight(1f))

            TextField(
                value = userTitle,
                onValueChange = { userTitle = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(36.dp)
                    .border(2.dp, color = colorResource(id = R.color.green), RoundedCornerShape(22))
                    .focusRequester(focusRequester),

                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    ),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )

            )

            Spacer(modifier = Modifier.weight(1f))

            val context = LocalContext.current

            OutlinedButton(
                onClick = { startActivity(ListComposeActivity.create(context = context)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(36.dp)
                 .align(Alignment.CenterHorizontally),
                border = BorderStroke(2.dp, colorResource(id = R.color.green)), shape = RoundedCornerShape(42)
            ) {

                Text("Navigate to Gallery", color = colorResource(id = R.color.green))

            }

            Spacer(modifier = Modifier.weight(1f))

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                FloatingActionButton(onClick = {
                    showImageDialog = true  // se muestra el dialogo de selección de imagen

                }, containerColor = colorResource(id = R.color.green)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "icono camara",
                        tint = Color.White
                    )
                }

            }


        }

          //   Spacer(modifier = Modifier.weight(1f))




    }


// Esta funcion es diferenta a la de XML, ya que en compose no se puede usar el metodo createFile() directamente, por lo que se debe de hacer de otra manera
    // en este caso se crea un archivo temporal y se obtiene su URI para poder usarlo en la cámara y subirlo a Firebase Storage después de tomar la foto

    private fun generateUri(userTitle: String): Uri {
        return FileProvider.getUriForFile(
            Objects.requireNonNull(this),
            "com.example.firestorage.provider",
            createFile(userTitle)
        )
    }

    private fun createFile(userTitle: String): File {

        val name: String = userTitle.ifEmpty {

            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + "image"
        }



        return File.createTempFile(name, ".jpg", externalCacheDir)
    }
}

