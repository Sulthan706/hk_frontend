package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailComplaintInternalBinding
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.chemicalsComplaintInternal.Data
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.detailcomplaintinternal.VisitorObject
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.adapter.ChemicalsComplaintAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.adapter.SelectedChemicalsAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.adapter.VisitorObjectComplaintAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.viewmodel.VendorComplaintInternalViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DetailComplaintInternalActivity : AppCompatActivity(),
    ChemicalsComplaintAdapter.ListChemicalsCallBack {

    private lateinit var binding: ActivityDetailComplaintInternalBinding
    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
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
    private var onUploadClick: String = ""
    private var loadingDialog: Dialog? = null
    private var createdBy: Int? = null
    var t: String = ""
    private var flag = 1
    private var idChemicals = ArrayList<Int>()
    private var nameChemicals = ArrayList<String>()
    private var visitorImage: String = ""
    private var visitorImageTwo: String = ""
    private var reportComments = ""
    private val totalWorker =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TOTAL_WORKER_DETAIL_CFTALK, "")
    private val clickFrom =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.DETAIL_COMPLAINT_CLICK_FROM, "")
    private var idWorkTypeCftalk = 0
    private var idWorkTypeCtalk = 0
    private var typeWork = ""
    lateinit var adapters: VisitorObjectComplaintAdapter


    companion object {
        //QR cam code Req
        private const val CAMERA_REQ = 101
    }

    private var latestTmpUri: Uri? = null
    private val previewImage by lazy {
        findViewById<ImageView>(R.id.iv_result_upload_complaintInternal)
    }

    private val viewModel: VendorComplaintInternalViewModel by lazy {
        ViewModelProviders.of(this).get(VendorComplaintInternalViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailComplaintInternalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        //set appbar layout
        binding.appbarDetailComplaintInternal.tvAppbarTitle.text = "Detail Komplain"
        binding.appbarDetailComplaintInternal.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // set rv list chemicals
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvBahanChemicalCfTalk.layoutManager = layoutManager

        //selayoutManagert rv objek visitor
        val layoutManagerVisitor = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvKomplainVisitorObjek.layoutManager = layoutManagerVisitor

        loadData()
        setObserver()
        setObserverUploaded()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getListComplaintInternalResponse().observe(this) { it ->
            if (it.code == 200) {
                val complaintVisitorValidation = it.data.complaintType == "COMPLAINT_VISITOR"
                // set image
                val image = it.data.image
                val url = getString(R.string.url) + "assets.admin_master/images/complaint/"
                applicationContext.let {
                    Glide.with(it)
                        .load(url + image)
                        .apply(RequestOptions.centerCropTransform())
                        .into(binding.ivComplaintInternal)
                }

                // set detail data
                binding.tvTitleDetailComplaintInternal.text = it.data.title
                binding.tvLocationDetailComplaintInternal.text =
                    "${it.data.locationName} - ${it.data.subLocationName}"
                binding.tvEscalationInDetailComplaintInternal.text = if (it.data.escalation == null
                    || it.data.escalation == "null" || it.data.escalation == ""
                ) {
                    "-"
                } else {
                    it.data.escalation
                }
//                binding.tvEscalationInDetailComplaintInternal.visibility = View.GONE

                // info client (pembuat komplain)
                if (clickFrom == "cTalk") {
                    binding.tvClientNameComplaintInternal.text = it.data.clientName
                    getPhotoProfile(
                        binding.ivClientComplaintInternal,
                        it.data.clientPhotoProfile
                    )
                } else {
                    binding.tvClientNameComplaintInternal.text = it.data.createdByEmployeeName
                    getPhotoProfile(
                        binding.ivClientComplaintInternal,
                        it.data.createdByEmployeePhotoProfile
                    )
                }

                // dibuat & diselesaikan pada
                binding.tvDateTimeDetailComplaintInternal.text = it.data.waitingAt
                if (it.data.doneAt != null) {
                    binding.tvDateTimeDoneDetailComplaintInternal.text = it.data.doneAt
                } else {
                    binding.tvDateTimeDoneDetailComplaintInternal.text = "-"
                }

                //complaint visitor
                if (complaintVisitorValidation) {
                    binding.tvTitleObjekComplaintVisitor.visibility = View.VISIBLE
                    binding.tvObjekItemComplaintVisitor.visibility = View.VISIBLE

                    binding.tvClientNameComplaintInternal.text = "VISITOR"
                    binding.ivComplaintVisitor.visibility = View.VISIBLE

                    binding.tvObjekItemComplaintVisitor.text = if (it.data.visitorOption == "" ||
                        it.data.visitorOption == "null" || it.data.visitorOption == null
                    ) {
                        "-"
                    } else {
                        it.data.visitorOption
                    }
                    //layout validasi foto perobjek
                    binding.llFotoPengerjaanImage.visibility = View.GONE
                    binding.llFotoPengerjaan.visibility = View.GONE

                    binding.rvKomplainVisitorObjek.visibility = View.VISIBLE

                    //set rv
                    adapters = VisitorObjectComplaintAdapter(this, it.data.visitorObject as ArrayList<VisitorObject>)
                    binding.rvKomplainVisitorObjek.adapter = adapters
                    // save complaint id
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_COMPLAINT, it.data.complaintId)



                    binding.btnDoneSubmitComplaintVisitor.setOnClickListener {
                        bottomDialogWorkReportVisitor()
                    }

                    binding.rvBahanChemicalCfTalk.visibility = View.GONE
                    if(it.data.statusComplaint == "CLOSE"){
                        binding.llButtonProcessComplaintVisitor.visibility = View.GONE
                    }




                }

                // isi komplain
                binding.tvNoteComplaintInternal.text = if (it.data.description == "" ||
                    it.data.description == "null" || it.data.description == null
                ) {
                    "-"
                } else {
                    it.data.description
                }


                // validasi foto komplain lebih dari 1
                val imageList = ArrayList<SlideModel>() // create image list
                imageList.add(
                    SlideModel(
                        getString(R.string.url) + "assets.admin_master/images/complaint/" + it.data.image,
                        "CF Talk 1."
                    )
                )

                if (it.data.imageTwo == null || it.data.imageTwo == "") {
                    //donoting
                } else {
                    imageList.add(
                        SlideModel(
                            getString(R.string.url) + "assets.admin_master/images/complaint/" + it.data.imageTwo,
                            "CF Talk 2."
                        )
                    )
                }

                if (it.data.imageThree == null || it.data.imageThree == "") {
                    //donothing
                } else {
                    imageList.add(
                        SlideModel(
                            getString(R.string.url) + "assets.admin_master/images/complaint/" + it.data.imageThree,
                            "CF Talk 3."
                        )
                    )
                }

                if (it.data.imageFourth == null || it.data.imageFourth == "") {
                    //donothing
                } else {
                    imageList.add(
                        SlideModel(
                            getString(R.string.url) + "assets.admin_master/images/complaint/" + it.data.imageFourth,
                            "CF Talk 4."
                        )
                    )
                }
                binding.ivComplaintInternal.setOnClickListener {
                    //pop up modal
                    val dialog = this.let { Dialog(it) }
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_custom_image_slide)
                    val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
                    val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView

                    ivZoom.setImageDrawable(binding.ivComplaintInternal.drawable)
                    val imageSlider = dialog.findViewById(R.id.image_slider) as ImageSlider

                    imageSlider.setImageList(imageList)
                    imageSlider.stopSliding()

                    close.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }

                // set data validation
                typeWork = if (it.data.typeJobs == "" || it.data.typeJobs == null) {
                    ""
                } else {
                    it.data.typeJobs
                }
                createdBy = it.data.createdByEmployeeId
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

                when (it.data.statusComplaint) {
                    "WAITING" -> {
                        if (complaintVisitorValidation){

                            binding.llButtonProcessComplaintInternal.visibility = View.VISIBLE
                            binding.llButtonProcessComplaintVisitor.visibility = View.GONE
                            binding.tvWorkTypeComplaintInternal.visibility = View.VISIBLE

                            binding.tvWorkTypeComplaintVisitor.visibility = View.GONE
                            binding.spinnerWorkTypeComplaintInternal.visibility = View.VISIBLE

                        }

                        binding.llProcessComplaintInternal.visibility = View.GONE
                        binding.llDoneComplaintInternal.visibility = View.GONE
                        binding.tvDateReplyFromLeaderComplaintClient.visibility = View.INVISIBLE
                        binding.tvReplyFromLeaderComplaintClient.visibility = View.INVISIBLE
                        binding.etReplyFromLeaderComplaintInternal.visibility = View.VISIBLE

                        // jenis pekerjaan
                        if (typeWork == "") {
                            binding.tvWorkTypeComplaintInternal.visibility = View.GONE
                            if (complaintVisitorValidation){
                                binding.spinnerWorkTypeComplaintInternal.visibility = View.GONE
                                binding.tvWorkTypeComplaintInternal.visibility = View.GONE
                            } else {
                                binding.spinnerWorkTypeComplaintInternal.visibility = View.VISIBLE
                            }
                        } else {
                            idWorkTypeCftalk = it.data.idTypesJobs
                            CarefastOperationPref.saveInt(
                                CarefastOperationPrefConst.JOB_TYPE_COMPLAINT,
                                it.data.idTypesJobs
                            )
                            binding.tvWorkTypeComplaintInternal.text = it.data.typeJobs
                            binding.tvWorkTypeComplaintInternal.visibility = View.VISIBLE
                            if (!complaintVisitorValidation){
                                binding.tvWorkTypeComplaintInternal.setBackgroundResource(R.drawable.bg_field_red1)
                            }
                            binding.spinnerWorkTypeComplaintInternal.visibility = View.GONE
                        }

                        onWaitingComplaint()
                    }

                    "ON PROGRESS" -> {
                        if (complaintVisitorValidation){
                            for (i in 0 until it.data.visitorObject.size){
                                visitorImage = it.data.visitorObject[i].file
                            }

                            binding.llButtonProcessComplaintInternal.visibility = View.GONE
                            binding.llButtonProcessComplaintVisitor.visibility = View.VISIBLE
                            binding.tvWorkTypeComplaintInternal.visibility = View.GONE

                            binding.tvWorkTypeComplaintVisitor.visibility = View.GONE
                            binding.spinnerWorkTypeComplaintInternal.visibility = View.GONE


                            if (visitorImage.isNullOrEmpty()){
                                binding.rlButtonNotSubmitComplaintVisitor.visibility = View.VISIBLE
                                binding.rlbuttonSubmitComplaintVisitor.visibility = View.GONE
                            } else {
                                binding.rlButtonNotSubmitComplaintVisitor.visibility = View.GONE
                                binding.rlbuttonSubmitComplaintVisitor.visibility = View.VISIBLE
                            }
                        }

                        binding.llProcessComplaintInternal.visibility = View.VISIBLE
                        binding.llDoneComplaintInternal.visibility = View.GONE
                        binding.tvDateReplyFromLeaderComplaintClient.visibility = View.VISIBLE
                        binding.tvReplyFromLeaderComplaintClient.visibility = View.VISIBLE
                        binding.etReplyFromLeaderComplaintInternal.visibility = View.INVISIBLE

                        // jenis pekerjaan
                        if (complaintVisitorValidation){
                            binding.tvWorkTypeComplaintInternal.visibility = View.GONE
                        } else {
                            binding.tvWorkTypeComplaintInternal.apply {
                                setBackgroundResource(R.drawable.bg_field_red1)
                                visibility = View.VISIBLE
                            }
                        }

                        binding.spinnerWorkTypeComplaintInternal.visibility = View.GONE
                        binding.tvWorkTypeComplaintInternal.text = it.data.typeJobs

                        // set comments data
                        binding.tvDateReplyFromLeaderComplaintClient.text =
                            "diproses pada ${it.data.processAt}"
                        binding.tvReplyFromLeaderComplaintClient.text = it.data.comments

                        // set processBy data
                        binding.tvOpsNameComplaintInternal.text = it.data.processByEmployeeName
                        getPhotoProfile(
                            binding.ivOpsComplaintInternal,
                            it.data.processByEmployeePhotoProfile
                        )

                        onProgressComplaint(it.data.typeJobs)
                    }

                    "DONE" -> {
                        binding.llProcessComplaintInternal.visibility = View.VISIBLE
                        binding.llDoneComplaintInternal.visibility = View.VISIBLE
                        binding.tvDateReplyFromLeaderComplaintClient.visibility = View.VISIBLE
                        binding.tvReplyFromLeaderComplaintClient.visibility = View.VISIBLE
                        binding.etReplyFromLeaderComplaintInternal.visibility = View.INVISIBLE

                        // jenis pekerjaan
                        binding.tvWorkTypeComplaintInternal.visibility = View.VISIBLE
                        if (!complaintVisitorValidation){
                            binding.tvWorkTypeComplaintInternal.setBackgroundResource(R.drawable.bg_field_red1)
                        }

                        binding.spinnerWorkTypeComplaintInternal.visibility = View.GONE
                        binding.tvWorkTypeComplaintInternal.text = it.data.typeJobs

                        // set comments data
                        binding.tvDateReplyFromLeaderComplaintClient.text =
                            "diproses pada ${it.data.processAt}"
                        binding.tvReplyFromLeaderComplaintClient.text = it.data.comments

                        // set processBy data
                        binding.tvOpsNameComplaintInternal.text = it.data.processByEmployeeName
                        getPhotoProfile(
                            binding.ivOpsComplaintInternal,
                            it.data.processByEmployeePhotoProfile
                        )

                        // set data jumlah pekerja
                        binding.rlInputJumlahOrangPekerja.visibility = View.GONE
                        binding.rlJumlahOrangPekerja.visibility = View.VISIBLE
                        binding.ivEditJumlahOrangPekerja.layoutParams.height = 0
                        binding.ivEditJumlahOrangPekerja.layoutParams.width = 0
                        binding.tvJumlahPekerja.text = it.data.totalWorkers.toString()

                        // set data chemicals
                        binding.ivEditBahanChemicalCfTalk.visibility = View.INVISIBLE
                        binding.tvAddBahanChemicalCftalk.visibility = View.GONE
                        binding.rvBahanChemicalCfTalk.visibility = View.VISIBLE

                        if (it.data.chemicalsName.isEmpty()) {
                            nameChemicals.add("Tidak menggunakan chemical")
                        } else {
                            for (i in 0 until it.data.chemicalsName.size) {
                                nameChemicals.add(it.data.chemicalsName[i].chemicalsName)
                            }
                        }
                        binding.rvBahanChemicalCfTalk.adapter =
                            SelectedChemicalsAdapter(nameChemicals)

                        // set laporan pengerjaan
                        binding.tvLaporanFromLeaderComplaintClient.text = it.data.reportComments

                        // layout status penyelesaian
                        binding.tvTimeStatusDoneComplaintInternal.text = it.data.doneAt
                        binding.tvTimeStatusDoneComplaintInternal.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_status_aktive,
                            0,
                            0,
                            0
                        )
                        binding.tvTimeStatusCloseComplaintInternal.visibility = View.GONE
                        binding.tvStatusCloseComplaintInternal.visibility = View.GONE

                        onDoneComplaint("done")
                    }

                    "CLOSE" -> {
                        binding.llProcessComplaintInternal.visibility = View.VISIBLE
                        binding.llDoneComplaintInternal.visibility = View.VISIBLE
                        binding.tvDateReplyFromLeaderComplaintClient.visibility = View.VISIBLE
                        binding.tvReplyFromLeaderComplaintClient.visibility = View.VISIBLE
                        binding.etReplyFromLeaderComplaintInternal.visibility = View.INVISIBLE

                        // jenis pekerjaan
                        binding.tvWorkTypeComplaintInternal.visibility = View.VISIBLE
                        if (!complaintVisitorValidation){
                            binding.tvWorkTypeComplaintInternal.setBackgroundResource(R.drawable.bg_field_red1)
                        }

                        binding.spinnerWorkTypeComplaintInternal.visibility = View.GONE
                        binding.tvWorkTypeComplaintInternal.text = it.data.typeJobs

                        // set comments data
                        binding.tvDateReplyFromLeaderComplaintClient.text =
                            "diproses pada ${it.data.processAt}"
                        binding.tvReplyFromLeaderComplaintClient.text = it.data.comments

                        // set processBy data
                        binding.tvOpsNameComplaintInternal.text = it.data.processByEmployeeName
                        getPhotoProfile(
                            binding.ivOpsComplaintInternal,
                            it.data.processByEmployeePhotoProfile
                        )

                        // set data jumlah pekerja
                        binding.rlInputJumlahOrangPekerja.visibility = View.GONE
                        binding.rlJumlahOrangPekerja.visibility = View.VISIBLE
                        binding.ivEditJumlahOrangPekerja.layoutParams.height = 0
                        binding.ivEditJumlahOrangPekerja.layoutParams.width = 0
                        binding.tvJumlahPekerja.text = it.data.totalWorkers.toString()

                        // set data chemicals
                        binding.ivEditBahanChemicalCfTalk.visibility = View.INVISIBLE
                        binding.tvAddBahanChemicalCftalk.visibility = View.GONE
                        if (complaintVisitorValidation){
                            binding.rvBahanChemicalCfTalk.visibility = View.GONE
                        } else {
                            binding.rvBahanChemicalCfTalk.visibility = View.VISIBLE
                        }

                        if (it.data.chemicalsName.isEmpty()) {
                            nameChemicals.add("Tidak menggunakan chemical")
                        } else {
                            for (i in 0 until it.data.chemicalsName.size) {
                                nameChemicals.add(it.data.chemicalsName[i].chemicalsName)
                            }
                        }
                        binding.rvBahanChemicalCfTalk.adapter =
                            SelectedChemicalsAdapter(nameChemicals)

                        // set laporan pengerjaan
                        binding.tvLaporanFromLeaderComplaintClient.text = it.data.reportComments

                        // layout status
                        binding.tvTimeStatusDoneComplaintInternal.text = it.data.doneAt
                        binding.tvTimeStatusCloseComplaintInternal.text = it.data.closedAt

                        onDoneComplaint("close")
                    }

                    else -> {
                        Log.d("ComplaintNotifiAct", "setObserver: status complaint not defined")
                        Log.e("ComplaintNotifiAct", "setObserver: status complaint not defined")
                    }
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObserverUploaded() {
        viewModel.processComplaintInternalModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.JOB_TYPE_COMPLAINT, 0)
                val i = Intent(this, DetailComplaintInternalActivity::class.java)
                startActivity(i)
                finish()
                Toast.makeText(this, "Berhasil memproses komplain", Toast.LENGTH_SHORT).show()
            } else {
                hideLoading()
                flag = 1
                Toast.makeText(this, "Gagal memproses komplain", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.beforeImageComplaintInternalModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                val i = Intent(this, DetailComplaintInternalActivity::class.java)
                startActivity(i)
                finish()
            } else {
                hideLoading()
                flag = 1
                Toast.makeText(
                    this, "Gagal uploa" +
                            "d before image", Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.progressImageComplaintInternalModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                val i = Intent(this, DetailComplaintInternalActivity::class.java)
                startActivity(i)
                finish()
            } else {
                hideLoading()
                flag = 1
                Toast.makeText(this, "Gagal upload progress image", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.afterImageComplaintInternalModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                val i = Intent(this, DetailComplaintInternalActivity::class.java)
                startActivity(i)
                finish()
            } else {
                hideLoading()
                flag = 1
                Toast.makeText(this, "Gagal upload after image", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.closeComplaintInternalResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                val i = Intent(this, DetailComplaintInternalActivity::class.java)
                startActivity(i)
                finish()
                Toast.makeText(this, "Berhasil menutup komplain", Toast.LENGTH_SHORT).show()
            } else {
                hideLoading()
                flag = 1
                Toast.makeText(this, "Gagal menutup komplain", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onDoneComplaint(string: String) {
        // set progress bar color
        binding.ivStatusWaitingDetailCftalk.setImageResource(R.drawable.ic_progress_disable)
        binding.ivStatusOnprogressDetailCftalk.setImageResource(R.drawable.ic_progress_disable)
        binding.ivStatusDoneDetailCftalk.setImageResource(R.drawable.ic_progress_green)
        binding.ivStatusCloseDetailCftalk.setImageResource(R.drawable.ic_progress_default)

        // set status text color
        binding.tvProgressWaitingDetailCftalk.setTextColor(resources.getColor(R.color.grayTxt))
        binding.tvProgressOnProgressDetailCftalk.setTextColor(resources.getColor(R.color.grayTxt))
        binding.tvProgressDoneDetailCftalk.setTextColor(resources.getColor(R.color.green2))
        binding.tvProgressCloseDetailCftalk.setTextColor(resources.getColor(R.color.grey3))

        // set images
        binding.ivBeforeDefault.visibility = View.GONE
        binding.ivProcessDefault.visibility = View.GONE
        binding.ivAfterDefault.visibility = View.GONE

        binding.ivBeforeComplaintInternal.visibility = View.VISIBLE
        binding.ivProcessComplaintInternal.visibility = View.VISIBLE
        binding.ivAfterComplaintInternal.visibility = View.VISIBLE

        setImages(beforeImage, binding.ivBeforeComplaintInternal)
        setImages(processImage, binding.ivProcessComplaintInternal)
        setImages(afterImage, binding.ivAfterComplaintInternal)

        binding.rlUploadBeforeImageComplaintInternal.setOnClickListener {
            openImage(binding.ivBeforeComplaintInternal.drawable)
        }
        binding.rlUploadProcessImageComplaintInternal.setOnClickListener {
            openImage(binding.ivProcessComplaintInternal.drawable)
        }
        binding.rlUploadAfterImageComplaintInternal.setOnClickListener {
            openImage(binding.ivAfterComplaintInternal.drawable)
        }

        setImages(beforeImage, binding.ivBeforeComplaintInternal)
        setImages(processImage, binding.ivProcessComplaintInternal)
        setImages(afterImage, binding.ivAfterComplaintInternal)

        // set gone button
        binding.llButtonProcessComplaintInternal.visibility = View.GONE
        binding.llButtonSubmitComplaintInternal.visibility = View.GONE
        when (string) {
            "done" -> {
                // set progress bar color
                binding.ivStatusWaitingDetailCftalk.setImageResource(R.drawable.ic_progress_disable)
                binding.ivStatusOnprogressDetailCftalk.setImageResource(R.drawable.ic_progress_disable)
                binding.ivStatusDoneDetailCftalk.setImageResource(R.drawable.ic_progress_green)
                binding.ivStatusCloseDetailCftalk.setImageResource(R.drawable.ic_progress_default)

                // set status text color
                binding.tvProgressWaitingDetailCftalk.setTextColor(resources.getColor(R.color.grayTxt))
                binding.tvProgressOnProgressDetailCftalk.setTextColor(resources.getColor(R.color.grayTxt))
                binding.tvProgressDoneDetailCftalk.setTextColor(resources.getColor(R.color.green2))
                binding.tvProgressCloseDetailCftalk.setTextColor(resources.getColor(R.color.grey3))

                if (createdBy == userId) {
                    binding.llButtonCloseComplaintInternal.visibility = View.VISIBLE
                    binding.rlButtonCloseComplaintInternal.setBackgroundResource(R.drawable.bg_primary)

                    // on click button close
                    binding.btnCloseComplaintInternal.setOnClickListener {
                        if (flag == 1) {
                            binding.btnCloseComplaintInternal.isEnabled = false
                            showLoading(getString(R.string.loading_string2), "btnClose")
                        }
                        flag = 0
                    }
                } else {
                    binding.llButtonCloseComplaintInternal.visibility = View.GONE
                }
            }

            "close" -> {
                // set progress bar color
                binding.ivStatusWaitingDetailCftalk.setImageResource(R.drawable.ic_progress_disable)
                binding.ivStatusOnprogressDetailCftalk.setImageResource(R.drawable.ic_progress_disable)
                binding.ivStatusDoneDetailCftalk.setImageResource(R.drawable.ic_progress_disable)
                binding.ivStatusCloseDetailCftalk.setImageResource(R.drawable.ic_progress_secondary)

                // set status text color
                binding.tvProgressWaitingDetailCftalk.setTextColor(resources.getColor(R.color.grayTxt))
                binding.tvProgressOnProgressDetailCftalk.setTextColor(resources.getColor(R.color.grayTxt))
                binding.tvProgressDoneDetailCftalk.setTextColor(resources.getColor(R.color.grayTxt))
                binding.tvProgressCloseDetailCftalk.setTextColor(resources.getColor(R.color.secondary_color))

                binding.llButtonCloseComplaintInternal.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onProgressComplaint(typeJob: String) {
        // set progress bar color
        binding.ivStatusWaitingDetailCftalk.setImageResource(R.drawable.ic_progress_disable)
        binding.ivStatusOnprogressDetailCftalk.setImageResource(R.drawable.ic_progress_primary)
        binding.ivStatusDoneDetailCftalk.setImageResource(R.drawable.ic_progress_default)
        binding.ivStatusCloseDetailCftalk.setImageResource(R.drawable.ic_progress_default)

        // set status text color
        binding.tvProgressWaitingDetailCftalk.setTextColor(resources.getColor(R.color.grayTxt))
        binding.tvProgressOnProgressDetailCftalk.setTextColor(resources.getColor(R.color.primary_color))
        binding.tvProgressDoneDetailCftalk.setTextColor(resources.getColor(R.color.grey3))
        binding.tvProgressCloseDetailCftalk.setTextColor(resources.getColor(R.color.grey3))

        // button
        binding.llButtonProcessComplaintInternal.visibility = View.GONE
        binding.llButtonSubmitComplaintInternal.visibility = View.VISIBLE
        binding.llButtonCloseComplaintInternal.visibility = View.GONE

        // show disable button submit
        binding.rlButtonSubmitComplaintInternal.setBackgroundResource(R.drawable.bg_disable)
        Log.d("pap", "$typeJob")

        // total worker
        if (totalWorker == "") {
            binding.rlJumlahOrangPekerja.visibility = View.GONE
            if (typeJob.isNullOrEmpty()) {
                binding.rlButtonSubmitComplaintInternal.visibility = View.VISIBLE
            } else {
                binding.rlButtonSubmitComplaintInternal.visibility = View.GONE
            }
            binding.rlInputJumlahOrangPekerja.visibility = View.VISIBLE

            binding.etJumlahPekerja.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0!!.isNotEmpty()) {
                        binding.rlButtonSubmitComplaintInternal.visibility = View.VISIBLE
                    } else {
                        binding.rlButtonSubmitComplaintInternal.visibility = View.GONE
                    }

                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            binding.etJumlahPekerja.addTextChangedListener {
                Log.d(
                    "DetailComplaintTag",
                    "onProgressComplaint: total worker = ${binding.etJumlahPekerja.text.toString()}"
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.TOTAL_WORKER_DETAIL_CFTALK,
                    binding.etJumlahPekerja.text.toString()
                )
            }
        } else {
            binding.rlJumlahOrangPekerja.visibility = View.VISIBLE
            binding.rlInputJumlahOrangPekerja.visibility = View.GONE

            binding.tvJumlahPekerja.text = totalWorker
            binding.ivEditJumlahOrangPekerja.setOnClickListener {
                binding.rlJumlahOrangPekerja.visibility = View.GONE
                binding.rlInputJumlahOrangPekerja.visibility = View.VISIBLE

                binding.etJumlahPekerja.addTextChangedListener {
                    Log.d(
                        "DetailComplaintTag",
                        "onProgressComplaint: total worker = ${binding.etJumlahPekerja.text.toString()}"
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.TOTAL_WORKER_DETAIL_CFTALK,
                        binding.etJumlahPekerja.text.toString()
                    )
                }
            }
        }


        // set layout choose chemicals
        binding.ivEditBahanChemicalCfTalk.visibility = View.INVISIBLE
        binding.rvBahanChemicalCfTalk.visibility = View.GONE

        if (beforeImage == null) {
            // set images
            binding.ivBeforeDefault.visibility = View.VISIBLE
            binding.ivProcessDefault.visibility = View.VISIBLE
            binding.ivAfterDefault.visibility = View.VISIBLE

            binding.ivBeforeComplaintInternal.visibility = View.GONE
            binding.ivProcessComplaintInternal.visibility = View.GONE
            binding.ivAfterComplaintInternal.visibility = View.GONE

            // set on click upload image
            binding.rlUploadProcessImageComplaintInternal.setOnClickListener {
                Toast.makeText(this, "Upload foto before dahulu", Toast.LENGTH_SHORT).show()
            }
            binding.rlUploadAfterImageComplaintInternal.setOnClickListener {
                Toast.makeText(this, "Upload foto before dahulu", Toast.LENGTH_SHORT).show()
            }
            binding.rlUploadBeforeImageComplaintInternal.setOnClickListener {
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
        } else {
            if (beforeImage != null) {
                // set before image
                binding.ivBeforeDefault.visibility = View.GONE
                binding.ivBeforeComplaintInternal.visibility = View.VISIBLE
                setImages(beforeImage, binding.ivBeforeComplaintInternal)
                if (processImage != null) {
                    // set process image
                    binding.ivProcessDefault.visibility = View.GONE
                    binding.ivProcessComplaintInternal.visibility = View.VISIBLE
                    setImages(processImage, binding.ivProcessComplaintInternal)
                    if (afterImage != null) {
                        // set after image
                        binding.ivAfterDefault.visibility = View.GONE
                        binding.ivAfterComplaintInternal.visibility = View.VISIBLE
                        setImages(afterImage, binding.ivAfterComplaintInternal)

                        // set on click add chemicals
                        binding.tvAddBahanChemicalCftalk.setOnClickListener {
                            bottomDialogChemicals()
                        }
                        binding.ivEditBahanChemicalCfTalk.setOnClickListener {
                            bottomDialogChemicals()
                        }

                        // on click button submit
                        binding.rlButtonSubmitComplaintInternal.setBackgroundResource(R.drawable.bg_primary)
                        binding.btnSubmitComplaintInternal.isEnabled = true
                        binding.btnSubmitComplaintInternal.setOnClickListener {
                            if (binding.tvJumlahPekerja.visibility == View.VISIBLE) {
                                if (flag == 1) {
                                    binding.btnSubmitComplaintInternal.isEnabled = false
                                    bottomDialogWorkReport()
                                }
                                flag = 0
                            } else {
                                if (binding.etJumlahPekerja.text.toString() == "" || totalWorker.isNullOrEmpty()) {
                                    binding.btnSubmitComplaintInternal.setOnClickListener {
                                        Toast.makeText(
                                            this,
                                            "Jumlah pekerja tidak bisa kosong",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    if (flag == 1) {
                                        binding.btnSubmitComplaintInternal.isEnabled = false
                                        bottomDialogWorkReport()
                                    }
                                    flag = 0
                                }
                            }
                            Log.d("agri", "click btn $flag")
                        }
                    } else {
                        // set on click upload after image
                        binding.ivAfterDefault.visibility = View.VISIBLE
                        binding.ivAfterComplaintInternal.visibility = View.GONE
                        binding.rlUploadAfterImageComplaintInternal.setOnClickListener {
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
                    binding.ivProcessDefault.visibility = View.VISIBLE
                    binding.ivProcessComplaintInternal.visibility = View.GONE
                    binding.rlUploadProcessImageComplaintInternal.setOnClickListener {
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
                    binding.rlUploadAfterImageComplaintInternal.setOnClickListener {
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

    private fun onWaitingComplaint() {

        Log.d("DetailComplaintTag", "setObserver: idJobType = $idWorkTypeCftalk")
        Log.d("DetailComplaintTag", "setObserver: clickFrom = $clickFrom")
        setSpinnerWorkType()

        // set progress bar color
        binding.ivStatusWaitingDetailCftalk.setImageResource(R.drawable.ic_progress_red)
        binding.ivStatusOnprogressDetailCftalk.setImageResource(R.drawable.ic_progress_default)
        binding.ivStatusDoneDetailCftalk.setImageResource(R.drawable.ic_progress_default)
        binding.ivStatusCloseDetailCftalk.setImageResource(R.drawable.ic_progress_default)

        // set progress bar text color
        binding.tvProgressWaitingDetailCftalk.setTextColor(resources.getColor(R.color.red1))
        binding.tvProgressOnProgressDetailCftalk.setTextColor(resources.getColor(R.color.grey3))
        binding.tvProgressDoneDetailCftalk.setTextColor(resources.getColor(R.color.grey3))
        binding.tvProgressCloseDetailCftalk.setTextColor(resources.getColor(R.color.grey3))

        // button
        binding.llButtonProcessComplaintInternal.visibility = View.VISIBLE
        binding.llButtonSubmitComplaintInternal.visibility = View.GONE
        binding.llButtonCloseComplaintInternal.visibility = View.GONE

        // reply complaint
        binding.etReplyFromLeaderComplaintInternal.addTextChangedListener {
            if (binding.etReplyFromLeaderComplaintInternal.text.toString() == "") {
                binding.rlButtonProcessComplaintInternal.setBackgroundResource(R.drawable.bg_disable)
            } else {
                binding.rlButtonProcessComplaintInternal.setBackgroundResource(R.drawable.bg_primary)
                binding.btnProcessComplaintInternal.setOnClickListener {
                    if (flag == 1) {
                        binding.btnProcessComplaintInternal.isEnabled = false
                        showLoading(getString(R.string.loading_string2), "btnProcess")
                    }
                    flag = 0
                }
            }
        }
    }

    private fun setSpinnerWorkType() {
        viewModel.getJobsTypeComplaintInternal()
        viewModel.jobsTypeComplaintResponse.observe(this) {
            if (it.code == 200) {
                val workTypes = ArrayList<String>()
                val length = it.data.size
                Log.d("CreateCftalkTag", "onCreate: data = ${it.data}")
                for (i in 0 until length) {
                    workTypes.add(it.data[i].nameTypeJob)
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item,
                    workTypes
                )
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                binding.spinnerWorkTypeComplaintInternal.adapter = adapter

                binding.spinnerWorkTypeComplaintInternal.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parentView: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            long: Long
                        ) {
                            CarefastOperationPref.saveInt(
                                CarefastOperationPrefConst.JOB_TYPE_COMPLAINT,
                                it.data[position].idTypeJob
                            )
                            idWorkTypeCtalk = it.data[position].idTypeJob
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }
            } else {
                Toast.makeText(this, "tidak ada jenis pekerjaan", Toast.LENGTH_SHORT).show()
            }
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

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    Glide.with(applicationContext).load(uri)
                        .into(binding.ivResultUploadComplaintInternal)

//                    binding.ablComplaintInternal.visibility = View.GONE
                    binding.layoutComplaintInternal.visibility = View.GONE
                    binding.llProgressStatusBarDetailCftalk.visibility = View.GONE
                    binding.llResultUploadImageComplaintInternal.visibility = View.VISIBLE

                    // button upload before, process, after image
                    binding.btnUploadImageComplaintInternal.setOnClickListener {
                        if (flag == 1) {
                            binding.btnUploadImageComplaintInternal.isEnabled = false
                            if (binding.ivResultUploadComplaintInternal.drawable == null) {
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

    private fun createTempFiles(bitmap: Bitmap): File? {
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "process-complaint-internal.JPEG"
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

    private fun setImages(image: String?, imageView: ImageView) {
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
        viewModel.getDetailComplaintInternal(complaintId)
    }

    private fun showLoading(loadingText: String, clickFrom: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        val totalWorkers = if (binding.rlInputJumlahOrangPekerja.visibility == View.VISIBLE) {
            binding.etJumlahPekerja.text.toString()
        } else {
            CarefastOperationPref.loadString(
                CarefastOperationPrefConst.TOTAL_WORKER_DETAIL_CFTALK,
                ""
            )
        }

        when (clickFrom) {
            "btnProcess" -> {
                if (this.clickFrom == "cTalk") {
                    viewModel.putProcessComplaintInternal(
                        complaintId,
                        employeeId,
                        "" + binding.etReplyFromLeaderComplaintInternal.text.toString(),
                        idWorkTypeCtalk
                    )
                } else {
                    viewModel.putProcessComplaintInternal(
                        complaintId,
                        employeeId,
                        "" + binding.etReplyFromLeaderComplaintInternal.text.toString(),
                        CarefastOperationPref.loadInt(
                            CarefastOperationPrefConst.JOB_TYPE_COMPLAINT,
                            0
                        )
                    )
                }
            }

            "btnUploadPhoto" -> loadUploadPhoto()
            "btnClose" -> viewModel.putCloseComplaintInternal(complaintId)
            "btnSubmit" -> if (totalWorkers.isEmpty()){
                viewModel.putSubmitComplaintInternal(
                    complaintId,
                    employeeId,
                    reportComments,
                    totalWorker = 0,
                    idChemicals
                )
            } else {
                viewModel.putSubmitComplaintInternal(
                    complaintId,
                    employeeId,
                    reportComments,
                    totalWorkers.toInt(),
                    idChemicals
                )
            }
        }
    }

    private fun loadUploadPhoto() {
        // bitmap
        val bitmap: Bitmap =
            (binding.ivResultUploadComplaintInternal.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val imageUpload = MultipartBody.Part.Companion.createFormData(
            "file",
            file?.name,
            reqFile!!
        )
        when (onUploadClick) {
            "before" -> viewModel.putBeforeImageComplaintInternal(
                complaintId,
                employeeId,
                imageUpload
            )

            "process" -> viewModel.putProgressImageComplaintInternal(
                complaintId,
                employeeId,
                imageUpload
            )

            "after" -> viewModel.putAfterImageComplaintInternal(
                complaintId,
                employeeId,
                imageUpload
            )
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun bottomDialogWorkReport() {
        val dialog = BottomSheetDialog(this)
//        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_work_report)
        val etReport = dialog.findViewById<AppCompatEditText>(R.id.etBottomWorkReport)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnAppliedBottomDefaultChoose)
        var flag = 1

        button?.setOnClickListener {
            if (etReport?.text.toString() == "") {
                Toast.makeText(this, "Laporan tidak bisa kosong", Toast.LENGTH_SHORT).show()
            } else {
                reportComments = etReport?.text.toString()
                if (flag == 1) {
                    button.isEnabled = false
                    showLoading(getString(R.string.loading_string2), "btnSubmit")
                }
                flag = 0
//                viewModel.putSubmitComplaintInternal(
//                    complaintId,
//                    employeeId,
//                    etReport.text.toString(),
//                    totalWorker.toInt(),
//                    idChemicals
//                )
            }
        }

        viewModel.submitComplaintInternalModel.observe(this) {
            if (it.code == 200) {
                hideLoading()

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.TOTAL_WORKER_DETAIL_CFTALK,
                    ""
                )
                Toast.makeText(this, "Berhasil menyelesaikan komplain", Toast.LENGTH_SHORT).show()

                setResult(Activity.RESULT_OK, Intent())
                finish()

                dialog.dismiss()
            } else {
                hideLoading()
                this.flag = 1
                binding.btnSubmitComplaintInternal.isEnabled = true
                Toast.makeText(this, "Gagal menyelesaikan komplain", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun bottomDialogWorkReportVisitor(){
        val dialog = BottomSheetDialog(this)
//        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_work_report)
        val etReport = dialog.findViewById<AppCompatEditText>(R.id.etBottomWorkReport)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnAppliedBottomDefaultChoose)
        var flag = 1

        button?.setOnClickListener {
            if (etReport?.text.toString() == "") {
                Toast.makeText(this, "Laporan tidak bisa kosong", Toast.LENGTH_SHORT).show()
            } else {
                reportComments = etReport?.text.toString()
                if (flag == 1) {
                    button.isEnabled = false
                    showLoading(getString(R.string.loading_string2), "btnSubmit")
                }
                flag = 0
                val chemicalIDVisitor = ArrayList<Int>()
                viewModel.putSubmitComplaintInternal(
                    complaintId,
                    employeeId,
                    etReport!!.text.toString(),
                    totalWorker.toInt(),
                    chemicalIDVisitor
                )
            }
        }

        viewModel.submitComplaintInternalModel.observe(this) {
            if (it.code == 200) {
                hideLoading()

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.TOTAL_WORKER_DETAIL_CFTALK,
                    ""
                )
                Toast.makeText(this, "Berhasil menyelesaikan komplain", Toast.LENGTH_SHORT).show()

                setResult(Activity.RESULT_OK, Intent())
                finish()

                dialog.dismiss()
            } else {
                hideLoading()
                this.flag = 1
                binding.btnSubmitComplaintInternal.isEnabled = true
                Toast.makeText(this, "Gagal menyelesaikan komplain", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun bottomDialogChemicals() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_default_choose)
        val title = dialog.findViewById<TextView>(R.id.tvTitleBottomDefaultChoose)
        val info = dialog.findViewById<TextView>(R.id.tvInfoBottomDefaultChoose)
        val close = dialog.findViewById<ImageView>(R.id.ivCloseBottomDefaultChoose)
        val rvChemicals = dialog.findViewById<RecyclerView>(R.id.rvBottomDefaultChoose)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnAppliedBottomDefaultChoose)

        info!!.visibility = View.INVISIBLE
        close!!.visibility = View.INVISIBLE
        title?.text = "Pilih Bahan Chemical"

        // set default list chemical
        idChemicals.clear()
        nameChemicals.clear()

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvChemicals?.layoutManager = layoutManager

        viewModel.getChemicalsComplaint()
        viewModel.chemicalsComplaintResponse.observe(this) {
            if (it.code == 200) {
                val adapter = ChemicalsComplaintAdapter(
                    it.data as ArrayList<Data>
                ).also { it1 -> it1.setListener(this) }
                rvChemicals?.adapter = adapter
            } else {
                Toast.makeText(this, "Gagal mengambil data chemicals", Toast.LENGTH_SHORT).show()
            }
        }

        // set button submit
        button?.setOnClickListener {
            if (idChemicals.isEmpty()) {
                binding.ivEditBahanChemicalCfTalk.visibility = View.INVISIBLE
                binding.rvBahanChemicalCfTalk.visibility = View.GONE
                binding.tvAddBahanChemicalCftalk.visibility = View.VISIBLE
            } else {
                binding.ivEditBahanChemicalCfTalk.visibility = View.VISIBLE
                binding.rvBahanChemicalCfTalk.visibility = View.VISIBLE
                binding.tvAddBahanChemicalCftalk.visibility = View.GONE

                binding.rvBahanChemicalCfTalk.adapter = SelectedChemicalsAdapter(nameChemicals)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onBackPressed() {
        if (intentNotif == "notification") {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.NOTIF_INTENT, "")
            setResult(Activity.RESULT_OK, Intent())
            finish()
        } else {
            super.onBackPressed()
            finish()
        }
    }

    override fun onClickChemical(chemicalId: Int, chemicalName: String, selected: String) {
        when (selected) {
            "true" -> {
                idChemicals.add(chemicalId)
                nameChemicals.add(chemicalName)
            }

            "false" -> {
                idChemicals.remove(chemicalId)
                nameChemicals.remove(chemicalName)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

}