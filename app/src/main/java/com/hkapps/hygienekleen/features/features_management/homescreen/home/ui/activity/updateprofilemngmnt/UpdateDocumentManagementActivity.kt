package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityUpdateDocumentManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment.BotSheetDocumentManagementFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class UpdateDocumentManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateDocumentManagementBinding
    private val homeManagementViewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }

    val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateDocumentManagementBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.layoutAppbarUpdateDocument.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        binding.layoutAppbarUpdateDocument.tvAppbarTitle.text = "Dokumen Saya"

        binding.llUploadKTP.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, "KTP")
            BotSheetDocumentManagementFragment().show(supportFragmentManager, "bottomsheetktp")
        }
        //bpjs
        binding.llbpjskerja.setOnClickListener {
            startActivity(Intent(this, UpdateJamsostekManagamentActivity::class.java))
        }
        binding.llbpjskesehatan.setOnClickListener {
            startActivity(Intent(this, UpdateBpjsManagementActivity::class.java))
        }
//        binding.llUploadIjazah.setOnClickListener {
//            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, "IJAZAH")
//            BotSheetDocumentManagementFragment().show(supportFragmentManager, "bottomsheetijazah")
//        }
//        binding.llUploadSertifikat.setOnClickListener {
//            CarefastOperationPref.saveString(
//                CarefastOperationPrefConst.TYPE_DOCUMENT,
//                "SERTIFIKAT KOMPETENSI"
//            )
//            BotSheetDocumentManagementFragment().show(supportFragmentManager, "bottomsheetsertifikat")
//        }
//        binding.llUploadSIO.setOnClickListener {
//            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, "SIO")
//            BotSheetDocumentManagementFragment().show(supportFragmentManager, "bottomsheetsio")
//        }
//        binding.llUploadSKCK.setOnClickListener {
//            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, "SKCK")
//            BotSheetDocumentManagementFragment().show(supportFragmentManager, "bottomsheetskck")
//        }

        loadData()
        setObserver()
    //oncreate
    }

    private fun setObserver() {
        homeManagementViewModel.getListDocumentManagementViewModel().observe(this){
            if (it.code == 200){
//                hideLoading()
//                //get nik
//                binding.tvNumberNikDokumen.text = if (numberNIK.isNullOrEmpty()) {
//                    "Number nik not found"
//                } else {
//                    numberNIK
//                }
                binding.tvUploadedKTP.text = if (it.data.adminKtp == null) {
                    CarefastOperationPref.saveBoolean(
                        CarefastOperationPrefConst.CHECK_DOCUMENT_KTP,
                        false
                    )
                    "file belum tersedia"
                } else {
                    CarefastOperationPref.saveBoolean(
                        CarefastOperationPrefConst.CHECK_DOCUMENT_KTP,
                        true
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.IMAGE_KTP,
                        it.data.adminKtp.toString()
                    )
                    it.data.adminKtp.toString()
                }
                //get bpjs
                if (it.data.adminBpjsKesehatan.isNullOrEmpty() || it.data.adminBpjsKesehatanFile.isNullOrEmpty()){
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.NUMBER_BPJSKES_MANAGEMENT, "")
                } else {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.NUMBER_BPJSKES_MANAGEMENT, it.data.adminBpjsKesehatan)
                }
                if (it.data.adminBpjsKetenagakerjaan.isNullOrEmpty() || it.data.adminBpjsKetenagakerjaanFile.isNullOrEmpty()){
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.NUMBER_BPJSTK_MANAGEMENT, "")
                } else {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.NUMBER_BPJSTK_MANAGEMENT, it.data.adminBpjsKetenagakerjaan)
                }

                binding.tvBpjsTkNumber.text = if (it.data.adminBpjsKetenagakerjaan.isNullOrEmpty()) "Nomer belum tersedia" else {
                    it.data.adminBpjsKetenagakerjaan
                }
                binding.tvBpjstkFile.text = if (it.data.adminBpjsKetenagakerjaanFile.isNullOrEmpty()) "file belum tersedia" else {
                    it.data.adminBpjsKetenagakerjaanFile
                }
                binding.tvBpjsKesNumber.text = if (it.data.adminBpjsKesehatan.isNullOrEmpty()) "Nomer belum tersedia" else {
                    it.data.adminBpjsKesehatan
                }
                binding.tvBpjsKesfile.text = if (it.data.adminBpjsKesehatanFile.isNullOrEmpty()) "file belum tersedia" else {
                    it.data.adminBpjsKesehatanFile
                }
//                binding.tvUploadedIjazah.text = if (it.data.adminIjazah == null) {
//                    CarefastOperationPref.saveBoolean(
//                        CarefastOperationPrefConst.CHECK_DOCUMENT_IJAZAH,
//                        false
//                    )
//                    "file belum tersedia"
//                } else {
//                    CarefastOperationPref.saveBoolean(
//                        CarefastOperationPrefConst.CHECK_DOCUMENT_IJAZAH,
//                        true
//                    )
//                    CarefastOperationPref.saveString(
//                        CarefastOperationPrefConst.IMAGE_IJAZAH,
//                        it.data.adminIjazah.toString()
//                    )
//                    it.data.adminIjazah.toString()
//                }
//                binding.tvUploadedSertifikat.text =
//                    if (it.data.adminSertifikatKompetensi == null) {
//                        CarefastOperationPref.saveBoolean(
//                            CarefastOperationPrefConst.CHECK_DOCUMENT_SERTIFIKAT_KOMPETENSI,
//                            false
//                        )
//                        "file belum tersedia"
//                    } else {
//                        CarefastOperationPref.saveBoolean(
//                            CarefastOperationPrefConst.CHECK_DOCUMENT_SERTIFIKAT_KOMPETENSI,
//                            true
//                        )
//                        CarefastOperationPref.saveString(
//                            CarefastOperationPrefConst.IMAGE_SERTIFIKAT,
//                            it.data.adminSertifikatKompetensi.toString())
//                        it.data.adminSertifikatKompetensi.toString()
//                    }
//                binding.tvUploadedSio.text = if (it.data.adminSio == null) {
//                    CarefastOperationPref.saveBoolean(
//                        CarefastOperationPrefConst.CHECK_DOCUMENT_SIO,
//                        false
//                    )
//                    "file belum tersedia"
//                } else {
//                    CarefastOperationPref.saveBoolean(
//                        CarefastOperationPrefConst.CHECK_DOCUMENT_SIO,
//                        true
//                    )
//                    CarefastOperationPref.saveString(
//                        CarefastOperationPrefConst.IMAGE_SIO,
//                        it.data.adminSio.toString()
//                    )
//                    it.data.adminSio.toString()
//                }
//                binding.tvUploadedSkck.text = if (it.data.adminSkck == null) {
//                    CarefastOperationPref.saveBoolean(
//                        CarefastOperationPrefConst.CHECK_DOCUMENT_SKCK,
//                        false
//                    )
//                    "file belum tersedia"
//                } else {
//                    CarefastOperationPref.saveBoolean(
//                        CarefastOperationPrefConst.CHECK_DOCUMENT_SKCK,
//                        true
//                    )
//                    CarefastOperationPref.saveString(
//                        CarefastOperationPrefConst.IMAGE_SKCK,
//                        it.data.adminSkck.toString()
//                    )
//                    it.data.adminSkck.toString()
//                }
            } else {
                Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        homeManagementViewModel.getListDocumentManagement(employeeId)
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
    //fun
}