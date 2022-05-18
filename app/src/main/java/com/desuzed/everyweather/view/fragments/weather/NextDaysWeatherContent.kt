package com.desuzed.everyweather.view.fragments.weather

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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.desuzed.everyweather.R
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.HourItemContent
import com.desuzed.everyweather.ui.elements.RegularText
import com.desuzed.everyweather.ui.elements.SmallText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.view.entity.HourUi
import com.desuzed.everyweather.view.fragments.weather.next_days.NextDaysState
import com.desuzed.everyweather.view.ui.DetailCard
import com.desuzed.everyweather.view.ui.NextDaysUi

//Todo migrate to ModalBottomSheetLayout
//todo тем самым решить проблему со скролом
@Composable
fun NextDaysBottomSheetContent(
    state: NextDaysState,
    //  onDayElementClick : () -> Unit,
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
            TopAreaDayItem(dayItem = dayItem, isExpanded)
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    CardDetailDayItem(dayItem = dayItem)
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
fun TopAreaDayItem(dayItem: NextDaysUi, isExpanded: Boolean) {
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
            BoldText(text = dayItem.date)
            RegularText(text = dayItem.description)
        }
        MaxMinTempWithImg(dayItem = dayItem, isExpanded)
    }
}

@Composable
fun MaxMinTempWithImg(dayItem: NextDaysUi, isExpanded: Boolean) {
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
            painter = rememberAsyncImagePainter(dayItem.iconUrl),
            //  painter = painterResource(id = R.drawable.ic_arrow_24),
            contentDescription = "",
        )

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BoldText(text = dayItem.maxTemp)
            RegularText(text = dayItem.minTemp)
        }
        Image(
            modifier = Modifier.rotate(if (isExpanded) 0f else 180f),
            painter = painterResource(id = R.drawable.ic_arrow_24),
            contentDescription = "",
            alignment = Alignment.CenterEnd,
        )
    }

}

@Composable
fun CardDetailDayItem(dayItem: NextDaysUi) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(
            dimensionResource(id = R.dimen.corner_radius_16),
            dimensionResource(id = R.dimen.corner_radius_16),
            dimensionResource(id = R.dimen.corner_radius_16),
            dimensionResource(id = R.dimen.corner_radius_16)
        ),
        backgroundColor = EveryweatherTheme.colors.onSurface,
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_20dp)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LeftColumn(dayItem = dayItem)
            RightColumn(dayItem = dayItem)
        }
    }
}

@Composable
fun LeftColumn(dayItem: NextDaysUi, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        TextPair(
            header = stringResource(id = R.string.humidity),
            text = dayItem.detailCard.humidity
        )
        TextPair(
            header = stringResource(id = R.string.pressure),
            text = dayItem.detailCard.pressure
        )
        TextPair(
            header = stringResource(id = R.string.sunrise_sunset),
            text = dayItem.detailCard.sun
        )
    }
}

@Composable
fun RightColumn(dayItem: NextDaysUi, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.End
    ) {
        TextPair(
            header = stringResource(id = R.string.probability_of_precipitation),
            text = dayItem.detailCard.pop
        )
        TextPair(header = stringResource(id = R.string.wind), text = dayItem.detailCard.wind)
        TextPair(
            header = stringResource(id = R.string.moonrise_moonset),
            text = dayItem.detailCard.moon
        )
    }
}

@Composable
fun TextPair(header: String, text: String) {
    SmallText(text = header)
    BoldText(text = text)
}

@Preview(
    showBackground = true,
    widthDp = 400,
    uiMode = UI_MODE_NIGHT_NO,
    name = "NextDaysBottomSheetContentPreview"
)
@Composable
private fun PreviewNextDaysBottomSheetContent() {
    val hour = HourUi("10:20", "27 ", "20 m/s", "icon url", 90f)
    val detailCard = DetailCard(
        "20 m/s",
        "125 mb",
        "98%",
        "88%",
        "06:20\n19:50",
        "06:20\n19:50"
    )
    val nextDay = NextDaysUi(
        "",
        "wed, 16/05",
        "desription",
        "27",
        "15",
        detailCard,
        listOf(hour, hour, hour, hour, hour),
    )
    val state = NextDaysState(nextDaysUiList = listOf(nextDay, nextDay, nextDay, nextDay))
    NextDaysBottomSheetContent(
        state,
        //onDayElementClick = {}
    )
}