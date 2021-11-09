package com.desuzed.clocknweather.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.mvvm.LocationApp
import com.desuzed.clocknweather.mvvm.vm.LocationViewModel
import com.desuzed.clocknweather.ui.StateRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LocationHandler(
    private val activity: Activity,
    private val locationViewModel: LocationViewModel
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

//TODO refactor
    fun postCurrentLocation() {
    locationViewModel.stateLiveData.postValue(StateRequest.Loading())
        if (permissionsGranted()) {
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, null)
                .addOnSuccessListener {
                    locationViewModel.stateLiveData.postValue(StateRequest.Loading())
                    if (it != null) {
                        val locationApp = LocationApp (it.latitude.toFloat(), it.longitude.toFloat())
                        locationViewModel.location.postValue(locationApp)
                        locationViewModel.stateLiveData.postValue(StateRequest.Success())
                    }else{
                        onError("Проверьте настройки местоположения")
                    }

                }
        } else {
           postLastLocation()
        }
    }

    fun postLastLocation() {
        if (permissionsGranted()) {
            locationViewModel.stateLiveData.postValue(StateRequest.Loading())
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    val locationApp = LocationApp (it.latitude.toFloat(), it.longitude.toFloat())
                 //   val latLon = LatLon(it.latitude, it.longitude)
                    locationViewModel.location.postValue(locationApp)
                    locationViewModel.stateLiveData.postValue(StateRequest.Success())

                }
            }
        } else {
            onError(activity.resources.getString(R.string.location_permissions_are_not_granted))
        }
    }

    fun onError(message: String) {
        locationViewModel.stateLiveData.postValue(StateRequest.Error(message))
    }

    fun permissionsGranted(): Boolean {
        val permissionFine = ContextCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val permissionCoarse = ContextCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionFine == PackageManager.PERMISSION_GRANTED
                || permissionCoarse == PackageManager.PERMISSION_GRANTED
    }


//        val request = LocationRequest.create().apply {
//            interval = 10000
//            fastestInterval = 5000
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }

//        fusedLocationClient.requestLocationUpdates(request, object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                val lastLocation: Location? = locationResult.lastLocation
//                Log.i(
//                    "TAG",
//                    "getCurrentLocation: lat: ${lastLocation.latitude} ; lon: ${lastLocation.longitude}"
//                )
//                if (lastLocation != null) {
//                    val latLon = LatLon(lastLocation.latitude, lastLocation.longitude)
//                    locationViewModel.postValue(latLon)
//                }
//            }
//        }, null)


}