package com.hkapps.hygienekleen.features.features_management.report.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailComplaintCftalkBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.CftalksByProjectActivity
import com.hkapps.hygienekleen.features.features_management.report.viewmodel.ReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class DetailComplaintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailComplaintCftalkBinding
    private val viewModel: ReportManagementViewModel by viewModels()

    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var complaintId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_REPORT_CFTALK, 0)
    private var userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private var typeFilter =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TYPE_FILTER,"")
    private var projectName: String = ""
    private var projectCode: String = ""
    private var createdBy: String = ""
    private var loadingDialog: Dialog? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailComplaintCftalkBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,null)
        binding.layoutAppbar.tvAppbarTitle.text = "Detail CFTalk"
        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        var flag = 1
        binding.btnCloseComplaintByUser.setOnClickListener {
            if (flag == 1) {
                binding.btnCloseComplaintByUser.isEnabled = false
                showLoading(getString(R.string.loading_string2))
            }
            flag = 0
        }

        loadData()
        setObserver()

    }

    private fun loadData() {
        viewModel.getReportDetailCftalk(complaintId)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getReportDetailCftalkViewModel().observe(this) {
            if (it.code == 200) {
                // set image
                val image = it.data.image
                val url = getString(R.string.url) + "assets.admin_master/images/complaint/"
                applicationContext.let { it1 ->
                    Glide.with(it1)
                        .load(url + image)
                        .apply(RequestOptions.fitCenterTransform())
                        .into(binding.ivMainPhotoComplaint)
                }

                //image created complaint
                getPhotoProfile(binding.ivWorkerImage, it.data.processByEmployeePhotoProfile ?: "")
                setImages(binding.ivFirstImageProgress, it.data.beforeImage ?: "")
                setImages(binding.ivSecondImageProgress, (it.data.processImage ?: "").toString())
                setImages(binding.ivThirdImageProgress, it.data.afterImage ?: "")

                binding.tvTitleReport.text = it?.data?.title ?: "-"
                binding.tvComplaintBy.text = it?.data?.createdByEmployeeName ?: "-"
                binding.tvLocationComplaint.text = it?.data?.projectName ?: "-"
                binding.tvDescriptionCftalk.text = it?.data?.description ?: "-"
                binding.tvEscallationComplaint.text = (it?.data?.notificationLevel ?: "-").toString()
                binding.tvNameWorker.text = (it?.data?.processByEmployeeName ?: "-").toString()

                binding.tvDateStatusWaiting.text = it?.data?.waitingAt ?: "0"
                binding.tvDateStatusOnprogress.text = it?.data?.processAt ?: "0"
                binding.tvDateStatusDone.text = it?.data?.doneAt ?: "0"
                binding.tvDateStatusClose.text = it?.data?.closedAt ?: "0"
                binding.tvDescriptionWorker.text = (it?.data?.comments ?: "-").toString()
                binding.tvDescriptionFinishWorker.text = it.data.reportComments ?: "-"

                projectName = it.data.projectName
                projectCode = it.data.projectId
                createdBy = it.data.createdByEmployeeName

                when (it.data.statusComplaint) {
                    "WAITING" -> {
                        // set progress bar color
                        binding.ivStatusWaiting.setImageResource(R.drawable.ic_progress_red)
                        binding.ivStatusOnprogress.setImageResource(R.drawable.ic_progress_default)
                        binding.ivStatusDone.setImageResource(R.drawable.ic_progress_default)
                        binding.ivStatusClose.setImageResource(R.drawable.ic_progress_default)

                        // set status text color
                        binding.tvProgressWaiting.setTextColor(resources.getColor(R.color.red1))
                        binding.tvProgressOnProgress.setTextColor(resources.getColor(R.color.grey3))
                        binding.tvProgressDone.setTextColor(resources.getColor(R.color.grey3))
                        binding.tvProgressClose.setTextColor(resources.getColor(R.color.grey3))

                        //history
                        binding.clStatusWaiting.visibility = View.VISIBLE
                        binding.ivDotChecked.visibility = View.VISIBLE

                        //progress
                        emptyProgress("waiting")
                    }
                    "ON PROGRESS" -> {
                        // set progress bar color
                        binding.ivStatusWaiting.setImageResource(R.drawable.ic_progress_disable)
                        binding.ivStatusOnprogress.setImageResource(R.drawable.ic_progress_primary)
                        binding.ivStatusDone.setImageResource(R.drawable.ic_progress_default)
                        binding.ivStatusClose.setImageResource(R.drawable.ic_progress_default)

                        // set status text color
                        binding.tvProgressWaiting.setTextColor(resources.getColor(R.color.grayTxt))
                        binding.tvProgressOnProgress.setTextColor(resources.getColor(R.color.primary_color))
                        binding.tvProgressDone.setTextColor(resources.getColor(R.color.grey3))
                        binding.tvProgressClose.setTextColor(resources.getColor(R.color.grey3))

                        //history
                        binding.clStatusWaiting.visibility = View.VISIBLE
                        binding.clStatusOnprogress.visibility = View.GONE
                        binding.ivDotCheckedOnprogress.visibility = View.GONE
                        binding.tvDateTimeProgress.text = "diproses pada ${it.data.processAt}"

                        //progress
                        emptyProgress("progress")

                    }
                    "DONE" -> {
                        // set progress bar color
                        binding.ivStatusWaiting.setImageResource(R.drawable.ic_progress_disable)
                        binding.ivStatusOnprogress.setImageResource(R.drawable.ic_progress_disable)
                        binding.ivStatusDone.setImageResource(R.drawable.ic_progress_green)
                        binding.ivStatusClose.setImageResource(R.drawable.ic_progress_default)

                        // set status text color
                        binding.tvProgressWaiting.setTextColor(resources.getColor(R.color.grayTxt))
                        binding.tvProgressOnProgress.setTextColor(resources.getColor(R.color.grayTxt))
                        binding.tvProgressDone.setTextColor(resources.getColor(R.color.green2))
                        binding.tvProgressClose.setTextColor(resources.getColor(R.color.grey3))

                        //history
                        binding.clStatusWaiting.visibility = View.VISIBLE
                        binding.clStatusOnprogress.visibility = View.GONE
                        binding.clStatusDone.visibility = View.GONE
                        binding.ivDotCheckedDone.visibility = View.GONE

                        binding.tvDescriptionFinishWorker.text = it.data.reportComments
                        binding.tvDateTimeProgress.text = "diproses pada ${it.data.processAt}"
                        binding.tvDateTimeDone.text = "diselesaikan pada ${it.data.doneAt}"

                        //button click by user
                        if (it.data.createdByEmployeeId == userId){
                            binding.btnCloseComplaintByUser.visibility = View.VISIBLE
                        } else {
                            binding.btnCloseComplaintByUser.visibility = View.GONE
                        }
                    }
                    "CLOSE" -> {
                        // set progress bar color
                        binding.ivStatusWaiting.setImageResource(R.drawable.ic_progress_disable)
                        binding.ivStatusOnprogress.setImageResource(R.drawable.ic_progress_disable)
                        binding.ivStatusDone.setImageResource(R.drawable.ic_progress_disable)
                        binding.ivStatusClose.setImageResource(R.drawable.ic_progress_secondary)

                        // set status text color
                        binding.tvProgressWaiting.setTextColor(resources.getColor(R.color.grayTxt))
                        binding.tvProgressOnProgress.setTextColor(resources.getColor(R.color.grayTxt))
                        binding.tvProgressDone.setTextColor(resources.getColor(R.color.grayTxt))
                        binding.tvProgressClose.setTextColor(resources.getColor(R.color.secondary_color))

                        //history
                        binding.clStatusWaiting.visibility = View.VISIBLE
                        binding.clStatusOnprogress.visibility = View.GONE
                        binding.clStatusClose.visibility = View.VISIBLE
                        binding.ivDotCheckedClose.visibility = View.VISIBLE

                        binding.tvDescriptionFinishWorker.text = it.data.reportComments
                        binding.tvDateTimeProgress.text = "diproses pada ${it.data.processAt}"
                        binding.tvDateTimeDone.text = "diselesaikan pada ${it.data.doneAt}"
                    }
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.putCloseComplaintCftalkViewModel().observe(this){
            if (it.code == 200){
                hideLoading()
                Toast.makeText(this, "Berhasil menutup komplain", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, CftalksByProjectActivity::class.java))
            } else {
                hideLoading()
                binding.btnCloseComplaintByUser.isEnabled = true
                Toast.makeText(this, "Gagal menutup komplain", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //fun
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

    private fun setImages(imageView: ImageView, image: String) {
        val requestOptions = RequestOptions()
            .centerCrop()
            .error(R.drawable.ic_error_image)

        Glide.with(this)
            .load(getString(R.string.url) + "assets.admin_master/images/complaint/$image")
            .apply(requestOptions)
            .into(imageView)
    }

    private fun emptyProgress(string: String){
        when(string) {
            "waiting" -> {
                binding.tvResponseWorkerComplaint.visibility = View.GONE
                binding.rlFeedbackWorker.visibility = View.GONE
                binding.tvDateTimeProgress.visibility = View.GONE
            }
            "progress" -> {
                binding.tvResponseWorkerComplaint.visibility = View.VISIBLE
                binding.rlFeedbackWorker.visibility = View.VISIBLE
                binding.tvDateTimeProgress.visibility = View.VISIBLE
            }
        }
        binding.llFirstProgress.visibility = View.GONE
        binding.llFotoPengerjaan.visibility = View.GONE
        binding.tvFotoConditionWorkerComplaint.visibility = View.GONE
        binding.tvReportWorkerComplaint.visibility = View.GONE
        binding.rlFeedbackFinishWorker.visibility = View.GONE
        binding.tvDateTimeDone.visibility = View.GONE
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        viewModel.closeComplaintCftalk(complaintId)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

}