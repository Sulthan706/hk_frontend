package com.hkapps.hygienekleen.features.features_vendor.service.resign.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListReqResignVendorBinding
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.listresignvendor.DataListResignVendor
import com.hkapps.hygienekleen.features.features_vendor.service.resign.ui.adapter.ListResignVendorAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.resign.viewmodel.ResignViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListReqResignVendorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListReqResignVendorBinding
    private val viewModel: ResignViewModel by lazy {
        ViewModelProviders.of(this).get(ResignViewModel::class.java)
    }
    private lateinit var adapters: ListResignVendorAdapter
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var statsResign = ArrayList<String>()
    private var statusResignValidate: Boolean = false
    private var statusResignApproval: Boolean = false


    //        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATS_RESIGN, "")
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListReqResignVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBarResignList.tvAppbarTitle.text = "Pengajuan Resign Saya"
        binding.appBarResignList.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        val fabColorNormal =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.secondary_color))
        binding.fabCreateResign.backgroundTintList = fabColorNormal

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCardResignVendor.layoutManager = layoutManager

        //validate stats button fab
        binding.fabCreateResign.setOnClickListener {
            if (statsResign.toTypedArray().isEmpty()) {
                startActivity(Intent(this, FormReqResignVendorActivity::class.java))
            } else {
                if (statusResignValidate) {
                    Toast.makeText(this, "Tunggu proses approval selesai", Toast.LENGTH_SHORT)
                        .show()
                } else if (statusResignApproval) {
                    Toast.makeText(this, "Pengajuan anda sudah disetujui", Toast.LENGTH_SHORT)
                        .show()
                } else
                    {
                        startActivity(Intent(this, FormReqResignVendorActivity::class.java))
                    }
                }
            }

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun loadData() {
        viewModel.getListResignVendor(userId)
    }

    private fun setObserver() {
        viewModel.getListResignVendorViewModel().observe(this) {
            if (it.code == 200) {
                if (it.data.isEmpty()) {
                    binding.tvEmptyResignVendor.visibility = View.VISIBLE
                } else {

                    val length = it.data.size
                    Log.d("TAG", "length: $length")
                    for (i in 0 until length) {
                        statsResign.add(it.data[i].status)
                        validateStatus(statsResign.toTypedArray())
                        Log.d("kece", "${validateStatus(statsResign.toTypedArray())}")
                        Log.d("kece", "$statsResign")
                        if (validateStatus(statsResign.toTypedArray())) {
                            statusResignValidate = true
                        }
                        if (validateStatusApproval(statsResign.toTypedArray())) {
                            statusResignApproval = true
                        }

                    }

                    binding.rvCardResignVendor.visibility = View.VISIBLE
                    binding.tvEmptyResignVendor.visibility = View.GONE

                    adapters = ListResignVendorAdapter(
                        supportFragmentManager,
                        it.data as ArrayList<DataListResignVendor>
                    )
                    binding.rvCardResignVendor.adapter = adapters
                }

            }
        }
    }

    private fun validateStatus(statusArray: Array<String>): Boolean {
        return statusArray.contains("Menunggu Approval") || statusArray.contains("Escalated to HC")
    }

    private fun validateStatusApproval(statusArray: Array<String>): Boolean {
        return statusArray.contains("Disetujui")
    }



    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }
}