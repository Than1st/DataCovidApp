package com.than.chapter5.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.than.chapter5.datastore.DataStoreManager
import com.than.chapter5.model.GetAllDataCovidResponse
import com.than.chapter5.model.User
import com.than.chapter5.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val pref: DataStoreManager): ViewModel() {
    val countryDetail: MutableLiveData<GetAllDataCovidResponse> = MutableLiveData()

    fun getDataCovid(id: String) {
        ApiClient.instance.getDataCovidById(id).enqueue(object : Callback<GetAllDataCovidResponse> {
            override fun onResponse(
                call: Call<GetAllDataCovidResponse>,
                response: Response<GetAllDataCovidResponse>
            ) {
                countryDetail.postValue(response.body())
            }

            override fun onFailure(call: Call<GetAllDataCovidResponse>, t: Throwable) {
                Log.d("DetailViewModel", "${t.message}")
            }

        })
    }

    fun getDataUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }
}