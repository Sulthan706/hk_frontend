package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListMeetingInspeksiBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listRoutine.Content

class ListRoutineReportAdapter(
    private val context: Context,
    var listRoutine: ArrayList<Content>
): RecyclerView.Adapter<ListRoutineReportAdapter.ViewHolder>() {

    private lateinit var listRoutineCallBack: ListRoutineCallBack

    inner class ViewHolder(val binding: ListMeetingInspeksiBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val selected = listRoutine[adapterPosition]
            listRoutineCallBack.onClickItem(selected.idroutine)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListMeetingInspeksiBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listRoutine.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listRoutine[position]

        holder.binding.tvTitleTimeListMeetingInspeksi.visibility = View.INVISIBLE
        holder.binding.tvTimeItemListMeetingInspeksi.visibility = View.INVISIBLE

        holder.binding.tv1ListMeetingInspeksi.text = "Rutin"
        holder.binding.tv1ListMeetingInspeksi.setTextColor(Color.parseColor("#0075FF"))

        holder.binding.tvDateItemListMeetingInspeksi.text = response.date
        holder.binding.tvTopicItemListMeetingInspeksi.text = response.title
    }

    fun setListener(listRoutineCallBack: ListRoutineCallBack) {
        this.listRoutineCallBack = listRoutineCallBack
    }

    interface ListRoutineCallBack {
        fun onClickItem(idRoutine: Int)
    }
}