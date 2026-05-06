package com.solyakov.playlist

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.solyakov.playlist.data.database.PlaylistEntity
import com.solyakov.playlist.data.database.TrackEntity
import com.solyakov.playlist.data.dto.TrackDto
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.data.playlist.Playlist

fun TrackDto.toTrackModel(): Track {
    val seconds = trackTimeMillis / 1000
    val minutes = seconds / 60
    val trackTime = "%02d".format(minutes) + ":" + "%02d".format(seconds - minutes * 60)
    return Track(
        trackId = id,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        image = artworkUrl100?.replace("100x100", "512x512") ?: "",
        previewUrl = previewUrl ?: ""
    )
}



fun TrackEntity.toTrack(): Track{
    return Track(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = this.trackTime,
        favorite = this.favorite,
        image = this.image,
        previewUrl = this.previewUrl
    )
}

fun Track.toEntity(): TrackEntity {
    return TrackEntity(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = this.trackTime,
        image = this.image,
        favorite = this.favorite,
        previewUrl = previewUrl
    )
}

fun Playlist.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        playlistId = this.playlistId,
        name = this.name,
        description = this.description,
        image = this.image
    )
}

fun PlaylistEntity.toPlaylist(): Playlist {
    return Playlist(
        playlistId = this.playlistId,
        name = this.name,
        description = this.description,
        image = this.image
    )
}

fun Track.toMediaItem(): MediaItem {
    val metadata = MediaMetadata.Builder()
        .setTitle(this.trackName)
        .setArtist(this.artistName)        .setArtworkUri(this.image.toUri())
        .build()

    return MediaItem.Builder()
        .setMediaId(this.trackId.toString())
        .setUri(this.previewUrl)
        .setMediaMetadata(metadata)
        .build()
}