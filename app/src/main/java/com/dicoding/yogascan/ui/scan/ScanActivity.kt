package com.dicoding.yogascan.ui.scan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import com.dicoding.yogascan.ViewModelFactory
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.databinding.ScanBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private var totalConfidence = 0.0

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
//                        binding.detect.text = data.data.prediction
                        binding.result.text = "${"%.2f".format(data.data.confidence * 100)}%"
                        totalConfidence += data.data.confidence
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

            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(640, 480)) // Set a lower resolution
                .build()

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
        val filesDir = context.cacheDir
        val imageFile = File(filesDir, "image.jpg")

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream) // Adjust compression level
        val byteArray = stream.toByteArray()

        FileOutputStream(imageFile).use {
            it.write(byteArray)
        }

        return imageFile
    }

    private fun startImageAnalysis() {
        val poseAnalysisJob = Job()
        val poseId = intent.getStringExtra(KEY)
        viewModel.getSession().observe(this){
            lifecycleScope.launch(poseAnalysisJob + Dispatchers.IO){
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

                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                lifecycleScope.launch {
                                    val bitmap = photoFile.toBitmap()
                                    photoFile.delete()

                                    val imageFile = bitmapToFile(bitmap, applicationContext)
                                    Log.e("UID", it.uid)
                                    viewModel.postPrediction(imageFile, it.uid, poseId!!)
                                }
                            }
                        }
                    )
                    withContext(Dispatchers.IO) {
                        Thread.sleep(5000)
                    }
                }
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            poseAnalysisJob.cancel()
            Toast.makeText(this, "End of session", Toast.LENGTH_SHORT).show()
            finish()
        }, 30000)
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
        const val KEY = "key"
        const val IMAGE = "image"
    }
}
