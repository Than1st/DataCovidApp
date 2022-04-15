package com.than.chapter5.model


import com.google.gson.annotations.SerializedName

data class GetAllSong(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("status")
    val status: Boolean
)

data class Data(
    @SerializedName("artist")
    val artist: String,
    @SerializedName("songId")
    val songId: String,
    @SerializedName("songLyrics")
    val songLyrics: String,
    @SerializedName("songTitle")
    val songTitle: String
)
