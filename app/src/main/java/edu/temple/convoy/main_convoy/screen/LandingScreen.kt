package edu.temple.convoy.main_convoy.screen

import android.Manifest
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.preference.PreferenceManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import edu.temple.convoy.R
import edu.temple.convoy.login_flow.data.RetrofitClient
import edu.temple.convoy.login_flow.screen.showToast
import edu.temple.convoy.main_convoy.location_data.ConvoyMockData
import edu.temple.convoy.main_convoy.location_data.LocationUtil
import edu.temple.convoy.main_convoy.location_data.LocationViewModel
import edu.temple.convoy.main_convoy.permission.RequestLocationPermission
import edu.temple.convoy.ui.components.CustomTopAppBar
import edu.temple.convoy.ui.components.CustomizedDialog
import edu.temple.convoy.ui.components.CustomizedFloatingAddButton
import edu.temple.convoy.ui.components.CustomizedText
import edu.temple.convoy.ui.components.GoogleMapView
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LandingScreen(
    context: Context,
    locationViewModel: LocationViewModel,
    onCreateAConvoy: () -> Unit,
    onSignOut: () -> Unit
) {
    val locationUtil = LocationUtil(context = context)

//    val locationState by locationViewModel.location.collectAsState()

    val scaffoldState = rememberScaffoldState()
    var showCreateConvoyDialog by remember { mutableStateOf(false) }
    var showJoinConvoyDialog by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    RequestLocationPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    val permissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    val sessionKey = sharedPreferences.getString("session_key", "") ?: ""
    val username = sharedPreferences.getString("username", "") ?: ""

    var currentSelectedConvoy by remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
                 CustomTopAppBar(
                     title = username,
                     actionIcon = R.drawable.baseline_logout_24,
                     navIcon = R.drawable.profile,
                     onActionClick = {showSignOutDialog = true},
                     onNavClick = {}
                 )
        },
        floatingActionButton = {
            CustomizedFloatingAddButton {
                showCreateConvoyDialog = true
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp),
                elevation = 5.dp
            ) {

                when (permissionState.status) {
                    is PermissionStatus.Granted -> {
                        locationUtil.getLocationUpdates(locationViewModel)
                        GoogleMapView(locationViewModel = locationViewModel)
//                        GoogleMapView(location = locationState)
                    }
                    is PermissionStatus.Denied -> {
                        Text("Map can't be displayed. Please grant permission in settings.")
                    }
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                CustomizedText(
                    text = "Current Open Convoys",
                    fontWeight = FontWeight.SemiBold
                )

                LazyColumn {
                    items(ConvoyMockData.convoyData.size) {
                        val convoy = ConvoyMockData.convoyData[it]
                        ConvoyItem(
                            convoyId = convoy.id,
                            numberOfPeople = convoy.number,
                            onSelected = {
                                showJoinConvoyDialog = true
                                currentSelectedConvoy = it
                            }
                        )
                    }
                }
            }
        }

        if (showCreateConvoyDialog) {
            CustomizedDialog(
                title = "Create a Convoy",
                content = "Please click the button below to confirm to create a new Convoy",
                onDismiss = { showCreateConvoyDialog = false },
                onConfirm = {
                    coroutineScope.launch {

                        val response = RetrofitClient.instance.createConvoy(
                            action = "CREATE",
                            username = username,
                            sessionKey = sessionKey
                        )

                        if (response.status == "SUCCESS") {
                            response.convoyId?.run {
                                with(sharedPreferences.edit()) {
                                    putString("convoy_id", this@run)
                                    apply()
                                }
                                showToast(
                                    context,
                                    "$this@run"
                                )
                            }
                            onCreateAConvoy()

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

        if (showJoinConvoyDialog) {
            CustomizedDialog(
                title = "Convoy $currentSelectedConvoy",
                content = "Please confirm if you want to join this convoy.",
                onDismiss = { showJoinConvoyDialog = false },
                onConfirm = {}
            )
        }

        if (showSignOutDialog) {
            CustomizedDialog(
                title = "Log Out",
                content = "Please confirm if you want to log out.",
                onDismiss = { showSignOutDialog = false },
                onConfirm = {
                    coroutineScope.launch {
                        val response = RetrofitClient.instance.logoutUser(
                            action = "LOGOUT",
                            username = username,
                            sessionKey = sessionKey
                        )

                        if (response.status == "SUCCESS") {
                            showToast(context, "Log out successfully")
                            onSignOut()
                        } else {
                            showToast(context, "${response.message}")
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConvoyItem(
    convoyId: String,
    numberOfPeople: Int,
    onSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier.padding(2.dp),
        elevation = 2.dp,
        onClick = {
            onSelected(convoyId)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.join),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = convoyId,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "Number of people: $numberOfPeople",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ConvoyItem(
        convoyId = "789",
        numberOfPeople = 12,
        {}
    )
}