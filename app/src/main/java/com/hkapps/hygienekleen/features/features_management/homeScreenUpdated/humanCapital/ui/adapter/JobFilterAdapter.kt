package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListTabmenuStatusBinding

class JobFilterAdapter(
    private var listJobPosition: ArrayList<String>
): RecyclerView.Adapter<JobFilterAdapter.ViewHolder>() {

    private lateinit var jobFilterCallback: JobFilterCallback
    var selectedItem = 0

    inner class ViewHolder(val binding: ListTabmenuStatusBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            selectedItem = adapterPosition
            jobFilterCallback.onClickJob(listJobPosition[adapterPosition])
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListTabmenuStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listJobPosition.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvTabmenuJobrole.text = listJobPosition[position]

        if (selectedItem == position) {
            holder.itemView.setBackgroundResource(R.drawable.bg_tabmenu_primarysoft5)
            holder.binding.tvTabmenuJobrole.setTextColor(Color.parseColor("#F47721"))
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_tabmenu_transparent)
            holder.binding.tvTabmenuJobrole.setTextColor(Color.parseColor("#969696"))
        }
    }

    fun setListener(jobFilterCallback: JobFilterCallback) {
        this.jobFilterCallback = jobFilterCallback
    }

    interface JobFilterCallback {
        fun onClickJob(job: String)
    }
}