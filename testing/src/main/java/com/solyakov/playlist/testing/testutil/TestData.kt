package com.solyakov.playlist.testing.testutil

import com.solyakov.playlist.domain.models.Playlist
import com.solyakov.playlist.domain.models.Track

object TestData {

    fun track(
        id: Long = 1L,
        name: String = "Test track",
        artist: String = "Test artist",
        favorite: Boolean = false,
        trackTime: String = "03:00"
    ): Track {
        return Track(
            trackId = id,
            trackName = name,
            artistName = artist,
            trackTime = trackTime,
            favorite = favorite,
            image = "https://example.com/image.jpg",
            previewUrl = "https://example.com/audio.mp3"
        )
    }



    fun playlist(
        id: Long = 1L,
        name: String = "Playlist",
        description: String = "Description",
        image: String? = null
    ): Playlist {
        return Playlist(
            playlistId = id,
            name = name,
            description = description,
            image = image
        )
    }
}