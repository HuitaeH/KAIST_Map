package com.example.kaistmap

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class SpinnerWheelFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_roulette, container, false)

        // X 버튼 클릭 시 팝업 닫기
        val backButton: ImageButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            dismiss()
        }

        val spinnerWheel = view.findViewById<SpinnerWheelView>(R.id.spinnerWheel)
        val sectorInput = view.findViewById<EditText>(R.id.sectorInput)
        val confirmButton = view.findViewById<Button>(R.id.confirmButton)
        val startButton = view.findViewById<Button>(R.id.startButton)




        // Confirm Button Logic
        confirmButton.setOnClickListener {
            val sectors = sectorInput.text.toString().toIntOrNull()
            if (sectors != null && sectors in 2..6) {
                spinnerWheel.setSectors(sectors) // Set the number of sectors on the spinner wheel
                startButton.isEnabled = true // Enable the Start button
            } else {
                Toast.makeText(context, "2에서 6사이의 숫자를 넣어주세요", Toast.LENGTH_SHORT).show()
            }
        }

        // Start Button Logic
        startButton.setOnClickListener {
            val sectors = sectorInput.text.toString().toIntOrNull()

            if (sectors == null || sectors !in 2..6) {
                // Show the toast if the sectors input is invalid or not within the range
                Toast.makeText(context, "고민하고 있는 메뉴의 개수를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                // Proceed with spinning the wheel if the input is valid
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
