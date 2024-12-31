package com.example.kaistmap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent



class SpinnerWheelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK         // Set the color to black for the outline
        strokeWidth = 10f            // Set a thin line width for the outline
        style = Paint.Style.STROKE  // Use STROKE style to only draw the outline (not filled)
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 40f // Adjust text size
        textAlign = Paint.Align.CENTER
    }

    private var numSectors = 2 // Default number of sectors
    private val sectorLabels = mutableListOf<String>() // Labels for each sector
    private val colors = listOf(
        Color.parseColor("#FFB3BA"), // Pastel Pink
        Color.parseColor("#FFDFBA"), // Pastel Peach
        Color.parseColor("#FFFFBA"), // Pastel Yellow
        Color.parseColor("#BAFFC9"), // Pastel Green
        Color.parseColor("#BAE1FF"), // Pastel Blue
        Color.parseColor("#D7BAFF")  // Pastel Purple
    )
    var onCenterCircleClick: (() -> Unit)? = null // Callback for center circle click

    fun setSectors(sectors: Int) {
        numSectors = sectors
        invalidate() // Redraw the view
    }

    fun setSectorLabels(labels: List<String>) {
        sectorLabels.clear()
        sectorLabels.addAll(labels)
        invalidate() // Redraw the view with the new labels
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Adjust the size to account for the outline stroke width (10f)
        val outlinePadding = outlinePaint.strokeWidth / 2
        val adjustedWidth = width - outlinePadding * 2
        val adjustedHeight = height - outlinePadding * 2

        val radius = Math.min(adjustedWidth, adjustedHeight) / 2f
        val centerX = width / 2f
        val centerY = height / 2f
        // Rotate the canvas by -90 degrees (anticlockwise) around the center
        canvas.save()
        canvas.rotate(-90f, centerX, centerY)

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

            // Draw label if available
            if (sectorLabels.size > i) {
                drawLabel(canvas, sectorLabels[i], startAngle, anglePerSector, radius, centerX, centerY)
            }
        }
        //circle at the center of the wheel
        paint.color = Color.WHITE
        canvas.drawCircle(centerX, centerY, radius / 5, paint) // Center dot
        canvas.drawCircle(centerX, centerY, radius / 5, outlinePaint) // Outline around center circle

        // Draw the outer circle outline
        canvas.drawCircle(centerX, centerY, radius, outlinePaint)

        // Draw the "GO!" text inside the center circle (fixed position)
         textPaint.color = Color.BLACK // Color for the "GO!" text
        textPaint.textSize = 80f // Make the text larger
        canvas.drawText("GO!", centerX, centerY + textPaint.textSize / 3, textPaint)





    }


    private fun drawLabel(
        canvas: Canvas, label: String, startAngle: Float, anglePerSector: Float,
        radius: Float, centerX: Float, centerY: Float
    ) {
        val midAngle = startAngle + anglePerSector / 2
        val labelRadius = radius * 0.7f // Adjust the position of the label closer to the center

        // Calculate the position of the label based on the angle
        val labelX = centerX + labelRadius * Math.cos(Math.toRadians(midAngle.toDouble())).toFloat()
        val labelY = centerY + labelRadius * Math.sin(Math.toRadians(midAngle.toDouble())).toFloat()

        // Rotate the text so it faces the center of the wheel
        val rotateAngle = midAngle + 90f
        canvas.save()
        canvas.rotate(rotateAngle, labelX, labelY)

        // Draw the label text
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 40f
            textAlign = Paint.Align.CENTER
        }

        // Draw the text at the calculated position
        canvas.drawText(label, labelX, labelY, textPaint)
        canvas.restore()
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width / 2f / 5 // Radius of the center circle

        val distance = Math.sqrt(
            Math.pow((event.x - centerX).toDouble(), 2.0) + Math.pow(
                (event.y - centerY).toDouble(),
                2.0
            )
        )

        // Check if the touch is inside the center circle
        if (distance <= radius) {
            onCenterCircleClick?.invoke() // Trigger the callback to start spinning
            return true
        }
        return super.onTouchEvent(event)
    }

}
