package com.example.kaistmap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SpinnerWheelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var numSectors = 6 // Default number of sectors
    private val colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA)

    fun setSectors(sectors: Int) {
        numSectors = sectors
        invalidate() // Redraw the view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = width / 2f
        val centerX = width / 2f
        val centerY = height / 2f

        val anglePerSector = 360f / numSectors
        for (i in 0 until numSectors) {
            paint.color = colors[i % colors.size]
            val startAngle = i * anglePerSector
            canvas.drawArc(
                0f, 0f, width.toFloat(), height.toFloat(),
                startAngle, anglePerSector, true, paint
            )
        }

        paint.color = Color.BLACK
        canvas.drawCircle(centerX, centerY, radius / 10, paint) // Center dot
    }
}
