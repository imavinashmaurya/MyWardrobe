package com.avinash.mywardrobe.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WearDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWear(wear: WearData): Long

    @Query("SELECT * FROM ImageTable")
    fun getAllWear(): LiveData<List<WearData>>

    @Query("SELECT * FROM ImageTable WHERE type LIKE :type")
    fun getWearsByType(type: String): LiveData<List<WearData>>

    @Update
    suspend fun updateWear(wear: WearData)

    @Delete
    suspend fun deleteWear(wear: WearData)

    @Query("SELECT COUNT(*) FROM ImageTable")
    fun getCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM ImageTable WHERE type LIKE :type")
    fun getTypeCount(type: String): LiveData<Int>
}