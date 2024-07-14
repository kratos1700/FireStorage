package com.example.firestorage.ui.xml.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.firestorage.data.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadXmlViewModel @Inject constructor(private val storageService: StorageService): ViewModel(){
    fun uploadSimpleImage(uri: Uri) {
        storageService.uploadSimpleImage(uri)

    }


}