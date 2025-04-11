package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListDateProjectVisitBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listCountProject.Data

class CountProjectDateAdapter(var listCountProject: ArrayList<Data>):
    RecyclerView.Adapter<CountProjectDateAdapter.ViewHolder>() {

    private lateinit var countProjectCallBack: CountProjectCallBack

    inner class ViewHolder(val binding: ListDateProjectVisitBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val selected = listCountProject[adapterPosition]
            countProjectCallBack.onClickItem(selected.date, selected.totalProject)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListDateProjectVisitBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listCountProject[position]

        holder.binding.tvDateProjectVisit.text = response.date
        holder.binding.tvCountProjectVisit.text = if (response.totalProject == 0 || response.totalProject == null) {
            "Tidak ada"
        } else {
            "${response.totalProject} project"
        }
    }

    override fun getItemCount(): Int {
        return listCountProject.size
    }

    fun setListener(countProjectCallBack: CountProjectCallBack) {
        this.countProjectCallBack = countProjectCallBack
    }

    interface CountProjectCallBack {
        fun onClickItem(date: String, countProject: Int)
    }
}