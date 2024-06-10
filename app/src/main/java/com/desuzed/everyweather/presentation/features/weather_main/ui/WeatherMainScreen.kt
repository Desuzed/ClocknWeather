package com.desuzed.everyweather.presentation.features.weather_main.ui

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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainAction
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainViewModel
import com.desuzed.everyweather.presentation.features.weather_main.WeatherState
import com.desuzed.everyweather.presentation.features.weather_next_days.ui.NextDaysWeatherBottomSheet
import com.desuzed.everyweather.presentation.ui.UiMapper
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import com.desuzed.everyweather.ui.extensions.CollectAction
import com.desuzed.everyweather.ui.extensions.collectAsStateWithLifecycle
import com.desuzed.everyweather.ui.navigation.Destination
import com.desuzed.everyweather.ui.navigation.getMainActivity
import com.desuzed.everyweather.ui.navigation.getResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

const val QUERY_KEY = "QUERY"
const val LAT_LNG_KEY = "LAT_LNG"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherMainScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    viewModel: WeatherMainViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle(initialState = WeatherState())
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val mappedWeatherUi = remember { mutableStateOf<WeatherMainUi?>(null) }
    //todo сделать передачу с locationScreen, а лучше избавиться от этого костыля
    navBackStackEntry.getResult<String>(QUERY_KEY)?.let {
        if (it.isNotBlank()) {
            viewModel.getForecast(it)
        }
    }
    navBackStackEntry.getResult<UserLatLng>(LAT_LNG_KEY)?.let {
        viewModel.getForecast(it.toString(), it)
    }
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
    CollectAction(source = viewModel.action) {
        when (it) {
            WeatherMainAction.NavigateToLocation -> navController.navigate(
                Destination.LocationScreen.route,
            )

            WeatherMainAction.NavigateToNextDaysWeather -> {

            } // todo
            is WeatherMainAction.ShowSnackbar -> {} // todo
        }
    }
    //TODO избавиться от этого костыля
    CollectAction(getMainActivity().getUserLatLngFlow()) { location ->
        if (location != null) {
            viewModel.getForecast(location.toString())
        }
    }
    WeatherMainBody(
        weatherData = mappedWeatherUi,
        nextDaysUiList = nextDaysUiList,
        isLoading = state.isLoading,
        isAddButtonEnabled = state.isAddButtonEnabled,
        onUserInteraction = viewModel::onUserInteraction,
        onNextDayClick = {
            //todo переместить в эффект
            coroutineScope.launch {
                selectedDayItem = it
                sheetState.show()
            }
        },
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