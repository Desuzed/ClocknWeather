package com.desuzed.everyweather.presentation.features.location_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.repository.providers.action_result.GeoActionResultProvider
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.result.ActionType
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.presentation.features.main_activity.MainActivity
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainFragment
import com.desuzed.everyweather.util.addOnBackPressedCallback
import com.desuzed.everyweather.util.navigateBackWithParameter
import com.desuzed.everyweather.util.onBackClick
import com.desuzed.everyweather.util.setArgumentObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationFragment : Fragment() {
    private val viewModel by viewModel<LocationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                // val state by viewModel.state.collectAsStateWithLifecycle(LocationMainState())
//                LocationMainScreen(
//                    state = state,
//                    onUserInteraction = viewModel::onUserInteraction,
//                    onGeoTextChanged = viewModel::onNewGeoText,
//                    onNewEditLocationText = viewModel::onNewEditLocationText,
//                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOnBackPressedCallback()
        //collect(viewModel.action, ::onNewAction)
        setArgumentObserver<UserLatLng>(MAP_LOCATION_ARGS) {
            //todo: back with result https://medium.com/@desilio/navigate-back-with-result-with-jetpack-compose-e91e6a6847c9
            viewModel.onUserInteraction(LocationUserInteraction.NavigateToWeather(it))
        }
    }

//    private fun onNewAction(action: LocationMainAction) {
//        when (action) {
//            is LocationMainAction.ShowSnackbar -> showSnackbar(action.queryResult)
//            is LocationMainAction.NavigateToWeather -> navigateToWeatherFragment(action.query)
//            LocationMainAction.MyLocation -> onMyLocationClick()
//            LocationMainAction.ShowMapFragment -> showMapBotSheet()
//            LocationMainAction.NavigateToSettings -> navigateToSettingsFragment()
//            LocationMainAction.NavigateBack -> onBackClick()
//            LocationMainAction.RequestLocationPermissions -> requestLocationPermissions()
//            is LocationMainAction.NavigateToWeatherWithLatLng -> toWeatherFromMap(action.latLng)
//        }
//    }

    private fun onMyLocationClick() {
        if (viewModel.areLocationPermissionsGranted()) {
            (activity as MainActivity).findUserLocation()
            onBackClick()
        } else {
            viewModel.launchRequireLocationPermissionsDialog()
        }
    }

    private fun requestLocationPermissions() {
        (activity as MainActivity).requestLocationPermissions()
    }

    private fun navigateToWeatherFragment(value: String) {
        navigateBackWithParameter(WeatherMainFragment.QUERY_KEY, value)
    }

    private fun toWeatherFromMap(value: UserLatLng) {
        navigateBackWithParameter(WeatherMainFragment.LAT_LNG_KEY, value)
    }

    private fun showSnackbar(queryResult: QueryResult) {
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

    companion object {
        const val MAP_LOCATION_ARGS = "MAP_LOCATION_ARGS"
    }

}