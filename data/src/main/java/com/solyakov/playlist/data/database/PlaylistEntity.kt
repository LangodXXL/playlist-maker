package com.solyakov.playlist.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist",
    indices = [Index(value = ["name"], unique = true)]
)
class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0,
    val name: String,
    val description: String,
    val image: String?
)