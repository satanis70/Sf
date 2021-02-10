package com.ermilov.sf.account.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ermilov.sf.account.model.RoomModelCity

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg roomModelCity: RoomModelCity)

    @Query("SELECT * FROM city")
    fun getCity(): RoomModelCity
}