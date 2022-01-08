package com.desuzed.everyweather.view.fragments

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.view.adapters.HourAdapter
import com.desuzed.everyweather.databinding.FragmentWeatherMainBinding
import com.desuzed.everyweather.model.model.WeatherResponse
import com.desuzed.everyweather.model.vm.AppViewModelFactory
import com.desuzed.everyweather.model.vm.SharedViewModel
import com.desuzed.everyweather.model.vm.WeatherViewModel
import com.desuzed.everyweather.util.editor.WeatherFragEditor
import com.desuzed.everyweather.view.StateUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_weather_main.*
import java.util.*

class WeatherMainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    private lateinit var binding: FragmentWeatherMainBinding

    private val hourAdapter by lazy { HourAdapter() }

    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(SharedViewModel::class.java)
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
       // bind()
        initRecyclers()
        observeLiveData()
        setOnClickListeners()
        setClickableUrl()


        binding.swipeRefreshWeather.setOnRefreshListener {
            sharedViewModel.queryLiveData.value?.let { weatherViewModel.getForecast(it)
            }
        }
    }

    private fun setClickableUrl() {
        binding.tvPoweredBy.setMovementMethod(LinkMovementMethod.getInstance())
        binding.tvPoweredBy.setText(Html.fromHtml(getString(R.string.powered_by)))
    }


    private fun setOnClickListeners() {
        binding.btnNextDaysForecast.setOnClickListener {
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
        weatherViewModel.weatherApiLiveData.observe(viewLifecycleOwner, weatherObserver)
        weatherViewModel.toggleLocationVisibility.observe(viewLifecycleOwner, saveLocationObserver)
        weatherViewModel.stateLiveData.observe(viewLifecycleOwner, stateObserver)


        sharedViewModel.queryLiveData.observe(viewLifecycleOwner, queryObserver)

    }

    private val queryObserver = Observer<String> {
        weatherViewModel.getForecast(it)
    }


    private val stateObserver = Observer<StateUI> {
        when (it) {
            is StateUI.Loading -> {
                launchRefresh(true)
            }
            is StateUI.Success -> {
                onSuccess(it)
            }
            is StateUI.Error -> {
                onError(it)
            }
            is StateUI.NoData -> {
                launchRefresh(false)
                toggleSaveButton(false)
            }
        }
    }

    private fun launchRefresh(state: Boolean) {
        binding.swipeRefreshWeather.isRefreshing = state
    }

    private fun onSuccess(it: StateUI.Success) {
        if (it.toggleSaveButton) toggleSaveButton(true)
        else toggleSaveButton(false)
        if (it.message.isNotEmpty()) {
            toast(it.message)
        }
        launchRefresh(false)
    }

    private fun onError(it: StateUI.Error) {
        launchRefresh(false)
        toggleSaveButton(false)
        toast(it.message)
    }

    private fun toggleSaveButton(state: Boolean) {
        weatherViewModel.toggleSaveButton(state)
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
        val fab = binding.fabAddLocation
        when (it) {
            true -> fab.visibility = View.VISIBLE
            false -> fab.visibility = View.GONE
        }
    }

    private fun updateUi(response: WeatherResponse) {
        val editor = WeatherFragEditor(response, requireContext())
        val favoriteLocation = editor.buildFavoriteLocationObj()
        binding.fabAddLocation.setOnClickListener {
            weatherViewModel.insert(favoriteLocation)
            weatherViewModel.toggleSaveButton(false)
        }
        val resultMap = editor.getResultMap()
        Glide
            .with(this)
            .load(resultMap["imgIcon"])
            .into(binding.imgIcon)
        hourAdapter.updateList(
            editor.generateCurrentDayList(),
            editor.timeZone
        )
        binding.rvHourly.startLayoutAnimation()
        binding.tvDate.text = resultMap["tvDate"]
        binding.tvTime.text = resultMap["tvTime"]
        binding.tvPlace.text = resultMap["tvPlace"]
        binding.tvCurrentTemp.text = resultMap["tvCurrentTemp"]
        binding.tvDescription.text = resultMap["tvDescription"]
        binding.tvFeelsLike.text = resultMap["tvFeelsLike"]
        binding.includedContainer.tvHumidityMain.text = resultMap["tvHumidity"]
        binding.includedContainer.tvPressureMain.text = resultMap["tvPressure"]
        binding.includedContainer.tvPopMain.text = resultMap["tvPop"]
        binding.includedContainer.tvWindMain.text = resultMap["tvWind"]
        binding.includedContainer.tvSunMain.text = resultMap["tvSun"]
        binding.includedContainer.tvMoonMain.text = resultMap["tvMoon"]
    }


    private fun initRecyclers() {
        val lvHour = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHourly.layoutManager = lvHour
        binding.rvHourly.adapter = hourAdapter
    }


    private fun toggleEmptyWeatherData(isWeatherDataEmpty: Boolean) {
        when (isWeatherDataEmpty) {
            true -> {
                binding.clMainWeather.visibility = View.GONE
                binding.containerNoData.visibility = View.VISIBLE
                weatherViewModel.toggleSaveButton(false)
            }
            false -> {
                binding.clMainWeather.visibility = View.VISIBLE
                binding.containerNoData.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        weatherViewModel.weatherApiLiveData.removeObserver(weatherObserver)
        weatherViewModel.toggleLocationVisibility.removeObserver(saveLocationObserver)
        weatherViewModel.stateLiveData.removeObserver(stateObserver)

        sharedViewModel.queryLiveData.removeObserver(queryObserver)
    }

//    private lateinit var tvFeelsLike: TextView
//    private lateinit var fabAddLocation: FloatingActionButton
//    private lateinit var tvDate: TextView
//    private lateinit var tvTime: TextView
//    private lateinit var tvPlace: TextView
//    private lateinit var tvCurrentTemp: TextView
//    private lateinit var tvDescription: TextView
//    private lateinit var tvPoweredBy: TextView
//    private lateinit var tvHumidity: TextView
//    private lateinit var tvPressure: TextView
//    private lateinit var tvSun: TextView
//    private lateinit var tvPop: TextView
//    private lateinit var tvWind: TextView
//    private lateinit var tvMoon: TextView
//    private lateinit var btnNextDaysForecast: TextView
//    private lateinit var containerNoData: CardView
//    private lateinit var clMainWeather: LinearLayout
//    private lateinit var imgIcon: ImageView

 //   private fun bind() {
//        tvDate = binding.tvDate
//        tvTime = binding.tvTime
//        tvPlace = binding.tvPlace
//        tvCurrentTemp = binding.tvCurrentTemp
//        tvDescription = binding.tvDescription
//        tvFeelsLike = binding.tvFeelsLike
//        fabAddLocation = binding.fabAddLocation
//        tvHumidity = binding.includedContainer.tvHumidityMain
//        tvPressure = binding.includedContainer.tvPressureMain
//        tvSun = binding.includedContainer.tvSunMain
//        tvPop = binding.includedContainer.tvPopMain
//        tvWind = binding.includedContainer.tvWindMain
//        tvMoon = binding.includedContainer.tvMoonMain
//        imgIcon = binding.imgIcon
//        btnNextDaysForecast = binding.btnNextDaysForecast
//        tvPoweredBy = binding.tvPoweredBy
//        clMainWeather = binding.clMainWeather
//        containerNoData = binding.containerNoData
//    }
}