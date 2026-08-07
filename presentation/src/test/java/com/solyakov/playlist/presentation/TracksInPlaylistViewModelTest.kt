package com.solyakov.playlist.ui.view_model

import com.solyakov.playlist.presentation.view_model.TracksInPlaylistViewModel
import com.solyakov.playlist.testing.testutil.FakePlaylistsRepository
import com.solyakov.playlist.testing.testutil.FakeTrackPlayer
import com.solyakov.playlist.testing.testutil.FakeTracksRepository
import com.solyakov.playlist.testing.testutil.MainDispatcherRule
import com.solyakov.playlist.testing.testutil.TestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TracksInPlaylistViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val playlistsRepository = FakePlaylistsRepository()
    private val tracksRepository = FakeTracksRepository()
    private val trackPlayer = FakeTrackPlayer()

    @Test
    fun `onTrackClick sends selected queue to trackPlayer`() {
        val viewModel = createViewModel()
        val tracks = listOf(TestData.track(id = 1L), TestData.track(id = 2L))

        viewModel.onTrackClick(tracks, startIndex = 1)

        assertEquals(tracks, trackPlayer.queue)
        assertEquals(1, trackPlayer.startIndex)
        assertEquals(false, trackPlayer.playWhenReady)
    }

    @Test
    fun `getAllTracksInPlaylist updates tracks and count`() = runTest {
        val tracks = listOf(
            TestData.track(id = 1L),
            TestData.track(id = 2L)
        )

        playlistsRepository.setTracksForPlaylist(
            playlistId = 5L,
            tracks = tracks
        )

        val viewModel = createViewModel()

        viewModel.getAllTracksInPlaylist(5L)

        advanceUntilIdle()

        assertEquals(tracks, viewModel.tracks.value)
        assertEquals(2, viewModel.tracks.value.size)
    }

    @Test
    fun `deleteTrack calls repository`() = runTest {
        val viewModel = createViewModel()
        val track = TestData.track(id = 10L)

        viewModel.deleteTrack(track, playlistId = 5L)
        advanceUntilIdle()

        assertEquals(listOf(10L to 5L), tracksRepository.deletedFromPlaylist)
    }

    private fun createViewModel(): TracksInPlaylistViewModel {
        return TracksInPlaylistViewModel(
            playlistsRepository = playlistsRepository,
            tracksRepository = tracksRepository,
            trackPlayer = trackPlayer
        )
    }
}
