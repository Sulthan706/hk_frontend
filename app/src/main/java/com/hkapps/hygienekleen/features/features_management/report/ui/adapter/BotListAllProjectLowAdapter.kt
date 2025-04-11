package com.hkapps.hygienekleen.features.features_management.report.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemBotSheetListprojectBinding
import com.hkapps.hygienekleen.features.features_management.report.model.listallprojectlow.ContentListProjectLow
import com.hkapps.hygienekleen.features.features_management.report.ui.fragment.BotCertainProjectFragment
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.util.*
import kotlin.collections.ArrayList

class BotListAllProjectLowAdapter(var listAllProjectLow: ArrayList<ContentListProjectLow>) :
    RecyclerView.Adapter<BotListAllProjectLowAdapter.ViewHolder>() {


    var selectedItem = -1
    private lateinit var botClickLowProject: BotClickLowProject

    inner class ViewHolder(val binding: ItemBotSheetListprojectBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val clickProjectLow = listAllProjectLow[adapterPosition]
            botClickLowProject.onClickListProjectLow(
                clickProjectLow.projectCode,
                clickProjectLow.projectName
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
        val item = listAllProjectLow[position]
        holder.binding.tvNameProjectBotSheet.text = item.projectName
        if (selectedItem == position) {
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.CHECKLIST_PROJECT, true)
            holder.binding.ivCheckProject.visibility = View.VISIBLE
        } else {
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.CHECKLIST_PROJECT, true)
            holder.binding.ivCheckProject.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return listAllProjectLow.size
    }

    fun setListenerLow(botCertainProjectFragment: BotCertainProjectFragment){
        this.botClickLowProject = botCertainProjectFragment
    }

    interface BotClickLowProject {
        fun onClickListProjectLow(projectCode: String, projectName: String)
    }
}


