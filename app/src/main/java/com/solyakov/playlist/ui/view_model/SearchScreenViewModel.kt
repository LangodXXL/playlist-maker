package com.solyakov.playlist.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.repository.SearchHistoryRepository
import com.solyakov.playlist.domain.repository.TracksRepository
import com.solyakov.playlist.toMediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    val historyRepository: SearchHistoryRepository,
    context: Context,
    sessionToken: SessionToken
) : ViewModel() {


    private var controller: MediaController? = null
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    init {
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            controller = controllerFuture.get()
        },
            MoreExecutors.directExecutor())
    }

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState  = _searchScreenState.asStateFlow()
    private var searchJob: Job? = null

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
        val controller = controller ?: return
        val mediaItems = tracks.map { it.toMediaItem() }

        controller.setMediaItems(mediaItems, startIndex, 0L)
        controller.prepare()
    }
    override fun onCleared() {
        super.onCleared()
        controller?.release()
        controller = null
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