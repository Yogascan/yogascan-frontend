package com.dicoding.yogascan.ui.home

import androidx.lifecycle.ViewModel
import com.dicoding.yogascan.data.repository.PoseRepository

class HomeViewModel(private val repository: PoseRepository) : ViewModel() {

    fun getPoses(uid: String?) = repository.getPoses(uid)

}