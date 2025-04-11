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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListQualityAuditBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.viewmodel.AuditViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ListChooseInspeksiAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListQualityAuditActivity : AppCompatActivity(), ListChooseInspeksiAdapter.OnItemSelectedCallBack {

    private lateinit var binding: ActivityListQualityAuditBinding
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.M_CLICK_AUDIT, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
    private val auditType = CarefastOperationPref.loadString(CarefastOperationPrefConst.AUDIT_TYPE, "")
    private val idAuditKualitas = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_AUDIT_KUALITAS, 0)
    private var date = ""
    private var loadingDialog: Dialog? = null
    private var flag = 1
    private var manpower = false
    private var organization = false
    private var managementProject = false
    private var materialStock = false
    private var administration = false
    private var workResult = false
    private var resultScore = ""
    private val questionType = ArrayList<String>()
    private var reloadedNeeded = true

    private val viewModel: AuditViewModel by lazy {
        ViewModelProviders.of(this).get(AuditViewModel::class.java)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListQualityAuditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarListQualityAudit.tvAppbarTitle.text = "Form Audit $auditType"
        binding.appbarListQualityAudit.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // get current date
        val dates = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        date = dateFormat.format(dates.time)

        // set visibility hasil kerja
        if (auditType == "L3") {
            binding.tvWorkResultListQualityAudit.visibility = View.GONE
        } else {
            binding.tvWorkResultListQualityAudit.visibility = View.VISIBLE
        }

        // set on click button submit
        binding.btnSubmitEnableListQualityAudit.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitEnableListQualityAudit.isEnabled = false
                showLoading(getString(R.string.loading_string2))
            }
            flag = 0
        }

        loadData()
        setObserver()
    }

    private fun loadData() {
        if (clickFrom == "detailAudit") {
            viewModel.getDetailAuditKualitas(idAuditKualitas)
        } else {
            viewModel.getListQualityAudit(userId, projectCode, auditType, date)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.listQualityAuditResponse.observe(this) {
            if (it.code == 200) {
                // validate hasil audit text
                binding.tvAuditResultListQualityAudit.setOnClickListener {
                    bottomSheetChooseAuditResult()
                }
                if (resultScore == "") {
                    binding.tvAuditResultListQualityAudit.text = "Pilih hasil audit"
                    binding.tvAuditResultListQualityAudit.setTextColor(resources.getColor(R.color.grayLine))
                } else {
                    binding.tvAuditResultListQualityAudit.text = resultScore
                    binding.tvAuditResultListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))
                }

                // manpower
                manpower = it.data.manpowerIsScored
                if (it.data.manpowerIsScored) {
                    binding.tvManpowerListQualityAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.tvManpowerListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_manpower_green, 0, 0, 0
                    )
                    binding.tvManpowerListQualityAudit.setTextColor(resources.getColor(R.color.green2))

                    questionType.add("MANPOWER")

                } else {
                    binding.tvManpowerListQualityAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.tvManpowerListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_manpower_grey, 0, 0, 0
                    )
                    binding.tvManpowerListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.tvManpowerListQualityAudit.setOnClickListener {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.QUESTION_TYPE,
                            "MANPOWER"
                        )
                        startActivityForResult(
                            Intent(
                                this,
                                ListQuestionAuditActivity::class.java
                            ), CREATE_CODE
                        )
                    }
                }

                // organisasi
                organization = it.data.organisasiIsScored
                if (it.data.organisasiIsScored) {
                    binding.tvOrganizationListQualityAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.tvOrganizationListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_organization_green, 0, 0, 0
                    )
                    binding.tvOrganizationListQualityAudit.setTextColor(resources.getColor(R.color.green2))
                    questionType.add("ORGANISASI")

                } else {
                    binding.tvOrganizationListQualityAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.tvOrganizationListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_organization_grey, 0, 0, 0
                    )
                    binding.tvOrganizationListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.tvOrganizationListQualityAudit.setOnClickListener {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.QUESTION_TYPE,
                            "ORGANISASI"
                        )
                        startActivityForResult(
                            Intent(
                                this,
                                ListQuestionAuditActivity::class.java
                            ), CREATE_CODE
                        )
                    }
                }

                // manajemen project
                managementProject = it.data.manajemenProjectIsScored
                if (it.data.manajemenProjectIsScored) {
                    binding.tvManagementProjectListQualityAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.tvManagementProjectListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_management_project_green, 0, 0, 0
                    )
                    binding.tvManagementProjectListQualityAudit.setTextColor(resources.getColor(R.color.green2))
                    questionType.add("MANAJEMEN_PROJECT")

                } else {
                    binding.tvManagementProjectListQualityAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.tvManagementProjectListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_management_project_grey, 0, 0, 0
                    )
                    binding.tvManagementProjectListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.tvManagementProjectListQualityAudit.setOnClickListener {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.QUESTION_TYPE,
                            "MANAJEMEN_PROJECT"
                        )
                        startActivityForResult(
                            Intent(
                                this,
                                ListQuestionAuditActivity::class.java
                            ), CREATE_CODE
                        )
                    }
                }

                // material & stock
                materialStock = it.data.materialStockIsScored
                if (it.data.materialStockIsScored) {
                    binding.tvMaterialStockListQualityAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.tvMaterialStockListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_material_stock_green, 0, 0, 0
                    )
                    binding.tvMaterialStockListQualityAudit.setTextColor(resources.getColor(R.color.green2))
                    questionType.add("MATERIAL_STOCK")

                } else {
                    binding.tvMaterialStockListQualityAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.tvMaterialStockListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_material_stock_grey, 0, 0, 0
                    )
                    binding.tvMaterialStockListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.tvMaterialStockListQualityAudit.setOnClickListener {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.QUESTION_TYPE,
                            "MATERIAL_STOCK"
                        )
                        startActivityForResult(
                            Intent(
                                this,
                                ListQuestionAuditActivity::class.java
                            ), CREATE_CODE
                        )
                    }
                }

                // administrasi
                administration = it.data.administrasiIsScored
                if (it.data.administrasiIsScored) {
                    binding.tvAdministrationListQualityAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.tvAdministrationListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_administration_green, 0, 0, 0
                    )
                    binding.tvAdministrationListQualityAudit.setTextColor(resources.getColor(R.color.green2))
                    questionType.add("ADMINISTRASI")

                } else {
                    binding.tvAdministrationListQualityAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.tvAdministrationListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_administration_grey, 0, 0, 0
                    )
                    binding.tvAdministrationListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.tvAdministrationListQualityAudit.setOnClickListener {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.QUESTION_TYPE,
                            "ADMINISTRASI"
                        )
                        startActivityForResult(
                            Intent(
                                this,
                                ListQuestionAuditActivity::class.java
                            ), CREATE_CODE
                        )
                    }
                }

                // hasil kerja
                workResult = it.data.hasilKerjaIsScored
                if (it.data.hasilKerjaIsScored) {
                    binding.tvWorkResultListQualityAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.tvWorkResultListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_work_result_green, 0, 0, 0
                    )
                    binding.tvWorkResultListQualityAudit.setTextColor(resources.getColor(R.color.green2))
                    questionType.add("HASIL_KERJA")

                } else {
                    binding.tvWorkResultListQualityAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.tvWorkResultListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_work_result_grey, 0, 0, 0
                    )
                    binding.tvWorkResultListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.tvWorkResultListQualityAudit.setOnClickListener {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.QUESTION_TYPE,
                            "HASIL_KERJA"
                        )
                        startActivityForResult(
                            Intent(
                                this,
                                ListQuestionAuditActivity::class.java
                            ), CREATE_CODE
                        )
                    }
                }

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.submitResultQualityAuditResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                setResult(RESULT_OK, Intent())
                finish()
            } else {
                hideLoading()
                flag = 1
                binding.btnSubmitEnableListQualityAudit.isEnabled = true
                Toast.makeText(this, "Gagal submit kualitas audit", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.detailAuditKualitasResponse.observe(this) {
            if (it.code == 200) {
                // set result audit score
                binding.tvAuditResultListQualityAudit.text = resultScore
                binding.tvAuditResultListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                // manpower
                if (it.data.manpowerIsScored) {
                    binding.tvManpowerListQualityAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.tvManpowerListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_manpower_green, 0, 0, 0
                    )
                    binding.tvManpowerListQualityAudit.setTextColor(resources.getColor(R.color.green2))


                    binding.tvManpowerListQualityAudit.setOnClickListener {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.QUESTION_TYPE,
                            "MANPOWER"
                        )
                        startActivityForResult(
                            Intent(
                                this,
                                ListQuestionAuditActivity::class.java
                            ), CREATE_CODE
                        )
                    }
                } else {
                    binding.tvManpowerListQualityAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.tvManpowerListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_manpower_grey, 0, 0, 0
                    )
                    binding.tvManpowerListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.tvManpowerListQualityAudit.setOnClickListener {
                        Toast.makeText(
                            this,
                            "Anda tidak mengisi form ini saat melakukan audit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                // organisasi
                if (it.data.organisasiIsScored) {
                    binding.tvOrganizationListQualityAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.tvOrganizationListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_organization_green, 0, 0, 0
                    )
                    binding.tvOrganizationListQualityAudit.setTextColor(resources.getColor(R.color.green2))

                    binding.tvOrganizationListQualityAudit.setOnClickListener {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.QUESTION_TYPE,
                            "ORGANISASI"
                        )
                        startActivityForResult(
                            Intent(
                                this,
                                ListQuestionAuditActivity::class.java
                            ), CREATE_CODE
                        )
                    }
                } else {
                    binding.tvOrganizationListQualityAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.tvOrganizationListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_organization_grey, 0, 0, 0
                    )
                    binding.tvOrganizationListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.tvOrganizationListQualityAudit.setOnClickListener {
                        Toast.makeText(
                            this,
                            "Anda tidak mengisi form ini saat melakukan audit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                // manajemen project
                if (it.data.manajemenProjectIsScored) {
                    binding.tvManagementProjectListQualityAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.tvManagementProjectListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_management_project_green, 0, 0, 0
                    )
                    binding.tvManagementProjectListQualityAudit.setTextColor(resources.getColor(R.color.green2))

                    binding.tvManagementProjectListQualityAudit.setOnClickListener {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.QUESTION_TYPE,
                            "MANAJEMEN_PROJECT"
                        )
                        startActivityForResult(
                            Intent(
                                this,
                                ListQuestionAuditActivity::class.java
                            ), CREATE_CODE
                        )
                    }
                } else {
                    binding.tvManagementProjectListQualityAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.tvManagementProjectListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_management_project_grey, 0, 0, 0
                    )
                    binding.tvManagementProjectListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.tvManagementProjectListQualityAudit.setOnClickListener {
                        Toast.makeText(
                            this,
                            "Anda tidak mengisi form ini saat melakukan audit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                // material & stock
                if (it.data.materialStockIsScored) {
                    binding.tvMaterialStockListQualityAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.tvMaterialStockListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_material_stock_green, 0, 0, 0
                    )
                    binding.tvMaterialStockListQualityAudit.setTextColor(resources.getColor(R.color.green2))

                    binding.tvMaterialStockListQualityAudit.setOnClickListener {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.QUESTION_TYPE,
                            "MATERIAL_STOCK"
                        )
                        startActivityForResult(
                            Intent(
                                this,
                                ListQuestionAuditActivity::class.java
                            ), CREATE_CODE
                        )
                    }
                } else {
                    binding.tvMaterialStockListQualityAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.tvMaterialStockListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_material_stock_grey, 0, 0, 0
                    )
                    binding.tvMaterialStockListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.tvMaterialStockListQualityAudit.setOnClickListener {
                        Toast.makeText(
                            this,
                            "Anda tidak mengisi form ini saat melakukan audit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                // administrasi
                if (it.data.administrasiIsScored) {
                    binding.tvAdministrationListQualityAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.tvAdministrationListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_administration_green, 0, 0, 0
                    )
                    binding.tvAdministrationListQualityAudit.setTextColor(resources.getColor(R.color.green2))

                    binding.tvAdministrationListQualityAudit.setOnClickListener {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.QUESTION_TYPE,
                            "ADMINISTRASI"
                        )
                        startActivityForResult(
                            Intent(
                                this,
                                ListQuestionAuditActivity::class.java
                            ), CREATE_CODE
                        )
                    }
                } else {
                    binding.tvAdministrationListQualityAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.tvAdministrationListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_administration_grey, 0, 0, 0
                    )
                    binding.tvAdministrationListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.tvAdministrationListQualityAudit.setOnClickListener {
                        Toast.makeText(
                            this,
                            "Anda tidak mengisi form ini saat melakukan audit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                // hasil kerja
                if (it.data.hasilKerjaIsScored) {
                    binding.tvWorkResultListQualityAudit.setBackgroundResource(R.drawable.bg_field_green)
                    binding.tvWorkResultListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_work_result_green, 0, 0, 0
                    )
                    binding.tvWorkResultListQualityAudit.setTextColor(resources.getColor(R.color.green2))

                    binding.tvWorkResultListQualityAudit.setOnClickListener {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.QUESTION_TYPE,
                            "HASIL_KERJA"
                        )
                        startActivityForResult(
                            Intent(
                                this,
                                DetailsKondisiAreaActivity::class.java
                            ), CREATE_CODE
                        )
                    }
                } else {
                    binding.tvWorkResultListQualityAudit.setBackgroundResource(R.drawable.bg_field)
                    binding.tvWorkResultListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_work_result_grey, 0, 0, 0
                    )
                    binding.tvWorkResultListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.tvWorkResultListQualityAudit.setOnClickListener {
                        Toast.makeText(
                            this,
                            "Anda tidak mengisi form ini saat melakukan audit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                // set data result audit
                binding.tvAuditResultListQualityAudit.text = it.data.resultScore
                binding.tvAuditResultListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))
                binding.tvAuditResultListQualityAudit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, 0, 0
                )

                // visibility button submit
                binding.btnSubmitEnableListQualityAudit.visibility = View.INVISIBLE
                binding.btnSubmitDisableListQualityAudit.visibility = View.GONE

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bottomSheetChooseAuditResult() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_default_choose)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomDefaultChoose)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitleBottomDefaultChoose)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfoBottomDefaultChoose)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvBottomDefaultChoose)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnAppliedBottomDefaultChoose)

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }

        tvTitle?.text = "Hasil Audit"
        tvInfo?.text = "Pilih hasil audit berdasarkan penilaian di area"

        val listChooseInspeksi = ArrayList<String>()
        listChooseInspeksi.add("Melebihi Ekspektasi/Ekselen")
        listChooseInspeksi.add("Baik/Sesuai Standar")
        listChooseInspeksi.add("Cukup")
        listChooseInspeksi.add("Buruk")
        listChooseInspeksi.add("Sangat Buruk")
        recyclerView?.adapter = ListChooseInspeksiAdapter(listChooseInspeksi).also { it.setListener(this) }

        button?.setOnClickListener {
            binding.tvAuditResultListQualityAudit.text = resultScore
            binding.tvAuditResultListQualityAudit.setTextColor(resources.getColor(R.color.grey2_client))

            // validate button submit
            if (!manpower && !organization && !managementProject && !materialStock && !administration && !workResult
            ) {
                binding.btnSubmitDisableListQualityAudit.visibility = View.VISIBLE
                binding.btnSubmitEnableListQualityAudit.visibility = View.INVISIBLE
            } else if (manpower || organization || managementProject || materialStock || administration || workResult
            ) {
                binding.btnSubmitDisableListQualityAudit.visibility = View.GONE
                binding.btnSubmitEnableListQualityAudit.visibility = View.VISIBLE
            } else {
                binding.btnSubmitDisableListQualityAudit.visibility = View.VISIBLE
                binding.btnSubmitEnableListQualityAudit.visibility = View.INVISIBLE
            }

            dialog.dismiss()
        }

        dialog.show()
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
            viewModel.deletePenilaianAudit(userId, projectCode, auditType, questionType, date)
            viewModel.deletePenilaianAuditResponse.observe(this) {
                if (it.code == 200) {
                    dialog.dismiss()
                    setResult(RESULT_OK, Intent())
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menghapus penilaian", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        viewModel.submitResultQualityAudit(userId, projectCode, auditType, resultScore, date)
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

    override fun onBackPressed() {
        if (clickFrom == "detailAudit") {
            setResult(RESULT_OK, Intent())
            finish()
        } else {
            if (!manpower && !organization && !managementProject && !materialStock && !administration && !workResult) {
                setResult(RESULT_OK, Intent())
                finish()
            } else if (manpower || organization || managementProject || materialStock || administration || workResult) {
                showDialogBackWarning()
            } else {
                setResult(RESULT_OK, Intent())
                finish()
            }
        }
    }

    override fun onItemSelected(item: String) {
        resultScore = item
    }
}