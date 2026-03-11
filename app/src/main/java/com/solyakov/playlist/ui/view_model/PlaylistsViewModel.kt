package com.solyakov.playlist.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solyakov.playlist.data.playlist.Playlist
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {


    val playlistsWithCounts: StateFlow<List<Pair<Playlist, Int>>> =
        playlistsRepository.getAllPlaylists()
            .flatMapLatest { playlistList ->
                val countFlows = playlistList.map { playlist ->
                    playlistsRepository.getCountTracksInPlaylist(playlist.playlistId)
                        .map { count -> playlist to count }
                }
                if (countFlows.isEmpty()) flowOf(emptyList())
                else combine(countFlows) { it.toList() }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun createNewPlayList(namePlaylist: String, description: String, image: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addPlaylist(namePlaylist, description, image)
        }
    }


    fun deletePlaylistById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.deletePlaylist(id)
        }
    }

}