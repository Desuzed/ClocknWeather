package com.desuzed.everyweather.presentation.features.weather_next_days.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import com.desuzed.everyweather.ui.elements.CardDetailDayItem
import com.desuzed.everyweather.ui.elements.HourItemContent
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ForecastListItem(
    dayItem: NextDaysUi,
    isLastItem: Boolean,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    Card(
        onClick = {
            isExpanded = !isExpanded
        },
        interactionSource = interactionSource,
        modifier = Modifier
            .padding(
                top = dimensionResource(id = R.dimen.dimen_4),
                bottom = if (isLastItem) dimensionResource(id = R.dimen.dimen_8)
                else dimensionResource(id = R.dimen.dimen_0),
            )
            .fillMaxSize(),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_16)),
        backgroundColor = EveryweatherTheme.colors.surfaceSecondary,
        elevation = dimensionResource(id = R.dimen.dimen_8),
    ) {
        Column(
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.dimen_10)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dimen_10))
        ) {
            TopAreaDayItem(nextDaysMainInfo = dayItem.nextDaysMainInfo, isExpanded)
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    CardDetailDayItem(
                        detailCard = dayItem.detailCard,
                        Modifier.padding(horizontal = dimensionResource(id = R.dimen.dimen_20))
                    )
                    LazyRow {
                        items(items = dayItem.hourList) { hourItem ->
                            HourItemContent(hourItem = hourItem)
                        }
                    }
                }
            }
        }
    }
}