package com.solyakov.playlist.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

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
    ],
    indices = [
        Index("trackId"),
        Index("playlistId")
    ]
    )
class TableLinkEntity(
    val trackId: Long,
    val playlistId: Long
)