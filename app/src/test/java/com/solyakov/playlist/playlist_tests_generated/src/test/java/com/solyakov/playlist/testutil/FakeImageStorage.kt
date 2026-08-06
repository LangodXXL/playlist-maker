package com.solyakov.playlist.testutil

import com.solyakov.playlist.domain.storage.PlaylistCoverStorage

class FakeImageStorage : PlaylistCoverStorage {

    var savedUri: String? = null
    var resultPath: String = "saved/image.jpg"

    override suspend fun savePlaylistCover(uri: String): String {
        savedUri = uri
        return resultPath
    }
}
