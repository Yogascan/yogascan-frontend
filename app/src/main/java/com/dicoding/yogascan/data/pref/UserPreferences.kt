package com.dicoding.yogascan.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.yogascan.data.response.SigninResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: SigninResponse) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = user.uid
        }
    }
    
    fun getSession(): Flow<SigninResponse> {
        return dataStore.data.map { preferences ->
            SigninResponse(
                preferences[USER_ID] ?: "",
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null
        private val USER_ID = stringPreferencesKey("uid")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
