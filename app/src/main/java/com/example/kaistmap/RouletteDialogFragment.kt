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
import android.widget.LinearLayout // Required to work with dynamic layout views like EditText
import androidx.fragment.app.DialogFragment
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.text.TextWatcher
import android.text.Editable


class SpinnerWheelFragment : DialogFragment() {
    private val sectorLabels = mutableListOf<String>()

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
        val sectorsLayout: LinearLayout = view.findViewById(R.id.sectorsLayout) // Layout to hold dynamic EditTexts
        startButton.isEnabled = false



        // Confirm Button Logic
        confirmButton.setOnClickListener {
            val sectors = sectorInput.text.toString().toIntOrNull()
            if (sectors != null && sectors in 2..6) {
                spinnerWheel.setSectors(sectors) // Set the number of sectors on the spinner wheel

                // Clear existing dynamic EditText fields
                sectorsLayout.removeAllViews()

                // Add dynamic EditText fields based on the number of sectors
                sectorLabels.clear() // Clear previous labels
                for (i in 0 until sectors) {
                    val editText = EditText(context).apply {
                        hint = "Label for sector ${i + 1}"
                        inputType = InputType.TYPE_CLASS_TEXT
                        setPadding(8, 8, 8, 8)
                    }

                    // Add EditText to the layout
                    sectorsLayout.addView(editText)

                    // Add a TextWatcher to update sectorLabels when text is changed
                    editText.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                        override fun afterTextChanged(s: Editable?) {
                            if (s != null) {
                                if (sectorLabels.size <= i) {
                                    sectorLabels.add(s.toString())
                                } else {
                                    sectorLabels[i] = s.toString()
                                }
                            }
                        }
                    })
                }


                startButton.isEnabled = true // Enable the Start button
            } else {
                Toast.makeText(context, "2에서 6사이의 숫자를 넣어주세요", Toast.LENGTH_SHORT).show()
            }
        }

        // Start Button Logic
        startButton.setOnClickListener {
            val sectors = sectorInput.text.toString().toIntOrNull()

            if ((sectors == null || sectors !in 2..6) || startButton.isEnabled != true) {
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
        val sector = (0 until sectors).random()

        // Add a small random offset to prevent the wheel from landing exactly on the sector borders
        val randomOffset = (0..360).random() // Random angle between 0 and 360 degrees
        val finalRotation = 360f * 5 + (360f / sectors) * sector + randomOffset

        val animator = ValueAnimator.ofFloat(0f, finalRotation)
        animator.duration = 3000 // 3 seconds
        animator.addUpdateListener {
            view.rotation = it.animatedValue as Float
        }
        animator.start()

        // Show the result after the spin ends
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(context, "오늘 밥은 ${sector + 1}!", Toast.LENGTH_SHORT).show()
        }, 3000)
    }
}
