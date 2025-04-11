package com.hkapps.hygienekleen.features.features_client.report.ui.new_.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListOperatorReportBinding
import com.hkapps.hygienekleen.features.features_client.report.model.jadwalkerja.Operator

class ListOperatorReportAdapter(
    private val context: Context,
    var listOperator: ArrayList<Operator>
) : RecyclerView.Adapter<ListOperatorReportAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ListOperatorReportBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListOperatorReportBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOperator[position]
        holder.binding.tvNameEmployeeHistoryAttendanceProject.text = response.employeeName
        holder.binding.tvStatusAbsentEmployeeHistoryAttendanceProject.text = when (response.statusAttendance) {
            "TIDAK HADIR", "Tidak Hadir", "TIDAK_HADIR", "Tidak_hadir", "Tidak_Hadir" -> "Tidak hadir"
            "BELUM ABSEN", "Belum Absen", "BELUM_ABSEN", "Belum_absen", "Belum_Absen" -> "Belum absen"
            "SEDANG BEKERJA", "Sedang Bekerja", "SEDANG_BEKERJA", "Sedang_bekerja", "Sedang_Bekerja",
            "Bertugas", "BERTUGAS" -> "Sedang bekerja"
            "SELESAI", "Selesai", "HADIR" -> "Hadir"
            "LIBUR" -> "Libur"
            else -> response.statusAttendance
        }
        when (response.statusAttendance) {
            "TIDAK HADIR", "Tidak Hadir", "Tidak hadir",
            "BELUM ABSEN", "Belum Absen", "Belum absen",
            "BELUM_ABSEN", "Belum_absen", "Belum_Absen",
            "TIDAK_HADIR", "Tidak_hadri", "Tidak_Hadir" -> {
                holder.binding.tvStatusAbsentEmployeeHistoryAttendanceProject.setTextColor(context.resources.getColor(
                    R.color.red1))
            }
            "SEDANG BEKERJA", "Sedang Bekerja", "Sedang bekerja", "Bertugas", "BERTUGAS",
            "SEDANG_BEKERJA", "Sedang_bekerja", "Sedang_Bekerja" -> {
                holder.binding.tvStatusAbsentEmployeeHistoryAttendanceProject.setTextColor(context.resources.getColor(
                    R.color.blue))
            }
            "SELESAI", "Selesai", "HADIR", "Hadir" -> {
                holder.binding.tvStatusAbsentEmployeeHistoryAttendanceProject.setTextColor(context.resources.getColor(
                    R.color.green))
            }
            "LIBUR", "Libur" -> {
                holder.binding.tvStatusAbsentEmployeeHistoryAttendanceProject.setTextColor(context.resources.getColor(
                    R.color.grey2_client))
            }
        }
        if (response.scheduleType == "LEMBUR GANTI" || response.scheduleType == "Lembur Ganti" ||
            response.scheduleType == "Lembur ganti") {
            holder.binding.tvStatusScheduleEmployeeHistoryAttendanceProject.visibility = View.VISIBLE
        } else {
            holder.binding.tvStatusScheduleEmployeeHistoryAttendanceProject.visibility = View.INVISIBLE
        }
        // set photo profile
        val imgEmployee = response.employeeImage
        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$imgEmployee"

        if (imgEmployee == "null" || imgEmployee == null || imgEmployee == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.getDrawable(imaResource)
            holder.binding.ivEmployeeHistoryAttendanceProject.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(context.getDrawable(R.drawable.ic_error_image))

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.binding.ivEmployeeHistoryAttendanceProject)
        }

    }

    override fun getItemCount(): Int {
        return listOperator.size
    }
}