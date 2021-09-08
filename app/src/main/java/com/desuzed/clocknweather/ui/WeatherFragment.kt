package com.desuzed.clocknweather.ui

import  android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.desuzed.clocknweather.databinding.FragmentWeatherBinding
import com.desuzed.clocknweather.mvvm.AppViewModelFactory
import com.desuzed.clocknweather.mvvm.Repository
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
    ): View {
        fragmentWeatherBinding = FragmentWeatherBinding.inflate(inflater, container, false)
        return fragmentWeatherBinding.root
    }

    val requestCode: Int = 100
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var fragmentWeatherBinding: FragmentWeatherBinding
    private lateinit var tvCache: TextView
    private lateinit var tvCommonInfo: TextView
    private lateinit var tvCurrentWeather: TextView

    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(
            this,
            AppViewModelFactory(Repository(requireActivity().application))
        )
            .get(WeatherViewModel::class.java)
    }

    private fun initRecyclers(hourlyAdapter: HourlyAdapter, dailyAdapter: DailyAdapter) {
        val rvDaily = fragmentWeatherBinding.rvDaily
        val rvHourly = fragmentWeatherBinding.rvHourly
        val lmDaily = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val lmHourly = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvDaily.layoutManager = lmDaily
        rvHourly.layoutManager = lmHourly
        rvHourly.adapter = hourlyAdapter
        rvDaily.adapter = dailyAdapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val hourlyAdapter = HourlyAdapter(ArrayList(), requireContext())
        val dailyAdapter = DailyAdapter(ArrayList(), requireContext())
        initRecyclers(hourlyAdapter, dailyAdapter)
        requestPermissions()
        observeLiveData(dailyAdapter, hourlyAdapter)
        initLocation()
    }

    private fun observeLiveData(dailyAdapter: DailyAdapter, hourlyAdapter: HourlyAdapter) {
        weatherViewModel.oneCallLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                tvCommonInfo.text = it.toString()
                tvCurrentWeather.text = it.current.toString()
                dailyAdapter.updateList(it.daily!!, it)
                hourlyAdapter.updateList(it.hourly!!, it)
            }
        })
        // getCurrentLocation(weatherViewModel)
        weatherViewModel.errorMessage.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
        weatherViewModel.loadMessage.observe(viewLifecycleOwner, {
            tvCache.text = it
        })

        weatherViewModel.fiveDayForecastLiveData.observe(viewLifecycleOwner, {
            Log.i("TAG", "observeLiveData: success ")
        })
//        weatherViewModel.location.observe(viewLifecycleOwner, {
//            weatherViewModel.getOnecallForecast(it)
//        })
        weatherViewModel.getCachedForecast()
    }

     private fun bind() {
        tvCommonInfo = fragmentWeatherBinding.tvCommonInfo
        tvCurrentWeather = fragmentWeatherBinding.tvCurrentWeather
        tvCache = fragmentWeatherBinding.tvCache
        val btnGpsWeather = fragmentWeatherBinding.btnGpsWeather
        btnGpsWeather.setOnClickListener {
            getOnecallForecast()
        }
        val etCity = fragmentWeatherBinding.etCity
        val btnGetFiveDayForecast = fragmentWeatherBinding.btnGetFiveDayForecast

        btnGetFiveDayForecast.setOnClickListener {
            val city = etCity.text.toString()
            weatherViewModel.getFiveDayForecast(city)
        }
    }

    private fun initLocation() {
        val request = LocationRequest.create().apply {
            interval =10*1000
            fastestInterval = 5*1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location: Location? = locationResult.lastLocation
                    if (location != null) {
                        Log.i(
                            "TAG",
                            "getCurrentLocation: lat: ${location.latitude} ; lon: ${location.longitude}"
                        )
//                        Toast.makeText(
//                            requireContext(),
//                            "lat: ${location.latitude} ; lon: ${location.longitude}",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        weatherViewModel.location.postValue(location)
                    }
                }
            }, null)
        } else {
            val snackbar = Snackbar.make(
                requireView(),
                "Требуется разрешение на местоположение",
                Snackbar.LENGTH_LONG
            )
            snackbar.setAction("Запросить") {
                requestPermissions()
            }
            snackbar.show()
        }
    }

    private fun getOnecallForecast() {
        weatherViewModel.getOnecallForecast(weatherViewModel.location.value!!)
    }

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