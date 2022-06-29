package com.desuzed.everyweather.view.fragments.weather

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.model.entity.UserLatLng
import com.desuzed.everyweather.view.AppViewModelFactory
import com.desuzed.everyweather.view.MainActivity
import com.desuzed.everyweather.view.fragments.navigate
import com.desuzed.everyweather.view.fragments.weather.main.WeatherMainContent
import com.desuzed.everyweather.view.fragments.weather.main.WeatherMainViewModel
import kotlinx.android.synthetic.main.fragment_weather_main.*

class WeatherMainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val state by weatherViewModel.state.collectAsState()
                WeatherMainContent(
                    state = state,
                    onNextDaysButtonCLick = {
                        navigate(R.id.action_weatherFragment_to_nextDaysBottomSheet)
                    },
                    onSaveLocationClick = {
                        weatherViewModel.saveLocation()
                        Log.i("TAG", "onCreateView: onClick ")
                    },
                    onLocationClick = {
                        navigate(R.id.action_weatherFragment_to_locationFragment)
                        Log.i("TAG", "onCreateView: onClick ")
                    },
                    onRefresh = { weatherViewModel.getForecast(weatherViewModel.state.value.query) }    //todo убрать во вью модель
                )
            }
        }
    }

    //todo koin
    private val weatherViewModel: WeatherMainViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(WeatherMainViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resolveArguments()
        //todo переделать
        (activity as MainActivity).getUserLatLngLiveData()
            .observe(viewLifecycleOwner, locationObserver)
    }

    //todo сделать через fragmentResult????
    private var loadForecastByUserLocation = false
    private fun resolveArguments() {
        loadForecastByUserLocation = arguments?.getBoolean(USER_LOCATION) ?: false
        val query = arguments?.getString(QUERY_KEY)
        if (!query.isNullOrEmpty()) {
            getQueryForecast(query)
            arguments?.remove(QUERY_KEY)
        }
    }

    private fun getQueryForecast(query: String) {
        weatherViewModel.getForecast(query)
    }

    private fun setClickableUrl() {
        tvPoweredBy.movementMethod = LinkMovementMethod.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvPoweredBy.text =
                Html.fromHtml(getString(R.string.powered_by), Html.FROM_HTML_MODE_LEGACY)
        } else {
            tvPoweredBy.text = Html.fromHtml(getString(R.string.powered_by))
        }
    }

    private val locationObserver = Observer<UserLatLng> { location ->
        if (!loadForecastByUserLocation) return@Observer
        else {
            getQueryForecast(location.toString())
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