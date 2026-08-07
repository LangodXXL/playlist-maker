package com.solyakov.playlist.ui.view_model

import com.solyakov.playlist.presentation.view_model.TrackScreenState
import com.solyakov.playlist.presentation.view_model.TrackViewModel
import com.solyakov.playlist.testing.testutil.FakePlaylistsRepository
import com.solyakov.playlist.testing.testutil.FakeTrackPlayer
import com.solyakov.playlist.testing.testutil.FakeTracksRepository
import com.solyakov.playlist.testing.testutil.MainDispatcherRule
import com.solyakov.playlist.testing.testutil.TestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrackViewModelTest {

    @get:Rule
    val mainDispatcherRule: MainDispatcherRule
        get() = MainDispatcherRule()

    private val playlistsRepository = FakePlaylistsRepository()
    private val tracksRepository = FakeTracksRepository()
    private val trackPlayer = FakeTrackPlayer()

    @Test
    fun `playTrack toggles player`() {
        val viewModel = createViewModel()

        viewModel.playTrack()

        assertTrue(trackPlayer.togglePlayPauseCalled)
    }

    @Test
    fun `playNext asks player to move to next track`() {
        val viewModel = createViewModel()

        viewModel.playNext()

        assertTrue(trackPlayer.seekToNextCalled)
    }

    @Test
    fun `playPrevious asks player to move to previous track`() {
        val viewModel = createViewModel()

        viewModel.playPrevious()

        assertTrue(trackPlayer.seekToPreviousCalled)
    }

    @Test
    fun `seekTo passes milliseconds to player`() {
        val viewModel = createViewModel()

        viewModel.seekTo(15_000f)

        assertEquals(15_000L, trackPlayer.seekToPositionMs)
    }

    @Test
    fun `getTrackById sets Success state when repository returns track`() = runTest {
        val track = TestData.track(id = 100L)
        tracksRepository.trackByIdResult = track
        val viewModel = createViewModel()

        viewModel.getTrackById(100L)
        advanceUntilIdle()

        assertEquals(100L, tracksRepository.lastRequestedTrackId)
        assertEquals(TrackScreenState.Success(track), viewModel.screenState.value)
        assertEquals(track, viewModel.track.value)
    }

    @Test
    fun `getTrackById sets Error state when repository throws exception`() = runTest {
        tracksRepository.getTrackByIdException = IllegalStateException("Track not found")
        val viewModel = createViewModel()

        viewModel.getTrackById(100L)
        advanceUntilIdle()

        assertEquals(TrackScreenState.Error("Track not found"), viewModel.screenState.value)
    }

    @Test
    fun `addTrackToFavorite calls repository and updates state with inverted favorite`() = runTest {
        val track = TestData.track(id = 10L, favorite = false)
        val viewModel = createViewModel()

        viewModel.addTrackToFavorite(track)
        advanceUntilIdle()

        val expected = track.copy(favorite = true)
        assertEquals(listOf(track), tracksRepository.updatedFavoriteTracks)
        assertEquals(expected, viewModel.track.value)
        assertEquals(TrackScreenState.Success(expected), viewModel.screenState.value)
    }

    @Test
    fun `addTrackInPlaylist calls repository`() = runTest {
        val track = TestData.track(id = 10L)
        val viewModel = createViewModel()

        viewModel.addTrackInPlaylist(track, playlistId = 7L)
        advanceUntilIdle()

        assertEquals(listOf(track to 7L), tracksRepository.insertedToPlaylist)
    }

    @Test
    fun `getAllPlaylists returns repository flow`() = runTest {
        val playlists = listOf(TestData.playlist(id = 1L), TestData.playlist(id = 2L))
        playlistsRepository.playlistsFlow.value = playlists
        val viewModel = createViewModel()

        assertEquals(playlists, viewModel.getAllPlaylists().first())
    }

    private fun createViewModel(): TrackViewModel {
        return TrackViewModel(
            playlistRepository = playlistsRepository,
            trackRepository = tracksRepository,
            trackPlayer = trackPlayer
        )
    }
}
