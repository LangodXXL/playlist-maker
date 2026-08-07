package com.solyakov.playlist.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("trackId") val id: Long,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Long,
    @SerializedName("artworkUrl100") val artworkUrl100: String?,
    @SerializedName("previewUrl") val previewUrl: String?,
)
