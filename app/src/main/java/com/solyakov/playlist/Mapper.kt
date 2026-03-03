package com.solyakov.playlist

import com.solyakov.playlist.data.dto.TrackDto
import com.solyakov.playlist.data.network.Track

fun TrackDto.toTrackModel(): Track {
    val seconds = trackTimeMillis / 1000
    val minutes = seconds / 60
    val trackTime = "%02d".format(minutes) + ":" + "%02d".format(seconds - minutes * 60)
    return Track(
        trackId = id,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        playlistId = null,
        image = artworkUrl100?.replace("100x100", "512x512")
    )
}

