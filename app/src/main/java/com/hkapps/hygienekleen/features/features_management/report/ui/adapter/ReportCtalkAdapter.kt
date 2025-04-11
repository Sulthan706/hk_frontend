package com.hkapps.hygienekleen.features.features_management.report.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemComplaintCftalkBinding
import com.hkapps.hygienekleen.features.features_management.report.model.mainreportctalk.ContentComplaintCtalk

class ReportCtalkAdapter(val listCtalk: ArrayList<ContentComplaintCtalk>) :
    RecyclerView.Adapter<ReportCtalkAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemComplaintCftalkBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val reportClicks = listCtalk[adapterPosition]
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemComplaintCftalkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCtalk[position]
        when (item.complaintStatus) {
            "WAITING" -> {
                holder.binding.rlBgCompliantStatus.setBackgroundResource(R.drawable.bg_stats_red)
            }
            "ON PROGRESS" -> {
                holder.binding.rlBgCompliantStatus.setBackgroundResource(R.drawable.bg_stats_orange)
            }
            "DONE" -> {
                holder.binding.rlBgCompliantStatus.setBackgroundResource(R.drawable.bg_stats_lightgreen)
            }
            "CLOSED" -> {
                holder.binding.rlBgCompliantStatus.setBackgroundResource(R.drawable.bg_stats_darkblue)
            }
        }
        holder.binding.tvLocationProject.text = item.projectName
        holder.binding.tvComplaintTitle.text = item.complaintTitle
        holder.binding.tvComplaintDescription.text = item.complaintDescription
        holder.binding.tvCreatedDate.text = item.createdDate
        holder.binding.tvComplaintStatus.text = item.complaintStatus
        //image
    }

    override fun getItemCount(): Int {
        return listCtalk.size
    }
}