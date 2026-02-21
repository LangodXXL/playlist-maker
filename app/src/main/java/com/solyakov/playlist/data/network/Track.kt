package com.solyakov.playlist.data.network

import com.solyakov.playlist.data.dto.Playlist

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val playlistId: Int?
)