package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListItemCheckboxBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.SelectedProjectsSchedule
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listProjectSchedule.Content

class SearchProjectsScheduleAdapter (
    private val context: Context,
    var listAllProject: ArrayList<Content>,
    private val listSelected: ArrayList<SelectedProjectsSchedule>
    ): RecyclerView.Adapter<SearchProjectsScheduleAdapter.ViewHolder>() {

    private lateinit var projectAllCallBack: ProjectAllCallBack
    var imageViewTag = ""

    inner class ViewHolder (val binding: ListItemCheckboxBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val project = listAllProject[adapterPosition]

            if (binding.ivListItemCheckbox.tag == null) {
                imageViewTag = "check"
                binding.ivListItemCheckbox.tag = "check"
                binding.ivListItemCheckbox.setImageDrawable(context.resources.getDrawable(R.drawable.ic_checkbox))
            } else {
                if (binding.ivListItemCheckbox.tag == "check") {
                    imageViewTag = "uncheck"
                    binding.ivListItemCheckbox.tag = "uncheck"
                    binding.ivListItemCheckbox.setImageDrawable(context.resources.getDrawable(R.drawable.ic_uncheckbox))
                } else {
                    imageViewTag = "check"
                    binding.ivListItemCheckbox.tag = "check"
                    binding.ivListItemCheckbox.setImageDrawable(context.resources.getDrawable(R.drawable.ic_checkbox))
                }
            }

            projectAllCallBack.onClickProjectAll(project.projectCode, project.projectName, imageViewTag)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listAllProject[position]

        holder.binding.tvListItemCheckbox.text = response.projectName

        // validate when list selected project not empty
        if (listSelected.isNotEmpty()) {
            val findProject = listSelected.any { it.projectCode == response.projectCode && it.projectName == response.projectName}
            if (findProject) {
                holder.binding.ivListItemCheckbox.tag = "check"
                holder.binding.ivListItemCheckbox.setImageDrawable(context.resources.getDrawable(R.drawable.ic_checkbox))
            } else {
                holder.binding.ivListItemCheckbox.tag = null
                holder.binding.ivListItemCheckbox.setImageDrawable(context.resources.getDrawable(R.drawable.ic_uncheckbox))
            }
        }
    }

    override fun getItemCount(): Int {
        return listAllProject.size
    }

    fun setListener(projectAllCallBack: ProjectAllCallBack) {
        this.projectAllCallBack = projectAllCallBack
    }

    interface ProjectAllCallBack {
        fun onClickProjectAll(projectCode: String, projectName: String, imageView: String)
    }
}