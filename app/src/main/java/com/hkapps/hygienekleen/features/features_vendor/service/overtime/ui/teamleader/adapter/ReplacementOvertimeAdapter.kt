package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListChooseOperatorBinding
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.operatorOvertimeChange.Data
import java.util.ArrayList

class ReplacementOvertimeAdapter(
    private var context: Context? = null,
    val listOperational: ArrayList<Data>
) : RecyclerView.Adapter<ReplacementOvertimeAdapter.ViewHolder>() {

    private lateinit var replacementCallback: ReplacementCallback
    var selectedItem = -1

    inner class ViewHolder(val binding: ListChooseOperatorBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val position = listOperational[adapterPosition]
            replacementCallback.onClickReplacement(position.idEmployee, position.employeeName, position.employeeNuc)

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
        Glide.with(holder.binding.ivChooseOps.context).load("http://13.215.81.247/assets.admin_master/images/photo_profile/${response.employeePhotoProfile}").into(holder.binding.ivChooseOps)

        if (selectedItem == position) {
            holder.binding.rlListChooseOperator.setBackgroundResource(R.drawable.bg_field_selected)
        } else {
            holder.binding.rlListChooseOperator.setBackgroundResource(R.drawable.bg_field)
        }
    }

    override fun getItemCount(): Int {
        return listOperational.size
    }

    interface ReplacementCallback {
        fun onClickReplacement(operatorId: Int, operatorName: String, operatorNuc: String)
    }

    fun setListener(replacementCallback: ReplacementCallback) {
        this.replacementCallback = replacementCallback
    }

}