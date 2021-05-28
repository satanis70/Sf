package com.ermilov.sf.account.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ermilov.sf.account.room.RoomModelCity

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg roomModelCity: RoomModelCity)

    @Query("SELECT * FROM city")
    fun getCity(): LiveData<RoomModelCity>
}