package com.hkapps.hygienekleen.features.features_management.damagereport.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.hkapps.hygienekleen.databinding.ActivityViewPagerBakManagementBinding
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.adapter.VwBakManagementAdapter
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.fragment.AllBakManagementFragment
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.fragment.BotSheetProjectBakFragment
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.fragment.FinishBakManagementFragment
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.fragment.NotFinishBakManagementFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ViewPagerBakManagementActivity : AppCompatActivity(), DateSelectedListener, ProjectSelectedListener,
    BotSheetProjectBakFragment.ProjectClickListener {
    private lateinit var binding: ActivityViewPagerBakManagementBinding
    private lateinit var adapter: VwBakManagementAdapter
    private var dates: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerBakManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBarBakManagement.tvAppbarTitle.text = "List BAK Mesin"
        binding.appBarBakManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        adapter = VwBakManagementAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Semua"
                1 -> "Belum selesai"
                2 -> "Selesai"
                else -> null
            }
        }.attach()

        binding.viewPager.offscreenPageLimit = 3

        binding.tvCalenderDamageReport.setOnClickListener {
            showDatePicker()
        }

        binding.autoCompleteProject.setOnClickListener {
            val fragment = BotSheetProjectBakFragment()
            fragment.projectClickListener = this
            fragment.show(supportFragmentManager, "YourFragmentTag")
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun showDatePicker() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            // Handle the user's selection
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateFormatString = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

            val selectedDate = Date(selection)
            val formattedDate = dateFormat.format(selectedDate)
            val formattedDates = dateFormatString.format(selectedDate)

            dates = formattedDate
            binding.tvCalenderDamageReport.setText(formattedDates)
            onDateSelected(formattedDate)

//            loadData()

        }

        picker.show(supportFragmentManager, picker.toString())
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }

    override fun onDateSelected(date: String) {
        for (i in 0 until adapter.itemCount) {
            val fragment = adapter.getFragment(i)
            when (fragment) {
                is AllBakManagementFragment -> fragment.updateDate(date)
                is NotFinishBakManagementFragment -> fragment.updateDate(date)
                is FinishBakManagementFragment -> fragment.updateDate(date)
            }
        }
    }

    override fun onProjectSelected(project: String) {
        for (i in 0 until adapter.itemCount) {
            val fragment = adapter.getFragment(i)
            when (fragment) {
                is AllBakManagementFragment -> fragment.updateProject(project)
                is NotFinishBakManagementFragment -> fragment.updateProject(project)
                is FinishBakManagementFragment -> fragment.updateProject(project)
            }
        }
    }

    override fun onProjectClicked(projectName: String, projectCode: String) {
        binding.autoCompleteProject.setText(projectName)
        onProjectSelected(projectCode)
    }



}