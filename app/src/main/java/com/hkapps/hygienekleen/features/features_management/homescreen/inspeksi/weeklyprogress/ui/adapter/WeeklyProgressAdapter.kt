package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListItemWeeklyProgressBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.listweeklyresponse.Content
import java.text.SimpleDateFormat
import java.util.Locale

class WeeklyProgressAdapter(
    private val name : String,
    val data : MutableList<Content>,
    private val listener : OnClickWeeklyProgressAdapter
) : RecyclerView.Adapter<WeeklyProgressAdapter.WeeklyProgressViewHolder>() {

    inner class WeeklyProgressViewHolder(val binding : ListItemWeeklyProgressBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyProgressViewHolder {
        return WeeklyProgressViewHolder(ListItemWeeklyProgressBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: WeeklyProgressViewHolder, position: Int) {
        holder.binding.apply {
            val content = data[position]
            tvLocation.text = content.location
            val created = " ${formatDate(content.createdAt)}"
            tvCreatedAt.text = created
            val name = " ${name.uppercase()}"
            tvCreatedWith.text = name
            if(content.afterImage != null){
                icImageAfterFilled.visibility = View.VISIBLE
                icImageAfterNotFilled.visibility = View.GONE
            }else{
                icImageAfterFilled.visibility = View.GONE
                icImageAfterNotFilled.visibility = View.VISIBLE
            }
            checkStatus(content.status,tvStatus)
            btnDetail.setOnClickListener {
                listener.onDetail(content)
            }
        }
    }

    interface OnClickWeeklyProgressAdapter{
        fun onDetail(data : Content)
    }

    fun updateData(newList: List<Content>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val date = inputFormat.parse(inputDate)
        return date?.let { outputFormat.format(it) } ?: ""
    }

    private fun checkStatus(status : String,textView : TextView){
        when(status){
            "Dikerjakan" -> {
                textView.text = "Dikerjakan"
                textView.setBackgroundResource(R.drawable.ic_bg_status_weekly_report_ongoing)
            }
            "Selesai" -> {
                textView.text = "Selesai"
                textView.setBackgroundResource(R.drawable.ic_bg_status_weekly_report_finish)
            }
            "Disetujui" -> {
                textView.text = "Disetujui"
                textView.setBackgroundResource(R.drawable.ic_bg_status_weekly_report_approved)
            }
        }
    }
}