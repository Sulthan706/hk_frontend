package com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.adapter.midlevel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListChooseOperatorBinding
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.DataOperatorPermission

class ChooseOperatorPermissionAdapter(
    private var context: Context? = null,
    var listOperator: ArrayList<DataOperatorPermission>
) : RecyclerView.Adapter<ChooseOperatorPermissionAdapter.ViewHolder>() {
    var selectedItem = -1
    private lateinit var callBACK: OperatorPermissionCallback

    inner class ViewHolder(val binding: ListChooseOperatorBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val position = listOperator[adapterPosition]
            callBACK.onClickOperator(position.idEmployee, position.employeeName)

            selectedItem = adapterPosition
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ListChooseOperatorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
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

    fun setListener(operatorCallback: OperatorPermissionCallback) {
        this.callBACK = operatorCallback
    }

    interface OperatorPermissionCallback {
        fun onClickOperator(idEmployee: Int, employeeName: String)
    }
}