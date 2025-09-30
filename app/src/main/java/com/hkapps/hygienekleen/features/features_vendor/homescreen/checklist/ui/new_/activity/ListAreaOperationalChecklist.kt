package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityListAreaOperationalChecklistBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter.ViewPagerAreaOpsAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ListAreaOperationalChecklist : AppCompatActivity() {

    private lateinit var binding: ActivityListAreaOperationalChecklistBinding
    private lateinit var adapter: ViewPagerAreaOpsAdapter
    private val shiftName = CarefastOperationPref.loadString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, "")
    private val shiftDesc = CarefastOperationPref.loadString(CarefastOperationPrefConst.CHECKLIST_SHIFT_DESC, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListAreaOperationalChecklistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set app bar
        binding.appbarListAreaOperationalChecklist.tvAppbarTitle.text = when(shiftName) {
            "WD" -> "Week Days"
            else -> shiftDesc
        }
        binding.appbarListAreaOperationalChecklist.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_PLOTTING_ID, 0)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_DESC, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, "")
            super.onBackPressed()
            finish()
        }
        binding.appbarListAreaOperationalChecklist.ivAppbarSearch.setOnClickListener {
            when(binding.viewPagerListAreaOperationalChecklist.currentItem) {
                0 -> {
                    val i = Intent(this, SearchAreaChecklistActivity::class.java)
                    startActivity(i)
                }
                else -> {
                    val i = Intent(this, SearchOperatorChecklistActivity::class.java)
                    startActivity(i)
                }
            }
        }

        // set tab layout & viewpager
        adapter = ViewPagerAreaOpsAdapter(supportFragmentManager, this)
        this.binding.viewPagerListAreaOperationalChecklist.adapter = adapter
        this.binding.tabLayoutListAreaOperationalChecklist.setupWithViewPager(this.binding.viewPagerListAreaOperationalChecklist)
        binding.viewPagerListAreaOperationalChecklist.currentItem = 0

    }

    override fun onBackPressed() {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_PLOTTING_ID, 0)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_DESC, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, "")
        super.onBackPressed()
        finish()
    }
}