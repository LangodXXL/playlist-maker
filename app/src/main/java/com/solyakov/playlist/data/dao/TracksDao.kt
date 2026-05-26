package com.solyakov.playlist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.solyakov.playlist.data.database.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksDao {
    @Upsert
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks WHERE trackName = :name AND artistName = :artist")
    fun getTrackByNameAndArtist(name: String, artist: String): Flow<TrackEntity?>

    @Query("DELETE FROM tracks WHERE trackId = :trackId")
    suspend fun deleteTrackFromAllPlaylist(trackId: Long)

    @Query("DELETE FROM table_link WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)

    @Query("Select * from tracks where favorite = 1")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Long): TrackEntity?
} 