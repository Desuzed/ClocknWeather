package com.desuzed.everyweather.presentation.features.in_app_update

import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus

data class InAppUpdateState(
    val updateStatus: InAppUpdateStatus = InAppUpdateStatus.READY_TO_LAUNCH_UPDATE,
)