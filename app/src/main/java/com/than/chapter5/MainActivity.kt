package com.than.chapter5

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.than.chapter5.DetailActivity.Companion.ID
import com.than.chapter5.adapter.MainAdapter
import com.than.chapter5.databinding.ActivityMainBinding
import com.than.chapter5.model.Data
import com.than.chapter5.model.GetAllSong
import com.than.chapter5.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchAllGetAllSong()
        binding.fabAdd.setOnClickListener {
            fetchAllGetAllSong()
        }
//        val myJson = JSONObject()
//        myJson.put("id", 1)
//        myJson.put("nama_pengutang", "Pack Rafly")
//        myJson.put("jumlah_hutang", 50000)
//        myJson.put("lunas", false)
//        myJson.put("bunga_hutang", null)
//        val arrayBarang = JSONArray()
//        myJson.put("list_barang",
//            arrayBarang
//                .put(JSONObject()
//                    .put("id_barang", 1)
//                    .put("nama_barang", "L.A Bold")
//                    .put("jumlah_barang", 1)
//                    .put("harga_barang", 21000)
//                ).put(JSONObject()
//                    .put("id_barang", 2)
//                    .put("nama_barang", "Dunhill Bold")
//                    .put("jumlah_barang", 1)
//                    .put("harga_barang", 24000)
//                ).put(JSONObject()
//                    .put("id_barang", 3)
//                    .put("nama_barang", "Teh Pucuk")
//                    .put("jumlah_barang", 1)
//                    .put("harga_barang", 5000)
//                )
//        )
//        Log.d("cek JSON", myJson.toString())
    }

    private fun fetchAllGetAllSong() {
        ApiClient.instance.getAllCar()
            .enqueue(object : Callback<GetAllSong> {
                override fun onResponse(
                    call: Call<GetAllSong>,
                    response: Response<GetAllSong>
                ) {
                    val body = response.body()
                    val code = response.code()
                    if (code == 200) {
                        if (response.isSuccessful) {
                            body?.let { showList(it.data) }
                        }

                        binding.pbMain.visibility = View.GONE
                    } else {
                        binding.pbMain.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<GetAllSong>, t: Throwable) {
                    binding.pbMain.visibility = View.GONE
                }

            })
    }

    private fun showList(data: List<Data>) {
        val adapter = MainAdapter(object : MainAdapter.OnClickListener {
            override fun onClickItem(data: Data) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(ID, data.songId)
                startActivity(intent)
            }
        })
        adapter.submitData(data)
        binding.rvMain.adapter = adapter
    }

    //    fun getLyric(){
//        AlertDialog.Builder(baseContext)
//            .setNegativeButton("Batal"){dialog,_->
//                dialog.dismiss()
//            }
//            .setMessage("Anda ingin Logout?")
//            .setTitle(data.get(1).songTitle)
//            .create()
//            .show()
//    }

}