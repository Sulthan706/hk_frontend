package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListProjectBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement.Content

class SearchProjectsManagementAdapter(
    var searchProject: ArrayList<Content>
): RecyclerView.Adapter<SearchProjectsManagementAdapter.ViewHolder>() {

    private lateinit var projectsManagementCallback: SearchProjectsManagementCallback

    inner class ViewHolder(val binding: ListProjectBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getProject = searchProject[adapterPosition]
            projectsManagementCallback.onClickSearchProject(getProject.projectCode)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return searchProject.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = searchProject[position]

        holder.binding.tvNameListProject.text = response.projectName
        holder.binding.tvBranchCodeListProject.text = response.branchCode
        holder.binding.tvProjectCodeListProject.text = response.projectCode
    }

    fun setListener(projectsManagementCallback: SearchProjectsManagementCallback) {
        this.projectsManagementCallback = projectsManagementCallback
    }

    interface SearchProjectsManagementCallback {
        fun onClickSearchProject(projectCode: String)
    }
}