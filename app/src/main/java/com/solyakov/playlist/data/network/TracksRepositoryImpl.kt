package com.solyakov.playlist.data.network

import com.solyakov.playlist.data.dto.TrackSearchByIdRequest
import com.solyakov.playlist.data.dto.TrackSearchByIdResponse
import com.solyakov.playlist.domain.api.NetworkClient
import com.solyakov.playlist.data.dto.TracksSearchRequest
import com.solyakov.playlist.data.dto.TracksSearchResponse
import com.solyakov.playlist.domain.repository.TracksRepository
import com.solyakov.playlist.toTrackModel

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return if (response.resultCode == 200) {
            (response as TracksSearchResponse).results.map {
                it.toTrackModel()
            }
        } else {
            emptyList()
        }
    }

    override suspend fun getTrackById(trackId: Long): Track {
        val response = networkClient.doRequest(TrackSearchByIdRequest(trackId))
        return (response as TrackSearchByIdResponse)
            .results
            .first()
            .toTrackModel()

    }
}