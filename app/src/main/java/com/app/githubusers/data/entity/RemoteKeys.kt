package com.app.githubusers.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val id: Int,
    val nextKey: Int?,
    val isEndReached: Boolean
)