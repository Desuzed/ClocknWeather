package com.desuzed.everyweather.view.fragments.weather.main

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.desuzed.everyweather.MockWeatherObject
import com.desuzed.everyweather.R
import com.desuzed.everyweather.ui.elements.*
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.view.ui.main.MainWeatherMapper
import com.desuzed.everyweather.view.ui.main.WeatherMainInfoUi
import com.desuzed.everyweather.view.ui.main.WeatherMainUi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Preview(
    showBackground = true,
    widthDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "PreviewWeatherMainContent"
)
@Composable
private fun PreviewWeatherMainContent() {
    WeatherMainContent(
        state = WeatherState(
            weatherUi = MainWeatherMapper(resources = LocalContext.current.resources).mapToMainWeatherUi(
                MockWeatherObject.weather
            )
        ),
        onNextDaysButtonCLick = {},
        onSaveLocationClick = {},
        onLocationClick = {},
        onRefresh = {},
    )
}


@Composable
fun WeatherMainContent(
    state: WeatherState,
    onNextDaysButtonCLick: () -> Unit,
    onSaveLocationClick: () -> Unit,
    onLocationClick: () -> Unit,
    onRefresh: () -> Unit,
) {
    EveryweatherTheme {
        val refreshingState = rememberSwipeRefreshState(isRefreshing = state.isLoading)
        Box(modifier = Modifier.fillMaxSize()) {
            var headerSize by remember { mutableStateOf(0) }
            var fabHeight by remember { mutableStateOf(0) }
            SwipeRefresh(state = refreshingState, onRefresh = onRefresh) {
                if (state.weatherUi != null) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        WeatherHeaderInfo(state.weatherUi.mainInfo, onLocationClick)
                        BottomDetailWeather(state.weatherUi, onNextDaysButtonCLick)
                    }
                    if (state.isAddButtonEnabled) {
                        FloatingButton(
                            modifier = Modifier
                                .padding(top = 210.dp, start = 20.dp)
                                .size(50.dp),
                            id = R.drawable.ic_round_add_location_24,
                            onClick = onSaveLocationClick
                        )
                        //.padding(top = (headerSize - fabHeight / 2).dp, start = 20.dp),//todo подумать как расположить кнопку посередине
                    }
                } else {
                    if (!refreshingState.isRefreshing) {
                        EmptyWeatherCard(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .verticalScroll(rememberScrollState()),
                            onLocationClick = onLocationClick,
                        )    //todo Выровнять по центру
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherHeaderInfo(
    mainInfoUi: WeatherMainInfoUi, onLocationClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        EveryweatherTheme.colors.gradientStart,
                        EveryweatherTheme.colors.gradientEnd,
                    )
                )
            )
//            .onGloballyPositioned {
//                onNewHeight(it.size.height)
//            }
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LocationText(text = mainInfoUi.geoText, onLocationClick)
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .height(60.dp)      //todo разобраться с размерами
                        .width(60.dp),
                    alignment = Alignment.CenterEnd,
                    painter = rememberAsyncImagePainter(mainInfoUi.iconUrl),
                    contentDescription = "",
                )
                LargeBoldText(
                    text = mainInfoUi.currentTemp,
                    color = EveryweatherTheme.colors.textColorSecondary
                )
            }
            MediumText(
                text = mainInfoUi.feelsLike,
                color = EveryweatherTheme.colors.textColorSecondary,
            )
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MediumText(
                    text = mainInfoUi.date,
                    color = EveryweatherTheme.colors.textColorSecondary,
                )
                MediumText(
                    text = "|",
                    color = EveryweatherTheme.colors.textColorSecondary,
                )
//                Box(
//                    modifier = Modifier
//                        .width(1.dp)//todo разобраться почему не применяется фон
//                        .background(EveryweatherTheme.colors.textColorSecondary)
//                )
                MediumBoldText(
//todo понять почему не применился жирный шрифт
                    text = mainInfoUi.time,
                    color = EveryweatherTheme.colors.textColorSecondary,
                )
            }
        }
    }
}

@Composable
fun BottomDetailWeather(weatherUi: WeatherMainUi, onNextDaysButtonCLick: () -> Unit) {
    Column(
        modifier = Modifier.padding(20.dp),
        //    horizontalAlignment = Alignment.Center,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow {
            items(items = weatherUi.hourList) { hourItem ->
                AnimatedVisibility( //todo сделать анимацию
                    visible = true,
                    exit = fadeOut(
                        animationSpec = TweenSpec(200, 200, FastOutLinearInEasing)
                    )
                ) {
                    HourItemContent(hourItem = hourItem)
                }
            }
        }
        CardDetailDayItem(detailCard = weatherUi.detailCard)
        RoundedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNextDaysButtonCLick,
            text = stringResource(id = R.string.next_days_forecast)
        )
        //  LinkText(Modifier)//TODO Не работает
    }

}

@Composable
fun EmptyWeatherCard(onLocationClick: () -> Unit, modifier: Modifier) {
    Card(
        modifier = modifier.padding(20.dp),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_16)),
        backgroundColor = EveryweatherTheme.colors.onSurface,
        elevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_20dp)),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BoldText(
                text = stringResource(id = R.string.no_weather_data_is_loaded_header),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            RegularText(text = stringResource(id = R.string.no_weather_data_is_loaded))
            RoundedButton(
                onClick = onLocationClick,
                text = stringResource(id = R.string.choose_location)
            )

        }
    }
}