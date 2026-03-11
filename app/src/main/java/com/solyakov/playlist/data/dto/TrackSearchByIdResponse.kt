package com.solyakov.playlist.data.dto

data class TrackSearchByIdResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : BaseResponse()
