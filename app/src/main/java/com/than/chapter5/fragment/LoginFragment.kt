package com.than.chapter5.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.than.chapter5.R
import com.than.chapter5.database.CovidDatabase
import com.than.chapter5.databinding.FragmentLoginBinding
import com.than.chapter5.datastore.DataStoreManager
import com.than.chapter5.model.User
import com.than.chapter5.viewmodel.HomeViewModel
import com.than.chapter5.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var covidDatabase: CovidDatabase? = null
    private lateinit var viewModel: HomeViewModel
    private lateinit var pref: DataStoreManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        covidDatabase = CovidDatabase.getInstance(requireContext())
        pref = DataStoreManager(requireContext())
        viewModel =
            ViewModelProvider(requireActivity(), ViewModelFactory(pref))[HomeViewModel::class.java]
        cekLogin()
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener {
            Log.d("Cek udah login", "mencet login")
            when {
                binding.etUsername.text.toString().isEmpty() || binding.etPassword.text.toString()
                    .isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Form tidak boleh Kosong!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Log.d("Cek udah login", "else")
                    val username = binding.etUsername.text.toString()
                    val password = binding.etPassword.text.toString()
                    lifecycleScope.launch(Dispatchers.IO) {
                        Log.d("Cek udah login", "IO")
                        val getUser = covidDatabase?.userDao()?.getUser(
                            username,
                            password
                        )
                        Log.d("Cek udah login", "val getuser")
                        activity?.runOnUiThread {
                            Log.d("Cek udah login", "Main")
                            if (getUser != null) {
                                Toast.makeText(requireContext(), "Login Berhasil", Toast.LENGTH_SHORT).show()
                                Log.d("Cek udah login", "Udah Login")
                                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            } else {
                                Log.d("Cek udah login", "gagal login")
                                Toast.makeText(requireContext(), "Username / Password salah!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        if (getUser != null) {
                            viewModel.setDataUser(getUser)
                        }
                    }
                }
            }
        }
    }


    private fun cekLogin() {
        viewModel.apply {
            getDataUser().observe(viewLifecycleOwner) {
                if (it.id_user != -1) {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }

}
