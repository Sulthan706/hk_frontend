package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.newoperational

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListTabmenuJobroleBinding

class JobRoleManagementAdapter(
    private val context: Context,
    var listJobRole: ArrayList<String>
): RecyclerView.Adapter<JobRoleManagementAdapter.ViewHolder>(){
    private lateinit var jobRoleEmployeeCallback: JobRoleManagementCallback
    var selectedItem = 0


    inner class ViewHolder(val binding: ListTabmenuJobroleBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(v: View?) {
            selectedItem = adapterPosition
            jobRoleEmployeeCallback.onClickJobManagement(listJobRole[adapterPosition])
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JobRoleManagementAdapter.ViewHolder {
        return ViewHolder(ListTabmenuJobroleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun getItemCount(): Int {
        return listJobRole.size

    }

    override fun onBindViewHolder(holder: JobRoleManagementAdapter.ViewHolder, position: Int) {
        holder.binding.tvTabmenuJobrole.text = listJobRole[position]
        if (selectedItem == position) {
            holder.itemView.setBackgroundResource(R.drawable.bg_tabmenu_jobrole_secondary)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_tabmenu_jobrole)
        }
    }

    fun setListener(jobRoleEmployeeCallback: JobRoleManagementCallback){
        this.jobRoleEmployeeCallback = jobRoleEmployeeCallback
    }

    interface JobRoleManagementCallback {
        fun onClickJobManagement(jobRole: String)
    }
}