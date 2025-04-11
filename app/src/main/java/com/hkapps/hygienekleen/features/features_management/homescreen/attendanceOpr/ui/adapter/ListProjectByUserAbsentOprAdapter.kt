package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByUser.Project

class ListProjectByUserAbsentOprAdapter (
    private val context: Context,
    var listProjectByUser: ArrayList<Project>
    ): RecyclerView.Adapter<ListProjectByUserAbsentOprAdapter.ViewHolder>() {

    private lateinit var listProjectByUserCallBack: ListProjectByUserCallBack

    inner class ViewHolder(val binding: ListProjectBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getProject = listProjectByUser[adapterPosition]
            listProjectByUserCallBack.onClickProjectByUser(getProject.projectCode, getProject.projectName)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listProjectByUser[position]

        holder.binding.tvNameListProject.text = response.projectName
        holder.binding.tvBranchCodeListProject.text = response.branchCode
        holder.binding.tvProjectCodeListProject.text = response.projectCode
    }

    override fun getItemCount(): Int {
        return listProjectByUser.size
    }

    fun setListener(listProjectByUserCallBack: ListProjectByUserCallBack) {
        this.listProjectByUserCallBack = listProjectByUserCallBack
    }

    interface ListProjectByUserCallBack{
        fun onClickProjectByUser(projectId: String, projectName: String)
    }
}