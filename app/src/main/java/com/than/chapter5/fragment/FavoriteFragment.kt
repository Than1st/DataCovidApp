package com.than.chapter5.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.than.chapter5.R
import com.than.chapter5.adapter.FavoriteAdapter
import com.than.chapter5.database.CovidDatabase
import com.than.chapter5.databinding.FragmentFavoriteBinding
import com.than.chapter5.datastore.DataStoreManager
import com.than.chapter5.model.Favorite
import com.than.chapter5.viewmodel.HomeViewModel
import com.than.chapter5.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private var covidDatabase: CovidDatabase? = null
    private val args: FavoriteFragmentArgs by navArgs()
    private lateinit var viewModel: HomeViewModel
    private lateinit var pref: DataStoreManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = DataStoreManager(requireContext())
        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref))[HomeViewModel::class.java]
        covidDatabase = CovidDatabase.getInstance(requireContext())
        viewModel.getDataUser().observe(viewLifecycleOwner){
            binding.tvWelcome.text = getString(R.string.favorite, it.nama)
        }
        val idUser = args.idUser
        getFavorite(idUser)
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_favoriteFragment_to_homeFragment)
        }
    }

    private fun getFavorite(idUser: Int) {
        lifecycleScope.launch(Dispatchers.IO){
            val dataFavorite = covidDatabase?.favoriteDao()?.getFavorite(idUser)
            if (dataFavorite != null){
                runBlocking(Dispatchers.Main){
                    showList(dataFavorite)
                    binding.pbMain.visibility = View.GONE
                }
            } else {
                runBlocking(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Gagal Ambil Favorite!", Toast.LENGTH_SHORT).show()
                    binding.pbMain.visibility = View.GONE
                }
            }
        }
//        val dataFav : MutableList<Favorite> = mutableListOf()
//        val dataFavorite : MutableList<GetAllDataCovidResponse> = mutableListOf()
//        if (idUser != -1){
//            lifecycleScope.launch(Dispatchers.IO){
//                val getFavorite = covidDatabase?.favoriteDao()?.getFavorite(idUser)
//                if (getFavorite != null) {
//                        dataFav.add(getFavorite)
//                        for (a in 0 until dataFav.size){
//                            dataFav.add(getFavorite)
//                            viewModel.setDataCovid(dataFav[a].country_name)
//                        }
//                    }
//                }
//        } else {
//            Toast.makeText(requireContext(), "Tidak Ada id_User", Toast.LENGTH_SHORT).show()
//        }
//        showList(dataFavorite)
    }

    private fun showList(list: List<Favorite>) {
        val adapter = FavoriteAdapter(object : FavoriteAdapter.OnClickListener{
            override fun onClickItem(data: Favorite) {
                val toDetail = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(data.country_name)
                findNavController().navigate(toDetail)
            }
        })
        adapter.submitData(list)
        binding.rvMain.adapter = adapter
    }

}