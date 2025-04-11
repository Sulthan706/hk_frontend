package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listAllProject.Content

class ProjectsAllCftalkAdapter(
    private var context: Context,
    var listAllProject: ArrayList<Content>
): RecyclerView.Adapter<ProjectsAllCftalkAdapter.ViewHolder>() {

    private lateinit var projectsAllCallBack: ProjectsAllCallBack

    inner class ViewHolder(val binding: ListProjectBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val project = listAllProject[adapterPosition]
            projectsAllCallBack.onClickProject(project.projectName, project.projectCode)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listAllProject[position]

        holder.binding.tvNameListProject.text = response.projectName
        holder.binding.tvBranchCodeListProject.text = response.branchCode
        holder.binding.tvProjectCodeListProject.text = response.projectCode
    }

    override fun getItemCount(): Int {
        return listAllProject.size
    }

    fun setListener(projectsAllCallBack: ProjectsAllCallBack) {
        this.projectsAllCallBack = projectsAllCallBack
    }

    interface ProjectsAllCallBack {
        fun onClickProject(projectName: String, projectCode: String)
    }
}