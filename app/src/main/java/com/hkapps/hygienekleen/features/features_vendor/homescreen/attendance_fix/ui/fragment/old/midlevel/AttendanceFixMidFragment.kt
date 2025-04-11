package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.fragment.old.midlevel

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentAttendanceFixBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.QRCodeGenerator
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.AttendanceFixHistoryActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback
import com.google.android.material.snackbar.Snackbar

class AttendanceFixMidFragment : Fragment() {

    private var listener: NoInternetConnectionCallback? = null

    private lateinit var binding: FragmentAttendanceFixBinding
    private val attedanceViewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    fun setListener(listener: NoInternetConnectionCallback) {
        this.listener = listener
    }

    companion object {
        @JvmStatic
        fun newInstance(params: Int) =
            AttendanceFixMidFragment().apply {
                arguments = Bundle().apply {
//                    putInt(params)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttendanceFixBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        Sementara
        binding.tvAttendanceTitle.setOnClickListener {
            val i = Intent(context, QRCodeGenerator::class.java)
            startActivity(i)
//            requireActivity().finishAffinity()
        }

        binding.tvAttendanceSeeAllHistory.setOnClickListener {
            val i = Intent(context, AttendanceFixHistoryActivity::class.java)
            startActivity(i)
        }

        attedanceViewModel.getStatusAttendance(employeeId, projectCode)
//        getStatusAttendance()
    }

//    @SuppressLint("SetTextI18n")
//    private fun getStatusAttendance() {
//        attedanceViewModel.attStatus.observe(requireActivity()) {
//            if (it.code == 200) {
//                //get status nya disini
//                when (it.data.status) {
//                    "Belum Absen" -> {
//                        binding.llCheckIn.setOnClickListener {
//                            val i =
//                                Intent(context, AttendanceFixMidDailyCheckInActivity::class.java)
//                            startActivity(i)
//                        }
//                        binding.llCheckOut.setOnClickListener {
//                            showDialog(getString(R.string.belum_absen))
//                        }
//                        onSNACK(binding.root, "Belum absen")
//                    }
//
//                    "Selesai" -> {
//                        binding.llCheckIn.setOnClickListener {
//                            Toast.makeText(
//                                context,
//                                "Anda sudah check in, status anda sudah selesai Bertugas.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//
//                        binding.llCheckOut.setOnClickListener {
//                            Toast.makeText(
//                                context,
//                                "Anda sudah check out, status anda sudah selesai Bertugas.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//
//                        if (it.data.attendanceInfo.scanOut == "" || it.data.attendanceInfo.scanOut == null) {
//                            binding.tvAttendanceHistoryEnabledOutStatus.text =
//                                it.data.statusAttendanceOut
//                        } else {
//                            binding.tvAttendanceHistoryEnabledOutStatus.text =
//                                it.data.attendanceInfo.scanOut
//                        }
//
//                        if (it.data.attendanceInfo.scanIn == "" || it.data.attendanceInfo.scanIn == null) {
//                            binding.tvAttendanceHistoryEnabledInStatus.text =
//                                it.data.statusAttendanceIn
//                        } else {
//                            binding.tvAttendanceHistoryEnabledInStatus.text =
//                                it.data.attendanceInfo.scanIn
//                        }
//
//                        binding.tvAttendanceHistoryEnabledOut.text = "Pulang"
//                        binding.tvAttendanceHistoryEnabledIn.text = "Masuk"
//
//                        binding.tvAttendanceHistoryEnabledDatetitle.text = "Hari ini:"
//                        binding.tvAttendanceHistoryEnabledDate.text = ""
//
//                        binding.llAttendanceHistoryAttendanceEnabled.visibility = View.VISIBLE
//                        binding.llAttendanceHistoryAttendanceDisabled.visibility = View.GONE
//                    }
//                    "Bertugas" -> {
//                        binding.llCheckIn.setOnClickListener {
//                            Toast.makeText(
//                                context,
//                                "Anda sudah check in, status anda sedang Bertugas.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//
//                        if (it.data.attendanceInfo.scanOut == "" || it.data.attendanceInfo.scanOut == null) {
//                            binding.tvAttendanceHistoryEnabledOutStatus.text =
//                                it.data.statusAttendanceOut
//                        } else {
//                            binding.tvAttendanceHistoryEnabledOutStatus.text =
//                                it.data.attendanceInfo.scanOut
//                        }
//
//                        if (it.data.attendanceInfo.scanIn == "" || it.data.attendanceInfo.scanIn == null) {
//                            binding.tvAttendanceHistoryEnabledInStatus.text =
//                                it.data.statusAttendanceIn
//                        } else {
//                            binding.tvAttendanceHistoryEnabledInStatus.text =
//                                it.data.attendanceInfo.scanIn
//                        }
//
//                        binding.tvAttendanceHistoryEnabledOut.text = "Pulang"
//                        binding.tvAttendanceHistoryEnabledIn.text = "Masuk"
//
//                        binding.tvAttendanceHistoryEnabledDatetitle.text = "Hari ini:"
//                        binding.tvAttendanceHistoryEnabledDate.text = ""
//
//                        binding.llAttendanceHistoryAttendanceEnabled.visibility = View.VISIBLE
//                        binding.llAttendanceHistoryAttendanceDisabled.visibility = View.GONE
//
//                        binding.llCheckOut.setOnClickListener {
//                            val i =
//                                Intent(context, AttendanceFixMidDailyCheckOutActivity::class.java)
//                            startActivity(i)
//                        }
//                    }
//                }
//            } else {
//                onSNACK(binding.root, "Terjadi kesalahan")
//            }
//        }
//
//        attedanceViewModel.attStatusFail().observe(requireActivity()) {
//            binding.llCheckIn.setOnClickListener {
//                val i = Intent(context, AttendanceFixMidDailyCheckInActivity::class.java)
//                startActivity(i)
//            }
//
//            binding.llCheckOut.setOnClickListener {
//                showDialog(getString(R.string.belum_absen))
//            }
//
//            onSNACK(binding.root, "Belum absen")
//        }
//    }

    //Snack bar kesalahan
    private fun onSNACK(view: View, s: String) {
        val snackbar = Snackbar.make(
            view, s,
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Error", null)
        snackbar.setActionTextColor(resources.getColor(R.color.primary_color))
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(resources.getColor(R.color.primary_color))
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 12f
        snackbar.show()
    }

    //pop up modal
    private fun showDialog(title: String) {
        val dialog = activity?.let { Dialog(it) }
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_not_attendance)
        val body = dialog.findViewById(R.id.tv_body) as TextView
        body.text = title
        val yesBtn = dialog.findViewById(R.id.yesBtn) as ImageView
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}