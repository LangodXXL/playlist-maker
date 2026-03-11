package com.solyakov.playlist.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.solyakov.playlist.data.database.TableLinkEntity
import com.solyakov.playlist.data.database.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TableLinkDao {

    @Query("""
        SELECT tracks.* FROM tracks
        INNER JOIN table_link ON tracks.trackId = table_link.trackId
        WHERE table_link.playlistId = :playlistId
    """)
    fun getTracksForPlaylist(playlistId: Long): Flow<List<TrackEntity>>

    @Query("Delete from table_link WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun deleteLink(trackId: Long, playlistId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLink(link: TableLinkEntity)

    @Query("SELECT COUNT(*) FROM table_link WHERE playlistId = :playlistId")
    fun getTracksCount(playlistId: Long): Flow<Int>

}