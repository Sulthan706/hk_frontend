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

class ReplacePengawasOvertimeAdapter(
    private var context: Context? = null,
    val listOperational: ArrayList<Data>
) : RecyclerView.Adapter<ReplacePengawasOvertimeAdapter.ViewHolder>() {

    private lateinit var replacePengawasCallback: ReplacePengawasCallback
    var selectedItem = -1

    inner class ViewHolder(val binding: ListChooseOperatorBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val position = listOperational[adapterPosition]
            replacePengawasCallback.onClickReplacePengawas(position.idEmployee, position.employeeName, position.employeeNuc)

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

    interface ReplacePengawasCallback {
        fun onClickReplacePengawas(operatorId: Int, operatorName: String, operatorNuc: String)
    }

    fun setListener(replacePengawasCallback: ReplacePengawasCallback) {
        this.replacePengawasCallback = replacePengawasCallback
    }

}