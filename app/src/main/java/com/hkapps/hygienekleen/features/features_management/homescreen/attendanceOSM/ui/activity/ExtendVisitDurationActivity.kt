package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityExtendVisitDurationBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.viewModel.AttendanceManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeManagementActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ExtendVisitDurationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExtendVisitDurationBinding

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val idRkbOperation = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, 0)
    private val lastVisitProject = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")
    private val lastVisitTime = CarefastOperationPref.loadString(CarefastOperationPrefConst.TIME_LAST_VISIT, "")
    private var extendTime = ""
    private var extendDuration = 0
    private var timeAttendanceOut = ""

    private val viewModel: AttendanceManagementViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceManagementViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtendVisitDurationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarExtendVisitDuration.tvAppbarTitle.text = "Extend Durasi Visit Project"
        binding.appbarExtendVisitDuration.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set last visit project
        binding.tvProjectExtendVisitDuration.text = lastVisitProject

        // set current date
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        val currentDate = sdf.format(Date())
        binding.tvDateExtendVisitDuration.text = currentDate

        // set time picker
//        binding.tvTimeExtendVisitDuration.setOnClickListener {
//            openDialogTime()
//        }

        // set input duration time
        binding.etDurationExtendVisitDuration.addTextChangedListener {
            if (binding.etDurationExtendVisitDuration.text.toString() == "") {
                binding.tvDurationExtendVisitDuration.text = "menit hingga jam --:--"
                extendDuration = 0
            } else {
                val minutes = binding.etDurationExtendVisitDuration.text.toString()
//                val extendedTime = LocalDateTime.now().plusMinutes(minutes.toInt().toLong())
                val timeFormatted = LocalTime.parse(timeAttendanceOut)
                val extendedTime = timeFormatted.plusMinutes(minutes.toInt().toLong())
                extendTime = extendedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                binding.tvDurationExtendVisitDuration.text = "menit hingga jam $extendTime"
                extendDuration = minutes.toInt()
            }
        }

        // set enable button submit
        binding.etReasonExtendVisitDuration.addTextChangedListener {
            if (binding.etReasonExtendVisitDuration.text.toString() == "") {
                binding.btnSubmitExtendVisitDuration.isEnabled = false
                binding.btnSubmitExtendVisitDuration.setBackgroundResource(R.drawable.bg_disable)
            } else {
                binding.btnSubmitExtendVisitDuration.isEnabled = true
                binding.btnSubmitExtendVisitDuration.setBackgroundResource(R.drawable.bg_secondary)

                binding.btnSubmitExtendVisitDuration.setOnClickListener {
                    viewModel.submitExtendVisitDuration(userId, idRkbOperation, extendDuration, binding.etReasonExtendVisitDuration.text.toString())
                }
            }
        }

        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getDetailScheduleManagement(idRkbOperation, userId)
    }

    private fun setObserver() {
        viewModel.extendVisitDuration.observe(this) {
            if (it.code == 200) {
                CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                showDialogSuccessExtendVisit()
            } else if (it.errorCode == "404") {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed submit extend visit duration", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.detailScheduleManagementModel.observe(this) {
            if (it.code == 200) {
                timeAttendanceOut = it.data.scanOutAvailableAt
            } else {
                timeAttendanceOut = ""
                Toast.makeText(this, "Gagal mengambil waktu absen pulang", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogSuccessExtendVisit() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_success_extend_visit)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfoExtendVisitDuration)
        val tvTime = dialog.findViewById<TextView>(R.id.tvTimeExtendVisitDuration)
        val btnClose = dialog.findViewById<AppCompatButton>(R.id.btnExtendVisitDuration)

        tvInfo.text = "Waktu kunjungan project diperpanjang selama $extendDuration menit. Silakan absen keluar pada jam :"
        tvTime.text = extendTime
        btnClose.setOnClickListener {
            startActivity(Intent(this, HomeManagementActivity::class.java))
            finishAffinity()
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun openDialogTime() {
        var hours = 0
        var minutes = 0
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            hours = hour
            minutes = minute
            extendTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
//            binding.tvTimeExtendVisitDuration.text = extendTime
//            binding.tvTimeExtendVisitDuration.setTextColor(resources.getColor(com.digimaster.academy.R.color.black2))

            // set duration
            val lastTime = LocalTime.parse(lastVisitTime)
            val extendedTime = LocalTime.parse(extendTime)
            val duration = Duration.between(extendedTime, lastTime)
//            val mHours = duration.toHours()
//            val mMinutes = duration.minusHours(mHours).toMinutes()
            val mMinutes = duration.toMinutes()
            extendDuration = mMinutes.toInt()
            binding.tvDurationExtendVisitDuration.text = "$mMinutes menit"

//            binding.tvDurationExtendVisitDuration.text = if (mHours == 0L) {
//                "$mMinutes menit"
//            } else if (mMinutes == 0L) {
//                "$mHours jam"
//            } else {
//                "$mHours jam $mMinutes menit"
//            }

        }

        val style = AlertDialog.THEME_HOLO_LIGHT
        val timePickerDialog = TimePickerDialog(this, style, timeSetListener, hours, minutes, true)
        timePickerDialog.setTitle("Pilih Waktu Extend")
        timePickerDialog.show()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}