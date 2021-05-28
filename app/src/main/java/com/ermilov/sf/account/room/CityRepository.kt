package com.ermilov.sf.account.room

import androidx.lifecycle.LiveData

class CityRepository(private val cityDao: CityDao) {

    val readCity: LiveData<RoomModelCity> = cityDao.getCity()

    suspend fun addCity(roomModelCity: RoomModelCity){
        cityDao.insert(roomModelCity)
    }
}