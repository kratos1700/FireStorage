package com.example.firestorage.data

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class StorageService @Inject constructor(private val storage: FirebaseStorage) {

    fun uploadSimpleImage(uri: Uri) {
        val reference = storage.reference.child(uri.lastPathSegment.orEmpty())
        reference.putFile(uri)

    }

    fun downloadBasicImage() {
        val reference = storage.reference.child("image/")
        reference.downloadUrl
        // reference.downloadUrl.await()
    }


    // esta función suspendida se encarga de subir una imagen a Firebase Storage y obtener la url de descarga de la imagen subida
    suspend fun uploadAndDownloadImage(uri: Uri): Uri {
        return suspendCancellableCoroutine<Uri> { cancellableContinuation ->
            val reference = storage.reference.child("download/${uri.lastPathSegment}") // aquí se crea una referencia al archivo en el storage de Firebase con el nombre del archivo

            reference.putFile(uri).addOnSuccessListener { // aquí se sube el archivo a Firebase Storage
                downloadImage(it, cancellableContinuation)    // aquí se llama a la función downloadImage para obtener la url de descarga del archivo subido
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it) // aquí se resuelve la suspensión con una excepción si ocurre un error
            }

        }

    }

    // esta función se encarga de obtener la url de descarga de un archivo subido a Firebase Storage
    private fun downloadImage(
        uploadTask: UploadTask.TaskSnapshot,
        cancellableContinuation: CancellableContinuation<Uri>
    ) {

        uploadTask.storage.downloadUrl // aquí se obtiene la url de descarga del archivo subido
            .addOnSuccessListener { uri -> cancellableContinuation.resume(uri) } // aquí se resuelve la suspensión con el resultado
            .addOnFailureListener { cancellableContinuation.resumeWithException(it)  // aquí se resuelve la suspensión con una excepción
        }
    }


}