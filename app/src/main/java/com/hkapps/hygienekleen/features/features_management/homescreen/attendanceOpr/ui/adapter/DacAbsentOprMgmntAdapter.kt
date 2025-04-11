package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemAttendanceDailyActAllBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.dacAbsentOpr.DailyActivity

class DacAbsentOprMgmntAdapter(
    private val context: Context,
    private val listDac: ArrayList<DailyActivity>
): RecyclerView.Adapter<DacAbsentOprMgmntAdapter.ViewHolder>() {

    private val UNSELECTED = -1
    private var selectedItem = UNSELECTED

    inner class ViewHolder(val binding: ItemAttendanceDailyActAllBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAttendanceDailyActAllBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listDac[position]) {
                val response = listDac[position]

                holder.binding.tvItemDailyAttendance.text = response.activity
                holder.binding.tvTimeItemDailyAttendance.text = response.startAt
                holder.binding.tvExpObject.text = response.objectId
                holder.binding.tvExpMachine.text = response.machineName
                holder.binding.tvExpAccecories.text = "-"
                holder.binding.tvExpChemical.text = response.chemicalName
                holder.binding.tvExpTools.text = response.toolName

                val position: Int = adapterPosition
                val isSelected = position == selectedItem

                binding.expandableLayout.isSelected = isSelected
                binding.expandableLayout.setExpanded(isSelected, false)

                if (response.statusCheklistActivity == "true") {
                    holder.binding.ivCheckDAC.setImageResource(R.drawable.ic_baseline_check_circle_24)
                } else {
                    holder.binding.ivCheckDAC.setImageResource(R.drawable.ic_baseline_circle)
                }

                holder.binding.layoutItemAttendanceDaily.setOnClickListener {
                    if (holder != null) {
                        binding.expandableLayout.isSelected = false
                        binding.expandableLayout.collapse()
                    }
                    val position = adapterPosition
                    selectedItem = if (position == selectedItem) {
                        UNSELECTED
                    } else {
                        binding.expandableLayout.isSelected = true
                        binding.expandableLayout.expand()
                        position
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listDac.size
    }
}