package com.desuzed.clocknweather.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.desuzed.clocknweather.mvvm.LocationApp
import com.desuzed.clocknweather.mvvm.vm.LocationViewModel
import com.desuzed.clocknweather.network.dto.LatLon
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LocationHandler(
    private val activity: Activity,
    private val locationViewModel: LocationViewModel
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)


    fun postCurrentLocation() {
        if (permissionsGranted()) {
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener {
                    if (it != null) {
                        val locationApp = LocationApp (it.latitude.toFloat(), it.longitude.toFloat())
                        locationViewModel.location.postValue(locationApp)
                    }else{
                        checkLocationToast.show()
                    }
                    Log.i("TAG", "postCurrentLocation: $it")

                }
        } else {
            checkLocationToast.show()
        }
    }

//    fun postLastLocation() {
//        if (permissionsGranted()) {
//            fusedLocationClient.lastLocation.addOnSuccessListener {
//                Log.i("TAG", "getLastLocation: $it")
//                if (it != null) {
//                    val locationApp = LocationApp (it.latitude.toFloat(), it.longitude.toFloat())
//                 //   val latLon = LatLon(it.latitude, it.longitude)
//                    locationViewModel.location.postValue(locationApp)
//                }
//            }
//        } else {
//            checkLocationToast.show()
//        }
//    }

    private val checkLocationToast = Toast.makeText(
        activity,
        "Проверьте настройки местоположения",
        Toast.LENGTH_LONG
    )


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