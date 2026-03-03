package com.solyakov.playlist.data.network

import com.solyakov.playlist.data.dto.BaseResponse
import com.solyakov.playlist.data.dto.TrackSearchByIdRequest
import com.solyakov.playlist.domain.api.NetworkClient
import com.solyakov.playlist.data.dto.TracksSearchRequest
import java.io.IOException

class RetrofitNetworkClient(private val api: ITunesApiService) : NetworkClient {

    override suspend fun doRequest(dto: Any): BaseResponse {
        return try {
            when (dto) {
            is TracksSearchRequest -> {
                api.searchTracks(
                    query = dto.expression,
                    media = "music",
                    entity = "song",
                    limit = 50
                ).apply { resultCode = 200 }
            }
                is TrackSearchByIdRequest -> {
                    api.lookupTrackById(trackId = dto.trackId).apply { resultCode = 200 }
                }
                else -> {
                    BaseResponse().apply { resultCode = 400 }
                }
            }
        }
        catch (e: IOException) {
            BaseResponse().apply { resultCode = -1 }
        } catch (e: Exception) {
            BaseResponse().apply { resultCode = -2 }
        }
    }
}

