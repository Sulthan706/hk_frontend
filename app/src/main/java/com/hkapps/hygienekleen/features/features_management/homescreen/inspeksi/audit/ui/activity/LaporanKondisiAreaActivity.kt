package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityLaporanKondisiAreaBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.viewmodel.AuditViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ListAreaInspeksiAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ListObjectInspeksiAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listArea.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listLaporanKondisiArea.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ListChooseInspeksiAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ListLaporanAreaAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.viewmodel.InspeksiViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class LaporanKondisiAreaActivity : AppCompatActivity(),
    ListAreaInspeksiAdapter.ListAreaInspeksiCallBack,
    ListObjectInspeksiAdapter.ListObjectInspeksiCallBack,
    ListChooseInspeksiAdapter.OnItemSelectedCallBack {

    private lateinit var binding: ActivityLaporanKondisiAreaBinding
    private lateinit var rvAdapter: ListLaporanAreaAdapter
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val idArea = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_AREA_KOMPONEN_AUDIT, 0)
    private val nameArea = CarefastOperationPref.loadString(CarefastOperationPrefConst.NAME_AREA_KOMPONEN_AUDIT, "")
    private val auditType = CarefastOperationPref.loadString(CarefastOperationPrefConst.AUDIT_TYPE, "")
    private var selectedAreaId = 0
    private var selectedAreaName = ""
    private var selectedObjectId = 0
    private var selectedObjectName = ""
    private var penilaian = ""
    private var skorPenilaian = -1
    private var latestTmpUri: Uri? = null
    private var listPenilaian = ArrayList<Content>()
    private var flag = 1

    private var page = 0
    private var isLastPage = false
    private var date = ""

    companion object {
        const val CAMERA_REQ = 101
    }

    private val viewModel: InspeksiViewModel by lazy {
        ViewModelProviders.of(this).get(InspeksiViewModel::class.java)
    }
    private val auditViewModel: AuditViewModel by lazy {
        ViewModelProviders.of(this).get(AuditViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporanKondisiAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarLaporanKondisiArea.tvAppbarTitle.text = "Laporan Kondisi Area"
        binding.appbarLaporanKondisiArea.ivAppbarBack.setOnClickListener {
            showDialogBackWarning()
        }

        // set current date
        val today = Calendar.getInstance()
        val day = android.text.format.DateFormat.format("dd", today) as String
        val months = android.text.format.DateFormat.format("MM", today) as String
        val year = android.text.format.DateFormat.format("yyyy", today) as String
        date = "$year-$months-$day"

        // set rv layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvLaporanKondisiArea.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvLaporanKondisiArea.addOnScrollListener(scrollListener)

        // set on click tambah penilaian
        binding.rlCreateLaporanKondisiArea.setOnClickListener {
            bottomSheetChooseObject()
        }

        // set on click button submit
        binding.btnSubmitEnableLaporanKondisiArea.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitEnableLaporanKondisiArea.isEnabled = false
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.SUBMITTED_HASIL_KERJA, true)
                setResult(RESULT_OK, Intent())
//                startActivity(Intent(this, ListQuestionAuditActivity::class.java))
                finish()
            }
            flag = 0
        }

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.listLaporanAreaResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    // validate button submit
                    binding.btnSubmitDisableLaporanKondisiArea.visibility = View.GONE
                    binding.btnSubmitEnableLaporanKondisiArea.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.rvLaporanKondisiArea.visibility = View.VISIBLE
                        binding.tv1LaporanKondisiArea.visibility = View.GONE
                        isLastPage = it.data.last
                        if (page == 0) {
                            rvAdapter = ListLaporanAreaAdapter(
                                this,
                                it.data.content as ArrayList<Content>,
                                "laporanKondisiArea",
                                auditViewModel,
                                this
                            )
                            binding.rvLaporanKondisiArea.adapter = rvAdapter
                        } else {
                            rvAdapter.listLaporanArea.addAll(it.data.content)
                            rvAdapter.notifyItemRangeChanged(
                                rvAdapter.listLaporanArea.size - it.data.content.size,
                                rvAdapter.listLaporanArea.size
                            )
                        }
                    }, 1500)
                } else {
                    // validate button submit
                    binding.btnSubmitDisableLaporanKondisiArea.visibility = View.VISIBLE
                    binding.btnSubmitEnableLaporanKondisiArea.visibility = View.INVISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.rvLaporanKondisiArea.adapter = null
                        binding.tv1LaporanKondisiArea.visibility = View.VISIBLE
                    }, 1500)
                }
            } else {
                binding.rvLaporanKondisiArea.adapter = null
                Toast.makeText(this, "Gagal mengambil data list laporan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListLaporanArea(userId, projectCode, auditType, idArea, date, page)
    }

    private fun bottomSheetChooseArea() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_area_inspeksi)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomSheetAreaInspeksi)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvBottomSheetAreaInspeksi)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnChooseBottomSheetAreaInspeksi)

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager

        ivClose?.setOnClickListener {
            selectedAreaName = ""
            selectedAreaId = 0
            dialog.dismiss()
        }

        button?.setOnClickListener {
            bottomSheetChooseObject()
            Handler(Looper.getMainLooper()).postDelayed({ dialog.dismiss() }, 500)
        }

        viewModel.getListAreaInspeksi()
        viewModel.listAreaResponse.observe(this) {
            if (it.code == 200) {
                recyclerView?.adapter = ListAreaInspeksiAdapter(
                    it.data as ArrayList<Data>
                ).also { it1 -> it1.setListener(this) }
            }
        }

        dialog.show()
    }

    private fun bottomSheetChooseObject() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_object_inspeksi)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomSheetObjectInspeksi)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvBottomSheetObjectInspeksi)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnChooseBottomSheetObjectInspeksi)

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager

        ivClose?.setOnClickListener {
            selectedObjectName = ""
            selectedObjectId = 0
            dialog.dismiss()
        }

        button?.setOnClickListener {
            bottomSheetPenilaian()
            Handler(Looper.getMainLooper()).postDelayed({ dialog.dismiss() }, 500)
        }

        viewModel.getListObjectInspeksi(idArea)
        viewModel.listObjectResponse.observe(this) {
            if (it.code == 200) {
                recyclerView?.adapter = ListObjectInspeksiAdapter(
                    it.data as ArrayList<com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listObject.Data>
                ).also { it1 -> it1.setListener(this) }
            }
        }

        dialog.show()
    }

    private fun bottomSheetTakePhoto() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_foto_area_inspeksi)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomFotoAreaInspeksi)
        val button = dialog.findViewById<RelativeLayout>(R.id.rlTakePhotoBottomFotoAreaInspeksi)

        ivClose?.setOnClickListener {
            selectedObjectName = ""
            selectedObjectId = 0
            penilaian = ""
            skorPenilaian = -1
            dialog.dismiss()
        }

        button?.setOnClickListener {
            checkPermissionCamera()
            Handler(Looper.getMainLooper()).postDelayed({ dialog.dismiss() }, 500)
        }

        dialog.show()
    }

    private fun checkPermissionCamera() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            takeImage()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
                isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    showDialogLaporanArea(uri)
                }
            } else {
                onBackPressed()
            }
        }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun createTempFiles(bitmap: Bitmap): File? {
        val file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photokondisiarea.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos)
        val bitmapdata = bos.toByteArray()
        //write the bytes in file
        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogLaporanArea(uri: Uri) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_laporan_area_inspeksi)
        val tvAreaKomponenTitle = dialog.findViewById<TextView>(R.id.tvAreaKomponenTitleDialogLaporanAreaInspeksi)
        val tvAreaKomponen = dialog.findViewById<TextView>(R.id.tvAreaKomponenDialogLaporanAreaInspeksi)
        val tvObject = dialog.findViewById<TextView>(R.id.tvObjectDialogLaporanAreaInspeksi)
        val tvPenilaian = dialog.findViewById<TextView>(R.id.tvPenilaianDialogLaporanAreaInspeksi)
        val tvSkor = dialog.findViewById<TextView>(R.id.tvScoreDialogLaporanAreaInspeksi)
        val ivFoto = dialog.findViewById<ImageView>(R.id.ivPreviewDialogLaporanAreaInspeksi)
        val etNote = dialog.findViewById<AppCompatEditText>(R.id.etNoteDialogLaporanAreaInspeksi)
        val btnBack = dialog.findViewById<AppCompatButton>(R.id.btnBackDialogLaporanAreaInspeksi)
        val btnSubmit = dialog.findViewById<AppCompatButton>(R.id.btnYesDialogLaporanAreaInspeksi)

        if (auditType == "L2") {
            tvAreaKomponenTitle?.text = "Komponen"
        } else {
            tvAreaKomponenTitle?.text = "Area"
        }
        tvAreaKomponen?.text = nameArea
        tvObject?.text = selectedObjectName
        Glide.with(applicationContext).load(uri).into(ivFoto!!)

        tvPenilaian?.text = penilaian
        tvSkor?.text = if (skorPenilaian == -1) {
            "-"
        } else {
            "$skorPenilaian"
        }

        btnBack?.setOnClickListener {
            selectedObjectName = ""
            selectedObjectId = 0
            penilaian = ""
            skorPenilaian = -1
            dialog.dismiss()
        }

        var flag = 1
        btnSubmit?.setOnClickListener {
            if (flag == 1) {
                btnSubmit.isEnabled = false
                if (skorPenilaian == -1) {
                    Toast.makeText(this, "Beri penilaian terlebih dahulu", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val bitmap: Bitmap = (ivFoto.drawable as BitmapDrawable).bitmap
                    val file = createTempFiles(bitmap)
                    val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                    val image: MultipartBody.Part = MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

                    auditViewModel.submitHasilKerja(userId, projectCode, auditType, idArea,
                        selectedObjectId, image, skorPenilaian, etNote?.text.toString(), date
                    )
                    auditViewModel.submitHasilKerjaResponse.observe(this) {
                        if (it.code == 201 || it.code == 200) {
                            Toast.makeText(this, "Berhasil submit laporan", Toast.LENGTH_SHORT).show()
                            onResume()
                            dialog.dismiss()
                        } else {
                            flag = 1
                            btnSubmit.isEnabled = true
                            Toast.makeText(this, "Gagal submit laporan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            flag = 0
        }

        dialog.show()
    }

    private fun bottomSheetPenilaian() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_penilaian_inspeksi)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomSheetPenilaianInspeksi)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvBottomSheetPenilaianInspeksi)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnChooseBottomSheetPenilaianInspeksi)

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager

        ivClose?.setOnClickListener {
            selectedObjectName = ""
            selectedObjectId = 0
            penilaian = ""
            skorPenilaian = -1
            dialog.dismiss()
        }

        button?.setOnClickListener {
            if (penilaian == "") {
                Toast.makeText(this, "Silahkan pilih posisi", Toast.LENGTH_SHORT).show()
            } else {
                bottomSheetTakePhoto()
                Handler(Looper.getMainLooper()).postDelayed({ dialog.dismiss() }, 500)
            }
        }

        val listPenilaian = ArrayList<String>()
        listPenilaian.add("Melebihi Ekspektasi/Ekselen")
        listPenilaian.add("Sangat Baik")
        listPenilaian.add("Standar")
        listPenilaian.add("Jelek/Kotor")
        listPenilaian.add("Sangat Jelek/Kotor/Rusak")
        recyclerView?.adapter = ListChooseInspeksiAdapter(listPenilaian).also { it.setListener(this) }

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

        val idArea = ArrayList<Int>()
        idArea.add(this.idArea)
        btnOke?.setOnClickListener {
            auditViewModel.deleteHasilKerja(userId, projectCode, auditType, idArea, date)
            auditViewModel.deleteHasilKerjaResponse.observe(this) {
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

    override fun onResume() {
        super.onResume()
        loadData()
        setObserver()
    }

    override fun onBackPressed() {
        showDialogBackWarning()
    }

    override fun onAreaSelected(areaId: Int, areaName: String) {
        selectedAreaId = areaId
        selectedAreaName = areaName
    }

    override fun onObjectSelected(objectId: Int, objectName: String) {
        selectedObjectId = objectId
        selectedObjectName = objectName
    }

    override fun onItemSelected(item: String) {
        penilaian = item
        skorPenilaian = when(item) {
            "Melebihi Ekspektasi/Ekselen" -> 4
            "Sangat Baik" -> 3
            "Standar" -> 2
            "Jelek/Kotor" -> 1
            "Sangat Jelek/Kotor/Rusak" -> 0
            else -> -1
        }
    }
}