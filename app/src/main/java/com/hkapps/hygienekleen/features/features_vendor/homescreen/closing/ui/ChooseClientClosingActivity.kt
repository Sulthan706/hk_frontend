package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityChooseClientClosingBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ClientClosing
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.viewmodel.ClosingManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter.ChooseClientClosingAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ChooseClientClosingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChooseClientClosingBinding

    private val closingViewModel by lazy {
        ViewModelProviders.of(this)[ClosingManagementViewModel::class.java]
    }

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val listChooseClient = ArrayList<ClientClosing>()

    private var i : String? = null

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseClientClosingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appBarName = "Daftar Klien"
        binding.appbarChooseClientClosing.tvAppbarTitle.text = appBarName
        binding.appbarChooseClientClosing.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        i = intent.getStringExtra("emailFor")

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvLaporanKondisiArea.layoutManager = layoutManager

        binding.btnEnableChooseClientMeeting.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putParcelableArrayListExtra("selectedDataList", ArrayList(listChooseClient))
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        loadData()
        setObserver()

    }

    private fun loadData() {
        val i = intent.getStringExtra("projectCode")
        if(i != null){
            closingViewModel.getListClientClosingManagement(i)
        }
    }

    private fun setObserver() {
        closingViewModel.clientClosingResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    binding.tv1ChooseClientMeeting.visibility = View.VISIBLE
                    binding.rvLaporanKondisiArea.visibility = View.VISIBLE
                    binding.tvEmptyChooseClientMeeting.visibility = View.GONE

                    val rvAdapter = ChooseClientClosingAdapter(java.util.ArrayList(it.data), object : ChooseClientClosingAdapter.OnClickChooseClientClosing{
                        override fun onChecked(
                            clientClosing: ClientClosing,
                            imageView: ImageView
                        ) {

                            if(i != null){
                                if(i.equals("to")){
                                    val clientClosingModify = ClientClosing(
                                        clientClosing.clientId,
                                        "to: ${clientClosing.clientName}",
                                        clientClosing.levelJabatan,
                                        clientClosing.projectCode,
                                        clientClosing.email,
                                        clientClosing.photoProfile,
                                        clientClosing.status,
                                        clientClosing.project
                                    )
                                    onChooseClient(clientClosingModify, imageView)
                                }else{
                                    val clientClosingModify = ClientClosing(
                                        clientClosing.clientId,
                                        "cc: ${clientClosing.clientName}",
                                        clientClosing.levelJabatan,
                                        clientClosing.projectCode,
                                        clientClosing.email,
                                        clientClosing.photoProfile,
                                        clientClosing.status,
                                        clientClosing.project
                                    )
                                    onChooseClient(clientClosingModify, imageView)
                                }
                            }
                        }

                    })
                    binding.rvLaporanKondisiArea.adapter = rvAdapter

                } else {
                    binding.tv1ChooseClientMeeting.visibility = View.GONE
                    binding.rvLaporanKondisiArea.visibility = View.GONE
                    binding.btnDisableChooseClientMeeting.visibility = View.GONE
                    binding.btnEnableChooseClientMeeting.visibility = View.GONE
                    binding.tvEmptyChooseClientMeeting.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(this, "Gagal mengambil list data klien", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onChooseClient(clientClosing: ClientClosing, imageView: ImageView) {
        if (imageView.tag == null) {
            imageView.tag = "check"
            imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_checkbox))
            listChooseClient.add(clientClosing)
        } else {
            if (imageView.tag == "check") {
                imageView.tag = "uncheck"
                imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_uncheckbox))
                listChooseClient.remove(clientClosing)
            } else {
                imageView.tag = "check"
                imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_checkbox))
                listChooseClient.add(clientClosing)
            }
        }

        // validate button choose
        if (listChooseClient.isNotEmpty()) {
            binding.btnDisableChooseClientMeeting.visibility = View.GONE
            binding.btnEnableChooseClientMeeting.visibility = View.VISIBLE
        } else {
            binding.btnDisableChooseClientMeeting.visibility = View.VISIBLE
            binding.btnEnableChooseClientMeeting.visibility = View.INVISIBLE
        }
    }


}