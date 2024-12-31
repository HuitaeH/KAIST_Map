package com.example.kaistmap

import android.os.Bundle
import android.text.Editable
import android.content.Intent
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import java.io.InputStreamReader


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var searchBox: EditText
    private var markerDataList: List<MarkerData> = listOf()
    private var markerList: MutableList<Marker> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        supportActionBar?.hide()

        // MapView 객체 초기화
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // 검색창 초기화
        searchBox = findViewById(R.id.search_box)

        // Go List 버튼 클릭 이벤트
        val golistbutton = findViewById<ImageButton>(R.id.golistbutton)
        golistbutton.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            true
        }
        val buttonShowRoulette: ImageButton = findViewById(R.id.buttonShowRoulette)


        buttonShowRoulette.setOnClickListener {
            val rouletteDialog = SpinnerWheelFragment()
            rouletteDialog.show(supportFragmentManager, "RouletteDialog")
        }

        // "초기화" 버튼 클릭 이벤트 - 지도 리셋
        val resetButton = findViewById<ImageButton>(R.id.reset_button)
        resetButton.setOnClickListener {
            resetMapToInitialPosition()
        }

        // 검색창 텍스트 변화에 따른 필터링
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterMarkers(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // 지도의 초기 위치 설정
        val cameraPosition = CameraPosition(LatLng(36.369809, 127.361298), 14.3) // 초기 위치 설정 (예: 서울)
        naverMap.cameraPosition = cameraPosition

        // 마커 데이터 로딩
        markerDataList = loadMarkerData()

        // 마커 추가
        addMarkers(markerDataList)
    }

    // 마커 리스트를 지도에 추가하는 메소드
    private fun addMarkers(markerDataList: List<MarkerData>) {
        for (markerData in markerDataList) {
            val marker = Marker()
            marker.position = LatLng(markerData.lat, markerData.lng)
            when (markerData.menu) {
                "한식" -> marker.icon = OverlayImage.fromResource(R.drawable.korea)
                "일식" -> marker.icon = OverlayImage.fromResource(R.drawable.japan)
                "중식" -> marker.icon = OverlayImage.fromResource(R.drawable.china)
                "양식" -> marker.icon = OverlayImage.fromResource(R.drawable.west)
                "기타" -> marker.icon = OverlayImage.fromResource(R.drawable.others)
            }
            marker.width = 100
            marker.height = 120
            marker.map = naverMap
            marker.captionText = markerData.name
            marker.captionTextSize = 16f

            // 마커 클릭 이벤트
            marker.setOnClickListener {
                val dialogFragment = MyDialogFragment.newInstance(markerData)
                dialogFragment.show(supportFragmentManager, "MyDialogFragment")
                true
            }

            markerList.add(marker)
        }
    }

    // 마커 데이터 로딩
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

    // 검색어에 맞는 마커만 필터링하여 보여주는 메소드
    private fun filterMarkers(query: String) {
        // 기존 마커 숨기기
        for (marker in markerList) {
            marker.map = null
        }

        // 검색어가 포함된 마커만 필터링하여 다시 표시
        var filteredMarkers = markerDataList.filter {
            it.name.contains(query, ignoreCase = true)
        }
        if (query.startsWith("#")) {
            filteredMarkers = markerDataList.filter {
                it.menu.contains(query.substring(1), ignoreCase = true)
            }
        }
        // 필터링된 마커를 다시 지도에 표시
        addMarkers(filteredMarkers)
    }

    private fun resetMapToInitialPosition() {
        val cameraPosition = CameraPosition(LatLng(36.369809, 127.361298), 14.3) // 초기 위치 설정 (예: 서울)
        naverMap.cameraPosition = cameraPosition
    }

    // 생명주기 메소드들 추가
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }
}
