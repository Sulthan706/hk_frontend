package com.hkapps.hygienekleen.features.features_client.overtime.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailOvertimeChangeClientBinding
import com.hkapps.hygienekleen.features.features_client.overtime.viewmodel.OvertimeClientViewModel
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class DetailOvertimeChangeClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOvertimeChangeClientBinding
    private var overtimeId: Int = 0

    private val viewModel: OvertimeClientViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeClientViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOvertimeChangeClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // get overtime id
        overtimeId = intent.getIntExtra("overtimeIdChangeClient", 0)
        Log.d("DetailOvertimeChangeCli", "onCreate: overtimeId = $overtimeId")

        // change status bar color
        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

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
        viewModel.detailOvertimeChangeClientModel().observe(this) {
            if (it.code == 200) {
                binding.tvTitleDetailOvertimeChangeClient.text = it.data.title
                binding.tvDateDetailOvertimeChangeClient.text = it.data.atDate
                binding.tvShiftDetailOvertimeChangeClient.text = "${it.data.shiftDescription} (${it.data.startAt}-${it.data.endAt})"
                binding.tvLocationDetailOvertimeChangeClient.text = it.data.locationName
                binding.tvSubLocDetailOvertimeChangeClient.text = it.data.subLocationName

                binding.tvEmployeeDetailOvertimeChangeClient.text = it.data.employeeName
                binding.tvCreatedDetailOvertimeChangeClient.text = it.data.employeeNameCreated
                binding.tvReplaceDetailOvertimeChangeClient.text = it.data.employeeReplaceName

                binding.tvDescDetailOvertimeChangeClient.text = it.data.description

                binding.tvCreatedDetailOvertimeChangeClient.text = it.data.employeeNameCreated
                getImage(binding.ivCreatedDetailOvertimeChangeClient, it.data.employeePhotoProfileCreated)

                binding.tvEmployeeDetailOvertimeChangeClient.text = it.data.employeeName
                getImage(binding.ivEmployeeDetailOvertimeChangeClient, it.data.employeePhotoProfile)

                binding.tvReplaceDetailOvertimeChangeClient.text = it.data.employeeReplaceName
                getImage(binding.ivReplaceDetailOvertimeChangeClient, it.data.employeeReplacePhotoProfile)
            }
        }
    }

    private fun loadData() {
        viewModel.getDetailOvertimeChangeClient(overtimeId)
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