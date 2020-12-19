package com.avinash.mywardrobe.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WearData::class], version = 1, exportSchema = false)
@TypeConverters(PairingIdConverter::class)

abstract class WearDataBase : RoomDatabase() {

    abstract fun wearDao(): WearDao

    companion object {

        @Volatile
        private var INSTANCE: WearDataBase? = null

        fun getWearDataBase(context: Context): WearDataBase {
            if (INSTANCE != null) return INSTANCE!!
            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(context, WearDataBase::class.java, "WearDataBaseDB.db")
                    .fallbackToDestructiveMigration()
                    .build()
                return INSTANCE!!
            }
        }
    }
}