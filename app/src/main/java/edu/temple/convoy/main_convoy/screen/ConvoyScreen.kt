package edu.temple.convoy.main_convoy.screen

import android.Manifest
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startForegroundService
import androidx.preference.PreferenceManager
import edu.temple.convoy.login_flow.data.RetrofitClient
import edu.temple.convoy.login_flow.screen.showToast
import edu.temple.convoy.main_convoy.location_data.LocationService
import edu.temple.convoy.main_convoy.location_data.LocationViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import edu.temple.convoy.R
import edu.temple.convoy.main_convoy.audio.ConvoyAudioPlayer
import edu.temple.convoy.main_convoy.audio.ConvoyAudioRecorder
import edu.temple.convoy.main_convoy.audio.component.RecordingView
import edu.temple.convoy.main_convoy.audio.SendAudio
import edu.temple.convoy.main_convoy.audio.component.AudioMessageItem
import edu.temple.convoy.main_convoy.audio.component.AudioPlayerViewModel
import edu.temple.convoy.main_convoy.fcm.FCMViewModel
import edu.temple.convoy.main_convoy.location_data.LocationUtil
import edu.temple.convoy.utils.Constant
import edu.temple.convoy.ui.components.CustomButton
import edu.temple.convoy.ui.components.CustomTopAppBar
import edu.temple.convoy.ui.components.CustomDialog
import edu.temple.convoy.ui.components.CustomFloatingAddButton
import edu.temple.convoy.ui.components.GoogleMapViewAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ConvoyScreen(
    fcmViewModel: FCMViewModel,
    locationViewModel: LocationViewModel,
    audioPlayerViewModel: AudioPlayerViewModel,
    backToHomeScreen: () -> Unit,
) {
    val context = LocalContext.current
    val locationUtil = LocationUtil(context = context)
    val recorder = ConvoyAudioRecorder(context)
    val player = ConvoyAudioPlayer(context, audioPlayerViewModel)

    val coroutineScope = rememberCoroutineScope()
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val sessionKey = sharedPreferences.getString(Constant.SESSION_KEY, "") ?: ""
    val username = sharedPreferences.getString(Constant.USERNAME, "") ?: ""
    val convoyId = sharedPreferences.getString(Constant.CONVOY_ID, "") ?: ""
    val userAction = sharedPreferences.getString(Constant.ACTION, "")

    var showEndConvoyDialog by remember { mutableStateOf(false) }
    var showLeaveConvoyDialog by remember { mutableStateOf(false) }
    var showRecorder by remember { mutableStateOf(false) }

    val audioMessages by audioPlayerViewModel.audioMessages.collectAsState()
    LaunchedEffect(audioMessages) {
        val newMessages = audioMessages.filter { !it.hasBeenPlayed }
        if (newMessages.isNotEmpty()) {
            player.playAudio(newMessages.first())
            audioPlayerViewModel.selectAudioMessage(newMessages.first())
        }
    }

    val sheetState = rememberModalBottomSheetState()

    var file: File? by remember { mutableStateOf(null) }
    val audioPermissionState = rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)

    LaunchedEffect(Unit) {
        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            startForegroundService(context, this)
        }
    }

    val location by locationViewModel.location.observeAsState()

    LaunchedEffect(location) {
        val response = RetrofitClient.instance.updateMyLocation(
            action = Constant.UPDATE,
            username = username,
            sessionKey = sessionKey,
            convoyId = convoyId,
            latitude = location?.latitude.toString(),
            longitude = location?.longitude.toString()
        )

        if (response.status == Constant.SUCCESS) {
            Log.i("Location sent", "$location")
        } else {
            showToast(
                context,
                "${response.message}"
            )
        }
    }

    val participantsLocation by fcmViewModel.convoyParticipantsData.observeAsState()
    val convoyEndId by fcmViewModel.convoyId.observeAsState(initial = null)

    LaunchedEffect(participantsLocation) {
        Log.i("Message through VM", participantsLocation.toString())
    }

    LaunchedEffect(convoyEndId) {
        convoyEndId?.let { endId ->
            if (endId == convoyId) {
                coroutineScope.launch {
                    RetrofitClient.instance.leaveConvoy(
                        action = Constant.LEAVE,
                        username = username,
                        sessionKey = sessionKey,
                        convoyId = convoyId
                    )
                }
                backToHomeScreen()
                showToast(
                    context,
                    "The owner has ended the convoy $convoyId"
                )
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = convoyId,
                actionIcon = R.drawable.baseline_logout_24,
                navIcon = R.drawable.channel,
                onActionClick = {},
                onNavClick = {}
            )
        },
        floatingActionButton = {
            CustomFloatingAddButton {
                showRecorder = true
            }
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
                    locationUtil.getLocationUpdates(locationViewModel)
                    GoogleMapViewAll(
                        locationViewModel = locationViewModel,
                        fcmViewModel = fcmViewModel,
                        context = context
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CustomButton(
                        text = if (userAction == Constant.CREATE) stringResource(R.string.end_convoy)
                            else stringResource(R.string.leave_convoy),
                        onClick = {
                            if (userAction == Constant.CREATE) showEndConvoyDialog = true
                            else showLeaveConvoyDialog = true
                        }
                    )
                }

                if (audioMessages.isNotEmpty()) {
                    val filteredMessages = audioMessages.filter { message-> message.username != username }
                    val reversedMessages = filteredMessages.reversed()

                    LazyColumn {
                        items(reversedMessages.size) {i ->
                            val message = reversedMessages[i]
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                AudioMessageItem(
                                    audioMessage = message,
                                    onPlay = {
                                        audioPlayerViewModel.selectAudioMessage(message)
                                        player.playAudio(message)
                                    },
                                    onStop = {
                                        player.stopAudio()
                                    },
                                    viewModel = audioPlayerViewModel
                                )
                            }
                        }
                    }
                }
            }

            if (showEndConvoyDialog) {
                CustomDialog(
                    title = stringResource(R.string.end_convoy),
                    content = stringResource(R.string.end_convoy_confirm_message),
                    onDismiss = { showEndConvoyDialog = false },
                    onConfirm = {
                        coroutineScope.launch {

                            val response = RetrofitClient.instance.endConvoy(
                                action = Constant.END,
                                username = username,
                                sessionKey = sessionKey,
                                convoyId = convoyId
                            )

                            if (response.status == Constant.SUCCESS) {
                                Intent(context, LocationService::class.java).apply {
                                    action = LocationService.ACTION_STOP
                                    startForegroundService(context, this)
                                }
                                audioPlayerViewModel.clearAudioMessages()
                                backToHomeScreen()
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

            if (showLeaveConvoyDialog) {
                CustomDialog(
                    title = stringResource(R.string.leave_convoy),
                    content = stringResource(R.string.leave_convoy_confirm_message),
                    onDismiss = { showLeaveConvoyDialog = false },
                    onConfirm = {
                        coroutineScope.launch {
                            val response = RetrofitClient.instance.leaveConvoy(
                                action = Constant.LEAVE,
                                username = username,
                                sessionKey = sessionKey,
                                convoyId = convoyId
                            )
                            if (response.status == Constant.SUCCESS) {
                                audioPlayerViewModel.clearAudioMessages()
                                backToHomeScreen()
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

            if (showRecorder) {
                ModalBottomSheet(
                    onDismissRequest = {},
                    sheetState = sheetState
                ) {
                    RecordingView(
                        onRecord = {
                            if (!audioPermissionState.status.isGranted) {
                                audioPermissionState.launchPermissionRequest()
                            } else {
                                file = File(context.cacheDir, "audio.mp4")
                                file?.let {
                                    recorder.startRecording(it)
                                }
                            }
                        },
                        onStop = {
                            recorder.stopRecording()
                                 },
                        onCancel = {
                            recorder.stopRecording()
                            showRecorder = false
                        },
                        onSend = {
                            val handler = Handler(Looper.getMainLooper())

                            CoroutineScope(Dispatchers.Main).launch {
                                file?.let { audioFile ->
                                    SendAudio.sendVoiceMessage(
                                        username = username,
                                        sessionKey = sessionKey,
                                        convoyId = convoyId,
                                        audioFile = audioFile
                                    ) { success, errorMessage, convoyId ->
                                        if (success) {
                                            // Message sent successfully
                                            showRecorder = false
                                            Log.d("VoiceMessage", "Message sent successfully for convoy ID: $convoyId")
                                            handler.post {
                                                showToast(
                                                    context,
                                                    "Message sent successfully for convoy ID: $convoyId"
                                                )
                                            }
                                        } else {
                                            // Error occurred
                                            Log.e("VoiceMessage", "Error: $errorMessage")
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


