package com.than.chapter5.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.than.chapter5.R
import com.than.chapter5.database.CovidDatabase
import com.than.chapter5.databinding.FragmentDetailBinding
import com.than.chapter5.datastore.DataStoreManager
import com.than.chapter5.model.Favorite
import com.than.chapter5.viewmodel.DetailViewModel
import com.than.chapter5.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private var covidDatabase: CovidDatabase? = null
    private lateinit var viewModel: DetailViewModel
    private lateinit var pref: DataStoreManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        covidDatabase = CovidDatabase.getInstance(requireContext())
        pref = DataStoreManager(requireContext())
        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref))[DetailViewModel::class.java]
        var idUser = 99999

        viewModel.getDataUser().observe(viewLifecycleOwner){
            idUser = it.id_user!!
        }

        val id = args.id
        viewModel.getDataCovid(id)
        viewModel.countryDetail.observe(viewLifecycleOwner) { dataCovid ->
            binding.apply {
                when (dataCovid.country) {
                    "Jamaica" -> {
                        ivRapli.setImageResource(R.drawable.rapli)
                        ivRapli.visibility = View.VISIBLE
                    }
                    "S. Korea" -> {
                        ivRapli.setImageResource(R.drawable.taeyon)
                        ivRapli.visibility = View.VISIBLE
                    }
                    else -> {
                        ivRapli.visibility = View.GONE
                    }
                }
                Glide.with(requireContext())
                    .load(dataCovid.countryInfo.flag)
                    .centerCrop()
                    .into(ivFlag)
                tvCountry.text = dataCovid.country
                tvAllCasesNumber.text = dataCovid.cases.toString()
                tvActiveNumber.text = dataCovid.deaths.toString()
                tvRecoveredNumber.text = dataCovid.recovered.toString()
                tvDeathNumber.text = dataCovid.deaths.toString()


                lifecycleScope.launch(Dispatchers.IO){
                    val cekFavorite = covidDatabase?.favoriteDao()?.cekFavorite(idUser, dataCovid.country)
                    if (cekFavorite == true){
                        val getDataFavorite = covidDatabase?.favoriteDao()?.getFavoriteByIdAndCountry(idUser, dataCovid.country)
                        if (getDataFavorite!!.country_name == dataCovid.country){
                            runBlocking(Dispatchers.Main) {
                                btnFavorite.setImageResource(R.drawable.ic_baseline_favorite)
                            }
                        }
                    } else {
                        btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border)
                    }
                }
                btnFavorite.setOnClickListener{
                    lifecycleScope.launch(Dispatchers.IO){
                        val cekFavorite = covidDatabase?.favoriteDao()?.cekFavorite(idUser, dataCovid.country)
                        if (cekFavorite == true){
                            val getDataFavorite = covidDatabase?.favoriteDao()?.getFavoriteByIdAndCountry(idUser, dataCovid.country)
                            if (getDataFavorite!!.country_name == dataCovid.country){
                                val favorite = Favorite(
                                    getDataFavorite.id_favorite,
                                    getDataFavorite.id_user,
                                    getDataFavorite.country_name
                                )
                                val deleteFavorite = covidDatabase?.favoriteDao()?.deleteFavorite(favorite)
                                if (deleteFavorite != 0){
                                    runBlocking(Dispatchers.Main){
                                        Toast.makeText(requireContext(), "${dataCovid.country} Tidak jadi Favorite", Toast.LENGTH_SHORT).show()
                                        btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border)
                                    }
                                }
                            }
                        } else {
                            val favorite = Favorite(
                                null,
                                idUser,
                                dataCovid.country
                            )
                            val tambahFavorite = covidDatabase?.favoriteDao()?.addFavorite(favorite)
                            if (tambahFavorite != 0.toLong()){
                                runBlocking(Dispatchers.Main){
                                    Toast.makeText(requireContext(), "${dataCovid.country} jadi Favorite", Toast.LENGTH_SHORT).show()
                                    btnFavorite.setImageResource(R.drawable.ic_baseline_favorite)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}