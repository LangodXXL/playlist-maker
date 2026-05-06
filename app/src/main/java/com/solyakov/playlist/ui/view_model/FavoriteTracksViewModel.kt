package com.solyakov.playlist.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.repository.TracksRepository
import com.solyakov.playlist.toMediaItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val tracksRepository: TracksRepository,
    sessionToken: SessionToken,
    context: Context
): ViewModel() {

    private var controller: MediaController? = null

    init {
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            controller = controllerFuture.get()
        },
            MoreExecutors.directExecutor())
    }




    val tracks = tracksRepository.getFavoriteTracks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    fun onTrackClick(tracks: List<Track>, startIndex: Int) {
        val controller = controller ?: return
        val mediaItems = tracks.map { it.toMediaItem() }

        if (controller.currentMediaItemIndex == startIndex && controller.mediaItemCount == tracks.size) {
            return
        }

        controller.setMediaItems(mediaItems, startIndex, 0L)
        controller.prepare()
    }

    override fun onCleared() {
        super.onCleared()
        controller?.release()
        controller = null
    }

    fun toggleFavorite(track: Track) {
        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(track)
        }
    }

}