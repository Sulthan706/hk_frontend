package com.hkapps.hygienekleen.features.features_management.report.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemBotSheetListprojectBinding
import com.hkapps.hygienekleen.features.features_management.report.model.searchproject.ContentBotSheetProject
import com.hkapps.hygienekleen.features.features_management.report.ui.fragment.BotSearchProjectFragment
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class BotSheetProjectAdapter(var listProjectBotSheet: ArrayList<ContentBotSheetProject>) :
    RecyclerView.Adapter<BotSheetProjectAdapter.ViewHolder>() {

    var selectedItem = -1
    private lateinit var botSheetClickProject: BotSheetClickProject
    inner class ViewHolder(val binding: ItemBotSheetListprojectBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val clickProject = listProjectBotSheet[adapterPosition]
            botSheetClickProject.onClickProject(
                clickProject.projectName,
                clickProject.projectCode
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
        val item = listProjectBotSheet[position]
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
        return listProjectBotSheet.size
    }

    fun setListener(botSearchProjectFragment: BotSearchProjectFragment){
        this.botSheetClickProject = botSearchProjectFragment
    }


    interface BotSheetClickProject {
        fun onClickProject(projectName: String, projectCode: String)
    }
}


