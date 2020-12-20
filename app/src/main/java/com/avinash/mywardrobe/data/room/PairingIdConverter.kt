package com.avinash.mywardrobe.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PairingIdConverter {

    /**
     * Convert a PairingId object to a Json
     */
    @TypeConverter
    fun fromObjectToJson(stat: HashSet<Int>): String {
        return Gson().toJson(stat)
    }

    /**
     * Convert a json to a PairingId object
     */
    @TypeConverter
    fun toObject(json: String): HashSet<Int> {
        val notesType = object : TypeToken<HashSet<Int>>() {}.type
        return Gson().fromJson<HashSet<Int>>(json, notesType)
    }
}