package edu.temple.convoy.ui.components

import android.util.Log
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import edu.temple.convoy.main_convoy.location_data.LocationData
import edu.temple.convoy.main_convoy.location_data.LocationViewModel


@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
//    location: LocationData
    locationViewModel: LocationViewModel
) {
    val locationState by locationViewModel.location.observeAsState()
    Log.i("Location ", "$locationState")

    val userLocation = remember {
        mutableStateOf(LatLng(locationState!!.latitude, locationState!!.longitude))
    }
//    Log.i("location", "$userLocation")

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