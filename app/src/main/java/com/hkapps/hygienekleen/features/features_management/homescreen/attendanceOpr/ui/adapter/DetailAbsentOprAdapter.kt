package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListDetailCountAbsentOprBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsent.ListCountAbsentModel

class DetailAbsentOprAdapter(
    private val context: Context,
    var listAbsent: ArrayList<ListCountAbsentModel>
): RecyclerView.Adapter<DetailAbsentOprAdapter.ViewHolder>() {

    private lateinit var detailAbsentCallback: DetailAbsentOprCallBack

    inner class ViewHolder(val binding: ListDetailCountAbsentOprBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

            init {
                itemView.setOnClickListener(this)
            }

        override fun onClick(p0: View?) {
            val response = listAbsent[adapterPosition]
            detailAbsentCallback.onClick(response.countAbsent, response.statusAbsent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListDetailCountAbsentOprBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listAbsent[position]
        holder.binding.tvTotalListDetailAbsentOpr.text = "" + response.countAbsent
        holder.binding.tvStatusListDetailAbsentOpr.text = response.statusAbsent
    }

    override fun getItemCount(): Int {
        return listAbsent.size
    }

    fun setListener(detailAbsentOperationalCallback: DetailAbsentOprCallBack){
        this.detailAbsentCallback = detailAbsentOperationalCallback
    }
    interface DetailAbsentOprCallBack{
        fun onClick(countAbsent: Int, statusAbsent: String)
    }
}