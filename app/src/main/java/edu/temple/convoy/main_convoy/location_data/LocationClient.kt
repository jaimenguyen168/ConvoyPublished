package edu.temple.convoy.main_convoy.location_data

import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(viewModel: LocationViewModel): Flow<LocationData>

    class LocationException(message: String): Exception()
}