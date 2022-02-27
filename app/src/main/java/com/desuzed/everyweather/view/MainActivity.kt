package com.desuzed.everyweather.view


import android.Manifest
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.databinding.ActivityMainBinding
import com.desuzed.everyweather.util.LocationHandler


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val locationHandler by lazy { LocationHandler(this, mainActivityViewModel) }
    private val locationCode = 100
    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(
            this,
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Everyweather)
        super.onCreate(savedInstanceState)
        bind()
        requestLocationPermissions()
        setLangForRequest()
        observeLiveData()
       // tryLoadAd()
      //  MobileAds.initialize(this) {}
    }

    private fun setLangForRequest() {
        val lang = ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0].language
        App.instance.setLang(lang)
    }

    private fun observeLiveData() {
        mainActivityViewModel.location.observe(this, {
            Log.i("TAG", "observeLiveData: $it")
        })

        mainActivityViewModel.toggleLookingForLocation.observe(this, {
            toggleLookingForLocation(it)
        })

        mainActivityViewModel.messageLiveData.observe(this, {
            if (it.hasBeenHandled){
                return@observe
            }
            Toast.makeText(
                applicationContext,
                it.getContentIfNotHandled(),
                Toast.LENGTH_LONG
            ).show()
        })
        mainActivityViewModel.getNetworkLiveData().observe(this, networkObserver)
    }

    private fun bind() {
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        val view: View = binding.root
        setContentView(view)
    }

    private fun toggleLookingForLocation (state : Boolean){
        when (state) {
            true -> {
                binding.tvLookingForLocation.visibility = View.VISIBLE
                binding.geoProgressBar.visibility = View.VISIBLE
            }
            false -> {
                binding.tvLookingForLocation.visibility = View.GONE
                binding.geoProgressBar.visibility = View.GONE
            }
        }
    }

    private val networkObserver = Observer<Boolean> {
        when (it) {
            true -> {
                binding.tvInternetConnection.visibility = View.GONE
            }
            else -> {
                binding.tvInternetConnection.visibility = View.VISIBLE
            }
        }
    }
//    private fun tryLoadAd (){
//        val adRequest = AdRequest.Builder().build()
//        binding.adView.loadAd(adRequest)
//    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        toggleLookingForLocation(true)
        locationHandler.findUserLocation()
    }

    fun requestLocationPermissions() {
        //if (locationHandler.permissionsGranted()) return
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
