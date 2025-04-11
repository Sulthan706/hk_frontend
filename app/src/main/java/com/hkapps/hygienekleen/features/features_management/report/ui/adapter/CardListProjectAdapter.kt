package com.hkapps.hygienekleen.features.features_management.report.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemListCardProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.DetailAttendanceProjectMgmntActivity
import com.hkapps.hygienekleen.features.features_management.myteam.ui.activity.ListMangementMgmntActivity
import com.hkapps.hygienekleen.features.features_management.report.model.listprojectforbranch.ContentCardProject
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.util.*
import kotlin.collections.ArrayList

class CardListProjectAdapter(var listCardStatsReport : ArrayList<ContentCardProject>):
RecyclerView.Adapter<CardListProjectAdapter.ViewHolder>(){


    inner class ViewHolder(val binding: ItemListCardProjectBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListCardProjectBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCardStatsReport[position]

        val rotateUp = RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateUp.duration = 300
        rotateUp.fillAfter = true

        val rotateDown = RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateDown.duration = 300
        rotateDown.fillAfter = true


        holder.binding.cardView.setOnClickListener {
            if (holder.binding.llContentDescription.visibility == View.VISIBLE) {
                // card is expanded, collapse it
                holder.binding.llContentDescription.visibility = View.GONE
                holder.binding.arrowImageView.startAnimation(rotateDown)
            } else {
                TransitionManager.beginDelayedTransition(holder.binding.cardView)
                // card is collapsed, expand it
                holder.binding.llContentDescription.visibility = View.VISIBLE
                holder.binding.arrowImageView.startAnimation(rotateUp)
            }
        }
        //isi content
        holder.binding.titleTextView.text = item.projectName
        holder.binding.tvPercentageProject.text = "${item.inPercentage}%"
        holder.binding.progressBarCardlist.setProgressPercentage(item.inPercentage)
        holder.binding.tvManpowerCount.text = item.totalManpower.toString()
        holder.binding.tvHolidayCount.text = item.totalLibur.toString()
        holder.binding.tvPresentCount.text = item.totalHadir.toString()
        //get month
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        //get year
        val currentYear = calendar.get(Calendar.YEAR)

        //button
        holder.binding.btnDetailAbsent.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_PROJECT_MANAGEMENT, currentMonth)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_PROJECT_MANAGEMENT, currentYear)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, item.projectCode)
            val intent = Intent(holder.itemView.context, DetailAttendanceProjectMgmntActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
        holder.binding.btnManajemen.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, item.projectCode)
            val intent = Intent(holder.itemView.context, ListMangementMgmntActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return listCardStatsReport.size
    }
}