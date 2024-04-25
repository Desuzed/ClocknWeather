package com.desuzed.everyweather.presentation.features.shared

import com.desuzed.everyweather.util.Constants.ZERO_LONG

data class SharedState(
    val totalBytes: Long = ZERO_LONG,
    val bytesDownloaded: Long = ZERO_LONG,
    val isUpdateLoading: Boolean = false,
)