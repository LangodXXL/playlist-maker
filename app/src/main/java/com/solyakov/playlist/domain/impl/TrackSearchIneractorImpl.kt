package com.solyakov.playlist.domain.impl

import com.solyakov.playlist.domain.api.TrackSearchInteractor
import com.solyakov.playlist.domain.api.TracksRepository
import com.solyakov.playlist.domain.models.Track

class TrackSearchInteractorImpl(private val repository: TracksRepository) : TrackSearchInteractor {

    override fun searchTracks(expression: String): List<Track> {
        return repository.searchTracks(expression)
    }
}