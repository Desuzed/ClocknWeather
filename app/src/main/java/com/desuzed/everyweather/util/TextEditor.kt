package com.desuzed.everyweather.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import java.text.SimpleDateFormat

class TextEditor(context: Context) {
    val resources: Resources = context.resources

    @SuppressLint("SimpleDateFormat")
    val ddMMyy = SimpleDateFormat("dd.MM.yy")

}