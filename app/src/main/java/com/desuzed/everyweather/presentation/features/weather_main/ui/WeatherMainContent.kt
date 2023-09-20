package com.desuzed.everyweather.presentation.features.weather_main.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.mapper.UiMapper
import com.desuzed.everyweather.presentation.features.weather_main.WeatherState
import com.desuzed.everyweather.presentation.features.weather_main.WeatherUserInteraction
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.*
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.MockWeatherObject
import com.desuzed.everyweather.util.toIntDp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AppPreview
@Composable
private fun PreviewWeatherMainContent() {
    WeatherMainContent(
        state = WeatherState(
            weatherData = MockWeatherObject.weather,
        ),
        onUserInteraction = {},
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WeatherMainContent(
    state: WeatherState,
    onUserInteraction: (WeatherUserInteraction) -> Unit,
) {
    EveryweatherTheme {
        val refreshingState = rememberSwipeRefreshState(isRefreshing = state.isLoading)
        val fabSize = dimensionResource(id = R.dimen.dimen_50)
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val mappedWeatherUi = remember { mutableStateOf<WeatherMainUi?>(null) }
        coroutineScope.launch {
            mappedWeatherUi.value = withContext(Dispatchers.Default) {
                state.weatherData?.let {
                    UiMapper(
                        context = context,
                        language = state.lang,
                        windSpeed = state.windSpeed,
                        temperature = state.temperature,
                        pressure = state.pressure,
                    ).mapToMainWeatherUi(it)
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            var fabPadding by remember { mutableStateOf(0.dp) }
            SwipeRefresh(
                state = refreshingState,
                onRefresh = { onUserInteraction(WeatherUserInteraction.Refresh) },
            ) {
                val weatherUi = mappedWeatherUi.value
                if (weatherUi != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        WeatherHeaderInfo(
                            weatherUi.mainInfo,
                            onUserInteraction
                        ) { headerHeight ->
                            fabPadding = headerHeight.toIntDp.dp - fabSize / 2
                        }
                        BottomDetailWeather(weatherUi, onUserInteraction)
                    }
                    if (state.isAddButtonEnabled) {
                        FloatingButton(
                            modifier = Modifier
                                .padding(
                                    top = fabPadding,
                                    start = dimensionResource(id = R.dimen.dimen_20)
                                )
                                .size(size = fabSize),
                            id = R.drawable.ic_round_add_location_24,
                            onClick = { onUserInteraction(WeatherUserInteraction.SaveLocation) }
                        )
                    }
                } else {
                    if (!state.isLoading) {
                        EmptyWeatherCard(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .verticalScroll(rememberScrollState()),
                            onUserInteraction = onUserInteraction,
                        )
                    }
                }
            }
        }
    }
}