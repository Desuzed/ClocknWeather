package com.desuzed.everyweather.view.fragments.weather

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
import com.desuzed.everyweather.model.model.WeatherResponse
import com.desuzed.everyweather.util.editor.WeatherFragEditor
import com.desuzed.everyweather.view.AppViewModelFactory
import com.desuzed.everyweather.view.SharedViewModel
import com.desuzed.everyweather.view.adapters.HourAdapter
import com.desuzed.everyweather.view.fragments.navigate
import kotlinx.android.synthetic.main.fragment_weather_main.*
import java.util.*

class WeatherMainFragment : Fragment() {
    private lateinit var binding: FragmentWeatherMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val hourAdapter by lazy { HourAdapter() }

    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(SharedViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        observeLiveData()
        setOnClickListeners()
        setClickableUrl()
    }

    private fun setClickableUrl() {
        tvPoweredBy.setMovementMethod(LinkMovementMethod.getInstance())
        tvPoweredBy.setText(Html.fromHtml(getString(R.string.powered_by)))
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
        sharedViewModel.weatherApiLiveData.observe(viewLifecycleOwner, weatherObserver)
        sharedViewModel.toggleLocationVisibility.observe(viewLifecycleOwner, saveLocationObserver)
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

    private fun updateUi(response: WeatherResponse) {
        val editor = WeatherFragEditor(response, requireContext())
        val favoriteLocation = editor.buildFavoriteLocationObj()
        fabAddLocation.setOnClickListener {
            sharedViewModel.insert(favoriteLocation)
            sharedViewModel.toggleSaveButton(false)
        }
        val resultMap = editor.getResultMap()
        Glide
            .with(this)
            .load(resultMap["imgIcon"])
            .into(binding.imgIcon)
        hourAdapter.submitList(
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
                sharedViewModel.toggleSaveButton(false)
            }
            false -> {
                clMainWeather.visibility = View.VISIBLE
                containerNoData.visibility = View.GONE
            }
        }
    }

}