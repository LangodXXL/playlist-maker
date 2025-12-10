package com.solyakov.playlist.data.network

import com.solyakov.playlist.data.NetworkClient
import com.solyakov.playlist.data.dto.BaseResponse
import com.solyakov.playlist.data.dto.TrackSearchResponse

class RetrofitNetworkClient : NetworkClient {

    override fun doRequest(dto: Any): BaseResponse {
        return TrackSearchResponse(listOf())
    }
}