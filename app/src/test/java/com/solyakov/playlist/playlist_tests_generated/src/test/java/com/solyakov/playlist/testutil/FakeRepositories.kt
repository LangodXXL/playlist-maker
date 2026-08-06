package com.solyakov.playlist.testutil

import com.solyakov.playlist.domain.models.Track
import com.solyakov.playlist.domain.models.Playlist
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import com.solyakov.playlist.domain.repository.SearchHistoryRepository
import com.solyakov.playlist.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTracksRepository : TracksRepository {

    var searchResult: List<Track> = emptyList()
    var searchException: Exception? = null
    var lastSearchExpression: String? = null

    var trackByIdResult: Track = TestData.track()
    var getTrackByIdException: Exception? = null
    var lastRequestedTrackId: Long? = null

    val favoriteTracksFlow = MutableStateFlow<List<Track>>(emptyList())

    val updatedFavoriteTracks = mutableListOf<Track>()
    val deletedFromAllPlaylists = mutableListOf<Long>()
    val deletedFromPlaylist = mutableListOf<Pair<Long, Long>>()
    val insertedToPlaylist = mutableListOf<Pair<Track, Long>>()

    override suspend fun searchTracks(expression: String): List<Track> {
        lastSearchExpression = expression
        searchException?.let { throw it }
        return searchResult
    }

    override suspend fun getTrackById(trackId: Long): Track {
        lastRequestedTrackId = trackId
        getTrackByIdException?.let { throw it }
        return trackByIdResult
    }

    override suspend fun updateTrackFavoriteStatus(track: Track) {
        updatedFavoriteTracks += track
    }

    override suspend fun deleteTrackFromAllPlaylists(trackId: Long) {
        deletedFromAllPlaylists += trackId
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        deletedFromPlaylist += trackId to playlistId
    }

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        insertedToPlaylist += track to playlistId
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = favoriteTracksFlow
}

class FakePlaylistsRepository : PlaylistsRepository {

    val playlistsFlow = MutableStateFlow<List<Playlist>>(emptyList())
    private val playlistTracksFlows = mutableMapOf<Long, MutableStateFlow<List<Track>>>()
    private val playlistCountFlows = mutableMapOf<Long, MutableStateFlow<Int>>()

    val addedPlaylists = mutableListOf<Triple<String, String, String?>>()
    val deletedPlaylistIds = mutableListOf<Long>()

    override fun getAllPlaylists(): Flow<List<Playlist>> = playlistsFlow

    override suspend fun addPlaylist(name: String, description: String, image: String?) {
        addedPlaylists += Triple(name, description, image)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        deletedPlaylistIds += playlistId
    }

    override suspend fun getPlaylist(id: Long): Playlist {
        return playlistsFlow.value.first { it.playlistId == id }
    }

    override fun getCountTracksInPlaylist(playlistId: Long): Flow<Int> {
        return playlistCountFlows.getOrPut(playlistId) { MutableStateFlow(0) }
    }

    override fun getAllTrackInPlaylist(playlistId: Long): Flow<List<Track>> {
        return playlistTracksFlows.getOrPut(playlistId) { MutableStateFlow(emptyList()) }
    }

    fun setTracksForPlaylist(playlistId: Long, tracks: List<Track>) {
        playlistTracksFlows.getOrPut(playlistId) { MutableStateFlow(emptyList()) }.value = tracks
        playlistCountFlows.getOrPut(playlistId) { MutableStateFlow(0) }.value = tracks.size
    }
}

class FakeSearchHistoryRepository : SearchHistoryRepository {

    val historyFlow = MutableStateFlow<List<String>>(emptyList())
    val addedQueries = mutableListOf<String>()

    override suspend fun addToHistory(word: String) {
        addedQueries += word
        historyFlow.value = listOf(word) + historyFlow.value.filter { it != word }
    }

    override fun getHistory(): Flow<List<String>> = historyFlow
}
