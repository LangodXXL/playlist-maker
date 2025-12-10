package com.solyakov.playlist.data.network

import com.solyakov.playlist.data.NetworkClient
import com.solyakov.playlist.data.dto.TrackSearchRequest
import com.solyakov.playlist.data.dto.TrackSearchResponse
import com.solyakov.playlist.domain.api.TracksRepository
import com.solyakov.playlist.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) { // успешный запрос
            return (response as TrackSearchResponse).results.map {
                val seconds = it.trackTimeMillis / 1000
                val minutes = seconds / 60
                val trackTime = "%02d".format(minutes) + ":" + "%02d".format(seconds - minutes * 60)
                Track(it.trackName, it.artistName, trackTime) }
        } else {
            return emptyList()
        }
    }
}