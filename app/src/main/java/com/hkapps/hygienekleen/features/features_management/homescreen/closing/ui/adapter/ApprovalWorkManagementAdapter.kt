package com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListItemApprovalWorkBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicbystatusmanagement.ContentListByStatsPeriodic

class ApprovalWorkManagementAdapter(
    val data : MutableList<ContentListByStatsPeriodic>,
    private val listener : OnClickApprovalWork
) : RecyclerView.Adapter<ApprovalWorkManagementAdapter.ApprovalWorkManagementViewHolder>() {

    inner class ApprovalWorkManagementViewHolder(val binding: ListItemApprovalWorkBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApprovalWorkManagementViewHolder {
        return ApprovalWorkManagementViewHolder(
            ListItemApprovalWorkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ApprovalWorkManagementViewHolder, position: Int) {
        holder.binding.apply {
            tvWeekly.text = data[position].typeJob
            tvTypeWorkRkb.text = data[position].detailJob
            tvAreaItemRkb.text = data[position].locationName
            tvSubAreaItemRkb.text = data[position].subLocationName
            tvShift.text = data[position].shift

            if (data[position].typeJob == "DAILY") {
                tvWeekly.setBackgroundResource(R.drawable.bg_rounded_skyblue)
            } else if (data[position].typeJob == "WEEKLY") {
                tvWeekly.setBackgroundResource(R.drawable.bg_rounded_orange)
            } else {
                tvWeekly.setBackgroundResource(R.drawable.bg_rounded_purple)
            }

            if(data[position].approved){
                imgNext.setImageDrawable(
                    ContextCompat.getDrawable(
                        imgNext.context,
                        R.drawable.ic_stamp
                    )
                )
                icImageAfterFilled.visibility = View.VISIBLE
                icImageAfterNotFilled.visibility = View.GONE
            }

            if (data[position].diverted) {
                cardDetailDiversion.visibility = View.VISIBLE

                val divertedTo = ": ${data[position].divertedTo ?: "-"}"
                val shiftDiversion = ": ${data[position].baShift ?: "-"}"
                tvDiversionFor.text = divertedTo
                tvShiftDiversion.text = shiftDiversion


            } else {
                cardDetailDiversion.visibility = View.GONE
                icImageAfterFilled.visibility = View.GONE
                icImageAfterNotFilled.visibility = View.VISIBLE
            }

            card.setOnClickListener {
                listener.OnClickDetail(data[position].idJob)
            }
            approve.setOnClickListener {
                listener.onClickApprove(data[position])
            }
        }
    }

    interface OnClickApprovalWork {
        fun OnClickDetail(idJob: Int)

        fun onClickApprove(content: ContentListByStatsPeriodic)
    }

}