package com.than.chapter5.service



import com.than.chapter5.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("covid-19/countries?sort=cases")
    fun getAllDataCovid(): Call<List<GetAllDataCovidResponse>>

    @GET("covid-19/countries/{country}")
    fun getDataCovidById(@Path("country") country: String): Call<GetAllDataCovidResponse>

    @GET("covid-19/all")
    fun getAllData(): Call<GetAllData>
}