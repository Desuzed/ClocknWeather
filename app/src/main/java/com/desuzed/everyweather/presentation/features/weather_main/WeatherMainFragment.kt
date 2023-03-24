package com.desuzed.everyweather.presentation.features.weather_main

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
import com.desuzed.everyweather.data.repository.providers.action_result.WeatherActionResultProvider
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.result.ActionType
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.presentation.features.in_app_update.InAppUpdateBottomSheet.Companion.IS_UPDATE_READY_TAG
import com.desuzed.everyweather.presentation.features.main_activity.MainActivity
import com.desuzed.everyweather.presentation.features.shared.SharedAction
import com.desuzed.everyweather.presentation.features.shared.SharedViewModel
import com.desuzed.everyweather.util.collect
import com.desuzed.everyweather.util.collectWhenResumed
import com.desuzed.everyweather.util.navigate
import com.desuzed.everyweather.util.setArgumentObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherMainFragment : Fragment() {
    private val viewModel by viewModel<WeatherMainViewModel>()
    private val sharedViewModel by viewModel<SharedViewModel>()

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
        collectWhenResumed(sharedViewModel.action, ::onNewSharedAction)
    }

    private fun resolveArguments() {
        setArgumentObserver(QUERY_KEY) {
            if (it.isNotBlank()) {
                getQueryForecast(it)
            }
        }
        setArgumentObserver(IN_APP_UPDATE) {
            when (it) {
                START_UPDATE -> {
                    sharedViewModel.startUpdate(requireActivity())
                }
                INSTALL_UPDATE -> {
                    sharedViewModel.completeUpdate()
                }
            }
        }
    }

    private fun getQueryForecast(query: String) {
        viewModel.getForecast(query)
    }

    private fun onNewAction(action: WeatherMainAction) {
        when (action) {
            is WeatherMainAction.ShowSnackbar -> showSnackbar(action.queryResult)
            WeatherMainAction.NavigateToLocation -> navigate(R.id.action_weatherFragment_to_locationFragment)
            WeatherMainAction.NavigateToNextDaysWeather -> navigate(R.id.action_weatherFragment_to_nextDaysBottomSheet)
        }
    }

    private fun onNewSharedAction(action: SharedAction) {
        when (action) {
            SharedAction.UpdateAvailableDialog -> navigate(
                directions = R.id.action_weatherFragment_to_updateAppFragment,
                bundle = bundleOf(IS_UPDATE_READY_TAG to false),
            )
            SharedAction.UpdateReadyToInstallDialog -> navigate(
                directions = R.id.action_weatherFragment_to_updateAppFragment,
                bundle = bundleOf(IS_UPDATE_READY_TAG to true),
            )
        }
    }

    private fun onNewLocation(location: UserLatLng?) {
        if (location != null) {
            getQueryForecast(location.toString())
        }
    }

    private fun showSnackbar(queryResult: QueryResult) {
        val provider = WeatherActionResultProvider(resources)
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
                    viewModel.onUserInteraction(WeatherUserInteraction.Refresh)
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
        const val QUERY_KEY = "QUERY"
        const val IN_APP_UPDATE = "IN_APP_UPDATE"
        const val START_UPDATE = "START_UPDATE"
        const val INSTALL_UPDATE = "INSTALL_UPDATE"
    }

}