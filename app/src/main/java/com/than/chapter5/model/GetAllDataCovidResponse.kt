package com.than.chapter5.model


import com.google.gson.annotations.SerializedName

data class GetAllDataCovidResponse(
    @SerializedName("active")
    val active: Int,
    @SerializedName("activePerOneMillion")
    val activePerOneMillion: Double,
    @SerializedName("cases")
    val cases: Int,
    @SerializedName("casesPerOneMillion")
    val casesPerOneMillion: Int,
    @SerializedName("continent")
    val continent: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("countryInfo")
    val countryInfo: CountryInfo,
    @SerializedName("critical")
    val critical: Int,
    @SerializedName("criticalPerOneMillion")
    val criticalPerOneMillion: Double,
    @SerializedName("deaths")
    val deaths: Int,
    @SerializedName("deathsPerOneMillion")
    val deathsPerOneMillion: Int,
    @SerializedName("oneCasePerPeople")
    val oneCasePerPeople: Int,
    @SerializedName("oneDeathPerPeople")
    val oneDeathPerPeople: Int,
    @SerializedName("oneTestPerPeople")
    val oneTestPerPeople: Int,
    @SerializedName("population")
    val population: Int,
    @SerializedName("recovered")
    val recovered: Int,
    @SerializedName("recoveredPerOneMillion")
    val recoveredPerOneMillion: Double,
    @SerializedName("tests")
    val tests: Int,
    @SerializedName("testsPerOneMillion")
    val testsPerOneMillion: Int,
    @SerializedName("todayCases")
    val todayCases: Int,
    @SerializedName("todayDeaths")
    val todayDeaths: Int,
    @SerializedName("todayRecovered")
    val todayRecovered: Int,
    @SerializedName("updated")
    val updated: Long
)

data class CountryInfo(
    @SerializedName("flag")
    val flag: String,
    @SerializedName("_id")
    val id: Int,
    @SerializedName("iso2")
    val iso2: String,
    @SerializedName("iso3")
    val iso3: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("long")
    val long: Double
)