package com.example.kaistmap

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView

class MyDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(markerData: MarkerData): MyDialogFragment {
            val fragment = MyDialogFragment()
            val args = Bundle()
            args.putInt("marker_id", markerData.id)
            args.putString("marker_name", markerData.name)
            args.putString("marker_addr", markerData.address)
            args.putString("marker_des", markerData.direction)
            fragment.arguments = args
            return fragment
        }
    }

    private var markerId: Int = 0
    private var markerName: String = ""
    private var markerAddr: String = ""
    private var markerDes: String = ""
    private lateinit var viewPager: ViewPager2
    private val imageList = mutableListOf<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val view = LayoutInflater.from(activity).inflate(R.layout.activity_detail, null)
        builder.setView(view)

        arguments?.let {
            markerId = it.getInt("marker_id", 0)
            markerName = it.getString("marker_name", "")
            markerAddr = it.getString("marker_addr", "")
            markerDes = it.getString("marker_des", "")
        }

        var index = 1
        while (true) {
            // 이미지 파일명을 f_1_1, f_1_2, f_1_3, ... 형식으로 생성
            val imageName = "f_${markerId}_$index"
            val resId = resources.getIdentifier(imageName, "drawable", requireContext().packageName)

            // 파일이 존재하지 않으면 종료
            if (resId == 0) break

            imageList.add(imageName)
            index++
        }

        // UI 업데이트
        val Title: TextView = view.findViewById(R.id.TitleTextView)
        Title.text = markerName
        val Address: TextView = view.findViewById(R.id.AddressTextView)
        Address.text = markerAddr
        val TextView: TextView = view.findViewById(R.id.TextView)
        TextView.text = markerDes

        // ViewPager2 설정
        viewPager = view.findViewById(R.id.viewPager)
        val adapter = ImagePagerAdapter(imageList)
        viewPager.adapter = adapter

        // X 버튼 클릭 시 팝업 닫기
        val backButton: ImageButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            dismiss()
        }

        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.let {
            // Dialog 크기 조정
            val width = ViewGroup.LayoutParams.WRAP_CONTENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)

            // 뒷 배경 음영 처리
            it.window?.setBackgroundDrawableResource(android.R.color.white)

            // 팝업에 패딩 추가 (상하좌우 24dp)
            it.window?.decorView?.setPadding(24, 24, 24, 24)

            // 배경을 둥글게 만들기
            it.window?.setBackgroundDrawable(requireContext().getDrawable(R.drawable.round_button))

            // 배경 음영 효과 설정
            it.window?.setDimAmount(0.7f)
        }
    }
}

class ImagePagerAdapter(private val imageList: List<String>) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageName = imageList[position]
        val resId = holder.itemView.context.resources.getIdentifier(imageName, "drawable", holder.itemView.context.packageName)
        holder.imageView.setImageResource(resId)
    }

    override fun getItemCount(): Int = imageList.size

    class ImageViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
}
