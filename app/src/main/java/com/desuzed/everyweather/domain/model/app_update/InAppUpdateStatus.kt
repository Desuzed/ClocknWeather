package com.desuzed.everyweather.domain.model.app_update

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class InAppUpdateStatus : Parcelable {
    READY_TO_LAUNCH_UPDATE,
    READY_TO_INSTALL,
}