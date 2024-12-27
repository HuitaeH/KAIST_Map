package com.example.kaistmap

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.InputStreamReader

class FilterActivity : Activity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var markerAdapter: MarkerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_activity)

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // JSON 파일 로드하여 파싱 후 어댑터에 연결
        val markerList = loadMarkerData()

        if (markerList.isNotEmpty()) {
            markerAdapter = MarkerAdapter(markerList)
            recyclerView.adapter = markerAdapter
        } else {
            Toast.makeText(this, "데이터가 없습니다.", Toast.LENGTH_SHORT).show()
        }
        val backButton: Button = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
    }

    // assets 폴더에서 JSON 파일을 로드하고 파싱하는 함수
    private fun loadMarkerData(): List<MarkerData> {
        val assetManager = assets
        val inputStream = assetManager.open("data.json") // assets 폴더 내 JSON 파일
        val reader = InputStreamReader(inputStream)
        val gson = Gson()

        // JSON을 MarkerData 리스트로 변환
        val markerList = gson.fromJson(reader, Array<MarkerData>::class.java).toList()
        reader.close()
        return markerList
    }
}
