package com.desuzed.everyweather.presentation.features.location_main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.desuzed.everyweather.data.repository.providers.action_result.GeoActionResultProvider
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.presentation.base.BaseComposeScreen
import com.desuzed.everyweather.presentation.features.location_main.ui.LocationMain
import com.desuzed.everyweather.ui.elements.AppSnackbar
import com.desuzed.everyweather.ui.elements.CollectSnackbar
import com.desuzed.everyweather.ui.navigation.Destination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

object LocationMainScreen : BaseComposeScreen<
        LocationMainState,
        LocationMainEffect,
        LocationAction,
        LocationViewModel>(
    initialState = LocationMainState()
) {

    override val route: String
        get() = Destination.LocationScreen.route

    @Composable
    override fun ProvideScreenForNavGraph(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry,
    ) {
        LocationMainComposeScreen(
            navController = navController,
            navBackStackEntry = navBackStackEntry
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun LocationMainComposeScreen(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry,
        viewModel: LocationViewModel = koinViewModel()
    ) {
        //TODO Рефакторинг, постараться вынести отсюда
        var snackData: QueryResult? by remember { mutableStateOf(null) }
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )

        CollectSnackbar(
            queryResult = snackData,
            snackbarState = snackbarHostState,
            providerClass = GeoActionResultProvider::class,
            onRetryClick = {
                viewModel.onAction(LocationAction.FindByQuery)
            },
        )
        ComposeScreen(
            viewModel = viewModel,
            onEffect = {
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
            },
            content = { state ->
                LocationMain(
                    state = state,
                    sheetState = sheetState,
                    onAction = viewModel::onAction,
                    snackbarContent = {
                        AppSnackbar(
                            snackbarState = snackbarHostState,
                        )
                    },
                )
            },
        )
    }
}