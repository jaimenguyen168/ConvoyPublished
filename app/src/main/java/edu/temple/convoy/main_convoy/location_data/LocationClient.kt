package edu.temple.convoy.main_convoy.location_data


interface LocationClient {
    fun getLocationUpdates(viewModel: LocationViewModel)

    class LocationException(message: String): Exception()
}