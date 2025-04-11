package com.hkapps.hygienekleen.features.features_management.service.overtime.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListOvertimeChangeBinding
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.listOvertimeChange.Content
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.activity.CreateOvertimeChangeManagementActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class OvertimeChangeManagementAdapter(
    private var context: Context,
    var listOvertime: ArrayList<Content>
) : RecyclerView.Adapter<OvertimeChangeManagementAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListOvertimeChangeBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListOvertimeChangeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOvertime[position]
        val replaceId = response.employeeReplaceById
        val replaceName = response.employeeReplaceByName

        holder.binding.tvSubLocListOvertimeChange.visibility = View.GONE
        holder.binding.tvReplaceNameListOvertimeChange.visibility = View.VISIBLE

        if (replaceId == null || replaceName == null || replaceId == 0 ||
            replaceName == "" || replaceName == "null") {
            holder.binding.ll1ListOvertimeChange.visibility = View.GONE
            holder.binding.rlListOvertimeChange.visibility = View.VISIBLE

            when (response.statusAttendance) {
                "Izin", "IZIN", "IZIN SURAT", "Izin Surat", "Izin surat",
                "Sakit", "SAKIT", "SAKIT SURAT", "Sakit Surat", "Sakit surat" -> {
                    holder.binding.rlListOvertimeChange.visibility = View.VISIBLE
                    holder.binding.rlListOvertimeChange.setOnClickListener {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "lembur ganti izin")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.OVERTIME_CHANGE_TITLE, "Lembur ganti izin")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.OVERTIME_CHANGE_DATE, response.date)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.OVERTIME_CHANGE_SHIFT, response.shiftDescription)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.OVERTIME_CHANGE_OPERATIONAL, response.employeeName)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.OVERTIME_CHANGE_DATE_FORMATTED, response.dateParameter)
                        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OVERTIME_CHANGE_SHIFT_ID, response.shiftId)
                        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OVERTIME_CHANGE_OPERATIONAL_ID, response.employeeId)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.OVERTIME_CHANGE_JOB_LEVEL, response.employeeJobLevel)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.OVERTIME_CHANGE_PROJECT_CODE, response.projectCode)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.OVERTIME_CHANGE_PROJECT_NAME, response.projectName)
                        val areaName = when(response.areaName) {
                            null, "null", "" -> ""
                            else -> response.areaName
                        }
                        val subAreaName = when(response.subAreaName) {
                            null, "null", "" -> ""
                            else -> response.subAreaName
                        }
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.OVERTIME_CHANGE_AREA, areaName)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.OVERTIME_CHANGE_SUB_AREA, subAreaName)

                        context.startActivity(Intent(context, CreateOvertimeChangeManagementActivity::class.java))
                    }
                }
                else -> {
                    holder.binding.rlListOvertimeChange.visibility = View.GONE
                    holder.binding.clListOvertimeChange.visibility = View.GONE
                    holder.binding.clListOvertimeChange.layoutParams = ViewGroup.LayoutParams(0, 0)
                }
            }
        } else {
            holder.binding.ll1ListOvertimeChange.visibility = View.VISIBLE
            holder.binding.clListOvertimeChange.visibility = View.VISIBLE
            holder.binding.rlListOvertimeChange.visibility = View.GONE
        }

        holder.binding.tvNamaListOvertimeChange.text = response.employeeName
        holder.binding.tvTitleListOvertimeChange.text = when(response.statusAttendance) {
            "TIDAK_HADIR", "TIDAK HADIR" -> "Tidak Hadir"
            "OFF", "Off", "LIBUR", "Libur" -> "Libur"
            "IZIN" -> "Izin"
            "IZIN SURAT" -> "Izin Surat"
            "SAKIT" -> "Sakit"
            "SAKIT SURAT" -> "Sakit Surat"
            "RESIGN" -> "Resign"
            else -> response.statusAttendance
        }
        holder.binding.tvDateListOvertimeChange.text = response.date
        holder.binding.tvTimeListOvertimeChange.text = response.shiftDescription
        holder.binding.tvLocListOvertimeChange.text = "Digantikan oleh:"
        holder.binding.tvReplaceNameListOvertimeChange.text = response.employeeReplaceByName

        // set user images
        val img = response.employeePhotoProfile
        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource = context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivListOvertimeChange.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(context.getDrawable(R.drawable.ic_error_image))

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.binding.ivListOvertimeChange)
        }
    }

    override fun getItemCount(): Int {
        return listOvertime.size
    }

}