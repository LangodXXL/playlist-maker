package com.solyakov.playlist.data.network

import com.solyakov.playlist.creator.Storage
import com.solyakov.playlist.domain.api.NetworkClient
import com.solyakov.playlist.data.dto.TracksSearchRequest
import com.solyakov.playlist.data.dto.TracksSearchResponse

class RetrofitNetworkClient(private val storage: Storage) : NetworkClient {

    override fun doRequest(request: Any): TracksSearchResponse {
        val searchList = storage.search((request as TracksSearchRequest).expression)
        return TracksSearchResponse(searchList).apply { resultCode = 200 }
    }
}