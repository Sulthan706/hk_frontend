package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemCardDailyTargetBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.DailyTarget
import java.text.SimpleDateFormat
import java.util.Locale

class DailyTargetCardAdapter(
    private val data : List<DailyTarget>,
    private val isChief : Boolean,
    private val listener : OnClickDailyTarget,
) : RecyclerView.Adapter<DailyTargetCardAdapter.DailyTargetCardViewHolder>() {


    inner class DailyTargetCardViewHolder(val binding: ListItemCardDailyTargetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTargetCardViewHolder {
        return DailyTargetCardViewHolder(ListItemCardDailyTargetBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: DailyTargetCardViewHolder, position: Int) {
        holder.binding.apply {

            tvArea.text = if (data[position].scheduleType == "ACTUAL SCHEDULE") {
                "${formatTanggal(data[position].date)} Area Penugasan"
            } else {
                "${formatTanggal(data[position].date)} Lembur Ganti"
            }

            val isClosed = data[position].closingStatus.equals("Closed", ignoreCase = true)
            if(isChief){
                btnClosing.visibility = if (isClosed && data[position].fileGenerated && data[position].emailSent) View.GONE else View.VISIBLE
                btnFinishClosing.visibility = if (isClosed && data[position].fileGenerated && data[position].emailSent) View.VISIBLE else View.GONE
            }else{
                btnClosing.visibility = if (isClosed) View.GONE else View.VISIBLE
                btnFinishClosing.visibility = if (isClosed) View.VISIBLE else View.GONE
            }

            val scheduleType = data[position].scheduleType == "ACTUAL SCHEDULE"

            btnFinishClosing.setOnClickListener {
                listener.onClickHistory()
                listener.isActualSchedule(scheduleType, data[position])
            }

            btnClosing.setOnClickListener {
               if(isChief){
                   listener.onClickDetail(data[position])
                   listener.isActualSchedule(scheduleType, data[position])
               }else{
                   if(data[position].scanOutHasPassed){
                       listener.onClickDetail(data[position])
                       listener.isActualSchedule(scheduleType, data[position])
                   }else{
                       listener.showToast()
                   }
               }
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
        fun onClickDetail(data : DailyTarget)

        fun isActualSchedule(isActualSchedule : Boolean,data : DailyTarget)

        fun showToast()

        fun onClickHistory()
    }
}