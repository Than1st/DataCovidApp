package com.than.chapter5

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.than.chapter5.model.GetLyricResponse
import com.than.chapter5.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {
    val lirik: MutableLiveData<GetLyricResponse> = MutableLiveData()

    fun getLirik(id: String) {
        ApiClient.instance.getLyricsById(id).enqueue(object : Callback<GetLyricResponse> {
            override fun onResponse(
                call: Call<GetLyricResponse>,
                response: Response<GetLyricResponse>
            ) {
                lirik.postValue(response.body())
            }

            override fun onFailure(call: Call<GetLyricResponse>, t: Throwable) {
                Log.d("DetailViewModel", "${t.message}")
            }

        })
    }
}