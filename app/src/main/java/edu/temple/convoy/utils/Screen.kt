package edu.temple.convoy.utils

import android.content.Context
import android.content.SharedPreferences

sealed class Screen(val route: String) {
    data object SignInScreen: Screen("sign_in_screen")
    data object SignUpScreen: Screen("sign_up_screen")
    data object HomeScreen: Screen("home_screen")
    data object ConvoyScreen: Screen("convoy_screen")
}

object LastScreen {
    private lateinit var sharedPreferences: SharedPreferences
    private const val LAST_SCREEN_PREF = "last_screen_preference"
    private const val LAST_SCREEN_KEY = "last_screen"

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(LAST_SCREEN_PREF, Context.MODE_PRIVATE)
    }

    var lastScreen: String
        get() = sharedPreferences.getString(LAST_SCREEN_KEY, Screen.SignInScreen.route) ?: Screen.SignInScreen.route
        set(value) {
            sharedPreferences.edit().putString(LAST_SCREEN_KEY, value).apply()
        }
}