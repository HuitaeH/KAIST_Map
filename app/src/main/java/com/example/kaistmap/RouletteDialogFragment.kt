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
import android.widget.TextView // Import TextView
import android.graphics.Color // Import Color for setting text color


class SpinnerWheelFragment : DialogFragment() {
    private val sectorLabels = mutableListOf<String>()
    private var currentRotation = 0f // Track the cumulative rotation


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


        // Inside the confirmButton's click listener, after setting sectors
        confirmButton.setOnClickListener {
            val sectors = sectorInput.text.toString().toIntOrNull()
            if (sectors != null && sectors in 2..6) {
                // Set the number of sectors and clear previous labels
                spinnerWheel.setSectors(sectors)

                // Clear existing dynamic EditText fields
                sectorsLayout.removeAllViews()

                // Add dynamic EditText fields based on the number of sectors
                sectorLabels.clear() // Clear previous labels
                for (i in 0 until sectors) {
                    // Create a LinearLayout to contain the TextView and EditText together
                    val sectorLayout = LinearLayout(context).apply {
                        orientation = LinearLayout.HORIZONTAL // Set orientation to horizontal
                        setPadding(8, 8, 8, 8) // Add padding around the layout
                    }

                    // Create a TextView for the sector number (e.g., "Sector 1")
                    val sectorTextView = TextView(context).apply {
                        text = "${i + 1}. " // Set the text to indicate the sector number
                        textSize = 16f // Adjust text size
                        setPadding(8, 8, 8, 8) // Padding for spacing
                        setTextColor(Color.BLACK) // Correct way to set text color
                    }

                    val editText = EditText(context).apply {
                        hint = "Label for sector ${i + 1}"
                        inputType = InputType.TYPE_CLASS_TEXT
                        setPadding(8, 8, 8, 8)
                    }



                    // Add the TextView and EditText to the LinearLayout
                    sectorLayout.addView(sectorTextView)
                    sectorLayout.addView(editText)

                    // Add the LinearLayout (which contains the TextView and EditText) to sectorsLayout
                    sectorsLayout.addView(sectorLayout)

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
                // Pass the labels to the spinner wheel
                spinnerWheel.setSectorLabels(sectorLabels)
                spinWheel(spinnerWheel, sectors)
            }
        }

        return view
    }


    private fun spinWheel(view: View, sectors: Int) {
        // Store the current rotation before starting the animation
        currentRotation = view.rotation

        // Randomly pick a sector
        val whichSector = (0 until sectors).random()

        // Add a random offset within the sector to avoid landing exactly on borders
        val randomOffset = (0 until (360 / sectors)).random()
        val finalRotation = currentRotation + 360f * 5 + (360f / sectors) * whichSector + randomOffset

        // Animate the rotation
        val animator = ValueAnimator.ofFloat(currentRotation, finalRotation)
        animator.duration = 3000 // 3 seconds
        animator.addUpdateListener {
            view.rotation = it.animatedValue as Float
        }
        // Update cumulative rotation
        currentRotation = finalRotation % 360f

        //check if the list is filled - else just print out the message
        if (sectorLabels.size<sectors) {
            Toast.makeText(requireContext(), "메뉴 리스트를 채워주세요", Toast.LENGTH_SHORT).show()
        } else {
            animator.start()

            // Post a delayed result calculation after the animation ends
            Handler(Looper.getMainLooper()).postDelayed({

                val currentSector = (sectors-1) - (currentRotation / (360f / sectors)).toInt()

                // Validate sectorLabels and display the result
                val chosen = sectorLabels[currentSector]
                Toast.makeText(requireContext(), "오늘 밥은 $chosen!", Toast.LENGTH_SHORT).show()
            }, 3000) // Delay matches the animation duration
        }



    }

}
