package com.hkapps.hygienekleen.features.features_management.damagereport.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailDamageReportManagementBinding
import com.hkapps.hygienekleen.features.features_management.damagereport.viewmodel.DamageReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.InternetCheckService
import com.google.android.material.button.MaterialButton
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class DetailDamageReportManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDamageReportManagementBinding
    private val viewModel: DamageReportManagementViewModel by lazy {
        ViewModelProviders.of(this)[DamageReportManagementViewModel::class.java]
    }
    private val idDamageReport =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_DAMAGE_REPORT, 0)
    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var statsDamageReport =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATS_DAMAGE_REPORT, false)
    private var keteranganAsset: String = ""
    private var loadingDialog: Dialog? = null

    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDamageReportManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appbarDetailDamageReportManagement.tvAppbarTitle.text = "Detail Kondisi Mesin"
        binding.appbarDetailDamageReportManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        //foto
        binding.rlBeforeBak.setOnClickListener {
            cameraBefore()
        }
        binding.rlAfterBak.setOnClickListener {
            cameraAfter()
        }
        //replace
        binding.ivReplaceImageUploadBakBefore.setOnClickListener {
            cameraBefore()
        }
        binding.ivReplaceImageUploadBakAfter.setOnClickListener {
            cameraAfter()
        }

        binding.btnSubmitBakManagement.setOnClickListener {
            showLoading(getString(R.string.loading_string2))
            submit()
        }

        binding.tvSubmitReportDamageReport.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                keteranganAsset = s.toString()
                if (!statsDamageReport) {
                    validateFields()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        loadData()
        setObserver()
        showLoading(getString(R.string.loading_string2))
        // Start the service
        val intent = Intent(this, InternetCheckService::class.java)
        startService(intent)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun cameraBefore() {
        val isCameraPermissionGranted = checkCameraPermission(this)
        if (isCameraPermissionGranted) {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_BAK_IMAGE, "FRONT")
            startActivity(Intent(this, UploadBakManagementActivity::class.java))
        } else {
            Toast.makeText(this, "Camera permission not allowed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cameraAfter() {
        val isCameraPermissionGranted = checkCameraPermission(this)
        if (isCameraPermissionGranted) {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.TYPE_BAK_IMAGE,
                "RESULTDAMAGE"
            )
            startActivity(Intent(this, UploadBakManagementActivity::class.java))
        } else {
            Toast.makeText(this, "Camera permission not allowed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCameraPermission(context: Context): Boolean {
        val cameraPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        return cameraPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun validateFields() {

        val isTextInputNotEmpty = keteranganAsset.isNotEmpty()
        val isImageNotEmpty = binding.ivBeforeBak.drawable != null
        val isImagesNotEmpty = binding.ivAfterBak.drawable != null

        if (isTextInputNotEmpty && isImageNotEmpty && isImagesNotEmpty) {
            binding.btnSubmitBakManagement.visibility = View.VISIBLE
        } else {
            binding.btnSubmitBakManagement.visibility = View.GONE
        }
    }

    private fun dialogSuccess() {
        val view = View.inflate(this, R.layout.dialog_success_upload_bak, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val tvBak = dialog.findViewById<TextView>(R.id.tvInfoBak)
        tvBak.text = "Anda sudah selesai mengerjakan perbaikan dan upload foto perbaikan mesin"
        val btnBack = dialog.findViewById<MaterialButton>(R.id.btnBackBakVendor)
        btnBack.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, ViewPagerBakManagementActivity::class.java))
            finish()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun loadData() {
        viewModel.getDetailDamageReportManagement(idDamageReport)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getDetailDamageReportMgmntViewModel().observe(this) {
            if (it.code == 200) {

                binding.tvNumberBAK.text = it?.data?.kodeBak ?: "-"
                binding.tvDateBAK.text = it?.data?.tglDibuat ?: "-"
                binding.tvEngineCode.text = (": " + it.data.kodeMesin).ifEmpty { "-" }
                binding.tvBrandEngine.text = (": " + it.data.merkMesin).ifEmpty { "-" }
                binding.tvTypeEngine.text = (": " + it.data.jenisMesin).ifEmpty { "-" }
                binding.tvKeteranganDamageReportManagement.setText(it?.data?.keteranganBak ?: "-")
                binding.tvSubmitReportDamageReport.setText(it.data.keteranganAssets)
                binding.tvProjectCode.text = (": " + it.data.projectName).ifEmpty { "-" }

                //validation card for image
                if (it.data.fotoDepan.isNullOrEmpty()) {
                    binding.rlBeforeBak.visibility = View.VISIBLE
                } else {
                    binding.rlBeforeBak.visibility = View.GONE

                    binding.mvImageBeforeBak.visibility = View.VISIBLE
                    binding.ivBeforeBak.visibility = View.VISIBLE
                }
                if (it.data.fotoKerusakan.isNullOrEmpty()) {
                    binding.rlAfterBak.visibility = View.VISIBLE
                } else {
                    binding.rlAfterBak.visibility = View.GONE

                    binding.mvImageAfterBak.visibility = View.VISIBLE
                    binding.ivAfterBak.visibility = View.VISIBLE
                }


                loadImage(
                    it.data.gambarDetailBak,
                    binding.ivDamageReport,
                    binding.progressIvDamageReport
                )
                loadImage(
                    it.data.fotoDepan,
                    binding.ivBeforeBak,
                    binding.progressIvBeforeDamageReport
                )
                loadImage(
                    it.data.fotoKerusakan,
                    binding.ivAfterBak,
                    binding.progressIvAfterDamageReport
                )

                if (it.data.validasiManagement == "FINISHED") {
                    binding.ivReplaceImageUploadBakBefore.visibility = View.GONE
                    binding.ivReplaceImageUploadBakAfter.visibility = View.GONE
                    binding.etSummaryResultDamageReport.isFocusable = false
                }

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
        viewModel.putUploadKeteranganBakViewModel().observe(this) {
            if (it.code == 200) {
                dialogSuccess()
            } else {
                Toast.makeText(this, "Gagal upload foto", Toast.LENGTH_SHORT).show()
            }
        }

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
            val url = getString(R.string.url) + "assets.admin_master/images/mesin/$img"
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


    private fun submit() {
        viewModel.uploadKeteranganBakManagement(idDamageReport, userId, keteranganAsset)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("INTERNET_STATUS")
        ContextCompat.registerReceiver(
            this,
            internetStatusReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        loadData()
    }

    private val internetStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isConnected = intent?.getBooleanExtra("isConnected", false) ?: false
            if (!isConnected) {
                Toast.makeText(
                    this@DetailDamageReportManagementActivity,
                    "Tidak ada koneksi internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(internetStatusReceiver)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }


}