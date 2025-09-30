package com.hkapps.hygienekleen.features.features_vendor.profile.ui.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivitySettingsBinding
import com.hkapps.hygienekleen.features.auth.login.ui.activity.LoginActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.StatusAbsenViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.firebase.messaging.FirebaseMessaging
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var loadingDialog: Dialog? = null
    private val userNus = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, "")


    private val attedanceViewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }

    private val statusAbsenViewModel: StatusAbsenViewModel by lazy {
        ViewModelProviders.of(this).get(StatusAbsenViewModel::class.java)
    }

    private val statusAttendanceFirst =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_ABSEN_FIRST, "")
    private val statusAttendanceSecond =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_ABSEN_SECOND, "")
    private val statusAttendanceThird =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_ABSEN_THIRD, "")
    private val statusAttendanceFourth =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_ABSEN_FOURTH, "")
    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        //check status absen untuk logout
//        attedanceViewModel.getStatusAttendance(employeeId, projectCode)
//        statusAbsenViewModel.statusAbsenModelMid(employeeId, projectCode)

        binding.tvLogout.setOnClickListener {
            showLoading(getString(R.string.loading_string2))

            fcmNotifUnsubs("RESIGN_APPROVAL_EMPLOYEE_$userNus")

//            fcmNotifUnsubs("Complaint_GM-CEO")
//            fcmNotifUnsubs("Complaint_FM")
            fcmNotifUnsubs("Permission_From_Operator_" + projectCode)
            fcmNotifUnsubs("Operator")
//            fcmNotifUnsubs("Complaint_Leader_" + projectCode)
//            fcmNotifUnsubs("Complaint_Supervisor_" + projectCode)
//            fcmNotifUnsubs("Complaint_Chief_Supervisor_" + projectCode)
            //new operator
            fcmNotifUnsubs("Permission_Operator_" + userId)
            fcmNotifUnsubs("Overtime_Operator_" + userId)
            //new tl
            fcmNotifUnsubs("Permission_TeamLeader_" + userId)
            fcmNotifUnsubs("Overtime_TeamLeader_" + userId)
            fcmNotifUnsubs("Permission_From_Operator_" + projectCode)
//            fcmNotifUnsubs("Complaint_Visitor_$projectCode")

            //new spv
            fcmNotifUnsubs("Permission_Supervisor_" + userId)
            fcmNotifUnsubs("Overtime_Supervisor_" + userId)
            fcmNotifUnsubs("Permission_From_Operator_" + projectCode)
            fcmNotifUnsubs("Permission_From_TeamLeader_" + projectCode)
//            fcmNotifUnsubs("Complaint_Visitor_$projectCode")

            //new cspv
            fcmNotifUnsubs("Permission_ChiefSupervisor_" + userId)
            fcmNotifUnsubs("Overtime_Chiefupervisor_" + userId)
            fcmNotifUnsubs("Permission_From_Operator_" + projectCode)
            fcmNotifUnsubs("Permission_From_Supervisor_" + projectCode)
//            fcmNotifUnsubs("Complaint_Visitor_$projectCode")


            CarefastOperationPref.logout()

            Handler(Looper.getMainLooper()).postDelayed( {
                hideLoading()
                val i = Intent(this, LoginActivity::class.java)
                finishAffinity()
                startActivity(i)
            }, 2000)

//            if (statusAttendanceFirst == "Selesai" || statusAttendanceFirst == "SELESAI" || statusAttendanceSecond == "Selesai" || statusAttendanceSecond == "SELESAI" || statusAttendanceThird == "Selesai" || statusAttendanceThird == "SELESAI" || statusAttendanceFourth == "Selesai" || statusAttendanceFourth == "SELESAI") {
//                CarefastOperationPref.logout()
//                val i = Intent(this, LoginActivity::class.java)
//                finishAffinity()
//                startActivity(i)
//            } else if (statusAttendanceFirst == "Belum Absen" || statusAttendanceFirst == "Belum absen" || statusAttendanceFirst == "BELUM ABSEN" || statusAttendanceSecond == "Belum Absen" || statusAttendanceSecond == "Belum absen" || statusAttendanceSecond == "BELUM ABSEN"
//                || statusAttendanceThird == "Belum Absen" || statusAttendanceThird == "Belum absen" || statusAttendanceThird == "BELUM ABSEN" || statusAttendanceFourth == "Belum Absen" || statusAttendanceFourth == "Belum absen" || statusAttendanceFourth == "BELUM ABSEN"
//            ) {
//                CarefastOperationPref.logout()
//                val i = Intent(this, LoginActivity::class.java)
//                finishAffinity()
//                startActivity(i)
//            } else if (statusAttendanceFirst == "Tidak ada jadwal") {
//                CarefastOperationPref.logout()
//                val i = Intent(this, LoginActivity::class.java)
//                finishAffinity()
//                startActivity(i)
//            } else if (statusAttendanceFirst == "OFF DAY" || statusAttendanceFirst == "Off Day" || statusAttendanceFirst == "Off day" || statusAttendanceSecond == "OFF DAY" || statusAttendanceSecond == "Off Day" || statusAttendanceSecond == "Off day") {
//                CarefastOperationPref.logout()
//                val i = Intent(this, LoginActivity::class.java)
//                finishAffinity()
//                startActivity(i)
//            } else {
//                Toast.makeText(
//                    this,
//                    "Anda hanya dapat melakukan logout, ketika status anda tidak Bertugas.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
        }


//        attedanceViewModel.attStatus.observe(this) { it ->
//            when (it.code) {
//                200 -> {
//                    //get status nya disini
//                    when (it.data.status) {
//                        "Belum Absen" -> {
//                            binding.tvLogout.setOnClickListener {
//                                CarefastOperationPref.logout()
//                                val i = Intent(this, LoginActivity::class.java)
//                                finishAffinity()
//                                startActivity(i)
//                            }
//                        }
//
//                        "Selesai" -> {
//                            binding.tvLogout.setOnClickListener {
//                                CarefastOperationPref.logout()
//                                val i = Intent(this, LoginActivity::class.java)
//                                finishAffinity()
//                                startActivity(i)
//                            }
//                        }
//
//                        "Bertugas" -> {
//                            binding.tvLogout.setOnClickListener {
//                                Toast.makeText(
//                                    this,
//                                    "Status absen anda masih Bertugas. Anda tidak dapat melakukan logout",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//
//                        "Off" -> {
//                            binding.tvLogout.setOnClickListener {
//                                binding.tvLogout.setOnClickListener {
//                                    CarefastOperationPref.logout()
//                                    val i = Intent(this, LoginActivity::class.java)
//                                    finishAffinity()
//                                    startActivity(i)
//                                }
//                            }
//                        }
//                    }
//                }
//
//                400 -> {
//                    statusAbsenViewModel.statusAbsenModelMid().observe(this) {
//                        when (it.code) {
//                            200 -> {
//                                //get status nya disini
//                                when (it.data.statusAttendance) {
//                                    "Belum absen" -> {
//                                        binding.tvLogout.setOnClickListener {
//                                            CarefastOperationPref.logout()
//                                            val i = Intent(this, LoginActivity::class.java)
//                                            finishAffinity()
//                                            startActivity(i)
//                                        }
//                                    }
//
//                                    "Selesai" -> {
//                                        binding.tvLogout.setOnClickListener {
//                                            CarefastOperationPref.logout()
//                                            val i = Intent(this, LoginActivity::class.java)
//                                            finishAffinity()
//                                            startActivity(i)
//                                        }
//                                    }
//
//                                    "Bertugas" -> {
//                                        binding.tvLogout.setOnClickListener {
//                                            Toast.makeText(
//                                                this,
//                                                "Status absen anda masih Bertugas. Anda tidak dapat melakukan logout",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                    }
//
//                                    "Off Day" -> {
//                                        binding.tvLogout.setOnClickListener {
//                                            binding.tvLogout.setOnClickListener {
//                                                CarefastOperationPref.logout()
//                                                val i = Intent(this, LoginActivity::class.java)
//                                                finishAffinity()
//                                                startActivity(i)
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            else -> {
//                                binding.tvLogout.setOnClickListener {
//                                    Toast.makeText(
//                                        this,
//                                        "Anda hanya dapat melakukan logout, ketika status anda Bertugas atau Selesai.",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                            }
//                        }
//                    }
//                }
//
//                else -> {
//                    binding.tvLogout.setOnClickListener {
//                        Toast.makeText(
//                            this,
//                            "Anda hanya dapat melakukan logout, ketika status anda Bertugas atau Selesai.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//        }


//        binding.tvEditProfile.setOnClickListener {
//            val i = Intent(this, EditProfileActivity::class.java)
//            startActivity(i)
//        }

        binding.tvChangepassProfile.setOnClickListener {
            val i = Intent(this, ChangePass::class.java)
            startActivity(i)
        }

//        binding.tvCertiProfile.setOnClickListener {
//            Toast.makeText(this, "Fitur belum tersedia.", Toast.LENGTH_SHORT).show()
//        }

        binding.layoutAppbar.tvAppbarTitle.text = "Setting"

    }

    fun fcmNotifUnsubs(topic: String) {
        Log.d("FCM", "fcm")
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener { task ->
            var msg = ""
            msg = if (!task.isSuccessful) {
                "failed"
            } else {
                "success"
            }
            Log.d("FCM", msg)
            //                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

}