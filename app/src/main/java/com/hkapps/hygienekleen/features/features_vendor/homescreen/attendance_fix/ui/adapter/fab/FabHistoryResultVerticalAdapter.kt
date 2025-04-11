package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.fab

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_history_result.Attendance

class FabHistoryResultVerticalAdapter(var myHistoryResultVer: ArrayList<Attendance>):
RecyclerView.Adapter<FabHistoryResultVerticalAdapter.MyViewHolder>(){
    lateinit var context: Context
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        var tvShift: TextView = view.findViewById(R.id.tvShiftHistoryResult)
        var tvStatus: TextView = view.findViewById(R.id.tvStatusResult)
        var tvDate: TextView = view.findViewById(R.id.tvDateResult)
        var tvSchedule: TextView = view.findViewById(R.id.tvScheduleResult)
        var tvScanIn: TextView = view.findViewById(R.id.tvScanInResult)
        var tvScanOut: TextView = view.findViewById(R.id.tvScanOutResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_schedule_result, parent, false)
        context = parent.context
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = myHistoryResultVer[position]

       holder.tvShift.text = if (item.shift == null || item.shift == ""){
           "---"
       } else {
           item.shift
       }
       when(item.statusAttendance){
           "TIDAK_HADIR" -> {
               holder.tvStatus.text = "Alpa"
               holder.tvStatus.setTextColor(Color.parseColor("#FF5656"))
           }
           "HADIR" -> {
               holder.tvStatus.text = "Hadir"
               holder.tvStatus.setTextColor(Color.parseColor("#0075FF"))
           }
           "LIBUR" -> {
               holder.tvStatus.text = "Libur"
               holder.tvStatus.setTextColor(Color.parseColor("#969696"))
           }
           "LUPA_ABSEN" -> {
               holder.tvStatus.text = "Lupa Absen"
               holder.tvStatus.setTextColor(Color.parseColor("#FF5656"))
           }
           "IZIN" -> {
               holder.tvStatus.text = "Izin"
               holder.tvStatus.setTextColor(Color.parseColor("#969696"))
           }
//           "SEDANG_BEKERJA" -> {
//               holder.tvStatus.text = "Sedang Bekerja"
//           }
//           "BELUM_ABSEN" -> {
//               holder.tvStatus.text = "Belum Absen"
//           }
//           "SAKIT" -> {
//               holder.tvStatus.text = "Sakit"
//           }
//           "IZIN SURAT" -> {
//               holder.tvStatus.text = "Izin Surat"
//           }
//           "SAKIT SURAT" -> {
//               holder.tvStatus.text = "Sakit Surat"
//           }
//           "SUDAH ABSEN" -> {
//               holder.tvStatus.text = "Sudah Absen"
//           }
       }
        when(item.scheduleType){
            "ACTUAL_SCHEDULE" -> {
                holder.tvSchedule.text = "Actual"
            }
            "LEMBUR GANTI" -> {
                holder.tvSchedule.text = "Lembur ganti"
            }
        }

        holder.tvDate.text = if (item.date == null || item.date == ""){
            "---"
        } else {
            item.date
        }

        holder.tvScanIn.text = if (item.scanIn == null || item.scanIn == ""){
            "--:--"
        } else {
            item.scanIn
        }
        holder.tvScanOut.text = if (item.scanOut == null || item.scheduleType == ""){
            "--:--"
        } else {
            item.scanOut.toString()
        }


    }

    override fun getItemCount(): Int {
        return myHistoryResultVer.size
    }
}