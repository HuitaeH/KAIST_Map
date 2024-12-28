package com.example.kaistmap

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class SpinnerWheelFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_roulette, container, false)

        val spinnerWheel = view.findViewById<SpinnerWheelView>(R.id.spinnerWheel)
        val sectorInput = view.findViewById<EditText>(R.id.sectorInput)
        val startButton = view.findViewById<Button>(R.id.startButton)

        startButton.setOnClickListener {
            val sectors = sectorInput.text.toString().toIntOrNull() ?: return@setOnClickListener
            if (sectors in 2..8) {
                spinnerWheel.setSectors(sectors)
                spinWheel(spinnerWheel, sectors)
            }
        }

        return view
    }

    private fun spinWheel(view: View, sectors: Int) {
        val animator = ValueAnimator.ofFloat(0f, 360f * 5 + (360f / sectors) * (0 until sectors).random())
        animator.duration = 3000 // 3 seconds
        animator.addUpdateListener {
            view.rotation = it.animatedValue as Float
        }
        animator.start()
    }
}
