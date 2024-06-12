package com.dicoding.yogascan.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.yogascan.ui.favorite.FavoriteFragment
import com.dicoding.yogascan.ui.home.HomeFragment
import com.dicoding.yogascan.HistoryFragment
import com.dicoding.yogascan.ProfileFragment
import com.dicoding.yogascan.R

import com.dicoding.yogascan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigationView()

        if (savedInstanceState == null) {
            // Set default fragment
            replaceFragment(HomeFragment())
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.History -> {
                    replaceFragment(HistoryFragment())
                    true
                }
                R.id.Favorite -> {
                    replaceFragment(FavoriteFragment())
                    true
                }
                R.id.Profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .commit()
    }
}
