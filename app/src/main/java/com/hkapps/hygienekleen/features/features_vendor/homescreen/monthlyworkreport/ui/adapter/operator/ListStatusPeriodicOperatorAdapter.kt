package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.adapter.operator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemMonthlyWorkreportCardBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbystatus.ContentListStatusRkb
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity.operator.PeriodicOperatorListByStatsActivity

class ListStatusPeriodicOperatorAdapter(val listPeriodicStats: ArrayList<ContentListStatusRkb>):
RecyclerView.Adapter<ListStatusPeriodicOperatorAdapter.ViewHolder>() {

    private lateinit var listClickStatusRkb: PeriodicOperatorListByStatsActivity
    var selectedItem = -1

    inner class ViewHolder (val binding: ItemMonthlyWorkreportCardBinding):
            RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        override fun onClick(p0: View?) {
            val itemListStatus = listPeriodicStats[adapterPosition]
            listClickStatusRkb.onclickListStatus(
                itemListStatus.idJob
            )
        }

        init {
            itemView.setOnClickListener(this)
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMonthlyWorkreportCardBinding.inflate(LayoutInflater.from(
                parent.context
            ), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPeriodicStats[position]
        when(item.typeJob){
            "DAILY" -> {
                holder.binding.tvTypeRkb.setBackgroundResource(R.drawable.bg_rounded_skyblue)
            }
            "WEEKLY" -> {
                holder.binding.tvTypeRkb.setBackgroundResource(R.drawable.bg_rounded_orange)
            }
            "MONTHLY" -> {
                holder.binding.tvTypeRkb.setBackgroundResource(R.drawable.bg_rounded_purple)
            }
        }

        holder.binding.tvTypeWorkRkb.text = item.detailJob
        holder.binding.tvTypeRkb.text = item.typeJob
        holder.binding.tvAreaItemRkb.text = item.locationName
        holder.binding.tvSubAreaItemRkb.text = item.subLocationName
        // validasi icon
        if (item.approved) {
            holder.binding.ivStampRkb.setImageResource(R.drawable.ic_stamp)
            holder.binding.ivStampRkb.visibility = View.VISIBLE
        } else if (!item.approved && item.done) {
            holder.binding.ivStampRkb.setImageResource(R.drawable.ic_stamp_disable)
            holder.binding.ivStampRkb.visibility = View.VISIBLE
        } else {
            holder.binding.ivStampRkb.visibility = View.GONE
        }


        if (item.done){
            holder.binding.ivChecklistRkb.apply {
                setImageResource(R.drawable.ic_checklist_radial)
                visibility = View.VISIBLE
            }
        } else {
            holder.binding.ivChecklistRkb.visibility = View.GONE
        }

        if (item.diverted){
            holder.binding.ivBaRkb.apply {
                setImageResource(R.drawable.ic_ba)
                visibility = View.VISIBLE
            }
        } else {
            holder.binding.ivBaRkb.visibility = View.GONE
        }

        if (item.statusPeriodic == "NOT_REALITATION"){
            holder.binding.ivRealizationRkb.apply {
                setImageResource(R.drawable.ic_realization_rkb)
                visibility = View.VISIBLE
            }
        } else {
            holder.binding.ivRealizationRkb.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return listPeriodicStats.size
    }

    fun setListener(periodicOperatorListByStatsActivity: PeriodicOperatorListByStatsActivity){
        this.listClickStatusRkb = periodicOperatorListByStatsActivity
    }

    interface ListClickStatus {
        fun onclickListStatus(idJob: Int)
    }
}