package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile

import BotUpdateNumbBpjsFragment
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityUpdateJamsostekBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class UpdateJamsostekActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUpdateJamsostekBinding
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID,0)
    private var numberJamsostek =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NUMBER_JAMSOSTEK,"")
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateJamsostekBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appbarJamsostek.tvAppbarTitle.text = "BPJS Ketenagakerjaan"
        binding.appbarJamsostek.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }


        binding.btnJamsostekUpdate.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_BPJS_DOCUMENT, "BPJS_KETENAGAKERJAAN")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_BPJS, "BPJSKET")
            BotUpdateNumbBpjsFragment().show(supportFragmentManager, "botsheetbpjs")

        }
        //empty jamsostek
        if (numberJamsostek == ""){
            binding.etNumbJamsostek.hint = "Nomor BPJS Ketenagakerjaan"
        }

        //validasi hint
        if (binding.tvNumbJamsostek.text!!.isNotEmpty()){
            binding.etNumbJamsostek.hint = "Nomor BPJS Ketenagakerjaan"
        }

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //oncreate
    }
    private fun loadData() {
        viewModel.getListDocument(userId)
    }

    private fun setObserver(){
        viewModel.getListDocumentViewModel().observe(this){
            if (it.code == 200){
                binding.tvNumbJamsostek.setText(it?.data?.employeeBpjsKetenagakerjaanNumber ?: "")
                if (it.data.employeeBpjsKetenagakerjaanFile.isNullOrEmpty()){
                    binding.ivJamsostek.visibility = View.GONE
                } else {
                    binding.ivJamsostek.visibility = View.VISIBLE
                }
                loadProfileDefault(it.data.employeeBpjsKetenagakerjaanFile)
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
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