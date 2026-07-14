package com.solyakov.playlist.data.player

import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.player.PlayerState
import com.solyakov.playlist.domain.player.TrackPlayer
import com.solyakov.playlist.toMediaItem
import com.solyakov.playlist.toTrackOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Media3TrackPlayer(
    context: Context
): TrackPlayer {

    private val appContext = context.applicationContext

    private val _state = MutableStateFlow(PlayerState())
    override val state: StateFlow<PlayerState> = _state.asStateFlow()

    private var positionJob: Job? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val sessionToken = SessionToken(
        appContext,
        ComponentName(appContext, AudioPlayerService::class.java)
    )
    private var controller: MediaController? = null
    private val controllerFuture = MediaController.Builder(
        appContext,
        sessionToken
    ).buildAsync()

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updateState()
            if (isPlaying) startPositionUpdates()
            else stopPositionUpdates()
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            updateState()
            if (playbackState == Player.STATE_ENDED) {
                stopPositionUpdates()
            }
        }

        override fun onEvents(
            player: Player,
            events: Player.Events
        ) {
            updateState()
        }

        override fun onMediaItemTransition(
            mediaItem: MediaItem?,
            reason: Int
        ) {
            updateState()
        }
    }

    init {
        controllerFuture.addListener(
            {
              val mediaController = controllerFuture.get()
                mediaController.addListener(playerListener)
                controller = mediaController
                updateState()
            },
            ContextCompat.getMainExecutor(appContext)

        )
    }
    override fun setQueue(
        tracks: List<Track>,
        startIndex: Int,
        playWhenReady: Boolean
    ) {
        val controller = controller ?: return
        if (tracks.isEmpty()) return
        if (startIndex !in tracks.indices) return

        val mediaItems = tracks.map {track ->
            track.toMediaItem()
        }

        controller.setMediaItems(
            mediaItems,
            startIndex,
            0L
        )
        controller.prepare()

        if (playWhenReady) {
            controller.play()
        }
        updateState()
    }

    override fun togglePlayPause() {
        val controller = controller ?: return
        if (controller.isPlaying) {
            controller.pause()
        }
        else {
            controller.play()
        }
        updateState()
    }

    override fun play() {
        controller?.play()
        updateState()
    }

    override fun pause() {
        controller?.pause()
        updateState()
    }

    override fun seekTo(positionMs: Long) {
        controller?.seekTo(positionMs)
        updateState()
    }

    override fun seekToNext() {
        val controller = controller ?: return

        if (controller.hasNextMediaItem()) {
            controller.seekToNextMediaItem()
        }
        updateState()
    }

    override fun seekToPrevious() {
        val controller = controller ?: return

        if (controller.hasPreviousMediaItem()) {
            controller.seekToPreviousMediaItem()
        } else {
            controller.seekTo(0L)
        }
        updateState()
    }

    override fun stop() {
        controller?.stop()
        stopPositionUpdates()
        _state.value = PlayerState()
    }
    private fun updateState() {

        val controller = controller ?: return
        val currentTrack = controller.currentMediaItem?.toTrackOrNull()
        val durationMs = controller.duration.takeIf { it > 0 } ?: DEFAULT_DURATION_MS


        _state.value = PlayerState(
            currentTrack = currentTrack,
            isPlaying = controller.isPlaying,
            positionMs = controller.currentPosition,
            durationMs = durationMs,
            currentIndex = controller.currentMediaItemIndex,
            hasNext = controller.hasNextMediaItem(),
            hasPrevious = controller.hasPreviousMediaItem()
        )

    }


    private fun startPositionUpdates() {
        positionJob?.cancel()
        positionJob = scope.launch {
            while (true) {
                updateState()
                delay(POSITION_UPDATE_DELAY_MS)
            }
        }
    }

    private fun stopPositionUpdates() {
        positionJob?.cancel()
        positionJob = null
    }

    fun release() {
        stopPositionUpdates()
        scope.cancel()

        controller?.removeListener(playerListener)
        controller = null
        MediaController.releaseFuture(controllerFuture)
    }

    private companion object {
        const val POSITION_UPDATE_DELAY_MS = 500L
        const val DEFAULT_DURATION_MS = 30_000L
    }
}