package com.desuzed.everyweather.presentation.features.weather_next_days.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.features.weather_next_days.NextDaysState
import com.desuzed.everyweather.presentation.ui.UiMapper
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.MockWeatherObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AppPreview
@Composable
private fun PreviewNextDaysBottomSheetContent() {
    val state = NextDaysState(
        weather = MockWeatherObject.weather
    )
    NextDaysBottomSheetContent(state)
}

@Composable
fun NextDaysBottomSheetContent(
    state: NextDaysState,
) {
    EveryweatherTheme {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val mappedWeatherUi = remember { mutableStateOf<List<NextDaysUi>?>(null) }
        LaunchedEffect(key1 = state.weather) {
            coroutineScope.launch {
                mappedWeatherUi.value = withContext(Dispatchers.Default) {
                    state.weather?.let {
                        UiMapper(
                            context = context,
                            selectedDistanceDimen = state.windSpeed,
                            selectedTemperature = state.temperature,
                            selectedLanguage = state.selectedLang,
                            selectedPressure = state.pressure,
                        ).mapToNextDaysUi(it)
                    }
                }
            }
        }

        Surface(
            modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_350)),
            shape = RoundedCornerShape(
                topStart = dimensionResource(id = R.dimen.corner_radius_30),
                topEnd = dimensionResource(id = R.dimen.corner_radius_30)
            ),
            color = EveryweatherTheme.colors.tertiaryBackground
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = dimensionResource(id = R.dimen.dimen_20),
                        start = dimensionResource(id = R.dimen.dimen_10),
                        end = dimensionResource(id = R.dimen.dimen_10),
                    )
            ) {
                val nextDaysWeather = mappedWeatherUi.value
                if (nextDaysWeather != null) {
                    val indexOfLastItem = nextDaysWeather.lastIndex
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState())
                    ) {
                        nextDaysWeather.forEach { forecastItem ->
                            val isLastElement =
                                nextDaysWeather.indexOf(forecastItem) == indexOfLastItem
                            ForecastListItem(dayItem = forecastItem, isLastItem = isLastElement)
                        }
                    }
                }
            }
        }

    }
}
