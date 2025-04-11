package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.operator.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailOvertimeChangeOprBinding
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.viewmodel.OvertimeViewModel

class DetailOvertimeChangeOprActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOvertimeChangeOprBinding
    private var overtimeId: Int = 0

    private val viewModel: OvertimeViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOvertimeChangeOprBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get overtime id
        overtimeId = intent.getIntExtra("overtimeId", 0)
        Log.d("DetailOvertimeChangeOpr", "onCreate: overtimeId = $overtimeId")

        // set layout appbar
        binding.appbarDetailOvertimeChangeOpr.tvAppbarTitle.text = "Permohonan Lembur Ganti"
        binding.appbarDetailOvertimeChangeOpr.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.detailOvertimeChangeOprModel().observe(this) {
            if (it.code == 200) {
                binding.tvTitleDetailOvertimeChangeOpr.text = it.data.title
                binding.tvDateDetailOvertimeChangeOpr.text = it.data.atDate
                binding.tvShiftDetailOvertimeChangeOpr.text = "${it.data.shiftDescription} (${it.data.startAt}-${it.data.endAt})"
                binding.tvLocationDetailOvertimeChangeOpr.text = it.data.locationName
                binding.tvSubLocDetailOvertimeChangeOpr.text = it.data.subLocationName
                binding.tvDescDetailOvertimeChangeOpr.text = it.data.description

                binding.tvCreatedDetailOvertimeChangeOpr.text = it.data.employeeNameCreated
                getImage(binding.ivCreatedDetailOvertimeChangeOpr, it.data.employeePhotoProfileCreated)

                binding.tvEmployeeDetailOvertimeChangeOpr.text = it.data.employeeName
                getImage(binding.ivEmployeeDetailOvertimeChangeOpr, it.data.employeePhotoProfile)

                binding.tvReplaceDetailOvertimeChangeOpr.text = it.data.employeeReplaceName
                getImage(binding.ivReplaceDetailOvertimeChangeOpr, it.data.employeeReplacePhotoProfile)
            }
        }
    }

    private fun loadData() {
        viewModel.getDetailOvertimeChangeOpr(overtimeId)
    }

    private fun getImage(imageView: ImageView, image: String) {
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

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}