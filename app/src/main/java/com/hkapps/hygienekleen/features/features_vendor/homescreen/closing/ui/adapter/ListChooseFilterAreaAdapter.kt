package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemChooserBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.area.AreaAssignment

class ListChooseFilterAreaAdapter( var listChooserInspeksi: List<AreaAssignment>,
                                   var selectedItem: Int = -1
): RecyclerView.Adapter<ListChooseFilterAreaAdapter.ViewHolder>() {

    private lateinit var onItemSelectedCallBack: OnItemSelectedCallBack

    inner class ViewHolder(val binding: ListItemChooserBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val selected = listChooserInspeksi[adapterPosition]
            onItemSelectedCallBack.onItemSelected(selected)

            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemChooserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListChooseFilterAreaAdapter.ViewHolder, position: Int) {
        val response = listChooserInspeksi[position]

        holder.binding.tvItemChooser.text = response.locationName
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
        return listChooserInspeksi.size
    }

    fun setListener(onItemSelectedCallBack: OnItemSelectedCallBack) {
        this.onItemSelectedCallBack = onItemSelectedCallBack
    }

    interface OnItemSelectedCallBack {
        fun onItemSelected(areaAssignment: AreaAssignment)
    }

}