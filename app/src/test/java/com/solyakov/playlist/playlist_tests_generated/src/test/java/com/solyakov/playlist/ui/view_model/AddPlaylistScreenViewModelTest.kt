package com.solyakov.playlist.ui.view_model

import android.net.Uri
import com.solyakov.playlist.testutil.FakeImageStorage
import com.solyakov.playlist.testutil.FakePlaylistsRepository
import com.solyakov.playlist.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AddPlaylistScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val playlistsRepository = FakePlaylistsRepository()
    private val imageStorage = FakeImageStorage()

    @Test
    fun `setters update screen fields`() {
        val viewModel = createViewModel()
        val uri = Uri.parse("content://images/1")

        viewModel.setPlaylistName("Road trip")
        viewModel.setPlaylistDescription("Music for road")
        viewModel.setSelectedImage(uri)

        assertEquals("Road trip", viewModel.playlistName.value)
        assertEquals("Music for road", viewModel.playlistDescription.value)
        assertEquals(uri, viewModel.selectedImage.value)
    }

    @Test
    fun `savePlaylist without image saves playlist with null image`() = runTest {
        val viewModel = createViewModel()
        viewModel.setPlaylistName("Road trip")
        viewModel.setPlaylistDescription("Music for road")

        viewModel.savePlaylist()
        advanceUntilIdle()

        assertEquals(listOf(Triple("Road trip", "Music for road", null)), playlistsRepository.addedPlaylists)
        assertNull(imageStorage.savedUri)
    }

    @Test
    fun `savePlaylist with image saves image and passes saved path to repository`() = runTest {
        val viewModel = createViewModel()
        val uri = Uri.parse("content://images/1")
        imageStorage.resultPath = "internal/playlist-cover.jpg"
        viewModel.setPlaylistName("Road trip")
        viewModel.setPlaylistDescription("Music for road")
        viewModel.setSelectedImage(uri)

        viewModel.savePlaylist()
        advanceUntilIdle()

        assertEquals(uri.toString(), imageStorage.savedUri)
        assertEquals(
            listOf(Triple("Road trip", "Music for road", "internal/playlist-cover.jpg")),
            playlistsRepository.addedPlaylists
        )
    }

    private fun createViewModel(): AddPlaylistScreenViewModel {
        return AddPlaylistScreenViewModel(
            playlistsRepository = playlistsRepository,
            imageStorage = imageStorage
        )
    }
}
