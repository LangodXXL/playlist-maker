package com.solyakov.playlist.data.network


data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val playlistId: Long?,
    var favorite: Boolean = false,
    val image: String?
)