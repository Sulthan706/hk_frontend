package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListChooseOperatorBinding
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.operatorOvertimeChange.Data
import java.util.ArrayList

class OperationalOvertimeAdapter(
    private var context: Context? = null,
    val listOperational: ArrayList<Data>
) : RecyclerView.Adapter<OperationalOvertimeAdapter.ViewHolder>() {

    private lateinit var operationalCallback: OperationalCallback
    var selectedItem = -1

    inner class ViewHolder(val binding: ListChooseOperatorBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val position = listOperational[adapterPosition]
            operationalCallback.onClickOperational(
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
        context = parent.context
        return ViewHolder(ListChooseOperatorBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOperational[position]
        val name = response.employeeName
        val nuc = response.employeeNuc

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

    interface OperationalCallback {
        fun onClickOperational(operatorId: Int, operatorName: String, operatorJobCode: String, idEmployeeProject: Int)
    }

    fun setListener(operationalCallback: OperationalCallback) {
        this.operationalCallback = operationalCallback
    }

}