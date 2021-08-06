package com.desuzed.clocknweather.util

import java.util.*

class TimeGetter {
    val hour: Int
        get() = GregorianCalendar()[Calendar.HOUR]
    val minute: Int
        get() = GregorianCalendar()[Calendar.MINUTE]
    val sec: Int
        get() = GregorianCalendar()[Calendar.SECOND]
    val mSec: Int
        get() = GregorianCalendar()[Calendar.MILLISECOND]
}