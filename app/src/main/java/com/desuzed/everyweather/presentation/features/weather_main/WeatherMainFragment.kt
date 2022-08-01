package com.desuzed.everyweather.presentation.features.weather_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.UserLatLng
import com.desuzed.everyweather.presentation.features.main_activity.MainActivity
import com.desuzed.everyweather.util.collect
import com.desuzed.everyweather.util.navigate
import com.desuzed.everyweather.util.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherMainFragment : Fragment() {
    private val viewModel by viewModel<WeatherMainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.state.collectAsState()
                WeatherMainContent(
                    state = state,
                    onUserInteraction = viewModel::onUserInteraction,
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resolveArguments()
        collect((activity as MainActivity).getUserLatLngFlow(), ::onNewLocation)
        collect(viewModel.action, ::onNewAction)
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
        viewModel.getForecast(query)
    }

    private fun onNewAction(action: WeatherMainAction) {
        when (action) {
            is WeatherMainAction.ShowToast -> toast(action.message)//todo избавиться от тостов
            WeatherMainAction.NavigateToLocation -> navigate(R.id.action_weatherFragment_to_locationFragment)
            WeatherMainAction.NavigateToNextDaysWeather -> navigate(R.id.action_weatherFragment_to_nextDaysBottomSheet)
        }
    }

    private fun onNewLocation(location: UserLatLng?) {
        if (!loadForecastByUserLocation) return
        else {
            if (location != null) {
                getQueryForecast(location.toString())
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