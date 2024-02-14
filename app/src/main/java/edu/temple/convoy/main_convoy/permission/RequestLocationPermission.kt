package edu.temple.convoy.main_convoy.permission

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    permission: String
) {
    val permissionState = rememberPermissionState(permission = permission)
    val rationaleMessage = "To use this app's functionalities, you need to give us the permission."

    HandleRequest(
        permissionState = permissionState,
        deniContent = {
            PermissionDeniedContent(
                rationaleMessage = rationaleMessage,
                shouldShowRationale = it,
                onRequestPermission = {
                    permissionState.launchPermissionRequest()
                }
            )
        },
        content = {

        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandleRequest(
    permissionState: PermissionState,
    deniContent: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    when (permissionState.status) {
        is PermissionStatus.Granted -> {
            content()
        }
        is PermissionStatus.Denied -> {
            deniContent(permissionState.status.shouldShowRationale)
        }
    }
}

@Composable
fun PermissionDeniedContent(
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {
    if (shouldShowRationale) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Permission Request",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(rationaleMessage)
            },
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text("Give Permission")
                }
            }
        )
    } else {
        Content(
            onClick = onRequestPermission
        )
    }
}

@Composable
fun Content(
    showButton: Boolean = true,
    onClick: () -> Unit
) {
    if (showButton) {
        val enableLocation = remember { mutableStateOf(true) }
        if (enableLocation.value) {
            CustomDialogLocation(
                title = "Turn On Location Service",
                desc = "Explore the world without getting lost and keep the track of your location.\n\nGive this app a permission to proceed. If it doesn't work, then you'll have to do it manually from the settings.",
                enableLocation,
                onClick
            )
        }
    }
}