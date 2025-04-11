package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.new_.midlevel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemAttendanceStaffNotAttendanceBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_not_absent.EmployeeNotAttendance
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_not_absent.EmployeePhoneNumberNotAttendance
import kotlin.collections.ArrayList


class AttendanceFixMidListStaffNotAttendanceAdapter(
    private var staffNotAttendance: ArrayList<EmployeeNotAttendance>,
) :
    RecyclerView.Adapter<AttendanceFixMidListStaffNotAttendanceAdapter.ViewHolder>() {
    private var context: Context? = null

    private lateinit var phoneCallback: PhoneCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemAttendanceStaffNotAttendanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(staffNotAttendance[position]) {
                val response = staffNotAttendance[position]
                binding.tvStaffNotAttendanceName.text = response.employeeName
                binding.tvStaffNotAttendanceJobCode.text = response.employeeCode

//                binding.llBtnCallAttendance.setOnClickListener {
//                    listener.openCallOperator(response.employeePhoneNumber)
//                    Log.d("telpon","$listener")
//                }

                val urls = context!!.getString(R.string.url) + "assets.admin_master/images/photo_profile/" + response.employeeImage

                if (response.employeeImage == "null" || response.employeeImage == null || response.employeeImage == "") {
                    val uri =
                        "@drawable/profile_default" // where myresource (without the extension) is the file
                    val imageResource = context!!.resources.getIdentifier(uri, null, context!!.packageName)
                    val res = context!!.resources.getDrawable(imageResource)
                    binding.imgNotAttendance.setImageDrawable(res)
                } else {
                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true)

                    Glide.with(context!!)
                        .load(urls)
                        .apply(requestOptions)
                        .into(binding.imgNotAttendance)
                }


            }
        }
    }



    interface PhoneCallback {
        fun onClickOperator(employeePhoneNumber: List<EmployeePhoneNumberNotAttendance>)
    }

    fun setListener(phoneCallback: PhoneCallback){
        this.phoneCallback = phoneCallback
    }

    override fun getItemCount(): Int {
        return staffNotAttendance.size
    }

    inner class ViewHolder(val binding: ItemAttendanceStaffNotAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val response = staffNotAttendance[adapterPosition]
            phoneCallback.onClickOperator(response.employeePhoneNumber)
        }
    }

}