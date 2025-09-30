package com.hkapps.hygienekleen.features.features_management.complaint.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailComplaintManagementBinding
import com.hkapps.hygienekleen.features.features_client.complaint.viewmodel.ClientComplaintViewModel
import com.hkapps.hygienekleen.features.features_management.complaint.viewmodel.ComplaintManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class DetailComplaintManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailComplaintManagementBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val complaintId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION, 0)
    private var processBy: Int? = null
    private var workerId: Int? = null
    private var beforeImage: String? = null
    private var processImage: String? = null
    private var afterImage: String? = null
    private var workerName: String? = null
    private var clientId: Int? = null
    private var loadingDialog: Dialog? = null

    private val viewModelClient: ClientComplaintViewModel by lazy {
        ViewModelProviders.of(this).get(ClientComplaintViewModel::class.java)
    }

    private val viewModel: ComplaintManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ComplaintManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailComplaintManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set project name
        if (clickFrom == "otherProject") {
            binding.tvProjectDetailComplaintManagement.visibility = View.VISIBLE
        } else {
            binding.tvProjectDetailComplaintManagement.visibility = View.GONE
        }

        //set app bar client
        if (levelJabatan == "CLIENT"){
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarDetailComplaintManagement.llAppbar.background = getDrawable(R.color.secondary_color)
        }

        // set appbar layout
        binding.appbarDetailComplaintManagement.tvAppbarTitle.text = "Komplain"
        binding.appbarDetailComplaintManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
        }

        loadData()
        setObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getDetailComplaintManagementResponse().observe(this) { it ->
            if (it.code == 200) {
                binding.tvTitleDetailComplaintManagement.text = it.data.title
                binding.tvDateDetailComplaintManagement.text = it.data.date
                binding.tvTimeDetailComplaintManagement.text = it.data.time
                binding.tvLocationDetailComplaintManagement.text = it.data.locationName
                binding.tvSubLocDetailComplaintManagement.text = it.data.subLocationName
                binding.tvNoteDetailComplaintManagement.text = it.data.description
                binding.tvEscalationInDetailComplaintManagement.text = "Eskalasi: " + it.data.notificationLevel
                binding.tvProjectDetailComplaintManagement.text = it.data.projectName

                // set photo complaint
                val image = it.data.image
                val url = getString(R.string.url) + "assets.admin_master/images/complaint/"
                applicationContext.let {
                    Glide.with(it)
                        .load(url + image)
                        .apply(RequestOptions.centerCropTransform())
                        .into(binding.ivDetailComplaintManagement)
                }

                // info client (pembuat komplain)
                binding.tvClientNameDetailComplaintManagement.text = if (it.data.clientName == null || it.data.clientName == "" || it.data.clientName == "null") {
                    it.data.createdByEmployeeName
                } else {
                    it.data.clientName
                }
                getPhotoProfile(binding.ivClientDetailComplaintManagement, it.data.clientPhotoProfile)

                if (it.data.doneAtDate != null) {
                    binding.tvDateDoneDetailComplaintManagement.text = it.data.doneAtDate
                } else {
                    binding.tvDateDoneDetailComplaintManagement.text = ""
                }

                if (it.data.doneAtTime != null) {
                    binding.tvTimeDoneDetailComplaintManagement.text = it.data.doneAtTime
                } else {
                    binding.tvTimeDoneDetailComplaintManagement.text = ""
                }

                if (it.data.comments == "null" || it.data.comments == null || it.data.comments == "") {
                    binding.tvReplyFromLeaderDetailComplaintManagement.text = "Tidak ada balasan"
                } else {
                    binding.tvReplyFromLeaderDetailComplaintManagement.text = "" + it.data.comments
                }

                val imageList = ArrayList<SlideModel>() // create image list
                imageList.add(
                    SlideModel(
                        getString(R.string.url) + "assets.admin_master/images/complaint/" + it.data.image,
                        "CTalk 1."
                    )
                )

                if (it.data.imageTwo == null || it.data.imageTwo == "") {
                    //donoting
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

                binding.ivDetailComplaintManagement.setOnClickListener {
                    //pop up modal
                    val dialog = this.let { Dialog(it) }
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_custom_image_slide)
                    val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
                    val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView

                    ivZoom.setImageDrawable(binding.ivDetailComplaintManagement.drawable)
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
                clientId = it.data.createdByEmployeeId
                processBy = it.data.processBy
                workerId = it.data.workerId
                beforeImage = it.data.beforeImage
                processImage = it.data.processImage
                afterImage = it.data.afterImage
                workerName = if (it.data.worker == null) {
                    null
                } else {
                    it.data.worker.employeeName
                }

                // set processBy data
                binding.tvOpsNameDetailComplaintManagement.text = it.data.processByEmployeeName
                getPhotoProfile(binding.ivOpsDetailComplaintManagement, it.data.processByEmployeePhotoProfile)

                when(it.data.statusComplaint) {
                    "WAITING" -> {
                        binding.llProcessDetailComplaintManagement.visibility = View.GONE
                    }
                    "ON PROGRESS" -> {
                        binding.llProcessDetailComplaintManagement.visibility = View.VISIBLE
                        onProgressComplaint()
                    }
                    "DONE" -> {
                        binding.llProcessDetailComplaintManagement.visibility = View.VISIBLE
                        onDoneComplaint()
                    }
                    "CLOSE" -> {
                        binding.llProcessDetailComplaintManagement.visibility = View.VISIBLE
                        onCloseComplaint()
                    }
                    else -> {
                        Log.d("ComplaintNotifiAct", "setObserver: status complaint not defined")
                        Log.e("ComplaintNotifiAct", "setObserver: status complaint not defined")
                    }
                }
            }
        }
        viewModelClient.closeComplaintResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                Toast.makeText(this, "Berhasil menutup komplain", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, Intent())
                finish()
            } else {
                hideLoading()
                binding.btnCloseComplaintManagement.isEnabled = true
                Toast.makeText(this, "Gagal menutup komplain", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onCloseComplaint() {
        binding.btnCloseComplaintManagement.visibility = View.GONE
        binding.rlStatusDetailComplaintManagement.visibility = View.VISIBLE
        binding.tvStatusDetailComplaintManagement.text = "Tutup"

        // set images
        binding.ivBeforeDefaultDetailComplaintManagement.visibility = View.GONE
        binding.ivProcessDefaultDetailComplaintManagement.visibility = View.GONE
        binding.ivAfterDefaultDetailComplaintManagement.visibility = View.GONE

        binding.ivBeforeDetailComplaintManagement.visibility = View.VISIBLE
        binding.ivProcessDetailComplaintManagement.visibility = View.VISIBLE
        binding.ivAfterDetailComplaintManagement.visibility = View.VISIBLE

        setImages(beforeImage, binding.ivBeforeDetailComplaintManagement)
        setImages(processImage, binding.ivProcessDetailComplaintManagement)
        setImages(afterImage, binding.ivAfterDetailComplaintManagement)

        binding.cardBeforeDetailComplaintManagement.setOnClickListener {
            openImage(binding.ivBeforeDetailComplaintManagement.drawable)
        }
        binding.cardProcessDetailComplaintManagement.setOnClickListener {
            openImage(binding.ivProcessDetailComplaintManagement.drawable)
        }
        binding.cardAfterDetailComplaintManagement.setOnClickListener {
            openImage(binding.ivAfterDetailComplaintManagement.drawable)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onDoneComplaint() {
        if (clientId == userId) {
            binding.rlStatusDetailComplaintManagement.visibility = View.GONE
            binding.btnCloseComplaintManagement.visibility = View.VISIBLE
            var flag = 1
            binding.btnCloseComplaintManagement.setOnClickListener {
                if (flag == 1) {
                    binding.btnCloseComplaintManagement.isEnabled = false
                    showLoading(getString(R.string.loading_string2))
                }
                flag = 0
            }
        } else {
            binding.btnCloseComplaintManagement.visibility = View.GONE
            binding.rlStatusDetailComplaintManagement.visibility = View.VISIBLE
            binding.tvStatusDetailComplaintManagement.text = "Selesai"
        }

        // set images
        binding.ivBeforeDefaultDetailComplaintManagement.visibility = View.GONE
        binding.ivProcessDefaultDetailComplaintManagement.visibility = View.GONE
        binding.ivAfterDefaultDetailComplaintManagement.visibility = View.GONE

        binding.ivBeforeDetailComplaintManagement.visibility = View.VISIBLE
        binding.ivProcessDetailComplaintManagement.visibility = View.VISIBLE
        binding.ivAfterDetailComplaintManagement.visibility = View.VISIBLE

        setImages(beforeImage, binding.ivBeforeDetailComplaintManagement)
        setImages(processImage, binding.ivProcessDetailComplaintManagement)
        setImages(afterImage, binding.ivAfterDetailComplaintManagement)

        binding.cardBeforeDetailComplaintManagement.setOnClickListener {
            openImage(binding.ivBeforeDetailComplaintManagement.drawable)
        }
        binding.cardProcessDetailComplaintManagement.setOnClickListener {
            openImage(binding.ivProcessDetailComplaintManagement.drawable)
        }
        binding.cardAfterDetailComplaintManagement.setOnClickListener {
            openImage(binding.ivAfterDetailComplaintManagement.drawable)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onProgressComplaint() {
        binding.btnCloseComplaintManagement.visibility = View.GONE
        binding.rlStatusDetailComplaintManagement.visibility = View.VISIBLE
        binding.tvStatusDetailComplaintManagement.text = "Sedang Proses"
        if (beforeImage == null) {
            // set images
            binding.ivBeforeDefaultDetailComplaintManagement.visibility = View.VISIBLE
            binding.ivProcessDefaultDetailComplaintManagement.visibility = View.VISIBLE
            binding.ivAfterDefaultDetailComplaintManagement.visibility = View.VISIBLE

            binding.ivBeforeDetailComplaintManagement.visibility = View.GONE
            binding.ivProcessDetailComplaintManagement.visibility = View.GONE
            binding.ivAfterDetailComplaintManagement.visibility = View.GONE
        } else {
            // set images
            if (beforeImage != null) {
                binding.ivBeforeDefaultDetailComplaintManagement.visibility = View.GONE
                binding.ivBeforeDetailComplaintManagement.visibility = View.VISIBLE
                setImages(beforeImage, binding.ivBeforeDetailComplaintManagement)
                binding.cardBeforeDetailComplaintManagement.setOnClickListener {
                    openImage(binding.ivBeforeDetailComplaintManagement.drawable)
                }
                if (processImage != null) {
                    binding.ivProcessDefaultDetailComplaintManagement.visibility = View.GONE
                    binding.ivProcessDetailComplaintManagement.visibility = View.VISIBLE
                    setImages(processImage, binding.ivProcessDetailComplaintManagement)
                    binding.cardProcessDetailComplaintManagement.setOnClickListener {
                        openImage(binding.ivProcessDetailComplaintManagement.drawable)
                    }
                    if (afterImage != null) {
                        binding.ivAfterDefaultDetailComplaintManagement.visibility = View.GONE
                        binding.ivAfterDetailComplaintManagement.visibility = View.VISIBLE
                        setImages(afterImage, binding.ivProcessDetailComplaintManagement)
                        binding.cardAfterDetailComplaintManagement.setOnClickListener {
                            openImage(binding.ivAfterDetailComplaintManagement.drawable)
                        }
                    } else {
                        binding.ivAfterDefaultDetailComplaintManagement.visibility = View.VISIBLE
                        binding.ivAfterDetailComplaintManagement.visibility = View.GONE
                    }
                } else {
                    binding.ivProcessDefaultDetailComplaintManagement.visibility = View.VISIBLE
                    binding.ivProcessDetailComplaintManagement.visibility = View.GONE
                }
            } else {
                binding.ivBeforeDefaultDetailComplaintManagement.visibility = View.VISIBLE
                binding.ivBeforeDetailComplaintManagement.visibility = View.GONE
            }
        }
    }

    private fun setImages(image: String?, imageView: ImageView) {
//        applicationContext.let {
//            Glide.with(it)
//                .load(getString(R.string.url) + "assets.admin_master/images/complaint/$image")
//                .apply(RequestOptions.centerCropTransform())
//                .into(imageView)
//        }
        val requestOptions = RequestOptions()
            .centerCrop()
            .error(R.drawable.ic_error_image)

        Glide.with(this)
            .load(getString(R.string.url) + "assets.admin_master/images/complaint/$image")
            .apply(requestOptions)
            .into(imageView)
    }

    private fun getPhotoProfile(imageView: ImageView, image: String) {
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

    private fun openImage(img: Drawable) {
        val dialog = this.let { Dialog(it) }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_image_zoom)
        val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
        val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView

        ivZoom.setImageDrawable(img)

        close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun loadData() {
        viewModel.getDetailComplaintManagement(complaintId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        viewModelClient.putCloseComplaint(complaintId)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

}