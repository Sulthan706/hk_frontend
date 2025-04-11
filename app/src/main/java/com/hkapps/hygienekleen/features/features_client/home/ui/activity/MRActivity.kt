package com.hkapps.hygienekleen.features.features_client.home.ui.activity

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMractivity2Binding
import com.hkapps.hygienekleen.features.features_client.home.viewmodel.HomeClientViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.MrAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MRActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMractivity2Binding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private lateinit var mrAdapter: MrAdapter

    private var loadingDialog: Dialog? = null



    private var startAt = "2024-10-17"
    private var endAt = "2024-10-18"
    private var startAtTxt = ""
    private var endAtTxt = ""

    private var page = 0
    private val size = 10
    private var isLastPage = false

    private val homeViewModel by lazy {
        ViewModelProvider(this)[HomeClientViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMractivity2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        getData()
        next()
        previous()
    }

    private fun initView(){
        binding.apply {
            val appBar = "Material Request"
            appbarMr.tvAppbarTitle.text = appBar
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this@MRActivity, R.color.secondary_color)
            binding.appbarMr.llAppbar.setBackgroundResource(R.color.secondary_color)
            appbarMr.ivAppbarBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            onBackPressedDispatcher.addCallback(onBackPressedCallback)

            val dateFormatFirst = SimpleDateFormat("yyyy-MM-dd")
            val dateTxtFormatFirst = SimpleDateFormat("dd MMM yyyy")
            val calFirst = Calendar.getInstance()
            calFirst.set(Calendar.DAY_OF_MONTH, 1)
            startAt = dateFormatFirst.format(calFirst.time)
            startAtTxt = dateTxtFormatFirst.format(calFirst.time)

            val currentDate = dateFormatFirst.format(Date())
            val currentDateTxt = dateTxtFormatFirst.format(Date())
            val dateFormatYesterday = SimpleDateFormat("yyyy-MM-dd")
            val dateTxtFormatYesterday = SimpleDateFormat("dd MMM yyyy")
            val calYesterday = Calendar.getInstance()
            calYesterday.add(Calendar.DATE, -1)
            endAt = if (currentDate == startAt) {
                startAt
            } else {
                dateFormatYesterday.format(calYesterday.time)
            }
            endAtTxt = if (currentDateTxt == startAtTxt) {
                startAtTxt
            } else {
                dateTxtFormatYesterday.format(calYesterday.time)
            }

            binding.tvPeriodAbsenceReportManagement.text = "$startAtTxt - $endAtTxt"

            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                ).build()

            binding.rlPeriodAbsenceReportManagement.setOnClickListener {
                picker.show(supportFragmentManager, "rangeDatePickerTag")
                picker.addOnPositiveButtonClickListener { selection ->
                    val calendarFirst = Calendar.getInstance()
                    calendarFirst.timeInMillis = selection.first!!

                    val calendarSecond = Calendar.getInstance()
                    calendarSecond.timeInMillis = selection.second!!

                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    startAt = sdf.format(calendarFirst.time)
                    endAt = sdf.format(calendarSecond.time)

                    page = 0
                    isLastPage = false
                    showLoading(getString(R.string.loading_string_progress))
                    getData()

                    val firstDate = android.text.format.DateFormat.format("dd MMM yyyy", calendarFirst) as String
                    val secondDate = android.text.format.DateFormat.format("dd MMM yyyy", calendarSecond) as String
                    val selectedRangeDate = "$firstDate - $secondDate"

                    binding.tvPeriodAbsenceReportManagement.text = if (firstDate == secondDate) {
                        firstDate
                    } else {
                        selectedRangeDate
                    }
                }
            }
        }
    }

    private fun setLoadingFirst(){
        binding.apply {
            progressBar.visibility = View.GONE
            ivNextAbsenceReportManagement.visibility = View.VISIBLE
            ivPrevAbsenceReportManagement.visibility = View.VISIBLE
            rvTableHistoryClosing.visibility = View.VISIBLE
            tvPageHistoryClosing.visibility = View.VISIBLE
        }
    }

    private fun getData(){
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        homeViewModel.getDataMr(CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE,""),currentMonth,currentYear,page,size)
        homeViewModel.getDataMr.observe(this){
            if(it.code == 200){
                hideLoading()
                if(it.data.content.isNotEmpty()){
                    setLoadingFirst()
                    binding.tableHistoryClosing.visibility = View.VISIBLE
                    binding.rvTableHistoryClosing.visibility = View.VISIBLE
                    binding.ivNextAbsenceReportManagement.visibility = View.VISIBLE
                    binding.ivPrevAbsenceReportManagement.visibility = View.VISIBLE
                    binding.tvPageHistoryClosing.visibility = View.VISIBLE
                    binding.tvEmpty.visibility = View.GONE
                    val pageStart = it.data.pageable.offset + 1
                    val pageEnd = it.data.pageable.offset + it.data.numberOfElements

                    val count = "Showing $pageStart-$pageEnd of ${it.data.size}"
                    binding.tvPageHistoryClosing.text = count
                    if (page == 0) {
                        mrAdapter = MrAdapter(it.data.content.toMutableList())
                        binding.rvTableHistoryClosing.adapter = mrAdapter
                        binding.rvTableHistoryClosing.layoutManager = LinearLayoutManager(this)
                    } else {
                        mrAdapter.data.clear()
                        for (i in 0 until it.data.content.size) {
                            mrAdapter.data.add(it.data.content[i])
                        }
                        mrAdapter.notifyDataSetChanged()
                    }
                }else{
                    binding.progressBar.visibility = View.GONE
                    binding.tableHistoryClosing.visibility = View.GONE
                    binding.rvTableHistoryClosing.visibility = View.GONE
                    binding.ivNextAbsenceReportManagement.visibility = View.GONE
                    binding.ivPrevAbsenceReportManagement.visibility = View.GONE
                    binding.tvPageHistoryClosing.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }else{
                hideLoading()
                Toast.makeText(this, "Gagal mengambil data history closing", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun next(){
        binding.ivNextAbsenceReportManagement.setOnClickListener {
            loadingDialog = CommonUtils.showLoadingDialog(this, getString(R.string.loading_string_progress))
            Handler(Looper.getMainLooper()).postDelayed( {
                if (!isLastPage) {
                    page++
                    getData()
                } else {
                    Toast.makeText(this, "Last Page", Toast.LENGTH_SHORT).show()
                }
            }, 500)
        }
    }

    private fun previous(){
        binding.ivPrevAbsenceReportManagement.setOnClickListener {
            loadingDialog = CommonUtils.showLoadingDialog(this, getString(R.string.loading_string_progress))
            Handler(Looper.getMainLooper()).postDelayed( {
                if (page != 0 ) {
                    page--
                    getData()
                } else {
                    hideLoading()
                    Toast.makeText(this, "First Page", Toast.LENGTH_SHORT).show()
                }
            }, 500)
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
//        loadData()
    }
}