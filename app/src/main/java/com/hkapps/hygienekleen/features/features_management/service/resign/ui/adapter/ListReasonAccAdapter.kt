package com.hkapps.hygienekleen.features.features_management.service.resign.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemSelectResignBinding
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listreasonresign.ContentReasonResign

class ListReasonAccAdapter(private var listAccReason: ArrayList<ContentReasonResign>) :
    RecyclerView.Adapter<ListReasonAccAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private lateinit var reasonResignCallback: ReasonResignCallback


    inner class ViewHolder(val binding: ItemSelectResignBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
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
            val selectedItem = listAccReason[adapterPosition]
            selectedPosition = adapterPosition
            reasonResignCallback.onClickReason(selectedItem.reasonId)
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
        val item = listAccReason[position]
        holder.binding.tvItemReasonResign.text = item.reason
        holder.binding.btnRadioReasonResign.isChecked = (position == selectedPosition)
    }

    override fun getItemCount(): Int {
        return listAccReason.size
    }

    fun setListener(reasonResignCallback: ReasonResignCallback){
        this.reasonResignCallback = reasonResignCallback
    }

    interface ReasonResignCallback {
        fun onClickReason(reasonId: Int)
    }


}