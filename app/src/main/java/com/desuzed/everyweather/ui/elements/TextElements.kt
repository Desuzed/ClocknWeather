package com.desuzed.everyweather.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.features.weather_main.WeatherUserInteraction
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@Composable
fun BoldText(text: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }
    Text(
        modifier = modifier.clickable(
            onClick = onClick,
            indication = null,
            interactionSource = interactionSource,
        ),
        text = text,
        style = EveryweatherTheme.typography.h3,
        color = EveryweatherTheme.colors.textColorPrimary,
    )
}

@Composable
fun RegularText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    Text(
        modifier = modifier.clickable(
            onClick = onClick,
            indication = null,
            interactionSource = interactionSource,
        ),
        text = text,
        style = EveryweatherTheme.typography.text,
        color = EveryweatherTheme.colors.textColorPrimary,
        textAlign = textAlign,
    )
}

@Composable
fun MediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = EveryweatherTheme.colors.textColorPrimary,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = 1,
    textAlign: TextAlign? = null,
) {
    Text(
        modifier = modifier,
        text = text,
        style = EveryweatherTheme.typography.textMedium,
        color = color,
        overflow = overflow,
        maxLines = maxLines,
        textAlign = textAlign,
    )
}

@Composable
fun MediumBoldText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    Text(
        modifier = modifier,
        text = text,
        style = EveryweatherTheme.typography.textMediumBold,
        color = color,
        overflow = overflow,
    )
}

@Composable
fun SmallText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = EveryweatherTheme.typography.textSmall,
        maxLines = 1,
        color = EveryweatherTheme.colors.textColorPrimary
    )
}

@Composable
fun UltraLargeBoldText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = EveryweatherTheme.colors.textColorPrimary
) {
    Text(
        modifier = modifier,
        text = text,
        style = EveryweatherTheme.typography.textUltraLarge,
        maxLines = 1,
        color = color,
    )
}

@Composable
fun LargeBoldText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = EveryweatherTheme.colors.textColorPrimary
) {
    Text(
        modifier = modifier,
        text = text,
        style = EveryweatherTheme.typography.textBoldLarge,
        maxLines = 1,
        color = color,
    )
}

@Composable
fun HintEditText(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = EveryweatherTheme.typography.textMedium,
        color = EveryweatherTheme.colors.editTextStrokeColor,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

@Composable
fun DelimiterText() {
    Text(
        text = stringResource(id = R.string.delimiter),
        style = EveryweatherTheme.typography.textLarge,
        color = EveryweatherTheme.colors.textColorSecondary
    )
}

@Composable
fun TextPair(header: String, text: String, modifier: Modifier = Modifier) {
    SmallText(text = header)
    BoldText(text = text)
}

@Composable
fun LocationText(text: String, onUserInteraction: (WeatherUserInteraction) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Card(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.dimen_4)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_16)),
        backgroundColor = EveryweatherTheme.colors.textBg,
        elevation = dimensionResource(id = R.dimen.dimen_0),
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = R.dimen.dimen_8),
                    vertical = dimensionResource(id = R.dimen.dimen_4)
                )
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { onUserInteraction(WeatherUserInteraction.Location) },
                ),
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            MediumText(
                modifier = Modifier.weight(1f),
                text = text,
                overflow = TextOverflow.Ellipsis,
                color = EveryweatherTheme.colors.textColorPrimary
            )
            Image(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.ic_edit_location),
                colorFilter = ColorFilter.tint(EveryweatherTheme.colors.textColorPrimary),
                contentDescription = "",
            )
        }

    }
}

@Composable
fun LinkText(modifier: Modifier) {

    val mAnnotatedLinkString = buildAnnotatedString {

        val inputStr = stringResource(id = R.string.powered_by)

        val startIndex = inputStr.indexOf("W")
        val endIndex = startIndex + 11
        append(inputStr)
        addStyle(
            style = SpanStyle(
                color = EveryweatherTheme.colors.secondary,
                textDecoration = TextDecoration.Underline
            ), start = startIndex, end = endIndex
        )
        addStringAnnotation(
            tag = "URL",
            annotation = stringResource(id = R.string.uri),
            start = startIndex,
            end = endIndex
        )

    }
    val uriHandler = LocalUriHandler.current
    ClickableText(
        text = mAnnotatedLinkString,
        modifier = modifier,
        style = EveryweatherTheme.typography.textMediumAnnotated,
        onClick = {
            mAnnotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OutlinedEditText(
    text: String,
    hint: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = text,
        onValueChange = onTextChanged,
        maxLines = 1,
        textStyle = EveryweatherTheme.typography.text,
        placeholder = {
            HintEditText(
                text = hint
            )
        },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_16)),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = EveryweatherTheme.colors.textColorPrimary,
            focusedBorderColor = EveryweatherTheme.colors.editTextStrokeColor,
            unfocusedBorderColor = EveryweatherTheme.colors.editTextStrokeColor,
            cursorColor = EveryweatherTheme.colors.secondary,
        ),
        trailingIcon = {
            Image(//todo clickable, handle state disabled/enabled
                painter = painterResource(id = R.drawable.ic_round_search),
                colorFilter = ColorFilter.tint(EveryweatherTheme.colors.editTextStrokeColor),
                contentDescription = "",
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            keyboard?.hide()
            focusManager.clearFocus()
            if (text.isNotEmpty()) onSearchClick()          //todo хендлить ошибку пустого текста?
        })
    )
}