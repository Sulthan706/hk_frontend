package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.new_.midlevel

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemAttendanceStaffAlreadyAttendanceBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_already_absent.EmployeeAlreadyAttendance


class AttendanceFixMidListStaffAlreadyAttendanceAdapter(
    private var staffAlreadyAttendance: List<EmployeeAlreadyAttendance>,
) :
    RecyclerView.Adapter<AttendanceFixMidListStaffAlreadyAttendanceAdapter.ViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemAttendanceStaffAlreadyAttendanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(staffAlreadyAttendance[position]) {
                val response = staffAlreadyAttendance[position]
                binding.tvStaffAlreadyAttendanceName.text = response.employeeName

                binding.tvStaffAlreadyAttendanceRankJobCode.text = if (response.jobCode.isNullOrEmpty() && response.jobName.isNullOrEmpty()){
                    ""
                } else {
                    "${response.jobCode} | ${response.jobName}"
                }


                val urls =
                    context!!.getString(R.string.url) + "assets.admin_master/images/photo_profile/" + response.employeeImage

                if (response.employeeImage == "null" || response.employeeImage == null || response.employeeImage == "") {
                    val uri =
                        "@drawable/profile_default" // where myresource (without the extension) is the file
                    val imageResource =
                        context!!.resources.getIdentifier(uri, null, context!!.packageName)
                    val res = context!!.resources.getDrawable(imageResource)
                    binding.imgAlreadyAttendance.setImageDrawable(res)
                } else {
                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true)
                        .error(R.drawable.ic_error_image)

                    Glide.with(context!!)
                        .load(urls)
                        .apply(requestOptions)
                        .into(binding.imgAlreadyAttendance)
                }


                val url =
                    context!!.getString(R.string.url) + "assets.admin_master/images/attendance_photo_selfie/" + response.attendanceImageIn

                val requestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                    .skipMemoryCache(true)
                    .error(R.drawable.ic_error_image)
                    .override(250, 400)

                Glide.with(context!!)
                    .load(url)
                    .apply(requestOptions)
                    .into(binding.ivSelfieInShadow)

                Log.d("TAG", "photoselfiein: $url")

                binding.ivSelfieIn.setOnClickListener {
                    showDialog(binding.ivSelfieInShadow.drawable)
                }


                when (response.scheduleType) {
                    "ACTUAL SCHEDULE" -> {
                        binding.tvScheduleAttendancee.text = "Actual"
                        binding.tvScheduleAttendancee.visibility = View.VISIBLE
                        binding.tvScheduleAttendance.visibility = View.INVISIBLE
                    }
                    "LEMBUR GANTI" -> {
                        binding.tvScheduleAttendance.text = "Lembur Ganti"
                        binding.tvScheduleAttendancee.visibility = View.INVISIBLE
                        binding.tvScheduleAttendance.visibility = View.VISIBLE
                    }

                }

                when(response.statusAttendance){
                    "SEDANG_BEKERJA" -> {
                        binding.tvStatusAttendance.text = "Sedang bekerja"
                        val textView = binding.tvStatusAttendance
                        textView.setTextColor(Color.parseColor("#0064FB"))
                    }
                    "IZIN" -> {
                        binding.tvStatusAttendance.text = "Izin"
                        val textView = binding.tvStatusAttendance
                        textView.setTextColor(Color.parseColor("#F47721"))
                    }
                    "HADIR" -> {
                        binding.tvStatusAttendance.text = "Hadir"
                        val textView = binding.tvStatusAttendance
                        textView.setTextColor(Color.parseColor("#FF2727"))
                    }
                    "BELUM_ABSEN" -> {
                        binding.tvStatusAttendance.text = "Belum Absen"
                        val textView = binding.tvStatusAttendance
                        textView.setTextColor(Color.parseColor("#848484"))
                    }
                    "LIBUR" -> {
                        binding.tvStatusAttendance.text = "Libur"
                        val textView = binding.tvStatusAttendance
                        textView.setTextColor(Color.parseColor("#848484"))
                    }
                    "LUPA_ABSEN" -> {
                        binding.tvStatusAttendance.text = "Lupa Absen"
                        val textView = binding.tvStatusAttendance
                        textView.setTextColor(Color.parseColor("#FF5656"))
                    }
                    "TIDAK_HADIR" -> {
                        binding.tvStatusAttendance.text = "Tidak Hadir"
                        val textView = binding.tvStatusAttendance
                        textView.setTextColor(Color.parseColor("#FF5656"))
                    }
                    "SAKIT" -> {
                        binding.tvStatusAttendance.text = "Sakit"
                        val textView = binding.tvStatusAttendance
                        textView.setTextColor(Color.parseColor("#FF5656"))
                    }
                    "IZIN SURAT" -> {
                        binding.tvStatusAttendance.text = "Izin Surat"
                        val textView = binding.tvStatusAttendance
                        textView.setTextColor(Color.parseColor("#FF5656"))
                    }
                    "SAKIT SURAT" -> {
                        binding.tvStatusAttendance.text = "Sakit Surat"
                        val textView = binding.tvStatusAttendance
                        textView.setTextColor(Color.parseColor("#FF5656"))
                    }
                    "SUDAH ABSEN" -> {
                        binding.tvStatusAttendance.text = "Sudah Absen"
                        val textView = binding.tvStatusAttendance
                        textView.setTextColor(Color.parseColor("#FF2727"))
                    }



                }


            }
        }
    }

    override fun getItemCount(): Int {
        return staffAlreadyAttendance.size
    }

    inner class ViewHolder(val binding: ItemAttendanceStaffAlreadyAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root)

    //pop up modal
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
