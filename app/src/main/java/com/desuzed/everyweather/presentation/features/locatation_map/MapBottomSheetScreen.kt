package com.desuzed.everyweather.presentation.features.locatation_map

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.desuzed.everyweather.ui.extensions.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapBottomSheetScreen(
    sheetState: SheetState,
    viewModel: MapLocationViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle(initialState = MapState())
    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),//todo
            dragHandle = null,
//            containerColor = getBackgroundColor(),
//            contentColor = getOnBackgroundColor(),
            onDismissRequest = onBackClick,
            content = {
                MapLocationContent(
                    location = state.location,
                    newPickedLocation = state.newPickedLocation,
                    loadNewLocationWeather = state.loadNewLocationWeather,
                    shouldShowDialog = state.shouldShowDialog,
                    onUserInteraction = viewModel::onUserInteraction,
                )
            }
        )
    }

}