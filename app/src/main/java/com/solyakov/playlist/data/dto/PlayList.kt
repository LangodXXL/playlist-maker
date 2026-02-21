package com.solyakov.playlist.data.dto

import com.solyakov.playlist.data.network.Track

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    var tracks: List<Track>
)