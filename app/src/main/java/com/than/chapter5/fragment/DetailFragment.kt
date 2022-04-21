package com.than.chapter5.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.than.chapter5.R
import com.than.chapter5.databinding.FragmentDetailBinding
import com.than.chapter5.viewmodel.DetailViewModel

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var viewModel: DetailViewModel
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
        viewModel = ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)
        val id = args.id
        viewModel.getDataCovid(id)
        viewModel.countryDetail.observe(viewLifecycleOwner) {
            binding.apply {
                when (it.country) {
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
                    .load(it.countryInfo.flag)
                    .centerCrop()
                    .into(ivFlag)
                tvCountry.text = it.country
                tvAllCasesNumber.text = it.cases.toString()
                tvActiveNumber.text = it.deaths.toString()
                tvRecoveredNumber.text = it.recovered.toString()
                tvDeathNumber.text = it.deaths.toString()
            }
        }
    }
}