package com.desuzed.everyweather.presentation.features.location_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.features.main_activity.MainActivity
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainFragment
import com.desuzed.everyweather.util.addOnBackPressedCallback
import com.desuzed.everyweather.util.collect
import com.desuzed.everyweather.util.navigate
import com.desuzed.everyweather.util.onBackClick
import org.koin.androidx.viewmodel.ext.android.viewModel


class LocationFragment : Fragment() {
    private val viewModel by viewModel<LocationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.state.collectAsState()
                LocationMainContent(
                    state = state,
                    onUserInteraction = viewModel::onUserInteraction,
                    onGeoTextChanged = viewModel::onNewGeoText,
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOnBackPressedCallback()
        collect(viewModel.action, ::onNewAction)
    }

    private fun onNewAction(action: LocationMainAction) {
        when (action) {
            is LocationMainAction.ShowSnackbar -> showSnackbar(action.message)
            is LocationMainAction.NavigateToWeather -> navigateToWeatherFragment(bundleOf(action.key to action.query))
            LocationMainAction.MyLocation -> onMyLocationClick()
            LocationMainAction.ShowMapFragment -> showMapBotSheet()
            LocationMainAction.NavigateToSettings -> navigateToSettingsFragment()
            LocationMainAction.NavigateBack -> onBackClick()
        }
    }

    private fun onMyLocationClick() {
        (activity as MainActivity).locationHandler.findUserLocation()
        val bundle = bundleOf(WeatherMainFragment.USER_LOCATION to true)
        navigateToWeatherFragment(bundle)
    }

    private fun showMapBotSheet() {
        navigate(R.id.action_locationFragment_to_mapBottomSheetFragment)
    }

    private fun navigateToWeatherFragment(bundle: Bundle) {
        navigate(R.id.action_locationFragment_to_weatherFragment, bundle)
    }

    private fun navigateToSettingsFragment() {
        navigate(R.id.action_locationFragment_to_settingsFragment)
    }

    private fun showSnackbar(message: String) {
        if (message.isEmpty()) return
        (activity as MainActivity).showSnackbar(message = message)
    }
}