package com.desuzed.clocknweather.ui


import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.desuzed.clocknweather.App
import com.desuzed.clocknweather.databinding.ActivityMainBinding
import com.desuzed.clocknweather.mvvm.vm.AppViewModelFactory
import com.desuzed.clocknweather.mvvm.vm.LocationViewModel
import com.desuzed.clocknweather.util.LocationHandler

class MainActivity : AppCompatActivity() {
    val locationHandler by lazy { LocationHandler(this, locationViewModel) }
    private val locationCode = 100
    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider(
            this,
            AppViewModelFactory(App.instance)
        )
            .get(LocationViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        requestLocationPermissions()
      //  locationHandler.postLastLocation()
    }

    private fun bind() {
        val activityBinding = ActivityMainBinding.inflate(
            layoutInflater
        )
        val view: View = activityBinding.root
        setContentView(view)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationHandler.postCurrentLocation()
    }

    fun requestLocationPermissions() {
        if (locationHandler.permissionsGranted()) return
        ActivityCompat
            .requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                locationCode
            )
    }

}
