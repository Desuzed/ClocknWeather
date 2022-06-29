package com.desuzed.everyweather.ui.elements

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.desuzed.everyweather.MockWeatherObject
import com.desuzed.everyweather.R
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.view.ui.HourUi

@Composable
fun HourItemContent(hourItem: HourUi) {
    EveryweatherTheme {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentWidth(),
            shape = RoundedCornerShape(
                dimensionResource(id = R.dimen.corner_radius_16),
                dimensionResource(id = R.dimen.corner_radius_16),
                dimensionResource(id = R.dimen.corner_radius_16),
                dimensionResource(id = R.dimen.corner_radius_16),
            ),
            backgroundColor = EveryweatherTheme.colors.onSurface,
            elevation = 4.dp
        ) {
            Column (modifier = Modifier.padding(8.dp) , horizontalAlignment = Alignment.CenterHorizontally) {
                SmallText(text = hourItem.time)
                BoldText(text = hourItem.temp)
                Image(painter = rememberAsyncImagePainter(hourItem.iconUrl), contentDescription = "", modifier = Modifier.size(34.dp))
                Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_wind_direction),
                        contentDescription = "",
                        Modifier
                            .rotate(hourItem.rotation)
                            .size(12.dp)
                    )
                    SmallText(modifier = Modifier.padding(start = 4.dp), text = hourItem.wind)
                }
            }
        }
    }
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
            MockWeatherObject.weather.forecastDay[0].hourForecast[0],
            MockWeatherObject.weather.location.timezone,
            LocalContext.current.resources
        )
    )
}