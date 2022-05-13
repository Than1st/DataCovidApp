package com.than.chapter5.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    var id_favorite: Int?,
    var id_user: Int,
    var country_name: String,
    var cases: Int,
    var image: String
): Parcelable
