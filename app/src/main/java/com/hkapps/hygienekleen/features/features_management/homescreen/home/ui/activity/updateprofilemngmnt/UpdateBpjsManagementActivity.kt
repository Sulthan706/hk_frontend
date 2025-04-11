package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityUpdateBpjsManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment.bot_bpjs_jamsos.BotUpdateNumbBpjsMgmntFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class UpdateBpjsManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBpjsManagementBinding
    private val homeManagementViewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID,0)
    private var numbersBpjs =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NUMBER_BPJS,"")


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateBpjsManagementBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbarKesehatan.tvAppbarTitle.text = "BPJS Kesehatan"
        binding.appbarKesehatan.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        binding.btnBpjsUpdate.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_BPJS, "BPJSKES")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_BPJS_DOCUMENT, "BPJS_KESEHATAN")
            BotUpdateNumbBpjsMgmntFragment("Nomor BPJS Kesehatan").show(supportFragmentManager, "botsheetupdatenum")
        }


        //validasi hint
        if (binding.tvNumbKesehatan.text!!.isNotEmpty()){
            binding.etNumbKesehatan.hint = "Nomer BPJS Kesehatan"
        }
        binding.tvNumbKesehatan.setText(numbersBpjs)

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //oncreate
    }

    private fun setObserver() {
        homeManagementViewModel.getListDocumentManagementViewModel().observe(this){
            if (it.code == 200){
                binding.tvNumbKesehatan.setText(it?.data?.adminBpjsKesehatan ?: "")
                if (it.data.adminBpjsKetenagakerjaanFile.isNullOrEmpty()){
                    binding.ivKesehatan.visibility = View.GONE
                } else {
                    binding.ivKesehatan.visibility = View.VISIBLE
                }
                loadProfileDefault(it.data.adminBpjsKesehatanFile)
            }
        }
    }

    private fun loadData() {
        homeManagementViewModel.getListDocumentManagement(userId)
    }

    private fun loadProfileDefault(img: String) {
        val url =
            getString(R.string.url) + "assets.admin_master/images/document/$img"
        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/logo_splash" // where myresource (without the extension) is the file
            val imageResource = resources.getIdentifier(uri, null, this.packageName)
            val res = resources.getDrawable(imageResource)
            binding.ivKesehatan.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(binding.ivKesehatan)
        }
    }

    //fun
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