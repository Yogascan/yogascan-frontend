package com.dicoding.yogascan.ui.detail

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.dicoding.yogascan.R
import com.dicoding.yogascan.ViewModelFactory
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.databinding.ActivityDetailBinding
import com.dicoding.yogascan.ui.scan.ScanActivity

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val id = intent.getStringExtra(KEY)

        val toolbar: Toolbar = binding.topAppBar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (id != null) {
            getData(id)
        }
    }

    private fun getData(id: String) {
        viewModel.getSession().observe(this){userData ->
            val uid = userData.uid
            viewModel.getUserFavorites(uid).observe(this){result ->
                when(result){
                    is ResultState.Success -> {
                        showLoading(false)

                        binding.favToggle.isChecked = result.data.favorites.any { posesItem ->
                            posesItem.poseId == id
                        }

                        binding.favToggle.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                viewModel.addFavoritePose(uid, id).observe(this){ result ->
                                    when(result){
                                        is ResultState.Success -> showResponse(result.data.message)
                                        is ResultState.Error -> {
                                            showResponse(result.message)
                                            binding.favToggle.isChecked = false
                                        }
                                        is ResultState.Loading -> Log.d("DETAIL", "loading.....")
                                    }
                                }
                            } else {
                                viewModel.deleteUserFavorite(uid, id).observe(this){result ->
                                    when(result){
                                        is ResultState.Success -> showResponse(result.data.message)
                                        is ResultState.Error -> {
                                            showResponse(result.message)
                                            binding.favToggle.isChecked = true
                                        }
                                        is ResultState.Loading -> Log.d("DETAIL", "loading.....")
                                    }
                                }
                            }
                        }
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                    }
                    ResultState.Loading -> showLoading(true)
                }
            }
        }
        viewModel.getDetailPose(id).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    showLoading(false)

                    val poseResponse = result.data.pose
                    setDetailPose(
                        poseResponse.poseName,
                        poseResponse.poseDescription,
                        poseResponse.poseBenefits,
                        poseResponse.poseImage
                    )
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showError(result.message)
                }
            }
        }
    }

    private fun setDetailPose(
        poseName: String,
        poseDescription: String,
        poseBenefits: String,
        poseImage: String
    ) {
        val capitalizedPoseName = poseName.uppercase()

        binding.nameDetail.text = capitalizedPoseName
        binding.descText.text = poseDescription
        binding.benText.text = poseBenefits
        Glide.with(this).load(poseImage).into(binding.imgPose)

        binding.favToggle.setOnClickListener {  }


        binding.btnStart.setOnClickListener {
            val context = binding.btnStart.context
            val intent = Intent(context, ScanActivity::class.java).apply {
                putExtra(ScanActivity.IMAGE, poseImage)
            }
            context.startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showResponse(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val KEY = "key"
    }
}
