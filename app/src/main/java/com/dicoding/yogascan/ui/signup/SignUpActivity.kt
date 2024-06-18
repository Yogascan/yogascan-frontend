package com.dicoding.yogascan.ui.signup

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.dicoding.yogascan.ui.signup.SignupViewModel
import com.dicoding.yogascan.R
import com.dicoding.yogascan.ViewModelFactory
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.databinding.ActivitySignupBinding
import com.dicoding.yogascan.ui.signin.SignInActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val name = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            signup(name, email, password)
        }
    }

    private fun signup(name: String, email: String, password: String) {
        viewModel.signup(name, email, password).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    showAlertDialog("Signup Successful", "Welcome to the app!", null)
                }
                is ResultState.Error -> {
                    showLoading(false)
                    showAlertDialog("Signup Failed", result.message ?: "An error occurred", null)
                }
            }
        }
    }

    private fun showAlertDialog(title: String, message: String, onPositiveButtonClick: (() -> Unit)?) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { _, _ -> onPositiveButtonClick?.invoke() }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}