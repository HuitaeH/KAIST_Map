package com.example.kaistmap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View


class SpinnerWheelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK         // Set the color to black for the outline
        strokeWidth = 3f            // Set a thin line width for the outline
        style = Paint.Style.STROKE  // Use STROKE style to only draw the outline (not filled)
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var numSectors = 2 // Default number of sectors
    private val colors = listOf(
        Color.parseColor("#FFB3BA"), // Pastel Pink
        Color.parseColor("#FFDFBA"), // Pastel Peach
        Color.parseColor("#FFFFBA"), // Pastel Yellow
        Color.parseColor("#BAFFC9"), // Pastel Green
        Color.parseColor("#BAE1FF"), // Pastel Blue
        Color.parseColor("#D7BAFF")  // Pastel Purple
    )

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
            val startAngle = i * anglePerSector // Store the start angle

            // Draw sector
            paint.color = colors[i % colors.size]
            canvas.drawArc(
                0f, 0f, width.toFloat(), height.toFloat(),
                startAngle, anglePerSector, true, paint
            )

            // Draw outline
            val endAngle = startAngle + anglePerSector
            val startX = centerX + radius * Math.cos(Math.toRadians(startAngle.toDouble())).toFloat()
            val startY = centerY + radius * Math.sin(Math.toRadians(startAngle.toDouble())).toFloat()
            val endX = centerX + radius * Math.cos(Math.toRadians(endAngle.toDouble())).toFloat()
            val endY = centerY + radius * Math.sin(Math.toRadians(endAngle.toDouble())).toFloat()

            canvas.drawLine(centerX, centerY, startX, startY, outlinePaint) // Line to start angle
            canvas.drawLine(centerX, centerY, endX, endY, outlinePaint)   // Line to end angle
        }

        //circle at the center of the wheel
        paint.color = Color.BLACK
        canvas.drawCircle(centerX, centerY, radius / 10, paint) // Center dot

        // Draw the outer circle outline
        canvas.drawCircle(centerX, centerY, radius, outlinePaint)


        // Draw a small inverted triangle (pointer) at the top
        val pointerHeight = 40f // Height of the pointer (increased for visibility)
        val pointerWidth = 60f // Width of the pointer (increased for visibility)
        val path = Path()

    }
}
