package com.solyakov.playlist.data

import com.solyakov.playlist.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}