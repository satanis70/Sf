package com.ermilov.sf.account.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ermilov.sf.account.model.RoomModelCity

@Database(entities = [RoomModelCity::class], version = 1)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}