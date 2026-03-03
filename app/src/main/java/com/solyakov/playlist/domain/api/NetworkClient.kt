package com.solyakov.playlist.domain.api

import com.solyakov.playlist.data.dto.BaseResponse
import com.solyakov.playlist.data.network.Track

interface NetworkClient {
    suspend fun doRequest(dto: Any): BaseResponse
}