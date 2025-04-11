package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListShiftReportBinding
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.shiftOvertimeChange.Data
import java.util.ArrayList

class ShiftOvertimeAdapter(
    private var context: Context? = null,
    val listShift: ArrayList<Data>
) : RecyclerView.Adapter<ShiftOvertimeAdapter.ViewHolder>() {

    private lateinit var shiftCallback: ShiftCallback
    var selectedItem = -1

    inner class ViewHolder(val binding: ListShiftReportBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val position = listShift[adapterPosition]
            shiftCallback.onClickShift(position.shift.shiftId, position.shift.shiftDescription, position.shift.shiftName)

            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ListShiftReportBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listShift[position]
        val name = response.shift.shiftDescription

        holder.binding.tvShiftListShiftReport.text = when(response.shift.shiftName) {
            "WD" -> "Week Days"
            else -> name
        }
        holder.binding.tvTimeListShiftReport.text = "${response.startAt} - ${response.endAt}"

        if (selectedItem == position) {
                holder.itemView.setBackgroundResource(R.drawable.bg_field_selected)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_field)
        }
    }

    override fun getItemCount(): Int {
        return listShift.size
    }

    interface ShiftCallback {
        fun onClickShift(shiftId: Int, shiftName: String, shiftWd: String)
    }

    fun setListener(shiftCallback: ShiftCallback) {
        this.shiftCallback = shiftCallback
    }

}