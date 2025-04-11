package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailRoutineReportBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.viewmodel.RoutineViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class DetailRoutineReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRoutineReportBinding
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val mClickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.M_CLICK_FROM, "")
    private val branchName = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_NAME_ROUTINE, "")
    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private val idRoutine = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ROUTINE_ID_INSPEKSI, 0)

    private val viewModel: RoutineViewModel by lazy {
        ViewModelProviders.of(this).get(RoutineViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRoutineReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarDetailRoutineReport.tvAppbarTitle.text = "Laporan Kunjungan Rutin"
        binding.appbarDetailRoutineReport.ivAppbarBack.setOnClickListener{
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.detailRoutineResponse.observe(this) {
            if (it.code == 200) {
                binding.tvBranchDetailRoutineReport.text = branchName ?: "-"
                binding.tvProjectDetailRoutineReport.text = it.data.projectName
                binding.tvCreatedDetailRoutineReport.text = userName
                binding.tvDateDetailRoutineReport.text = it.data.date
                binding.tvTopikDetailRoutineReport.text = it.data.title
                binding.tvNoteDetailRoutineReport.text = it.data.description ?: "-"
                binding.tvUploadImageRoutineReport.text = it.data.fileDescription ?: "-"

                setPhoto(it.data.file, binding.ivResultUriRoutineReport)
            }
        }
    }

    private fun setPhoto(img: String, imageView: ImageView) {
        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/error_image" // where myresource (without the extension) is the file
            val imaResource =
                resources.getIdentifier(uri, null, packageName)
            val res = resources.getDrawable(imaResource)
            imageView.setImageDrawable(res)
        } else {
            val url = getString(R.string.url) + "assets.admin_master/files/inspection/$img"
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(imageView)
        }
    }

    private fun loadData() {
        viewModel.getDetailRoutine(idRoutine)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }
}