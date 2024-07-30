package com.example.firestorage.data

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storageMetadata
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
            val reference =
                storage.reference.child("download/${uri.lastPathSegment}") // aquí se crea una referencia al archivo en el storage de Firebase con el nombre del archivo

            reference.putFile(uri, createMetaData())
                .addOnSuccessListener { // aquí se sube el archivo a Firebase Storage
                    downloadImage(
                        it,
                        cancellableContinuation
                    )    // aquí se llama a la función downloadImage para obtener la url de descarga del archivo subido
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
            .addOnFailureListener {
                cancellableContinuation.resumeWithException(it)  // aquí se resuelve la suspensión con una excepción
            }
    }

    // esta funcion es para crear un objeto de tipo StorageMetadata y configurar el tipo de contenido del archivo a subir a Firebase Storage
    private fun createMetaData(): StorageMetadata {
        val metadata = storageMetadata {// aquí se crea un objeto de tipo StorageMetadata
            contentType = "image/jpeg"       // aquí se configura el tipo de contenido del archivo
            setCustomMetadata("date", "01-01-1980") // aquí se configura un metadato personalizado
            setCustomMetadata(
                "kratos",
                "test de metadatos"
            ) // aquí se configura un metadato personalizado
        }
        return metadata
    }

    // esta función suspendida se encarga de leer los metadatos de un archivo subido a Firebase Storage
    private suspend fun readMetaDataBasic() {
        val reference = storage.reference.child("download/imagen.jpg")
        val response = reference.metadata.await()
        val metainfo = response.getCustomMetadata("kratos")
        Log.i("METADATA", metainfo.orEmpty())

    }

    // esta función suspendida se encarga de leer los metadatos de un archivo subido a Firebase Storage de forma avanzada recorriendo todos los metadatos
    private suspend fun readMetaDataAdvance() {
        val reference = storage.reference.child("download/imagen.jpg")
        val response = reference.metadata.await()

        // si no sabem el nombre del metadato que queremos leer, podemos obtener todos los metadatos y recorrerlos
        response.customMetadataKeys.forEach { key ->
            response.getCustomMetadata(key)?.let { valor ->
                Log.i("METADATA", "para la key: $key el valor es: $valor")
            }
        }


    }


}