package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.desuzed.everyweather.presentation.features.location_main.LocationMainAction
import com.desuzed.everyweather.presentation.features.location_main.LocationMainState
import com.desuzed.everyweather.presentation.features.location_main.LocationViewModel
import com.desuzed.everyweather.presentation.features.location_main.ui.map.MapBottomSheetScreen
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.extensions.CollectAction
import com.desuzed.everyweather.ui.extensions.collectAsStateWithLifecycle
import com.desuzed.everyweather.ui.navigation.Destination
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        LocationMainScreen(
            navController = NavController(LocalContext.current)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationMainScreen(
    navController: NavController,
    viewModel: LocationViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle(initialState = LocationMainState())
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val coroutineScope = rememberCoroutineScope()
    CollectAction(source = viewModel.action) {
        when (it) {
            LocationMainAction.MyLocation -> {} //TODO
            LocationMainAction.NavigateBack -> navController.popBackStack() //TODO()
            LocationMainAction.NavigateToSettings -> navController.navigate(
                route = Destination.SettingsScreen.route,
            )

            is LocationMainAction.NavigateToWeather -> navController.popBackStack()// TODO()
            is LocationMainAction.NavigateToWeatherWithLatLng -> navController.popBackStack() //TODO()
            LocationMainAction.RequestLocationPermissions -> TODO()
            is LocationMainAction.ToggleMap -> {
                coroutineScope.launch {
                    if (it.isVisible) {
                        sheetState.show()
                    } else {
                        sheetState.hide()
                    }
                }
            }

            is LocationMainAction.ShowSnackbar -> {} //TODO()
        }
    }
    LocationMainBody(
        locations = state.locations,
        isLoading = state.isLoading,
        geoText = state.geoText,
        onUserInteraction = viewModel::onUserInteraction,
    )
    MapBottomSheetScreen(
        sheetState = sheetState,
        state = state,
        onUserInteraction = viewModel::onUserInteraction,
    )
    LocationDialogContent(
        dialog = state.locationDialog,
        geoData = state.geoData,
        editLocationText = state.editLocationText,
        onUserInteraction = viewModel::onUserInteraction,
    )
}



