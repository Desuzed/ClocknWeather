package com.desuzed.everyweather.presentation.features.weather_main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.features.weather_main.WeatherAction
import com.desuzed.everyweather.presentation.ui.main.WeatherMainInfoUi
import com.desuzed.everyweather.ui.elements.DelimiterText
import com.desuzed.everyweather.ui.elements.GradientBox
import com.desuzed.everyweather.ui.elements.LocationText
import com.desuzed.everyweather.ui.elements.MediumBoldText
import com.desuzed.everyweather.ui.elements.MediumText
import com.desuzed.everyweather.ui.elements.UltraLargeBoldText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherHeaderInfo(
    mainInfoUi: WeatherMainInfoUi,
    refreshingState: PullRefreshState,
    onAction: (WeatherAction) -> Unit,
    onNewHeight: (Int) -> Unit,
) {
    GradientBox(
        modifier = Modifier.onGloballyPositioned {
            onNewHeight(it.size.height)
        },
        colors = EveryweatherTheme.colors.secondaryBackground,
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.dimen_20))
                .pullRefresh(refreshingState)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dimen_10))
        ) {
            LocationText(text = mainInfoUi.geoText, onAction)
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dimen_16)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.dimen_60))
                        .width(dimensionResource(id = R.dimen.dimen_60)),
                    alignment = Alignment.CenterEnd,
                    painter = rememberAsyncImagePainter(mainInfoUi.iconUrl),
                    contentDescription = "",
                )
                UltraLargeBoldText(
                    text = mainInfoUi.currentTemp,
                    color = EveryweatherTheme.colors.onBackgroundSecondary
                )
            }
            MediumText(
                text = mainInfoUi.feelsLike,
                color = EveryweatherTheme.colors.onBackgroundSecondary,
            )
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dimen_8)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MediumText(
                    text = mainInfoUi.date,
                    color = EveryweatherTheme.colors.onBackgroundSecondary,
                )
                DelimiterText()
                MediumBoldText(
                    text = mainInfoUi.time,
                    color = EveryweatherTheme.colors.onBackgroundSecondary,
                )
            }
            MediumText(
                text = mainInfoUi.description,
                color = EveryweatherTheme.colors.onBackgroundSecondary,
                maxLines = 2,
                textAlign = TextAlign.End
            )
        }
    }
}
