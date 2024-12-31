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
import android.widget.GridLayout




class SpinnerWheelFragment : DialogFragment() {
    private val sectorLabels = mutableListOf<String>()
    private var currentRotation = 0f // Track the cumulative rotation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_roulette, container, false)
        dialog?.window?.setBackgroundDrawable(requireContext().getDrawable(R.drawable.round_button))

        // X 버튼 클릭 시 팝업 닫기
        val backButton: ImageButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            dismiss()
        }

        //----------------------------------------------------------------------------------
        // controlling the number of sectors
        val sectorCountTextView: TextView = view.findViewById(R.id.sectorCountTextView)
        val minusButton: ImageButton = view.findViewById(R.id.minusButton)
        val plusButton: ImageButton = view.findViewById(R.id.plusButton)

        // Sector count and layout updates
        var sectorCount = 2
        sectorCountTextView.text = sectorCount.toString()

        // Function to dynamically update the spinner and labels
        fun updateSectors() {
            val spinnerWheel = view.findViewById<SpinnerWheelView>(R.id.spinnerWheel)  // Access spinnerWheel here

            spinnerWheel.setSectors(sectorCount)  // Update the number of sectors
            spinnerWheel.setSectorLabels(sectorLabels)  // Update the labels too

            // Create a GridLayout for 2-column layout
            val sectorLayout = GridLayout(context).apply {
                rowCount = sectorCount / 2 + sectorCount % 2 // Rows needed for odd counts
                columnCount = 4
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            // Clear existing dynamic views
            sectorLayout.removeAllViews()
            sectorLabels.clear()

            // Add TextViews and EditTexts dynamically in 2-column grid
            for (i in 0 until sectorCount) {
                // TextView
                val sectorTextView = TextView(context).apply {
                    text = "${i + 1}. "
                    textSize = 14f
                    setPadding(8, 8, 8, 8)
                    setTextColor(Color.BLACK)
                }
                // EditText
                val sectorEditText = EditText(context).apply {
                    hint = "${i + 1}번째 메뉴"
                    inputType = InputType.TYPE_CLASS_TEXT
                    setPadding(8, 8, 8, 8)
                }

                // Set layout params for 2-column grid
                val rowIndex = i / 2
                val columnIndex = if (i % 2 == 0) 0 else 2
                val editTextColumnIndex = if (i % 2 == 0) 1 else 3

                sectorTextView.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(rowIndex)
                    columnSpec = GridLayout.spec(columnIndex)
                }
                sectorEditText.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(rowIndex)
                    columnSpec = GridLayout.spec(editTextColumnIndex)
                }

                // Add TextView and EditText to the layout
                sectorLayout.addView(sectorTextView)
                sectorLayout.addView(sectorEditText)

                // Handle dynamic updates to sector labels
                sectorEditText.addTextChangedListener(object : TextWatcher {
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

            // Add GridLayout to the parent layout
            val sectorsLayout: LinearLayout = view.findViewById(R.id.sectorsLayout) // Layout to hold dynamic EditTexts
            sectorsLayout.addView(sectorLayout)
        }

        // Decrease button logic
        minusButton.setOnClickListener {
            if (sectorCount > 2) { // Minimum sectors = 2
                minusButton.isEnabled = true
                sectorCount--
                sectorCountTextView.text = sectorCount.toString()
                updateSectors()
            } else {
                minusButton.isEnabled = false
            }
        }

        // Increase button logic
        plusButton.setOnClickListener {
            if (sectorCount < 6) { // Maximum sectors = 6
                plusButton.isEnabled = true
                sectorCount++
                sectorCountTextView.text = sectorCount.toString()
                updateSectors()
            } else {
                plusButton.isEnabled = false
            }
        }

        // Initialize with default sector count
        updateSectors()

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