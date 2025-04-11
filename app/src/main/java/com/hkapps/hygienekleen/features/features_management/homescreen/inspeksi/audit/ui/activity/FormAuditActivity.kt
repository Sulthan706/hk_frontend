package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityFormAuditBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.viewmodel.AuditViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.activity.InspeksiMainActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import java.text.SimpleDateFormat
import java.util.*

class FormAuditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormAuditBinding
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.M_CLICK_AUDIT, "")
    private val periode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PERIOD_AUDIT, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
    private val idReport = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_REPORT_AUDIT, 0)
    private var date = ""
    private var day = ""
    private var monthss = ""
    private var year = ""
    private var auditL1 = false
    private var auditL2 = false
    private var auditL3 = false
    private var detailAuditL1 = false
    private var detailAuditL2 = false
    private var detailAuditL3 = false
    private var idAuditKualitasL1 = 0
    private var idAuditKualitasL2 = 0
    private var idAuditKualitasL3 = 0
    private var loadingDialog: Dialog? = null
    private var flag = 1
    private var reloadedNeeded = true

    private val viewModel: AuditViewModel by lazy {
        ViewModelProviders.of(this).get(AuditViewModel::class.java)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAuditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarFormAudit.tvAppbarTitle.text = "Form Audit"
        binding.appbarFormAudit.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // get current date
        val dates = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        date = dateFormat.format(dates.time)

        day = android.text.format.DateFormat.format("dd", dates) as String
        val month = android.text.format.DateFormat.format("MMMM", dates) as String
        year = android.text.format.DateFormat.format("yyyy", dates) as String

        if (month == "January" || month == "Januari") {
            monthss = "Januari"
        } else if (month == "February" || month == "Februari") {
            monthss = "Februari"
        } else if (month == "March" || month == "Maret") {
            monthss = "Maret"
        } else if (month == "April" || month == "April") {
            monthss = "April"
        } else if (month == "May" || month == "Mei") {
            monthss = "Mei"
        } else if (month == "June" || month == "Juni") {
            monthss = "Juni"
        } else if (month == "July" || month == "Juli") {
            monthss = "Juli"
        } else if (month == "August" || month == "Agustus") {
            monthss = "Agustus"
        } else if (month == "September" || month == "September") {
            monthss = "September"
        } else if (month == "October" || month == "Oktober") {
            monthss = "Oktober"
        } else if (month == "November" || month == "Nopember") {
            monthss = "Nopember"
        } else if (month == "December" || month == "Desember") {
            monthss = "Desember"
        }

        // set on click button submit
        binding.btnSubmitEnableFormKontrolArea.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitEnableFormKontrolArea.isEnabled = false
                showDialogSubmitConfirmation()
            }
            flag = 0
        }

        // set on click form audit
        if (clickFrom == "detailAudit") {
            binding.rlAuditL1FormAudit.setOnClickListener {
                if (detailAuditL1) {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.AUDIT_TYPE, "L1")
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_AUDIT_KUALITAS, idAuditKualitasL1)
                    startActivityForResult(Intent(this, ListQualityAuditActivity::class.java), CREATE_CODE)
                } else {
                    Toast.makeText(this, "Anda tidak mengisi form L1 saat melakukan audit", Toast.LENGTH_SHORT).show()
                }
            }
            binding.rlAuditL2FormAudit.setOnClickListener {
                if (detailAuditL2) {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.AUDIT_TYPE, "L2")
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_AUDIT_KUALITAS, idAuditKualitasL2)
                    startActivityForResult(Intent(this, ListQualityAuditActivity::class.java), CREATE_CODE)
                } else {
                    Toast.makeText(this, "Anda tidak mengisi form L2 saat melakukan audit", Toast.LENGTH_SHORT).show()
                }
            }
            binding.rlAuditL3FormAudit.setOnClickListener {
                if (detailAuditL3) {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.AUDIT_TYPE, "L3")
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_AUDIT_KUALITAS, idAuditKualitasL3)
                    startActivityForResult(Intent(this, ListQualityAuditActivity::class.java), CREATE_CODE)
                } else {
                    Toast.makeText(this, "Anda tidak mengisi form L3 saat melakukan audit", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.rlAuditL1FormAudit.setOnClickListener {
                if (!auditL1) {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.AUDIT_TYPE, "L1")
                    startActivityForResult(Intent(this, ListQualityAuditActivity::class.java), CREATE_CODE)
                } else {
                    Toast.makeText(this, "Form L1 sudah diisi saat melakukan audit", Toast.LENGTH_SHORT).show()
                }
            }
            binding.rlAuditL2FormAudit.setOnClickListener {
                if (!auditL2) {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.AUDIT_TYPE, "L2")
                    startActivityForResult(Intent(this, ListQualityAuditActivity::class.java), CREATE_CODE)
                } else {
                    Toast.makeText(this, "Form L2 sudah diisi saat melakukan audit", Toast.LENGTH_SHORT).show()
                }
            }
            binding.rlAuditL3FormAudit.setOnClickListener {
                if (!auditL3) {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.AUDIT_TYPE, "L3")
                    startActivityForResult(Intent(this, ListQualityAuditActivity::class.java), CREATE_CODE)
                } else {
                    Toast.makeText(this, "Form L3 sudah diisi saat melakukan audit", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loadData()
        setObserver()
    }

    private fun loadData() {
        if (clickFrom == "detailAudit") {
            viewModel.getDetailAudit(idReport)
        } else {
            viewModel.getListFormAudit(userId, projectCode, date)
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.listFormAuditResponse.observe(this) {
            if (it.code == 200) {
                // set data
                binding.tvBranchFormAudit.text = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_NAME_LAST_VISIT, "")
                binding.tvProjectFormAudit.text = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")
                binding.tvDateFormAudit.text = "$day $monthss $year"
                binding.tvAuditorFormAudit.text = userName
                binding.tvPeriodeFormAudit.text = periode

                // validate audit L1
                auditL1 = it.data.auditL1IsScored
                if (it.data.auditL1IsScored) {
                    binding.rlAuditL1FormAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.ivAuditL1FormAudit.setImageResource(R.drawable.ic_note_green)
                    binding.tvAuditL1FormAudit.setTextColor(resources.getColor(R.color.green2))
                    binding.tvInfoAuditL1FormAudit.setTextColor(resources.getColor(R.color.green2))

                } else {
                    binding.rlAuditL1FormAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.ivAuditL1FormAudit.setImageResource(R.drawable.ic_note_grey)
                    binding.tvAuditL1FormAudit.setTextColor(resources.getColor(R.color.grey2_client))
                    binding.tvInfoAuditL1FormAudit.setTextColor(resources.getColor(R.color.grayTxt))

                }

                // validate audit L2
                auditL2 = it.data.auditL2IsScored
                if (it.data.auditL2IsScored) {
                    binding.rlAuditL2FormAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.ivAuditL2FormAudit.setImageResource(R.drawable.ic_note_green)
                    binding.tvAuditL2FormAudit.setTextColor(resources.getColor(R.color.green2))
                    binding.tvInfoAuditL2FormAudit.setTextColor(resources.getColor(R.color.green2))

                } else {
                    binding.rlAuditL2FormAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.ivAuditL2FormAudit.setImageResource(R.drawable.ic_note_grey)
                    binding.tvAuditL2FormAudit.setTextColor(resources.getColor(R.color.grey2_client))
                    binding.tvInfoAuditL2FormAudit.setTextColor(resources.getColor(R.color.grayTxt))

                }

                // validate audit L3
                auditL3 = it.data.auditL3IsScored
                if (it.data.auditL3IsScored) {
                    binding.rlAuditL3FormAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.ivAuditL3FormAudit.setImageResource(R.drawable.ic_note_green)
                    binding.tvAuditL3FormAudit.setTextColor(resources.getColor(R.color.green2))
                    binding.tvInfoAuditL3FormAudit.setTextColor(resources.getColor(R.color.green2))

                } else {
                    binding.rlAuditL3FormAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.ivAuditL3FormAudit.setImageResource(R.drawable.ic_note_grey)
                    binding.tvAuditL3FormAudit.setTextColor(resources.getColor(R.color.grey2_client))
                    binding.tvInfoAuditL3FormAudit.setTextColor(resources.getColor(R.color.grayTxt))

                }

                // validate button submit
                if (!it.data.auditL1IsScored && !it.data.auditL2IsScored && !it.data.auditL3IsScored) {
                    binding.btnSubmitDisableFormKontrolArea.visibility = View.VISIBLE
                    binding.btnSubmitEnableFormKontrolArea.visibility = View.INVISIBLE
                } else if (it.data.auditL1IsScored || it.data.auditL2IsScored || it.data.auditL3IsScored) {
                    binding.btnSubmitDisableFormKontrolArea.visibility = View.GONE
                    binding.btnSubmitEnableFormKontrolArea.visibility = View.VISIBLE
                } else {
                    binding.btnSubmitDisableFormKontrolArea.visibility = View.VISIBLE
                    binding.btnSubmitEnableFormKontrolArea.visibility = View.INVISIBLE
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data list form audit", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.submitFormAuditResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                showDialogSuccessSubmit()
            } else {
                hideLoading()
                flag = 1
                binding.btnSubmitEnableFormKontrolArea.isEnabled = true
                Toast.makeText(this, "Gagal submit form audit", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.detailAuditResponse.observe(this) {
            if (it.code == 200) {
                // validate button submit
                binding.btnSubmitEnableFormKontrolArea.visibility = View.INVISIBLE
                binding.btnSubmitDisableFormKontrolArea.visibility = View.GONE

                // set data
                binding.tvBranchFormAudit.text = it.data.branchName
                binding.tvProjectFormAudit.text = it.data.projectName
                binding.tvDateFormAudit.text = it.data.date
                binding.tvAuditorFormAudit.text = it.data.auditCreatorName
                binding.tvPeriodeFormAudit.text = it.data.periodDate

                // validate audit L1
                detailAuditL1 = it.data.l1Checked
                idAuditKualitasL1 = it.data.l1Id
                if (it.data.l1Checked) {
                    binding.rlAuditL1FormAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.ivAuditL1FormAudit.setImageResource(R.drawable.ic_note_green)
                    binding.tvAuditL1FormAudit.setTextColor(resources.getColor(R.color.green2))
                    binding.tvInfoAuditL1FormAudit.setTextColor(resources.getColor(R.color.green2))
                } else {
                    binding.rlAuditL1FormAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.ivAuditL1FormAudit.setImageResource(R.drawable.ic_note_grey)
                    binding.tvAuditL1FormAudit.setTextColor(resources.getColor(R.color.grey2_client))
                    binding.tvInfoAuditL1FormAudit.setTextColor(resources.getColor(R.color.grayTxt))

                }

                // validate audit L2
                detailAuditL2 = it.data.l2Checked
                idAuditKualitasL2 = it.data.l2Id
                if (it.data.l2Checked) {
                    binding.rlAuditL2FormAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.ivAuditL2FormAudit.setImageResource(R.drawable.ic_note_green)
                    binding.tvAuditL2FormAudit.setTextColor(resources.getColor(R.color.green2))
                    binding.tvInfoAuditL2FormAudit.setTextColor(resources.getColor(R.color.green2))
                } else {
                    binding.rlAuditL2FormAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.ivAuditL2FormAudit.setImageResource(R.drawable.ic_note_grey)
                    binding.tvAuditL2FormAudit.setTextColor(resources.getColor(R.color.grey2_client))
                    binding.tvInfoAuditL2FormAudit.setTextColor(resources.getColor(R.color.grayTxt))
                }

                // validate audit L3
                detailAuditL3 = it.data.l3Checked
                idAuditKualitasL3 = it.data.l3Id
                if (it.data.l3Checked) {
                    binding.rlAuditL3FormAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.ivAuditL3FormAudit.setImageResource(R.drawable.ic_note_green)
                    binding.tvAuditL3FormAudit.setTextColor(resources.getColor(R.color.green2))
                    binding.tvInfoAuditL3FormAudit.setTextColor(resources.getColor(R.color.green2))

                } else {
                    binding.rlAuditL3FormAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.ivAuditL3FormAudit.setImageResource(R.drawable.ic_note_grey)
                    binding.tvAuditL3FormAudit.setTextColor(resources.getColor(R.color.grey2_client))
                    binding.tvInfoAuditL3FormAudit.setTextColor(resources.getColor(R.color.grayTxt))

                }
            }
        }
    }

    private fun showDialogBackWarning() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_warning_back_audit)
        val btnBack = dialog.findViewById<AppCompatButton>(R.id.btnContinueDialogWarningBackAudit)
        val btnOke = dialog.findViewById<AppCompatButton>(R.id.btnYesDialogWarningBackAudit)

        btnBack?.setOnClickListener {
            dialog.dismiss()
        }

        btnOke?.setOnClickListener {
            viewModel.deleteLaporanAudit(userId, projectCode, date)
            viewModel.deleteLaporanAuditResponse.observe(this) {
                if (it.code == 200) {
                    dialog.dismiss()
                    super.onBackPressed()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menghapus laporan audit", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }

    private fun showDialogSubmitConfirmation() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_warning_submit_audit)
        val btnBack = dialog.findViewById<AppCompatButton>(R.id.btnContinueDialogWarningSubmitAudit)
        val btnOke = dialog.findViewById<AppCompatButton>(R.id.btnYesDialogWarningSubmitAudit)

        btnBack?.setOnClickListener {
            dialog.dismiss()
        }

        var flags = 1
        btnOke?.setOnClickListener {
            if (flags == 1) {
                dialog.dismiss()
                showLoading(getString(R.string.loading_string2))
            }
            flags = 0
        }

        dialog.show()
    }

    private fun showDialogSuccessSubmit() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_success_submit_audit)
        val tvDate = dialog.findViewById<TextView>(R.id.tvDateDialogSuccessSubmitAudit)
        val tvPeriod = dialog.findViewById<TextView>(R.id.tvPeriodeDialogSuccessSubmitAudit)
        val tvName = dialog.findViewById<TextView>(R.id.tvNameDialogSuccessSubmitAudit)
        val tvL1 = dialog.findViewById<TextView>(R.id.tvL1DialogSuccessSubmitAudit)
        val tvL2 = dialog.findViewById<TextView>(R.id.tvL2DialogSuccessSubmitAudit)
        val tvL3 = dialog.findViewById<TextView>(R.id.tvL3DialogSuccessSubmitAudit)
        val btnLaporan = dialog.findViewById<AppCompatButton>(R.id.btnLaporanDialogSuccessSubmitAudit)
        val btnClose = dialog.findViewById<AppCompatButton>(R.id.btnCloseDialogSuccessSubmitAudit)

        tvDate.text = date
        tvPeriod.text = periode
        tvName.text = userName

        if (auditL1) {
            tvL1.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_done_circle_green, 0, 0, 0)
        } else {
            tvL1.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_chooser, 0, 0, 0)
        }
        if (auditL2) {
            tvL2.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_done_circle_green, 0, 0, 0)
        } else {
            tvL2.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_chooser, 0, 0, 0)
        }
        if (auditL3) {
            tvL3.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_done_circle_green, 0, 0, 0)
        } else {
            tvL3.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_chooser, 0, 0, 0)
        }

        btnClose?.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, InspeksiMainActivity::class.java))
            finish()
        }

        btnLaporan?.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, ListLaporanAuditActivity::class.java))
            finish()
        }

        dialog.show()
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        viewModel.submitFormAudit(userId, projectCode, periode, date)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                this.reloadedNeeded = true
            }
        }
    }

    companion object {
        private const val CREATE_CODE = 31
    }

    override fun onResume() {
        super.onResume()
        if (this.reloadedNeeded) {
            loadData()
        }
        this.reloadedNeeded = false
    }

    override fun onPause() {
        super.onPause()
        this.reloadedNeeded = true
    }

    override fun onBackPressed() {
        if (clickFrom == "detailAudit") {
            setResult(RESULT_OK, Intent())
            finish()
        } else {
            if (!auditL1 && !auditL2 && !auditL3) {
                setResult(RESULT_OK, Intent())
                finish()
            } else if (auditL1 || auditL2 || auditL3) {
                showDialogBackWarning()
            } else {
                setResult(RESULT_OK, Intent())
                finish()
            }
        }
    }
}