package com.desuzed.everyweather.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.desuzed.everyweather.R
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.Constants

@Composable
fun FloatingButton(modifier: Modifier = Modifier, id: Int, onClick: () -> Unit) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        backgroundColor = EveryweatherTheme.colors.primary,
    ) {
        Image(
            painter = painterResource(id),
            colorFilter = ColorFilter.tint(EveryweatherTheme.colors.onPrimary),
            contentDescription = Constants.EMPTY_STRING,
        )
    }
}

@Composable
fun RoundedButton(modifier: Modifier = Modifier, onClick: () -> Unit, text: String) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_16)),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = EveryweatherTheme.colors.primary
        )
    )
    {
        MediumText(
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.dimen_4)),
            text = text,
            color = EveryweatherTheme.colors.onPrimary
        )
    }
}

@Composable
fun AppRadioButton(modifier: Modifier = Modifier, isSelected: Boolean, onClick: () -> Unit) {
    RadioButton(
        modifier = modifier,
        colors = RadioButtonDefaults.colors(
            selectedColor = EveryweatherTheme.colors.primary,
            unselectedColor = EveryweatherTheme.colors.primary,
            disabledColor = EveryweatherTheme.colors.onPrimary,
        ),
        selected = isSelected,
        onClick = onClick
    )
}