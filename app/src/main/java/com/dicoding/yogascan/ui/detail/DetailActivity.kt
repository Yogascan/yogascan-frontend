package com.dicoding.yogascan.ui.detail

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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

    private var currentImageUri: Uri? = null
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
        viewModel.getDetailPose(id).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Tindakan yang dilakukan saat sedang memuat
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

    private fun showError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val KEY = "key"
        const val IMAGE = "image"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}