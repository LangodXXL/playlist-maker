package com.solyakov.playlist.data

import com.solyakov.playlist.data.dto.BaseResponse
import com.solyakov.playlist.data.network.NetworkClient

class FakeNetworkClient: NetworkClient {

    var lastRequest: Any? = null
    var response: BaseResponse = BaseResponse().apply { resultCode = 200 }

    override suspend fun doRequest(dto: Any): BaseResponse {
        lastRequest = dto
        return response
    }
}
