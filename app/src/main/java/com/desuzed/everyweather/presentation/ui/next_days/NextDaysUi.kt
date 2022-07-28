package com.desuzed.everyweather.presentation.ui.next_days

import com.desuzed.everyweather.presentation.ui.HourUi

data class NextDaysUi(
    val nextDaysMainInfo: NextDaysMainInfo,
    val detailCard: DetailCardNextDays,
    val hourList: List<HourUi>,
)