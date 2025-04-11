package com.hkapps.hygienekleen.features.features_vendor.damagereport.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListItemBakMesinBinding
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachine

class ListBakMachineAdapter(
    private val context : Context,
    val data : MutableList<BakMachine>,
    val listener : BakMachineOnClick
) : RecyclerView.Adapter<ListBakMachineAdapter.ListBakMachineViewHolder>() {

    inner class ListBakMachineViewHolder(val binding : ListItemBakMesinBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBakMachineViewHolder {
        return ListBakMachineViewHolder((ListItemBakMesinBinding.inflate(LayoutInflater.from(parent.context),parent,false)))
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ListBakMachineViewHolder, position: Int) {
        holder.binding.apply {
            tvStatus.text = if(data[position].machineStatus == "Y") "Baik" else "Rusak"
            if(data[position].machineStatus == "Y"){
                tvStatus.setBackgroundResource(R.drawable.ic_bg_status_weekly_report_finish)
            }else{
                tvStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_red)
            }

            mechineCode.text = data[position].machineCode ?: "-"
            mechineType.text = data[position].machineType ?: "-"
            mechineMerk.text = data[position].machineMerk ?: "-"

            if(data[position].imageFront != null && data[position].imageFront.isNotEmpty()){
                ivFront.visibility = View.VISIBLE
                val url = context.getString(R.string.url) + "assets.admin_master/mesin/${data[position].imageFront}"
                Glide.with(ivFront.context).load(url).into(ivFront)

            }

            if(data[position].imageSide != null && data[position].imageSide.isNotEmpty()){
                ivWide.visibility = View.VISIBLE
                val url = context.getString(R.string.url) + "assets.admin_master/mesin/${data[position].imageSide}"
                Glide.with(ivWide.context).load(url).into(ivWide)
            }

            cardFront.setOnClickListener {
                listener.onDetailImageFront(data[position].imageFront)
            }
            cardWide.setOnClickListener {
                listener.onDetailImageSide(data[position].imageSide)
            }

        }
    }

    interface BakMachineOnClick{

        fun onDetailImageFront(image : String)
        fun onDetailImageSide(image : String)
    }
}