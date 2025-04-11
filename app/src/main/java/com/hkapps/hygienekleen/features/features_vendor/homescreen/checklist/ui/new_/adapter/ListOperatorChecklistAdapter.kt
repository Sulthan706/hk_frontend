package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListOperationalChecklistBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listOperator.Data
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel

class ListOperatorChecklistAdapter(
    private val context: Context,
    var listOperator: ArrayList<Data>,
    private val viewModel: ChecklistViewModel,
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<ListOperatorChecklistAdapter.ViewHolder>() {

    private lateinit var listOperatorCallBack: ListOperatorCallBack

    inner class ViewHolder(val binding: ListOperationalChecklistBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getOperator = listOperator[adapterPosition]
            listOperatorCallBack.onClickOperator(getOperator.idDetailEmployeeProject, getOperator.idEmployee)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListOperationalChecklistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOperator[position]

        holder.binding.tvNameListOperationalChecklist.text = response.employeeName
        holder.binding.tvAreaListOperationalChecklist.text = if (response.locationName == "" || response.locationName == null || response.locationName == "null") {
            "Tidak ada data"
        } else {
            response.locationName
        }
        holder.binding.tvSubAreaListOperationalChecklist.text = if (response.subLocationName == "" || response.subLocationName == null || response.subLocationName == "null") {
            "Tidak ada data"
        } else {
            response.subLocationName
        }

        // check icon
        if (response.checklistByEmployee == "Y") {
            holder.binding.ivCheckListOperationalChecklist.visibility = View.VISIBLE
        } else {
            holder.binding.ivCheckListOperationalChecklist.visibility = View.GONE
        }

        // set photo profile
        val img = response.employeePhotoProfile
        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivListOperationalChecklist.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.binding.ivListOperationalChecklist)
        }
    }

    override fun getItemCount(): Int {
        return listOperator.size
    }

    fun setListener(listOperatorCallBack: ListOperatorCallBack) {
        this.listOperatorCallBack = listOperatorCallBack
    }

    interface ListOperatorCallBack {
        fun onClickOperator(idDetailEmployeeProject: Int, employeeId: Int)
    }
}