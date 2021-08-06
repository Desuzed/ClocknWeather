package com.desuzed.clocknweather.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

@SuppressLint("AppCompatCustomView")
class ArrowImageView : ImageView {
    private var listener: OnRotationChangedListener? = null
    fun setListener(listener: OnRotationChangedListener?) {
        this.listener = listener
    }

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    fun setRotation(rotation: Float, arrowIndex: Int) {
        val oldRotation = rotation
        if (oldRotation != rotation) {
            listener!!.rotationChanged(rotation, arrowIndex)
        }
        super.setRotation(rotation)
    }

    //метод для того, чтобы музыка не включалась при запуске приложения
    fun rotateArrow(rotation: Float): Boolean {
        var isChanged = false
        val oldRotation = this.rotation
        if (oldRotation != rotation) {
            isChanged = true
        }
        super.setRotation(rotation)
        return isChanged
    }

    interface OnRotationChangedListener {
        fun rotationChanged(rotation: Float, arrowIndex: Int)
    }
}