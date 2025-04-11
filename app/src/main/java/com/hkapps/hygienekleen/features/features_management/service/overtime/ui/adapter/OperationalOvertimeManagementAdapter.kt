package com.hkapps.hygienekleen.features.features_management.service.overtime.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListChooseOperatorBinding
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.selectEmployee.Data

class OperationalOvertimeManagementAdapter(
    val listOperational: ArrayList<Data>
) : RecyclerView.Adapter<OperationalOvertimeManagementAdapter.ViewHolder>() {

    private lateinit var operationalOvertimeCallback: OperationalOvertimeCallback
    var selectedItem = -1

    inner class ViewHolder(val binding: ListChooseOperatorBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val position = listOperational[adapterPosition]
            operationalOvertimeCallback.onClickOperational(
                position.idEmployee,
                position.employeeName,
                position.employeeJobCode,
                position.idEmployeeProject
            )

            selectedItem = adapterPosition
            notifyDataSetChanged()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListChooseOperatorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOperational[position]
        val name = response.employeeName

        holder.binding.tvNameChooseOps.text = name

        if (selectedItem == position) {
            holder.binding.rlListChooseOperator.setBackgroundResource(R.drawable.bg_field_selected)
        } else {
            holder.binding.rlListChooseOperator.setBackgroundResource(R.drawable.bg_field)
        }
    }

    override fun getItemCount(): Int {
        return listOperational.size
    }

    fun setListener(operationalOvertimeCallback: OperationalOvertimeCallback) {
        this.operationalOvertimeCallback = operationalOvertimeCallback
    }

    interface OperationalOvertimeCallback {
        fun onClickOperational(operationalId: Int, operationalName: String, operationalJobCode: String, idEmployeeProject: Int)
    }
}