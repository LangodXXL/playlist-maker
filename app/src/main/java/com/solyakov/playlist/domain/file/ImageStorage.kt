package com.solyakov.playlist.domain.file

interface ImageStorage {
    suspend fun saveImageToInternalStorage(uri: String): String
}