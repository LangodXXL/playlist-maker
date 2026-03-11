package com.solyakov.playlist.data.network

import android.util.Log
import androidx.room.withTransaction
import com.solyakov.playlist.data.database.AppDatabase
import com.solyakov.playlist.data.database.TableLinkEntity
import com.solyakov.playlist.data.dto.TrackSearchByIdRequest
import com.solyakov.playlist.data.dto.TrackSearchByIdResponse
import com.solyakov.playlist.domain.api.NetworkClient
import com.solyakov.playlist.data.dto.TracksSearchRequest
import com.solyakov.playlist.data.dto.TracksSearchResponse
import com.solyakov.playlist.domain.repository.TracksRepository
import com.solyakov.playlist.toEntity
import com.solyakov.playlist.toTrack
import com.solyakov.playlist.toTrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    database: AppDatabase
) : TracksRepository {

    private val tracksDao = database.TracksDao()
    private val linkDao = database.TableLinkDao()

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return if (response.resultCode == 200) {
            (response as TracksSearchResponse).results.map {
                it.toTrackModel()
            }
        } else {
            emptyList()
        }
    }

    override suspend fun getTrackById(trackId: Long): Track {
        val track = tracksDao.getTrackById(trackId)?.toTrack()
        Log.d("Track", track.toString())
        if (track == null) {
            val response = networkClient.doRequest(TrackSearchByIdRequest(trackId))
            return (response as TrackSearchByIdResponse)
                .results
                .first()
                .toTrackModel()
        }
        return track
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, ) {
        val newFavoriteStatus = !track.favorite
        val entity = track.copy(favorite = newFavoriteStatus).toEntity()
        tracksDao.insertTrack(entity)
    }

    override suspend fun deleteTrackFromAllPlaylists(track: Track) {
        tracksDao.deleteTrackFromAllPlaylist(track.toEntity())
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        linkDao.deleteLink(trackId, playlistId)
    }

    override suspend fun insertTrackToPlaylist(
        track: Track,
        playlistId: Long
    ) {
        tracksDao.insertTrack(track.toEntity())
        linkDao.addLink(TableLinkEntity(track.trackId, playlistId))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return tracksDao.getFavoriteTracks().map {
            it.map {
                it.toTrack()
            }
        }
    }

}