package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListItemPendingClosingBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ClosingModel
import java.text.SimpleDateFormat
import java.util.Locale

class ClosingPendingAdapter(
    val data : MutableList<ClosingModel>
) : RecyclerView.Adapter<ClosingPendingAdapter.ClosingPendingViewHolder>() {

    inner class ClosingPendingViewHolder(val binding : ListItemPendingClosingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClosingPendingViewHolder {
        return ClosingPendingViewHolder(ListItemPendingClosingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ClosingPendingViewHolder, position: Int) {
        holder.binding.apply {
            if(data[position].employeePhotoProfile != null){
                val imageUrl = "http://13.215.81.247/assets.admin_master/images/photo_profile/${data[position].employeePhotoProfile}"
                Glide.with(imgProfile.context).load(imageUrl).into(imgProfile)
            }
            tvName.text = data[position].employeeName
            val desc = data[position].employeeRole
            tvDesc.text = desc
            if(data[position].closedAtDateOnly != null){
                val dateTimeClosing = "Waktu ${formatTanggal(data[position].closedAtDateOnly!!)},${data[position].closedAtTimeOnly}"
                tvTimeClosing.text = dateTimeClosing
            }else{
                tvTimeClosing.text = "-"
            }
            if(data[position].status == "Closed"){
                tvClosing.text = "Closed"
                tvClosing.setBackgroundResource(R.drawable.bg_rounded_green)
            }else{
                tvClosing.text = "Belum Closing"
                tvClosing.setBackgroundResource(R.drawable.bg_rounded_red)
            }

            tvCountFinishWork.text = createSpannable("${if(data[position].totalDone != null) data[position].totalDone else "-"}/${if(data[position].totalTarget != null) data[position].totalTarget else "-"}", "#00C49A")
            tvCountDiversionWork.text = createSpannable("${if(data[position].totalDiverted != null) data[position].totalDiverted else "-"}/${if(data[position].totalTarget != null) data[position].totalTarget else "-"}", "#FF6347")
        }
    }

    private fun formatTanggal(tanggal: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val date = inputFormat.parse(tanggal)
        return outputFormat.format(date)
    }

    private fun createSpannable(text: String, firstPartColor: String): SpannableString {
        val separatorIndex = text.indexOf("/")
        val spannableString = SpannableString(text)

        val firstColor = ForegroundColorSpan(Color.parseColor(firstPartColor))
        spannableString.setSpan(firstColor, 0, separatorIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val blackColor = ForegroundColorSpan(Color.BLACK)
        spannableString.setSpan(blackColor, separatorIndex, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableString
    }
}