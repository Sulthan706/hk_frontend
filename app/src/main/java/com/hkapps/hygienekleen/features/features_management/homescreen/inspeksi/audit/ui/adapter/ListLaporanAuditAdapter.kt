package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListAuditInspeksiBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listLaporanAudit.Content

class ListLaporanAuditAdapter(
    private val context: Context,
    var listAudit: ArrayList<Content>
): RecyclerView.Adapter<ListLaporanAuditAdapter.ViewHolder>() {

    private lateinit var laporanAuditCallBack: LaporanAuditCallBack

    inner class ViewHolder(val binding: ListAuditInspeksiBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val selected = listAudit[adapterPosition]
            laporanAuditCallBack.onClickAudit(selected.idReport)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListAuditInspeksiBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listAudit[position]

        holder.binding.tvPeriodeListAuditInspeksi.text = response.periodDate
        holder.binding.tvDateTimeListAuditInspeksi.text = response.date
        holder.binding.tvAuditorListAuditInspeksi.text = response.auditCreatorName

        // validate check L1
        if (response.l1Checked) {
            holder.binding.tvL1ListAuditInspeksi.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_done_circle_green, 0, 0, 0)
        } else {
            holder.binding.tvL1ListAuditInspeksi.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_chooser, 0, 0, 0)
        }

        // validate check L2
        if (response.l2Checked) {
            holder.binding.tvL2ListAuditInspeksi.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_done_circle_green, 0, 0, 0)
        } else {
            holder.binding.tvL2ListAuditInspeksi.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_chooser, 0, 0, 0)
        }

        // validate check L3
        if (response.l3Checked) {
            holder.binding.tvL3ListAuditInspeksi.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_done_circle_green, 0, 0, 0)
        } else {
            holder.binding.tvL3ListAuditInspeksi.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_chooser, 0, 0, 0)
        }
    }

    override fun getItemCount(): Int {
        return listAudit.size
    }

    fun setListener(listLaporanAuditCallBack: LaporanAuditCallBack) {
        laporanAuditCallBack = listLaporanAuditCallBack
    }

    interface LaporanAuditCallBack {
        fun onClickAudit (idReport: Int)
    }
}