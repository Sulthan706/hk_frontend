package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListProjectNewBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement.Content

class ProjectsManagementAdapter(
    var listProject: ArrayList<Content>
): RecyclerView.Adapter<ProjectsManagementAdapter.ViewHolder>() {

    private lateinit var projectsManagementCallback: ProjectsManagementCallback

    inner class ViewHolder(val binding: ListProjectNewBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getProject = listProject[adapterPosition]
            projectsManagementCallback.onClickProjectManagement(getProject.projectCode)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListProjectNewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listProject.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listProject[position]

        holder.binding.tvBranchListProjectNew.text = response.branchName
        holder.binding.tvProjectListProjectNew.text = response.projectName
        holder.binding.tvDateListProjectNew.text = response.endDateProject
        if (response.statusProject == "NearExpired") {
            holder.binding.tvNearExpiryListProjectNew.visibility = View.VISIBLE
        } else {
            holder.binding.tvNearExpiryListProjectNew.visibility = View.GONE
        }
    }

    fun setListener(projectsManagementCallback: ProjectsManagementCallback) {
        this.projectsManagementCallback = projectsManagementCallback
    }

    interface ProjectsManagementCallback {
        fun onClickProjectManagement(projectCode: String)
    }
}