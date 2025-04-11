package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityChecklistTeamleadBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.adapter.ChecklistViewPagerAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel

class ChecklistTeamleadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChecklistTeamleadBinding
    private lateinit var viewPagerAdapter: ChecklistViewPagerAdapter
//    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
//    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val userId: Int = 6744
    private val projectCode: String = "CFHO"
    private var statusAbsen: String = "Bertugas"

    private val viewModel: ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChecklistTeamleadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar layout
        binding.appbarChecklistTeamlead.tvAppbarTitle.text = "Checklist"
        binding.appbarChecklistTeamlead.ivAppbarTune.setOnClickListener {
            val i = Intent(this, ChecklistFilterTlActivity::class.java)
            startActivity(i)
        }
        binding.appbarChecklistTeamlead.ivAppbarSearch.setOnClickListener {
            val i = Intent(this, ChecklistSearchTlActivity::class.java)
            startActivity(i)
        }
        binding.appbarChecklistTeamlead.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // set tab layout & view pager
        viewPagerAdapter = ChecklistViewPagerAdapter(supportFragmentManager, this)
        this.binding.vpChecklistTl.adapter = viewPagerAdapter
        this.binding.tabLayoutChecklistTl.setupWithViewPager(this.binding.vpChecklistTl)
        binding.vpChecklistTl.currentItem = 0

        // set layout by status absen
        if (statusAbsen == "Belum Absen") {
            binding.flAttendanceChecklistTl.visibility = View.VISIBLE
            binding.vpChecklistTl.visibility = View.GONE
            binding.tabLayoutChecklistTl.visibility = View.GONE
            binding.appbarChecklistTeamlead.ivAppbarSearch.visibility = View.GONE
            binding.appbarChecklistTeamlead.ivAppbarTune.visibility = View.GONE
        } else {
            binding.flAttendanceChecklistTl.visibility = View.GONE
            binding.vpChecklistTl.visibility = View.VISIBLE
            binding.tabLayoutChecklistTl.visibility = View.VISIBLE
            binding.appbarChecklistTeamlead.ivAppbarSearch.visibility = View.VISIBLE
            binding.appbarChecklistTeamlead.ivAppbarTune.visibility = View.VISIBLE
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
//        val i = Intent(this, MainActivity::class.java)
//        startActivity(i)
//        finishAffinity()
    }

}