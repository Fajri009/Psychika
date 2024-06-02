package com.example.psychika.data.local.preference

import android.content.Context

class UserPreference(context: Context) {
    private val preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(data: User) {
        val editor = preference.edit()
        editor.putString(EMAIL, data.email)
        editor.apply()
    }

    fun getUser(): User {
        val user = User()
        user.email = preference.getString(EMAIL, "")

        return user
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val EMAIL = "email"
    }
}