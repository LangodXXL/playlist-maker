package com.solyakov.playlist.ui.view_model

import com.solyakov.playlist.presentation.view_model.FavoriteTracksViewModel
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
class FavoriteTracksViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val tracksRepository = FakeTracksRepository()
    private val trackPlayer = FakeTrackPlayer()

    @Test
    fun `onTrackClick sends selected queue to trackPlayer`() {
        val viewModel = createViewModel()
        val tracks = listOf(
            TestData.track(id = 1L),
            TestData.track(id = 2L)
        )

        viewModel.onTrackClick(tracks, startIndex = 1)

        assertEquals(tracks, trackPlayer.queue)
        assertEquals(1, trackPlayer.startIndex)
        assertEquals(false, trackPlayer.playWhenReady)
    }

    @Test
    fun `toggleFavorite calls repository`() = runTest {
        val viewModel = createViewModel()
        val track = TestData.track(id = 42L, favorite = true)

        viewModel.toggleFavorite(track)
        advanceUntilIdle()

        assertEquals(listOf(track), tracksRepository.updatedFavoriteTracks)
    }

    private fun createViewModel(): FavoriteTracksViewModel {
        return FavoriteTracksViewModel(
            tracksRepository = tracksRepository,
            trackPlayer = trackPlayer
        )
    }
}
