package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemMonthlyWorkreportCardBinding
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.listbystatsrkbclient.ListByStatsRkbClientContent
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.activity.ListByStatusRkbClientActivity

class ListByStatsRkbClientAdapter(var listByStatsRkbClient: ArrayList<ListByStatsRkbClientContent>) :
    RecyclerView.Adapter<ListByStatsRkbClientAdapter.ViewHolder>() {

    private lateinit var listClickRkbClient: ListByStatusRkbClientActivity
    inner class ViewHolder(val binding: ItemMonthlyWorkreportCardBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val itemClickRkbClient = listByStatsRkbClient[adapterPosition]
            listClickRkbClient.onClickRkb(
                itemClickRkbClient.idJob
            )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMonthlyWorkreportCardBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listByStatsRkbClient[position]
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
        if (item.approved){
            holder.binding.ivStampRkb.apply {
                setImageResource(R.drawable.ic_stamp)
                visibility = View.VISIBLE
            }
        }
        if (!item.approved && item.done){
            holder.binding.ivStampRkb.apply {
                setImageResource(R.drawable.ic_stamp_disable)
                visibility = View.VISIBLE
            }
        }

        if (item.done){
            holder.binding.ivChecklistRkb.apply {
                setImageResource(R.drawable.ic_checklist_radial)
                visibility = View.VISIBLE
            }
        }
        if (item.diverted){
            holder.binding.ivBaRkb.apply {
                setImageResource(R.drawable.ic_ba)
                visibility = View.VISIBLE
            }
        }
        if (item.statusPeriodic == "NOT_REALITATION"){
            holder.binding.ivRealizationRkb.apply {
                setImageResource(R.drawable.ic_realization_rkb)
                visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return listByStatsRkbClient.size
    }

    fun setListener(listByStatusRkbClientActivity: ListByStatusRkbClientActivity){
        this.listClickRkbClient = listByStatusRkbClientActivity
    }

    interface ClickRkbClient {
        fun onClickRkb(idJob: Int)
    }

}