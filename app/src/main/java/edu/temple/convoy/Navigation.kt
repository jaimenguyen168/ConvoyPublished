package edu.temple.convoy

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.temple.convoy.login_flow.data.LoginState
import edu.temple.convoy.login_flow.screen.SignInScreen
import edu.temple.convoy.login_flow.screen.SignUpScreen
import edu.temple.convoy.main_convoy.audio.component.AudioPlayerViewModel
import edu.temple.convoy.main_convoy.fcm.FCMViewModel
import edu.temple.convoy.main_convoy.location_data.LocationViewModel
import edu.temple.convoy.main_convoy.screen.ConvoyScreen
import edu.temple.convoy.main_convoy.screen.HomeScreen
import edu.temple.convoy.utils.LastScreen
import edu.temple.convoy.utils.Screen
import java.io.File

@Composable
fun Navigation(
    context: Context,
    locationViewModel: LocationViewModel,
    fcmViewModel: FCMViewModel,
    navController: NavHostController,
    audioPlayerViewModel: AudioPlayerViewModel
) {
    NavHost(
        navController = navController,
        startDestination = LastScreen.lastScreen
    ) {
        composable(Screen.SignInScreen.route) {
            SignInScreen(
                goToSignUpClick = {
                    LastScreen.lastScreen = Screen.SignUpScreen.route
                    navController.navigate(Screen.SignUpScreen.route) {
                        popUpTo(Screen.SignInScreen.route) { inclusive = true }
                    }
                },
                onSignInSuccess = {
                    LastScreen.lastScreen = Screen.HomeScreen.route
                    navController.navigate(Screen.HomeScreen.route)
                }
            )
        }

        composable(Screen.SignUpScreen.route) {
            SignUpScreen(
                goToSignInClick = {
                    LastScreen.lastScreen = Screen.SignInScreen.route
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
                    LastScreen.lastScreen = Screen.ConvoyScreen.route
                    navController.navigate(Screen.ConvoyScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
                    }
                },
                onSignOut = {
                    LastScreen.lastScreen = Screen.SignInScreen.route
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
                audioPlayerViewModel = audioPlayerViewModel,
                backToHomeScreen = {
                    LastScreen.lastScreen = Screen.HomeScreen.route
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.ConvoyScreen.route) { inclusive = true }
                    }
                }
            )
        }
    }
}