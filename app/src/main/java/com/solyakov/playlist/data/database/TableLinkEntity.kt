package com.solyakov.playlist.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "table_link",
    primaryKeys = ["trackId", "playlistId"],
    foreignKeys = [
        ForeignKey(
            entity = TrackEntity::class,
            parentColumns = ["trackId"],
            childColumns = ["trackId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["playlistId"],
            childColumns = ["playlistId"],
            onDelete = CASCADE
        )
    ]
    )
class TableLinkEntity(
    val trackId: Long,
    val playlistId: Long
)