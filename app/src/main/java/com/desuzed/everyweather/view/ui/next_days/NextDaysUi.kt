package com.desuzed.everyweather.view.ui.next_days

import com.desuzed.everyweather.view.ui.HourUi

data class NextDaysUi(
    val nextDaysMainInfo: NextDaysMainInfo,
    val detailCard: DetailCardNextDays,
    val hourList: List<HourUi>,
)