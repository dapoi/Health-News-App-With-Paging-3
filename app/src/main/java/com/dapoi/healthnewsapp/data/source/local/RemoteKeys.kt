package com.dapoi.healthnewsapp.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val id: String,
    val nextPageKey: Int?,
    val prevPageKey: Int?
)
