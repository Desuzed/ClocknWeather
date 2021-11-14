package com.desuzed.everyweather.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.adapters.HourAdapter
import com.desuzed.everyweather.databinding.FragmentWeatherMainBinding
import com.desuzed.everyweather.mvvm.model.WeatherResponse
import com.desuzed.everyweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.everyweather.mvvm.vm.AppViewModelFactory
import com.desuzed.everyweather.mvvm.vm.SharedViewModel
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

    private val hourAdapter by lazy { HourAdapter() }

    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance)
        )
            .get(SharedViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        initRecyclers()
        observeLiveData()
        tvNextDays.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_nextDaysBottomSheet)

        }

        fragmentWeatherBinding.fabTest.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_locationFragment)
        }


        tvPoweredBy.setMovementMethod(LinkMovementMethod.getInstance())
        tvPoweredBy.setText(Html.fromHtml(getString(R.string.powered_by)))

    }


    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("dd.MM.yy\nHH:mm")

    @SuppressLint("SetTextI18n")
    private fun observeLiveData() {
        sharedViewModel.weatherApiLiveData.observe(viewLifecycleOwner, weatherObserver)
        sharedViewModel.saveLocationVisibility.observe(viewLifecycleOwner, saveLocationObserver)
    }

    private val weatherObserver = Observer<WeatherResponse?> { response ->
        if (response == null) {
            toggleEmptyWeatherData(true)
            return@Observer
        } else {
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


    @SuppressLint("SetTextI18n")
    private fun updateUi(response: WeatherResponse) {
        val lat = response.location.lat
        val lon = response.location.lon
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.CEILING
        val favoriteLocation = FavoriteLocationDto(
            "${df.format(lat)},${df.format(lon)}",
            response.location.name,
            response.location.region,
            response.location.country,
            response.location.lat.toString(),
            response.location.lon.toString()
        )
        tvSaveLocation.setOnClickListener {
            sharedViewModel.insert(favoriteLocation)
            sharedViewModel.toggleSaveButton(false)
        }

        val date = response.location.localtime_epoch.times(1000)
        val timeZone = response.location.tzId
        val current = response.current
        val forecastDay = response.forecastDay[0]
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        Glide.with(this).load("https:${current.icon}").into(ivIcon)
        hourAdapter.updateList(
            sharedViewModel.generateCurrentDayList(date, response, timeZone),
            timeZone
        )
        tvDate.text = sdf.format(date)
        //TODO Есть места у которых нет региона и тогда будет запятая с пробелом после города
        tvPlace.text =
            "${response.location.name}, ${response.location.region}"
        tvCurrentTemp.text =
            current.temp.roundToInt().toString() + resources.getString(R.string.celsius)
        tvDescription.text = current.text
        tvFeelsLike.text = resources.getString(R.string.feels_like) +
                current.feelsLike.roundToInt() + resources.getString(R.string.celsius)
        tvHumidity.text = "${current.humidity}%"
        tvPressure.text = "${current.pressureMb} mb"
        tvPop.text =
            "${forecastDay.day.popRain}%, ${current.precipMm} mm" //TODO обработать снежные осадки
        tvWind.text = "${current.windSpeed} km/h, ${current.windDir}"
        tvSun.text = "${forecastDay.astro.sunrise}\n${forecastDay.astro.sunset}"
        tvMoon.text = "${forecastDay.astro.moonrise}\n${forecastDay.astro.moonset}"

    }


    private fun initRecyclers() {
        val rvHour = fragmentWeatherBinding.rvHourly
        val lvHour = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvHour.layoutManager = lvHour
        rvHour.adapter = hourAdapter
    }


    private fun toggleEmptyWeatherData(isWeatherDataEmpty: Boolean) {
        when (isWeatherDataEmpty) {
            true -> {
                clMainWeather.visibility = View.GONE
                tvWeatherData.visibility = View.VISIBLE
            }
            false -> {
                clMainWeather.visibility = View.VISIBLE
                tvWeatherData.visibility = View.GONE
            }
        }
    }

    private lateinit var fragmentWeatherBinding: FragmentWeatherMainBinding
    private lateinit var tvFeelsLike: TextView
    private lateinit var tvSaveLocation: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvPlace: TextView
    private lateinit var tvCurrentTemp: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvPoweredBy: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvPressure: TextView
    private lateinit var tvSun: TextView
    private lateinit var tvPop: TextView
    private lateinit var tvWind: TextView
    private lateinit var tvMoon: TextView
    private lateinit var tvNextDays: TextView
    private lateinit var tvWeatherData: TextView
    private lateinit var clMainWeather: ConstraintLayout
    private lateinit var ivIcon: ImageView

    private fun bind() {
        tvDate = fragmentWeatherBinding.tvDate
        tvPlace = fragmentWeatherBinding.tvPlace
        tvCurrentTemp = fragmentWeatherBinding.tvCurrentTemp
        tvDescription = fragmentWeatherBinding.tvDescription
        tvFeelsLike = fragmentWeatherBinding.tvFeelsLike
        tvSaveLocation = fragmentWeatherBinding.tvSaveLocation
        tvHumidity = fragmentWeatherBinding.includedContainer.tvHumidityMain
        tvPressure = fragmentWeatherBinding.includedContainer.tvPressureMain
        tvSun = fragmentWeatherBinding.includedContainer.tvSunMain
        tvPop = fragmentWeatherBinding.includedContainer.tvPopMain
        tvWind = fragmentWeatherBinding.includedContainer.tvWindMain
        tvMoon = fragmentWeatherBinding.includedContainer.tvMoonMain
        ivIcon = fragmentWeatherBinding.imgIcon
        tvNextDays = fragmentWeatherBinding.tvNextDaysHeader
        tvPoweredBy = fragmentWeatherBinding.tvPoweredBy
        clMainWeather = fragmentWeatherBinding.clMainWeather
        tvWeatherData = fragmentWeatherBinding.tvWeatherData
    }


    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.weatherApiLiveData.removeObserver(weatherObserver)
        sharedViewModel.saveLocationVisibility.removeObserver(saveLocationObserver)
    }

}