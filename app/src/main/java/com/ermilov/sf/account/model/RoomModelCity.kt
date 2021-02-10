package com.ermilov.sf.account.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class RoomModelCity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "city") val city: String?
)