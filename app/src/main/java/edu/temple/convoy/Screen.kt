package edu.temple.convoy

sealed class Screen(val route: String) {
    data object SignInScreen: Screen("sign_in_screen")
    data object SignUpScreen: Screen("sign_up_screen")
    data object LandingScreen: Screen("landing_screen")
    data object ConvoyScreen: Screen("convoy_screen")
}