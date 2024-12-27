package com.example.kaistmap

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class DetailActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Intent로부터 전달된 데이터 받기
        val markerLat = intent.getDoubleExtra("marker_lat", 0.0)
        val markerLng = intent.getDoubleExtra("marker_lng", 0.0)

        // JSON 파일 읽기
        // UI 업데이트
        val placeNameTextView: TextView = findViewById(R.id.latTextView)
        val descriptionTextView: TextView = findViewById(R.id.lngTextView)

        placeNameTextView.text = "위도: $markerLat"
        descriptionTextView.text = "경도: $markerLng"

        val backButton: Button = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
    }
}
