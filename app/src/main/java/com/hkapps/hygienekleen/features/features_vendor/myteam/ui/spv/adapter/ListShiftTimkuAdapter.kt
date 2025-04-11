package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listShiftModel.ListShiftData

class ListShiftTimkuAdapter(
    private val context: Context,
    var listShift: ArrayList<ListShiftData>
) : RecyclerView.Adapter<ListShiftTimkuAdapter.ViewHolder>() {

    private lateinit var listShiftCallback: ListShiftCallBack

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_shift, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listShift[position]

        holder.tvShift.text = response.shift.shiftName
    }

    override fun getItemCount(): Int {
        return listShift.size
    }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvShift: TextView = itemView.findViewById(R.id.tv_shiftTimku)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val getShift = listShift[position]

            listShiftCallback.onClickShift(getShift.shift.shiftName, getShift.shift.shiftId)
        }
    }

    fun setListener (listShiftCallBack: ListShiftCallBack) {
        this.listShiftCallback = listShiftCallBack
    }

    interface ListShiftCallBack {
        fun onClickShift(shiftName: String, shiftId: Int)
    }
}