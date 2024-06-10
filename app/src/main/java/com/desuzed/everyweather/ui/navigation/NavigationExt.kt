package com.desuzed.everyweather.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.desuzed.everyweather.presentation.features.main_activity.MainActivity

@Composable
@ReadOnlyComposable
fun getMainActivity(): MainActivity {
    return LocalContext.current as MainActivity
}

fun <T> NavController.setResult(key: String, value: T) {
    currentBackStackEntry
        ?.savedStateHandle
        ?.set(key, value)
}

fun <T> NavBackStackEntry.getResult(key: String): T? {
    return savedStateHandle.remove(key)
}