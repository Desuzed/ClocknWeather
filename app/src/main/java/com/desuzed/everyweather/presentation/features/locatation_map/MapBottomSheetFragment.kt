package com.desuzed.everyweather.presentation.features.locatation_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainFragment
import com.desuzed.everyweather.util.collect
import com.desuzed.everyweather.util.navigate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapBottomSheetFragment : BottomSheetDialogFragment() {
    private val viewModel by viewModel<MapLocationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val state by viewModel.state.collectAsState()
            MapLocationContent(
                state = state,
                onUserInteraction = viewModel::onUserInteraction
            )
        }
        isNestedScrollingEnabled = true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initState()
        collect(viewModel.action, ::onNewAction)
    }

    private fun navigateToMainFragment(query: String) {
        val bundle = bundleOf(WeatherMainFragment.QUERY_KEY to query)
        navigate(R.id.action_mapBottomSheetFragment_to_weatherFragment, bundle)
    }

    private fun onNewAction(action: MapAction) {
        when (action) {
            is MapAction.NavigateToWeather -> navigateToMainFragment(action.query)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}