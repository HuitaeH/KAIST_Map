package com.example.kaistmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MarkerAdapter(private val markerList: List<MarkerData>) : RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>() {

    // ViewHolder 정의
    inner class MarkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.marker_name)
        val latTextView: TextView = itemView.findViewById(R.id.marker_lat)
        val lngTextView: TextView = itemView.findViewById(R.id.marker_lng)
    }

    // onCreateViewHolder는 ViewHolder를 생성하여 반환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_marker, parent, false)
        return MarkerViewHolder(itemView)
    }

    // onBindViewHolder는 데이터를 ViewHolder에 바인딩
    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val currentMarker = markerList[position]
        holder.nameTextView.text = currentMarker.name
        holder.latTextView.text = "위도: ${currentMarker.lat}"
        holder.lngTextView.text = "경도: ${currentMarker.lng}"
    }

    // 데이터의 크기 반환
    override fun getItemCount(): Int {
        return markerList.size
    }
}