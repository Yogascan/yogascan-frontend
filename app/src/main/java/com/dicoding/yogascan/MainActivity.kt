package com.dicoding.yogascan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val historyFragment = HistoryFragment()
        val favoriteFragment = FavoriteFragment()
        val bottomNavigationView: BottomNavigationView

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        SetCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.Home -> SetCurrentFragment(homeFragment)
                R.id.Profile -> SetCurrentFragment(profileFragment)
                R.id.History -> SetCurrentFragment(historyFragment)
                R.id.Favorite -> SetCurrentFragment(favoriteFragment)
            }
            true
        }
    }

    private fun SetCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}