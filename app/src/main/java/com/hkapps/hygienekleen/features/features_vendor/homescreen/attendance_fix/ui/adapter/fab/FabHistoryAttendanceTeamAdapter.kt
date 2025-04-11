package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.fab

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_team_model.Employee
import java.util.*


class FabHistoryAttendanceTeamAdapter(var myHistory: ArrayList<Employee>):
    RecyclerView.Adapter<FabHistoryAttendanceTeamAdapter.MyViewHolder>(){
    lateinit var context: Context

    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        var tvEmployeeName: TextView = view.findViewById(R.id.tvEmployeeNameHistory)
        var tvEmployeeNucName: TextView = view.findViewById(R.id.tvEmplNucJobnameHistory)
        var tvEmployeeAttendIn: TextView = view.findViewById(R.id.tvStatusAttendanceIn)
        var tvEmployeeAttendOut: TextView = view.findViewById(R.id.tvStatusAttendanceOut)
        var ivEmployeeAttend: ImageView = view.findViewById(R.id.ivImageTeamProfile)
        var btnEmployeeSeeImage: TextView = view.findViewById(R.id.tvBtnSeePhotoTeam)
        var ivPopUpPhoto: ImageView = view.findViewById(R.id.iv_selfie_in_shadow)
        var tvEmployeeSchedule: TextView = view.findViewById(R.id.tvScheduleAttendance)
        var tvEmployeeSchedulee: TextView = view.findViewById(R.id.tvScheduleAttendancee)
        var tvEmployeeStatus: TextView = view.findViewById(R.id.tvStatusAttendance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_team_adapter, parent, false)
            context = parent.context
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = myHistory[position]

        holder.tvEmployeeSchedule.text = if (item.scheduleType == null || item.scheduleType == ""){
            "--"
        } else {
            item.scheduleType
        }

        holder.tvEmployeeName.text = if (item.employeeName == null || item.employeeName == ""){
            ""
        } else {
            item.employeeName
        }
        holder.tvEmployeeNucName.text = if (item.employeeCode == null || item.jobCode == null){
            ""
        } else {
            "${item.employeeCode} | ${item.jobCode}"
        }
        holder.tvEmployeeAttendIn.text = if (item.attendanceInfo.scanIn == null || item.attendanceInfo.scanOut == ""){
            "--:--"
        } else {
            item.attendanceInfo.scanIn
        }
        holder.tvEmployeeAttendOut.text = if (item.attendanceInfo.scanOut == null || item.attendanceInfo.scanOut == ""){
            "--:--"
        } else {
            "" + item.attendanceInfo.scanOut
        }
        val urls = context!!.getString(R.string.url) + "assets.admin_master/images/photo_profile/" + item.employeePhotoProfile

        if (item.employeePhotoProfile == "null" || item.employeePhotoProfile == null || item.employeePhotoProfile == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imageResource = context!!.resources.getIdentifier(uri, null, context!!.packageName)
            val res = context!!.resources.getDrawable(imageResource)
            holder.ivEmployeeAttend.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(context!!)
                .load(urls)
                .apply(requestOptions)
                .into(holder.ivEmployeeAttend)
        }

        val url =
            context!!.getString(R.string.url) + "assets.admin_master/images/attendance_photo_selfie/" + item.attendanceInfo.employeeImgSelfieIn

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
            .skipMemoryCache(true)
            .error(R.drawable.ic_error_image)
            .override(250, 400)

        Glide.with(context!!)
            .load(url)
            .apply(requestOptions)
            .into(holder.ivPopUpPhoto)

        holder.btnEmployeeSeeImage.setOnClickListener {
            showDialog(holder.ivPopUpPhoto.drawable)
        }


        when(item.scheduleType){
            "ACTUAL SCHEDULE" -> {
                holder.tvEmployeeSchedule.text = "Actual"
                holder.tvEmployeeSchedulee.visibility = View.VISIBLE
            }
            "LEMBUR GANTI" -> {
                holder.tvEmployeeSchedulee.text = "Lembur Ganti"
                holder.tvEmployeeSchedule.visibility = View.VISIBLE
            }
        }

        when(item.statusAttendance){
            "SEDANG_BEKERJA" -> {
                holder.tvEmployeeStatus.text = "Sedang bekerja"
                val textView = holder.tvEmployeeStatus
                textView.setTextColor(Color.parseColor("#0064FB"))
            }
            "IZIN" -> {
                holder.tvEmployeeStatus.text = "Izin"
                val textView = holder.tvEmployeeStatus
                textView.setTextColor(Color.parseColor("#FEA800"))
            }
            "HADIR" -> {
                holder.tvEmployeeStatus.text = "Hadir"
                val textView = holder.tvEmployeeStatus
                textView.setTextColor(Color.parseColor("#0075FF"))
            }
            "BELUM_ABSEN" -> {
                holder.tvEmployeeStatus.text = "Belum absen"
                val textView = holder.tvEmployeeStatus
                textView.setTextColor(Color.parseColor("#848484"))
            }
            "LIBUR" -> {
                holder.tvEmployeeStatus.text = "Libur"
                val textView = holder.tvEmployeeStatus
                textView.setTextColor(Color.parseColor("#1746A2"))
            }
            "LUPA_ABSEN" -> {
                holder.tvEmployeeStatus.text = "Lupa absen"
                val textView = holder.tvEmployeeStatus
                textView.setTextColor(Color.parseColor("#FF5656"))
            }
            "TIDAK_HADIR" -> {
                holder.tvEmployeeStatus.text = "Alpa"
                val textView = holder.tvEmployeeStatus
                textView.setTextColor(Color.parseColor("#FF5656"))
            }
            "SAKIT" -> {
                holder.tvEmployeeStatus.text = "Sakit"
                val textView = holder.tvEmployeeStatus
                textView.setTextColor(Color.parseColor("#FEA800"))
            }
            "IZIN SURAT" -> {
                holder.tvEmployeeStatus.text = "Izin surat"
                val textView = holder.tvEmployeeStatus
                textView.setTextColor(Color.parseColor("#FEA800"))
            }
            "SAKIT SURAT" -> {
                holder.tvEmployeeStatus.text = "Sakit surat"
                val textView = holder.tvEmployeeStatus
                textView.setTextColor(Color.parseColor("#FEA800"))
            }
            "SUDAH ABSEN" -> {
                holder.tvEmployeeStatus.text = "Sudah absen"
                val textView = holder.tvEmployeeStatus
                textView.setTextColor(Color.parseColor("#FF2727"))
            }

        }



    }


    override fun getItemCount(): Int {
        return myHistory.size
    }
    //modal foto
    private fun showDialog(img: Drawable) {
        val dialog = Dialog(context!!)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_image_zoom)
        val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
        val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView
        ivZoom.setImageDrawable(img)
        close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}