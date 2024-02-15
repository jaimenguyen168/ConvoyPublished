package edu.temple.convoy.login_flow.data

import android.content.Context

object LoginState {

    private const val LOGIN_STATE = "login_state"
    private const val IS_LOGGED_IN = "is_logged_in"
    fun setLoginState(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences = context.getSharedPreferences(LOGIN_STATE, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(LOGIN_STATE, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }
}