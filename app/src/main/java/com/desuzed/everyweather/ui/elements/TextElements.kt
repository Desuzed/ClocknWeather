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
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.desuzed.everyweather.R
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@Composable
fun BoldText(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = EveryweatherTheme.typography.h3
    )
}

@Composable
fun RegularText(text: String) {
    Text(
        text = text,
        style = EveryweatherTheme.typography.text,
    )
}

@Composable
fun MediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color,
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
        shape = RoundedCornerShape(
            dimensionResource(id = R.dimen.corner_radius_16),    //todo добавить более округлые
        ),
        backgroundColor = EveryweatherTheme.colors.textBg,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth()
                .clickable(
//todo onClick отрабатывает нестабильно, не всегда
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