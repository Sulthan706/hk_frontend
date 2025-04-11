package com.hkapps.hygienekleen.features.features_management.damagereport.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemBotSheetListprojectBinding
import com.hkapps.hygienekleen.features.features_management.damagereport.model.listprojectdamagereport.ContentListProjectBak
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.fragment.BotSheetProjectBakFragment

class ListProjectBakAdapter(val listProjectBak : ArrayList<ContentListProjectBak>):
RecyclerView.Adapter<ListProjectBakAdapter.ViewHolder>() {
    var selectedItem = -1
    private lateinit var botSheetClickProject: BotSheetClickProjectBak
    inner class ViewHolder(val binding: ItemBotSheetListprojectBinding):
            RecyclerView.ViewHolder(binding.root), View.OnClickListener{

                init {
                    itemView.setOnClickListener(this)
                }
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(v: View?) {
            val clickedPosition = adapterPosition
            if (clickedPosition != RecyclerView.NO_POSITION) {
                val clickProject = listProjectBak[clickedPosition]
                botSheetClickProject.onClickProject(
                    clickProject.projectName,
                    clickProject.projectCode
                )
                selectedItem = adapterPosition
                notifyDataSetChanged()
            }
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
        val item = listProjectBak[position]
        holder.binding.tvNameProjectBotSheet.text = item.projectName
        // Set visibility of check mark based on selected item
        if (position == selectedItem) {
            holder.binding.ivCheckProject.visibility = View.VISIBLE
        } else {
            holder.binding.ivCheckProject.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
       return listProjectBak.size
    }

    fun setListener(botSheetProjectProjectBakFragment: BotSheetProjectBakFragment){
        this.botSheetClickProject = botSheetProjectProjectBakFragment
    }

    interface BotSheetClickProjectBak {
        fun onClickProject(projectName: String, projectCode: String)
    }


}


