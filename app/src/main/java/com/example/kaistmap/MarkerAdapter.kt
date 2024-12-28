package com.example.kaistmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity

class MarkerAdapter(private val markerList: List<MarkerData>) : RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>() {

    // ViewHolder 정의
    inner class MarkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.marker_name)
        val foodTextView: TextView = itemView.findViewById(R.id.marker_menu)
        val addrTextView: TextView = itemView.findViewById(R.id.marker_addr)
        val infoButton: ImageButton = itemView.findViewById(R.id.info_button)
        val foodimgView: ImageView = itemView.findViewById(R.id.marker_image)


        init {
            // infoButton 클릭 리스너를 여기에서 설정합니다.
            infoButton.setOnClickListener {
                val markerData = markerList[adapterPosition] // 현재 항목에 해당하는 MarkerData 가져오기
                val dialogFragment = MyDialogFragment.newInstance(markerData)
                dialogFragment.show((itemView.context as AppCompatActivity).supportFragmentManager, "MyDialogFragment")
            }
        }
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
        holder.foodTextView.text = currentMarker.menu
        holder.addrTextView.text = currentMarker.address
        val imageName = "f_${currentMarker.id}_1" // 여기에서 "1" 부분은 적절하게 조정 가능
        val resId = holder.itemView.context.resources.getIdentifier(imageName, "drawable", holder.itemView.context.packageName)

        // 이미지 설정
        if (resId != 0) {
            holder.foodimgView.setImageResource(resId)
        }
    }

    // 데이터의 크기 반환
    override fun getItemCount(): Int {
        return markerList.size
    }
}