package com.avinash.mywardrobe.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.avinash.mywardrobe.data.room.WearData
import com.avinash.mywardrobe.data.room.WearDataBase
import com.avinash.mywardrobe.utility.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WearRepository {

    companion object {
        private val mInstance = WearRepository()
        fun getInstance(): WearRepository {
            return mInstance
        }
    }

    private var currentTopWear: MutableLiveData<WearData> = MutableLiveData()
    private var currentBottomWear: MutableLiveData<WearData> = MutableLiveData()
    private var shuffleEvent: MutableLiveData<Boolean> = MutableLiveData()

    private fun getDataBase(context: Context): WearDataBase {
        return WearDataBase.getWearDataBase(context)
    }

    fun insertWearData(wearData: WearData, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataBase = getDataBase(context)
            dataBase.wearDao().insertWear(wearData)
        }
    }

    fun updateWearData(wearData: WearData, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataBase = getDataBase(context)
            dataBase.wearDao().updateWear(wearData)
        }
    }

    fun deleteWearData(wearData: WearData, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataBase = getDataBase(context)
            dataBase.wearDao().deleteWear(wearData)
        }
    }

    fun getTopWear(context: Context): LiveData<List<WearData>> {
        val dataBase = getDataBase(context)
        return dataBase.wearDao().getWearsByType(Constant.TOP_WEAR)
    }

    fun getBottomWear(context: Context): LiveData<List<WearData>> {
        val dataBase = getDataBase(context)
        return dataBase.wearDao().getWearsByType(Constant.BOTTOM_WEAR)
    }

    fun getCount(context: Context): LiveData<Int> {
        val dataBase = getDataBase(context)
        return dataBase.wearDao().getCount()
    }

    fun getCountByType(type: String, context: Context): LiveData<Int> {
        val dataBase = getDataBase(context)
        return dataBase.wearDao().getTypeCount(type)
    }

    fun getCurrentTopWear(): MutableLiveData<WearData> {
        return currentTopWear
    }

    fun getCurrentBottomWear(): MutableLiveData<WearData> {
        return currentBottomWear
    }
    fun getShuffleEvent(): MutableLiveData<Boolean> {
        return shuffleEvent
    }
}