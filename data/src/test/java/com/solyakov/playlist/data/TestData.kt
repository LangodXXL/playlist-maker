package com.solyakov.playlist.data

import com.solyakov.playlist.data.dto.TrackDto
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

    fun trackDto(
        id: Long = 1L,
        name: String = "Test track",
        artist: String = "Test artist",
        trackTimeMillis: Long = 180_000L,
        artworkUrl100: String? = "https://example.com/100x100.jpg",
        previewUrl: String? = "https://example.com/audio.mp3"
    ): TrackDto {
        return TrackDto(
            id = id,
            trackName = name,
            artistName = artist,
            trackTimeMillis = trackTimeMillis,
            artworkUrl100 = artworkUrl100,
            previewUrl = previewUrl
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