package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.projectCode.Project

class ListProjectManagementAdapter(
    private var context: Context,
    var listProject: ArrayList<Project>
) : RecyclerView.Adapter<ListProjectManagementAdapter.ViewHolder>() {

    private lateinit var listProjectCallback: ListProjectManagementCallback

    inner class ViewHolder(val binding: ListProjectBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val response = listProject[adapterPosition]
            listProjectCallback.onClickProject(response.projectCode)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ListProjectBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listProject[position]

        holder.binding.tvNameListProject.text = response.projectName
        holder.binding.tvBranchCodeListProject.text = response.jabatan
        holder.binding.tvProjectCodeListProject.text = response.projectCode
    }

    override fun getItemCount(): Int {
        return listProject.size
    }

    fun setListener(listOperatorManagementCallback: ListProjectManagementCallback) {
        this.listProjectCallback = listOperatorManagementCallback
    }

    interface ListProjectManagementCallback {
        fun onClickProject(projectId: String)
    }
}