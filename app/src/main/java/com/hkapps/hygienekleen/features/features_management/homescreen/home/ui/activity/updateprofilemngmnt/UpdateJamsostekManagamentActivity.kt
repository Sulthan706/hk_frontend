package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityUpdateJamsostekManagamentBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment.bot_bpjs_jamsos.BotUpdateNumbBpjsMgmntFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class UpdateJamsostekManagamentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateJamsostekManagamentBinding
    private val homeManagementViewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }
    private var adminId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID,0)
    private var numberJamsostek =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NUMBER_BPJSTK_MANAGEMENT, "")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateJamsostekManagamentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbarJamsostek.tvAppbarTitle.text = "BPJS Ketenagakerjaan"
        binding.appbarJamsostek.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }



        binding.btnJamsostekUpdate.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_BPJS_DOCUMENT, "BPJS_KETENAGAKERJAAN")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_BPJS, "BPJSKET")
            BotUpdateNumbBpjsMgmntFragment().show(supportFragmentManager, "botsheetbpjsmngmnt")
        }

        if (numberJamsostek == ""){
            binding.etNumbJamsostek.hint = "Nomer BPJS Ketenagakerjaan"
        }

        if (binding.tvNumbJamsostek.text!!.isNotEmpty()){
            binding.etNumbJamsostek.hint = "Nomer Bpjs Ketenagakerjaan"
        }

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //oncreate
    }

    private fun loadData() {
        homeManagementViewModel.getListDocumentManagement(adminId)
    }

    private fun setObserver() {
        homeManagementViewModel.getListDocumentManagementViewModel().observe(this){
            if (it.code == 200){
                binding.tvNumbJamsostek.setText(it?.data?.adminBpjsKetenagakerjaan ?: "")

                if (it.data.adminBpjsKetenagakerjaanFile.isNullOrEmpty()){
                    binding.ivJamsostek.visibility = View.GONE
                } else {
                    binding.ivJamsostek.visibility = View.VISIBLE
                }
                loadProfileDefault(it.data.adminBpjsKetenagakerjaanFile)
            }
        }
    }
    private fun loadProfileDefault(img: String) {
        val url =
            getString(R.string.url) + "assets.admin_master/images/document/$img"
        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/logo_splash" // where myresource (without the extension) is the file
            val imageResource = resources.getIdentifier(uri, null, this.packageName)
            val res = resources.getDrawable(imageResource)
            binding.ivJamsostek.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(binding.ivJamsostek)
        }
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

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }
}