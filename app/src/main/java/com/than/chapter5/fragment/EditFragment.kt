package com.than.chapter5.fragment

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.than.chapter5.utils.PermissionUtils
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
    private var uri: String = "no_image"
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

        binding.profileImage.setOnClickListener{
            if (PermissionUtils.isPermissionsGranted(requireActivity(), getRequiredPermission())) {
                openGallery()
            }
        }

        val dataUser = args.user
        binding.apply {
            etNama.setText(dataUser.nama)
            etEmail.setText(dataUser.email)
            etUsername.setText(dataUser.username)
            etPassword.setText(dataUser.password)
            loadImage(Uri.parse(dataUser.image))
        }
        uri = dataUser.image
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
                        binding.etPassword.text.toString(),
                        uri
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
                        if (result != 0) {
                            viewModel.setDataUser(user)
                        }
                    }
                }
            }
        }
    }


    private fun loadImage(uri: Uri) {
        Log.d("Cek URI", uri.toString())
        binding.profileImage.setImageURI(uri)
//        val s: String = mUri.toString()
//        val mUri = Uri.parse(s)
    }

    private fun openGallery() {
        activity?.intent?.type = "image/*"
        galleryResult.launch("image/*")
        return
    }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                uri = result.toString()
                loadImage(result)
            }
        }

    private fun getRequiredPermission(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }
    }
}