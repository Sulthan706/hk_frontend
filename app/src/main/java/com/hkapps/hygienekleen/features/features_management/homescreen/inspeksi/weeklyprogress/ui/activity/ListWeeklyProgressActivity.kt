package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.ui.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListWeeklyProgressBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.listweeklyresponse.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.ui.adapter.WeeklyProgressAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.viewmodel.WeeklyProgressViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ListWeeklyProgressActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListWeeklyProgressBinding

    private var filter = "All"

    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_WEEKLY, "")

    private var projectLastVisit = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")

    private var projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_WEEKLY, "")

    private var projectNameLastVisit = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")

    private var page = 0

    private var isLastPage = false

    private var size = 10

    private var startDate : String = ""

    private var endDate : String = ""

    private lateinit var weeklyProgressAdapter : WeeklyProgressAdapter

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val weeklyProgressViewModel by lazy {
        ViewModelProviders.of(this)[WeeklyProgressViewModel::class.java]
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListWeeklyProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)
        initView()
    }

    private fun initView(){

        val appBar = "Daily Progress"
        binding.appbarFormRoutineReport.tvAppbarTitle.text = appBar
        binding.appbarFormRoutineReport.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                recreate()
            }
        }

        binding.shimmerLayout.startShimmerAnimation()
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.tvVisitation.text = projectName.ifBlank { projectNameLastVisit }


        spanText()
        setDropDown()
        getDateRangeFromStartOfMonth()
        Handler(Looper.getMainLooper()).postDelayed({
            loadData(startDate, endDate)
            getDataWeeklyProgress()
        }, 1500)
        setRecycler()
    }

    private fun spanText(){
        val message = resources.getString(R.string.text_empty_list_weekly_progress)
        val spannableString = SpannableString(message)
        val startIndex = message.indexOf("Mulai Aktivitas di Area")
        val endIndex = startIndex + "Mulai Aktivitas di Area".length

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvEmpty.text = spannableString
        binding.tvDateListRoutineVisited.setOnClickListener {
            binding.tvDateListRoutineVisited.isEnabled = false
            datePickerDialog()
        }
    }

    private fun datePickerDialog() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select a date range")

        val datePicker = builder.build()
        datePicker.addOnPositiveButtonClickListener { selection ->

            val startDate = selection?.first
            val endDate = selection?.second

            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val simpleDateFormatResult = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateStr = startDate?.let { simpleDateFormat.format(Date(it)) }
            val endDateStr = endDate?.let { simpleDateFormat.format(Date(it)) }
            val startDateStrResult = startDate?.let { simpleDateFormatResult.format(Date(it)) }
            val endDateStrResult = endDate?.let { simpleDateFormatResult.format(Date(it)) }

            val selectedDateRange = "$startDateStr - $endDateStr"

            page = 0
            this.startDate = startDateStrResult.toString()
            this.endDate = endDateStrResult.toString()
            binding.tvDateListRoutineVisited.text = selectedDateRange
            loadData(startDateStrResult.toString(),endDateStrResult.toString())
            getDataWeeklyProgress()
        }

        datePicker.show(supportFragmentManager, "DATE_PICKER")
        binding.tvDateListRoutineVisited.isEnabled = true
    }

    private fun setDropDown(){
        val filterObject = resources.getStringArray(R.array.filter_weekly_progress)
        val adapterFilter = ArrayAdapter(this, R.layout.spinner_item, filterObject)
        adapterFilter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerReasonChangeScheduleManagement.adapter = adapterFilter
        binding.spinnerReasonChangeScheduleManagement.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                filter = if(filterObject[position] == "Semua"){
                    "All"
                }else{
                    filterObject[position]
                }
                page = 0
                loadData(startDate,endDate)
                getDataWeeklyProgress()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun loadData(startDate: String,endDate : String){ weeklyProgressViewModel.getListWeeklyProgress(
        projectCode.ifBlank { projectLastVisit },userId,startDate,endDate,filter,page,size) }

    private fun setRecycler(){
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListWeeklyProgress.layoutManager = layoutManager
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(startDate,endDate)
                    getDataWeeklyProgress()
                }
            }
        }
        binding.rvListWeeklyProgress.addOnScrollListener(scrollListener)
    }

    private fun getDataWeeklyProgress() {
        weeklyProgressViewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    binding.shimmerLayout.stopShimmerAnimation()
                    binding.shimmerLayout.visibility = View.GONE
                }
            }
        }

        weeklyProgressViewModel.listWeeklyProgressResponse.observe(this) { it ->
            if (it != null && it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.rvListWeeklyProgress.visibility = View.VISIBLE
                    binding.tvEmpty.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        weeklyProgressAdapter = WeeklyProgressAdapter(
                            userName,
                            it.data.content.distinct().toMutableList(),
                            object : WeeklyProgressAdapter.OnClickWeeklyProgressAdapter {
                                override fun onDetail(data: Content) {
                                    val i = Intent(this@ListWeeklyProgressActivity,DetailWeeklyProgressActivity::class.java).also{
                                        it.putExtra("id_weekly",data.idWeekly)
                                    }
                                    resultLauncher.launch(i)
                                }
                            })
                        binding.rvListWeeklyProgress.adapter = weeklyProgressAdapter
                    } else {
                        val newFilteredContent = it.data.content.distinct().filter { newItem ->
                            weeklyProgressAdapter.data.none { existingItem -> existingItem.idWeekly == newItem.idWeekly }
                        }
                        weeklyProgressAdapter.data.addAll(newFilteredContent)
                        weeklyProgressAdapter.notifyItemRangeInserted(
                            weeklyProgressAdapter.data.size - newFilteredContent.size,
                            newFilteredContent.size
                        )
                    }
                } else {
                    binding.rvListWeeklyProgress.adapter = null
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvListWeeklyProgress.visibility = View.GONE
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data weekly progress", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDateRangeFromStartOfMonth(): String {
        val today = Calendar.getInstance()
        val startOfMonth = Calendar.getInstance()
        startOfMonth.set(Calendar.DAY_OF_MONTH, 1)

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        startDate = formatter.format(startOfMonth.time)
        endDate = formatter.format(today.time)

        val date = "${simpleDateFormat.format(startOfMonth.time)} - ${simpleDateFormat.format(today.time)}"
        binding.tvDateListRoutineVisited.text = date

        return "${formatter.format(startOfMonth.time)} - ${formatter.format(today.time)}"
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}