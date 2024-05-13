package com.desuzed.everyweather.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <S> Flow<S>.collectAsStateWithLifecycle(
    initialState: S,
): State<S> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val stateLifecycleAware = remember(this, lifecycleOwner) {
        flowWithLifecycle(lifecycleOwner.lifecycle)
    }
    return stateLifecycleAware.collectAsState(initial = initialState)
}