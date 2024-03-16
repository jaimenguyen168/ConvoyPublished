package edu.temple.convoy.main_convoy.screen

import android.Manifest
import android.content.Context
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import edu.temple.convoy.R
import edu.temple.convoy.login_flow.data.RetrofitClient
import edu.temple.convoy.login_flow.screen.showToast
import edu.temple.convoy.main_convoy.location_data.LocationUtil
import edu.temple.convoy.main_convoy.location_data.LocationViewModel
import edu.temple.convoy.main_convoy.permission.RequestLocationPermission
import edu.temple.convoy.utils.Constant
import edu.temple.convoy.ui.components.CustomButton
import edu.temple.convoy.ui.components.CustomTopAppBar
import edu.temple.convoy.ui.components.CustomDialog
import edu.temple.convoy.ui.components.CustomFloatingAddButton
import edu.temple.convoy.ui.components.CustomOutlinedTextField
import edu.temple.convoy.ui.components.CustomText
import edu.temple.convoy.ui.components.GoogleMapView
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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

    val sessionKey = sharedPreferences.getString(Constant.SESSION_KEY, "") ?: ""
    val username = sharedPreferences.getString(Constant.USERNAME, "") ?: ""

    var convoyId by remember { mutableStateOf("") }
    var newToken by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {

        newToken = Firebase.messaging.token.await()

        val response = RetrofitClient.instance.updateFcmToken(
            action = Constant.UPDATE,
            username = username,
            sessionKey = sessionKey,
            fcmToken = newToken
        )

        if (response.status == Constant.SUCCESS) {
            Log.i("FCM Token saved", newToken)
        } else {
            showToast(
                context,
                "${response.message}"
            )
        }
    }


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
                        Text(stringResource(R.string.no_map_display_message))
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
                    text = stringResource(R.string.ask_to_join_convoy),
                    fontWeight = FontWeight.SemiBold
                )

                CustomOutlinedTextField(
                    value = convoyId,
                    label = stringResource(R.string.convoy_id),
                    icon = R.drawable.join,
                    onValueChange = { id ->
                        convoyId = id
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))

                CustomButton(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.enter),
                    onClick = {
                        showJoinConvoyDialog = true
                    }
                )
            }
        }

        if (showCreateConvoyDialog) {
            CustomDialog(
                title = stringResource(R.string.create_a_convoy),
                content = stringResource(R.string.create_convoy_confirm_message),
                onDismiss = { showCreateConvoyDialog = false },
                onConfirm = {
                    coroutineScope.launch {

                        val response = RetrofitClient.instance.createConvoy(
                            action = Constant.CREATE,
                            username = username,
                            sessionKey = sessionKey
                        )

                        if (response.status == Constant.SUCCESS) {
                            response.convoyId?.run {
                                with(sharedPreferences.edit()) {
                                    putString(Constant.CONVOY_ID, this@run)
                                    putString(Constant.ACTION, Constant.CREATE)
                                    apply()
                                }
                                showToast(
                                    context,
                                    "Created a Convoy successfully"
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
                content = stringResource(R.string.join_convoy_confirm_message),
                onDismiss = { showJoinConvoyDialog = false },
                onConfirm = {
                    coroutineScope.launch {
                        val response = RetrofitClient.instance.joinConvoy(
                            action = Constant.JOIN,
                            username = username,
                            sessionKey = sessionKey,
                            convoyId = convoyId
                        )

                        if (response.status == Constant.SUCCESS) {
                            with(sharedPreferences.edit()) {
                                putString(Constant.ACTION, Constant.JOIN)
                                putString(Constant.CONVOY_ID, convoyId)
                                apply()
                            }
                            showToast(context, "Join a Convoy successfully")
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
                title = stringResource(R.string.log_out),
                content = stringResource(R.string.log_out_confirm_message),
                onDismiss = { showSignOutDialog = false },
                onConfirm = {
                    coroutineScope.launch {
                        val response = RetrofitClient.instance.logoutUser(
                            action = Constant.LOGOUT,
                            username = username,
                            sessionKey = sessionKey
                        )

                        if (response.status == Constant.SUCCESS) {
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