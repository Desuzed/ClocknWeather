package com.desuzed.everyweather.ui.elements

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.desuzed.everyweather.R
import com.desuzed.everyweather.ui.theming.EveryweatherTheme


@Composable
fun AppAlertDialog(title: String, onPositiveButtonClick: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = EveryweatherTheme.colors.onSurface,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_16)),
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                RegularText(text = title)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BoldText(text = stringResource(id = R.string.cancel), onClick = onDismiss)
                    RoundedButton(
                        text = stringResource(id = R.string.ok),
                        onClick = onPositiveButtonClick
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "PreviewAppAlertDialog"
)
@Composable
private fun PreviewAppAlertDialog() {
    AppAlertDialog("Delete", onPositiveButtonClick = {}, {})
}