package com.desuzed.everyweather.presentation.features.main_activity.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.desuzed.everyweather.R
import com.desuzed.everyweather.ui.elements.RegularText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@Preview
@Composable
private fun Preview() {
    EveryweatherTheme {
        Column {
            DownloadingInAppUpdateWidget(true, 500, 200)
        }
    }
}

@Composable
fun ColumnScope.DownloadingInAppUpdateWidget(
    isDownloadingInProgress: Boolean,
    totalBytes: Long,
    bytesDownloaded: Long,
) {
    if (isDownloadingInProgress) {
        RegularText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.dimen_4)),
            text = stringResource(id = R.string.downloading_update),
            color = EveryweatherTheme.colors.onBackgroundPrimary,
            textAlign = TextAlign.Center,
        )
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = bytesDownloaded.toFloat() / totalBytes.toFloat(),
            color = EveryweatherTheme.colors.primary,
        )
    }
}