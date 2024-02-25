package edu.temple.convoy.main_convoy.screen

import android.Manifest
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.preference.PreferenceManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import edu.temple.convoy.R
import edu.temple.convoy.login_flow.data.RetrofitClient
import edu.temple.convoy.login_flow.screen.showToast
import edu.temple.convoy.main_convoy.location_data.LocationUtil
import edu.temple.convoy.main_convoy.location_data.LocationViewModel
import edu.temple.convoy.main_convoy.permission.RequestLocationPermission
import edu.temple.convoy.ui.Constant
import edu.temple.convoy.ui.components.CustomButton
import edu.temple.convoy.ui.components.CustomTopAppBar
import edu.temple.convoy.ui.components.CustomDialog
import edu.temple.convoy.ui.components.CustomFloatingAddButton
import edu.temple.convoy.ui.components.CustomOutlinedTextField
import edu.temple.convoy.ui.components.CustomText
import edu.temple.convoy.ui.components.GoogleMapView
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    context: Context,
    locationViewModel: LocationViewModel,
    toConvoy: () -> Unit,
    onSignOut: () -> Unit
) {
    val locationUtil = LocationUtil(context = context)

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

    var convoyId by remember { mutableStateOf("") }

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
            CustomFloatingAddButton {
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
                    }
                    is PermissionStatus.Denied -> {
                        Text("Map can't be displayed. Please grant permission in settings.")
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CustomText(
                    text = "Join a Convoy by entering its ID",
                    fontWeight = FontWeight.SemiBold
                )

                CustomOutlinedTextField(
                    value = convoyId,
                    label = "Convoy_ID",
                    icon = R.drawable.join,
                    onValueChange = { id ->
                        convoyId = id
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))

                CustomButton(
                    modifier = Modifier.padding(16.dp),
                    text = "Enter",
                    onClick = {
                        showJoinConvoyDialog = true
                    }
                )
            }
        }

        if (showCreateConvoyDialog) {
            CustomDialog(
                title = "Create a Convoy",
                content = "Please click the button below to confirm to create a new Convoy",
                onDismiss = { showCreateConvoyDialog = false },
                onConfirm = {
                    coroutineScope.launch {

                        val response = RetrofitClient.instance.createConvoy(
                            action = Constant.CREATE,
                            username = username,
                            sessionKey = sessionKey
                        )

                        if (response.status == "SUCCESS") {
                            response.convoyId?.run {
                                with(sharedPreferences.edit()) {
                                    putString("convoy_id", this@run)
                                    putString(Constant.ACTION, Constant.CREATE)
                                    apply()
                                }
                                showToast(
                                    context,
                                    "$this@run"
                                )
                            }
                            toConvoy()

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
            CustomDialog(
                title = "Convoy @$convoyId",
                content = "Please confirm if you want to join this convoy.",
                onDismiss = { showJoinConvoyDialog = false },
                onConfirm = {
                    coroutineScope.launch {
                        val response = RetrofitClient.instance.joinConvoy(
                            action = Constant.JOIN,
                            username = username,
                            sessionKey = sessionKey,
                            convoyId = convoyId
                        )

                        if (response.status == "SUCCESS") {
                            with(sharedPreferences.edit()) {
                                putString(Constant.ACTION, Constant.JOIN)
                                putString("convoy_id", convoyId)
                                apply()
                            }
                            showToast(context, "Join successfully")
                            toConvoy()
                        } else {
                            showToast(context, "${response.message}")
                        }
                    }
                }
            )
        }

        if (showSignOutDialog) {
            CustomDialog(
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



@Preview(showBackground = true)
@Composable
fun Preview() {
    HomeScreen(
        context = LocalContext.current,
        locationViewModel = viewModel<LocationViewModel>(),
        toConvoy = { /*TODO*/ }) {
    }
}