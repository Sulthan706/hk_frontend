package com.hkapps.hygienekleen.features.features_client.report.ui.old.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListShiftReportBinding
import com.hkapps.hygienekleen.features.features_client.report.model.listShift.Data

class ListShiftReportClientAdapter(
    private val context: Context,
    var listShift: ArrayList<Data>
): RecyclerView.Adapter<ListShiftReportClientAdapter.ViewHolder>() {

    private lateinit var listShiftReportCallBack: ListShiftReportCallBack

    inner class ViewHolder(val binding: ListShiftReportBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getShift = listShift[adapterPosition]
            listShiftReportCallBack.onClickShift(getShift.shiftId, getShift.shiftName, getShift.shiftDescription)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListShiftReportBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listShift[position]

        holder.binding.tvShiftListShiftReport.text = when(response.shiftName) {
            "WD" -> "Week Days"
            else -> response.shiftDescription
        }
        holder.binding.tvTimeListShiftReport.text = "${response.startAt} - ${response.endAt}"
    }

    override fun getItemCount(): Int {
        return listShift.size
    }

    fun setListener(listShifReportCallBack: ListShiftReportCallBack) {
        this.listShiftReportCallBack = listShifReportCallBack
    }

    interface ListShiftReportCallBack {
        fun onClickShift(shiftId: Int, shiftName: String, shiftDesc: String)
    }
}