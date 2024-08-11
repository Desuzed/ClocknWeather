package com.desuzed.everyweather.presentation.features.weather_main.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
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
import com.desuzed.everyweather.data.repository.providers.action_result.WeatherActionResultProvider
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.presentation.features.weather_main.WeatherAction
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainEffect
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainViewModel
import com.desuzed.everyweather.presentation.features.weather_main.WeatherState
import com.desuzed.everyweather.presentation.features.weather_next_days.ui.NextDaysWeatherBottomSheet
import com.desuzed.everyweather.presentation.ui.UiMapper
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import com.desuzed.everyweather.ui.elements.AppSnackbar
import com.desuzed.everyweather.ui.elements.CollectSnackbar
import com.desuzed.everyweather.ui.extensions.CollectSideEffect
import com.desuzed.everyweather.ui.extensions.collectAsStateWithLifecycle
import com.desuzed.everyweather.ui.navigation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

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
    //TODO рефакторинг снекбаров на новую архитектуру
    val snackbarHostState = remember { SnackbarHostState() }
    var snackData: QueryResult? by remember { mutableStateOf(null) }
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
    CollectSideEffect(source = viewModel.sideEffect) {
        when (it) {
            WeatherMainEffect.NavigateToLocation -> navController.navigate(
                Destination.LocationScreen.route,
            )

            WeatherMainEffect.NavigateToNextDaysWeather -> {

            } // todo
            is WeatherMainEffect.ShowSnackbar -> {
                snackData = it.queryResult
            }
        }
    }

    CollectSnackbar(
        queryResult = snackData,
        snackbarState = snackbarHostState,
        providerClass = WeatherActionResultProvider::class,
        onRetryClick = {
            viewModel.onAction(WeatherAction.Refresh)
        },
    )

    WeatherMainBody(
        weatherData = mappedWeatherUi,
        nextDaysUiList = nextDaysUiList,
        isLoading = state.isLoading,
        isAddButtonEnabled = state.isAddButtonEnabled,
        onAction = viewModel::onAction,
        onNextDayClick = {
            //todo переместить в эффект
            coroutineScope.launch {
                selectedDayItem = it
                sheetState.show()
            }
        },
        boxScopeContent = {
            AppSnackbar(
                snackbarState = snackbarHostState,
            )
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