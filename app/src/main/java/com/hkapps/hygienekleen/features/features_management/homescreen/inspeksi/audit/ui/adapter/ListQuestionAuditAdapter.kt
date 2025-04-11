package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListQuestionAuditBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listQuestionAudit.Data

class ListQuestionAuditAdapter(
    private val context: Context,
    var listQuestion: ArrayList<Data>
): RecyclerView.Adapter<ListQuestionAuditAdapter.ViewHolder>() {

    private lateinit var questionAuditCallBack: QuestionAuditCallBack

    inner class ViewHolder(val binding: ListQuestionAuditBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val selected = listQuestion[adapterPosition]
            questionAuditCallBack.onClickQuestion(selected.questionId, selected.idSubmitQuestion)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListQuestionAuditBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listQuestion[position]

        holder.binding.tvQuestionListQuestion.text = response.questionName
        if (response.scoreStatus == "" || response.scoreStatus == "null" || response.scoreStatus == null) {
            holder.binding.tvScoreListQuestion.text = "Belum ada nilai"
            holder.binding.tvScoreListQuestion.setTextColor(context.resources.getColor(R.color.grayTxt))
        } else {
            holder.binding.tvScoreListQuestion.text = response.scoreStatus
            holder.binding.tvScoreListQuestion.setTextColor(context.resources.getColor(R.color.blue3))
        }
        if (!response.score) {
            holder.binding.tvListQuestion.text = "Beri Nilai"
            holder.binding.tvListQuestion.setTextColor(context.resources.getColor(R.color.grayLine))
            holder.binding.tvListQuestion.setBackgroundResource(R.drawable.bg_field)
        } else {
            holder.binding.tvListQuestion.text = "Ubah Nilai"
            holder.binding.tvListQuestion.setTextColor(context.resources.getColor(R.color.blue3))
            holder.binding.tvListQuestion.setBackgroundResource(R.drawable.bg_field_blue)
        }
    }

    override fun getItemCount(): Int {
        return listQuestion.size
    }

    fun setListener(questionAuditCallBack: QuestionAuditCallBack) {
        this.questionAuditCallBack = questionAuditCallBack
    }

    interface QuestionAuditCallBack {
        fun onClickQuestion(questionId: Int, idSubmitQuestion: Int)
    }
}