package com.solyakov.playlist.ui.view_model

import com.solyakov.playlist.testutil.FakePlaylistsRepository
import com.solyakov.playlist.testutil.MainDispatcherRule
import com.solyakov.playlist.testutil.TestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlaylistsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val playlistsRepository = FakePlaylistsRepository()

    @Test
    fun `playlistsWithCounts combines playlists with track counts`() = runTest {
        val playlist1 = TestData.playlist(id = 1L, name = "First")
        val playlist2 = TestData.playlist(id = 2L, name = "Second")
        playlistsRepository.playlistsFlow.value = listOf(playlist1, playlist2)
        playlistsRepository.setTracksForPlaylist(1L, listOf(TestData.track(id = 10L)))
        playlistsRepository.setTracksForPlaylist(2L, listOf(TestData.track(id = 20L), TestData.track(id = 21L)))
        val viewModel = createViewModelWithInjectedDispatcher()
        val collectJob = launch { viewModel.playlistsWithCounts.collect {} }

        advanceUntilIdle()

        assertEquals(
            listOf(playlist1 to 1, playlist2 to 2),
            viewModel.playlistsWithCounts.value
        )

        collectJob.cancel()
    }

    @Test
    fun `createNewPlayList calls repository`() = runTest {
        val viewModel = createViewModelWithInjectedDispatcher()

        viewModel.createNewPlayList(
            namePlaylist = "Road trip",
            description = "Music",
            image = "cover.jpg"
        )
        advanceUntilIdle()

        assertEquals(listOf(Triple("Road trip", "Music", "cover.jpg")), playlistsRepository.addedPlaylists)
    }

    @Test
    fun `deletePlaylistById calls repository`() = runTest {
        val viewModel = createViewModelWithInjectedDispatcher()

        viewModel.deletePlaylistById(5L)
        advanceUntilIdle()

        assertEquals(listOf(5L), playlistsRepository.deletedPlaylistIds)
    }

    private fun createViewModelWithInjectedDispatcher(): PlaylistsViewModel {
        return PlaylistsViewModel(
            playlistsRepository = playlistsRepository,
            dispatcherIO = mainDispatcherRule.testDispatcher
        )
    }
}
