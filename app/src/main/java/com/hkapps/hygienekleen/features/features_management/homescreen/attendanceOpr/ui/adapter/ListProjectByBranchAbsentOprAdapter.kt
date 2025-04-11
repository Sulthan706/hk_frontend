package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByBranch.Content

class ListProjectByBranchAbsentOprAdapter(
    private var context: Context,
    var listProjectByBranch: ArrayList<Content>
): RecyclerView.Adapter<ListProjectByBranchAbsentOprAdapter.ViewHolder>() {

    private lateinit var listProjectByBranchCallBack: ListProjectByBranchCallBack

    inner class ViewHolder(val binding: ListProjectBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getProject = listProjectByBranch[adapterPosition]
            listProjectByBranchCallBack.onClickProjectByBranch(getProject.projectCode, getProject.projectName)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listProjectByBranch[position]

        holder.binding.tvNameListProject.text = response.projectName
        holder.binding.tvBranchCodeListProject.text = response.branchCode
        holder.binding.tvProjectCodeListProject.text = response.projectCode
    }

    override fun getItemCount(): Int {
        return listProjectByBranch.size
    }

    fun setListener(listProjectByBranchCallBack: ListProjectByBranchCallBack) {
        this.listProjectByBranchCallBack = listProjectByBranchCallBack
    }

    interface ListProjectByBranchCallBack {
        fun onClickProjectByBranch(projectId: String, projectName: String)
    }
}