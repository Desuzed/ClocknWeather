@file:OptIn(ExperimentalMaterialApi::class)

package com.desuzed.everyweather.presentation.features.weather_main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.presentation.features.weather_main.WeatherAction
import com.desuzed.everyweather.presentation.features.weather_next_days.ui.NextDaysUiListContent
import com.desuzed.everyweather.presentation.ui.UiMapper
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.CardDetailDayItem
import com.desuzed.everyweather.ui.elements.GradientBox
import com.desuzed.everyweather.ui.elements.HourItemContent
import com.desuzed.everyweather.ui.elements.LinkText
import com.desuzed.everyweather.ui.elements.MediumText
import com.desuzed.everyweather.ui.elements.RoundedCardItem
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.MockWeatherObject
import kotlinx.coroutines.launch

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val mappedWeatherUi = remember { mutableStateOf<WeatherMainUi?>(null) }
        LaunchedEffect(key1 = Unit) {
            scope.launch {
                mappedWeatherUi.value = UiMapper(
                    context = context,
                    selectedLanguage = Lang.RU,
                    selectedDistanceDimen = DistanceDimen.METRIC_KMH,
                    selectedTemperature = TempDimen.CELCIUS,
                    selectedPressure = PressureDimen.MILLIMETERS,
                ).mapToMainWeatherUi(MockWeatherObject.weather)
            }
        }
        mappedWeatherUi.value?.let {
            BottomDetailWeather(
                weatherUi = it,
                onAction = {},
                onNextDayClick = {},
                nextDaysUiList = remember { mutableStateOf(null) },
                refreshingState = rememberPullRefreshState(
                    refreshing = false,
                    onRefresh = {},
                )
            )
        }
    }
}

private const val WEATHER_LINK_START_LETTER = ":"
private const val WEATHER_LINK_START_INDEX = 2
private const val WEATHER_LINK_LENGTH = 11

@Composable
fun BottomDetailWeather(
    weatherUi: WeatherMainUi,
    refreshingState: PullRefreshState,
    nextDaysUiList: State<List<NextDaysUi>?>,
    onNextDayClick: (NextDaysUi) -> Unit,
    onAction: (WeatherAction) -> Unit,
) {
    GradientBox(
        modifier = Modifier.fillMaxSize(),
        colors = EveryweatherTheme.colors.primaryBackground,
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.dimen_20),
                    start = dimensionResource(id = R.dimen.dimen_20),
                    end = dimensionResource(id = R.dimen.dimen_20),
                )
                .pullRefresh(refreshingState)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dimen_10)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //todo придумать анимацию
            if (weatherUi.hourList.isNotEmpty()) {
                LazyRow {
                    items(items = weatherUi.hourList) { hourItem ->
                        HourItemContent(hourItem = hourItem)
                    }
                }
            } else {
                RoundedCardItem(modifier = Modifier.padding(top = 8.dp)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        MediumText(
                            text = stringResource(id = R.string.could_not_load_hourly_weather),
                            color = EveryweatherTheme.colors.onBackgroundPrimary,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        MediumText(
                            text = stringResource(id = R.string.retry_again),
                            color = EveryweatherTheme.colors.onBackgroundPrimary,
                        )
                    }
                }
            }
            CardDetailDayItem(detailCard = weatherUi.detailCard)
            BoldText(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.dimen_10),
                        start = dimensionResource(id = R.dimen.dimen_10)
                    )
                    .align(Alignment.Start),
                text = stringResource(id = R.string.next_days_forecast),
            )
            NextDaysUiListContent(
                nextDaysWeatherData = nextDaysUiList.value,
                onNextDayClick = onNextDayClick,
            )
            //TODO: Вынести отсюда
            val inputText = stringResource(id = R.string.powered_by)
            val startIndex by remember {
                mutableStateOf(
                    inputText.indexOf(WEATHER_LINK_START_LETTER) + WEATHER_LINK_START_INDEX
                )
            }
            LinkText(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = dimensionResource(id = R.dimen.dimen_20)),
                inputText = inputText,
                url = stringResource(id = R.string.uri),
                startIndex = startIndex,
                endIndex = startIndex + WEATHER_LINK_LENGTH,
                onClick = { onAction(WeatherAction.Redirection) },
            )
        }
    }

}