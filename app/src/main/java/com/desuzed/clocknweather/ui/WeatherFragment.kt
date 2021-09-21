package com.desuzed.clocknweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.databinding.FragmentWeatherBinding
import com.desuzed.clocknweather.mvvm.AppViewModelFactory
import com.desuzed.clocknweather.mvvm.WeatherViewModel
import com.desuzed.clocknweather.util.adapters.HourAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentWeatherBinding = FragmentWeatherBinding.inflate(inflater, container, false)
        return fragmentWeatherBinding.root
    }

    private lateinit var fragmentWeatherBinding: FragmentWeatherBinding
    private lateinit var tvFeelsLike: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvPlace: TextView
    private lateinit var tvCurrentTemp: TextView
    private lateinit var tvDescription: TextView

    private lateinit var tvHumidity: TextView
    private lateinit var tvPressure: TextView
    private lateinit var tvSun: TextView
    private lateinit var tvPop: TextView
    private lateinit var tvWind: TextView
    private lateinit var tvMoon: TextView

    private lateinit var ivIcon: ImageView

    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(requireActivity().application)
        )
            .get(WeatherViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        val hourAdapter = HourAdapter(requireContext())
        initRecycler(hourAdapter)
        observeLiveData(hourAdapter)
        weatherViewModel.getCachedForecast()
    }

    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("dd.MM.yy\nHH:mm")

    @SuppressLint("SetTextI18n")
    private fun observeLiveData(hourAdapter: HourAdapter) {
        //TODO refactoring
        weatherViewModel.weatherApiLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                val date = it.location?.localtime_epoch?.times(1000)
                val timeZone = it.location?.tzId.toString()
                val current = it.current
                val forecastDay = it.forecast?.forecastday!![0]
                sdf.timeZone = TimeZone.getTimeZone(timeZone)
                Glide.with(this).load("https:${current?.condition?.icon}").into(ivIcon)
                hourAdapter.updateList(
                    weatherViewModel.generateList(date, it, timeZone),
                    timeZone
                )
                tvDate.text = sdf.format(date)
                tvPlace.text =
                    "${it.location?.name}, ${it.location?.region}"
                tvCurrentTemp.text =
                    current?.temp?.roundToInt().toString() + resources.getString(R.string.celsius)
                tvDescription.text = current?.condition?.text
                tvFeelsLike.text =
                    "Fells like:${current?.feelsLike?.roundToInt()}" + resources.getString(R.string.celsius)
                tvHumidity.text = "${current?.humidity.toString()}%"
                tvPressure.text = "${current?.pressureMb.toString()} mb"
                tvPop.text = "${forecastDay.day?.popRain.toString()}%, ${current?.precipMm} mm" //TODO обработать снежные осадки
                tvWind.text = "${current?.windSpeed} km/h, ${current?.windDir}"
                tvSun.text = "${forecastDay.astro?.sunrise}\n${forecastDay.astro?.sunset}"
                tvMoon.text = "${forecastDay.astro?.moonrise}\n${forecastDay.astro?.moonset}"
            }
        })

        weatherViewModel.errorMessage.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun initRecycler(hourlyAdapter: HourAdapter) {
        val rvHour = fragmentWeatherBinding.rvHourly
        val lvHour = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvHour.layoutManager = lvHour
        rvHour.adapter = hourlyAdapter
    }

    private fun bind() {
        tvDate = fragmentWeatherBinding.tvDate
        tvPlace = fragmentWeatherBinding.tvPlace
        tvCurrentTemp = fragmentWeatherBinding.tvCurrentTemp
        tvDescription = fragmentWeatherBinding.tvDescription
        tvFeelsLike = fragmentWeatherBinding.tvFeelsLike
        tvHumidity = fragmentWeatherBinding.includedContainer.tvHumidityMain
        tvPressure = fragmentWeatherBinding.includedContainer.tvPressureMain
        tvSun = fragmentWeatherBinding.includedContainer.tvSunMain
        tvPop = fragmentWeatherBinding.includedContainer.tvPopMain
        tvWind = fragmentWeatherBinding.includedContainer.tvWindMain
        tvMoon = fragmentWeatherBinding.includedContainer.tvMoonMain
        ivIcon = fragmentWeatherBinding.imgIcon

        val btnGpsWeather = fragmentWeatherBinding.btnGpsWeather
        btnGpsWeather.setOnClickListener {
            getGpsForecast()
        }
        val etQuery = fragmentWeatherBinding.etQuery
        val btnGetForecast = fragmentWeatherBinding.btnGetForecast
        btnGetForecast.setOnClickListener {
            val city = etQuery.text.toString()
            weatherViewModel.getForecast(city)
        }
    }


    private fun getGpsForecast() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            weatherViewModel.getForecast("${weatherViewModel.location.value}")
        } else {
            Toast.makeText(
                requireContext(),
                "Требуется разрешение на местоположение",
                Toast.LENGTH_LONG
            )
                .show()
            (activity as MainActivity).requestPermissions()
        }

    }
}