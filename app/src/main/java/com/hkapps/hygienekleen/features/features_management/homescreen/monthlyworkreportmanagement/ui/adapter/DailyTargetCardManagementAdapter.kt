package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.ui.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemCardDailyTargetBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.DailyTargetManagement
import java.text.SimpleDateFormat
import java.util.Locale

class DailyTargetCardManagementAdapter(
    private val data : List<DailyTargetManagement>,
private val listener : OnClickDailyTarget,
) : RecyclerView.Adapter<DailyTargetCardManagementAdapter.DailyTargetCardManagementViewHolder>() {


    inner class DailyTargetCardManagementViewHolder(val binding: ListItemCardDailyTargetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTargetCardManagementViewHolder {
        return DailyTargetCardManagementViewHolder(ListItemCardDailyTargetBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: DailyTargetCardManagementViewHolder, position: Int) {
        holder.binding.apply {

            tvArea.text = if (data[position].scheduleType == "ACTUAL SCHEDULE") {
                "${formatTanggal(data[position].date)} Area Penugasan"
            } else {
                "${formatTanggal(data[position].date)} Lembur Ganti"
            }

            val isClosed = data[position].closingStatus.equals("Closed", ignoreCase = true)
            btnClosing.visibility = if (isClosed && data[position].fileGenerated && data[position].emailSent) View.GONE else View.VISIBLE
            btnFinishClosing.visibility = if (isClosed && data[position].fileGenerated && data[position].emailSent) View.VISIBLE else View.GONE

            val scheduleType = data[position].scheduleType == "ACTUAL SCHEDULE"

            btnFinishClosing.setOnClickListener {
                listener.onClickHistory()
                listener.isActualSchedule(scheduleType, data[position])
            }

            btnClosing.setOnClickListener {
                listener.onClickDetail(data[position])
                listener.isActualSchedule(scheduleType, data[position])
            }
            val text = "${data[position].targetsDone}/${data[position].totalTarget}"
            val separatorIndex = text.indexOf("/")
            val spannableString = SpannableString(text)

            val greenColor = ForegroundColorSpan(Color.parseColor("#00C49A"))
            spannableString.setSpan(greenColor, 0, separatorIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            val blackColor = ForegroundColorSpan(Color.BLACK)
            spannableString.setSpan(blackColor, separatorIndex, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tvFinish.text = spannableString
            if(data[position].percentTargetsDone != 0.00){
                val rounded = "${String.format("%.2f", data[position].percentTargetsDone)}%"
                tvFinishPercent.text = rounded
            }else{
                tvFinishPercent.text = "0.00%"
            }
        }
    }

    private fun formatTanggal(tanggal: String): String {
        val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val date = inputFormat.parse(tanggal)
        return outputFormat.format(date)
    }


    interface OnClickDailyTarget{
        fun onClickDetail(data : DailyTargetManagement)

        fun isActualSchedule(isActualSchedule : Boolean,data : DailyTargetManagement)


        fun onClickHistory()
    }
}