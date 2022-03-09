package com.desuzed.everyweather.view.fragments.weather

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.databinding.FragmentWeatherMainBinding
import com.desuzed.everyweather.model.Event
import com.desuzed.everyweather.model.entity.UserLatLng
import com.desuzed.everyweather.model.entity.WeatherResponse
import com.desuzed.everyweather.view.AppViewModelFactory
import com.desuzed.everyweather.view.MainActivity
import com.desuzed.everyweather.view.StateUI
import com.desuzed.everyweather.view.adapters.HourAdapter
import com.desuzed.everyweather.view.entity.WeatherEntityView
import com.desuzed.everyweather.view.fragments.navigate
import com.desuzed.everyweather.view.fragments.toast
import kotlinx.android.synthetic.main.fragment_weather_main.*
import java.util.*

class WeatherMainFragment : Fragment() {
    private lateinit var binding: FragmentWeatherMainBinding
    private val hourAdapter by lazy { HourAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(WeatherViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        observeLiveData()
        setOnClickListeners()
        setClickableUrl()
        resolveArguments()
        binding.swipeRefresh.setOnRefreshListener {
            weatherViewModel.queryLiveData.value?.let {
                getQueryForecast(it)
            }
        }
    }

    private var loadForecastByUserLocation = false
    private fun resolveArguments() {
        loadForecastByUserLocation = arguments?.getBoolean(USER_LOCATION) ?: false
        val query = arguments?.getString(QUERY_KEY)
        if (!query.isNullOrEmpty()) {
            getQueryForecast(query)
            arguments?.remove(QUERY_KEY)
        }
    }

    private val stateObserver = Observer<Event<StateUI>> {
        when (it.getContentIfNotHandled()) {
            is StateUI.Loading -> {
                launchRefresh(true)
            }
            is StateUI.Success -> {
                onSuccess(it.peekContent() as StateUI.Success)
            }
            is StateUI.Error -> {
                onError(it.peekContent() as StateUI.Error)
            }
            is StateUI.NoData -> {
                launchRefresh(false)
                toggleSaveButton(false)
            }
        }
    }

    private fun onSuccess(success: StateUI.Success) {
        if (success.toggleSaveButton) toggleSaveButton(true)
        else toggleSaveButton(false)
        val message = success.message
        if (!message.isNullOrEmpty()) {
            toast(message)
        }
        launchRefresh(false)
    }

    private fun onError(error: StateUI.Error) {
        launchRefresh(false)
        toggleSaveButton(false)
        toast(error.message)
    }

    private fun toggleSaveButton(state: Boolean) {
        weatherViewModel.toggleSaveButton(state)
    }

    private fun getQueryForecast(query: String) {
        weatherViewModel.getForecast(query)
    }

    private fun launchRefresh(state: Boolean) {
        binding.swipeRefresh.isRefreshing = state
    }

    private fun setClickableUrl() {
        tvPoweredBy.movementMethod = LinkMovementMethod.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvPoweredBy.text = Html.fromHtml(getString(R.string.powered_by), Html.FROM_HTML_MODE_LEGACY)
        } else {
            tvPoweredBy.text = Html.fromHtml(getString(R.string.powered_by))
        }
    }


    private fun setOnClickListeners() {
        btnNextDaysForecast.setOnClickListener {
            navigate(R.id.action_weatherFragment_to_nextDaysBottomSheet)
        }
        binding.tvPlace.setOnClickListener {
            navigate(R.id.action_weatherFragment_to_locationFragment)
        }
        binding.btnToLocation.setOnClickListener {
            navigate(R.id.action_weatherFragment_to_locationFragment)
        }
    }


    private fun observeLiveData() {
        weatherViewModel.stateLiveData.observe(viewLifecycleOwner, stateObserver)
        weatherViewModel.weatherApiLiveData.observe(viewLifecycleOwner, weatherObserver)
        weatherViewModel.toggleLocationVisibility.observe(viewLifecycleOwner, saveLocationObserver)
        (activity as MainActivity).getUserLatLngLiveData()
            .observe(viewLifecycleOwner, locationObserver)
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
            true -> fabAddLocation.visibility = View.VISIBLE
            false -> fabAddLocation.visibility = View.GONE
        }
    }

    private val locationObserver = Observer<UserLatLng> { location ->
        if (!loadForecastByUserLocation) return@Observer
        else {
            getQueryForecast(location.toString())
        }
    }

    private fun updateUi(response: WeatherResponse) {
        val weatherEntityView = WeatherEntityView (response, resources)
        val favoriteLocation = weatherEntityView.buildFavoriteLocationObj()
        fabAddLocation.setOnClickListener {
            weatherViewModel.insert(favoriteLocation)
            weatherViewModel.toggleSaveButton(false)
        }
        Glide
            .with(this)
            .load(weatherEntityView.iconUrl)
            .into(binding.imgIcon)
        hourAdapter.submitList(
            weatherEntityView.generateCurrentDayList(),
            weatherEntityView.timeZone
        )
        binding.rvHourly.startLayoutAnimation()
        binding.tvDate.text = weatherEntityView.date
        binding.tvTime.text = weatherEntityView.time
        binding.tvPlace.text = weatherEntityView.place
        binding.tvCurrentTemp.text = weatherEntityView.currentTemp
        binding.tvDescription.text = weatherEntityView.description
        binding.tvFeelsLike.text = weatherEntityView.feelsLike
        binding.includedContainer.tvHumidityMain.text = weatherEntityView.humidity
        binding.includedContainer.tvPressureMain.text = weatherEntityView.pressure
        binding.includedContainer.tvPopMain.text = weatherEntityView.pop
        binding.includedContainer.tvWindMain.text = weatherEntityView.wind
        binding.includedContainer.tvSunMain.text = weatherEntityView.sun
        binding.includedContainer.tvMoonMain.text = weatherEntityView.moon
    }


    private fun initRecycler() {
        binding.rvHourly.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHourly.adapter = hourAdapter
    }


    private fun toggleEmptyWeatherData(isWeatherDataEmpty: Boolean) {
        when (isWeatherDataEmpty) {
            true -> {
                clMainWeather.visibility = View.GONE
                containerNoData.visibility = View.VISIBLE
                weatherViewModel.toggleSaveButton(false)
            }
            false -> {
                clMainWeather.visibility = View.VISIBLE
                containerNoData.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loadForecastByUserLocation = false
        arguments?.remove(USER_LOCATION)
    }

    companion object {
        const val QUERY_KEY = "QUERY"
        const val USER_LOCATION = "USER_LOCATION"
    }

}