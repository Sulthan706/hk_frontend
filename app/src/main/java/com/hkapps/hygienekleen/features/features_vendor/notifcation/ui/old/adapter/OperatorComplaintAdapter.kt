package com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.old.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListChooseOperatorBinding
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.listOperator.DataOperator

class OperatorComplaintAdapter(
    private var context: Context? = null,
    var listOperator: ArrayList<DataOperator>
) : RecyclerView.Adapter<OperatorComplaintAdapter.ViewHolder>() {

    private lateinit var operatorCallback: OperatorCallback
    var selectedItem = -1

    inner class ViewHolder(val binding: ListChooseOperatorBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val position = listOperator[adapterPosition]
            operatorCallback.onClickOperator(position.idEmployee, position.employeeName)

            selectedItem = adapterPosition
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ListChooseOperatorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOperator[position]

        holder.binding.tvNameChooseOps.text = response.employeeName

        if (selectedItem == position) {
            holder.itemView.setBackgroundResource(R.drawable.bg_field_selected)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_field)
        }
    }

    override fun getItemCount(): Int {
        return listOperator.size
    }

    fun setListener(operatorCallback: OperatorCallback) {
        this.operatorCallback = operatorCallback
    }

    interface OperatorCallback {
        fun onClickOperator(workerId: Int, workerName: String)
    }
}