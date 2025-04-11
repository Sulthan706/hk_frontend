package com.hkapps.academy.features.features_trainer.myclass.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ItemListSelectedBinding
import com.hkapps.academy.features.features_trainer.myclass.model.listProject.Data

class ProjectsInviteClassAdapter(
    var listProject: ArrayList<Data>
): RecyclerView.Adapter<ProjectsInviteClassAdapter.ViewHolder>(), Filterable {

    private var filteredList: ArrayList<Data> = listProject
    private lateinit var listProjectCallBack: ListProjectCallBack
    var selectedItem = -1

    inner class ViewHolder (val binding: ItemListSelectedBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getProject = listProject[adapterPosition]
            listProjectCallBack.onClickProject(getProject.projectCode, getProject.projectName)
            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listProject.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listProject[position]

        holder.binding.tvItemListSelected.text = response.projectName

        if (selectedItem == position) {
            holder.binding.itemListSelected.setBackgroundResource(R.color.blue5)
            holder.binding.tvItemListSelected.setTextColor(Color.WHITE)
        } else {
            holder.binding.itemListSelected.setBackgroundResource(R.color.white)
            holder.binding.tvItemListSelected.setTextColor(Color.parseColor("#363636"))
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = mutableListOf<Data>()

                if (!constraint.isNullOrBlank()) {
                    for (item in listProject) {
                        if (item.projectName.contains(constraint, ignoreCase = true)) {
                            filteredResults.add(item)
                        }
                    }
                } else {
                    filteredResults.addAll(listProject)
                }

                val filterResults = FilterResults()
                filterResults.values = filteredResults
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<Data>
                notifyDataSetChanged()
            }

        }
    }

    fun setListener(listProjectCallBack: ListProjectCallBack) {
        this.listProjectCallBack = listProjectCallBack
    }

    interface ListProjectCallBack {
        fun onClickProject(projectCode: String, projectName: String)
    }
}