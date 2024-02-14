package edu.temple.convoy.main_convoy.location_data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class LocationUtil(
    private val context: Context,
) : LocationClient {

    private var previousLocation: Location? = null
    private var client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(viewModel: LocationViewModel) {
        if(!context.hasLocationPermission()) {
            throw LocationClient.LocationException("Missing location permission")
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if(!isGpsEnabled && !isNetworkEnabled) {
            throw LocationClient.LocationException("GPS is disabled")
        }

        val locationCallback = object : LocationCallback() {
            @SuppressLint("MissingPermission")
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {currentLocation ->
                    if (previousLocation == null) {
                        val newLocation = LocationData(
                            latitude = currentLocation.latitude,
                            longitude = currentLocation.longitude
                        )
                        viewModel.updateLocation(newLocation)
                        previousLocation = currentLocation
                    } else {
                        val distance = previousLocation!!.distanceTo(currentLocation)
                        if (distance >= 10) {
                            val newLocation = LocationData(
                                latitude = currentLocation.latitude,
                                longitude = currentLocation.longitude
                            )
                            viewModel.updateLocation(newLocation)
                            previousLocation = currentLocation
                        }
                    }
                }
//                locationResult.lastLocation?.let {
//                    val newLocation = LocationData(
//                        latitude = it.latitude,
//                        longitude = it.longitude
//                    )
//                    viewModel.updateLocation(newLocation)
//                    previousLocation?.let { oldLocation ->
//                        val distance = oldLocation.distanceTo(it)
//                        if (distance >= 10) {
//                            val location = LocationData(
//                                latitude = newLocation.latitude,
//                                longitude = newLocation.longitude)
//                            viewModel.updateLocation(location)
//                        }
//                    }
//                }
            }
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(1000)
            .build()

        client.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}