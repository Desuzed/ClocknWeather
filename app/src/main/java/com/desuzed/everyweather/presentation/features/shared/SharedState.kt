package com.desuzed.everyweather.presentation.features.shared

data class SharedState(
    val totalBytes: Long,
    val bytesDownloaded: Long,
    val isUpdateLoading: Boolean,
)