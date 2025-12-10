package com.solyakov.playlist

import com.solyakov.playlist.data.network.RetrofitNetworkClient
import com.solyakov.playlist.data.network.TracksRepositoryImpl
import com.solyakov.playlist.domain.api.TrackSearchInteractor
import com.solyakov.playlist.domain.api.TracksRepository
import com.solyakov.playlist.domain.impl.TrackSearchInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTracksRepository())
    }
}