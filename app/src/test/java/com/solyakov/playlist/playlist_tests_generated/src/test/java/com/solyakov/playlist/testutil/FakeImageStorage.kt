package com.solyakov.playlist.testutil

import com.solyakov.playlist.domain.file.ImageStorage

class FakeImageStorage : ImageStorage {

    var savedUri: String? = null
    var resultPath: String = "saved/image.jpg"

    override suspend fun saveImageToInternalStorage(uri: String): String {
        savedUri = uri
        return resultPath
    }
}
