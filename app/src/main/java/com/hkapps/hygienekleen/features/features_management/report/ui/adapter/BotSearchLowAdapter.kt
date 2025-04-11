package com.hkapps.hygienekleen.features.features_management.report.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemBotSheetListprojectBinding
import com.hkapps.hygienekleen.features.features_management.report.model.searchprojectlowlevel.ContentSearchLow
import com.hkapps.hygienekleen.features.features_management.report.ui.fragment.BotSearchProjectFragment
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class BotSearchLowAdapter(var listProjectBotLow: ArrayList<ContentSearchLow>) :
    RecyclerView.Adapter<BotSearchLowAdapter.ViewHolder>() {

    var selectedItem = -1
    private lateinit var botSheetClickLow: BotSheetClickLow



    inner class ViewHolder(val binding: ItemBotSheetListprojectBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val clickSearch = listProjectBotLow[adapterPosition]
            botSheetClickLow.onClickSearchLow(
                clickSearch.projectCode,
                clickSearch.projectName
            )
            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBotSheetListprojectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listProjectBotLow[position]
        if (selectedItem == position){
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.CHECKLIST_PROJECT, true)
            holder.binding.ivCheckProject.visibility = View.VISIBLE
        } else {
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.CHECKLIST_PROJECT, true)
            holder.binding.ivCheckProject.visibility = View.GONE
        }
        holder.binding.tvNameProjectBotSheet.text = item.projectName
    }

    override fun getItemCount(): Int {
        return listProjectBotLow.size
    }

    fun setListenerLow(botSearchProjectFragment: BotSearchProjectFragment){
        this.botSheetClickLow = botSearchProjectFragment
    }

    interface BotSheetClickLow {
        fun onClickSearchLow(projectCode: String, projectName: String)
    }

}


