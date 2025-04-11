package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile

import BotUpdateNumbBpjsFragment
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityUpdateBpjsBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class UpdateBpjsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBpjsBinding
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID,0)
    private var numberBpjs =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NUMBER_BPJS,"")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateBpjsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbarKesehatan.tvAppbarTitle.text = "BPJS Kesehatan"
        binding.appbarKesehatan.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        binding.btnBpjsUpdate.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_BPJS_DOCUMENT, "BPJS_KESEHATAN")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_BPJS, "BPJSKES")
            BotUpdateNumbBpjsFragment("Nomor BPJS Kesehatan").show(supportFragmentManager, "botsheetbpjs")
        }

        //empty state numb bpjs
        if (numberBpjs == ""){
            binding.etNumbKesehatan.hint = "Nomor BPJS Kesehatan"
        }
        //validasi hint
        if (binding.tvNumbKesehatan.text!!.isNotEmpty()){
            binding.etNumbKesehatan.hint = "Nomor BPJS Kesehatan"
        }


        //oncreate
        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getListDocument(userId)
    }

    private fun setObserver() {
        viewModel.getListDocumentViewModel().observe(this){
            if (it.code == 200){
                binding.tvNumbKesehatan.setText(it?.data?.employeeBpjsKesehatanNumber ?: "")
                if (it.data.employeeBpjsKetenagakerjaanFile.isNullOrEmpty()){
                    binding.ivKesehatan.visibility = View.GONE
                } else {
                    binding.ivKesehatan.visibility = View.VISIBLE
                }
                loadProfileDefault(it.data.employeeBpjsKesehatanFile)
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //fun
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

    override fun onResume() {
        super.onResume()
        loadData()
    }

}