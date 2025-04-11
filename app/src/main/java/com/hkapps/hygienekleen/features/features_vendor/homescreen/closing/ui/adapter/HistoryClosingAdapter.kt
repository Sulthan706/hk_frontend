package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemHistoryClosingBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.historyclosing.HistoryClosing


class HistoryClosingAdapter(
    val data : MutableList<HistoryClosing>,
    private val isChief : Boolean
) : RecyclerView.Adapter<HistoryClosingAdapter.HistoryClosingViewHolder>() {

    inner class HistoryClosingViewHolder(val binding : ListItemHistoryClosingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryClosingViewHolder {
        return HistoryClosingViewHolder(ListItemHistoryClosingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: HistoryClosingViewHolder, position: Int) {
        holder.binding.apply {
            tvNo.text = data[position].rowNumber.toString()
            if(isChief){
                tvShift.visibility = View.GONE
                linearClosingHistory.gravity = Gravity.NO_GRAVITY

            }
            tvShift.text = data[position].shift
            tvDate.text = data[position].date
            tvTime.text = data[position].closingAt
        }
    }
}