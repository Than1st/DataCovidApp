package com.than.chapter5.model


import com.google.gson.annotations.SerializedName

data class GetLyricResponse(
    @SerializedName("data")
    val data: DataLyric,
    @SerializedName("status")
    val status: Boolean
)

data class DataLyric(
    @SerializedName("artist")
    val artist: String? = "Dummy Artis",
    @SerializedName("songLyrics")
    val songLyrics: String? = "Dummy Lirik",
    @SerializedName("songLyricsArr")
    val songLyricsArr: List<String>? = (listOf("kosong")),
    @SerializedName("songTitle")
    val songTitle: String? = "Dummy Judul"
)