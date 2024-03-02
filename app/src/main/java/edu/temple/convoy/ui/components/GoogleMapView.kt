package edu.temple.convoy.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.asFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import edu.temple.convoy.R
import edu.temple.convoy.main_convoy.fcm.ConvoyParticipant
import edu.temple.convoy.main_convoy.fcm.FCMViewModel
import edu.temple.convoy.main_convoy.location_data.LocationData
import edu.temple.convoy.main_convoy.location_data.LocationViewModel


@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    locationViewModel: LocationViewModel
) {
    val locationState by locationViewModel.location.observeAsState()
    Log.i("Location ", "$locationState")

    val userLocation = remember {
        mutableStateOf(LatLng(locationState!!.latitude, locationState!!.longitude))
    }

    LaunchedEffect(locationState) {
        userLocation.value = LatLng(locationState!!.latitude, locationState!!.longitude)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation.value, 12f)
    }

    GoogleMap(
        modifier = modifier
            .padding(8.dp),
        cameraPositionState = cameraPositionState,
    ) {
        Marker(
            state = MarkerState(position = userLocation.value),
            title = "My current location",
            draggable = true,

        )
    }

    LaunchedEffect(userLocation.value) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(userLocation.value, 12f)
    }
}

@Composable
fun GoogleMapViewAll(
    modifier: Modifier = Modifier,
    locationViewModel: LocationViewModel,
    fcmViewModel: FCMViewModel,
    context: Context
) {
    val locationState by locationViewModel.location.observeAsState()
    Log.i("Location ", "$locationState")

    val userLocation = remember {
        mutableStateOf(LatLng(locationState!!.latitude, locationState!!.longitude))
    }

    LaunchedEffect(locationState) {
        userLocation.value = LatLng(locationState!!.latitude, locationState!!.longitude)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation.value, 10f)
    }

    val convoyParticipantsData by fcmViewModel.convoyParticipantsData.observeAsState(initial = emptyList())
    val participantLocations = remember { mutableStateOf<List<ConvoyParticipant>>(emptyList()) }

    LaunchedEffect(convoyParticipantsData) {
        val updatedParticipantLocations = convoyParticipantsData.map { participant ->
            ConvoyParticipant(
                username = participant.username,
                firstname = participant.firstname,
                lastname = participant.lastname,
                latitude = participant.latitude,
                longitude = participant.longitude
            )
        }
        participantLocations.value = updatedParticipantLocations
    }

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val username = sharedPreferences.getString("username", "") ?: ""

    GoogleMap(
        modifier = modifier
            .padding(8.dp),
        cameraPositionState = cameraPositionState,
    ) {
        Marker(
            state = MarkerState(position = userLocation.value),
            title = "My current location",
            draggable = true
        )

        participantLocations.value.forEach {
            if (it.username != username) {
                MapMarker(
                    context = context,
                    position = LatLng(it.latitude, it.longitude),
                    title = it.username,
                    iconResourceId = R.drawable.baseline_location_pin_24
                )
            }
        }
    }

    LaunchedEffect(userLocation.value) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(userLocation.value, 10f)
    }
}

@Composable
fun MapMarker(
    context: Context,
    position: LatLng,
    title: String,
    @DrawableRes iconResourceId: Int
) {
    val icon = bitmapDescriptorFromVector(
        context, iconResourceId
    )

    Marker(
        state = MarkerState(position = position),
        title = title,
        icon = icon,
    )
}

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}