package com.desuzed.everyweather.presentation.features.weather_main

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.desuzed.everyweather.data.repository.providers.action_result.WeatherActionResultProvider
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.presentation.base.BaseComposeScreen
import com.desuzed.everyweather.presentation.base.navigate
import com.desuzed.everyweather.presentation.features.location_main.LocationMainScreen
import com.desuzed.everyweather.presentation.features.weather_main.ui.WeatherMain
import com.desuzed.everyweather.ui.elements.AppSnackbar
import com.desuzed.everyweather.ui.elements.CollectSnackbar
import com.desuzed.everyweather.ui.navigation.Destination
import org.koin.androidx.compose.koinViewModel

object WeatherMainScreen : BaseComposeScreen<
        WeatherState,
        WeatherMainEffect,
        WeatherAction,
        WeatherMainViewModel>(
    initialState = WeatherState()
) {

    override val route: String
        get() = Destination.WeatherMainScreen.route

    @Composable
    override fun ProvideScreenForNavGraph(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry,
    ) {
        WeatherMainComposeScreen(
            navController = navController,
            navBackStackEntry = navBackStackEntry
        )
    }

    @Composable
    private fun WeatherMainComposeScreen(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry,
        viewModel: WeatherMainViewModel = koinViewModel()
    ) {
        //TODO подумать куда можно ещё вынести
        var snackData: QueryResult? by remember { mutableStateOf(null) }
        val snackbarHostState = remember { SnackbarHostState() }

        CollectSnackbar(
            queryResult = snackData,
            snackbarState = snackbarHostState,
            providerClass = WeatherActionResultProvider::class,
            onRetryClick = {
                viewModel.onAction(WeatherAction.Refresh)
            },
        )
        ComposeScreen(
            viewModel = viewModel,
            onEffect = {
                when (it) {
                    WeatherMainEffect.NavigateToLocation -> navController.navigate(
                        screen = LocationMainScreen,
                    )

                    WeatherMainEffect.NavigateToNextDaysWeather -> {

                    }

                    is WeatherMainEffect.ShowSnackbar -> {
                        snackData = it.queryResult
                    }
                }
            },
            content = { state ->
                WeatherMain(
                    state = state,
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