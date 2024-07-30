package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import com.desuzed.everyweather.presentation.features.location_main.LocationAction
import com.desuzed.everyweather.presentation.features.location_main.LocationMainState
import com.desuzed.everyweather.presentation.features.location_main.ui.map.MapBottomSheetScreen

//@AppPreview
//@Composable
//private fun Preview() {
//    EveryweatherTheme {
//        LocationMain(
//            navController = NavController(LocalContext.current)
//        )
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationMain(
    state: LocationMainState,
    sheetState: SheetState,
    onAction: (LocationAction) -> Unit,
    snackbarContent: @Composable BoxScope. () -> Unit,
) {
    LocationMainBody(
        locations = state.locations,
        isLoading = state.isLoading,
        geoText = state.geoText,
        onAction = onAction,
        boxScopeContent = snackbarContent
    )
    MapBottomSheetScreen(
        sheetState = sheetState,
        state = state,
        onAction = onAction,
    )
    LocationDialogContent(
        dialog = state.locationDialog,
        geoData = state.geoData,
        editLocationText = state.editLocationText,
        onAction = onAction,
    )
}



