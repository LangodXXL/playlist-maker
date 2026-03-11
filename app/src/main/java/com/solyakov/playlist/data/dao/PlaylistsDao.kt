package com.solyakov.playlist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.solyakov.playlist.data.database.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Query("Select * from playlist")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("Delete from playlist where playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    @Query("Select * from playlist where playlistId = :playlistId")
    suspend fun getPlaylist(playlistId: Long): PlaylistEntity

}