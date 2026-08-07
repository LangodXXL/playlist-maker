package com.solyakov.playlist.data.network

import com.solyakov.playlist.data.dto.BaseResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): BaseResponse
}