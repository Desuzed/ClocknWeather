package com.desuzed.everyweather.presentation.features.weather_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.repository.providers.action_result.WeatherActionResultProvider
import com.desuzed.everyweather.domain.model.result.ActionType
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.presentation.features.main_activity.MainActivity
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
//                val state by viewModel.state.collectAsStateWithLifecycle(WeatherState())
//                WeatherMainScreen(
//                    state = state,
//                    onAction = viewModel::onAction,
//                )
            }
        }
    }

    //    private fun onNewAction(action: WeatherMainAction) {
//        when (action) {
//            is WeatherMainAction.ShowSnackbar -> showSnackbar(action.queryResult)
//            WeatherMainAction.NavigateToLocation -> navigate(R.id.action_weatherFragment_to_locationFragment)
//            WeatherMainAction.NavigateToNextDaysWeather -> navigate(R.id.action_weatherFragment_to_nextDaysBottomSheet)
//        }
//    }

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
                    viewModel.onAction(WeatherAction.Refresh)
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
        const val LAT_LNG_KEY = "LAT_LNG"
    }

}