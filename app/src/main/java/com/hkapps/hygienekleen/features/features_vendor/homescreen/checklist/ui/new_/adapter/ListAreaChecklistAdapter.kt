package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListAreaChecklistBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listArea.Content
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel

class ListAreaChecklistAdapter(
    private val context: Context,
    var listArea: ArrayList<Content>,
    private val viewModel: ChecklistViewModel,
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<ListAreaChecklistAdapter.ViewHolder>() {

    private lateinit var listAreaChecklistCallBack: ListAreaChecklistCallBack

    inner class ViewHolder(val binding: ListAreaChecklistBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getArea = listArea[adapterPosition]
            listAreaChecklistCallBack.onClickArea(getArea.shiftId, getArea.idSubLocationArea)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListAreaChecklistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listArea[position]

        holder.binding.tvAreaListAreaChecklist.text = response.locationName
        holder.binding.tvSubAreaListAreaChecklist.text = response.subLocationName
        // check icon
        if (response.isAreaChecklist != "false") {
            holder.binding.ivListAreaChecklist.visibility = View.VISIBLE
        } else {
            holder.binding.ivListAreaChecklist.visibility = View.GONE
        }

//        viewModel.detailAreaResponseModel.observe(lifecycleOwner) {
//            if (it.code == 200) {
//                if (it.data.operational.isEmpty()) {
//                    holder.binding.clListAreaChecklist.visibility = View.GONE
//                    holder.binding.clListAreaChecklist.layoutParams = ViewGroup.LayoutParams(0, 0)
//                } else {
//                    holder.binding.clListAreaChecklist.visibility = View.VISIBLE
//                }
//            }
//        }
//        viewModel.getDetailArea(response.idProject, response.shiftId, response.idSubLocationArea)
    }

    override fun getItemCount(): Int {
        return listArea.size
    }

    fun setListener(listAreaChecklistCallBack: ListAreaChecklistCallBack) {
        this.listAreaChecklistCallBack = listAreaChecklistCallBack
    }

    interface ListAreaChecklistCallBack {
        fun onClickArea(shiftId: Int, plottingId: Int)
    }
}