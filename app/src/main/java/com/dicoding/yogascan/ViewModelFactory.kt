package com.dicoding.yogascan

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.yogascan.data.repository.PoseRepository
import com.dicoding.yogascan.data.di.Injection
import com.dicoding.yogascan.data.repository.UserRepository
import com.dicoding.yogascan.ui.detail.DetailViewModel
import com.dicoding.yogascan.ui.favorite.FavoriteViewModel
import com.dicoding.yogascan.ui.history.HistoryViewModel
import com.dicoding.yogascan.ui.home.HomeViewModel
import com.dicoding.yogascan.ui.profile.ProfileViewModel
import com.dicoding.yogascan.ui.scan.ScanViewModel
import com.dicoding.yogascan.ui.signin.SigninViewModel
import com.dicoding.yogascan.ui.signup.SignupViewModel

class ViewModelFactory private constructor(
    private val poseRepository: PoseRepository,
    private val userRepository: UserRepository
) :
    ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(poseRepository) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(poseRepository) as T
            }

            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(poseRepository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(poseRepository) as T
            }

            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(poseRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.providePoseRepository(context),
                    (Injection.provideRepository(context))
                )
            }
    }
}
