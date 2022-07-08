package com.desuzed.everyweather.view.fragments.location.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.view.AppViewModelFactory
import com.desuzed.everyweather.view.fragments.collect
import com.desuzed.everyweather.view.fragments.navigate
import com.desuzed.everyweather.view.fragments.weather.main.WeatherMainFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

class MapBottomSheetFragment : BottomSheetDialogFragment() {
    private var job: Job? = null

    private val viewModel: MapLocationViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(MapLocationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val state by viewModel.state.collectAsState()
            MapLocationContent(
                state = state,
                onNewLocation = viewModel::onNewLocationPicked,
                onNewLocationConfirm = viewModel::onNewLocationConfirm,
                onDismiss = viewModel::onDismiss,
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
        job?.cancel()
    }
}