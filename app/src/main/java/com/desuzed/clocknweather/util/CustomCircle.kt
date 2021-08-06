package com.desuzed.clocknweather.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class CustomCircle @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCircle(canvas)
    }

    private fun drawCircle(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.BLUE
        canvas.drawArc(RectF(), 0f, 300f, false, paint)
        canvas.drawCircle(pivotX, pivotY, 50f, paint)
    }
}