package com.solyakov.playlist.data.network

import com.solyakov.playlist.data.dto.TrackSearchByIdRequest
import com.solyakov.playlist.data.dto.TrackSearchByIdResponse
import com.solyakov.playlist.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("/search")
    suspend fun searchTracks(
        @Query("term") query: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 50
    ): TracksSearchResponse

    @GET("/lookup")
    suspend fun lookupTrackById(
        @Query("id") trackId: Long
    ): TrackSearchByIdResponse
}

