package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemChooserBinding

class DefaultChooseInspeksiAdapter(
    var listChooserInspeksi: ArrayList<String>,
    var selectedItem: Int
): RecyclerView.Adapter<DefaultChooseInspeksiAdapter.ViewHolder>() {

    private lateinit var onItemSelectedCallBack: OnItemDefaultSelectedCallBack

    inner class ViewHolder(val binding: ListItemChooserBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val selected = listChooserInspeksi[adapterPosition]
            onItemSelectedCallBack.onItemDefaultSelected(selected, adapterPosition)

            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemChooserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listChooserInspeksi[position]

        holder.binding.tvItemChooser.text = response
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

    fun setListener(onItemSelectedCallBack: OnItemDefaultSelectedCallBack) {
        this.onItemSelectedCallBack = onItemSelectedCallBack
    }

    interface OnItemDefaultSelectedCallBack {
        fun onItemDefaultSelected(item: String, position: Int)
    }
}