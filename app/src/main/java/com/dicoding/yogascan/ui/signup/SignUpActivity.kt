package com.dicoding.yogascan.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
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

        viewModel = getViewModel(this)

        binding.signInTv.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
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
                    showAlertDialog("Signup Successful", "Time To Login", ){
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

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

    private fun getViewModel(appCompatActivity: AppCompatActivity) : SignupViewModel{
        val factory = ViewModelFactory.getInstance(appCompatActivity)
        return ViewModelProvider(appCompatActivity, factory)[SignupViewModel::class.java]
    }
}
