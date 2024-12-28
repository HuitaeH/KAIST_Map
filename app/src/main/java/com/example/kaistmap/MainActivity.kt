package com.example.kaistmap

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.map.MapView
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.google.gson.Gson
import java.io.InputStreamReader
import com.naver.maps.map.overlay.OverlayImage

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        supportActionBar?.hide()

        // MapView 객체 초기화
        mapView = findViewById(R.id.map_view)

        // MapView의 onCreate 메소드 호출
        mapView.onCreate(savedInstanceState)

        // MapView가 준비되면 호출되는 콜백 메소드
        mapView.getMapAsync(this)
        val golistbutton = findViewById<Button>(R.id.golistbutton)
        golistbutton.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java)
            startActivity(intent)
            true
        }
    }

    // 지도 준비가 완료되었을 때 호출되는 메소드
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // 지도의 초기 위치 설정
        val cameraPosition = CameraPosition(LatLng(36.36567, 127.36395), 15.0) // 서울의 위도, 경도
        naverMap.cameraPosition = cameraPosition

        // 마커 데이터 로딩
        val markerDataList = loadMarkerData()

        // 마커 추가
        for (markerData in markerDataList) {
            val marker = Marker()
            marker.position = LatLng(markerData.lat, markerData.lng)
            if (markerData.menu == "한식") {
                marker.icon = OverlayImage.fromResource(R.drawable.korea)
            }
            if (markerData.menu == "일식") {
                marker.icon = OverlayImage.fromResource(R.drawable.japan)
            }
            if (markerData.menu == "중식") {
                marker.icon = OverlayImage.fromResource(R.drawable.china)
            }
            if (markerData.menu == "양식") {
                marker.icon = OverlayImage.fromResource(R.drawable.west)
            }
            if (markerData.menu == "기타") {
                marker.icon = OverlayImage.fromResource(R.drawable.others)
            }

            marker.width = 100
            marker.height = 120

            marker.map = naverMap
            marker.captionText = markerData.name
            marker.captionTextSize = 16f

            marker.setOnClickListener {
                // 마커 클릭 시 DetailActivity로 이동
                val dialogFragment = MyDialogFragment.newInstance(markerData)
                dialogFragment.show(supportFragmentManager, "MyDialogFragment")
                true
            }
        }
    }

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