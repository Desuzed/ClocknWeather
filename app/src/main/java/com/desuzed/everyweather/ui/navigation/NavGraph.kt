package com.desuzed.everyweather.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.desuzed.everyweather.presentation.features.location_main.ui.LocationMainScreen
import com.desuzed.everyweather.presentation.features.settings.ui.SettingsScreen
import com.desuzed.everyweather.presentation.features.weather_main.ui.WeatherMainScreen

fun NavGraphBuilder.appNavGraph(
    navController: NavHostController,
) = apply {

//    composable(Destination.SplashScreen.route) {
//        SplashScreen(
//            navController = navController,
//        )
//    }

    composable(Destination.WeatherMainScreen.route) { navEntry ->
        WeatherMainScreen(
            navController = navController,
            navBackStackEntry = navEntry,
        )
    }

    composable(Destination.LocationScreen.route) {
        LocationMainScreen(
            navController = navController,
        )
    }

    composable(Destination.SettingsScreen.route) {
        SettingsScreen(
            navController = navController,
        )
    }

}

