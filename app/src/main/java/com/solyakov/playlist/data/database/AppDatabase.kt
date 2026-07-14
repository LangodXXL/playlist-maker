package com.solyakov.playlist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.solyakov.playlist.data.dao.PlaylistsDao
import com.solyakov.playlist.data.dao.TableLinkDao
import com.solyakov.playlist.data.dao.TracksDao

@Database(
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        TableLinkEntity::class
    ], version = 3, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun TracksDao(): TracksDao
    abstract fun PlaylistsDao(): PlaylistsDao
    abstract fun TableLinkDao(): TableLinkDao

}