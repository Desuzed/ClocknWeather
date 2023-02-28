package com.desuzed.everyweather.analytics

import android.content.Context
import com.desuzed.everyweather.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics

abstract class Analytics(context: Context) {
    private val analyticsInstance = FirebaseAnalytics.getInstance(context)

    init {
        analyticsInstance.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    fun logEvent(event: String) {
        analyticsInstance.logEvent(event, null)
    }
}