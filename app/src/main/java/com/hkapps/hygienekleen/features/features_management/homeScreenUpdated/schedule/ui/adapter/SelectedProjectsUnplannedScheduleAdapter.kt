package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemListCreateScheduleManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.SelectedProjectsSchedule

class SelectedProjectsUnplannedScheduleAdapter(
    val context: Context,
    private val selectedProjects: ArrayList<SelectedProjectsSchedule>
) : RecyclerView.Adapter<SelectedProjectsUnplannedScheduleAdapter.ViewHolder>(){

    private lateinit var selectedProjectsCallBack: SelectedProjectsCallBack
    var clickFrom = ""
    var reason = ""

    inner class ViewHolder (val binding: ItemListCreateScheduleManagementBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListCreateScheduleManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return selectedProjects.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = selectedProjects[position]

        // validate layout
        holder.binding.ivDeleteListCreateScheduleManagement.visibility = View.GONE
        holder.binding.spinnerListCreateScheduleManagement.visibility = View.VISIBLE
        if (position == 0) {
            holder.binding.clDateListCreateScheduleManagement.visibility = View.VISIBLE
            holder.binding.tvDateListCreateScheduleManagement.text = response.dateTxt
            holder.binding.tvVisitsListCreateScheduleManagement.text = "${selectedProjects.size} visits"
        } else {
            holder.binding.clDateListCreateScheduleManagement.visibility = View.GONE
        }

        // set data visit
        holder.binding.tvProjectListCreateScheduleManagement.text = response.projectName
        holder.binding.tvProjectCodeListCreateScheduleManagement.text = response.projectCode

        // spinner reason
        val reasonObject = context.resources.getStringArray(R.array.listActivitiesUnplanned)
        val adapterReason = ArrayAdapter(context, R.layout.spinner_item, reasonObject)
        adapterReason.setDropDownViewResource(R.layout.spinner_item)
        holder.binding.spinnerListCreateScheduleManagement.adapter = adapterReason
        holder.binding.spinnerListCreateScheduleManagement.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                reason = if (position == 0) {
                    ""
                } else {
                    reasonObject[position]
                }
                selectedProjectsCallBack.onClickSelected(response.projectCode, response.projectName, response.date, response.dateTxt, clickFrom, reason)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        // set on click edit
        holder.binding.tvEditListCreateScheduleManagement.setOnClickListener {
            clickFrom = "edit"
            selectedProjectsCallBack.onClickSelected(response.projectCode, response.projectName, response.date, response.dateTxt, clickFrom, reason)
        }

    }

    fun setListener(selectedProjectsCallBack: SelectedProjectsCallBack) {
        this.selectedProjectsCallBack = selectedProjectsCallBack
    }

    interface SelectedProjectsCallBack {
        fun onClickSelected(projectCode: String, projectName: String, date: String, dateTxt: String, clickFrom: String, reason: String)
    }
}