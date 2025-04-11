package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.listAllProject.Content
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.customcalendar.setTextColorRes

class ProjectsAllAttendanceAdapter (
    var listAllProject: ArrayList<Content>
    ): RecyclerView.Adapter<ProjectsAllAttendanceAdapter.ViewHolder>() {

    private lateinit var projectAllCallBack: ProjectAllCallBack
    var selectedItem = -1

    inner class ViewHolder (val binding: ListProjectBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val project = listAllProject[adapterPosition]

            val latitude = if (project.latitude == null || project.latitude == "" || project.latitude == "null") {
                "0.0"
            } else {
                project.latitude
            }
            val longitude = if (project.longitude == null || project.longitude == "" || project.longitude == "null") {
                "0.0"
            } else {
                project.longitude
            }
            val radius = if (project.radius == null || project.radius == 0) {
                0
            } else {
                project.radius
            }

            projectAllCallBack.onClickProjectAll(project.projectCode, project.projectName, latitude, longitude, radius)
            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listAllProject[position]

        holder.binding.clListProject.setBackgroundResource(R.drawable.bg_field)
        holder.binding.viewListProject.visibility = View.GONE
        holder.binding.tvBranchCodeListProject.visibility = View.GONE

        holder.binding.tvNameListProject.text = response.projectName
        holder.binding.tvProjectCodeListProject.text = response.projectCode

        if (selectedItem == position) {
            holder.binding.clListProject.setBackgroundResource(R.drawable.bg_selected_blue)
            holder.binding.tvNameListProject.setTextColorRes(R.color.blueInfo)
            holder.binding.tvProjectCodeListProject.setTextColorRes(R.color.blueInfo)
        } else {
            holder.binding.clListProject.setBackgroundResource(R.drawable.bg_field)
            holder.binding.tvNameListProject.setTextColorRes(R.color.neutral100)
            holder.binding.tvProjectCodeListProject.setTextColorRes(R.color.grayTxt)
        }

    }

    override fun getItemCount(): Int {
        return listAllProject.size
    }

    fun setListener(projectAllCallBack: ProjectAllCallBack) {
        this.projectAllCallBack = projectAllCallBack
    }

    interface ProjectAllCallBack {
        fun onClickProjectAll(projectCode: String, projectName: String, latitude: String, longitude: String, radius: Int)
    }
}