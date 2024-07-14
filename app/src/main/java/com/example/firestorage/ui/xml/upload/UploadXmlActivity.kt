package com.example.firestorage.ui.xml.upload

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firestorage.R
import com.example.firestorage.databinding.ActivityUploadXmlBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadXmlActivity : AppCompatActivity() {

    companion object {
        // esta función estática es para crear un intent que se usará para navegar a esta actividad
        fun create(context: Context) :Intent = Intent(context, UploadXmlActivity::class.java)
    }

    private lateinit var binding: ActivityUploadXmlBinding

    private val uploadViewModel: UploadXmlViewModel by viewModels()
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

    }
}