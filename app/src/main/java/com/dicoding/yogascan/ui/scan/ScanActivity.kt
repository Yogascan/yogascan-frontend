package com.dicoding.yogascan.ui.scan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.yogascan.R
import com.dicoding.yogascan.ViewModelFactory
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.databinding.ScanBinding
import com.dicoding.yogascan.ui.history.HistoryFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ScanBinding
    private lateinit var imageCapture: ImageCapture
    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val executor = Executors.newSingleThreadExecutor()
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    private val REQUEST_CODE_PERMISSIONS = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val poseImage = intent.getStringExtra(IMAGE)
        Glide.with(this).load(poseImage).into(binding.tutorImg)
        binding.back.setOnClickListener {
            onBackPressed()
        }
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS
            )
        }
        viewModel.predictionResult.observe(this) { result ->
            result?.let { resultState ->
                when (resultState) {
                    is ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> {
                        showLoading(false)
                        val data = resultState.data // PredictionResponse
                        binding.detect.text = data.data.prediction
                        binding.result.text = "${"%.2f".format(data.data.confidence * 100)}%"
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        showError(resultState.message)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        baseContext, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
                startImageAnalysis()
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bitmapToFile(bitmap: Bitmap, context: Context): File {
        // Membuat file kosong di direktori cache aplikasi
        val filesDir = context.cacheDir
        val imageFile = File(filesDir, "image.jpg")

        // Mengonversi bitmap ke byte array
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()

        // Menulis byte array ke file image
        FileOutputStream(imageFile).use {
            it.write(byteArray)
        }

        return imageFile
    }

    private fun startImageAnalysis() {
        lifecycleScope.launch {
            while (true) {
                val photoFile = File(
                    externalMediaDirs.firstOrNull(),
                    "${System.currentTimeMillis()}.jpg"
                )
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(this@ScanActivity),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            lifecycleScope.launch {
                                val bitmap = photoFile.toBitmap()
                                photoFile.delete()

                                // Mengonversi bitmap ke file
                                val imageFile = bitmapToFile(bitmap, applicationContext)

                                // Memanggil viewModel.postPrediction dengan file yang telah dikonversi
                                viewModel.postPrediction(imageFile)
                            }
                        }
                    }
                )
                withContext(Dispatchers.IO) {
                    Thread.sleep(5000) // Adjust the interval as needed
                }
            }
        }
    }

    private fun File.toBitmap(): Bitmap {
        return BitmapFactory.decodeFile(this.path)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "ScanActivity"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        const val KEY = "key"
        const val IMAGE = "image"
    }
}