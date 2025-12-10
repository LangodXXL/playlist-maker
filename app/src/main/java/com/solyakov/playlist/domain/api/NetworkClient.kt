package com.solyakov.playlist.domain.api

import com.solyakov.playlist.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}