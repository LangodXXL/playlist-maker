package com.solyakov.playlist.data.playlist

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.solyakov.playlist.data.database.AppDatabase
import com.solyakov.playlist.data.network.TracksRepositoryImpl
import com.solyakov.playlist.testutil.FakeNetworkClient
import com.solyakov.playlist.testutil.TestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PlaylistsRepositoryImplTest {

    private lateinit var database: AppDatabase
    private lateinit var playlistsRepository: PlaylistsRepositoryImpl
    private lateinit var tracksRepository: TracksRepositoryImpl

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        playlistsRepository = PlaylistsRepositoryImpl(database)
        tracksRepository = TracksRepositoryImpl(
            networkClient = FakeNetworkClient(),
            database = database
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `addPlaylist saves playlist`() = runTest {
        playlistsRepository.addPlaylist(
            name = "Road trip",
            description = "Music for road",
            image = "cover.jpg"
        )

        val playlists = playlistsRepository.getAllPlaylists().first()

        assertEquals(1, playlists.size)
        assertEquals("Road trip", playlists.first().name)
        assertEquals("Music for road", playlists.first().description)
        assertEquals("cover.jpg", playlists.first().image)
    }

    @Test
    fun `deletePlaylist removes playlist`() = runTest {
        playlistsRepository.addPlaylist(
            name = "Road trip",
            description = "Music for road",
            image = null
        )
        val playlist = playlistsRepository.getAllPlaylists().first().first()

        playlistsRepository.deletePlaylist(playlist.playlistId)

        assertTrue(playlistsRepository.getAllPlaylists().first().isEmpty())
    }

    @Test
    fun `getPlaylist returns playlist by id`() = runTest {
        playlistsRepository.addPlaylist(
            name = "Road trip",
            description = "Music for road",
            image = null
        )
        val savedPlaylist = playlistsRepository.getAllPlaylists().first().first()

        val result = playlistsRepository.getPlaylist(savedPlaylist.playlistId)

        assertEquals(savedPlaylist, result)
    }

    @Test
    fun `getCountTracksInPlaylist returns current count`() = runTest {
        playlistsRepository.addPlaylist(
            name = "Road trip",
            description = "Music for road",
            image = null
        )
        val playlist = playlistsRepository.getAllPlaylists().first().first()
        tracksRepository.insertTrackToPlaylist(
            track = TestData.track(id = 10L),
            playlistId = playlist.playlistId
        )
        tracksRepository.insertTrackToPlaylist(
            track = TestData.track(id = 20L),
            playlistId = playlist.playlistId
        )

        val count = playlistsRepository.getCountTracksInPlaylist(playlist.playlistId).first()

        assertEquals(2, count)
    }

    @Test
    fun `getAllTrackInPlaylist returns linked tracks`() = runTest {
        playlistsRepository.addPlaylist(
            name = "Road trip",
            description = "Music for road",
            image = null
        )
        val playlist = playlistsRepository.getAllPlaylists().first().first()
        val track1 = TestData.track(id = 10L, name = "First")
        val track2 = TestData.track(id = 20L, name = "Second")
        tracksRepository.insertTrackToPlaylist(track1, playlist.playlistId)
        tracksRepository.insertTrackToPlaylist(track2, playlist.playlistId)

        val tracks = playlistsRepository.getAllTrackInPlaylist(playlist.playlistId).first()

        assertEquals(listOf(track1, track2).map { it.trackId }.toSet(), tracks.map { it.trackId }.toSet())
    }
}
