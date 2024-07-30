package com.desuzed.everyweather.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.desuzed.everyweather.presentation.base.composeScreenDestination
import com.desuzed.everyweather.presentation.features.location_main.LocationMainScreen
import com.desuzed.everyweather.presentation.features.settings.ui.SettingsScreen
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainScreen

fun NavGraphBuilder.appNavGraph(
    navController: NavHostController,
) = apply {

//    composable(Destination.SplashScreen.route) {
//        SplashScreen(
//            navController = navController,
//        )
//    }

    composeScreenDestination(WeatherMainScreen, navController)

    composeScreenDestination(LocationMainScreen, navController)

    composable(Destination.SettingsScreen.route) {
        SettingsScreen(
            navController = navController,
        )
    }

}

