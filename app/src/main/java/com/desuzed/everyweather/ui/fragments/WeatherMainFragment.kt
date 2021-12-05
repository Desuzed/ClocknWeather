package com.desuzed.everyweather.ui.fragments

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
import com.desuzed.everyweather.adapters.HourAdapter
import com.desuzed.everyweather.databinding.FragmentWeatherMainBinding
import com.desuzed.everyweather.mvvm.model.WeatherResponse
import com.desuzed.everyweather.mvvm.vm.AppViewModelFactory
import com.desuzed.everyweather.mvvm.vm.SharedViewModel
import com.desuzed.everyweather.util.editor.WeatherFragEditor
import com.desuzed.everyweather.util.navigate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_weather_main.*
import java.util.*

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
        fragmentWeatherBinding.tvPlace.setOnClickListener {
            navigate(R.id.action_weatherFragment_to_locationFragment)
        }
        fragmentWeatherBinding.btnToLocation.setOnClickListener {
            navigate(R.id.action_weatherFragment_to_locationFragment)
        }
    }


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
            .into(imgIcon)
        hourAdapter.updateList(
            editor.generateCurrentDayList(),
            editor.timeZone
        )
        rvHour.startLayoutAnimation()
        tvDate.text = resultMap["tvDate"]
        tvTime.text = resultMap["tvTime"]
        tvPlace.text = resultMap["tvPlace"]
        tvCurrentTemp.text = resultMap["tvCurrentTemp"]
        tvDescription.text = resultMap["tvDescription"]
        tvFeelsLike.text = resultMap["tvFeelsLike"]
        tvHumidity.text = resultMap["tvHumidity"]
        tvPressure.text = resultMap["tvPressure"]
        tvPop.text = resultMap["tvPop"]
        tvWind.text = resultMap["tvWind"]
        tvSun.text = resultMap["tvSun"]
        tvMoon.text = resultMap["tvMoon"]
    }


    private fun initRecyclers() {
        rvHour = fragmentWeatherBinding.rvHourly
        val lvHour = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvHour.layoutManager = lvHour
        rvHour.adapter = hourAdapter
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

    private lateinit var rvHour: RecyclerView
    private lateinit var fragmentWeatherBinding: FragmentWeatherMainBinding
    private lateinit var tvFeelsLike: TextView
    private lateinit var fabAddLocation: FloatingActionButton
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
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
    private lateinit var btnNextDaysForecast: TextView
    private lateinit var containerNoData: CardView
    private lateinit var clMainWeather: LinearLayout
    private lateinit var imgIcon: ImageView

    private fun bind() {
        tvDate = fragmentWeatherBinding.tvDate
        tvTime = fragmentWeatherBinding.tvTime
        tvPlace = fragmentWeatherBinding.tvPlace
        tvCurrentTemp = fragmentWeatherBinding.tvCurrentTemp
        tvDescription = fragmentWeatherBinding.tvDescription
        tvFeelsLike = fragmentWeatherBinding.tvFeelsLike
        fabAddLocation = fragmentWeatherBinding.fabAddLocation
        tvHumidity = fragmentWeatherBinding.includedContainer.tvHumidityMain
        tvPressure = fragmentWeatherBinding.includedContainer.tvPressureMain
        tvSun = fragmentWeatherBinding.includedContainer.tvSunMain
        tvPop = fragmentWeatherBinding.includedContainer.tvPopMain
        tvWind = fragmentWeatherBinding.includedContainer.tvWindMain
        tvMoon = fragmentWeatherBinding.includedContainer.tvMoonMain
        imgIcon = fragmentWeatherBinding.imgIcon
        btnNextDaysForecast = fragmentWeatherBinding.btnNextDaysForecast
        tvPoweredBy = fragmentWeatherBinding.tvPoweredBy
        clMainWeather = fragmentWeatherBinding.clMainWeather
        containerNoData = fragmentWeatherBinding.containerNoData
    }


    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.weatherApiLiveData.removeObserver(weatherObserver)
        sharedViewModel.saveLocationVisibility.removeObserver(saveLocationObserver)
    }

}