package com.example.firestorage.ui.compose.upload

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firestorage.R
import com.example.firestorage.databinding.ActivityUploadComposeBinding

class UploadComposeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadComposeBinding

    companion object{
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
    fun UploadScreen(){

        Text(text ="Upload Screen")

    }



}