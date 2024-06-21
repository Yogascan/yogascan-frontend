package com.dicoding.yogascan.ui.profile

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dicoding.yogascan.R
import com.dicoding.yogascan.ViewModelFactory
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.response.SigninResponse
import com.dicoding.yogascan.databinding.FragmentProfileBinding
import com.dicoding.yogascan.ui.signin.SignInActivity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var uid : String

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val requestReadPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startGallery()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signout.setOnClickListener {
            startActivity(Intent(requireActivity(), SignInActivity::class.java))
            viewModel.logout()
            requireActivity().finish()
        }

        binding.btnEdit.setOnClickListener {
            checkAndRequestPermission()
        }

        viewModel.getSession().observe(viewLifecycleOwner, Observer { user: SigninResponse ->
            uid = user.uid
            getData(user.uid)
        })
    }

    private fun getData(uid: String) {
        viewModel.getProfile(uid).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }
                is ResultState.Success -> {
                    showLoading(false)

                    val profileResponse = result.data
                    val username = profileResponse.username
                    val email = profileResponse.email
                    val profilePhotoUrl = profileResponse.photoProfile ?: ""

                    Log.d("ProfileFragment", "Profile photo URL: $profilePhotoUrl")
                    setProfile(username, email, profilePhotoUrl)
                }
                is ResultState.Error -> {
                    showLoading(false)
                }
            }
        })
    }

    private fun checkAndRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                startGallery()
            }
            else -> {
                requestReadPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            binding.imgProfile.setImageURI(uri)
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            viewModel.updateProfilePicture(imageFile, uid).observe(viewLifecycleOwner){result ->
                when(result){
                    is ResultState.Success -> Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()
                    is ResultState.Error -> Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    is ResultState.Loading -> Log.d("Profile", "Loading...")

                }
            }
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    private fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile("today", ".jpg", filesDir)
    }

    private fun File.reduceFileImage(): File {
        val maximalSize = 1000000
        val file = this
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > maximalSize)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun setProfile(username: String?, email: String?, profilePhotoUrl: String) {
        binding.tvUsername.text = username
        binding.tvEmail.text = email
        Glide.with(this)
            .load(profilePhotoUrl)
            .circleCrop()
            .placeholder(R.drawable.img_1)
            .error(R.drawable.img_1)
            .into(binding.imgProfile)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
