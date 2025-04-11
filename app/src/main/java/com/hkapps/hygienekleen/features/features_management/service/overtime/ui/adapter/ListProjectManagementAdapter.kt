package com.hkapps.hygienekleen.features.features_management.service.overtime.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemListProjectManagementBinding
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.searchProjectManagement.ContentSearchProjectMgmnt
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.fragment.BotSheetListProjectFragment

class ListProjectManagementAdapter(var listProjectMgmnt: ArrayList<ContentSearchProjectMgmnt>):
RecyclerView.Adapter<ListProjectManagementAdapter.ViewHolder>(){

    private lateinit var botClickProject: BotSheetListProjectFragment
    var selectedItem = -1
    inner class ViewHolder(val binding:ItemListProjectManagementBinding):
    RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val itemProject = listProjectMgmnt[adapterPosition]
            botClickProject.onClickProject(
                itemProject.projectCode,
                itemProject.projectName
            )

            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListProjectManagementBinding.inflate(LayoutInflater.from(
                parent.context
            ), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listProjectMgmnt[position]
        holder.binding.tvNameProjectBotSheet.text = item.projectName
//        if (selectedItem == position){
//            holder.binding.ivCheckProject.visibility = View.VISIBLE
//        } else {
//            holder.binding.ivCheckProject.visibility = View.GONE
//        }
    }

    override fun getItemCount(): Int {
        return listProjectMgmnt.size
    }
    fun setListener(botSheetListProjectFragment: BotSheetListProjectFragment){
        this.botClickProject = botSheetListProjectFragment
    }
    interface BotClickListProject {
        fun onClickProject(projectCode: String, projectName: String)

    }


}