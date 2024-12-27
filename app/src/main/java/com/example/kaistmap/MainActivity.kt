package com.example.kaistmap

import android.content.Intent
import android.app.Activity
import android.os.Bundle
import com.naver.maps.map.MapView
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker

class MainActivity : Activity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // MapView 객체 초기화
        mapView = findViewById(R.id.map_view)

        // MapView의 onCreate 메소드 호출
        mapView.onCreate(savedInstanceState)

        // MapView가 준비되면 호출되는 콜백 메소드
        mapView.getMapAsync(this)
    }

    // 지도 준비가 완료되었을 때 호출되는 메소드
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // 지도의 초기 위치 설정
        val cameraPosition = CameraPosition(LatLng(36.36567, 127.36395), 15.0) // 서울의 위도, 경도
        naverMap.cameraPosition = cameraPosition

        // 마커 추가
        val marker = Marker()
        marker.position = LatLng(36.36567, 127.36395) // 서울의 위도, 경도
        marker.map = naverMap

        marker.setOnClickListener {
            // 마커 클릭 시 DetailActivity로 이동
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("marker_lat", 37.5665)
            intent.putExtra("marker_lng", 126.9780)
            startActivity(intent)
            true
        }
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
