package com.ermilov.sf.account.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityViewModel(application: Application): AndroidViewModel(application) {

    val readCity: LiveData<RoomModelCity>
    private val repository: CityRepository

    init {
        val cityDao = CityDatabase.getDatabase(application).cityDao()
        repository = CityRepository(cityDao)
        readCity = repository.readCity
    }

    fun addCity(roomModelCity: RoomModelCity){
        viewModelScope.launch(Dispatchers.IO){
            repository.addCity(roomModelCity)
        }
    }


}