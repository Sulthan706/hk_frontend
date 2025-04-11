package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.schedulesAttendanceManagement.Content
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.customcalendar.setTextColorRes

class SchedulesAttendanceManagementAdapter (
    private var context: Context,
    var listProjectManagement: ArrayList<Content>
    ): RecyclerView.Adapter<SchedulesAttendanceManagementAdapter.ViewHolder>() {

    private lateinit var schdAttnManagementCallBack: SchdAttnManagementCallBack
    var selectedItem = -1

    inner class ViewHolder(val binding: ListProjectBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val project = listProjectManagement[adapterPosition]
            val latitude = if (project.projectLatitude == null || project.projectLatitude == "" || project.projectLatitude == "null") {
                "0.0"
            } else {
                project.projectLatitude
            }
            val longitude = if (project.projectLongitude == null || project.projectLongitude == "" || project.projectLongitude == "null") {
                "0.0"
            } else {
                project.projectLongitude
            }
            val radius = if (project.projectRadius == null || project.projectRadius == 0) {
                0
            } else {
                project.projectRadius
            }

            schdAttnManagementCallBack.onClickSchdAttnManagement(project.projectCode, project.projectName, project.idRkbOperation, latitude, longitude, radius)
            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listProjectManagement[position]

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
        return listProjectManagement.size
    }

    fun setListener(schdAttnManagementCallBack: SchdAttnManagementCallBack) {
        this.schdAttnManagementCallBack = schdAttnManagementCallBack
    }

    interface SchdAttnManagementCallBack {
        fun onClickSchdAttnManagement(projectCode: String, projectName: String, idRkbOperation: Int, latitude: String, longitude: String, radius: Int)
    }
}