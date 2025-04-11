package com.hkapps.hygienekleen.features.features_client.report.ui.old.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListAreaChecklistBinding
import com.hkapps.hygienekleen.features.features_client.report.model.listArea.Content

class ListAreaReportClientAdapter(
    private val context: Context,
    var listArea: ArrayList<Content>
): RecyclerView.Adapter<ListAreaReportClientAdapter.ViewHolder>() {

    private lateinit var listAreaCallBack: ListAreaCallBack

    inner class ViewHolder(val binding: ListAreaChecklistBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getArea = listArea[adapterPosition]
            listAreaCallBack.onClickArea(getArea.idSubLocationArea, getArea.shiftId)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListAreaChecklistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listArea[position]

        holder.binding.tvAreaListAreaChecklist.text = response.locationName
        holder.binding.tvSubAreaListAreaChecklist.text = response.subLocationName
        if (response.isAreaChecklist != "false") {
            holder.binding.ivListAreaChecklist.visibility = View.VISIBLE
        } else {
            holder.binding.ivListAreaChecklist.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return listArea.size
    }

    fun setListener(listAreaCallBack: ListAreaCallBack) {
        this.listAreaCallBack = listAreaCallBack
    }

    interface ListAreaCallBack {
        fun onClickArea(plottingId: Int, shiftId: Int)
    }
}