package com.solyakov.playlist.data.playlist

import android.content.Context
import androidx.core.net.toUri
import com.solyakov.playlist.domain.file.ImageStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageSaver(private val context: Context): ImageStorage {

    private val imagesDir: File = context.filesDir
    override suspend fun saveImageToInternalStorage(url: String): String {
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        val file = File(imagesDir, fileName)

        withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(url.toUri())?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
        return file.absolutePath
    }

    fun isInternal(url: String): Boolean {
        return url.startsWith(imagesDir.absolutePath)
    }

    suspend fun deleteImageFromInternalStorage(url: String) {
        withContext(Dispatchers.IO) {
            val file = File(url)
            if (isInternal(file.absolutePath) && file.exists()) {
                file.delete()
            }
        }
    }
}