package com.hkapps.academy.features.features_trainer.homescreen.home.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ItemTrainingScheduleBinding
import com.hkapps.academy.features.features_trainer.homescreen.home.model.listClass.Content
import java.text.SimpleDateFormat

class AllClassesTrainerAdapter(
    var listClass: ArrayList<Content>
): RecyclerView.Adapter<AllClassesTrainerAdapter.ViewHolder>() {

    private lateinit var listClassCallBack: ListClassCallBack

    inner class ViewHolder (val binding: ItemTrainingScheduleBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getClass = listClass[adapterPosition]
            listClassCallBack.onClickClass(getClass.trainingId)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTrainingScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listClass.size
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listClass[position]
        val sdfBefore = SimpleDateFormat("yyyy-MM-dd")
        val dateParam = sdfBefore.parse(response.trainingDate)
        val sdfAfter = SimpleDateFormat("dd MMM yyyy")

        // set visible participant textview
        holder.binding.tvParticipantTraining.visibility = View.VISIBLE
        holder.binding.llTrainerTraining.visibility = View.GONE
        holder.binding.tvDaysTraining.visibility = View.GONE
        holder.binding.ivNextTrainingSchedule.visibility = View.VISIBLE
        holder.binding.tvApplyTraining.visibility = View.GONE

        when(response.jenisKelas) {
            "ONLINE" -> {
                holder.binding.tvStatusTraining.text = "Online"
                holder.binding.tvStatusTraining.setBackgroundResource(R.drawable.bg_rounded_blue5)
            }
            "ONSITE" -> {
                holder.binding.tvStatusTraining.text = "Onsite"
                holder.binding.tvStatusTraining.setBackgroundResource(R.drawable.bg_rounded_orange)
            }
        }

        holder.binding.tvTimeTraining.text = "${response.durationInMinute} menit"
        holder.binding.tvTitleTraining.text = response.trainingName
        holder.binding.tvMateriTraining.text = response.moduleName
        holder.binding.tvScheduleTraining.text = "${sdfAfter.format(dateParam)} | ${response.trainingStart} - ${response.trainingEnd} ${response.region}"
        holder.binding.tvParticipantTraining.text = "${response.jumlahPeserta} peserta"
    }

    fun setListener(listClassCallBack: ListClassCallBack) {
        this.listClassCallBack = listClassCallBack
    }

    interface ListClassCallBack {
        fun onClickClass(trainingId: Int)
    }
}