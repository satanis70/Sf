package com.ermilov.sf.account.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ermilov.sf.account.room.RoomModelCity

@Database(entities = [RoomModelCity::class], version = 1)

abstract class CityDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object{
        @Volatile
        private var INSTANSE: CityDatabase? = null

        fun getDatabase(context: Context): CityDatabase{
            val tempInstance = INSTANSE
            if (tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, CityDatabase::class.java, "city-database").build()
                INSTANSE = instance
                return instance
            }
        }
    }

}