package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.desuzed.everyweather.presentation.features.locatation_map.MapBottomSheetScreen
import com.desuzed.everyweather.presentation.features.location_main.LocationMainAction
import com.desuzed.everyweather.presentation.features.location_main.LocationMainState
import com.desuzed.everyweather.presentation.features.location_main.LocationViewModel
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
            LocationMainAction.ShowMapFragment -> {
                coroutineScope.launch {
                    sheetState.show()
                }
            } // TODO()
            is LocationMainAction.ShowSnackbar -> {} //TODO()
        }
    }
    LocationMainBody(
        locations = state.locations,
        isLoading = state.isLoading,
        editLocationText = state.editLocationText,
        geoText = state.geoText,
        geoData = state.geoData,
        showPickerDialog = state.showPickerDialog,
        showEditLocationDialog = state.showEditLocationDialog,
        showRequireLocationPermissionsDialog = state.showRequireLocationPermissionsDialog,
        onUserInteraction = viewModel::onUserInteraction,
        onGeoTextChanged = viewModel::onNewGeoText,
        onNewEditLocationText = viewModel::onNewEditLocationText,
    )
    MapBottomSheetScreen(
        sheetState = sheetState,
        onBackClick = {
            coroutineScope.launch {
                sheetState.hide()
            }
        }
    )
}



