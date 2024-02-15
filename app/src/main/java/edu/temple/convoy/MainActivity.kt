package edu.temple.convoy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import edu.temple.convoy.login_flow.data.LoginState
import edu.temple.convoy.main_convoy.location_data.LocationViewModel
import edu.temple.convoy.ui.theme.ConvoyLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConvoyLabTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = applicationContext
                    MyApp(context)
                }
            }
        }
    }
}

@Composable
fun MyApp(
    context: Context
) {
    val navController = rememberNavController()
    val locationViewModel: LocationViewModel = viewModel()

    val isLoggedIn = LoginState.isLoggedIn(context)
    val startDestination = if (isLoggedIn) Screen.LandingScreen.route else Screen.SignInScreen.route

    Navigation(
        context = context,
        locationViewModel = locationViewModel,
        navController = navController,
        startDestination = startDestination
    )
}



//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ConvoyLabTheme {
//        MyApp(app)
//    }
//}