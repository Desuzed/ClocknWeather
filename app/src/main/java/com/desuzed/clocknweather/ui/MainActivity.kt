package com.desuzed.clocknweather.ui


import android.Manifest
import android.app.SearchManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.databinding.ActivityMainBinding
import com.desuzed.clocknweather.mvvm.AppViewModelFactory
import com.desuzed.clocknweather.mvvm.WeatherViewModel
import com.desuzed.clocknweather.retrofit.pojo.LatLon
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity(){
    //TODO check internet permissions and make its request
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestCode = 100
    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(
            this,
            AppViewModelFactory(application)
        )
            .get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
        bind()
    }

    private fun bind() {
        val activityBinding = ActivityMainBinding.inflate(
            layoutInflater
        )
        val toolbar = activityBinding.toolbar
        setSupportActionBar(toolbar)
        val view: View = activityBinding.root
        setContentView(view)
        val bottomNavigationView = activityBinding.bottomNavigationView
        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(navController)
    }

    fun requestPermissions() {
        ActivityCompat
            .requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                requestCode
            )
    }

    private fun initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.i("TAG", "initLocation: ")
        val request = LocationRequest.create().apply {
            interval = 10 * 1000
            fastestInterval = 5 * 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val lastLocation: Location? = locationResult.lastLocation
                    if (lastLocation != null) {
                        val latLon = LatLon(lastLocation.latitude, lastLocation.longitude)
                        Log.i(
                            "TAG",
                            "getCurrentLocation: lat: ${lastLocation.latitude} ; lon: ${lastLocation.longitude}"
                        )
                        weatherViewModel.location.postValue(latLon)
                    }
                }
            }, null)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        initLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_LONG).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                Log.i("TAG", "onOptionsItemSelected: search")
                return true

            }
            R.id.action_location -> {
                Log.i("TAG", "onOptionsItemSelected: location")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}