package com.than.chapter5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.than.chapter5.databinding.ActivityDetailBinding
import com.than.chapter5.model.GetLyricResponse
import com.than.chapter5.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.time.measureTimedValue

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    lateinit var viewModel: DetailViewModel
    companion object{
        const val ID = "id"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra(ID)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.getLirik(id.toString())
        viewModel.lirik.observe(this, Observer {
            binding.tvJudul.text = it.data.songTitle
        })
    }
//    fun getLirik(id: String) {
//        ApiClient.instance.getLyricsById(id).enqueue(object : Callback<GetLyricResponse> {
//            override fun onResponse(
//                call: Call<GetLyricResponse>,
//                response: Response<GetLyricResponse>
//            ) {
//                binding.tvJudul.text = response.body()!!.data.songTitle
//                binding.tvLirik.text = response.body()!!.data.songLyricsArr!!.joinToString(separator = "\n")
//                Toast.makeText(baseContext, "Success Get Lyric", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onFailure(call: Call<GetLyricResponse>, t: Throwable) {
//                Toast.makeText(this@DetailActivity, "${t.message}", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }
}