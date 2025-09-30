package com.hkapps.hygienekleen.features.features_client.complaint.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailHistoryComplaintBinding
import com.hkapps.hygienekleen.features.features_client.complaint.model.detailHistoryComplaint.VisitorObjectClient
import com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter.visitor.VisitorObjectClientAdapter
import com.hkapps.hygienekleen.features.features_client.complaint.viewmodel.ClientComplaintViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.adapter.SelectedChemicalsAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Locale

class DetailHistoryComplaintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryComplaintBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val intentNotif = CarefastOperationPref.loadString(CarefastOperationPrefConst.NOTIF_INTENT, "")
    private var complaintId: Int = 0
    private var processBy: Int? = null
    private var workerId: Int? = null
    private var beforeImage: String? = null
    private var processImage: String? = null
    private var afterImage: String? = null
    private var workerName: String? = null
    private var createdId: Int = 0
    private var loadingDialog: Dialog? = null
    private var complaintVisitorValidation: Boolean = false
    private lateinit var adapters: VisitorObjectClientAdapter
    private var nameChemicals = ArrayList<String>()

    private val viewModel: ClientComplaintViewModel by lazy {
        ViewModelProviders.of(this).get(ClientComplaintViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryComplaintBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

//        complaintId = intent.getIntExtra("complaintIdClient", 0)
        complaintId =
                CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_COMPLAINT_CLIENT, 0)

        // set appbar layout
        binding.appbarDetailHistoryClient.tvAppbarTitle.text = "Riwayat CTalk"
        binding.appbarDetailHistoryClient.ivAppbarBack.setOnClickListener {
            if (intentNotif == "notification"){
                CarefastOperationPref.saveString(CarefastOperationPrefConst.NOTIF_INTENT, "")
                setResult(Activity.RESULT_OK, Intent())
                finish()
            } else {
                super.onBackPressed()
                finish()
            }

        }
        //chemical init rv
        val layoutManagers = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvBahanChemicalCfTalk.layoutManager = layoutManagers
        //visitor object init rv
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvKomplainVisitorObjek.layoutManager = layoutManager

        Log.d("DetailComplainClientTag", "setObserver: createdId = $createdId")
        loadData()
        setObserver()
    }

    fun convertDate(inputDateStr: String): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id","ID"))

        try {
            val date = inputDateFormat.parse(inputDateStr)
            return outputDateFormat.format(date)
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    fun convertTime(inputDateStr: String): String {
        val inputDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val outputDateFormat = SimpleDateFormat("HH:mm", Locale("id","ID"))

        try {
            val date = inputDateFormat.parse(inputDateStr)
            return outputDateFormat.format(date)
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    fun convertTanggal(inputDateStr: String): String {
        val inputDateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
        val outputDateFormat = SimpleDateFormat("d MMM yyyy", Locale("id", "ID"))

        try {
            val date = inputDateFormat.parse(inputDateStr)
            return outputDateFormat.format(date)
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.clientComplaintDetailHistoryResponseModel.observe(this) { it ->
            if (it.code == 200) {
                complaintVisitorValidation = it.data.complaintType == "COMPLAINT_VISITOR"

                binding.tvTitleDetailComplaintInternal.text = it.data.title
                binding.tvDateTimeDoneDetailComplaintInternal.text =  if (it.data.time.isNullOrEmpty()){
                    "-"
                } else {
                    convertTanggal(it.data.date)
                }
                binding.tvDateTimeDetailComplaintInternal.text = "${convertTime(it.data.time)} ${convertTanggal(it.data.date)} "
                binding.tvLocationDetailComplaintInternal.text = it.data.locationName
                binding.tvLocationDetailComplaintInternal.text = it.data.subLocationName
                binding.tvNoteComplaintInternal.text = it.data.description
                binding.tvClientNameComplaintInternal.text = it?.data?.clientName ?: ""
                binding.tvEscalationInDetailComplaintInternal.text = it?.data?.escalation ?: ""
                binding.etReplyFromLeaderComplaintInternal.isEnabled = false
                binding.tvTimeStatusDoneComplaintInternal.text = it?.data?.doneAtTime ?: "00:00"
                binding.tvTimeStatusCloseComplaintInternal.text = it?.data?.closedAt ?: "00:00"
                binding.tvLaporanFromLeaderComplaintClient.text = it?.data?.reportComments ?: ""
                binding.tvWorkTypeComplaintInternal.visibility = View.GONE
                binding.spinnerWorkTypeComplaintInternal.visibility = View.GONE
                binding.tvWorkTypeComplaintVisitor.visibility = View.GONE

                //comments / balasan
                binding.etReplyFromLeaderComplaintInternal.setText(it?.data?.comments ?: "")

                //total worker
                binding.etJumlahPekerja.setText(it.data.totalWorkers.toString())

                //image
                binding.ivComplaintInternal.apply {
                    setImages(it.data.image, binding.ivComplaintInternal)
                    visibility = View.VISIBLE
                }



                // set chemical

                binding.rvBahanChemicalCfTalk.visibility = View.VISIBLE
                binding.tvAddBahanChemicalCftalk.visibility = View.GONE
                if (it.data.chemicalsName.isEmpty()) {
                    nameChemicals.add("Tidak menggunakan chemical")
                } else {
                    for (i in 0 until it.data.chemicalsName.size) {
                        nameChemicals.add(it.data.chemicalsName[i].chemicalsName)
                        Log.d("agri","$nameChemicals")
                    }
                }
                binding.rvBahanChemicalCfTalk.adapter =
                    SelectedChemicalsAdapter(nameChemicals)




//                binding.tvEscalationIn.text = "Eskalasi: " + it.data.notificationLevel

                val image = it.data.image
//                setImages(image, binding.ivDetailHistoryComplaint)

//                if (it.data.doneAtDate != null){
//                    binding.tvDateDoneDetailHistoryClient.text = it.data.doneAtDate
//                } else{
//                    binding.tvDateDoneDetailHistoryClient.text = ""
//                }

//                if (it.data.doneAtTime != null) {
//                    binding.tvTimeDoneDetailHistoryClient.text = it.data.doneAtTime
//                } else {
//                    binding.tvTimeDoneDetailHistoryClient.text = ""
//                }

                if (it.data.comments == "null" || it.data.comments == null || it.data.comments == "") {
                    binding.tvReplyFromLeaderComplaintClient.text = "Tidak ada balasan"
                } else {
                    binding.tvReplyFromLeaderComplaintClient.text = "" + it.data.comments
                }

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

                binding.ivComplaintInternal.setOnClickListener {
                    //pop up modal
                    val dialog = this.let { Dialog(it) }
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_custom_image_slide)
                    val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
                    val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView

//                    ivZoom.setImageDrawable(binding.ivDetailHistoryComplaint.drawable)
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
                Log.d("DetailComplainClientTag", "setObserver: createdId = $createdId")
                createdId = it.data.clientId
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
                binding.tvOpsNameComplaintInternal.text = it.data.processByEmployeeName
                getPhotoProfile(binding.ivOpsComplaintInternal, it.data.processByEmployeePhotoProfile)

                when (it.data.statusComplaint) {
                    "WAITING" -> {
                        binding.llProcessComplaintInternal.visibility = View.GONE
                        onWaitingComplaint()
                    }
                    "ON PROGRESS" -> {
                        binding.llProcessComplaintInternal.visibility = View.VISIBLE
                        onProgressComplaint()
                    }
                    "DONE" -> {
                        binding.llProcessComplaintInternal.visibility = View.VISIBLE
                        onDoneComplaint()
                    }
                    "CLOSE" -> {
                        binding.llProcessComplaintInternal.visibility = View.VISIBLE
                        onCloseComplaint()
                    }
                    else -> {
                        Log.d("ComplaintNotifiAct", "setObserver: status complaint not defined")
                        Log.e("ComplaintNotifiAct", "setObserver: status complaint not defined")
                    }
                }
                //visitor client
                if (complaintVisitorValidation){
                    binding.llFotoPengerjaanImage.visibility = View.GONE
                    binding.llFotoPengerjaan.visibility = View.GONE
                    binding.tvClientNameComplaintInternal.text = "VISITOR"
                    binding.tvTitleObjekComplaintVisitor.visibility = View.VISIBLE
                    binding.tvObjekItemComplaintVisitor.apply {
                        visibility = View.VISIBLE
                        text = it?.data?.visitorOption ?: "-"
                    }
                    binding.rvKomplainVisitorObjek.visibility = View.VISIBLE
                    //set rv
                    adapters = VisitorObjectClientAdapter(this, it.data.visitorObject as ArrayList<VisitorObjectClient>)
                    binding.rvKomplainVisitorObjek.adapter = adapters
                    //type work visitor
                    binding.tvWorkTypeComplaintInternal.visibility = View.GONE
                    binding.spinnerWorkTypeComplaintInternal.visibility = View.GONE
                    binding.tvWorkTypeComplaintVisitor.visibility = View.GONE

                    //set image
                    binding.ivComplaintInternal.visibility = View.GONE
                    binding.ivComplaintVisitor.visibility = View.VISIBLE

                }
            }
        }
        viewModel.closeComplaintResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                Toast.makeText(this, "Berhasil menutup complaint", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, Intent())
                finish()
            } else {
                hideLoading()
                Toast.makeText(this, "Gagal menutup complaint", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onWaitingComplaint() {
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

        binding.llDoneComplaintInternal.visibility = View.GONE
        binding.llStatusComplaintInternal.visibility = View.GONE

        binding.tvDateTimeDoneDetailComplaintInternal.text = "-"




        //visitor validation
        if (complaintVisitorValidation){
            binding.spinnerWorkTypeComplaintInternal.visibility = View.GONE
            binding.tvWorkTypeComplaintInternal.visibility = View.GONE
            binding.tvWorkTypeComplaintVisitor.visibility = View.GONE


        }
    }


    @SuppressLint("SetTextI18n")
    private fun onCloseComplaint() {

        // set progress bar color
        binding.ivStatusWaitingDetailCftalk.setImageResource(R.drawable.ic_progress_default)
        binding.ivStatusOnprogressDetailCftalk.setImageResource(R.drawable.ic_progress_default)
        binding.ivStatusDoneDetailCftalk.setImageResource(R.drawable.ic_progress_default)
        binding.ivStatusCloseDetailCftalk.setImageResource(R.drawable.ic_progress_secondary)

        // set progress bar text color
        binding.tvProgressWaitingDetailCftalk.setTextColor(resources.getColor(R.color.grey3))
        binding.tvProgressOnProgressDetailCftalk.setTextColor(resources.getColor(R.color.grey3))
        binding.tvProgressDoneDetailCftalk.setTextColor(resources.getColor(R.color.grey3))
        binding.tvProgressCloseDetailCftalk.setTextColor(resources.getColor(R.color.secondary_color))
        binding.llDoneComplaintInternal.visibility = View.VISIBLE


        // set worker data


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

        binding.cardDetailComplaintInternal.setOnClickListener {
            openImage(binding.ivBeforeComplaintInternal.drawable)
        }
        binding.cardDetailComplaintInternal.setOnClickListener {
            openImage(binding.ivProcessComplaintInternal.drawable)
        }
        binding.cardDetailComplaintInternal.setOnClickListener {
            openImage(binding.ivAfterComplaintInternal.drawable)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onDoneComplaint() {
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
        binding.llDoneComplaintInternal.visibility = View.VISIBLE

        binding.ivEditBahanChemicalCfTalk.visibility = View.INVISIBLE
        binding.tvAddBahanChemicalCftalk.visibility = View.GONE
        binding.rvBahanChemicalCfTalk.visibility = View.VISIBLE

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

        binding.cardDetailComplaintInternal.setOnClickListener {
            openImage(binding.ivBeforeComplaintInternal.drawable)
        }
        binding.cardDetailComplaintInternal.setOnClickListener {
            openImage(binding.ivProcessComplaintInternal.drawable)
        }
        binding.cardDetailComplaintInternal.setOnClickListener {
            openImage(binding.ivAfterComplaintInternal.drawable)
        }

        // set button close complaint
        Log.d("DetailHistoryCtalkTag", "onDoneComplaint: createdId = $createdId")
        Log.d("DetailHistoryCtalkTag", "onDoneComplaint: userId = $userId")

        //time line work
        binding.tvTimeStatusCloseComplaintInternal.visibility = View.GONE
        binding.tvStatusCloseComplaintInternal.visibility = View.GONE
        binding.tvStatusDoneComplaintInternal.text = "Sudah selesai dikerjakan, sedang menunggu ditutup"
        binding.tvTimeStatusDoneComplaintInternal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_aktive, 0, 0 ,0)
        //button submit
        if (!complaintVisitorValidation){
            binding.llButtonCloseComplaintInternal.visibility = View.VISIBLE
            binding.btnCloseComplaintClient.setOnClickListener {
                viewModel.putCloseComplaint(complaintId)
            }
        }



    }

    @SuppressLint("SetTextI18n")
    private fun onProgressComplaint() {
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

        binding.llProcessComplaintInternal.visibility = View.VISIBLE
        binding.tvStatusDoneComplaintInternal.text = "Sedang Proses"
        binding.tvDateTimeDoneDetailComplaintInternal.text = "-"

        binding.llStatusComplaintInternal.visibility = View.GONE
        binding.llDoneComplaintInternal.visibility = View.GONE
        binding.llProcessComplaintInternal.visibility = View.VISIBLE

        if (complaintVisitorValidation){
            binding.spinnerWorkTypeComplaintInternal.visibility = View.GONE
            binding.tvWorkTypeComplaintInternal.visibility = View.GONE
            binding.tvWorkTypeComplaintVisitor.visibility = View.GONE


        }


        if (beforeImage == null) {
            //            workerName == null && beforeImage == null -> {
            //                binding.llProcessDetailHistoryClient.visibility = View.GONE
            //            }

            // set worker data
//            binding.llProcessDetailHistoryClient.visibility = View.VISIBLE
//            binding.tvOpsNameComplaintClient.text = workerName
//            Log.d("DetailHistoryComplaint", "onProgressComplaint: worker name = $workerName")

            // set images
            binding.ivBeforeDefault.visibility = View.GONE
            binding.ivProcessDefault.visibility = View.GONE
            binding.ivAfterDefault.visibility = View.GONE

            binding.ivBeforeComplaintInternal.visibility = View.VISIBLE
            binding.ivProcessComplaintInternal.visibility = View.VISIBLE
            binding.ivAfterComplaintInternal.visibility = View.VISIBLE
        } else {
//            binding.llProcessDetailHistoryClient.visibility = View.VISIBLE
//
//            // set worker data
//            binding.tvOpsNameComplaintClient.text = workerName
//            Log.d("DetailHistoryComplaint", "onProgressComplaint: worker name = $workerName")

            // set images
            if (beforeImage != null) {
                binding.ivBeforeDefault.visibility = View.GONE
                binding.ivBeforeComplaintInternal.visibility = View.VISIBLE
                setImages(beforeImage, binding.ivBeforeComplaintInternal)
                binding.cardDetailComplaintInternal.setOnClickListener {
                    openImage(binding.ivBeforeComplaintInternal.drawable)
//                    Toast.makeText(this, "Muncul foto before", Toast.LENGTH_SHORT).show()
                }
                if (processImage != null) {
                    binding.ivProcessDefault.visibility = View.GONE
                    binding.ivProcessComplaintInternal.visibility = View.VISIBLE
                    setImages(processImage, binding.ivProcessComplaintInternal)
                    binding.cardDetailComplaintInternal.setOnClickListener {
                        openImage(binding.ivProcessComplaintInternal.drawable)
                    }
                    if (afterImage != null) {
                        binding.ivAfterDefault.visibility = View.GONE
                        binding.ivAfterComplaintInternal.visibility = View.VISIBLE
                        setImages(afterImage, binding.ivAfterComplaintInternal)
                        binding.cardDetailComplaintInternal.setOnClickListener {
                            openImage(binding.ivAfterComplaintInternal.drawable)
                        }
                    } else {
                        binding.ivAfterDefault.visibility = View.VISIBLE
                        binding.ivAfterComplaintInternal.visibility = View.GONE
                    }
                } else {
                    binding.ivProcessDefault.visibility = View.VISIBLE
                    binding.ivProcessComplaintInternal.visibility = View.GONE
                }
            } else {
                binding.ivBeforeDefault.visibility = View.VISIBLE
                binding.ivBeforeComplaintInternal.visibility = View.GONE
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
        val url = this.getString(R.string.url) + "assets.admin_master/images/complaint/$image"
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
        viewModel.getDetailHistoryComplaint(complaintId)
    }

    override fun onBackPressed() {
        if (intentNotif == "notification"){
            CarefastOperationPref.saveString(CarefastOperationPrefConst.NOTIF_INTENT, "")
            setResult(Activity.RESULT_OK, Intent())
            finish()
        } else {
            super.onBackPressed()
            finish()
        }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        viewModel.putCloseComplaint(complaintId)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
}