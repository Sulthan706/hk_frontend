package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemChooserBinding

class JobPositionChooserAdapter(
    var listChooser: ArrayList<String>,
    var selectedItem: Int = -1
): RecyclerView.Adapter<JobPositionChooserAdapter.ViewHolder>() {

    private lateinit var onJobPositionCallBack: OnJobPositionCallBack

    inner class ViewHolder(val binding: ListItemChooserBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val response = listChooser[adapterPosition]
            onJobPositionCallBack.onJobPositionSelected(response)

            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemChooserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listChooser[position]

        holder.binding.tvItemChooser.text = response
        holder.binding.ivDefaultChooser.visibility = View.VISIBLE
        holder.binding.ivSelectedChooser.visibility = View.GONE

        if (selectedItem == position) {
            holder.binding.ivDefaultChooser.visibility = View.INVISIBLE
            holder.binding.ivSelectedChooser.visibility = View.VISIBLE
        } else {
            holder.binding.ivDefaultChooser.visibility = View.VISIBLE
            holder.binding.ivSelectedChooser.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return listChooser.size
    }

    fun setListener (onJobPositionCallBack: OnJobPositionCallBack) {
        this.onJobPositionCallBack = onJobPositionCallBack
    }

    interface OnJobPositionCallBack {
        fun onJobPositionSelected(item: String)
    }
}