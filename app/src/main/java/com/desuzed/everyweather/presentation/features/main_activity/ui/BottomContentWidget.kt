package com.desuzed.everyweather.presentation.features.main_activity.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
        BottomContentWidget(isLookingForLocation = true) {}
    }
}

@Composable
fun BottomContentWidget(
    isLookingForLocation: Boolean,
    columnScopeContent: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(EveryweatherTheme.colors.primaryBackground.first())
            .navigationBarsPadding()
            .imePadding()
    ) {
        if (isLookingForLocation) {
            RegularText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_4)),
                text = stringResource(id = R.string.looking_for_your_location),
                color = EveryweatherTheme.colors.onBackgroundPrimary,
                textAlign = TextAlign.Center,
            )
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = EveryweatherTheme.colors.primary,
            )
        }
        columnScopeContent.invoke(this)
    }
}