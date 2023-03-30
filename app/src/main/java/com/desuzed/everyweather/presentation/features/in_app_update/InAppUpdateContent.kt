package com.desuzed.everyweather.presentation.features.in_app_update

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.RegularText
import com.desuzed.everyweather.ui.elements.RoundedButton
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@Composable
fun InAppUpdateContent(
    state: InAppUpdateState,
    onUserInteraction: (InAppUpdateUserInteraction) -> Unit,
) {
    EveryweatherTheme {
        Surface(
            modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_350)),
            shape = RoundedCornerShape(
                topStart = dimensionResource(id = R.dimen.corner_radius_30),
                topEnd = dimensionResource(id = R.dimen.corner_radius_30)
            ),
            color = EveryweatherTheme.colors.bottomDialogBackground
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.dimen_10),
                    )
            ) {
                val titleId: Int
                val descriptionId: Int
                val negativeButtonTextId: Int
                val positiveButtonTextId: Int
                val positiveButtonInteraction: InAppUpdateUserInteraction
                when (state.updateStatus) {
                    InAppUpdateStatus.READY_TO_LAUNCH_UPDATE -> {
                        titleId = R.string.update_available_title
                        descriptionId = R.string.update_available_description
                        negativeButtonTextId = R.string.update_available_later_button
                        positiveButtonTextId = R.string.update_available_update_button
                        positiveButtonInteraction = InAppUpdateUserInteraction.AgreedToUpdate
                    }
                    InAppUpdateStatus.READY_TO_INSTALL -> {
                        titleId = R.string.update_ready_to_install_title
                        descriptionId = R.string.should_install_update_description
                        negativeButtonTextId = R.string.update_dismiss_button
                        positiveButtonTextId = R.string.update_install_button
                        positiveButtonInteraction = InAppUpdateUserInteraction.AgreedToInstallUpdate
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = dimensionResource(id = R.dimen.dimen_20)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        BoldText(text = stringResource(id = titleId))
                        RegularText(
                            text = stringResource(id = descriptionId),
                            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dimen_20))
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_weather_splash),
                        contentDescription = "",
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                vertical = dimensionResource(id = R.dimen.dimen_10)
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RegularText(
                            onClick = {
                                onUserInteraction(
                                    InAppUpdateUserInteraction.Dismiss
                                )
                            },
                            text = stringResource(id = negativeButtonTextId)
                        )
                        RoundedButton(
                            onClick = {
                                onUserInteraction(positiveButtonInteraction)
                            },
                            text = stringResource(id = positiveButtonTextId)
                        )
                    }
                }
            }
        }

    }
}

@Preview(
    showBackground = true,
    widthDp = 400,
    uiMode = UI_MODE_NIGHT_NO,
    name = "InAppUpdateContenttPreview"
)
@Composable
private fun PreviewNextDaysBottomSheetContent() {
    InAppUpdateContent(InAppUpdateState()) {}
}