package com.desuzed.clocknweather.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.desuzed.clocknweather.App
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.adapters.HourAdapter
import com.desuzed.clocknweather.databinding.FragmentWeatherMainBinding
import com.desuzed.clocknweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.clocknweather.mvvm.vm.AppViewModelFactory
import com.desuzed.clocknweather.mvvm.vm.LocationViewModel
import com.desuzed.clocknweather.mvvm.vm.NetworkViewModel
import com.desuzed.clocknweather.mvvm.vm.SharedViewModel
import com.desuzed.clocknweather.network.dto.WeatherApi
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherMainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentWeatherBinding = FragmentWeatherMainBinding.inflate(inflater, container, false)
        return fragmentWeatherBinding.root
    }

    //private val tenAdapter by lazy { TenDaysRvAdapter() }
    private val hourAdapter by lazy { HourAdapter() }
    private val networkViewModel: NetworkViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance)
        )
            .get(NetworkViewModel::class.java)
    }
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance)
        )
            .get(LocationViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclers()
        observeLiveData()
        tvNextDays.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_nextDaysBottomSheet)

        }

    }


    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("dd.MM.yy\nHH:mm")

    @SuppressLint("SetTextI18n")
    private fun observeLiveData() {
        networkViewModel.weatherApiLiveData.observe(viewLifecycleOwner, weatherObserver)
        sharedViewModel.saveLocationVisibility.observe(viewLifecycleOwner, saveLocationObserver)
    }

    private val weatherObserver = Observer<WeatherApi?> { response ->
        if (response == null) {
            toggleEmptyWeatherData(true)
            return@Observer
        }else{
            toggleEmptyWeatherData(false)
            updateUi(response)
        }
    }

    private val saveLocationObserver = Observer<Boolean> {
        when (it) {
            true -> tvSaveLocation.visibility = View.VISIBLE
            false -> tvSaveLocation.visibility = View.GONE
        }
    }


    //TODO refactor to mapper
    @SuppressLint("SetTextI18n")
    private fun updateUi(response: WeatherApi) {
        //TODO remove to mapper
        val lat = response.locationDto?.lat
        val lon = response.locationDto?.lon
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.CEILING
        val favoriteLocation = FavoriteLocationDto(
            "${df.format(lat)},${df.format(lon)}",
            response.locationDto?.name.toString(),
            response.locationDto?.region.toString(),
            response.locationDto?.country.toString(),
            response.locationDto?.lat.toString(),
            response.locationDto?.lon.toString()
        )
        tvSaveLocation.setOnClickListener {
            locationViewModel.insert(favoriteLocation)
            sharedViewModel.toggleSaveButton(false)
        }

        val date = response.locationDto?.localtime_epoch?.times(1000)
        val timeZone = response.locationDto?.tzId.toString()
        val current = response.current
        val forecastDay = response.forecast?.forecastday!![0]
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        Glide.with(this).load("https:${current?.condition?.icon}").into(ivIcon)
        hourAdapter.updateList(
            networkViewModel.generateCurrentDayList(date, response, timeZone),
            timeZone
        )
//        tenAdapter.updateList(
//            response.forecast?.forecastday!!,
//            response.locationDto?.tzId.toString()
//        )
        tvDate.text = sdf.format(date)
        //TODO Есть места у которых нет региона и тогда будет запятая с пробелом после города
        tvPlace.text =
            "${response.locationDto?.name}, ${response.locationDto?.region}"
        tvCurrentTemp.text =
            current?.temp?.roundToInt().toString() + resources.getString(R.string.celsius)
        tvDescription.text = current?.condition?.text
        tvFeelsLike.text = resources.getString(R.string.feels_like) +
                current?.feelsLike?.roundToInt() + resources.getString(R.string.celsius)
        tvHumidity.text = "${current?.humidity.toString()}%"
        tvPressure.text = "${current?.pressureMb.toString()} mb"
        tvPop.text =
            "${forecastDay.day?.popRain.toString()}%, ${current?.precipMm} mm" //TODO обработать снежные осадки
        tvWind.text = "${current?.windSpeed} km/h, ${current?.windDir}"
        tvSun.text = "${forecastDay.astro?.sunrise}\n${forecastDay.astro?.sunset}"
        tvMoon.text = "${forecastDay.astro?.moonrise}\n${forecastDay.astro?.moonset}"

    }


    private fun initRecyclers() {
        val rvHour = fragmentWeatherBinding.rvHourly
        val lvHour = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvHour.layoutManager = lvHour
        rvHour.adapter = hourAdapter
    }

    private lateinit var fragmentWeatherBinding: FragmentWeatherMainBinding
    private val tvFeelsLike: TextView by lazy { fragmentWeatherBinding.tvFeelsLike }
    private val tvSaveLocation: TextView by lazy { fragmentWeatherBinding.tvSaveLocation }
    private val tvDate: TextView by lazy { fragmentWeatherBinding.tvDate }
    private val tvPlace: TextView by lazy { fragmentWeatherBinding.tvPlace }
    private val tvCurrentTemp: TextView by lazy { fragmentWeatherBinding.tvCurrentTemp }
    private val tvDescription: TextView by lazy { fragmentWeatherBinding.tvDescription }
    private val tvHumidity: TextView by lazy { fragmentWeatherBinding.includedContainer.tvHumidityMain }
    private val tvPressure: TextView by lazy { fragmentWeatherBinding.includedContainer.tvPressureMain }
    private val tvSun: TextView by lazy { fragmentWeatherBinding.includedContainer.tvSunMain }
    private val tvPop: TextView by lazy { fragmentWeatherBinding.includedContainer.tvPopMain }
    private val tvWind: TextView by lazy { fragmentWeatherBinding.includedContainer.tvWindMain }
    private val tvMoon: TextView by lazy { fragmentWeatherBinding.includedContainer.tvMoonMain }
    private val tvNextDays: TextView by lazy { fragmentWeatherBinding.tvNextDaysHeader }
    private val tvWeatherData: TextView by lazy { fragmentWeatherBinding.tvWeatherData }
    private val scrollViewWeather: NestedScrollView by lazy { fragmentWeatherBinding.scrollViewWeather }
    private val ivIcon: ImageView by lazy { fragmentWeatherBinding.imgIcon }


    private fun toggleEmptyWeatherData (isWeatherDataEmpty : Boolean){
        when (isWeatherDataEmpty){
            true ->{
                scrollViewWeather.visibility = View.GONE
                tvWeatherData.visibility = View.VISIBLE
            }
            false ->{
                scrollViewWeather.visibility = View.VISIBLE
                tvWeatherData.visibility = View.GONE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        networkViewModel.weatherApiLiveData.removeObserver(weatherObserver)
        sharedViewModel.saveLocationVisibility.removeObserver(saveLocationObserver)
    }

}