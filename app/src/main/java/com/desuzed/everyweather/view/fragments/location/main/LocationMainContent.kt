package com.desuzed.everyweather.view.fragments.location.main

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.desuzed.everyweather.MockWeatherObject
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.ui.elements.*
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@Preview(
    showBackground = true,
    widthDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "PreviewLocationMainContent"
)
@Composable
private fun PreviewWeatherMainContent() {
    LocationMainContent(
        state = LocationMainState(locations = MockWeatherObject.locations),
        onMyLocationClick = {},
        onFavoriteLocationClick = {},
        onFavoriteLocationLongClick = {},
        onFindMapLocationClick = {},
        onFindTypedQueryClick = {},
        onGeoTextChanged = {},
    )
}

//todo заглушку
@Composable
fun LocationMainContent(
    state: LocationMainState,
    onMyLocationClick: () -> Unit,
    onFavoriteLocationClick: (FavoriteLocationDto) -> Unit,
    onFavoriteLocationLongClick: (FavoriteLocationDto) -> Unit,
    onFindMapLocationClick: () -> Unit,
    onFindTypedQueryClick: () -> Unit,
    onGeoTextChanged: (text: String) -> Unit,
) {
    EveryweatherTheme {
        val showDeleteDialog = remember { mutableStateOf<FavoriteLocationDto?>(null) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            EveryweatherTheme.colors.secondaryGradientStart,
                            EveryweatherTheme.colors.secondaryGradientEnd,
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                OutlinedEditText(
                    text = state.geoText,
                    modifier = Modifier.padding(10.dp),
                    hint = stringResource(id = R.string.search_hint),
                    onTextChanged = onGeoTextChanged,
                    onSearchClick = onFindTypedQueryClick,
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        //        .fillMaxHeight()
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                ) {
                    items(items = state.locations) { locationItem ->
                        AnimatedVisibility( //todo сделать анимацию
                            visible = true,
                            exit = fadeOut(
                                animationSpec = TweenSpec(200, 200, FastOutLinearInEasing)
                            )
                        ) {
                            LocationItemContent(
                                item = locationItem,
                                onClick = onFavoriteLocationClick,
                                onLongClick = { showDeleteDialog.value = it }
                            )
                        }
                    }
                }
                RoundedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    onClick = onFindMapLocationClick,
                    text = stringResource(id = R.string.find_on_map)
                )
            }
            FloatingButton(
                id = R.drawable.ic_my_location,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 100.dp)
                    .align(Alignment.BottomEnd),
                onClick = onMyLocationClick,
            )
            if (showDeleteDialog.value != null) {
                AppAlertDialog(title = stringResource(id = R.string.delete),
                    onPositiveButtonClick = {
                        onFavoriteLocationLongClick(showDeleteDialog.value!!)
                        showDeleteDialog.value = null
                    }, onDismiss = {
                        showDeleteDialog.value = null
                    }
                )
            }
            if (state.locations.isEmpty()) {
                RegularText(
                    text = stringResource(id = R.string.your_saved_cities_list_is_empty),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
        }
    }
}