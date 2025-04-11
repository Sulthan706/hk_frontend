package com.hkapps.hygienekleen.features.features_management.complaint.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListProjectBinding
import com.hkapps.hygienekleen.features.features_management.complaint.model.listProjectAll.Content
import com.hkapps.hygienekleen.features.features_management.complaint.viewmodel.ComplaintManagementViewModel

class ListProjectAllManagementAdapter(
    private var context: Context,
    var listProject: ArrayList<Content>,
    private val userId: Int,
    private val viewModel: ComplaintManagementViewModel,
    private val lifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<ListProjectAllManagementAdapter.ViewHolder>() {

    private lateinit var projectCallback: ListProjectAllCallback

    inner class ViewHolder(val binding: ListProjectBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getProject = listProject[adapterPosition]
            projectCallback.onClickProjectFull(getProject.projectCode)
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

        // notif complaint
        viewModel.getProjectCountResponse().observe(lifecycleOwner) {
            if (it.code == 200) {
                if (response.projectCode == it.data.projectId) {
                    if (it.data.totalComplaintStatusWaiting != 0) {
                        holder.binding.tvNotifListProject.visibility = View.VISIBLE
                        holder.binding.tvNotifListProject.text = it.data.totalComplaintStatusWaiting.toString()
                    } else {
                        holder.binding.tvNotifListProject.visibility = View.GONE
                    }
                }
            }
        }
        viewModel.getCountProject(userId, response.projectCode)
    }

    override fun getItemCount(): Int {
        return listProject.size
    }

    fun setListener(listProjectAllCallback: ListProjectAllCallback) {
        this.projectCallback = listProjectAllCallback
    }

    interface ListProjectAllCallback {
        fun onClickProjectFull(projectCode: String)
    }
}