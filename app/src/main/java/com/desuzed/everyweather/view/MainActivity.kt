package com.desuzed.everyweather.view


import android.Manifest
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.ViewModelProvider
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.databinding.ActivityMainBinding
import com.desuzed.everyweather.model.vm.AppViewModelFactory
import com.desuzed.everyweather.model.vm.SharedViewModel
import com.desuzed.everyweather.util.LocationHandler
import com.google.android.gms.ads.MobileAds
import java.util.*


class MainActivity : AppCompatActivity() {
    val locationHandler by lazy { LocationHandler(this, sharedViewModel) }
    private val locationCode = 100
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(
            this,
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(SharedViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Everyweather)
        super.onCreate(savedInstanceState)
        bind()
        requestLocationPermissions()
        setLangForRequest()
        MobileAds.initialize(this) {}
    }

    private fun setLangForRequest() {
        val lang = ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0].language
        App.instance.setLang(lang)
    }

    private fun bind() {
        val activityBinding = ActivityMainBinding.inflate(
            layoutInflater
        )
        val view: View = activityBinding.root
        setContentView(view)
        //TODO Сделать прозрачный статус бар
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        ViewCompat.setOnApplyWindowInsetsListener(view) { v: View, windowInsets: WindowInsetsCompat ->
//            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
//            val mlp = v.layoutParams as MarginLayoutParams
//            mlp.topMargin = insets.top
//            v.layoutParams = mlp
//            v.updatePadding(top = insets.top, bottom = insets.bottom)
//            WindowInsetsCompat.CONSUMED
//        }

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
