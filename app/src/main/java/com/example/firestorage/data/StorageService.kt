package com.example.firestorage.data

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class StorageService @Inject constructor(private val storage: FirebaseStorage)  {
    fun uploadSimpleImage(uri: Uri) {
        val reference = storage.reference.child(uri.lastPathSegment.orEmpty())

        reference.putFile(uri)

    }



}