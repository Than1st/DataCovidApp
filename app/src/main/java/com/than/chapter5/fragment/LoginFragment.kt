package com.than.chapter5.fragment

import android.content.Context
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
import com.than.chapter5.databinding.FragmentLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var covidDatabase: CovidDatabase? = null
    companion object{
        const val SHARED_FILE = "kotlinsharedpreferences"
    }
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
        val sharedPreferences = requireContext().getSharedPreferences(SHARED_FILE, Context.MODE_PRIVATE)
        val cekUsername = sharedPreferences.getString("username", "default_username")
        if (cekUsername != "default_username"){
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val login = covidDatabase?.userDao()?.loginUser(binding.etUsername.text.toString(), binding.etPassword.text.toString())
                runBlocking(Dispatchers.Main){
                    when {
                        binding.etUsername.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty() -> {
                            Toast.makeText(requireContext(), "Form tidak boleh Kosong!", Toast.LENGTH_SHORT).show()
                        }
                        login == true-> {
                            val editor = sharedPreferences.edit()
                                editor.putString("username", binding.etUsername.text.toString())
                                editor.putString("password", binding.etPassword.text.toString())
                                editor.apply()

                            Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                        else -> {
                            Toast.makeText(requireContext(), "Username/Password Salah!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

}
