package com.example.firestorage.ui.xml.upload

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firestorage.data.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UploadXmlViewModel @Inject constructor(private val storageService: StorageService) :
    ViewModel() {

    private var _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun uploadSimpleImage(uri: Uri) {
        storageService.uploadSimpleImage(uri)

    }


    fun uploadAndGetImage(uri: Uri, onSuccesDownload: (Uri) -> (Unit)) {

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result =
                    withContext(Dispatchers.IO) { // aquí se cambia el contexto de ejecución a IO para realizar operaciones de red o de disco de forma segura

                        storageService.uploadAndDownloadImage(uri)  // aquí se llama a la función uploadAndDownloadImage para subir y obtener la url de descarga del archivo subido
                    }
                onSuccesDownload(result) // aquí se llama a la función de éxito con el resultado obtenido
            } catch (e: Exception) {
                Log.i("Error", e.message.orEmpty())
            }
            _isLoading.value = false
        }


    }


}


