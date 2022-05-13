package com.than.chapter5.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.than.chapter5.R
import com.than.chapter5.database.CovidDatabase
import com.than.chapter5.databinding.FragmentRegisterBinding
import com.than.chapter5.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private var covidDatabase: CovidDatabase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        covidDatabase = CovidDatabase.getInstance(requireContext())
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.btnRegister.setOnClickListener {
            when{
                binding.etUsername.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty() || binding.etNama.text.toString().isEmpty() || binding.etEmail.text.toString().isEmpty() || binding.etConfirmPassword.text.toString().isEmpty() -> {
                    Toast.makeText(requireContext(), "Form tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                }
                binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString() -> {
                    Toast.makeText(requireContext(), "Password tidak sama!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val data = User(
                        null,
                        binding.etNama.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etUsername.text.toString(),
                        binding.etPassword.text.toString(),
                        "no_image"
                    )
                    lifecycleScope.launch(Dispatchers.IO){
                        val register = covidDatabase?.userDao()?.insertUser(data)
                        runBlocking(Dispatchers.Main){
                            if (register != 0.toLong()){
                                Toast.makeText(requireContext(), "Berhasil Registrasi", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                            } else {
                                Toast.makeText(requireContext(), "Gagal Registrasi", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

}