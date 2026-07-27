package com.solyakov.playlist.ui.view_model

import com.solyakov.playlist.testutil.FakeSearchHistoryRepository
import com.solyakov.playlist.testutil.FakeTrackPlayer
import com.solyakov.playlist.testutil.FakeTracksRepository
import com.solyakov.playlist.testutil.MainDispatcherRule
import com.solyakov.playlist.testutil.TestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val tracksRepository = FakeTracksRepository()
    private val historyRepository = FakeSearchHistoryRepository()
    private val trackPlayer = FakeTrackPlayer()

    @Test
    fun `blank query clears state to Initial`() {
        val viewModel = createViewModel()

        viewModel.search("")

        assertEquals("", viewModel.searchText.value)
        assertEquals(SearchState.Initial, viewModel.searchScreenState.value)
    }

    @Test
    fun `clearQuery clears text and state`() {
        val viewModel = createViewModel()

        viewModel.search("queen")
        viewModel.clearQuery()

        assertEquals("", viewModel.searchText.value)
        assertEquals(SearchState.Initial, viewModel.searchScreenState.value)
    }

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
    fun `getHistory returns history repository flow`() = runTest {
        val viewModel = createViewModel()
        historyRepository.historyFlow.value = listOf("queen", "metallica")

        assertEquals(listOf("queen", "metallica"), viewModel.getHistory().first())
    }

    /**
     * These tests are deterministic if SearchScreenViewModel receives an injected dispatcher.
     * Add this constructor parameter:
     * private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
     * and replace viewModelScope.launch(Dispatchers.IO) with viewModelScope.launch(ioDispatcher).
     */
    @Test
    fun `search emits Success when repository returns tracks`() = runTest {
        val tracks = listOf(TestData.track(id = 1L))
        tracksRepository.searchResult = tracks
        val viewModel = createViewModelWithInjectedDispatcher()

        viewModel.search("queen")
        advanceTimeBy(500)
        advanceUntilIdle()

        assertEquals("queen", tracksRepository.lastSearchExpression)
        assertEquals(SearchState.Success(tracks), viewModel.searchScreenState.value)
    }

    @Test
    fun `search emits network_error when repository throws IOException`() = runTest {
        tracksRepository.searchException = IOException("No internet")
        val viewModel = createViewModelWithInjectedDispatcher()

        viewModel.search("queen")
        advanceTimeBy(500)
        advanceUntilIdle()

        assertEquals(SearchState.Fail("network_error"), viewModel.searchScreenState.value)
    }

    @Test
    fun `search emits server_error when repository throws generic exception`() = runTest {
        tracksRepository.searchException = RuntimeException("Server")
        val viewModel = createViewModelWithInjectedDispatcher()

        viewModel.search("queen")
        advanceTimeBy(500)
        advanceUntilIdle()

        assertEquals(SearchState.Fail("server_error"), viewModel.searchScreenState.value)
    }

    @Test
    fun `searchAndAddToHistory adds query and searches`() = runTest {
        val tracks = listOf(TestData.track(id = 1L))
        tracksRepository.searchResult = tracks
        val viewModel = createViewModelWithInjectedDispatcher()

        viewModel.searchAndAddToHistory("queen")
        advanceUntilIdle()

        assertEquals(listOf("queen"), historyRepository.addedQueries)
        assertEquals(SearchState.Success(tracks), viewModel.searchScreenState.value)
    }

    private fun createViewModel(): SearchScreenViewModel {
        return SearchScreenViewModel(
            tracksRepository = tracksRepository,
            historyRepository = historyRepository,
            trackPlayer = trackPlayer
        )
    }

    private fun createViewModelWithInjectedDispatcher(): SearchScreenViewModel {
        return SearchScreenViewModel(
            tracksRepository = tracksRepository,
            historyRepository = historyRepository,
            trackPlayer = trackPlayer,
            dispatcherIO = mainDispatcherRule.testDispatcher
        )
    }
}
