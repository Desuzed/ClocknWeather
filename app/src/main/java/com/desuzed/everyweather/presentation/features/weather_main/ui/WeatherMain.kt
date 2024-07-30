package com.desuzed.everyweather.presentation.features.weather_main.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.desuzed.everyweather.presentation.features.weather_main.WeatherAction
import com.desuzed.everyweather.presentation.features.weather_main.WeatherState
import com.desuzed.everyweather.presentation.features.weather_next_days.ui.NextDaysWeatherBottomSheet
import com.desuzed.everyweather.presentation.ui.UiMapper
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val QUERY_KEY = "QUERY"
const val LAT_LNG_KEY = "LAT_LNG"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherMain(
    // navBackStackEntry: NavBackStackEntry,
    state: WeatherState,
    onAction: (WeatherAction) -> Unit,
    snackbarContent: @Composable BoxScope. () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val mappedWeatherUi = remember { mutableStateOf<WeatherMainUi?>(null) }
//    //todo сделать передачу с locationScreen, а лучше избавиться от этого костыля
//    navBackStackEntry.getResult<String>(QUERY_KEY)?.let {
//        if (it.isNotBlank()) {
//            viewModel.getForecast(it)
//        }
//    }
//    navBackStackEntry.getResult<UserLatLng>(LAT_LNG_KEY)?.let {
//        viewModel.getForecast(it.toString(), it)
//    }
    var selectedDayItem by remember {
        mutableStateOf<NextDaysUi?>(null)
    }

    val nextDaysUiList = remember { mutableStateOf<List<NextDaysUi>?>(null) }
    LaunchedEffect(key1 = state.weatherData) {
        coroutineScope.launch {
            nextDaysUiList.value = state.weatherData?.let {
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

//    //TODO избавиться от этого костыля
//    CollectSideEffect(getMainActivity().getUserLatLngFlow()) { location ->
//        if (location != null) {
//            viewModel.getForecast(location.toString())
//        }
//    }

    WeatherMainBody(
        weatherData = mappedWeatherUi,
        nextDaysUiList = nextDaysUiList,
        isLoading = state.isLoading,
        isAddButtonEnabled = state.isAddButtonEnabled,
        onAction = onAction,
        onNextDayClick = {
            //todo переместить в эффект
            coroutineScope.launch {
                selectedDayItem = it
                sheetState.show()
            }
        },
        boxScopeContent = snackbarContent,
    )
    NextDaysWeatherBottomSheet(
        sheetState = sheetState,
        dayItem = selectedDayItem,
        onBackClick = {
            coroutineScope.launch {
                sheetState.hide()
                selectedDayItem = null
            }
        }
    )
}