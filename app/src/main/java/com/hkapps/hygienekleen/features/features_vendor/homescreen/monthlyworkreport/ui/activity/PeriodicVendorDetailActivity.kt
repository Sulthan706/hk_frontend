package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityChecklistMonthlyWorkReportBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.FormDiversionActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.StatusAbsenViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import de.hdodenhof.circleimageview.CircleImageView
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PeriodicVendorDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChecklistMonthlyWorkReportBinding
    private val viewModelMonthlyWork: MonthlyWorkReportViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkReportViewModel::class.java)
    }
    private val statusAbsenViewModel: StatusAbsenViewModel by lazy {
        ViewModelProviders.of(this).get(StatusAbsenViewModel::class.java)
    }
    private var idJob =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_JOB, 0)
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var userLevel =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var userProjectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var dateSelect =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_FROM_MONTH_RKB, "")

    private var loadingDialog: Dialog? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_CAPTURE_2 = 2
    private val REQUEST_IMAGE_CAPTURE_3 = 3

    var approveName: String = ""
    var approveJobCode: String = ""
    var approveDate: String = ""
    private var msg: String = ""
    var beforeImage: Boolean = false
    var progressImage: Boolean = false
    var afterImage: Boolean = false



    private var statsApproval: Boolean = false
    var escalationStats: String = ""
    var statsPeriodicItem: String = ""
    private var oneTimeShow: Boolean = false
    private var oneTimeToast: Boolean = false

    private var zoomImageDialog: Dialog? = null
    private lateinit var currentPhotoPath: String

    private var i : Boolean = false



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChecklistMonthlyWorkReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        i = intent.getBooleanExtra("from_closing",false)

        binding.appbarMonthlyWorkReportChecklist.tvAppbarTitle.text = "Ceklis Pekerjaan"
        binding.appbarMonthlyWorkReportChecklist.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }


        showLoading("Loading..")
        loadData()
        setObserver()

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //oncreate
    }

    private fun loadData() {
        statusAbsenViewModel.statusTIMEInOut(userId, userProjectCode)
        viewModelMonthlyWork.getLimitasiCreateBa(userId, idJob)
        viewModelMonthlyWork.getEscalationLowLevel(userId, userProjectCode)
        viewModelMonthlyWork.getDetailChecklist(idJob)
        viewModelMonthlyWork.getStatusApprovalJobs(userId)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setObserver() {
        statusAbsenViewModel.statusAttendanceTime().observe(this){
            if (it.code == 200){
                if (it.message == "Your schedule not found or something wrong"){
                    binding.clUploadBeforeImgPeriodic.visibility = View.GONE
                    binding.clUploadProcessImgPeriodic.visibility = View.GONE
                    binding.clUploadAfterImgPeriodic.visibility = View.GONE

                    binding.clUploadBeforeImgPeriodicLate.visibility = if (!beforeImage) View.GONE else View.VISIBLE
                    binding.clUploadProcessImgPeriodicLate.visibility = if (!progressImage) View.GONE else View.VISIBLE
                    binding.clUploadAfterImgPeriodicLate.visibility = if (!afterImage) View.GONE else View.VISIBLE

                    binding.clUploadBeforeImgPeriodicLate.setOnClickListener {
                        Toast.makeText(this, "Silahkan absen terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }
                    binding.clUploadProcessImgPeriodicLate.setOnClickListener {
                        Toast.makeText(this, "Silahkan absen terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }
                    binding.clUploadAfterImgPeriodicLate.setOnClickListener {
                        Toast.makeText(this, "Silahkan absen terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }

                    binding.btnReplaceBeforePeriodic.visibility = View.GONE
                    binding.btnReplaceProgressPeriodic.visibility = View.GONE
                    binding.btnReplaceAfterPeriodic.visibility = View.GONE
                    binding.btnCreateBa.apply { isEnabled = false }
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModelMonthlyWork.getLimitasiCreateBaViewModel().observe(this){
            val message = it.message
            if (it.code == 200){
                binding.btnCreateBaDisable.visibility = View.GONE
                binding.btnCreateBa.visibility = View.VISIBLE
            } else {
                binding.btnCreateBaDisable.visibility = View.VISIBLE
                binding.btnCreateBaDisable.setOnClickListener {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModelMonthlyWork.putApproveJobViewModel().observe(this) {
            if (it.code == 200) {
               if(i){
                   setResult(RESULT_OK)
                   finish()
                   Toast.makeText(this, "Approved", Toast.LENGTH_SHORT).show()
               }else{
                   Toast.makeText(this, "Approved", Toast.LENGTH_SHORT).show()
                   startActivity(Intent(this, PeriodicVendorCalendarActivity::class.java))
                   finish()
               }
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
        viewModelMonthlyWork.putJobRkbViewModel().observe(this) {
            if (it.code == 200) {
                val userNames = it.data.uploadByName
                val userPositions = it.data.jobCode
                val createDates = it.data.uploadDate
                val userImg = it.data.uploadByImage
                showDialogSuccess(userNames, userPositions, createDates, userImg)
            } else {
                Toast.makeText(this, "Gagal upload", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
        viewModelMonthlyWork.getDetailChecklistRkbViewModel().observe(this) {
            if (it.code == 200) {
                binding.tvTypeJob.text = it?.data?.typeJob ?: "-"
                binding.tvDetailJob.text = it?.data?.detailJob ?: "-"
                binding.tvLocationChecklistRkb.text = it?.data?.locationName ?: "-"
                binding.tvSubAreaChecklistRkb.text = it?.data?.subLocationName ?: "-"
                binding.tvShiftNameChecklistRkb.text = it?.data?.shiftDesc ?: "-"

                //validasi ba from item created
                CarefastOperationPref.saveString(CarefastOperationPrefConst.BA_CREATED_ITEM, it.data.itemDate)

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
                beforeImage = it.data.beforeImage.isNullOrEmpty()
                progressImage = it.data.progressImage.isNullOrEmpty()
                afterImage = it.data.afterImage.isNullOrEmpty()

                if (!beforeImage){
                    binding.llBeforeRkb.visibility = View.VISIBLE
                    binding.clUploadBeforeImgPeriodic.visibility = View.GONE
                    binding.clUploadBeforeImgPeriodicLate.visibility = View.GONE
                }
                if (!progressImage){
                    binding.llProcessRkb.visibility = View.VISIBLE
                    binding.clUploadProcessImgPeriodic.visibility = View.GONE
                    binding.clUploadProcessImgPeriodicLate.visibility = View.GONE
                }
                if (!afterImage){
                    binding.llAfterRkb.visibility = View.VISIBLE
                    binding.clUploadAfterImgPeriodic.visibility = View.GONE
                    binding.clUploadAfterImgPeriodicLate.visibility = View.GONE
                }
                //valdasi comments
                binding.tvCommentsBeforePeriodic.text = it?.data?.beforeComment ?: ""
                binding.tvCommentsProcessPeriodic.text = it?.data?.progressComment ?: ""
                binding.tvCommentsAfterPeriodic.text = it?.data?.afterComment ?: ""




                binding.ivBeforeRkb.visibility = if (beforeImage) View.GONE else View.VISIBLE
                binding.ivBeforeRkbEmpty.visibility = if (beforeImage) View.VISIBLE else View.GONE

                binding.ivProcessRkb.visibility = if (progressImage) View.GONE else View.VISIBLE
                binding.ivProcessRkbEmpty.visibility =
                    if (progressImage) View.VISIBLE else View.GONE

                binding.ivAfterRkb.visibility = if (afterImage) View.GONE else View.VISIBLE
                binding.ivAfterRkbEmpty.visibility = if (afterImage) View.VISIBLE else View.GONE

                loadProfileImages(it.data.beforeImage, it.data.progressImage, it.data.afterImage)

                //button approval
                if (!statsApproval) {
                    binding.btnApprovalJob.visibility  = View.GONE
                } else {
                    if (it.data.beforeImage.isNullOrEmpty() || it.data.progressImage.isNullOrEmpty() || it.data.afterImage.isNullOrEmpty() || it.data.approved) {
                        binding.btnApprovalJob.visibility = View.GONE
                    } else {
                        binding.btnApprovalJob.visibility = View.VISIBLE
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
                    binding.btnReplaceBeforePeriodic.visibility = View.GONE
                    binding.btnReplaceProgressPeriodic.visibility = View.GONE
                    binding.btnReplaceAfterPeriodic.visibility = View.GONE
                    binding.clUploadBeforeImgPeriodic.isEnabled = false
                    binding.clUploadProcessImgPeriodic.isEnabled = false
                    binding.clUploadAfterImgPeriodic.isEnabled = false
                } else if (it.data.diverted && msg != baCreateToDate && it.data.statusPeriodic == "DIVERSION") {
                    showDialog(this, "Maaf, pekerjaan ini tidak bisa dikerjakan karena belum masuk tanggal pengalihan atau BA sudah lewat tanggal. BA hanya bisa dikerjakan pada tanggal pengalihan.")
                    binding.btnReplaceBeforePeriodic.visibility = View.GONE
                    binding.btnReplaceProgressPeriodic.visibility = View.GONE
                    binding.btnReplaceAfterPeriodic.visibility = View.GONE
                    binding.clUploadBeforeImgPeriodic.isEnabled = false
                    binding.clUploadProcessImgPeriodic.isEnabled = false
                    binding.clUploadAfterImgPeriodic.isEnabled = false
                }


                binding.btnApprovalJob.setOnClickListener {
                    viewModelMonthlyWork.putApproveJob(idJob, userId)
                }


                //validasi show approve by
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
                    //validasi button replace
                    binding.btnReplaceBeforePeriodic.visibility = View.GONE
                    binding.btnReplaceProgressPeriodic.visibility = View.GONE
                    binding.btnReplaceAfterPeriodic.visibility = View.GONE
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
                if (it.data.statusPeriodic == "NOT_REALITATION") {
                    binding.ivBeforeRkbEmpty.apply {
                        isClickable = false
                    }
                    binding.ivProcessRkbEmpty.apply {
                        isClickable = false
                    }
                    binding.ivAfterRkbEmpty.apply {
                        isClickable = false
                    }
                } else {
                    // validation input photo
                    val buttons = listOf(binding.clUploadBeforeImgPeriodic, binding.clUploadProcessImgPeriodic, binding.clUploadAfterImgPeriodic)
                    val uploadTypes = listOf("BEFORE", "PROGRESS", "AFTER")

                    for (i in buttons.indices){
                        val button = buttons[i]
                        val uploadType = uploadTypes[i]

                        button.setOnClickListener{
                            val intent = Intent(this, PeriodicVendorResultImageActivity::class.java)
                            val bundle = Bundle()
                            bundle.putInt("idJob", idJob)
                            bundle.putString("uploadType", uploadType)
                            intent.putExtra("bundle", bundle)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                //replace foto
                val buttons = listOf(binding.btnReplaceBeforePeriodic, binding.btnReplaceProgressPeriodic, binding.btnReplaceAfterPeriodic)
                val uploadTypes = listOf("BEFORE", "PROGRESS", "AFTER")

                for (i in buttons.indices) {
                    val button = buttons[i]
                    val uploadType = uploadTypes[i]

                    button.setOnClickListener {
                        val intent = Intent(this, PeriodicVendorResultImageActivity::class.java)
                        val bundle = Bundle()
                        bundle.putInt("idJob", idJob)
                        bundle.putString("uploadType", uploadType)
                        intent.putExtra("bundle", bundle)
                        startActivity(intent)
                        finish()
                    }
                }

                if(it.data.beforeImage.isNullOrBlank() || it.data.afterImage.isNullOrBlank() || it.data.progressImage.isNullOrBlank()){
                    binding.btnCreateBa.isEnabled = true
                    binding.btnCreateBa.setOnClickListener {
//                            BotSheetCreateBAFragment().show(supportFragmentManager, "createba")
                        startActivity(Intent(this@PeriodicVendorDetailActivity,FormDiversionActivity::class.java).also{
                            it.putExtra("idJob",idJob)
                            it.putExtra("true",true)
                        })
                        finish()
                    }
                }else{
                    binding.btnCreateBa.isEnabled = false
                    binding.btnCreateBa.setBackgroundResource(R.drawable.bg_rounded_grey_disable)
                }

                //btn create ba
                //when forget to approve
//                if (!it.data.approved && it.data.statusPeriodic == "NOT_REALITATION") {
//                    binding.btnCreateBa.apply {
//                        isEnabled = true
//                        setOnClickListener {
////                            BotSheetCreateBAFragment().show(supportFragmentManager, "createba")
//                            startActivity(Intent(this@PeriodicVendorDetailActivity,FormDiversionActivity::class.java).also{
//                                it.putExtra("idJob",idJob)
//                                it.putExtra("true",true)
//                            })
//                        }
//                    }
//                } else {
//                    binding.btnCreateBa.apply {
//                        setOnClickListener {
//                            startActivity(Intent(this@PeriodicVendorDetailActivity,FormDiversionActivity::class.java).also{
//                                it.putExtra("idJob",idJob)
//                                it.putExtra("true",true)
//                            })
//                        }
//                    }
//                }


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
        //delete
        viewModelMonthlyWork.getStatusApprovalJobViewModel().observe(this) {
            statsApproval = it.code == 200
        }
        // escallation
        viewModelMonthlyWork.getEscalationLowlevelViewModel().observe(this) {
            if (it.code == 200) {
                escalationStats = it.message
            } else {
                escalationStats = it.message
            }
        }
    }

    private fun showDialog(context: Context, text: String) {
        if (oneTimeShow){
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
        }
        oneTimeShow = true

    }

    private fun lockDateInRange(targetDate: String) {
        // Define date format with the correct locale
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
        Log.d("yiha", targetDate)

        try {
            // Convert target date string to a Date object
            val targetDateObj: Date? = dateFormat.parse(targetDate)

            if (targetDateObj == null) {
                // Handle the case where the date is invalid
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show()
                return
            }

            // Get today's date
            val calendar = Calendar.getInstance()

            // Get yesterday's date
            val yesterday = calendar.clone() as Calendar
            yesterday.add(Calendar.DATE, -1)

            // Get tomorrow's date
            val tomorrow = calendar.clone() as Calendar
            tomorrow.add(Calendar.DATE, 1)

            Log.d("agri", "Yesterday: ${yesterday.time}, Tomorrow: ${tomorrow.time}")

            // Check if the target date is between yesterday and tomorrow
            if (targetDateObj.after(yesterday.time) && targetDateObj.before(tomorrow.time)) {
                // Date is within the range
                Log.d("caca", "Date in range")
            } else {
                // Target date is not within the specified range
                if (!oneTimeToast) {
                    Toast.makeText(this, "Tidak bisa dikerjakan di luar tanggal yang diizinkan", Toast.LENGTH_SHORT).show()
                    oneTimeToast = true
                }

                // Disable and hide UI elements
                binding.clUploadBeforeImgPeriodic.apply {
                    isClickable = false
                }
                binding.clUploadProcessImgPeriodic.apply {
                    isClickable = false
                }
                binding.clUploadAfterImgPeriodic.apply {
                    isClickable = false
                }
                binding.btnReplaceBeforePeriodic.visibility = View.GONE
                binding.btnReplaceProgressPeriodic.visibility = View.GONE
                binding.btnReplaceAfterPeriodic.visibility = View.GONE
            }
        } catch (e: ParseException) {
            // Handle parsing error
//            Toast.makeText(this, "Error: Unparseable date", Toast.LENGTH_SHORT).show()
            Log.e("DateParseError", "Unparseable date: $targetDate", e)
        }
    }





    private fun convertDate(inputDateStr: String): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))

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
//            val url = getString(R.string.url) + "/rkb/$img"
            val url = getString(R.string.url) + "assets.admin_master/images/rkb/$img"
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


    private fun showDialogSuccess(
        userNameDialog: String,
        userPositionDialog: String,
        createdDateDialog: String,
        usrImg: String
    ) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_upload_job_rkb)
        val btnBack = dialog.findViewById<AppCompatButton>(R.id.btnCreatedRkb)
        val userName = dialog.findViewById<TextView>(R.id.tvNameCreatedJobByRkb)
        val userPosition = dialog.findViewById<TextView>(R.id.tvPositionCreatedJobByRkb)
        val createdDate = dialog.findViewById<TextView>(R.id.tvDateCreatedJobByRkb)
        val userImages = dialog.findViewById<CircleImageView>(R.id.ivIconDialogRkb)

        userName.text = userNameDialog
        userPosition.text = userPositionDialog
        createdDate.text = createdDateDialog
        setPhotoProfile(usrImg, userImages)


        btnBack?.setOnClickListener {
            loadData()
            dialog.dismiss()
        }


        dialog.show()
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

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
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