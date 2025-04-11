package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listProject.ListProjectData

class ListProjectAdapter(
    private val context: Context,
    var listProjectData: ArrayList<ListProjectData>
    ) :
    RecyclerView.Adapter<ListProjectAdapter.ViewHolder>() {

    private lateinit var listProjectCallback: ListProjectCallBack

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_project, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listProjectData[position]

        holder.tvProject.text = response.projectName
    }

    override fun getItemCount(): Int {
        return listProjectData.size
    }


    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvProject: TextView = itemView.findViewById(R.id.tv_nameListProject)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val getProject = listProjectData[position]



            tvProject.background = ContextCompat.getDrawable(context, R.drawable.bg_field_selected)
            listProjectCallback.onClickListProject(getProject.projectName, getProject.projectCode)
        }

    }

    fun setListener (listProjectCallBack: ListProjectCallBack) {
        this.listProjectCallback = listProjectCallBack
    }

    interface ListProjectCallBack {
        fun onClickListProject(projectName: String, projectCode: String)

    }

}
