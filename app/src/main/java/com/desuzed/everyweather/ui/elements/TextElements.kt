package com.desuzed.everyweather.ui.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.desuzed.everyweather.R
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
fun RegularText(text: String, modifier: Modifier = Modifier, textAlign: TextAlign? = null) {
    Text(
        modifier = modifier,
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
) {
    Text(
        modifier = modifier,
        text = text,
        style = EveryweatherTheme.typography.textMedium,
        color = color,
        overflow = overflow,
        maxLines = 1,
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
        style = EveryweatherTheme.typography.textLarge,
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
fun TextPair(header: String, text: String, modifier: Modifier = Modifier) {
    SmallText(text = header)
    BoldText(text = text)
}

@Composable
fun LocationText(text: String, onLocationClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                onLocationClick()
            },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_16)),
        backgroundColor = EveryweatherTheme.colors.textBg,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onLocationClick,
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
    // Creating an annonated string
    val mAnnotatedLinkString = buildAnnotatedString {

        // creating a string to display in the Text
        val mStr = stringResource(id = R.string.powered_by1)

        // word and span to be hyperlinked
        val mStartIndex = mStr.indexOf("W")
        val mEndIndex = mStr.indexOf("i")

        append(mStr)
        addStyle(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            ), start = mStartIndex, end = mEndIndex
        )

        addStringAnnotation(
            tag = "URL",
            annotation = "https://www.weatherapi.com/",
            start = mStartIndex,
            end = mEndIndex
        )

    }

// UriHandler parse and opens URI inside
// AnnotatedString Item in Browse
    val mUriHandler = LocalUriHandler.current

    // ???? Clickable text returns position of text
    // that is clicked in onClick callback
    ClickableText(
        text = mAnnotatedLinkString,
        modifier = modifier,
        onClick = {
            mAnnotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    mUriHandler.openUri(stringAnnotation.item)
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
        shape = RoundedCornerShape(
            dimensionResource(id = R.dimen.corner_radius_16)
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = EveryweatherTheme.colors.textColorPrimary,
            focusedBorderColor = EveryweatherTheme.colors.editTextStrokeColor,
            unfocusedBorderColor = EveryweatherTheme.colors.editTextStrokeColor,
            cursorColor = EveryweatherTheme.colors.secondary,
        ),
        leadingIcon = {
            Image(
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppEditText(text: String, onTextChanged: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(
                    2.dp,
                    EveryweatherTheme.colors.editTextStrokeColor
                ),
                shape = RoundedCornerShape(
                    dimensionResource(id = R.dimen.corner_radius_16)
                )
            )
            .padding(4.dp)
    ) {


        BasicTextField(
            value = "",
            onValueChange = onTextChanged,
            modifier = Modifier.fillMaxWidth(),
            decorationBox = @Composable { innerTextField ->
                // places leading icon, text field with label and placeholder, trailing icon
                TextFieldDefaults.TextFieldDecorationBox(
                    value = "12344",
                    placeholder = {
                        HintEditText(
                            text = stringResource(id = R.string.search_hint)
                        )
                    },
                    innerTextField = innerTextField,
                    visualTransformation = VisualTransformation.None,
                    singleLine = true,
                    enabled = true,
                    interactionSource = remember { MutableInteractionSource() },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = EveryweatherTheme.colors.textColorPrimary,
                        focusedBorderColor = EveryweatherTheme.colors.editTextStrokeColor,
                        unfocusedBorderColor = EveryweatherTheme.colors.editTextStrokeColor,
                        backgroundColor = Color.Transparent,
                    ),
                    contentPadding = PaddingValues(4.dp)
                )
            }
        )
    }
}


