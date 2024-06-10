package com.desuzed.everyweather.presentation.features.weather_next_days.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import coil.compose.rememberAsyncImagePainter
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysMainInfo
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.RegularText

@Composable
fun MaxMinTempWithImg(nextDaysMainInfo: NextDaysMainInfo) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dimen_8))
    ) {
        Image(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.dimen_40))
                .width(dimensionResource(id = R.dimen.dimen_40)),
            alignment = Alignment.CenterEnd,
            painter = rememberAsyncImagePainter(nextDaysMainInfo.iconUrl),
            contentDescription = null,
        )

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dimen_8))
        ) {
            BoldText(text = nextDaysMainInfo.maxTemp)
            RegularText(text = nextDaysMainInfo.minTemp)
        }
//        Image(
//            modifier = Modifier.rotate(if (isExpanded) 0f else 180f),
//            painter = painterResource(id = R.drawable.ic_arrow_24),
//            contentDescription = null,
//            alignment = Alignment.CenterEnd,
//        )
    }

}