package com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listprojectmanagement.Content
import com.hkapps.hygienekleen.features.features_management.project.viewmodel.ProjectManagementViewModel

class ListProjectByBranchManagementAdapter(
    private var context: Context,
    var listProject: ArrayList<Content>,
    private val userId: Int,
    private val viewModel: ProjectManagementViewModel,
    private val lifescycleOwner: LifecycleOwner
): RecyclerView.Adapter<ListProjectByBranchManagementAdapter.ViewHolder>() {

    private lateinit var projectCallBack: ListProjectByBranchCallBack

    inner class ViewHolder(val binding: ListProjectBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getProject = listProject[adapterPosition]
            projectCallBack.onClickProject(getProject.projectCode)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder((ListProjectBinding.inflate(LayoutInflater.from(context), parent, false)))
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

    fun setListener(listProjectByBranchCallBack : ListProjectByBranchCallBack){
        this.projectCallBack = listProjectByBranchCallBack
    }

    interface ListProjectByBranchCallBack{
        fun onClickProject(projectCode: String)
    }


}