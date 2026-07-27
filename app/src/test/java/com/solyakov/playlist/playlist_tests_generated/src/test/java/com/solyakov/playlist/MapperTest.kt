package com.solyakov.playlist

import androidx.media3.common.MediaItem
import com.solyakov.playlist.data.database.PlaylistEntity
import com.solyakov.playlist.testutil.TestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MapperTest {

    @Test
    fun `TrackDto toTrackModel formats time and increases artwork resolution`() {
        val dto = TestData.trackDto(
            id = 10L,
            name = "Song",
            artist = "Artist",
            trackTimeMillis = 185_000L,
            artworkUrl100 = "https://example.com/100x100.jpg",
            previewUrl = "https://example.com/audio.mp3"
        )

        val track = dto.toTrackModel()

        assertEquals(10L, track.trackId)
        assertEquals("Song", track.trackName)
        assertEquals("Artist", track.artistName)
        assertEquals("03:05", track.trackTime)
        assertEquals("https://example.com/512x512.jpg", track.image)
        assertEquals("https://example.com/audio.mp3", track.previewUrl)
    }

    @Test
    fun `Track toEntity and TrackEntity toTrack preserve fields`() {
        val track = TestData.track(id = 10L, name = "Song", artist = "Artist", favorite = true)

        val entity = track.toEntity()
        val mappedTrack = entity.toTrack()

        assertEquals(track, mappedTrack)
    }

    @Test
    fun `PlaylistEntity toPlaylist maps fields`() {
        val entity = PlaylistEntity(
            playlistId = 7L,
            name = "Road trip",
            description = "Music",
            image = "cover.jpg"
        )

        val playlist = entity.toPlaylist()

        assertEquals(7L, playlist.playlistId)
        assertEquals("Road trip", playlist.name)
        assertEquals("Music", playlist.description)
        assertEquals("cover.jpg", playlist.image)
    }

    @Test
    fun `Track toMediaItem and MediaItem toTrackOrNull roundtrip important fields`() {
        val track = TestData.track(
            id = 100L,
            name = "Song",
            artist = "Artist",
            favorite = true,
            trackTime = "02:37"
        )

        val mediaItem = track.toMediaItem()
        val mappedTrack = mediaItem.toTrackOrNull()

        assertEquals("100", mediaItem.mediaId)
        assertEquals(track.previewUrl, mediaItem.localConfiguration?.uri.toString())
        assertEquals(track.trackName, mediaItem.mediaMetadata.title.toString())
        assertEquals(track.artistName, mediaItem.mediaMetadata.artist.toString())
        assertEquals(track.image, mediaItem.mediaMetadata.artworkUri.toString())
        assertEquals(track, mappedTrack)
    }

    @Test
    fun `MediaItem toTrackOrNull returns null for non numeric mediaId`() {
        val mediaItem = MediaItem.Builder()
            .setMediaId("not_number")
            .setUri("https://example.com/audio.mp3")
            .build()

        assertNull(mediaItem.toTrackOrNull())
    }
}
