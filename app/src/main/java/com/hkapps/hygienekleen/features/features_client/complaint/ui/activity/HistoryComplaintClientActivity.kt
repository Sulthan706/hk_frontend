package com.hkapps.hygienekleen.features.features_client.complaint.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHistoryComplaintClientBinding
import com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter.ViewPagerHistoryComplaintAdapter
import com.hkapps.hygienekleen.features.features_client.complaint.viewmodel.ClientComplaintViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.tabs.TabLayout

class HistoryComplaintClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryComplaintClientBinding
    private val tabTitles = arrayListOf("Semua", "Menunggu", "Dikerjakan", "Selesai")
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")

    private val viewModel: ClientComplaintViewModel by lazy {
        ViewModelProviders.of(this).get(ClientComplaintViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryComplaintClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this,R.color.secondary_color)

        // set appbar layout
        binding.appbarHistoryComplaint.tvAppbarTitle.text = "Daftar CTalk"
        binding.appbarHistoryComplaint.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // setup tab layout
        binding.viewPagerHistoryComplaint.adapter = ViewPagerHistoryComplaintAdapter(supportFragmentManager, this)
        binding.tabLayoutHistoryComplaint.setupWithViewPager(binding.viewPagerHistoryComplaint)
        binding.tabLayoutHistoryComplaint.tabMode = TabLayout.MODE_SCROLLABLE
        binding.viewPagerHistoryComplaint.currentItem = 0

        // set on click create complaint
        binding.ivCreateComplaintClient.setOnClickListener {
            viewModel.getValidateCreateCtalk(projectCode)
            viewModel.validateCreateComplaintClientResponse.observe(this) {
                when (it.message) {
                    "VALID" -> {
                        binding.tvInfoValidationCreateComplaintClient.visibility = View.INVISIBLE
                        val i = Intent(this, ComplaintActivity::class.java)
                        startActivity(i)
                    }
                    "INVALID" -> {
                        binding.tvInfoValidationCreateComplaintClient.visibility = View.VISIBLE
                        Handler().postDelayed({
                            binding.tvInfoValidationCreateComplaintClient.visibility = View.INVISIBLE
                        }, 2000)
                    }
                    else -> {
                        Toast.makeText(
                            this,
                            "Gagal mengambil status validasi komplain",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

}