package com.desuzed.everyweather.presentation.features.in_app_update

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.desuzed.everyweather.presentation.features.in_app_update.ui.InAppUpdateContent
import com.desuzed.everyweather.presentation.features.shared.SharedViewModel
import com.desuzed.everyweather.ui.extensions.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InAppUpdateBottomSheetScreen(
    viewModel: InAppUpdateViewModel = koinViewModel(),
    sharedViewModel: SharedViewModel = koinViewModel(),
    sheetState: SheetState,
    onBackClick: () -> Unit,
) {
    //todo переместить вью модель в настройки
    val state by viewModel.state.collectAsStateWithLifecycle(initialState = InAppUpdateState())
    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),//todo
            dragHandle = null,
//            containerColor = getBackgroundColor(), //TODO
//            contentColor = getOnBackgroundColor(), //TODO
            onDismissRequest = onBackClick,
            content = {
                InAppUpdateContent(
                    state = state,
                    onAction = viewModel::onAction,
                )
            }
        )
    }
}