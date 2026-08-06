package com.solyakov.playlist.data.network

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.solyakov.playlist.data.database.AppDatabase
import com.solyakov.playlist.data.database.PlaylistEntity
import com.solyakov.playlist.data.dto.BaseResponse
import com.solyakov.playlist.data.dto.TrackSearchByIdRequest
import com.solyakov.playlist.data.dto.TrackSearchByIdResponse
import com.solyakov.playlist.data.dto.TracksSearchRequest
import com.solyakov.playlist.data.dto.TracksSearchResponse
import com.solyakov.playlist.domain.models.Track
import com.solyakov.playlist.testutil.FakeNetworkClient
import com.solyakov.playlist.testutil.TestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class TracksRepositoryImplTest {

    private lateinit var database: AppDatabase
    private lateinit var networkClient: FakeNetworkClient
    private lateinit var repository: TracksRepositoryImpl

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        networkClient = FakeNetworkClient()
        repository = TracksRepositoryImpl(
            networkClient = networkClient,
            database = database
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `searchTracks returns mapped tracks for success response`() = runTest {
        val response = TracksSearchResponse(
            resultCount = 1,
            results = listOf(TestData.trackDto(id = 10L, name = "Song", artist = "Artist"))
        ).apply { resultCode = 200 }
        networkClient.response = response

        val result = repository.searchTracks("queen")

        assertEquals(TracksSearchRequest("queen"), networkClient.lastRequest)
        assertEquals(1, result.size)
        assertEquals(10L, result.first().trackId)
        assertEquals("Song", result.first().trackName)
        assertEquals("Artist", result.first().artistName)
    }

    @Test(expected = IOException::class)
    fun `searchTracks throws IOException for no internet response`() = runTest {
        networkClient.response = BaseResponse().apply { resultCode = -1 }

        repository.searchTracks("queen")
    }

    @Test(expected = Exception::class)
    fun `searchTracks throws generic exception for server error response`() = runTest {
        networkClient.response = BaseResponse().apply { resultCode = 500 }

        repository.searchTracks("queen")
    }

    @Test
    fun `getTrackById returns track from local database when it exists`() = runTest {
        val track = TestData.track(id = 10L, favorite = true)
        database.TracksDao().insertTrack(track.toEntityForTest())

        val result = repository.getTrackById(10L)

        assertEquals(track, result)
    }

    @Test
    fun `getTrackById loads track from network when it is absent in database`() = runTest {
        val response = TrackSearchByIdResponse(
            resultCount = 1,
            results = listOf(TestData.trackDto(id = 10L, name = "Song", artist = "Artist"))
        ).apply { resultCode = 200 }
        networkClient.response = response

        val result = repository.getTrackById(10L)

        assertEquals(TrackSearchByIdRequest(10L), networkClient.lastRequest)
        assertEquals(10L, result.trackId)
        assertEquals("Song", result.trackName)
    }

    @Test
    fun `updateTrackFavoriteStatus inserts track with inverted favorite`() = runTest {
        val track = TestData.track(id = 10L, favorite = false)

        repository.updateTrackFavoriteStatus(track)

        val favorites = repository.getFavoriteTracks().first()
        assertEquals(1, favorites.size)
        assertEquals(10L, favorites.first().trackId)
        assertTrue(favorites.first().favorite)
    }

    @Test
    fun `updateTrackFavoriteStatus can remove track from favorites`() = runTest {
        val track = TestData.track(id = 10L, favorite = true)

        repository.updateTrackFavoriteStatus(track)

        val favorites = repository.getFavoriteTracks().first()
        assertTrue(favorites.isEmpty())
        assertFalse(database.TracksDao().getTrackById(10L)!!.favorite)
    }

    @Test
    fun `insertTrackToPlaylist saves track and playlist link`() = runTest {
        database.PlaylistsDao().addPlaylist(
            PlaylistEntity(
                playlistId = 7L,
                name = "Playlist",
                description = "Description",
                image = null
            )
        )
        val track = TestData.track(id = 10L)

        repository.insertTrackToPlaylist(track, playlistId = 7L)

        assertEquals(track.trackId, database.TracksDao().getTrackById(10L)!!.trackId)
        assertEquals(1, database.TableLinkDao().getTrackUsageCount(10L))
    }

    @Test
    fun `deleteTrackFromPlaylist removes not favorite track when it has no playlist usage`() = runTest {
        database.PlaylistsDao().addPlaylist(
            PlaylistEntity(
                playlistId = 7L,
                name = "Playlist",
                description = "Description",
                image = null
            )
        )
        val track = TestData.track(id = 10L, favorite = false)
        repository.insertTrackToPlaylist(track, playlistId = 7L)

        repository.deleteTrackFromPlaylist(trackId = 10L, playlistId = 7L)

        assertEquals(0, database.TableLinkDao().getTrackUsageCount(10L))
        assertEquals(null, database.TracksDao().getTrackById(10L))
    }

    @Test
    fun `deleteTrackFromPlaylist keeps favorite track even without playlist usage`() = runTest {
        database.PlaylistsDao().addPlaylist(
            PlaylistEntity(
                playlistId = 7L,
                name = "Playlist",
                description = "Description",
                image = null
            )
        )
        val track = TestData.track(id = 10L, favorite = true)
        repository.insertTrackToPlaylist(track, playlistId = 7L)

        repository.deleteTrackFromPlaylist(trackId = 10L, playlistId = 7L)

        assertEquals(0, database.TableLinkDao().getTrackUsageCount(10L))
        assertEquals(track.trackId, database.TracksDao().getTrackById(10L)!!.trackId)
    }
}

private fun Track.toEntityForTest(): com.solyakov.playlist.data.database.TrackEntity {
    return com.solyakov.playlist.data.database.TrackEntity(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        image = image,
        favorite = favorite,
        previewUrl = previewUrl
    )
}
