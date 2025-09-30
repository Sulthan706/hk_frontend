package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity

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
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailAreaChecklistBinding
import com.hkapps.hygienekleen.features.features_client.complaint.ui.activity.ComplaintActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.detailArea.ListOperatorSpinnerModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter.SpinnerOpsChecklistAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DetailAreaChecklistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailAreaChecklistBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val shiftId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
    private val plottingId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.CHECKLIST_PLOTTING_ID, 0)

    private var selectedReview: Int = 0
    private var pengawas: Int = 0
    private var lengthOperational: Int = 0
    private var listIdOperational = ArrayList<Int>()
    private var loadingDialog: Dialog? = null

    private val viewModel: ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
    }

    private var mLastClickTime: Long = 0
    companion object {
        const val CAMERA_REQ = 101
        const val MIN_CLICK_INTERVAL: Long = 600
    }
    private var latestTmpUri: Uri? = null
    private val previewImage by lazy {
        findViewById<ImageView>(R.id.iv_penilaianDetailAreaChecklist)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAreaChecklistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set appbar
        binding.appbarDetailAreaChecklist.tvAppbarTitle.text = "Konfirmasi Area"
        binding.appbarDetailAreaChecklist.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()

            CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_PLOTTING_ID, 0)

//            val returnIntent = Intent()
//            setResult(Activity.RESULT_OK, returnIntent)
//            finish()
        }

        Log.d("DetailAreaCheck", "onCreate: plottingId = $plottingId")

//        setLayoutPenilaian()
        loadData()
        setObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.detailAreaResponseModel.observe(this) {
            if (it.code == 200) {
                // set pengawas
                pengawas = it.data.pengawas.idEmployee
                binding.tvPengawasDetailAreaChecklist.text = "${it.data.pengawas.employeeName} (${it.data.pengawas.employeeNuc})"
                setPhotoProfile(it.data.pengawas.employeePhotoProfile, binding.ivPengawasDetailAreaChecklist)

                // set dropdown operational
                val listOps: MutableList<ListOperatorSpinnerModel> = ArrayList()
                val operational = ListOperatorSpinnerModel("operational", "operational", "operational")
                listOps.add(operational)
                lengthOperational = it.data.operational.size
                if (lengthOperational != 0) {
                    for (i in 0 until lengthOperational) {
                        val name = it.data.operational[i].employeeName
                        val nuc = it.data.operational[i].employeeNuc
                        val image = if (it.data.operational[i].employeePhotoProfile == "" ||
                            it.data.operational[i].employeePhotoProfile == null ||
                            it.data.operational[i].employeePhotoProfile == "null") {
                            ""
                        } else {
                            it.data.operational[i].employeePhotoProfile
                        }

                        val m = ListOperatorSpinnerModel(name, nuc, image)
                        listOps.add(m)

                        listIdOperational.add(it.data.operational[i].idEmployee)
                    }
                }
                val spinnerAdapter = SpinnerOpsChecklistAdapter(this, listOps)
                binding.spinnerOpsDetailAreaChecklist.adapter = spinnerAdapter

                binding.spinnerOpsDetailAreaChecklist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        Log.d("DetailAreaCheckAct", "onItemSelected: posisi spinner operator = $p2")
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

                // detail plotting area
                binding.tvCodePlottingDetailAreaChecklist.text = it.data.codePlottingArea
                binding.tvAreaDetailAreaChecklist.text = it.data.locationName
                binding.tvSubAreaDetailAreaChecklist.text = it.data.subLocationName
                binding.tvShiftDetailAreaChecklist.text = when(it.data.shiftDescription) {
                    "Week Days (Hari Sabtu dan Hari Minggu Office)" -> "Week Days"
                    else -> it.data.shiftDescription
                }

                setLayoutPenilaian()
            }
        }
        viewModel.checkPlottingResponseModel.observe(this) {
            if (it.code == 200) {
                binding.llReviewDetailAreaChecklist.visibility = View.VISIBLE
                binding.llPenilaianDetailAreaChecklist.visibility = View.GONE

                // set data penilaian
                binding.tvCheckByDetailAreaChecklist.text = it.data.submitByName
                binding.tvPenilaianObjectDetailAreaChecklist.text = it.data.checklistReviewName
                binding.tvNoteReviewDetailAreaChecklist.text =
                    if (it.data.notes == "" || it.data.notes == null || it.data.notes == "null") {
                        "Tidak ada catatan"
                    } else {
                        it.data.notes
                    }

                // set photo penilaian
                val img = it.data.image
                val url = getString(R.string.url) + "assets.admin_master/images/checklist_image/$img"
                val requestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                    .skipMemoryCache(true)
                    .centerCrop()
                    .error(R.drawable.ic_error_image)
                Glide.with(this)
                    .load(url)
                    .apply(requestOptions)
                    .into(binding.ivReviewDetailAreaChecklist)

                // pop up photo
                binding.cardReviewDetailAreaChecklist.setOnClickListener {
                    val dialog = this.let { Dialog(it) }
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_custom_image_zoom)
                    val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
                    val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView

                    ivZoom.setImageDrawable(binding.ivReviewDetailAreaChecklist.drawable)

                    close.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }

            } else {
                binding.llReviewDetailAreaChecklist.visibility = View.GONE
                binding.llPenilaianDetailAreaChecklist.visibility = View.VISIBLE
            }
        }
        viewModel.submitChecklistResponseModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                Toast.makeText(this, "Penilaian berhasil", Toast.LENGTH_SHORT).show()
                val i = Intent(this, DetailAreaChecklistActivity::class.java)
                startActivity(i)
                finish()
            } else {
                hideLoading()
                Toast.makeText(this, "Gagal mengirim penilaian", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getDetailArea(projectCode, shiftId, plottingId)
        viewModel.getCheckPlotting(plottingId)
    }

    private fun setLayoutPenilaian() {
        // dropdown penilaian
        viewModel.getListReviewArea(userId, projectCode, plottingId, 3)
        viewModel.listReviewAreaResponseModel.observe(this) {
            val data = ArrayList<String>()
            val length = it.data.size
            for (i in 0 until length) {
                data.add(it.data[i].checklistReviewValue)
            }
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this, R.layout.spinner_item, data
            )
//            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.spinnerPenilaianDetailAreaChecklist.adapter = adapter
            binding.spinnerPenilaianDetailAreaChecklist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    selectedReview = it.data[position].checklistReviewId
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }

        // take a photo
        if (lengthOperational == 0) {
            binding.cardPenilaianDetailAreaChecklist.setOnClickListener {
                Toast.makeText(this, "Tidak ada operational", Toast.LENGTH_SHORT).show()
            }
            binding.btnDetailAreaChecklist.setBackgroundResource(R.drawable.bg_disable)
            binding.btnDetailAreaChecklist.isEnabled = false
        } else {
            binding.cardPenilaianDetailAreaChecklist.setOnClickListener {
                setupPermissions()
            }
            binding.btnDetailAreaChecklist.setBackgroundResource(R.drawable.bg_primary)
            binding.btnDetailAreaChecklist.isEnabled = true
        }

        // button submit
//        if (lengthOperational == 0) {
//            binding.btnDetailAreaChecklist.setBackgroundResource(R.drawable.bg_disable)
//            binding.btnDetailAreaChecklist.isEnabled = false
//        } else {
//            binding.btnDetailAreaChecklist.setBackgroundResource(R.drawable.bg_primary)
//            binding.btnDetailAreaChecklist.isEnabled = true
//            binding.btnDetailAreaChecklist.setOnClickListener {
//                Toast.makeText(this, "Foto belum tersedia", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            takeImage()
        }
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri!!)
            }
        }
    }

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                previewImage.setImageURI(uri)

                // button submit
                var flag = 1
                Log.d("DetailAreaCheckAct", "onViewCreated: running (flag = $flag")
                binding.btnDetailAreaChecklist.setOnClickListener {
                    if (flag == 1) {
                        binding.btnDetailAreaChecklist.isEnabled = false
//                        binding.btnDetailAreaChecklist.setBackgroundResource(R.drawable.bg_disable)
                        Log.d("DetailAreaCheckAct", "onViewCreated: running (flag on click = $flag")
                        showLoading("Sedang mengirim nilai")
                    }
                    flag = 0

//                    val bitmap: Bitmap =
//                        (binding.ivPenilaianDetailAreaChecklist.drawable as BitmapDrawable).bitmap
//                    val file = createTempFiles(bitmap)
//                    val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
//                    val image: MultipartBody.Part = MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
//
//                    viewModel.submitChecklist(
//                        projectCode,
//                        userId,
//                        pengawas,
//                        plottingId,
//                        shiftId,
//                        selectedReview,
//                        binding.etNoteDetailAreaChecklist.text.toString(),
//                        image,
//                        listIdOperational
//                    )
                }
            }
        }
    }

    private fun createTempFiles(bitmap: Bitmap?): File? {
//        File file = new File(TahapTigaActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                , System.currentTimeMillis() + "_education.JPEG");
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photochecklist.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 10, bos)
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

    private fun getTmpFileUri(): Uri? {
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

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ComplaintActivity.CAMERA_REQ -> {
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

    private fun setPhotoProfile(img: String?, imageView: ImageView) {
        val url = getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                resources.getIdentifier(uri, null, packageName)
            val res = resources.getDrawable(imaResource)
            imageView.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(imageView)
        }
    }

    private fun isOnceClick(): Boolean {
        val currentClickTime: Long = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        mLastClickTime = currentClickTime

        if (elapsedTime <= MIN_CLICK_INTERVAL) {
            return true
        }
        return false
    }

    private fun showLoading(loadingText: String) {
        Log.d("DetailAreaChecklist", "showLoading: ")
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        val bitmap: Bitmap =
            (binding.ivPenilaianDetailAreaChecklist.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part = MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

        viewModel.submitChecklist(
            projectCode,
            userId,
            pengawas,
            plottingId,
            shiftId,
            selectedReview,
            binding.etNoteDetailAreaChecklist.text.toString(),
            image,
            listIdOperational
        )
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()

        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_PLOTTING_ID, 0)

//        val returnIntent = Intent()
//        setResult(Activity.RESULT_OK, returnIntent)
//        finish()
    }
}

