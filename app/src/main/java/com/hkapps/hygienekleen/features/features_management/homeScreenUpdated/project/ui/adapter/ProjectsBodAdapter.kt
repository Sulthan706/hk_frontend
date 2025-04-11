package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListProjectNewBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectBod.Content

class ProjectsBodAdapter(
    var listProject: ArrayList<Content>
): RecyclerView.Adapter<ProjectsBodAdapter.ViewHolder>() {

    private lateinit var projectsBodCallback: ProjectsBodCallback

    inner class ViewHolder(val binding: ListProjectNewBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getProject = listProject[adapterPosition]
            projectsBodCallback.onClickProjectBod(getProject.projectCode)
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

    fun setListener(projectsBodCallback: ProjectsBodCallback) {
        this.projectsBodCallback = projectsBodCallback
    }

    interface ProjectsBodCallback {
        fun onClickProjectBod(projectCode: String)
    }
}