package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.desuzed.everyweather.data.repository.providers.action_result.WeatherActionResultProvider
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.presentation.features.location_main.LocationAction
import com.desuzed.everyweather.presentation.features.location_main.LocationMainEffect
import com.desuzed.everyweather.presentation.features.location_main.LocationMainState
import com.desuzed.everyweather.presentation.features.location_main.LocationViewModel
import com.desuzed.everyweather.presentation.features.location_main.ui.map.MapBottomSheetScreen
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.AppSnackbar
import com.desuzed.everyweather.ui.elements.CollectSnackbar
import com.desuzed.everyweather.ui.extensions.CollectSideEffect
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
    //TODO рефакторинг снекбаров на новую архитектуру
    var snackData: QueryResult? by remember { mutableStateOf(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    CollectSideEffect(source = viewModel.sideEffect) {
        when (it) {
            LocationMainEffect.MyLocation -> {} //TODO
            LocationMainEffect.NavigateBack -> navController.popBackStack() //TODO()
            LocationMainEffect.NavigateToSettings -> navController.navigate(
                route = Destination.SettingsScreen.route,
            )

            is LocationMainEffect.NavigateToWeather -> navController.popBackStack()// TODO()
            is LocationMainEffect.NavigateToWeatherWithLatLng -> navController.popBackStack() //TODO()
            LocationMainEffect.RequestLocationPermissions -> TODO()
            is LocationMainEffect.ToggleMap -> {
                coroutineScope.launch {
                    if (it.isVisible) {
                        sheetState.show()
                    } else {
                        sheetState.hide()
                    }
                }
            }

            is LocationMainEffect.ShowSnackbar -> {
                snackData = it.queryResult
            }
        }
    }
    CollectSnackbar(
        queryResult = snackData,
        snackbarState = snackbarHostState,
        providerClass = WeatherActionResultProvider::class,
        onRetryClick = {
            viewModel.onAction(LocationAction.FindByQuery)
        },
    )
    LocationMainBody(
        locations = state.locations,
        isLoading = state.isLoading,
        geoText = state.geoText,
        onAction = viewModel::onAction,
        boxScopeContent = {
            AppSnackbar(
                snackbarState = snackbarHostState,
            )
        }
    )
    MapBottomSheetScreen(
        sheetState = sheetState,
        state = state,
        onAction = viewModel::onAction,
    )
    LocationDialogContent(
        dialog = state.locationDialog,
        geoData = state.geoData,
        editLocationText = state.editLocationText,
        onAction = viewModel::onAction,
    )
}



