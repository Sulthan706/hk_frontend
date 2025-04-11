package com.hkapps.hygienekleen.features.features_vendor.service.resign.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemResignVendorBinding
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.listresignvendor.DataListResignVendor
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListResignVendorAdapter(private val fragmentManager: FragmentManager, var listResignVendor: ArrayList<DataListResignVendor>):
RecyclerView.Adapter<ListResignVendorAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemResignVendorBinding) :
    RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListResignVendorAdapter.ViewHolder {
        return ViewHolder(ItemResignVendorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListResignVendorAdapter.ViewHolder, position: Int) {
        val item = listResignVendor[position]
        when(item.status){
            "Menunggu Approval" -> {
                holder.binding.tvStatsResign.apply {
                    text = item.status
                    setBackgroundResource(R.drawable.bg_stats_red)
                }
                CarefastOperationPref.saveString(CarefastOperationPrefConst.STATS_RESIGN, item.status)
            }
            "Disetujui" -> {
                holder.binding.tvStatsResign.apply {
                    text = item.status
                    setBackgroundResource(R.drawable.bg_status_history_complaint_green)
                }
//                holder.binding.rlButtonRejectInformationResign.visibility = View.VISIBLE

            }
            "Ditolak" -> {
                holder.binding.tvStatsResign.apply {
                    text = item.status
                    setBackgroundResource(R.drawable.bg_status_history_complaint_disable)
                }
//                holder.binding.rlButtonRejectInformationResign.visibility = View.VISIBLE
            }
            "Escalated to HC" ->{
                holder.binding.tvStatsResign.apply {
                    text = "Menunggu Approval"
                    setBackgroundResource(R.drawable.bg_stats_red)
                }
            }
        }
        holder.binding.tvUserProject.text = item.projectSekarang
        holder.binding.tvCreateResignVendor.text = "Dibuat pada : ${item.createdAtTurnOver}"
        holder.binding.tvUserRequestResignDate.text = item.tanggalPermintaan

//        holder.binding.rlButtonRejectInformationResign.setOnClickListener {
//            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_TURN_OVER, item.idTurnOver)
//            BotSheetInformationFragment().show(fragmentManager, "bottomsheet")
//        }

    }

    override fun getItemCount(): Int {
        return listResignVendor.size
    }
}