package com.hkapps.hygienekleen.features.features_management.report.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemCardFoldRecaptotalBinding
import com.hkapps.hygienekleen.features.features_management.report.model.recaptotaldaily.DataCardRecap


class CardRecapAdapter(
    private val context: Context,
    var listCardRecap: ArrayList<DataCardRecap>
) :
    RecyclerView.Adapter<CardRecapAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCardFoldRecaptotalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCardFoldRecaptotalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCardRecap[position]



        holder.binding.text.text = item.date
        holder.binding.profile.tvWaitingCard.text = item.totalWaiting.toString()
        holder.binding.profile.tvOnProgressCountCard.text = item.totalOnProgress.toString()
        holder.binding.profile.tvDoneCountCard.text = item.totalDone.toString()
        holder.binding.profile.tvCloseCountCard.text = item.totalClosed.toString()
        holder.binding.expandableLayout.setOnClickListener{
//            val tvWaiting =
//                it.findViewById<TextView>(com.digimaster.carefastoperation.R.id.tvWaitingCountCard)
//            val tvOnprogress =
//                it.findViewById<TextView>(com.digimaster.carefastoperation.R.id.tvOnProgressCountCard)
//            val tvDone =
//                it.findViewById<TextView>(com.digimaster.carefastoperation.R.id.tvDoneCountCard)
//            val tvClose =
//                it.findViewById<TextView>(com.digimaster.carefastoperation.R.id.tvCloseCountCard)
            if(holder.binding.expandableLayout.isExpanded){
                holder.binding.expandableLayout.collapse()
            }else{
                holder.binding.expandableLayout.expand()
            }

        }


    }

    override fun getItemCount(): Int {
        return listCardRecap.size
    }
}