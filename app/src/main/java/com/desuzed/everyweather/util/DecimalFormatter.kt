package com.desuzed.everyweather.util

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object DecimalFormatter {
    const val KPH_TO_MS_MULTIPLIER = 0.277777f
    private const val precision: String = "%.1f"
    private const val decimalPattern: String = "#.#"

    fun formatFloat(value: Float): String = String.format(precision, value)

    fun formatFloatWithRoundingMode(value: Float): String {
        val decimalFormat = DecimalFormat(decimalPattern, DecimalFormatSymbols(Locale.ENGLISH))
        decimalFormat.roundingMode = RoundingMode.CEILING
        return decimalFormat.format(value)
    }
}