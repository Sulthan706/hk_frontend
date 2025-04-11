package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListQuestionAuditBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listWorkResult.Data

class QuestionsWorkResultAdapter(
    private val context: Context,
    var listQuestion: ArrayList<Data>
): RecyclerView.Adapter<QuestionsWorkResultAdapter.ViewHolder>() {

    private lateinit var workResultCallBack: WorkResultCallBack

    inner class ViewHolder(val binding: ListQuestionAuditBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val selected = listQuestion[adapterPosition]
            workResultCallBack.onClickWorkResult(selected.idArea, selected.areaName)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListQuestionAuditBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listQuestion[position]

        holder.binding.tvQuestionListQuestion.text = response.areaName
        if (response.totalObject == 0 || response.totalObject == null) {
            holder.binding.tvScoreListQuestion.text = "Belum ada nilai"
            holder.binding.tvScoreListQuestion.setTextColor(context.resources.getColor(R.color.grayTxt))

            holder.binding.tvListQuestion.text = "Beri Nilai"
            holder.binding.tvListQuestion.setTextColor(context.resources.getColor(R.color.grayLine))
            holder.binding.tvListQuestion.setBackgroundResource(R.drawable.bg_field)
        } else {
            holder.binding.tvScoreListQuestion.text = "${response.totalObject} obyek"
            holder.binding.tvScoreListQuestion.setTextColor(context.resources.getColor(R.color.blue3))

            holder.binding.tvListQuestion.text = "Ubah Nilai"
            holder.binding.tvListQuestion.setTextColor(context.resources.getColor(R.color.blue3))
            holder.binding.tvListQuestion.setBackgroundResource(R.drawable.bg_field_blue)
        }
    }

    override fun getItemCount(): Int {
        return listQuestion.size
    }

    fun setListener(workResultCallBack: WorkResultCallBack) {
        this.workResultCallBack = workResultCallBack
    }

    interface WorkResultCallBack {
        fun onClickWorkResult(idAreaKomponen: Int, nameAreaKomponen: String)
    }
}