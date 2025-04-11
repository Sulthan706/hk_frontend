package com.hkapps.hygienekleen.features.features_management.shareloc.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemBotSheetListprojectBinding
import com.hkapps.hygienekleen.features.features_management.shareloc.model.listsearchgetmanagement.ListSearchManagementContent
import com.hkapps.hygienekleen.features.features_management.shareloc.ui.fragment.BotSheetSearchManagementFragment
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListSearchAllManagamentAdapter(var listSearchManagement: ArrayList<ListSearchManagementContent>):
RecyclerView.Adapter<ListSearchAllManagamentAdapter.ViewHolder>(){

    var selectedItem = -1
    private lateinit var botClickManagement: BotClickManagement
    inner class ViewHolder(val binding: ItemBotSheetListprojectBinding):
            RecyclerView.ViewHolder(binding.root), View.OnClickListener{
                init {
                    itemView.setOnClickListener(this)
                }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val clickManagements = listSearchManagement[adapterPosition]
            botClickManagement.onClickManagements(
                clickManagements.adminMasterId,
                clickManagements.adminMasterName
            )
            selectedItem = adapterPosition
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBotSheetListprojectBinding.inflate(LayoutInflater.from(
                parent.context
            ), parent, false)
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listSearchManagement[position]
        holder.binding.tvNameProjectBotSheet.text = item.adminMasterName

        if (selectedItem == position) {
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.CHECKLIST_PROJECT, true)
            holder.binding.ivCheckProject.visibility = View.VISIBLE
        } else {
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.CHECKLIST_PROJECT, true)
            holder.binding.ivCheckProject.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return listSearchManagement.size
    }

    fun setListeners(botSheetSearchManagementFragment: BotSheetSearchManagementFragment){
        this.botClickManagement = botSheetSearchManagementFragment
    }

    interface BotClickManagement {
        fun onClickManagements(adminMasterId: Int, adminMasterName: String)
    }

}


