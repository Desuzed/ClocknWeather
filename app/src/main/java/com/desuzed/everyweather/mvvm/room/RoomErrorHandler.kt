package com.desuzed.everyweather.mvvm.room

import android.content.res.Resources
import com.desuzed.everyweather.R

class RoomErrorHandler(private val resources: Resources) {

    fun getInsertInfo(saved: Boolean): Pair <Int, String> {
        return if (saved) Pair (success, resources.getString(R.string.saved))
        else Pair (fail, getUnknownError())
    }

    fun getDeleteInfo(deleted: Boolean): Pair <Int, String> {
        return if (deleted)Pair (success, resources.getString(R.string.deleted))
        else Pair (fail, getUnknownError())
    }

    private fun getUnknownError(): String = resources.getString(R.string.unknown_app_error)

    companion object{
        const val success = 1
        const val fail = -1
    }
}