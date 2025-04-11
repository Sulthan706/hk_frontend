package com.hkapps.academy.features.features_trainer.myclass.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ItemListSelectedBinding

class PositionsInviteClassAdapter(
    var listPosition: ArrayList<String>
): RecyclerView.Adapter<PositionsInviteClassAdapter.ViewHolder>() {

    private lateinit var listPositionCallBack: ListPositionCallBack
    var selectedItem = -1

    inner class ViewHolder (val binding: ItemListSelectedBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(v: View?) {
            val getPosition = listPosition[adapterPosition]
            listPositionCallBack.onClickPosition(getPosition)
            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listPosition.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listPosition[position]

        holder.binding.tvItemListSelected.text = response

        if (selectedItem == position) {
            holder.binding.itemListSelected.setBackgroundResource(R.color.blue5)
            holder.binding.tvItemListSelected.setTextColor(Color.WHITE)
        } else {
            holder.binding.itemListSelected.setBackgroundResource(R.color.white)
            holder.binding.tvItemListSelected.setTextColor(Color.parseColor("#363636"))
        }
    }

    fun setListener(listPositionCallBack: ListPositionCallBack) {
        this.listPositionCallBack = listPositionCallBack
    }

    interface ListPositionCallBack {
        fun onClickPosition(position: String)
    }

}