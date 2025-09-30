package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityPeriodicManagementDetailBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.viewmodel.PeriodicManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.FormDiversionActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PeriodicManagementDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeriodicManagementDetailBinding
    private val viewModel: PeriodicManagementViewModel by lazy {
        ViewModelProviders.of(this).get(PeriodicManagementViewModel::class.java)
    }
    private var idJob =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_JOB, 0)
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT,"")
    private lateinit var escallationStats: MutableLiveData<String>
    private var statsEscallation: Int = 0
    private var loadingDialog: Dialog? = null
    private var dateSelect =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_FROM_MONTH_RKB, "")


    private var statsApproval: Boolean = false
    var statsPeriodicItem: String = ""
    var approveName: String = ""
    var approveJobCode: String = ""
    var approveDate: String = ""
    private var msg: String = ""
    private var oneTimeShow: Boolean = false

    private var zoomImageDialog: Dialog? = null
    private lateinit var currentPhotoPath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeriodicManagementDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appbarMonthlyWorkReportChecklist.tvAppbarTitle.text = "Ceklis Pekerjaan"
        binding.appbarMonthlyWorkReportChecklist.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        loadDataEscallation()
        escallationStats = MutableLiveData()
        showLoading("Loading..")
        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //oncreate
    }
    private fun loadDataEscallation(){
        viewModel.getEscallationManagement(projectCode)
    }

    private fun loadData() {
        viewModel.getDetailPeriodicManagement(idJob)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getEscallationManagementViewModel().observe(this){
            if (it.code == 200){
                escallationStats.value = it.message
            } else {
                escallationStats.value = it.message
            }
        }
        viewModel.getDetailPeriodicCalendarManagementViewModel().observe(this){
            if (it.code == 200) {
                binding.tvTypeJob.text = it?.data?.typeJob ?: "-"
                binding.tvDetailJob.text = it?.data?.detailJob ?: "-"
                binding.tvLocationChecklistRkb.text = it?.data?.locationName ?: "-"
                binding.tvSubAreaChecklistRkb.text = it?.data?.subLocationName ?: "-"
                binding.tvShiftNameChecklistRkb.text = it?.data?.shiftDesc ?: "-"

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
                    binding.clUploadBeforeImgPeriodic.visibility = View.GONE
                }
                if (!progressImage){
                    binding.llProcessRkb.visibility = View.VISIBLE
                    binding.clUploadProcessImgPeriodic.visibility = View.GONE
                }
                if (!afterImage){
                    binding.llAfterRkb.visibility = View.VISIBLE
                    binding.clUploadAfterImgPeriodic.visibility = View.GONE
                }


                //valdasi comments
                binding.tvCommentsBeforePeriodic.text = it?.data?.beforeComment ?: "-"
                binding.tvCommentsProcessPeriodic.text = it?.data?.progressComment ?: "-"
                binding.tvCommentsAfterPeriodic.text = it?.data?.afterComment ?: "-"

                binding.ivBeforeRkb.visibility = if (beforeImage) View.GONE else View.VISIBLE
                binding.ivBeforeRkbEmpty.visibility = if (beforeImage) View.VISIBLE else View.GONE

                binding.ivProcessRkb.visibility = if (progressImage) View.GONE else View.VISIBLE
                binding.ivProcessRkbEmpty.visibility =
                    if (progressImage) View.VISIBLE else View.GONE

                binding.ivAfterRkb.visibility = if (afterImage) View.GONE else View.VISIBLE
                binding.ivAfterRkbEmpty.visibility = if (afterImage) View.VISIBLE else View.GONE

                loadProfileImages(it.data.beforeImage, it.data.progressImage, it.data.afterImage)

                binding.nsPeriodicManagement.setOnScrollChangeListener { _, _, _, _, _ ->
                    if (binding.nsPeriodicManagement.canScrollVertically(1)){
                        binding.llBtnApproveJob.setBackgroundResource(R.drawable.gradient_sticky_button_background)
                    } else {
                        binding.llBtnApproveJob.setBackgroundResource(R.drawable.bg_white_card)
                    }
                }

                if (it.data.beforeImage.isNullOrEmpty() || it.data.progressImage.isNullOrEmpty() || it.data.afterImage.isNullOrEmpty()) {
                    binding.llBtnApproveJob.visibility = View.GONE
                } else {
                    escallationStats.value?.let { value ->
                        if (!it.data.approved && value == "Valid") {

                            binding.llBtnApproveJob.visibility = View.VISIBLE
                            binding.btnApprovalJob.apply {
                                visibility = View.VISIBLE
                                setOnClickListener {
                                    showLoading(getString(R.string.loading_string2))
                                    viewModel.putApproveJobManagements(idJob, userId)
                                }
                            }
                        } else {
                            binding.llBtnApproveJob.visibility = View.GONE
                        }
                    }

                    Log.d("agri","tes open 2 $escallationStats")

                }

                if (it.data.approved) {
                    binding.llBtnApproveJob.visibility = View.GONE
                    binding.btnApprovalJob.isEnabled = false
                }

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


                //btn create ba
                if (it.data.beforeImage.isNullOrEmpty()|| it.data.afterImage.isNullOrBlank() || it.data.progressImage.isNullOrBlank()) {
                    binding.btnCreateBa.isEnabled = true
                    binding.btnCreateBa.setOnClickListener {
//                            BotSheetCreateBAFragment().show(supportFragmentManager, "createba")
                        startActivity(Intent(this@PeriodicManagementDetailActivity,
                            FormDiversionActivity::class.java).also{
                            it.putExtra("idJob",idJob)
                            it.putExtra("true",true)
                        })
                    }
                } else {
                    binding.btnCreateBa.isEnabled = false
                    binding.btnCreateBa.setBackgroundResource(R.drawable.bg_rounded_grey_disable)
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

                //validasi after ba
                val itemsDate = if (it.data.itemDate == null) "" else it.data.itemDate
                val createdItemJob = convertDate(itemsDate)
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
                msg = sdf.format(Date())
                val baCreateToDate = if (it.data.divertedToDate == null) "" else it.data.divertedToDate.toString()
                Log.d("rkb","$dateSelect")
                val dateRkb = convertDate(dateSelect)

                //ba lewat datenow
                if (it.data.diverted && baCreateToDate < msg && it.data.statusPeriodic == "DIVERSION"){
                    showDialog(this, "Maaf, pekerjaan ini tidak bisa dikerjakan karena belum masuk tanggal pengalihan atau BA sudah lewat tanggal. BA hanya bisa dikerjakan pada tanggal pengalihan.")
                    binding.clUploadBeforeImgPeriodic.isEnabled = false
                    binding.clUploadProcessImgPeriodic.isEnabled = false
                    binding.clUploadAfterImgPeriodic.isEnabled = false
                    Log.d("fefe","kena $msg $baCreateToDate ll")

                } else if (it.data.diverted && msg != baCreateToDate && it.data.statusPeriodic == "DIVERSION") {
                    showDialog(this, "Maaf, pekerjaan ini tidak bisa dikerjakan karena belum masuk tanggal pengalihan atau BA sudah lewat tanggal. BA hanya bisa dikerjakan pada tanggal pengalihan.")
                    binding.clUploadBeforeImgPeriodic.isEnabled = false
                    binding.clUploadProcessImgPeriodic.isEnabled = false
                    binding.clUploadAfterImgPeriodic.isEnabled = false
                    Log.d("fefe","kena $msg $baCreateToDate")
                }
                Log.d("fefe","kena $msg $baCreateToDate")


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

                //lock rkb in date range
                if (it.data.diverted){
                    val dateDiverted = convertDates(it.data.divertedToDate)
                    lockDateInRange(dateDiverted)
                } else {
                    lockDateInRange(it.data.itemDate)
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
        viewModel.putApproveJobManagementViewModel().observe(this){
            if (it.code == 200){
                startActivity(Intent(this, PeriodicManagementCalendarActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Failed approve", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }

    }
    private fun lockDateInRange(targetDate: String) {
        // Define date format
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")

        // Convert target date string to a Date object
        val targetDateObj: Date = dateFormat.parse(targetDate)

        // Get today's date
        val today: Date = Calendar.getInstance().time

        // Get yesterday's date
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -2)

        // Get tomorrow's date
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DATE, 2)

        // Check if the target date is between yesterday and tomorrow
        if (targetDateObj.after(yesterday.time) && targetDateObj.before(tomorrow.time)) {
            // Perform your action or show toast here
            Log.d("caca","date in range")
        } else {
            // Target date is not within the specified range
            Log.d("caca","date not in range")
            binding.clUploadBeforeImgPeriodic.apply {
                isClickable = false
            }
            binding.clUploadProcessImgPeriodic.apply {
                isClickable = false
            }
            binding.clUploadAfterImgPeriodic.apply {
                isClickable = false
            }
        }
    }
    private fun convertDate(inputDateStr: String): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        val outputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))

        try {
            val date = inputDateFormat.parse(inputDateStr)
            return outputDateFormat.format(date)
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    private fun convertDates(inputDateStr: String): String {
        val inputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val outputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))

        try {
            val date = inputDateFormat.parse(inputDateStr)
            return outputDateFormat.format(date)
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }


    private fun showDialog(context: Context, text: String) {

            val dialog = let { Dialog(it) }
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            dialog.setContentView(R.layout.dialog_rkb_ba_failure)

            val textFailure = dialog.findViewById<TextView>(R.id.tvRkbDialogBaFailure)
            val btnDismiss = dialog.findViewById<RelativeLayout>(R.id.btnRkbFailure)
            textFailure.text = text
            btnDismiss.setOnClickListener {
                dialog.dismiss()
            }


            dialog.show()

//        oneTimeShow = true

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
    private fun loadProfileImages(img1: String, img2: String, img3: String) {
        loadImage(img1, binding.ivBeforeRkb, binding.progressBarRkb)
        loadImage(img2, binding.ivProcessRkb, binding.progressBarProgressRkb)
        loadImage(img3, binding.ivAfterRkb, binding.progressBarAfterRkb)
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


    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
        loadingDialog?.setCancelable(false)
    }
    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            setResult(RESULT_OK)
            finish()
        }

    }

}