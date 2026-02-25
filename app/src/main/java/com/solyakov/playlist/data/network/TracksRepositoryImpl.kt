package com.solyakov.playlist.data.network

import com.solyakov.playlist.data.dto.TrackDto
import com.solyakov.playlist.domain.api.NetworkClient
import com.solyakov.playlist.data.dto.TracksSearchRequest
import com.solyakov.playlist.data.dto.TracksSearchResponse
import com.solyakov.playlist.domain.repository.TracksRepository
import kotlinx.coroutines.delay

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        delay(100)
        return if (response.resultCode == 200) {
            (response as TracksSearchResponse).results.map {
                val seconds = it.trackTimeMillis / 1000
                val minutes = seconds / 60
                val trackTime = "%02d".format(minutes) + ":" + "%02d".format(seconds - minutes * 60)
                Track(0L, it.trackName, it.artistName, trackTime, null) }
        } else {
            emptyList()
        }
    }

    override suspend fun getAllTracks(): List<Track> {
        return networkClient.getAllTracks()
    }
}