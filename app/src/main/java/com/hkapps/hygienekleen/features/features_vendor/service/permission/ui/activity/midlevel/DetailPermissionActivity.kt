package com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailPermissionBinding
import com.hkapps.hygienekleen.features.features_vendor.service.permission.viewmodel.PermissionViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*

class DetailPermissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPermissionBinding
    private val permissionId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.PERMISSION_ID, 0)

    private val viewModel: PermissionViewModel by lazy {
        ViewModelProviders.of(this).get(PermissionViewModel::class.java)
    }
    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val intentNotif =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NOTIF_INTENT, "")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.layoutAppbarPermission.tvAppbarTitle.text = "Permohonan Izin"
        binding.layoutAppbarPermission.ivAppbarBack.setOnClickListener {
            if (intentNotif == "notification"){
                CarefastOperationPref.saveString(CarefastOperationPrefConst.NOTIF_INTENT, "")
                setResult(Activity.RESULT_OK, Intent())
                finish()
            } else {
                onBackPressed()
            }
        }

        viewModel.getDetailPermission(permissionId)
        viewModel.getDetailPermission().observe(this) { it ->
            if (it.code == 200) {
                binding.tvReasonPermissionDetail.text = it.data.description
                binding.tvPersonPermissionDetail.text = it.data.employeeName
                binding.tvAreaPermissionDetail.text = it.data.locationName
                binding.tvSubAreaPermissionDetail.text = it.data.subLocationName
                binding.tvTitlePermissionDetail.text = it.data.title
                binding.tvDatePermissionDetail.text = it.data.atDate
                binding.tvShiftPermissionDetail.text =
                    it.data.shiftDescription + " (" + it.data.startAt + "-" + it.data.endAt + ")"
                val u = it.data.image
                val url = getString(R.string.url) + "assets.admin_master/images/permission_image/"

                binding.ivPermissionDetailPreview.visibility = View.VISIBLE
                binding.ivPermissionDetail.visibility = View.GONE
                let {
                    Glide.with(it)
                        .load(url + u)
                        .apply(RequestOptions.fitCenterTransform())
                        .into(binding.ivPermissionDetailPreview)
                }

                val currentDate: String =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                        Date()
                    )

                val permissionId = it.data.permissionEmployeeId
                val shiftId = it.data.shiftId
                val idDetailEmployeeProject = it.data.idDetailEmployeeProject
                val employeePhotoProfile = it.data.employeePhotoProfile

                val urls = getString(R.string.url) + "assets.admin_master/images/photo_profile/"
                let {
                    Glide.with(it)
                        .load(urls + employeePhotoProfile)
                        .apply(RequestOptions.fitCenterTransform())
                        .into(binding.ivPeople)
                }
                when (it.data.statusPermission) {
                    "WAITING" -> {
                        binding.llTolakIzin.visibility = View.VISIBLE
                        binding.tvResultButton.visibility = View.GONE

                        binding.tvTolak.setOnClickListener {
//                            val fragmentManager: FragmentManager =
//                                (this as FragmentActivity).supportFragmentManager
//                            val fragmentTransaction: FragmentTransaction =
//                                fragmentManager.beginTransaction()
//                            val dialog = DenialPermissionDialog()
//                            dialog.show(fragmentTransaction, "ChooseOperatorDialog")
                            showDialogDenial("")
                        }

                        binding.tvIzinkan.setOnClickListener {
//                            CarefastOperationPref.saveString(
//                                CarefastOperationPrefConst.DATE_OPERATOR_PERMISSION,
//                                currentDate
//                            )
//                            CarefastOperationPref.saveInt(
//                                CarefastOperationPrefConst.PERMISSION_ID,
//                                permissionId
//                            )
//
//                            CarefastOperationPref.saveInt(
//                                CarefastOperationPrefConst.ID_SHIFT_OPERATOR,
//                                shiftId
//                            )
//
//                            CarefastOperationPref.saveInt(
//                                CarefastOperationPrefConst.ID_DETAIL_PROJECT_OPERATOR,
//                                idDetailEmployeeProject
//                            )
//                            val fragmentManager: FragmentManager =
//                                (this as FragmentActivity).supportFragmentManager
//                            val fragmentTransaction: FragmentTransaction =
//                                fragmentManager.beginTransaction()
//                            val dialog = ChooseOperatorPermissionDialog()
//                            dialog.show(fragmentTransaction, "ChooseOperatorDialog")
                            showDialogAllow("")
                        }
                    }
                    "REFUSE" -> {
                        binding.llTolakIzin.visibility = View.GONE
                        binding.tvResultButton.visibility = View.VISIBLE

                        binding.tvResultButton.text = "Permohonan ditolak"
                        binding.tvResultButton.background =
                            ContextCompat.getDrawable(this, R.drawable.rounded_ditolak)
                    }
                    "ACCEPT" -> {
                        binding.llTolakIzin.visibility = View.GONE
                        binding.tvResultButton.visibility = View.VISIBLE

                        binding.tvBy.visibility = View.GONE
                        binding.llTvBy.visibility = View.GONE
                        binding.tvPersonPermissionDetailBys.visibility = View.GONE
                        binding.tvPersonPermissionDetailBys.text = it.data.employeeReplaceName

                        binding.tvResultButton.text = "Permohonan disetujui"
                        binding.tvResultButton.background =
                            ContextCompat.getDrawable(this, R.drawable.rounded_izinkan)
                    }
                    else -> {

                    }
                }
            } else {
                Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_LONG).show()
            }
        }
    }

    //pop up modal
    private fun showDialogAllow(title: String) {
        val dialog = let { Dialog(it) }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_permission)
        dialog.show()

        val yesBtn = dialog.findViewById(R.id.btn_yes) as AppCompatButton
        yesBtn.setOnClickListener {
            viewModel.putSubmitOperatorNew(
                permissionId,
                userId,
                projectCode
            )
            val a = Intent(this, DetailPermissionActivity::class.java)
            startActivity(a)
            finishAffinity()
        }
        val noBtn = dialog.findViewById(R.id.btn_no) as AppCompatButton
        noBtn.setOnClickListener {
            dialog.cancel()
        }
    }

    private fun showDialogDenial(title: String) {
        val dialog = let { Dialog(it) }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_permission_denial)
        dialog.show()

        val yesBtn = dialog.findViewById(R.id.btn_yes) as AppCompatButton
        yesBtn.setOnClickListener {
            viewModel.putDenialOperatorNew(
                permissionId
            )
            val a = Intent(this, DetailPermissionActivity::class.java)
            startActivity(a)
            finishAffinity()
        }
        val noBtn = dialog.findViewById(R.id.btn_no) as AppCompatButton
        noBtn.setOnClickListener {
            dialog.cancel()
        }
    }

    override fun onBackPressed() {
        if (intentNotif == "notification"){
            CarefastOperationPref.saveString(CarefastOperationPrefConst.NOTIF_INTENT, "")
            setResult(Activity.RESULT_OK, Intent())
            finish()
        } else {
            super.onBackPressed()
            finish()
        }
    }
}