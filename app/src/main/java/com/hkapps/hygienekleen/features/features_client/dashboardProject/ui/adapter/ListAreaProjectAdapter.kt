package com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListAreaDashboardProjectBinding
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.AreaProject

class ListAreaProjectAdapter(var listArea: ArrayList<AreaProject>) :
    RecyclerView.Adapter<ListAreaProjectAdapter.ViewHolder>() {

    private lateinit var listAreaProjectCallBack: ListAreaProjectCallBack

    inner class ViewHolder(val binding: ListAreaDashboardProjectBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getArea = listArea[adapterPosition]
            listAreaProjectCallBack.onClickArea(getArea.locationId, getArea.locationName)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListAreaDashboardProjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listArea[position]
        holder.binding.tvListAreaDashboardProject.text = response.locationName
    }

    override fun getItemCount(): Int {
        return listArea.size
    }

    fun setListener(listAreaProjectCallBack: ListAreaProjectCallBack) {
        this.listAreaProjectCallBack = listAreaProjectCallBack
    }

    interface ListAreaProjectCallBack {
        fun onClickArea(locationId: Int, locationName: String)
    }
}