package com.solyakov.playlist.domain.storage

interface PlaylistCoverStorage {
    suspend fun savePlaylistCover(uri: String): String
}