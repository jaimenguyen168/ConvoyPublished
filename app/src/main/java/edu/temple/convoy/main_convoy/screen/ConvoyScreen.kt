package edu.temple.convoy.main_convoy.screen

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.preference.PreferenceManager
import com.google.accompanist.permissions.PermissionStatus
import edu.temple.convoy.login_flow.data.RetrofitClient
import edu.temple.convoy.login_flow.screen.showToast
import edu.temple.convoy.main_convoy.location_data.LocationService
import edu.temple.convoy.main_convoy.location_data.LocationViewModel
import edu.temple.convoy.ui.components.GoogleMapView
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import edu.temple.convoy.R
import edu.temple.convoy.ui.components.CustomTopAppBar
import edu.temple.convoy.ui.components.CustomizedDialog

@Composable
fun ConvoyScreen(
    locationViewModel: LocationViewModel,
    backToLandingScreen: () -> Unit
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val sessionKey = sharedPreferences.getString("session_key", "") ?: ""
    val username = sharedPreferences.getString("username", "") ?: ""
    val convoyId = sharedPreferences.getString("convoy_id", "") ?: ""

    val locationState by locationViewModel.location.collectAsState()
    Log.i("Location ", "$locationState")

    LaunchedEffect(Unit) {
        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            startForegroundService(context, this)
        }
    }

    var showLeaveConvoyDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = convoyId,
                actionIcon = R.drawable.baseline_logout_24,
                navIcon = R.drawable.channel,
                onActionClick = { showLeaveConvoyDialog = true },
                onNavClick = {}
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(16.dp),
                    elevation = 5.dp
                ) {
                    GoogleMapView(locationViewModel = locationViewModel)
//                    GoogleMapView(location = locationState)
                }

                Text(text = locationState.toString())
            }

            if (showLeaveConvoyDialog) {
                CustomizedDialog(
                    title = "Leave Convoy",
                    content = "Please confirm if you want to leave this convoy.",
                    onDismiss = { showLeaveConvoyDialog = false },
                    onConfirm = {
                        coroutineScope.launch {

                            val response = RetrofitClient.instance.endConvoy(
                                action = "END",
                                username = username,
                                sessionKey = sessionKey,
                                convoyId = convoyId
                            )

                            if (response.status == "SUCCESS") {
                                Intent(context, LocationService::class.java).apply {
                                    action = LocationService.ACTION_STOP
                                    startForegroundService(context, this)
                                }
                                backToLandingScreen()
                            } else {
                                showToast(
                                    context,
                                    "${response.message}"
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConvoy() {
    val locationViewModel: LocationViewModel = viewModel()
    ConvoyScreen(
        locationViewModel = locationViewModel,
        {}
    )
}