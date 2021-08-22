package com.desuzed.clocknweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.mvvm.WeatherViewModel
import com.desuzed.clocknweather.util.adapters.DailyAdapter
import com.desuzed.clocknweather.util.adapters.HourlyAdapter
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar

class WeatherFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    val requestCode: Int = 100
    lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val weatherViewModel: WeatherViewModel = ViewModelProvider(
            this,
            ViewModelProvider
                .AndroidViewModelFactory.getInstance(requireActivity().application)
        )
            .get(WeatherViewModel::class.java)
        val tvCommonInfo = view.findViewById<TextView>(R.id.tvCommonInfo)
        val tvCurrentWeather = view.findViewById<TextView>(R.id.tvCurrentWeather)
        val etCity = view.findViewById<EditText>(R.id.etCity)
        val btnGetCityWeather = view.findViewById<Button>(R.id.bthGetWeather)
        val btnGpsWeather = view.findViewById<Button>(R.id.btnGpsWeather)
        btnGpsWeather.setOnClickListener {
            getCurrentLocation(weatherViewModel)
        }
        val rvDaily = view.findViewById<RecyclerView>(R.id.rvDaily)
        val rvHourly = view.findViewById<RecyclerView>(R.id.rvHourly)
        val lmDaily = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val lmHourly = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvDaily.layoutManager = lmDaily
        rvHourly.layoutManager = lmHourly
        val hourlyAdapter = HourlyAdapter(ArrayList(), requireContext())
        val dailyAdapter = DailyAdapter(ArrayList(), requireContext())
        rvHourly.adapter = hourlyAdapter
        rvDaily.adapter = dailyAdapter
        requestPermissions()


        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, {
            tvCommonInfo.text = it.toString()
            tvCurrentWeather.text = it.current.toString()
            dailyAdapter.updateList(it.daily!!, it)
            hourlyAdapter.updateList(it.hourly!!, it)

        })

    }

    @SuppressLint("ShowToast")
    private fun getCurrentLocation(weatherViewModel: WeatherViewModel) {
        val request = LocationRequest.create().apply {
            interval = 100000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location: Location? = locationResult.lastLocation
                    if (location != null) {
                        Log.i("TAG", "callForLocation: lat: ${location.latitude} ; lon: ${location.longitude}")
                        weatherViewModel.getOnecallForecast(location.latitude, location.longitude)
                    }
                }
            }, null)
        }else {
            val snackbar = Snackbar.make(requireView(), "Требуется разрешение на местоположение", Snackbar.LENGTH_LONG)
            snackbar.setAction("Запросить") {
                requestPermissions()
            }
            snackbar.show()
        }

    }

//    private fun isPermissionGranted(): Boolean {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED &&
//            ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            return true
//        }
//        return false
//    }

    private fun requestPermissions() {
        ActivityCompat
            .requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                requestCode
            )
    }

    //
    companion object {
        fun newInstance(): WeatherFragment {
            return WeatherFragment()
        }
    }
}