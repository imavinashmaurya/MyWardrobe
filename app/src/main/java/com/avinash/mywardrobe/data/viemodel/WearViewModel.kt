package com.avinash.mywardrobe.data.viemodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.avinash.mywardrobe.data.repository.WearRepository
import com.avinash.mywardrobe.data.room.WearData
import com.avinash.mywardrobe.utility.Constant

class WearViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private var wearRepository: WearRepository? = null

    init {
        wearRepository = WearRepository.getInstance()
    }

    fun insertWearData(wearData: WearData) {
        wearRepository?.insertWearData(wearData, context)
    }

    fun updateWearData(wearData: WearData) {
        wearRepository?.updateWearData(wearData, context)
    }

    fun deleteWearData(wearData: WearData) {
        wearRepository?.deleteWearData(wearData, context)
    }

    fun getTopWear(): LiveData<List<WearData>>? {
        return wearRepository?.getTopWear(context)
    }

    fun getBottomWear(): LiveData<List<WearData>>? {
        return wearRepository?.getBottomWear(context)
    }

    fun getCount(): LiveData<Int>? {
        return wearRepository?.getCount(context)
    }

    fun getTopCount(): LiveData<Int>? {
        return wearRepository?.getCountByType(Constant.TOP_WEAR, context)
    }

    fun getBottomCount(): LiveData<Int>? {
        return wearRepository?.getCountByType(Constant.BOTTOM_WEAR, context)
    }

    fun getCurrentTopWear(): MutableLiveData<WearData>? {
        return wearRepository?.getCurrentTopWear()
    }

    fun getCurrentBottomWear(): MutableLiveData<WearData>? {
        return wearRepository?.getCurrentBottomWear()
    }

    fun getShuffleEvent(): MutableLiveData<Boolean>? {
        return wearRepository?.getShuffleEvent()
    }

    fun getDataChangedEvent(): MutableLiveData<Boolean>? {
        return wearRepository?.getDataChangedEvent()
    }
}