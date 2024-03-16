package edu.temple.convoy

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import edu.temple.convoy.main_convoy.fcm.FCMViewModel
import edu.temple.convoy.main_convoy.fcm.MessageReceived
import edu.temple.convoy.main_convoy.location_data.LocationApp
import edu.temple.convoy.main_convoy.location_data.LocationViewModel
import edu.temple.convoy.ui.theme.ConvoyLabTheme
import edu.temple.convoy.utils.Constant
import edu.temple.convoy.utils.Screen

class MainActivity : ComponentActivity(), LocationApp.FCMCallback {

    private val fcmViewModel: FCMViewModel = FCMViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as LocationApp).registerCallback(this)

        setContent {
            ConvoyLabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = applicationContext
                    MyApp(
                        context,
                        fcmViewModel
                    )
                }
            }
        }
    }

    override fun messageReceived(messageReceived: MessageReceived) {
        when (messageReceived.action) {
            Constant.UPDATE -> {
                messageReceived.data?.let {
                    Handler(Looper.getMainLooper()).post {
                        fcmViewModel.updateConvoyParticipantsData(it)
                    }
                }
            }
            Constant.END -> {
                messageReceived.convoy_id?.let {
                    Handler(Looper.getMainLooper()).post {
                        fcmViewModel.getConvoyId(it)
                    }
                }
            }
            else -> {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as LocationApp).registerCallback(null)
    }
}

@Composable
fun MyApp(
    context: Context,
    fcmViewModel: FCMViewModel
) {
    val navController = rememberNavController()
    val locationViewModel: LocationViewModel = viewModel()

    val isLoggedIn = LoginState.isLoggedIn(context)
    val startDestination = if (isLoggedIn) Screen.HomeScreen.route else Screen.SignInScreen.route

    Navigation(
        context = context,
        locationViewModel = locationViewModel,
        fcmViewModel = fcmViewModel,
        navController = navController,
        startDestination = startDestination
    )
}
