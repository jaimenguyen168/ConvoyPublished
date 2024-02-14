package edu.temple.convoy

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.temple.convoy.login_flow.screen.SignInScreen
import edu.temple.convoy.login_flow.screen.SignUpScreen
import edu.temple.convoy.main_convoy.location_data.LocationViewModel
import edu.temple.convoy.main_convoy.screen.ConvoyScreen
import edu.temple.convoy.main_convoy.screen.LandingScreen

@Composable
fun Navigation(
    context: Context,
    locationViewModel: LocationViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignInScreen.route
    ) {
        composable(Screen.SignInScreen.route) {
            SignInScreen(
                goToSignUpClick = {
                    navController.navigate(Screen.SignUpScreen.route) {
                        popUpTo(Screen.SignInScreen.route) { inclusive = true }
                    }
                },
                onSignInSuccess = {
                    navController.navigate(Screen.LandingScreen.route)
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

        composable(Screen.LandingScreen.route) {
            LandingScreen(
                context = context,
                locationViewModel = locationViewModel,
                onCreateAConvoy = {
                    navController.navigate(Screen.ConvoyScreen.route) {
                        popUpTo(Screen.LandingScreen.route) { inclusive = true }
                    }
                },
                onSignOut = {
                    navController.navigate(Screen.SignInScreen.route) {
                        popUpTo(Screen.LandingScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ConvoyScreen.route) {
            ConvoyScreen(
                locationViewModel = locationViewModel,
                backToLandingScreen = {
                    navController.navigate(Screen.LandingScreen.route) {
                        popUpTo(Screen.ConvoyScreen.route) { inclusive = true }
                    }
                }
            )
        }
    }
}