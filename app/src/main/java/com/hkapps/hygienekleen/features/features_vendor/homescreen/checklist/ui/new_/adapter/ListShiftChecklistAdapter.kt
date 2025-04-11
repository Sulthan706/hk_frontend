package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListShiftChecklistBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listShift.Data

class ListShiftChecklistAdapter(
    private val context: Context,
    var listShift: ArrayList<Data>
): RecyclerView.Adapter<ListShiftChecklistAdapter.ViewHolder>() {

    private lateinit var listShiftChecklistCallBack: ListShiftChecklistCallBack

    inner class ViewHolder(val binding: ListShiftChecklistBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getShift = listShift[adapterPosition]
            listShiftChecklistCallBack.onClickShift(getShift.shiftId, getShift.shiftName, getShift.shiftDescription)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListShiftChecklistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listShift[position]

        holder.binding.tvShiftListShiftChecklist.text = when(response.shiftName) {
            "WD" -> "Week Days"
            else -> response.shiftDescription
        }
        holder.binding.tvTimeListShiftChecklist.text = "${response.startAt} - ${response.endAt}"
        holder.binding.tvCountAreaListShiftChecklist.text = "${response.totalAreaChecklist}/${response.totalArea} Area"
        holder.binding.tvCountOpsListShiftChecklist.text = "${response.totalOperationalChecklist}/${response.totalOperational} Operational"
    }

    override fun getItemCount(): Int {
        return listShift.size
    }

    fun setListener(listShiftChecklistCallBack: ListShiftChecklistCallBack) {
        this.listShiftChecklistCallBack = listShiftChecklistCallBack
    }

    interface ListShiftChecklistCallBack {
        fun onClickShift(shiftId: Int, shiftName: String, shiftDesc: String)
    }
}