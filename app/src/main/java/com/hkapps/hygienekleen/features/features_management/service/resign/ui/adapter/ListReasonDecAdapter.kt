package com.hkapps.hygienekleen.features.features_management.service.resign.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemSelectResignBinding
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listreasonresign.ContentReasonResign

class ListReasonDecAdapter(private var listDecReason: ArrayList<ContentReasonResign>):
RecyclerView.Adapter<ListReasonDecAdapter.ViewHolder>(){

    private var selectedPosition = -1
    private lateinit var reasonResignsCallback: ReasonResignsCallback

    inner class ViewHolder(val binding: ItemSelectResignBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
            binding.btnRadioReasonResign.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(v: View?) {
            val selectedItem = listDecReason[adapterPosition]
            selectedPosition = adapterPosition
            reasonResignsCallback.onClickReason(selectedItem.reasonId)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSelectResignBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listDecReason[position]
        holder.binding.tvItemReasonResign.text = item.reason
        holder.binding.btnRadioReasonResign.isChecked = (position == selectedPosition)
    }

    override fun getItemCount(): Int {
        return listDecReason.size
    }

    fun setListener(reasonResignsCallback: ReasonResignsCallback){
        this.reasonResignsCallback = reasonResignsCallback
    }

    interface ReasonResignsCallback {
        fun onClickReason(reasonId: Int)
    }

}


