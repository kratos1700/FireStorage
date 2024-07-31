package com.example.firestorage.ui.xml.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.firestorage.R
import com.example.firestorage.databinding.ActivityListXmlBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListXmlActivity : AppCompatActivity() {

    companion object {
        fun create(context: Context): Intent = Intent(
            context,
            ListXmlActivity::class.java
        )   // función estática para crear un intent que se usará para navegar a esta actividad
    }

    private lateinit var binding: ActivityListXmlBinding

    private val viewModel: ListXmlViewModel by viewModels() // instanciar el viewmodel
    private lateinit var galleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListXmlBinding.inflate(layoutInflater)

        enableEdgeToEdge() // habilitar el modo de pantalla completa

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->  // aplicar los insets a la vista principal
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
        viewModel.getAllImages() // obtener todas las imágenes
    }

    private fun initUI() {
        initUIState()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        galleryAdapter = GalleryAdapter() // instanciar el adaptador
        binding.rvList.apply {
            adapter = galleryAdapter // establecer el adaptador
            layoutManager = GridLayoutManager(this@ListXmlActivity, 2) // establecer el layout manager
        }


    //    binding.rvList.layoutManager = GridLayoutManager(this, 2) // establecer el layout manager
      //  binding.rvList.adapter = galleryAdapter // establecer el adaptador

    }

    private fun initUIState() {

        lifecycleScope.launch { // lanzar un nuevo scope de corutinas
            repeatOnLifecycle(Lifecycle.State.STARTED) {// repetir el ciclo de vida de la actividad
                viewModel.uiState.collect { state -> // recolectar el estado del viewmodel
                    galleryAdapter.updateImages(state.images) // actualizar las imágenes del adaptador
                    binding.pbList.isVisible = state.isLoading // mostrar el progress bar si isLoading es true


                }

            }


        }
    }
}