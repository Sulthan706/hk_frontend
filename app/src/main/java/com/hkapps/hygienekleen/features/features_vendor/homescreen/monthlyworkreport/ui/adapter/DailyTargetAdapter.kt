package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.adapter


import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemDailyTargetBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.DailyTarget
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class DailyTargetAdapter(
    private val data : List<DailyTarget>,
    private val isChief : Boolean,
    private val listener : OnClickDailyTarget,
) : RecyclerView.Adapter<DailyTargetAdapter.DailyTargetViewHolder>() {


    inner class DailyTargetViewHolder(val binding: ListItemDailyTargetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTargetViewHolder {
        return DailyTargetViewHolder(ListItemDailyTargetBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: DailyTargetViewHolder, position: Int) {
        holder.binding.apply {
            tvArea.text = if (data[position].scheduleType == "ACTUAL SCHEDULE") {
                "Area Penugasan"
            } else {
                "Lembur Ganti"
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
            if(scheduleType){
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_DETAIL_EMPLOYEE_PROJECT_VALUE,data[position].idDetailEmployeeProject)
            }

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
            if (data[position].percentTargetsDone == 0.0) {
                tvFinishPercent.text = "0.00%"
            } else if (data[position].percentTargetsDone == 100.0) {
                tvFinishPercent.text = "100%"
            } else {
                val rounded = "${String.format("%.2f", data[position].percentTargetsDone)}%"
                tvFinishPercent.text = rounded
            }
        }
    }


    interface OnClickDailyTarget{
        fun onClickDetail(data : DailyTarget)

        fun isActualSchedule(isActualSchedule : Boolean,data : DailyTarget)

        fun showToast()

        fun onClickHistory()
    }
}