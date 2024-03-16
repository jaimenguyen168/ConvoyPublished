package edu.temple.convoy

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.temple.convoy.login_flow.data.LoginState
import edu.temple.convoy.login_flow.screen.SignInScreen
import edu.temple.convoy.login_flow.screen.SignUpScreen
import edu.temple.convoy.main_convoy.fcm.FCMViewModel
import edu.temple.convoy.main_convoy.location_data.LocationViewModel
import edu.temple.convoy.main_convoy.screen.ConvoyScreen
import edu.temple.convoy.main_convoy.screen.HomeScreen
import edu.temple.convoy.utils.Screen

@Composable
fun Navigation(
    context: Context,
    locationViewModel: LocationViewModel,
    fcmViewModel: FCMViewModel,
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.SignInScreen.route) {
            SignInScreen(
                goToSignUpClick = {
                    navController.navigate(Screen.SignUpScreen.route) {
                        popUpTo(Screen.SignInScreen.route) { inclusive = true }
                    }
                },
                onSignInSuccess = {
                    LoginState.setLoginState(context, true)
                    navController.navigate(Screen.HomeScreen.route)
                }
            )
        }

        composable(Screen.SignUpScreen.route) {
            SignUpScreen(
                goToSignInClick = {
                    navController.navigate(Screen.SignInScreen.route) {
                        popUpTo(Screen.SignUpScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.HomeScreen.route) {
            HomeScreen(
                context = context,
                locationViewModel = locationViewModel,
                toConvoy = {
                    navController.navigate(Screen.ConvoyScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
                    }
                },
                onSignOut = {
                    LoginState.setLoginState(context, false)
                    navController.navigate(Screen.SignInScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ConvoyScreen.route) {
            ConvoyScreen(
                fcmViewModel = fcmViewModel,
                locationViewModel = locationViewModel,
                backToHomeScreen = {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.ConvoyScreen.route) { inclusive = true }
                    }
                }
            )
        }
    }
}