package com.desuzed.everyweather.presentation.features.weather_main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.features.weather_main.WeatherState
import com.desuzed.everyweather.presentation.features.weather_main.WeatherUserInteraction
import com.desuzed.everyweather.presentation.ui.UiMapper
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.FloatingButton
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.MockWeatherObject
import com.desuzed.everyweather.util.toIntDp
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherMainContent(
    state: WeatherState,
    onUserInteraction: (WeatherUserInteraction) -> Unit,
) {
    EveryweatherTheme {
        val refreshingState = rememberPullRefreshState(
            refreshing = state.isLoading,
            onRefresh = { onUserInteraction(WeatherUserInteraction.Refresh) },
        )
        val fabSize = dimensionResource(id = R.dimen.dimen_50)
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val mappedWeatherUi = remember { mutableStateOf<WeatherMainUi?>(null) }
        LaunchedEffect(key1 = state.weatherData) {
            coroutineScope.launch {
                mappedWeatherUi.value = withContext(Dispatchers.Default) {
                    state.weatherData?.let {
                        UiMapper(
                            context = context,
                            selectedLanguage = state.selectedLang,
                            selectedDistanceDimen = state.windSpeed,
                            selectedTemperature = state.temperature,
                            selectedPressure = state.pressure,
                        ).mapToMainWeatherUi(it)
                    }
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            var fabPadding by remember { mutableStateOf(0.dp) }
            val weatherUi = mappedWeatherUi.value
            if (weatherUi != null) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    WeatherHeaderInfo(
                        mainInfoUi = weatherUi.mainInfo,
                        onUserInteraction = onUserInteraction,
                        refreshingState = refreshingState,
                    ) { headerHeight ->
                        fabPadding = headerHeight.toIntDp.dp - fabSize / 2
                    }
                    BottomDetailWeather(weatherUi, refreshingState, onUserInteraction)
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
                            .pullRefresh(refreshingState)
                            .verticalScroll(rememberScrollState()),
                        onUserInteraction = onUserInteraction,
                    )
                }
            }
            PullRefreshIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = dimensionResource(id = R.dimen.dimen_20)),
                refreshing = state.isLoading,
                state = refreshingState,
                backgroundColor = EveryweatherTheme.colors.secondaryGradientStart,
                contentColor = EveryweatherTheme.colors.secondary,
            )
        }
    }
}