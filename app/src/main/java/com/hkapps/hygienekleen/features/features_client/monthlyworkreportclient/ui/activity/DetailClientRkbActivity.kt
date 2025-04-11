package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailClientRkbBinding
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.viewmodel.MonthlyWorkClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class DetailClientRkbActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailClientRkbBinding
    private val viewModel: MonthlyWorkClientViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkClientViewModel::class.java)
    }
    private val idJobs =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_JOB, 0)
    private var zoomImageDialog: Dialog? = null
    var approveName: String = ""
    var approveJobCode: String = ""
    var approveDate: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailClientRkbBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this,R.color.secondary_color)

        binding.appbarMonthlyWorkReportClient.tvAppbarTitle.text = "Ceklis Pekerjaan"
        binding.appbarMonthlyWorkReportClient.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        loadData()
        setObserver()

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun loadData() {
        viewModel.getDetailJobRkbClient(idJobs)
    }

    private fun setObserver() {
        viewModel.getDetailJobRkbClientViewModel().observe(this){
            if (it.code == 200){
                binding.tvTypeJob.text = if (it.data.typeJob.isNullOrEmpty()){
                    ""
                } else {
                    it.data.typeJob
                }
                binding.tvDetailJobClient.text = if(it.data.detailJob.isNullOrEmpty()){
                    ""
                } else {
                    it.data.detailJob
                }
                binding.tvLocationChecklistRkb.text = if (it.data.locationName.isNullOrEmpty()) {
                    ""
                } else {
                    it.data.locationName
                }
                binding.tvSubAreaChecklistRkb.text = if (it.data.subLocationName.isNullOrEmpty()) {
                    ""
                } else {
                    it.data.subLocationName
                }
                binding.tvFinishJobDateChecklistRkb.text =
                    if (it.data.finishDate.isNullOrEmpty()) "-" else it.data.finishDate
                when (it.data.typeJob) {
                    "DAILY" -> {
                        binding.tvTypeJob.setBackgroundResource(R.drawable.bg_rounded_skyblue)
                    }
                    "WEEKLY" -> {
                        binding.tvTypeJob.setBackgroundResource(R.drawable.bg_rounded_orange)
                    }
                    "MONTHLY" -> {
                        binding.tvTypeJob.setBackgroundResource(R.drawable.bg_rounded_purple)
                    }
                }
                //load images process
                val beforeImage = it.data.beforeImage.isNullOrEmpty()
                val progressImage = it.data.progressImage.isNullOrEmpty()
                val afterImage = it.data.afterImage.isNullOrEmpty()

                if (!beforeImage){
                    binding.llBeforeRkb.visibility = View.VISIBLE
//                    binding.clUploadBeforeImgPeriodic.visibility = View.GONE
                }
                if (!progressImage){
                    binding.llProcessRkb.visibility = View.VISIBLE
//                    binding.clUploadProcessImgPeriodic.visibility = View.GONE
                }
                if (!afterImage){
                    binding.llAfterRkb.visibility = View.VISIBLE
//                    binding.clUploadAfterImgPeriodic.visibility = View.GONE
                }
                //valdasi comments
                binding.tvCommentsBeforePeriodic.text = if (it.data.beforeComment.isNullOrEmpty()){
                    "-"
                } else {
                    it.data.beforeComment
                }
                binding.tvCommentsProcessPeriodic.text = if (it.data.progressComment.isNullOrEmpty()){
                    "-"
                } else {
                    it.data.progressComment
                }
                binding.tvCommentsAfterPeriodic.text = if (it.data.afterComment.isNullOrEmpty()){
                    "-"
                } else {
                    it.data.afterComment
                }

                binding.ivBeforeRkb.visibility = if (beforeImage) View.GONE else View.VISIBLE
                binding.ivBeforeRkbEmpty.visibility = if (beforeImage) View.VISIBLE else View.GONE

                binding.ivProcessRkb.visibility = if (progressImage) View.GONE else View.VISIBLE
                binding.ivProcessRkbEmpty.visibility =
                    if (progressImage) View.VISIBLE else View.GONE

                binding.ivAfterRkb.visibility = if (afterImage) View.GONE else View.VISIBLE
                binding.ivAfterRkbEmpty.visibility = if (afterImage) View.VISIBLE else View.GONE

                loadProfileImages(it.data.beforeImage, it.data.progressImage, it.data.afterImage)

                if (it.data.approvedById == null || it.data.approvedById == "") {
                    approveName = if (it.data.approvedByNameManagement.isNullOrEmpty()) "" else it.data.approvedByNameManagement
                    approveJobCode = if (it.data.approvedByJobCodeManagement.isNullOrEmpty()) "" else it.data.approvedByJobCodeManagement
                    approveDate = if (it.data.approvedDateManagement.isNullOrEmpty()) "" else it.data.approvedDateManagement
                } else {
                    approveName = if (it.data.approvedByName.isNullOrEmpty()) "" else it.data.approvedByName
                    approveJobCode = if (it.data.approvedByJobCode.isNullOrEmpty()) "" else it.data.approvedByJobCode
                    approveDate = if (it.data.approvedDate.isNullOrEmpty()) "" else it.data.approvedDate
                }
                if (it.data.approved) {
                    binding.ivDotStatusApproval.setImageResource(R.drawable.ic_checked_rkb)
                    binding.tvStatsResult.text =
                        "Disetujui oleh $approveName (${approveJobCode})\n" +
                                approveDate
                } else {
                    binding.tvStatsResult.text =
                        "Menunggu persetujuan penanggung jawab atau pengawas tertinggi"
                    binding.ivDotStatusApproval.setImageResource(R.drawable.ic_uncheck_rkb)
                }
                //ic detail validation
                // validasi icon
                if (it.data.approved) {
                    binding.ivStampDetailRkb.apply {
                        setImageResource(R.drawable.ic_stamp)
                        visibility = View.VISIBLE
                    }
                }
                if (!it.data.approved && it.data.done) {
                    binding.ivStampDetailRkb.apply {
                        setImageResource(R.drawable.ic_stamp_disable)
                        visibility = View.VISIBLE
                    }
                }
                if (it.data.done) {
                    binding.ivCheckjobDetailRkb.apply {
                        setImageResource(R.drawable.ic_checklist_radial)
                        visibility = View.VISIBLE
                    }
                }
                if (it.data.diverted) {
                    binding.ivBaDetailRkb.apply {
                        setImageResource(R.drawable.ic_ba)
                        visibility = View.VISIBLE
                    }
                }
                if (it.data.statusPeriodic == "NOT_REALITATION") {
                    binding.ivMessageDetailRkb.apply {
                        setImageResource(R.drawable.ic_realization_rkb)
                        visibility = View.VISIBLE
                    }
                }

                //validasi result ba
                if (it.data.diverted) {
                    binding.rlBtnCreateBa.visibility = View.GONE
                    binding.clResultBa.visibility = View.VISIBLE

                    binding.tvResultBa.setText(it.data.divertedDesc)

                    binding.tvResultDateBa.text =
                        if (it.data.divertedByDate == null || it.data.divertedByDate == "") "-" else "Dibuat pada: ${it.data.divertedByDate}"

                    //redirected diverted date
                    binding.tvRedirectedDateBa.text =
                        if (it.data.divertedToDate == null || it.data.divertedToDate == "") "-" else "Dialihkan ke tanggal: ${it.data.divertedToDate}"

                    binding.tvResultCreatedByBa.text =
                        if (it.data.divertedByName == "") "" else "oleh: ${it.data.divertedByName}"

                    loadImage(it.data.divertedImage, binding.ivDivertedImage, binding.progresbarBa)
                    val divertedImg = it.data.divertedImage
                    binding.ivDivertedImage.setOnClickListener {
                        zoomImage(divertedImg)
                    }
                }
                val beforeZoomImg = it.data.beforeImage
                val processZoomImg = it.data.progressImage
                val afterZoomImg = it.data.afterImage
                //zoom dialog
                binding.ivBeforeRkb.setOnClickListener {
                    zoomImage(beforeZoomImg)
                }
                binding.ivProcessRkb.setOnClickListener {
                    zoomImage(processZoomImg)
                }
                binding.ivAfterRkb.setOnClickListener {
                    zoomImage(afterZoomImg)
                }




            }
        }
    }

    private fun zoomImage(img: String?) {
        zoomImageDialog = Dialog(this)
        zoomImageDialog?.setContentView(R.layout.dialog_zoom_image)
        zoomImageDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val zoomImg = zoomImageDialog?.findViewById<ImageView>(R.id.ivImageZoomRkb)
        val closeZoom = zoomImageDialog?.findViewById<ImageView>(R.id.ivCloseZoomRkb)

        val requestOptions = RequestOptions()
            .centerCrop()
            .error(R.drawable.ic_error_image)


        Glide.with(this)
            .load(getString(R.string.url) + "assets.admin_master/images/rkb/$img")
//            .load(getString(R.string.url) + "rkb/$img")
            .apply(requestOptions)
            .into(zoomImg!!)


        closeZoom?.setOnClickListener {
            zoomImageDialog?.dismiss()
        }

        // Show the zoom-in dialog
        zoomImageDialog?.show()
    }

    private fun loadImage(img: String?, imageView: ImageView, progressBar: ProgressBar) {
        // Show the progress bar for the current image while loading
        progressBar.visibility = View.VISIBLE

        if (img == "null" || img == null || img == "") {
            val uri = "@drawable/ic_camera_black" // Replace with your default image
            val imageResource = resources.getIdentifier(uri, null, this.packageName)
            val res = resources.getDrawable(imageResource)
            imageView.setImageDrawable(res)
            // Hide the progress bar when the default image is set
            progressBar.visibility = View.GONE
        } else {
            val url = getString(R.string.url) + "assets.admin_master/images/rkb/$img"
//            val url = getString(R.string.url) + "rkb/$img"

            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Because file name is always the same
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Handle image loading failure here (e.g., show an error message)
                        progressBar.visibility = View.GONE // Hide the progress bar on failure
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Hide the progress bar for the current image when it is loaded successfully
                        progressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(imageView)
        }
    }

    private fun loadProfileImages(img1: String, img2: String, img3: String) {
        loadImage(img1, binding.ivBeforeRkb, binding.progressBarRkb)
        loadImage(img2, binding.ivProcessRkb, binding.progressBarProgressRkb)
        loadImage(img3, binding.ivAfterRkb, binding.progressBarAfterRkb)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }
}