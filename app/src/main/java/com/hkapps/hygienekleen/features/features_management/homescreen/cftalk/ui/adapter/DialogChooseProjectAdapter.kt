package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listAllProject.Content

class DialogChooseProjectAdapter(
    private var context: Context,
    var listAllProject: ArrayList<Content>
): RecyclerView.Adapter<DialogChooseProjectAdapter.ViewHolder>() {

    private lateinit var projectsAllCallBack: ProjectsAllCallBack
    var selectedItem = -1

    inner class ViewHolder(val binding: ListProjectBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val project = listAllProject[adapterPosition]
            projectsAllCallBack.onClickProject(project.projectCode, project.projectName)
            selectedItem = adapterPosition
            notifyDataSetChanged()
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

        if (selectedItem == position) {
            holder.itemView.setBackgroundResource(R.drawable.bg_empty_card)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_white_card)
        }
    }

    override fun getItemCount(): Int {
        return listAllProject.size
    }

    fun setListener(projectsAllCallBack: ProjectsAllCallBack) {
        this.projectsAllCallBack = projectsAllCallBack
    }

    interface ProjectsAllCallBack {
        fun onClickProject(projectCode: String, projectName: String)
    }
}