package com.desuzed.everyweather.view.fragments.weather.next_days

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.desuzed.everyweather.MockWeatherObject
import com.desuzed.everyweather.R
import com.desuzed.everyweather.ui.elements.*
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.view.ui.next_days.NextDaysMainInfo
import com.desuzed.everyweather.view.ui.next_days.NextDaysMapper
import com.desuzed.everyweather.view.ui.next_days.NextDaysUi

//Todo migrate to ModalBottomSheetLayout
//todo тем самым решить проблему со скролом
@Composable
fun NextDaysBottomSheetContent(
    state: NextDaysState,
) {
    EveryweatherTheme {
        Surface(
            modifier = Modifier.height(350.dp) ,
            shape = RoundedCornerShape(
                dimensionResource(id = R.dimen.corner_radius_16),
                dimensionResource(id = R.dimen.corner_radius_16)
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 20.dp,
                        start = 4.dp,
                        end = 4.dp,
                        bottom = 8.dp,
                    )
            ) {
                LazyColumn(
                    modifier = Modifier
                       // .padding(top = 40.dp, bottom = 1.dp)
                        .fillMaxHeight()
                ) { //todo разобраться с паддингами и тенями от карточки
                    items(items = state.nextDaysUiList) { forecastItem ->
                        ForecastListItem(dayItem = forecastItem)
                    }
                }
            }
        }

    }
}


@Composable
fun ForecastListItem(
    dayItem: NextDaysUi,
) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxSize()
            .clickable {
                isExpanded =
                    !isExpanded        //Todo подумать как можно сделать чтобы клик отрабатывался не на всю карточку, а на верхнюю область
            },
        shape = RoundedCornerShape(
            dimensionResource(id = R.dimen.corner_radius_16),
            dimensionResource(id = R.dimen.corner_radius_16),
            dimensionResource(id = R.dimen.corner_radius_16),
            dimensionResource(id = R.dimen.corner_radius_16),
        ),
        backgroundColor = EveryweatherTheme.colors.tertiary,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TopAreaDayItem(nextDaysMainInfo = dayItem.nextDaysMainInfo, isExpanded)
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    CardDetailDayItem(
                        detailCard = dayItem.detailCard,
                        Modifier.padding(horizontal = 20.dp)
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

@Composable
fun TopAreaDayItem(nextDaysMainInfo: NextDaysMainInfo, isExpanded: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BoldText(text = nextDaysMainInfo.date)
            RegularText(text = nextDaysMainInfo.description)
        }
        MaxMinTempWithImg(nextDaysMainInfo = nextDaysMainInfo, isExpanded)
    }
}

@Composable
fun MaxMinTempWithImg(nextDaysMainInfo: NextDaysMainInfo, isExpanded: Boolean) {
    var iconSize by remember { mutableStateOf(0) }
    Row(
        modifier = Modifier.onGloballyPositioned {
            iconSize = it.size.height
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp),
            alignment = Alignment.CenterEnd,
            painter = rememberAsyncImagePainter(nextDaysMainInfo.iconUrl),
            //  painter = painterResource(id = R.drawable.ic_arrow_24),
            contentDescription = "",
        )

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BoldText(text = nextDaysMainInfo.maxTemp)
            RegularText(text = nextDaysMainInfo.minTemp)
        }
        Image(
            modifier = Modifier.rotate(if (isExpanded) 0f else 180f),
            painter = painterResource(id = R.drawable.ic_arrow_24),
            contentDescription = "",
            alignment = Alignment.CenterEnd,
        )
    }

}

@Preview(
    showBackground = true,
    widthDp = 400,
    uiMode = UI_MODE_NIGHT_NO,
    name = "NextDaysBottomSheetContentPreview"
)
@Composable
private fun PreviewNextDaysBottomSheetContent() {
    val state = NextDaysState(
        nextDaysUiList = NextDaysMapper(LocalContext.current.resources).mapToNextDaysList(
            MockWeatherObject.weather
        )
    )
    NextDaysBottomSheetContent(
        state,
    )
}