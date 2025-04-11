package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListQuestionAuditBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailAuditQuestion.Data

class DetailQuestionsAuditAdapter(
    private val context: Context,
    var listQuestion: ArrayList<Data>
): RecyclerView.Adapter<DetailQuestionsAuditAdapter.ViewHolder>() {

    private lateinit var detailQuestionAuditCallback: DetailQuestionAuditCallback

    inner class ViewHolder(val binding: ListQuestionAuditBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            detailQuestionAuditCallback.onClickDetail()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListQuestionAuditBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listQuestion[position]

        holder.binding.tvQuestionListQuestion.text = response.questionName
        holder.binding.tvScoreListQuestion.visibility = View.GONE

        if (response.questionAnswer == "" || response.questionAnswer == "null" || response.questionAnswer == null) {
            holder.binding.tvListQuestion.text = "-"
            holder.binding.tvListQuestion.setTextColor(context.resources.getColor(R.color.grayTxt))
        } else {
            when(response.questionAnswer) {
                "TIDAK", "Tidak", "tidak" -> {
                    holder.binding.tvListQuestion.text = "Tidak"
                    holder.binding.tvListQuestion.setTextColor(context.resources.getColor(R.color.red1))
                }
                "YA", "Ya", "ya" -> {
                    holder.binding.tvListQuestion.text = "Ya"
                    holder.binding.tvListQuestion.setTextColor(context.resources.getColor(R.color.green2))
                }
                else -> {
                    holder.binding.tvListQuestion.text = "-"
                    holder.binding.tvListQuestion.setTextColor(context.resources.getColor(R.color.grayTxt))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listQuestion.size
    }

    fun setListener(detailQuestionAuditCallback: DetailQuestionAuditCallback) {
        this.detailQuestionAuditCallback = detailQuestionAuditCallback
    }

    interface DetailQuestionAuditCallback {
        fun onClickDetail ()
    }

}