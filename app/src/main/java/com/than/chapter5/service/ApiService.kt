package com.than.chapter5.service



import com.than.chapter5.model.DataLyric
import com.than.chapter5.model.GetAllSong
import com.than.chapter5.model.GetLyricResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ApiService {
    @GET("hot")
    fun getAllCar(): Call<GetAllSong>

    @GET("lyrics/{id}")
    fun getLyricsById(@Path("id") id: String): Call<GetLyricResponse>
}