package edu.temple.convoy

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import edu.temple.convoy.main_convoy.audio.component.AudioPlayerViewModel
import edu.temple.convoy.main_convoy.fcm.FCMViewModel
import edu.temple.convoy.main_convoy.fcm.MessageReceived
import edu.temple.convoy.main_convoy.location_data.LocationApp
import edu.temple.convoy.main_convoy.location_data.LocationViewModel
import edu.temple.convoy.ui.theme.ConvoyLabTheme
import edu.temple.convoy.utils.Constant
import edu.temple.convoy.utils.LastScreen
import edu.temple.convoy.utils.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity(), LocationApp.FCMCallback {

    private val fcmViewModel: FCMViewModel = FCMViewModel()
    private val audioPlayerViewModel: AudioPlayerViewModel = AudioPlayerViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as LocationApp).registerCallback(this)
        LastScreen.init(this)

        setContent {
            ConvoyLabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = applicationContext
                    MyApp(
                        context,
                        fcmViewModel,
                        audioPlayerViewModel
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
            Constant.MESSAGE -> {
                CoroutineScope(Dispatchers.Main).launch {
                    messageReceived.username?.let { username ->
                        val messageUrl = messageReceived.message_file
                        if (messageUrl != null) {
                            audioPlayerViewModel.downloadAndAddMessage(applicationContext, username, messageUrl)
                        } else {
                            Log.d("FailAudio", "There's a problem with getting the file")
                        }
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
    fcmViewModel: FCMViewModel,
    audioPlayerViewModel: AudioPlayerViewModel
) {
    val navController = rememberNavController()
    val locationViewModel: LocationViewModel = viewModel()

    Navigation(
        context = context,
        locationViewModel = locationViewModel,
        fcmViewModel = fcmViewModel,
        navController = navController,
        audioPlayerViewModel = audioPlayerViewModel
    )
}
