package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemAttendanceDailyActAllBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActDataArrayResponseModel


class DacAdapter(
    private var dailyActDataArrayResponseModel: ArrayList<DailyActDataArrayResponseModel>,
) :
    RecyclerView.Adapter<DacAdapter.ViewHolder>() {
    private var context: Context? = null
    private val UNSELECTED = -1
    private var selectedItem = UNSELECTED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemAttendanceDailyActAllBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(dailyActDataArrayResponseModel[position]) {
                val response = dailyActDataArrayResponseModel[position]
                holder.binding.tvItemDailyAttendance.text =
                    response.activity
                holder.binding.tvTimeItemDailyAttendance.text = response.startAt

                holder.binding.tvExpObject.text = response.objectId
                holder.binding.tvExpMachine.text = response.machineName
                holder.binding.tvExpAccecories.text = "-"
                holder.binding.tvExpChemical.text = response.chemicalName
                holder.binding.tvExpTools.text = response.toolName

                val position: Int = adapterPosition
                val isSelected = position == selectedItem

//                binding.expandableLayout.isSelected = isSelected
//                binding.expandableLayout.isExpanded = true

                holder.binding.layoutItemAttendanceDaily.setOnClickListener {
//                    android.widget.Toast.makeText(
//                        context,
//                        "" + dailyActDataArrayResponseModel[position].dailyActDataResponseModel.activityName,
//                        android.widget.Toast.LENGTH_SHORT
//                    ).show()

//                    binding.expandableLayout.setInterpolator(OvershootInterpolator())
//                    binding.expandableLayout.setOnExpansionUpdateListener(this)
//                    binding.expandableLayout.setOnClickListener(this)

//                    if (holder != null) {
//                        binding.expandableLayout.isSelected = false
//                        binding.expandableLayout.collapse()
//                    }
//                    val position = adapterPosition
//                    selectedItem = if (position == selectedItem) {
//                        UNSELECTED
//                    } else {
//                        binding.expandableLayout.isSelected = true
//                        binding.expandableLayout.expand()
//                        position
//                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dailyActDataArrayResponseModel.size
    }

    inner class ViewHolder(val binding: ItemAttendanceDailyActAllBinding) :
        RecyclerView.ViewHolder(binding.root)

}