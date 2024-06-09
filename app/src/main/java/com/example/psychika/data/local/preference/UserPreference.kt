package com.example.psychika.data.local.preference

import android.content.Context

class UserPreference(context: Context) {
    private val preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(data: User) {
        val editor = preference.edit()
        editor.putString(ID, data.id)
        editor.putBoolean(REMEMBER_ME, data.rememberMe)
        editor.putBoolean(GOOGLE_AUTH, data.googleAuth)
        editor.apply()
    }

    fun getUser(): User {
        val user = User()
        user.id = preference.getString(ID, "")
        user.rememberMe = preference.getBoolean(REMEMBER_ME, false)
        user.googleAuth = preference.getBoolean(GOOGLE_AUTH, false)

        return user
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val ID = "id"
        private const val REMEMBER_ME = "remember_me"
        private const val GOOGLE_AUTH = "google_auth"
    }
}