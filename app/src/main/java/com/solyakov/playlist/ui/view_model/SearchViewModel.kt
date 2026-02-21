package com.solyakov.playlist.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solyakov.playlist.data.history.SearchHistoryRepositoryImpl
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.api.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed class SearchState {
    object Initial: SearchState()
    object Searching: SearchState()
    data class Success(val foundList: List<Track>): SearchState()
    data class Fail(val error: String): SearchState()
}

class SearchViewModel(
    private val tracksRepository: TracksRepository,
    val historyRepository: SearchHistoryRepositoryImpl
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState  = _searchScreenState.asStateFlow()

    fun search(whatSearch: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchScreenState
                    .update { SearchState.Searching }
                val list = tracksRepository.searchTracks(expression = whatSearch)
                if (list.isEmpty() && whatSearch.isBlank()) {
                    _searchScreenState.update { SearchState.Initial }
                }
                else {
                    _searchScreenState.update { SearchState.Success(foundList = list) }
                }
            } catch (e: IOException){
                _searchScreenState.update { SearchState.Fail(e.message.toString()) }
            }
        }
    }

    fun searchAndAddToHistory(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                historyRepository.addToHistory(query)
            }
        }

    }


}