package com.desuzed.everyweather.presentation.features.weather_next_days.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysMainInfo
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.RegularText

@Composable
fun TopAreaDayItem(nextDaysMainInfo: NextDaysMainInfo, isExpanded: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.dimen_20))
            .padding(top = dimensionResource(id = R.dimen.dimen_10)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dimen_8))
        ) {
            BoldText(text = nextDaysMainInfo.date)
            RegularText(text = nextDaysMainInfo.description, maxLines = 1)
        }
        MaxMinTempWithImg(nextDaysMainInfo = nextDaysMainInfo, isExpanded)
    }
}