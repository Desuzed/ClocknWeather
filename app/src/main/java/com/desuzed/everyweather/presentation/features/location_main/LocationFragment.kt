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
import com.desuzed.everyweather.data.repository.providers.action_result.GeoActionResultProvider
import com.desuzed.everyweather.domain.model.result.ActionType
import com.desuzed.everyweather.domain.model.result.QueryResult
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
            is LocationMainAction.ShowSnackbar -> showSnackbar(action.queryResult)
            is LocationMainAction.NavigateToWeather -> navigateToWeatherFragment(bundleOf(action.key to action.query))
            LocationMainAction.MyLocation -> onMyLocationClick()
            LocationMainAction.ShowMapFragment -> showMapBotSheet()
            LocationMainAction.NavigateToSettings -> navigateToSettingsFragment()
            LocationMainAction.NavigateBack -> onBackClick()
            LocationMainAction.RequestLocationPermissions -> requestLocationPermissions()
        }
    }

    private fun onMyLocationClick() {
        if (viewModel.areLocationPermissionsGranted()) {
            (activity as MainActivity).findUserLocation()
            val bundle = bundleOf(WeatherMainFragment.USER_LOCATION to true)
            navigateToWeatherFragment(bundle)
        } else {
            viewModel.launchRequireLocationPermissionsDialog()
        }
    }

    private fun requestLocationPermissions() {
        (activity as MainActivity).requestLocationPermissions()
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

    private fun showSnackbar(queryResult: QueryResult) {
        if (queryResult.code == GeoActionResultProvider.RATE_LIMIT) {
            navigateToWeatherFragment(bundleOf(WeatherMainFragment.QUERY_KEY to queryResult.query))
            return
        }
        val provider = GeoActionResultProvider(resources)
        val message = provider.parseCode(queryResult.code, queryResult.query)
        val onClick: () -> Unit
        val buttonTextId: Int
        when (queryResult.actionType) {
            ActionType.OK -> {
                onClick = {}
                buttonTextId = R.string.ok
            }
            ActionType.RETRY -> {
                buttonTextId = R.string.retry
                onClick = {
                    viewModel.onUserInteraction(LocationUserInteraction.FindByQuery)
                }
            }
        }
        (activity as MainActivity).showSnackbar(
            message = message,
            actionStringId = buttonTextId,
            onActionClick = onClick
        )
    }

}