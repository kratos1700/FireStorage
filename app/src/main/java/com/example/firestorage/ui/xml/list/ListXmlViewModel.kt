package com.example.firestorage.ui.xml.list

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
class ListXmlViewModel @Inject constructor(private val storageService: StorageService) : ViewModel() {

    private var _uiState = MutableStateFlow(ListUIState(false, emptyList())) // estado inicial de la UI
    val uiState: StateFlow<ListUIState> = _uiState



    fun getAllImages() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true) // actualizar el estado de la UI a isLoading = true
            val result = withContext(Dispatchers.IO) { // cambiar al hilo IO
                storageService.getAllImages().map { // obtener todas las imágenes
                    it.toString()
                }
            }

            _uiState.value = _uiState.value.copy(isLoading = false, images = result) // actualizar el estado de la UI a isLoading = false y asignar las imágenes obtenidas al estado


        }
    }



}


data class ListUIState(
    val isLoading: Boolean,
    val images: List<String>,


    )