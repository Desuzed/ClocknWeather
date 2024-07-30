package com.desuzed.everyweather.presentation.features.location_main.ui.map

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.desuzed.everyweather.presentation.features.location_main.LocationAction
import com.desuzed.everyweather.presentation.features.location_main.LocationMainState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapBottomSheetScreen(
    state: LocationMainState,
    sheetState: SheetState,
    onAction: (LocationAction) -> Unit,
) {
    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),//todo
            dragHandle = null,
//            containerColor = getBackgroundColor(),
//            contentColor = getOnBackgroundColor(),
            onDismissRequest = { onAction(LocationAction.ToggleMap(false)) },
            content = {
                MapLocationContent(
                    location = state.mapPinLocation,
                    newPickedLocation = state.newPickedLocation,
                    loadNewLocationWeather = state.loadNewLocationWeather,
                    onAction = onAction,
                )
            }
        )
    }

}