package com.dicoding.yogascan.data

import android.content.Context
import android.content.SharedPreferences

class Helper (context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "YogascanSession",
        Context.MODE_PRIVATE
    )

    fun saveUid(uid: String) {
        val editor = sharedPreferences.edit()
        editor.putString("UID", uid)
        editor.apply()
    }

    fun getUid(): String? {
        return sharedPreferences.getString("UID", null)
    }

    fun clearSession() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}