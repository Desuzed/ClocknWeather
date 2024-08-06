package com.desuzed.everyweather.presentation.features.weather_main

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.desuzed.everyweather.data.repository.providers.action_result.WeatherActionResultProvider
import com.desuzed.everyweather.presentation.base.BaseComposeScreen
import com.desuzed.everyweather.presentation.base.navigate
import com.desuzed.everyweather.presentation.features.location_main.LocationMainScreen
import com.desuzed.everyweather.presentation.features.weather_main.ui.WeatherMain
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

        ComposeScreen(
            viewModel = viewModel,
            snackBarParams = SnackBarParams(
                snackBarProviderClass = WeatherActionResultProvider::class,
                snackBarRetryAction = WeatherAction.Refresh,
                snackBarEffectClass = WeatherMainEffect.ShowSnackBar::class
            ),
            onEffect = {
                when (it) {
                    WeatherMainEffect.NavigateToLocation -> navController.navigate(
                        screen = LocationMainScreen,
                    )

                    WeatherMainEffect.NavigateToNextDaysWeather -> {

                    }

                    is WeatherMainEffect.ShowSnackBar -> {
                        /** Эффект обрабатывается в обёртке ComposeScreen **/
                    }
                }
            },
            content = { state ->
                WeatherMain(
                    state = state,
                    onAction = viewModel::onAction,
                )
            },
        )
    }
}