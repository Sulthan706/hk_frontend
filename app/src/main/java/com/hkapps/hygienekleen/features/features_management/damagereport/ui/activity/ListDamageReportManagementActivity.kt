package com.hkapps.hygienekleen.features.features_management.damagereport.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityListDamageReportManagementBinding
import com.hkapps.hygienekleen.features.features_management.damagereport.model.listdamagereport.ContentDamageReportManagement
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.adapter.ListDamageReportManagementAdapter
import com.hkapps.hygienekleen.features.features_management.damagereport.viewmodel.DamageReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListDamageReportManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListDamageReportManagementBinding
    private val viewModel: DamageReportManagementViewModel by lazy {
        ViewModelProviders.of(this)[DamageReportManagementViewModel::class.java]
    }
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val projectName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECTNAME_MANAGEMENT,"")
    private lateinit var adapters: ListDamageReportManagementAdapter
    var page: Int = 0
    var isLastPage = false
    var date: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDamageReportManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appbarDamageReportManagement.tvAppbarTitle.text = projectName
        binding.appbarDamageReportManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDamageReportManagement.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
//                    loadData()
                }
            }
        }
        binding.rvDamageReportManagement.addOnScrollListener(scrollListener)

        binding.tvCalenderDamageReport.setOnClickListener {
            showDatePicker()
        }

//        loadData()
        setObserver()

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

//    private fun loadData() {
//        viewModel.getListDamageReportMgmnt(projectCode, date, page)
//    }

    private fun setObserver() {
        viewModel.getListDamageReportMgmntViewModel().observe(this){
            if (it.code == 200){
                if (it.data.content.isEmpty()){
                    binding.tvEmptyListBakManagement.visibility = View.VISIBLE
                    binding.rvDamageReportManagement.visibility = View.GONE
                } else {
                    binding.rvDamageReportManagement.visibility = View.VISIBLE
                    binding.tvEmptyListBakManagement.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0){
                        adapters = ListDamageReportManagementAdapter(this, it.data.content as ArrayList<ContentDamageReportManagement>)
                        binding.rvDamageReportManagement.adapter = adapters
                    }else {
                        adapters.listDamageReport.addAll(it.data.content)
                        adapters.notifyItemRangeChanged(
                            adapters.listDamageReport.size - it.data.content.size,
                            adapters.listDamageReport.size
                        )
                    }
                }

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            // Handle the user's selection
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateFormatString = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val selectedDate = Date(selection)
            val formattedDate = dateFormat.format(selectedDate)
            val formattedDates = dateFormatString.format(selectedDate)

            date = formattedDate
            binding.tvCalenderDamageReport.setText(formattedDates)
//            loadData()
        }

        picker.show(supportFragmentManager, picker.toString())
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }


}