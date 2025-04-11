package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityChecklistSpvBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.adapter.ViewPagerChecklistSpvAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel

class ChecklistSpvActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChecklistSpvBinding
    private lateinit var viewPagerAdapter: ViewPagerChecklistSpvAdapter
//    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
//    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val userId: Int = 6744
    private val projectCode: String = "CFHO"
    private var statusAbsen: String = "Bertugas"

    private val viewModel : ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChecklistSpvBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set view appbar
        binding.appbarChecklistSpv.tvAppbarTitle.text = "Checklist"
        binding.appbarChecklistSpv.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }
        binding.appbarChecklistSpv.ivAppbarSearch.setOnClickListener {
            val searchStaff = Intent(this, ChecklistSearchSpvActivity::class.java)
            startActivity(searchStaff)
        }
        binding.appbarChecklistSpv.ivAppbarTune.setOnClickListener {
            val filter = Intent(this, ChecklistFilterSpvActivity::class.java)
            startActivity(filter)
        }

        // set tab layout & view pager
        viewPagerAdapter = ViewPagerChecklistSpvAdapter(supportFragmentManager, this)
        this.binding.vpChecklistSpv.adapter = viewPagerAdapter
        this.binding.tabLayoutChecklistSpv.setupWithViewPager(this.binding.vpChecklistSpv)
        binding.vpChecklistSpv.currentItem = 0

        if (statusAbsen == "Belum Absen") {
            binding.flAttendanceChecklistSpv.visibility = View.VISIBLE
            binding.vpChecklistSpv.visibility = View.GONE
            binding.tabLayoutChecklistSpv.visibility = View.GONE
            binding.appbarChecklistSpv.ivAppbarSearch.visibility = View.GONE
            binding.appbarChecklistSpv.ivAppbarTune.visibility = View.GONE
        } else {
            binding.flAttendanceChecklistSpv.visibility = View.GONE
            binding.vpChecklistSpv.visibility = View.VISIBLE
            binding.tabLayoutChecklistSpv.visibility = View.VISIBLE
            binding.appbarChecklistSpv.ivAppbarSearch.visibility = View.VISIBLE
            binding.appbarChecklistSpv.ivAppbarTune.visibility = View.VISIBLE
        }

        setObserver()
        loadData()

    }

    private fun loadData() {
        viewModel.getAttendanceStatus(userId, projectCode)
    }

    private fun setObserver() {
        viewModel.attendanceStatusResponse().observe(this, {
            if (it.code == 200) {
//                statusAbsen = it.data.statusAttendance
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}