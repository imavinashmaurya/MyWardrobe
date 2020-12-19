package com.avinash.mywardrobe.data.room

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "ImageTable")
data class WearData(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "image")
    var image: String? = null,
    @ColumnInfo(name = "type")
    var type: String? = null,
    @ColumnInfo(name = "pairingIdList")
    @TypeConverters(PairingIdConverter::class)
    var pairingIdList: HashSet<Int>? = HashSet()
)