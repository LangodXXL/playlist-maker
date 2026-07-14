package com.solyakov.playlist.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.player.TrackPlayer
import com.solyakov.playlist.domain.repository.SearchHistoryRepository
import com.solyakov.playlist.domain.repository.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.coroutines.coroutineContext

sealed class SearchState {
    object Initial: SearchState()
    object Searching: SearchState()
    data class Success(val foundList: List<Track>): SearchState()
    data class Fail(val error: String): SearchState()
}

class SearchScreenViewModel(
    private val tracksRepository: TracksRepository,
    private val historyRepository: SearchHistoryRepository,
    private val trackPlayer: TrackPlayer
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState  = _searchScreenState.asStateFlow()
    private var searchJob: Job? = null

    fun getHistory(): Flow<List<String>> {
        return historyRepository.getHistory()
    }

    fun search(whatSearch: String) {
        _searchText.value = whatSearch
        searchJob?.cancel()
        if (whatSearch.isBlank()) {
            _searchScreenState.update { SearchState.Initial }
            return
        }
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            executeSearch(whatSearch)

        }

    }
    private suspend fun executeSearch(whatSearch: String) {
        try {
            if (coroutineContext.isActive) {
                _searchScreenState.update { SearchState.Searching }
                val list = tracksRepository.searchTracks(expression = whatSearch)
                _searchScreenState.update { SearchState.Success(foundList = list) }
            }
        } catch (e: IOException) {
            if (coroutineContext.isActive) {
                _searchScreenState.update { SearchState.Fail("network_error") }
            }
        } catch (e: Exception) {
            if (coroutineContext.isActive) {
                _searchScreenState.update { SearchState.Fail("server_error") }
            }
        }
    }

    fun onTrackClick(tracks: List<Track>, startIndex: Int) {
        trackPlayer.setQueue(
            tracks,
            startIndex,
            playWhenReady = false
        )
    }

    fun clearQuery() {
        _searchText.value = ""
        searchJob?.cancel()
        _searchScreenState.update { SearchState.Initial }
    }

    fun searchAndAddToHistory(query: String) {
        if (query.isNotBlank()) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch(Dispatchers.IO) {
                historyRepository.addToHistory(query)
                executeSearch(query)
            }
        }

    }

}