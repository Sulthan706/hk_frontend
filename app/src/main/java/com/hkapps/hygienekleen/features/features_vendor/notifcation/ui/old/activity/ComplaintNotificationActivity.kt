package com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.old.activity

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
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityComplaintNotificationBinding
import com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.old.fragment.ChooseOperatorDialog
import com.hkapps.hygienekleen.features.features_vendor.notifcation.viewmodel.NotifVendorViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ComplaintNotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComplaintNotificationBinding
    private val complaintId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION, 0)
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val position =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val intentNotif =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NOTIF_INTENT, "")
    private var processPhotoProfile: String = ""
    private var processName: String? = null
    private var processBy: Int? = null
    private var workerId: Int? = null
    private var beforeImage: String? = null
    private var processImage: String? = null
    private var afterImage: String? = null
    private var workerName: String? = null
    private var loadingDialog: Dialog? = null

    private var onUploadClick: String = ""

    var t: String = ""
    private var latestTmpUri: Uri? = null
    private val previewImage by lazy {
        findViewById<ImageView>(R.id.iv_result_upload_complaint)
    }

    private val viewModel: NotifVendorViewModel by lazy {
        ViewModelProviders.of(this).get(NotifVendorViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar layout
        binding.appbarComplaintNotification.tvAppbarTitle.text = "CTalk"
        binding.appbarComplaintNotification.ivAppbarBack.setOnClickListener {
            if (intentNotif == "notification"){
                CarefastOperationPref.saveString(CarefastOperationPrefConst.NOTIF_INTENT, "")
                setResult(Activity.RESULT_OK, Intent())
                finish()
            } else {
                onBackPressed()
                finish()
            }
//            super.onBackPressed()
//            finish()
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION, 0)
        }
        t = "" + binding.etReplyFromLeader.editText?.text.toString()

        println(position)
        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.getDetailComplaintModel().observe(this, Observer {
            if (it.code == 200) {
                // set detail data
                binding.tvTitleDetailComplaintNotification.text = it.data.title
                binding.tvDateDetailComplaintNotification.text = it.data.date
                binding.tvTimeDetailComplaintNotification.text = it.data.time
                binding.tvLocationDetailComplaintNotification.text = it.data.locationName
                binding.tvSubLocDetailComplaintNotification.text = it.data.subLocationName
                binding.tvEscalationInDetailComplaintNotification.text = "Eskalasi: " + it.data.notificationLevel

                binding.tvClientNameComplaintNotification.text = if (it.data.clientName == null || it.data.clientName == "" || it.data.clientName == "null") {
                    it.data.createdByEmployeeName
                } else {
                    it.data.clientName
                }
                getPhotoProfile(binding.ivClientComplaintNotification, it.data.clientPhotoProfile)

                binding.tvNoteComplaintNotification.text = it.data.description
                setImages(it.data.image, binding.ivComplaintNotification)

                val imageList = ArrayList<SlideModel>() // Create image list

                imageList.add(
                    SlideModel(
                        getString(R.string.url) + "assets.admin_master/images/complaint/" + it.data.image,
                        "CTalk 1."
                    )
                )

                if (it.data.imageTwo == null || it.data.imageTwo == "") {
                    //donothing
                } else {
                    imageList.add(
                        SlideModel(
                            getString(R.string.url) + "assets.admin_master/images/complaint/" + it.data.imageTwo,
                            "CTalk 2."
                        )
                    )
                }

                if (it.data.imageThree == null || it.data.imageThree == "") {
                    //donothing
                } else {
                    imageList.add(
                        SlideModel(
                            getString(R.string.url) + "assets.admin_master/images/complaint/" + it.data.imageThree,
                            "CTalk 3."
                        )
                    )
                }

                if (it.data.imageFourth == null || it.data.imageFourth == "") {
                    //donothing
                } else {
                    imageList.add(
                        SlideModel(
                            getString(R.string.url) + "assets.admin_master/images/complaint/" + it.data.imageFourth,
                            "CTalk 4."
                        )
                    )
                }


                binding.ivComplaintNotification.setOnClickListener {
                    //pop up modal
                    val dialog = this.let { Dialog(it) }
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_custom_image_slide)
                    val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
                    val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView

                    ivZoom.setImageDrawable(binding.ivComplaintNotification.drawable)
                    val imageSlider = dialog.findViewById(R.id.image_slider) as ImageSlider

                    imageSlider.setImageList(imageList)
//                    imageSlider.startSliding(3000) // with new period
//                    imageSlider.startSliding()
                    imageSlider.stopSliding()

                    close.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }

                // set data validation
                processPhotoProfile = it.data.processByEmployeePhotoProfile
                processName = it.data.processByEmployeeName
                processBy = it.data.processBy
                workerId = it.data.workerId
                beforeImage = it.data.beforeImage
                processImage = it.data.processImage
                afterImage = it.data.afterImage
                if (it.data.worker == null) {
                    workerName = null
                } else {
                    workerName = it.data.worker.employeeName
                }

                // set processBy data
                binding.tvOpsNameComplaintNotification.text = it.data.processByEmployeeName
                getPhotoProfile(
                    binding.ivOpsComplaintNotification,
                    it.data.processByEmployeePhotoProfile
                )

                if (it.data.doneAtDate != null) {
                    binding.tvDateDoneDetailComplaintNotification.text = it.data.doneAtDate
                } else {
                    binding.tvDateDoneDetailComplaintNotification.text = ""
                }

                if (it.data.doneAtTime != null) {
                    binding.tvTimeDoneDetailComplaintNotification.text = it.data.doneAtTime
                } else {
                    binding.tvTimeDoneDetailComplaintNotification.text = ""
                }

                when (it.data.statusComplaint) {
                    "WAITING" -> {
                        binding.tvReplyFromLeader.visibility = View.INVISIBLE
                        binding.etReplyFromLeader.visibility = View.VISIBLE
                        onWaitingComplaint()
                    }
                    "ON PROGRESS" -> {
                        if (it.data.comments == null || it.data.comments == "" || it.data.comments == "null") {
                            binding.tvReplyFromLeader.text = "Unknown Balasan"
                        } else {
                            binding.tvReplyFromLeader.text = it.data.comments
                        }
                        binding.tvReplyFromLeader.visibility = View.VISIBLE
                        binding.etReplyFromLeader.visibility = View.INVISIBLE
                        onProgressComplaint()
                    }
                    "DONE" -> {
                        if (it.data.comments == null || it.data.comments == "" || it.data.comments == "null") {
                            binding.tvReplyFromLeader.text = "Unknown Balasan"
                        } else {
                            binding.tvReplyFromLeader.text = it.data.comments
                        }
                        binding.tvReplyFromLeader.visibility = View.VISIBLE
                        binding.etReplyFromLeader.visibility = View.INVISIBLE
                        onDoneComplaint()
                    }
                    "CLOSE" -> {
                        if (it.data.comments == null || it.data.comments == "" || it.data.comments == "null") {
                            binding.tvReplyFromLeader.text = "Unknown Balasan"
                        } else {
                            binding.tvReplyFromLeader.text = it.data.comments
                        }
                        binding.tvReplyFromLeader.visibility = View.VISIBLE
                        binding.etReplyFromLeader.visibility = View.INVISIBLE
                        onDoneComplaint()
                    }
                    else -> {
                        Log.d("ComplaintNotifiAct", "setObserver: status complaint not defined")
                        Log.e("ComplaintNotifiAct", "setObserver: status complaint not defined")
                    }
                }

            }
        })
        viewModel.getProcessModel().observe(this, Observer {
            if (it.code == 200) {
                hideLoading()
                val i = Intent(this, ComplaintNotificationActivity::class.java)
                startActivity(i)
                finish()
                Toast.makeText(this, "Berhasil memproses CTalk", Toast.LENGTH_SHORT).show()
            } else {
                hideLoading()
                Toast.makeText(this, "Gagal memproses CTalk", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.getBeforeImageModel().observe(this, Observer {
            if (it.code == 200) {
                hideLoading()
                val i = Intent(this, ComplaintNotificationActivity::class.java)
                startActivity(i)
                finish()
            } else {
                hideLoading()
                Toast.makeText(this, "Gagal upload before image", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.getProgressImageModel().observe(this, Observer {
            if (it.code == 200) {
                hideLoading()
                val i = Intent(this, ComplaintNotificationActivity::class.java)
                startActivity(i)
                finish()
            } else {
                hideLoading()
                Toast.makeText(this, "Gagal upload progress image", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.getAfterImageModel().observe(this, Observer {
            if (it.code == 200) {
                hideLoading()
                val i = Intent(this, ComplaintNotificationActivity::class.java)
                startActivity(i)
                finish()
            } else {
                hideLoading()
                Toast.makeText(this, "Gagal upload after image", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.getSubmitProcessModel().observe(this, Observer {
            if (it.code == 200) {
                hideLoading()
                onBackPressed()
                finish()
                Toast.makeText(this, "Berhasil menyelesaikan CTalk", Toast.LENGTH_SHORT).show()
            } else {
                hideLoading()
                Toast.makeText(this, "Gagal menyelesaikan CTalk", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onDoneComplaint() {
        // set before, process, after image
        binding.ivBeforeDefault.visibility = View.GONE
        binding.ivProcessDefault.visibility = View.GONE
        binding.ivAfterDefault.visibility = View.GONE

        binding.ivBeforeComplaintNotification.visibility = View.VISIBLE
        binding.ivProcessComplaintNotification.visibility = View.VISIBLE
        binding.ivAfterComplaintNotification.visibility = View.VISIBLE

        binding.ivBeforeComplaintNotification.setOnClickListener {
            val dialog = this.let { Dialog(it) }
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_custom_image_zoom)
            val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
            val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView

            ivZoom.setImageDrawable(binding.ivBeforeComplaintNotification.drawable)

            close.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.ivProcessComplaintNotification.setOnClickListener {
            val dialog = this.let { Dialog(it) }
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_custom_image_zoom)
            val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
            val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView

            ivZoom.setImageDrawable(binding.ivProcessComplaintNotification.drawable)

            close.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.ivAfterComplaintNotification.setOnClickListener {
            val dialog = this.let { Dialog(it) }
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_custom_image_zoom)
            val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
            val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView

            ivZoom.setImageDrawable(binding.ivAfterComplaintNotification.drawable)

            close.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }


        setImages(beforeImage, binding.ivBeforeComplaintNotification)
        setImages(processImage, binding.ivProcessComplaintNotification)
        setImages(afterImage, binding.ivAfterComplaintNotification)

        // set gone button
        binding.rlButtonProcessComplaintNotification.visibility = View.GONE
        binding.rlButtonSubmitComplaintNotification.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun onWaitingComplaint() {
        binding.llProcessComplaintNotif.visibility = View.GONE
        if (position == "Supervisor" || position == "Chief Supervisor") {
            binding.rlButtonSubmitComplaintNotification.visibility = View.GONE
            binding.rlButtonProcessComplaintNotification.visibility = View.VISIBLE
        } else {
            binding.rlButtonSubmitComplaintNotification.visibility = View.GONE
            binding.rlButtonProcessComplaintNotification.visibility = View.VISIBLE
        }
        binding.rlButtonProcessComplaintNotification.setBackgroundResource(R.drawable.bg_primary)
        binding.btnProcessComplaintNotification.text = "Proses"

        // on click button process
        var flag = 1
        binding.btnProcessComplaintNotification.setOnClickListener {
            if (flag == 1) {
                binding.btnProcessComplaintNotification.isEnabled = false
                if ("" + binding.etReplyFromLeader.editText?.text.toString() == "") {
                    Toast.makeText(
                        this,
                        "Harap isi balasan pengawas terlebih dahulu.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showLoading(getString(R.string.loading_string2), "btnProcess")
                }
            }
            flag = 0
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onProgressComplaint() {
        binding.llProcessComplaintNotif.visibility = View.VISIBLE
        binding.rlButtonProcessComplaintNotification.visibility = View.GONE
        binding.rlButtonSubmitComplaintNotification.visibility = View.GONE

        if (beforeImage == null) {
            binding.rlUploadProcessImageComplaintNotif.setOnClickListener {
                Toast.makeText(this, "Upload foto before dahulu", Toast.LENGTH_SHORT).show()
            }
            binding.rlUploadAfterImageComplaintNotif.setOnClickListener {
                Toast.makeText(this, "Upload foto before dahulu", Toast.LENGTH_SHORT).show()
            }

            binding.rlUploadBeforeImageComplaintNotif.setOnClickListener {
                // set camera permission
                val permission = ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                )
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.CAMERA),
                        CAMERA_REQ
                    )
                } else {
                    takeImage()
                    onUploadClick = "before"
                }
            }
        }
        else {
            if (beforeImage != null) {
                // set before image
                binding.ivBeforeDefault.visibility = View.GONE
                binding.ivBeforeComplaintNotification.visibility = View.VISIBLE
                setImages(beforeImage, binding.ivBeforeComplaintNotification)

                if (processImage != null) {
                    // set process image
                    binding.ivProcessDefault.visibility = View.GONE
                    binding.ivProcessComplaintNotification.visibility = View.VISIBLE
                    setImages(processImage, binding.ivProcessComplaintNotification)

                    if (afterImage != null) {
                        // set after image
                        binding.ivAfterDefault.visibility = View.GONE
                        binding.ivAfterComplaintNotification.visibility = View.VISIBLE
                        setImages(afterImage, binding.ivAfterComplaintNotification)

                        binding.rlButtonProcessComplaintNotification.visibility = View.GONE
                        binding.rlButtonSubmitComplaintNotification.visibility =
                            View.VISIBLE
                        binding.rlButtonSubmitComplaintNotification.setBackgroundResource(R.drawable.bg_primary)

                        // on click button submit
                        var flag = 1
                        binding.btnSubmitComplaintNotification.setOnClickListener {
                            if (flag == 1) {
                                binding.btnSubmitComplaintNotification.isEnabled = false
                                showLoading(getString(R.string.loading_string2), "btnSubmit")
                            }
                            flag = 0
                        }
                    } else {
                        binding.ivAfterDefault.visibility = View.VISIBLE
                        binding.ivAfterComplaintNotification.visibility = View.GONE

                        // set on click upload after image
                        binding.ivAfterDefault.visibility = View.VISIBLE
                        binding.ivAfterComplaintNotification.visibility = View.GONE
                        binding.rlUploadAfterImageComplaintNotif.setOnClickListener {
                            // set camera permission
                            val permission = ContextCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.CAMERA
                            )
                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(android.Manifest.permission.CAMERA),
                                    CAMERA_REQ
                                )
                            } else {
                                takeImage()
                                onUploadClick = "after"
                            }
                        }
                    }
                } else {
                    // set on click upload process image
                    binding.ivProcessDefault.visibility = View.VISIBLE
                    binding.ivProcessComplaintNotification.visibility = View.GONE
                    binding.rlUploadProcessImageComplaintNotif.setOnClickListener {
                        // set camera permission
                        val permission = ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.CAMERA
                        )
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(android.Manifest.permission.CAMERA),
                                CAMERA_REQ
                            )
                        } else {
                            takeImage()
                            onUploadClick = "process"
                        }
                    }
                    binding.rlUploadAfterImageComplaintNotif.setOnClickListener {
                        Toast.makeText(
                            this,
                            "Upload foto proses dahulu",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun createTempFiles(bitmap: Bitmap): File? {
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "process-complaint.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos)
        val bitmapdata = bos.toByteArray()
        // write the bytes in file
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

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    Glide.with(applicationContext).load(uri).into(binding.ivResultUploadComplaint)

                    binding.ablComplaintNotif.visibility = View.GONE
                    binding.layoutComplaintNotification.visibility = View.GONE
                    binding.llResultUploadImage.visibility = View.VISIBLE

                    // button upload before, process, after image
                    var flag = 1
                    binding.btnUploadImageComplaint.setOnClickListener {
                        if (flag == 1) {
                            binding.btnUploadImageComplaint.isEnabled = false
                            if (binding.ivResultUploadComplaint.drawable == null) {
                                Toast.makeText(
                                    applicationContext,
                                    "Image not ready",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                showLoading(getString(R.string.loading_string2), "btnUploadPhoto")
                            }
                        }
                        flag = 0
                    }
                }
            } else {
                onBackPressed()
            }
        }

    private fun takeImage() {
        lifecycleScope.launchWhenCreated {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            applicationContext, "${BuildConfig.APPLICATION_ID}.provider", tmpFile
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
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

    companion object {
        //QR cam code Req
        private const val CAMERA_REQ = 101
    }

    private fun chooseOpsDialog() {
        val dialog = ChooseOperatorDialog()
        dialog.show(supportFragmentManager, "ChooseOperatorDialog")
    }

    private fun setImages(image: String?, imageView: ImageView) {
        applicationContext.let {
            Glide.with(it)
                .load(getString(R.string.url) + "assets.admin_master/images/complaint/$image")
                .apply(RequestOptions.centerCropTransform())
                .into(imageView)
        }
    }

    private fun getPhotoProfile(imageView: ImageView, image: String?) {
        val url = this.getString(R.string.url) + "assets.admin_master/images/photo_profile/$image"
        if (image == "null" || image == null || image == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource = this.resources.getIdentifier(uri, null, this.packageName)
            val res = this.resources.getDrawable(imaResource)
            imageView.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(imageView)
        }
    }

    private fun loadData() {
        viewModel.getDetailProcessComplaint(complaintId)
        Log.d("ComplaintNotificationA", "loadData: complaint id = $complaintId")
    }

    private fun showLoading(loadingText: String, clickFrom: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        when(clickFrom) {
            "btnProcess" -> viewModel.putProcessComplaint(
                complaintId,
                employeeId,
                "" + binding.etReplyFromLeader.editText?.text.toString()
            )
            "btnUploadPhoto" -> loadUploadPhoto()
            "btnSubmit" -> viewModel.putSubmitComplaint(complaintId, employeeId)
        }
    }

    private fun loadUploadPhoto() {
        // bitmap
        val bitmap: Bitmap =
            (binding.ivResultUploadComplaint.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val imageUpload = MultipartBody.Part.Companion.createFormData(
            "file",
            file?.name,
            reqFile!!
        )
        when(onUploadClick) {
            "before" -> viewModel.putBeforeImageComplaint(complaintId, employeeId, imageUpload)
            "process" -> viewModel.putProgressImageComplaint(complaintId, employeeId, imageUpload)
            "after" -> viewModel.putAfterImageComplaint(complaintId, employeeId, imageUpload)
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        if (intentNotif == "notification"){
            CarefastOperationPref.saveString(CarefastOperationPrefConst.NOTIF_INTENT, "")
            setResult(Activity.RESULT_OK, Intent())
            finish()
        } else {
            onBackPressed()
            finish()
        }
//        super.onBackPressed()
//        finish()
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION, 0)
    }
}