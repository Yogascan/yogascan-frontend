package com.dicoding.yogascan.ui.splashscreen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.yogascan.R
import com.dicoding.yogascan.databinding.SplashScreenBinding
import com.dicoding.yogascan.ui.main.MainActivity
import com.dicoding.yogascan.ui.signin.SignInActivity

class SplashScreen : AppCompatActivity() {
    private var _binding : SplashScreenBinding? = null
    private val binding get() = _binding

    private val SPLASH_TIME_OUT: Long = 2500

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val animasi : Animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation)
        binding?.ivLogoSplashScreen?.startAnimation(animasi)

        Handler().postDelayed({
            if (isInternetAvailable()) {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)

                finish()
            }else{
            }
        }, SPLASH_TIME_OUT)

    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}