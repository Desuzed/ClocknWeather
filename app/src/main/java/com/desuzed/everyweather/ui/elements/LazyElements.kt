package com.desuzed.everyweather.ui.elements

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.presentation.ui.HourUi
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.MockWeatherObject

@Composable
fun HourItemContent(hourItem: HourUi) {
    EveryweatherTheme {
        Card(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.dimen_8))
                .wrapContentWidth(),
            shape = RoundedCornerShape(
                dimensionResource(id = R.dimen.corner_radius_16),
            ),
            backgroundColor = EveryweatherTheme.colors.onSurface,
            elevation = dimensionResource(id = R.dimen.dimen_4)
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_8)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SmallText(text = hourItem.time)
                BoldText(text = hourItem.temp)
                Image(
                    painter = rememberAsyncImagePainter(hourItem.iconUrl),
                    contentDescription = "",
                    modifier = Modifier.size(size = dimensionResource(id = R.dimen.dimen_34))
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_wind_direction),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(EveryweatherTheme.colors.textColorPrimary),
                        modifier = Modifier
                            .rotate(hourItem.rotation)
                            .size(size = dimensionResource(id = R.dimen.dimen_12))
                    )
                    SmallText(
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.dimen_4)),
                        text = hourItem.wind
                    )
                }
            }
        }
    }
}

@Composable
fun LocationItemContent(
    item: FavoriteLocationDto,
    onClick: (FavoriteLocationDto) -> Unit,
    onLongClick: (FavoriteLocationDto) -> Unit,
    onEditClick: (FavoriteLocationDto) -> Unit,
) {
    EveryweatherTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.dimen_4)),
            shape = RoundedCornerShape(
                dimensionResource(id = R.dimen.corner_radius_16),
            ),
            backgroundColor = EveryweatherTheme.colors.onSurface,
            elevation = dimensionResource(id = R.dimen.dimen_4)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(dimensionResource(id = R.dimen.dimen_10))
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = { onLongClick(item) },
                                onTap = { onClick(item) }
                            )
                        }
                ) {
                    LargeBoldText(
                        text = item.customName.ifBlank { item.cityName },
                        overflow = TextOverflow.Ellipsis,
                    )
                    MediumText(text = item.toString())
                }
                IconButton(onClick = { onEditClick(item) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit_location),
                        tint = EveryweatherTheme.colors.textColorPrimary,
                        contentDescription = ""
                    )
                }
            }

        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "LocationItemContentPreview"
)
@Composable
fun PreviewLocationItemContent() {
    LocationItemContent(
        item = FavoriteLocationDto("", "London", "RegionName", "England", "", ""),
        {}, {}, {})
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewDark"
)
@Composable
private fun PreviewHourItemContent() {
    HourItemContent(
        HourUi(
            windSpeed = WindSpeed("", 0, 0),
            temperature = Temperature("", 0, 0),
            hour = MockWeatherObject.weather.forecastDay[0].hourForecast[0],
            timeZone = MockWeatherObject.weather.location.timezone,
            res = LocalContext.current.resources
        )
    )
}