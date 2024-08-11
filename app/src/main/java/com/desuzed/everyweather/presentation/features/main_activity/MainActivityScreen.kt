package com.desuzed.everyweather.presentation.features.main_activity

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainScreen
import com.desuzed.everyweather.ui.navigation.appNavGraph

@Composable
fun MainActivityScreen() {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        NavHost(
            navController = navController,
            startDestination = WeatherMainScreen.destination.route,
        ) {
            appNavGraph(navController)
        }
    }
}