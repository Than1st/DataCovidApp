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
import com.than.chapter5.database.CovidDatabase
import com.than.chapter5.databinding.FragmentEditBinding
import com.than.chapter5.datastore.DataStoreManager
import com.than.chapter5.model.User
import com.than.chapter5.viewmodel.HomeViewModel
import com.than.chapter5.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private val args: EditFragmentArgs by navArgs()
    private var covidDatabase: CovidDatabase? = null
    private lateinit var viewModel: HomeViewModel
    private lateinit var pref: DataStoreManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        covidDatabase = CovidDatabase.getInstance(requireContext())
        pref = DataStoreManager(requireContext())
        viewModel =
            ViewModelProvider(requireActivity(), ViewModelFactory(pref))[HomeViewModel::class.java]
        val dataUser = args.user
        binding.apply {
            etNama.setText(dataUser.nama)
            etEmail.setText(dataUser.email)
            etUsername.setText(dataUser.username)
            etPassword.setText(dataUser.password)
        }
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_editFragment_to_homeFragment)
        }
        binding.btnEdit.setOnClickListener {
            when {
                binding.etNama.text.toString().isEmpty() ||
                        binding.etEmail.text.toString().isEmpty() ||
                        binding.etUsername.text.toString().isEmpty() ||
                        binding.etPassword.text.toString().isEmpty() -> {
                    Toast.makeText(requireContext(), "Form tidak boleh kosong!", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    val user = User(
                        dataUser.id_user,
                        binding.etNama.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etUsername.text.toString(),
                        binding.etPassword.text.toString()
                    )
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = covidDatabase?.userDao()?.updateUser(user)
                        runBlocking(Dispatchers.Main) {
                            if (result != 0) {
                                Toast.makeText(
                                    requireContext(),
                                    "Data berhasil di Update!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(R.id.action_editFragment_to_homeFragment)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Data gagal di Update!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        if (result != 0){
                            viewModel.setDataUser(user)
                        }
                    }
                }
            }
        }
    }
}