package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailOvertimeChangeTlBinding
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.viewmodel.OvertimeViewModel

class DetailOvertimeChangeTlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOvertimeChangeTlBinding
    private var overtimeId: Int = 0

    private val viewModel: OvertimeViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOvertimeChangeTlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get overtime id
        overtimeId = intent.getIntExtra("overtimeChangeId", 0)

        // set app bar
        binding.appbarDetailChangeScheduleTeamlead.tvAppbarTitle.text = "Permohonan Lembur Ganti"
        binding.appbarDetailChangeScheduleTeamlead.ivAppbarBack.setOnClickListener {
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
                binding.tvTitleDetailOvertimeChangeTeamlead.text = it.data.title
                binding.tvDateDetailOvertimeChangeTeamlead.text = it.data.atDate
                binding.tvShiftDetailOvertimeChangeTeamlead.text = "${it.data.shiftDescription} (${it.data.startAt}-${it.data.endAt}"
                binding.tvLocationDetailOvertimeChangeTeamlead.text = it.data.locationName
                binding.tvSubLocDetailOvertimeChangeTeamlead.text = it.data.subLocationName
                binding.tvDescDetailOvertimeChangeTeamlead.text = it.data.description

                binding.tvEmployeeDetailOvertimeChangeTeamlead.text = it.data.employeeName
                getImage(binding.ivEmployeeDetailOvertimeChangeTeamlead, it.data.employeePhotoProfile)

                binding.tvCreatedDetailOvertimeChangeTeamlead.text = it.data.employeeNameCreated
                getImage(binding.ivCreatedDetailOvertimeChangeTeamlead, it.data.employeePhotoProfileCreated)

                binding.tvReplaceDetailOvertimeChangeTeamlead.text = it.data.employeeReplaceName
                getImage(binding.ivReplaceDetailOvertimeChangeTeamlead, it.data.employeeReplacePhotoProfile)
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