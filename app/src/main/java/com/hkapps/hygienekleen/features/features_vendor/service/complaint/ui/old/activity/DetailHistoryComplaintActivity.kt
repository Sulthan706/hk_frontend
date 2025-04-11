package com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.old.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailHistoryComplaintBinding
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.viewmodel.VendorComplaintViewModel

class DetailHistoryComplaintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryComplaintBinding
    private var complaintId: Int = 0
    private var processPhotoProfile: String = ""
    private var processName: String? = null
    private var processBy: Int? = null
    private var workerId: Int? = null
    private var beforeImage: String? = null
    private var processImage: String? = null
    private var afterImage: String? = null
    private var workerName: String? = null

    private val viewModel: VendorComplaintViewModel by lazy {
        ViewModelProviders.of(this).get(VendorComplaintViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryComplaintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this,R.color.secondary_color)

        complaintId = intent.getIntExtra("complaintIdClient", 0)

        // set appbar layout
        binding.appbarDetailHistoryClient.tvAppbarTitle.text = "Riwayat CTalk"
        binding.appbarDetailHistoryClient.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        loadData()
//        setObserver()
    }

//    private fun setObserver() {
////        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
////            if (isLoading != null) {
////                if (isLoading) {
////                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
////                } else {
////                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
////                        // shimmer effect disini
////                    }, 1500)
////                }
////            }
////        })
//        viewModel.clientComplaintDetailHistoryResponseModel.observe(this) { it ->
//            if (it.code == 200) {
//                binding.tvTitleDetailHistoryClient.text = it.data.title
//                binding.tvDateDetailHistoryClient.text = it.data.date
//                binding.tvTimeDetailHistoryClient.text = it.data.time
//                binding.tvLocationDetailHistoryClient.text = it.data.locationName
//                binding.tvSubLocDetailHistoryClient.text = it.data.subLocationName
//                binding.tvNoteDetailHistoryClient.text = it.data.description
//                val image = it.data.image
//                val url = getString(R.string.url) +"assets.admin_master/images/complaint/"
//                applicationContext.let {
//                    Glide.with(it)
//                        .load(url + image)
//                        .apply(RequestOptions.centerCropTransform())
//                        .into(binding.ivDetailHistoryComplaint)
//                }
//
//                // set data validation
//                processPhotoProfile = it.data.processByEmployeePhotoProfile
//                processName = it.data.processName
//                processBy = it.data.processBy
//                workerId = it.data.workerId
//                beforeImage = it.data.beforeImage
//                processImage = it.data.processImage
//                afterImage = it.data.afterImage
//                if (it.data.worker == null) {
//                    workerName = null
//                } else {
//                    workerName = it.data.worker.employeeName
//                }
//
//                // set processBy data
//                binding.tvOpsNameComplaintClient.text = it.data.processName
//                getPhotoProfile(binding.ivOpsComplaintClient, it.data.processByEmployeePhotoProfile)
//
//                when (it.data.statusComplaint) {
//                    "WAITING" -> {
//                        binding.llProcessDetailHistoryClient.visibility = View.GONE
//                    }
//                    "ON PROGRESS" -> {
//                        binding.llProcessDetailHistoryClient.visibility = View.VISIBLE
//                        onProgressComplaint(it.data.processByEmployeePhotoProfile)
//                    }
//                    "DONE" -> {
//                        binding.llProcessDetailHistoryClient.visibility = View.VISIBLE
//                        onDoneComplaint(it.data.processByEmployeePhotoProfile)
//                    }
//                    else -> {
//                        Log.d("ComplaintNotifiAct", "setObserver: status complaint not defined")
//                        Log.e("ComplaintNotifiAct", "setObserver: status complaint not defined")
//                    }
//                }
//            }
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun onDoneComplaint(profile: String) {
////        binding.llProcessDetailHistoryClient.visibility = View.VISIBLE
//        // set processBy data
////        binding.tvOpsNameComplaintClient.text = processName
////        getImage(binding.ivOpsComplaintClient, profile)
//
//        binding.tvStatusComplaintClient.text = "Selesai"
//
//        // set worker data
////        binding.tvOpsNameComplaintClient.text = workerName
//
//        // set images
//        binding.ivBeforeDefaultClient.visibility = View.GONE
//        binding.ivProcessDefaultClient.visibility = View.GONE
//        binding.ivAfterDefaultClient.visibility = View.GONE
//
//        binding.ivBeforeComplaintClient.visibility = View.VISIBLE
//        binding.ivProcessComplaintClient.visibility = View.VISIBLE
//        binding.ivAfterComplaintClient.visibility = View.VISIBLE
//
//        setImages(beforeImage, binding.ivBeforeComplaintClient)
//        setImages(processImage, binding.ivProcessComplaintClient)
//        setImages(afterImage, binding.ivAfterComplaintClient)
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun onProgressComplaint(profile: String) {
//        // set processBy data
////        binding.tvOpsNameComplaintClient.text = processName
////        getImage()
//
////        binding.llProcessDetailHistoryClient.visibility = View.VISIBLE
//
//        // set processBy data
////        binding.tvOpsNameComplaintClient.text = processName
////        getPhotoProfile(binding.ivOpsComplaintClient, processPhotoProfile)
//
//        binding.tvStatusComplaintClient.text = "Sedang Proses"
//
//        if (beforeImage == null) {
//            //            workerName == null && beforeImage == null -> {
//            //                binding.llProcessDetailHistoryClient.visibility = View.GONE
//            //            }
////            binding.llProcessDetailHistoryClient.visibility = View.VISIBLE
//
//            // set worker data
//            //binding.tvOpsNameComplaintClient.text = workerName
//            //Log.d("DetailHistoryComplaint", "onProgressComplaint: worker name = $workerName")
//
//            // set images
//            binding.ivBeforeDefaultClient.visibility = View.VISIBLE
//            binding.ivProcessDefaultClient.visibility = View.VISIBLE
//            binding.ivAfterDefaultClient.visibility = View.VISIBLE
//
//            binding.ivBeforeComplaintClient.visibility = View.GONE
//            binding.ivProcessComplaintClient.visibility = View.GONE
//            binding.ivAfterComplaintClient.visibility = View.GONE
//        } else {
////            binding.llProcessDetailHistoryClient.visibility = View.VISIBLE
////
////            // set worker data
////            binding.tvOpsNameComplaintClient.text = workerName
////            Log.d("DetailHistoryComplaint", "onProgressComplaint: worker name = $workerName")
//
//            // set images
//            if (beforeImage != null) {
//                binding.ivBeforeDefaultClient.visibility = View.GONE
//                binding.ivBeforeComplaintClient.visibility = View.VISIBLE
//                setImages(beforeImage, binding.ivBeforeComplaintClient)
//                if (processImage != null) {
//                    binding.ivProcessDefaultClient.visibility = View.GONE
//                    binding.ivProcessComplaintClient.visibility = View.VISIBLE
//                    setImages(processImage, binding.ivProcessComplaintClient)
//                    if (afterImage != null) {
//                        binding.ivAfterDefaultClient.visibility = View.GONE
//                        binding.ivAfterComplaintClient.visibility = View.VISIBLE
//                        setImages(afterImage, binding.ivAfterComplaintClient)
//                    } else {
//                        binding.ivAfterDefaultClient.visibility = View.VISIBLE
//                        binding.ivAfterComplaintClient.visibility = View.GONE
//                    }
//                } else {
//                    binding.ivProcessDefaultClient.visibility = View.VISIBLE
//                    binding.ivProcessComplaintClient.visibility = View.GONE
//                }
//            } else {
//                binding.ivBeforeDefaultClient.visibility = View.VISIBLE
//                binding.ivBeforeComplaintClient.visibility = View.GONE
//            }
//        }
//    }

    private fun getPhotoProfile(imageView: ImageView, image: String?) {
        Log.d("DetailHistoryClientAc", "getPhotoProfile: running")
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

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(imageView)
        }
    }

    private fun setImages(image: String?, imageView: ImageView) {
        applicationContext.let {
            Glide.with(it)
                .load(getString(R.string.url) +"assets.admin_master/images/complaint/$image")
                .apply(RequestOptions.centerCropTransform())
                .into(imageView)
        }
    }

    private fun loadData() {
        viewModel.getDetailHistoryComplaint(complaintId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}