package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listProjectByUserId.Project

class ProjectsEmployeeIdCftalkAdapter(
    private var context: Context,
    var listProject: ArrayList<Project>
): RecyclerView.Adapter<ProjectsEmployeeIdCftalkAdapter.ViewHolder>() {

    private lateinit var projectsEmployeeCallBack: ProjectsEmployeeCallBack

    inner class ViewHolder(val binding: ListProjectBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val project = listProject[adapterPosition]
            projectsEmployeeCallBack.onClickProjectEmployee(project.projectName, project.projectCode)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listProject[position]

        holder.binding.tvNameListProject.text = response.projectName
        holder.binding.tvBranchCodeListProject.text = response.branchCode
        holder.binding.tvProjectCodeListProject.text = response.projectCode
    }

    override fun getItemCount(): Int {
        return listProject.size
    }

    fun setListener(projectsEmployeeCallBack: ProjectsEmployeeCallBack) {
        this.projectsEmployeeCallBack = projectsEmployeeCallBack
    }

    interface ProjectsEmployeeCallBack {
        fun onClickProjectEmployee (projectName: String, projectCode: String)
    }
}