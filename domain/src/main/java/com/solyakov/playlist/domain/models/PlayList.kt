package com.solyakov.playlist.domain.models


data class Playlist(
    val playlistId: Long = 0,
    val name: String,
    val description: String,
    val image: String?
)