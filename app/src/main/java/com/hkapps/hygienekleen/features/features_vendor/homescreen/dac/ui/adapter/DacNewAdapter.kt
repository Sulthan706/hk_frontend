package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemAttendanceDailyActAllBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.DailyActDataArrayResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.DailyActPlottingResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.viewmodel.DacViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils


class DacNewAdapter(
    private var dailyActDataArrayResponseModel: ArrayList<DailyActDataArrayResponseModel>,
    private var dacResponse: DailyActPlottingResponseModel,
    private var context: Context? = null,
    private var viewModel: DacViewModel,
    private var lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<DacNewAdapter.ViewHolder>() {
    private val UNSELECTED = -1
    private var selectedItem = UNSELECTED
    private var loadingDialog: Dialog? = null


    private var selectedIteme = 9999
    private var selectedItems = 0
    private var lastSelecteds = 0
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val idDetailEmployeeProject =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_DETAIL_PROJECT, 0)
    private val idPlotting =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_PLOTTING, 0)
    private val idShift = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_SHIFT, 0)
    private val idLocation =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_LOCATION, 0)
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    var rowIndx = -1

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

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
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

                binding.expandableLayout.isSelected = isSelected
                binding.expandableLayout.setExpanded(isSelected, false)

                when(response.statusCheklistActivity) {
                    "true" -> {
                        holder.binding.ivCheckDAC.setImageResource(R.drawable.ic_baseline_check_circle_24)
                        holder.binding.ivCheckDAC.tag = "ya"

                        holder.binding.ivCheckDAC.setOnClickListener {
                            if (holder.binding.ivCheckDAC.tag == "ya") {
                                Toast.makeText(
                                    context,
                                    "anda sudah melakukan checklist pada activity ini.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            holder.binding.ivCheckDAC.setImageResource(R.drawable.ic_baseline_check_circle_24)
                            holder.binding.ivCheckDAC.tag = "ya"
                        }
                    }
                    "false" -> {
//                        holder.binding.ivCheckDAC.setImageResource(R.drawable.ic_baseline_circle)
//                        holder.binding.ivCheckDAC.tag = "no"

                        var flag = 1
                        holder.binding.ivCheckDAC.setOnClickListener {
                            if (flag == 1) {
                                holder.binding.ivCheckDAC.isClickable = false
                                showLoading(context!!.getString(R.string.loading_string2), response.idSubLocationActivity)
                            }
                            flag = 0
                            holder.binding.ivCheckDAC.setImageResource(R.drawable.ic_baseline_check_circle_24)
                            holder.binding.ivCheckDAC.tag = "ya"
                        }
                    }
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

                viewModel.checkResponseModel.observe(lifecycleOwner) {
                    if (it.code == 200) {
                        hideLoading()
                        holder.binding.ivCheckDAC.isClickable = true
                        Toast.makeText(
                            context,
                            "Berhasil check DAC",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        hideLoading()
                        holder.binding.ivCheckDAC.isClickable = true
                        Toast.makeText(
                            context,
                            "Gagal check DAC",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    //item not change when scroll
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //item not change when scroll
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return dailyActDataArrayResponseModel.size
    }

    inner class ViewHolder(val binding: ItemAttendanceDailyActAllBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun showLoading(loadingText: String, idSubLocationActivity: Int) {
        loadingDialog = CommonUtils.showLoadingDialog(context!!, loadingText)
        viewModel.postDACCheckLowLevel(
            employeeId,
            idDetailEmployeeProject,
            projectCode,
            idPlotting,
            idShift,
            idLocation,
            dacResponse.subLocationId,
            idSubLocationActivity
        )
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
}