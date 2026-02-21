package com.solyakov.playlist.domain.api

import com.solyakov.playlist.data.dto.BaseResponse
import com.solyakov.playlist.data.network.Track

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse

    fun getAllTracks(): List<Track>
}