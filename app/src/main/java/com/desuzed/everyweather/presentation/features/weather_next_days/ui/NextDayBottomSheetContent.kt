package com.desuzed.everyweather.presentation.features.weather_next_days.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import com.desuzed.everyweather.ui.elements.CardDetailDayItem
import com.desuzed.everyweather.ui.elements.HourItemContent
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

//@AppPreview
//@Composable
//private fun Preview() {
//    EveryweatherTheme {
//        NextDayBottomSheetContent(dayItem = NextDaysUi())
//    }
//}

@Composable
fun NextDayBottomSheetContent(
    dayItem: NextDaysUi,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(EveryweatherTheme.colors.primaryBackground.first())
            .padding(bottom = dimensionResource(id = R.dimen.dimen_10)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dimen_10))
    ) {
        NextDayItemContent(nextDaysMainInfo = dayItem.nextDaysMainInfo)
        CardDetailDayItem(
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.dimen_20),
            ),
            detailCard = dayItem.detailCard,
        )
        LazyRow {
            items(items = dayItem.hourList) { hourItem ->
                HourItemContent(hourItem = hourItem)
            }
        }
    }
}
