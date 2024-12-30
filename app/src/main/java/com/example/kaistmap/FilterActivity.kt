package com.example.kaistmap

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.InputStreamReader

class FilterActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var markerAdapter: MarkerAdapter
    private lateinit var markerList: List<MarkerData>

    private lateinit var koreanFoodCheckbox: CheckBox
    private lateinit var japaneseFoodCheckbox: CheckBox
    private lateinit var chineseFoodCheckbox: CheckBox
    private lateinit var westernFoodCheckbox: CheckBox
    private lateinit var otherFoodCheckbox: CheckBox

    private lateinit var noDataText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_activity)

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 체크박스 초기화
        koreanFoodCheckbox = findViewById(R.id.checkBoxKorean)
        japaneseFoodCheckbox = findViewById(R.id.checkBoxJapanese)
        chineseFoodCheckbox = findViewById(R.id.checkBoxChinese)
        westernFoodCheckbox = findViewById(R.id.checkBoxWestern)
        otherFoodCheckbox = findViewById(R.id.checkBoxOther)

        // 데이터가 없을 때 표시할 텍스트
        noDataText = findViewById(R.id.noDataText)

        // JSON 파일 로드하여 파싱 후 어댑터에 연결
        markerList = loadMarkerData()

        // 체크박스 클릭 리스너 설정
        koreanFoodCheckbox.setOnCheckedChangeListener { _, _ -> filterMarkers() }
        japaneseFoodCheckbox.setOnCheckedChangeListener { _, _ -> filterMarkers() }
        chineseFoodCheckbox.setOnCheckedChangeListener { _, _ -> filterMarkers() }
        westernFoodCheckbox.setOnCheckedChangeListener { _, _ -> filterMarkers() }
        otherFoodCheckbox.setOnCheckedChangeListener { _, _ -> filterMarkers() }

        // 필터링 버튼 클릭 이벤트
        filterMarkers()

        // 뒤로가기 버튼
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
        // Roulette creating code
        val buttonShowRoulette: ImageButton = findViewById(R.id.buttonShowRoulette)

        buttonShowRoulette.setOnClickListener {
            val rouletteDialog = SpinnerWheelFragment()
            rouletteDialog.show(supportFragmentManager, "RouletteDialog")
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

    // 체크박스에 따라 마커 필터링
    private fun filterMarkers() {
        // 필터링된 마커 리스트
        val filteredList = markerList.filter { marker ->
            // 체크박스 상태에 따라 필터링
            val isKoreanFood = koreanFoodCheckbox.isChecked && marker.menu.contains("한식")
            val isJapaneseFood = japaneseFoodCheckbox.isChecked && marker.menu.contains("일식")
            val isChineseFood = chineseFoodCheckbox.isChecked && marker.menu.contains("중식")
            val isWesternFood = westernFoodCheckbox.isChecked && marker.menu.contains("양식")
            val isOtherFood = otherFoodCheckbox.isChecked && marker.menu.contains("기타")

            isKoreanFood || isJapaneseFood || isChineseFood || isWesternFood || isOtherFood
        }

        // 필터링된 리스트를 어댑터에 갱신
        if (filteredList.isNotEmpty()) {
            // 마커 어댑터에 필터링된 리스트를 전달
            markerAdapter = MarkerAdapter(filteredList)
            recyclerView.adapter = markerAdapter
            noDataText.visibility = TextView.GONE // 데이터가 있으면 메시지 숨기기
        } else {
            // 필터링된 리스트가 비어 있으면 RecyclerView 비우고 메시지 보이기
            recyclerView.adapter = null
            noDataText.visibility = TextView.VISIBLE // 데이터가 없으면 메시지 보이기
        }
    }
}
