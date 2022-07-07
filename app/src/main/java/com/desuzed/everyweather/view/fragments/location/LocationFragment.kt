package com.desuzed.everyweather.view.fragments.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.data.room.FavoriteLocationMapper
import com.desuzed.everyweather.view.AppViewModelFactory
import com.desuzed.everyweather.view.main_activity.MainActivity
import com.desuzed.everyweather.view.fragments.addOnBackPressedCallback
import com.desuzed.everyweather.view.fragments.collect
import com.desuzed.everyweather.view.fragments.location.main.LocationMainAction
import com.desuzed.everyweather.view.fragments.location.main.LocationMainContent
import com.desuzed.everyweather.view.fragments.location.main.LocationViewModel
import com.desuzed.everyweather.view.fragments.navigate
import com.desuzed.everyweather.view.fragments.toast
import com.desuzed.everyweather.view.fragments.weather.main.WeatherMainFragment


class LocationFragment : Fragment() {
    private val viewModel: LocationViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(LocationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.state.collectAsState()
                LocationMainContent(
                    state = state,
                    onMyLocationClick = ::onMyLocationClick,
                    onFavoriteLocationClick = ::onFavoriteLocationClick,
                    onFavoriteLocationLongClick = viewModel::deleteFavoriteLocation,
                    onFindMapLocationClick = ::showMapBotSheet,
                    onFindTypedQueryClick = viewModel::findTypedLocation,
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

    private fun onFavoriteLocationClick(favoriteLocationDto: FavoriteLocationDto) {
        val userLatLng = FavoriteLocationMapper().mapFromEntity(favoriteLocationDto)
        val bundle = bundleOf(WeatherMainFragment.QUERY_KEY to userLatLng.toString())
        navigateToWeatherFragment(bundle) //todo вынести в onNewAction
    }

    private fun onNewAction(action: LocationMainAction) {
        when (action) {
            is LocationMainAction.ShowToast -> toast(action.message)//todo избавиться от тостов
            is LocationMainAction.NavigateToWeather -> navigateToWeatherFragment(bundleOf(action.key to action.query))
        }
    }

    private fun onMyLocationClick() {
        (activity as MainActivity).locationHandler.findUserLocation()
        val bundle = bundleOf(WeatherMainFragment.USER_LOCATION to true)
        navigateToWeatherFragment(bundle)//todo вынести в onNewAction
    }

    private fun showMapBotSheet() {
        navigate(R.id.action_locationFragment_to_mapBottomSheetFragment)
    }

    private fun navigateToWeatherFragment(bundle: Bundle) {
        navigate(R.id.action_locationFragment_to_weatherFragment, bundle)
    }

}