package com.dicoding.yogascan.data.di

import android.content.Context
import com.dicoding.yogascan.api.ApiConfig
import com.dicoding.yogascan.data.pref.UserPreferences
import com.dicoding.yogascan.data.pref.dataStore
import com.dicoding.yogascan.data.repository.PoseRepository
import com.dicoding.yogascan.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }

    fun providePoseRepository(context: Context): PoseRepository {
        val apiService = ApiConfig.getApiService()
        return PoseRepository.getInstance(apiService)
    }
}