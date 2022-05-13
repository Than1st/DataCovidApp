package com.than.chapter5.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.than.chapter5.R
import com.than.chapter5.adapter.MainAdapter
import com.than.chapter5.database.CovidDatabase
import com.than.chapter5.databinding.FragmentHomeBinding
import com.than.chapter5.datastore.DataStoreManager
import com.than.chapter5.model.GetAllData
import com.than.chapter5.model.GetAllDataCovidResponse
import com.than.chapter5.service.ApiClient
import com.than.chapter5.viewmodel.HomeViewModel
import com.than.chapter5.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var covidDatabase: CovidDatabase? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var pref: DataStoreManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = DataStoreManager(requireActivity())
        homeViewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref))[HomeViewModel::class.java]
        covidDatabase = CovidDatabase.getInstance(requireContext())
        homeViewModel.getDataUser().observe(viewLifecycleOwner){
            binding.tvWelcome.text = getString(R.string.welcome, it.nama)
        }

        binding.toolbar.setOnClickListener {
            homeViewModel.getDataUser().observe(viewLifecycleOwner){
                AlertDialog.Builder(requireContext())
                    .setTitle("User Profile")
                    .setMessage("""
                Nama: ${it.nama}
                Email: ${it.email}
                Username: ${it.username}
            """.trimIndent())
                    .setNeutralButton("Cancel"){dialog, _ ->
                    dialog.dismiss()
                }
                    .setPositiveButton("Logout"){dialog, _ ->
                        AlertDialog
                            .Builder(requireContext())
                            .setTitle("Konfirmasi Logout")
                            .setMessage("Anda yakin mau keluar?")
                            .setNeutralButton("Tidak"){dialog, _ -> dialog.dismiss()}
                            .setPositiveButton("Iya"){ _, _ ->
                                lifecycleScope.launch(Dispatchers.IO) {
                                    pref.deleteUser()
                                }
                                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                            }
                            .create()
                            .show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Edit Profile"){ _, _ ->
                        val edit = HomeFragmentDirections.actionHomeFragmentToEditFragment(it)
                        findNavController().navigate(edit)
                    }
                    .create().show()
            }

        }
        getCases()
        fetchAllData()
    }


    private fun getCases() {
        ApiClient.instance.getAllData().enqueue(object : Callback<GetAllData> {
            override fun onResponse(call: Call<GetAllData>, response: Response<GetAllData>) {
                binding.tvAllCases.text = getString(R.string.jumlah_kasus_seluruh_dunia_s, response.body()?.cases.toString())
            }

            override fun onFailure(call: Call<GetAllData>, t: Throwable) {
                Toast.makeText(requireContext(), "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchAllData() {
        ApiClient.instance.getAllDataCovid()
            .enqueue(object : Callback<List<GetAllDataCovidResponse>> {
                override fun onResponse(
                    call: Call<List<GetAllDataCovidResponse>>,
                    response: Response<List<GetAllDataCovidResponse>>
                ) {
                    val body = response.body()
                    val code = response.code()
                    if (code == 200) {
                        showList(body)
                        binding.pbMain.visibility = View.GONE
                    } else {
                        binding.pbMain.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<List<GetAllDataCovidResponse>>, t: Throwable) {
                    binding.pbMain.visibility = View.GONE
                }

            })
    }
    private fun showList(data: List<GetAllDataCovidResponse>?) {
        val adapter = MainAdapter(object : MainAdapter.OnClickListener {
            override fun onClickItem(data: GetAllDataCovidResponse) {
                val toDetail = HomeFragmentDirections.actionHomeFragmentToDetailFragment(data.countryInfo.iso3)
                findNavController().navigate(toDetail)
            }
        })
        adapter.submitData(data)
        binding.rvMain.adapter = adapter
    }

}