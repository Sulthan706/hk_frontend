package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemChooserBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listObject.Data

class ListObjectInspeksiAdapter(
    var listObject: ArrayList<Data>,
    var selectedItem: Int = -1
): RecyclerView.Adapter<ListObjectInspeksiAdapter.ViewHolder>() {

    private lateinit var listObjectInspeksiCallBack: ListObjectInspeksiCallBack

    inner class ViewHolder(val binding: ListItemChooserBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val selected = listObject[adapterPosition]
            listObjectInspeksiCallBack.onObjectSelected(selected.idObjectList, selected.objectName)

            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemChooserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listObject[position]

        holder.binding.tvItemChooser.text = response.objectName
        holder.binding.ivDefaultChooser.visibility = View.VISIBLE
        holder.binding.ivSelectedChooser.visibility = View.GONE

        if (selectedItem == position) {
            holder.binding.ivDefaultChooser.visibility = View.INVISIBLE
            holder.binding.ivSelectedChooser.visibility = View.VISIBLE
        } else {
            holder.binding.ivDefaultChooser.visibility = View.VISIBLE
            holder.binding.ivSelectedChooser.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return listObject.size
    }

    fun setListener(listObjectInspeksiCallBack: ListObjectInspeksiCallBack) {
        this.listObjectInspeksiCallBack = listObjectInspeksiCallBack
    }

    interface ListObjectInspeksiCallBack {
        fun onObjectSelected(objectId: Int, objectName: String)
    }
}