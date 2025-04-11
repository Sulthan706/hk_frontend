package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
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
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.R.drawable.ic_error_image
import com.hkapps.hygienekleen.databinding.ActivityListQuestionAuditBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listQuestionAudit.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.adapter.DetailQuestionsAuditAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.adapter.ListQuestionAuditAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.adapter.QuestionsWorkResultAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.viewmodel.AuditViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListQuestionAuditActivity : AppCompatActivity(),
    ListQuestionAuditAdapter.QuestionAuditCallBack,
    QuestionsWorkResultAdapter.WorkResultCallBack,
    DetailQuestionsAuditAdapter.DetailQuestionAuditCallback
{

    private lateinit var binding: ActivityListQuestionAuditBinding
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.M_CLICK_AUDIT, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
    private val auditType = CarefastOperationPref.loadString(CarefastOperationPrefConst.AUDIT_TYPE, "")
    private val questionType = CarefastOperationPref.loadString(CarefastOperationPrefConst.QUESTION_TYPE, "")
    private val idAuditKualitas = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_AUDIT_KUALITAS, 0)
    private var submittedHasilKerja = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.SUBMITTED_HASIL_KERJA, false)
    private var idSubmitQuestion = 0
    private var date = ""
    private var questionId = 0
    private var flag = 1
    private var latestTmpUri: Uri? = null
    private var cameraFrom = ""
    private var formStatus = ""
    private var note = ""
    private var reloadedNeeded = true
    private var questionTypes = ArrayList<String>()
    private var idArea = ArrayList<Int>()

    companion object {
        const val CAMERA_REQ = 101
        private const val CREATE_CODE = 31
    }

    private val viewModel: AuditViewModel by lazy {
        ViewModelProviders.of(this).get(AuditViewModel::class.java)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListQuestionAuditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        val mQuestionType = when (questionType) {
            "MANPOWER" -> "Manpower"
            "ORGANISASI" -> "Organisasi"
            "MANAJEMEN_PROJECT" -> "Manajemen Project"
            "MATERIAL_STOCK" -> "Material & Stock"
            "ADMINISTRASI" -> "Administrasi"
            "HASIL_KERJA" -> "Hasil Kerja"
            else -> ""
        }
        binding.appbarListQuestionAudit.tvAppbarTitle.text = "Audit $auditType - $mQuestionType"
        binding.appbarListQuestionAudit.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // set default layout
        binding.shimmerListQuestionAudit.startShimmerAnimation()
        binding.shimmerListQuestionAudit.visibility = View.VISIBLE
        binding.rvListQuestionAudit.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListQuestionAudit.layoutManager = layoutManager

        // get current date
        val dates = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        date = dateFormat.format(dates.time)

        // set on click button submit
        binding.btnSubmitEnableListQuestionAudit.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitEnableListQuestionAudit.isEnabled = false
                setResult(RESULT_OK, Intent())
                finish()
            }
            flag = 0
        }

        loadData()
        setObserver()
    }

    private fun loadData() {
        if (clickFrom == "detailAudit") {
            // set visibility button submit
            binding.btnSubmitDisableListQuestionAudit.visibility = View.GONE
            binding.btnSubmitEnableListQuestionAudit.visibility = View.INVISIBLE

            viewModel.getDetailAuditQuestion(idAuditKualitas, questionType)
        } else {
            if (questionType == "HASIL_KERJA") {
                // set visibility button submit
                if (submittedHasilKerja) {
                    binding.btnSubmitDisableListQuestionAudit.visibility = View.GONE
                    binding.btnSubmitEnableListQuestionAudit.visibility = View.VISIBLE
                } else {
                    binding.btnSubmitDisableListQuestionAudit.visibility = View.VISIBLE
                    binding.btnSubmitEnableListQuestionAudit.visibility = View.INVISIBLE
                }

                viewModel.getListHasilKerja(userId, projectCode, auditType, date)
            } else {
                // set visibility button submit
                binding.btnSubmitDisableListQuestionAudit.visibility = View.VISIBLE
                binding.btnSubmitEnableListQuestionAudit.visibility = View.INVISIBLE

                viewModel.getListQuestionAudit(userId, projectCode, auditType, questionType, date)
            }
        }
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    binding.shimmerListQuestionAudit.stopShimmerAnimation()
                    binding.shimmerListQuestionAudit.visibility = View.GONE
                    binding.rvListQuestionAudit.visibility = View.VISIBLE
                }
            }
        }
        viewModel.listQuestionAuditResponse.observe(this) {
            if (it.code == 200) {
                val rvAdapter = ListQuestionAuditAdapter(
                    this,
                    it.data as ArrayList<Data>
                ).also { it1 -> it1.setListener(this) }
                binding.rvListQuestionAudit.adapter = rvAdapter
            } else {
                Toast.makeText(this, "Gagal mengambil data list", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.listWorkResultResponse.observe(this) {
            if (it.code == 200) {
                // save idArea total object = 0
                val length = it.data.size
                for (i in 0 until length) {
                    if (it.data[i].totalObject != 0) {
                        idArea.add(it.data[i].idArea)
                    }
                }

                val rvAdapter = QuestionsWorkResultAdapter(
                    this,
                    it.data as ArrayList<com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listWorkResult.Data>
                ).also { it1 -> it1.setListener(this) }
                binding.rvListQuestionAudit.adapter = rvAdapter
            } else {
                Toast.makeText(this, "Gagal mengambil data list", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.detailAuditQuestionResponse.observe(this) {
            if (it.code == 200) {
                val rvAdapter = DetailQuestionsAuditAdapter(
                    this,
                    it.data as ArrayList<com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailAuditQuestion.Data>
                ).also { it1 -> it1.setListener(this) }
                binding.rvListQuestionAudit.adapter = rvAdapter
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialogPenilaian(uri: Uri, clickFrom: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_penilaian_audit)
        val spinner = dialog.findViewById<AppCompatSpinner>(R.id.spinnerDialogPenilaianAudit)
        val etNote = dialog.findViewById<AppCompatEditText>(R.id.etNoteDialogPenilaianAudit)
        val ivDefault = dialog.findViewById<ImageView>(R.id.ivDefaultDialogPenilaianAudit)
        val ivPreview = dialog.findViewById<ImageView>(R.id.ivPreviewDialogPenilaianAudit)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancelDialogPenilaianAudit)
        val btnSubmit = dialog.findViewById<AppCompatButton>(R.id.btnSubmitDialogPenilaianAudit)

        // set spinner
        val objectValue = resources.getStringArray(R.array.penilaianAudit)
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_item, objectValue)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                formStatus = objectValue[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        // validate on click photo
        if (clickFrom == "beforePhoto") {
            ivDefault?.visibility = View.VISIBLE
            ivPreview.visibility = View.GONE

            // set note
            etNote.addTextChangedListener {
                note = etNote.text.toString()
            }

            ivDefault?.setOnClickListener {
                cameraFrom = "dialogPenilaian"
                checkPermissionCamera()
                dialog.dismiss()
            }
        } else {
            ivDefault?.visibility = View.GONE
            ivPreview.visibility = View.VISIBLE

            // set image preview
            Glide.with(applicationContext).load(uri).into(ivPreview!!)

            // set selection spinner
            val scoreStatus = if (formStatus == "YA" || formStatus == "Ya" || formStatus == "ya") {
                0
            } else {
                1
            }
            spinner.setSelection(scoreStatus)

            // set note
            if (note == "") {
                etNote.hint = "berikan catatan atau alasan di sini"
            } else {
                etNote.hint = note
            }
            etNote.addTextChangedListener {
                note = etNote.text.toString()
            }

        }

        btnCancel?.setOnClickListener {
            formStatus = ""
            note = ""
            dialog.dismiss()
        }

        var flag = 1
        btnSubmit?.setOnClickListener {
            if (flag == 1) {
                btnSubmit.isEnabled = false

                if (clickFrom == "beforePhoto") {
                    val file: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "file",
                        "null",
                        RequestBody.create(MultipartBody.FORM, "")
                    )
                    viewModel.submitPenilaianAudit(userId, projectCode, auditType, questionId,
                        questionType, formStatus, note, file, date)
                } else {
                    val bitmap: Bitmap = (ivPreview.drawable as BitmapDrawable).bitmap
                    val file = createTempFiles(bitmap)
                    val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                    val image: MultipartBody.Part =
                        MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

                    viewModel.submitPenilaianAudit(userId, projectCode, auditType, questionId,
                        questionType, formStatus, note, image, date)
                }

                viewModel.submitPenilaianAuditResponse.observe(this) {
                    if (it.code == 200) {
                        note = ""
                        formStatus = ""
                        onResume()
                        dialog.dismiss()
                        binding.btnSubmitEnableListQuestionAudit.visibility = View.VISIBLE
                        binding.btnSubmitDisableListQuestionAudit.visibility = View.GONE
                    } else {
                        flag = 1
                        btnSubmit.isEnabled = true
                        Toast.makeText(this, "Gagal submit penilaian", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            flag = 0
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
            LaporanKondisiAreaActivity.CAMERA_REQ -> {
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
                    if (cameraFrom == "dialogPenilaian") {
                        showDialogPenilaian(uri, "afterPhoto")
                    } else {
                        showDialogUpdatePenilaian(uri, "afterPhoto")
                    }
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

    override fun onResume() {
        super.onResume()
        loadData()
        setObserver()

        if (this.reloadedNeeded) {
            submittedHasilKerja = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.SUBMITTED_HASIL_KERJA, false)
            loadData()
        }
        this.reloadedNeeded = false
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

        questionTypes.add(this.questionType)
        btnOke?.setOnClickListener {
            if (this.questionType == "HASIL_KERJA") {
                viewModel.deleteHasilKerja(userId, projectCode, auditType, idArea, date)
                viewModel.deleteHasilKerjaResponse.observe(this) {
                    if (it.code == 200) {
                        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.SUBMITTED_HASIL_KERJA, false)
                        dialog.dismiss()
                        setResult(RESULT_OK, Intent())
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal menghapus penilaian", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                viewModel.deletePenilaianAudit(userId, projectCode, auditType, questionTypes, date)
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
        }

        dialog.show()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showDialogUpdatePenilaian(uri: Uri, clickFrom: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_penilaian_audit)
        val spinner = dialog.findViewById<AppCompatSpinner>(R.id.spinnerDialogPenilaianAudit)
        val etNote = dialog.findViewById<AppCompatEditText>(R.id.etNoteDialogPenilaianAudit)
        val ivDefault = dialog.findViewById<ImageView>(R.id.ivDefaultDialogPenilaianAudit)
        val ivPreview = dialog.findViewById<ImageView>(R.id.ivPreviewDialogPenilaianAudit)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancelDialogPenilaianAudit)
        val btnSubmit = dialog.findViewById<AppCompatButton>(R.id.btnSubmitDialogPenilaianAudit)

        btnCancel?.setOnClickListener {
            formStatus = ""
            note = ""
            dialog.dismiss()
        }

        viewModel.getDetailSubmitPenilaian(idSubmitQuestion)
        viewModel.detailSubmitPenilaianResponse.observe(this) {
            if (it.code == 200) {
                // set score status
                formStatus = it.data.scoreStatus

                // set spinner
                val objectValue = resources.getStringArray(R.array.penilaianAudit)
                val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_item, objectValue)
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_item)
                spinner.adapter = spinnerAdapter
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        formStatus = objectValue[position]
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
                val scoreStatus = when (it.data.scoreStatus) {
                    "YA", "Ya", "ya" -> 0
                    "TIDAK", "Tidak", "tidak" -> 1
                    else -> -1
                }
                spinner.setSelection(scoreStatus)

                // set note text
                note = it.data.description
                etNote?.hint = if (it.data.description == "") {
                    "berikan catatan atau alasan di sini"
                } else {
                    it.data.description
                }
                etNote?.addTextChangedListener {
                    note = etNote.text.toString()
                }

                // validate on click photo
                if (clickFrom == "beforePhoto") {
                    // set image
                    val img = it.data.image
                    val url = getString(R.string.url) + "assets.admin_master/images/audit/$img"

                    if (img == "null" || img == null || img == "") {
                        ivDefault?.visibility = View.VISIBLE
                        ivPreview.visibility = View.GONE

                        ivDefault?.setOnClickListener {
                            cameraFrom = "dialogUpdatePenilaian"
                            checkPermissionCamera()
                            dialog.dismiss()
                        }
                    } else {
                        ivDefault?.visibility = View.GONE
                        ivPreview.visibility = View.VISIBLE

                        val requestOptions = RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                            .skipMemoryCache(true)
                            .error(ic_error_image)

                        Glide.with(this)
                            .load(url)
                            .apply(requestOptions)
                            .into(ivPreview)

                        ivPreview?.setOnClickListener {
                            cameraFrom = "dialogUpdatePenilaian"
                            checkPermissionCamera()
                            dialog.dismiss()
                        }
                    }
                } else {
                    // set image preview
                    Glide.with(applicationContext).load(uri).into(ivPreview!!)

                }

                var flag = 1
                btnSubmit?.setOnClickListener {
                    if (flag == 1) {
                        btnSubmit.isEnabled = false

                        if (ivPreview.drawable == resources.getDrawable(ic_error_image) || ivDefault.isVisible) {
                            val image: MultipartBody.Part = MultipartBody.Part.createFormData(
                                "file",
                                "null",
                                RequestBody.create(MultipartBody.FORM, "")
                            )

                            viewModel.updatePenilaianAudit(idSubmitQuestion, formStatus, note, image)
                            viewModel.updatePenilaianAuditResponse.observe(this) { it1 ->
                                if (it1.code == 200) {
                                    formStatus = ""
                                    note = ""

                                    onResume()
                                    dialog.dismiss()

                                    binding.btnSubmitEnableListQuestionAudit.visibility = View.VISIBLE
                                    binding.btnSubmitDisableListQuestionAudit.visibility = View.GONE
                                } else {
                                    flag = 1
                                    btnSubmit.isEnabled = true
                                    Toast.makeText(this, "Gagal submit penilaian", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            val bitmap: Bitmap = (ivPreview.drawable as BitmapDrawable).bitmap
                            val file = createTempFiles(bitmap)
                            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                            val image: MultipartBody.Part =
                                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

                            viewModel.updatePenilaianAudit(idSubmitQuestion, formStatus, note, image)
                            viewModel.updatePenilaianAuditResponse.observe(this) { it1 ->
                                if (it1.code == 200) {
                                    formStatus = ""
                                    note = ""

                                    onResume()
                                    dialog.dismiss()

                                    binding.btnSubmitEnableListQuestionAudit.visibility = View.VISIBLE
                                    binding.btnSubmitDisableListQuestionAudit.visibility = View.GONE
                                } else {
                                    flag = 1
                                    btnSubmit.isEnabled = true
                                    Toast.makeText(
                                        this,
                                        "Gagal submit penilaian",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                    }
                    flag = 0
                }
            }
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                this.reloadedNeeded = true
            }
        }
    }

    override fun onBackPressed() {
        if (clickFrom == "detailAudit") {
            setResult(RESULT_OK, Intent())
            finish()
        } else {
            if (idArea.isNotEmpty() || questionTypes.isNotEmpty()) {
                showDialogBackWarning()
            } else {
                setResult(RESULT_OK, Intent())
                finish()
            }
        }
    }

    override fun onClickQuestion(questionId: Int, idSubmitQuestion: Int) {
        this.questionId = questionId
        this.idSubmitQuestion = idSubmitQuestion

        if (idSubmitQuestion == 0) {
            showDialogPenilaian(getTmpFileUri(), "beforePhoto")
        } else {
            showDialogUpdatePenilaian(getTmpFileUri(), "beforePhoto")
        }
    }

    override fun onClickWorkResult(idAreaKomponen: Int, nameAreaKomponen: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_AREA_KOMPONEN_AUDIT, idAreaKomponen)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.NAME_AREA_KOMPONEN_AUDIT, nameAreaKomponen)
        startActivityForResult(
            Intent(
                this,
                LaporanKondisiAreaActivity::class.java
            ), CREATE_CODE
        )
    }

    override fun onClickDetail() {
        if (questionType == "HASIL_KERJA") {
            startActivity(Intent(this, DetailsKondisiAreaActivity::class.java))
        }
    }

}