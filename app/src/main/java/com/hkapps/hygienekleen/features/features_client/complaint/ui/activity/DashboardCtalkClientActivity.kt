package com.hkapps.hygienekleen.features.features_client.complaint.ui.activity

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDashboardCtalkClientBinding
import com.hkapps.hygienekleen.features.features_client.complaint.viewmodel.ClientComplaintViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.datepicker.MaterialDatePicker
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import ir.mahozad.android.PieChart
import java.lang.Float
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class DashboardCtalkClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardCtalkClientBinding
    private val viewModel: ClientComplaintViewModel by lazy {
        ViewModelProviders.of(this).get(ClientComplaintViewModel::class.java)
    }
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private var beginDate: String = ""
    private var endDate: String = ""
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardCtalkClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this,R.color.secondary_color)


//        val dateFormatString = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
//
//        val formattedDates = dateFormatString.format(beginDate)

        binding.tvCalenderCtalkClient.setText(getDateNow())
        beginDate = getDateNowApi()

        val fabColorNormal =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.fabBackButton.backgroundTintList = fabColorNormal
        binding.fabBackButton.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        binding.btnDetailCtalk.setOnClickListener {
            startActivity(Intent(this, HistoryComplaintClientActivity::class.java))
        }

        binding.tvCalenderCtalkClient.setOnClickListener {
            showDateRangePicker()
        }

        binding.swipeCtalkClient.setOnRefreshListener {
            loadData()
            startActivity(Intent(this, DashboardCtalkClientActivity::class.java))
            finish()
            binding.swipeCtalkClient.isRefreshing = false
        }

        loadData()
        setObserver()
        showLoading(getString(R.string.loading_string2))
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun getDateNow(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return formatDate(day, month, year)
    }

    private fun formatDate(day: Int, month: Int, year: Int): String {
        val locale = Locale("id", "ID")
        val monthFormat = SimpleDateFormat("MMMM", locale)
        val monthName = monthFormat.format(Date(year - 1900, month, day))

        val formattedDay = day.toString().padStart(2, '0')
        val formattedYear = year.toString()

        return "$formattedDay $monthName $formattedYear"
    }

    private fun showDateRangePicker() {
        val builder =
            MaterialDatePicker.Builder.dateRangePicker() // Use dateRangePicker() for selecting a date range
                .setTitleText("Select Date Range")

        val picker = builder.build()
        picker.addOnPositiveButtonClickListener { selection ->
            val startDateMillis = selection.first ?: return@addOnPositiveButtonClickListener
            val endDateMillis = selection.second ?: return@addOnPositiveButtonClickListener

            val formattedDateRange = formatDateForUI(startDateMillis, endDateMillis)

            // Update ui
            binding.tvCalenderCtalkClient.setText(formattedDateRange)

            val startDateApi = formatDateForApi(startDateMillis)
            val endDateApi = formatDateForApi(endDateMillis)

            // for list rkb
            beginDate = startDateApi
            endDate = endDateApi
            viewModel.getDashboardCtalkClient(projectCode, beginDate, endDate)
            showLoading(getString(R.string.loading_string))
        }

        picker.show(supportFragmentManager, picker.toString())
    }

    private fun getDateNowApi(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Adding 1 because months are zero-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        beginDate = formatDateNowApi(year, month, day)
        return formatDateNowApi(year, month, day)
    }
    private fun formatDateNowApi(year: Int, month: Int, day: Int): String {
        return "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
    }
    private fun formatDateForUI(startDateInMillis: Long, endDateInMillis: Long): String {
        val startCalendar = Calendar.getInstance()
        startCalendar.timeInMillis = startDateInMillis

        val endCalendar = Calendar.getInstance()
        endCalendar.timeInMillis = endDateInMillis

        val startDay = startCalendar.get(Calendar.DAY_OF_MONTH)
        val endDay = endCalendar.get(Calendar.DAY_OF_MONTH)

        val monthFormat = SimpleDateFormat("MMMM", Locale("id", "ID"))
        val startMonth = monthFormat.format(startCalendar.time)


        val year = startCalendar.get(Calendar.YEAR)
        return if (startDay == endDay) {
            "$startDay $startMonth $year"
        } else {
            "$startDay - $endDay $startMonth $year"
        }
    }
    private fun formatDateForApi(dateInMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateInMillis

        val year = calendar.get(Calendar.YEAR)
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
            .padStart(2, '0') // Adding 1 because months are zero-based
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')

        return "$year-$month-$day"
    }


    private fun loadData() {
        viewModel.getDashboardCtalkClient(projectCode, beginDate, beginDate)
    }

    private fun setObserver() {
        viewModel.getDashboardCtalkClientViewModel().observe(this){
            if (it.code == 200){


                val totalRemaining = it.data.total.toFloat()

                val waitingSliceValue = (it.data.menunggu.toFloat() / totalRemaining) * 100f
                val onprogressSliceValue = (it.data.dikerjakan.toFloat() / totalRemaining) * 100f
                val finishSliceValue = (it.data.selesai.toFloat() / totalRemaining) * 100f
                val closeSliceValues = (it.data.tutup.toFloat() / totalRemaining) * 100f


                val roundSliceWaiting = if (Float.isNaN(waitingSliceValue)) 0f else waitingSliceValue.roundToInt() / 100f
                val roundSliceOnprogress = if (Float.isNaN(onprogressSliceValue)) 0f else onprogressSliceValue.roundToInt() / 100f
                val roundSliceFinish = if (Float.isNaN(finishSliceValue)) 0f else finishSliceValue.roundToInt() / 100f
                val roundSliceClose = if (Float.isNaN(closeSliceValues)) 0f else closeSliceValues.roundToInt() / 100f


                val totalRealization = it.data.total.toFloat()
                val summaryNotRealization = 100f
                val resultSumarryNotRealization = summaryNotRealization - totalRealization
                val countNotRealization = if (Float.isNaN(resultSumarryNotRealization)) 0f else resultSumarryNotRealization.roundToInt() / 100f


                binding.tvTotalPercentage.text = "${it.data.total}%"
                val pieChart = binding.pieChart
                pieChart.isAnimationEnabled = true
                pieChart.isLegendEnabled = false
                pieChart.holeRatio = 0.50f
//                pieChart.centerLabel = "${it.data.realizationInPercent}%"
                pieChart.centerLabelColor = Color.BLACK
                pieChart.isCenterLabelEnabled = false
                val slices = mutableListOf<PieChart.Slice>()

                if (roundSliceWaiting > 0) {
                    slices.add(PieChart.Slice(roundSliceWaiting, Color.parseColor("#FF5656")))
                }

                if (roundSliceOnprogress > 0) {
                    slices.add(PieChart.Slice(roundSliceOnprogress, Color.parseColor("#F47721")))
                }

                if (roundSliceFinish > 0) {
                    slices.add(PieChart.Slice(roundSliceFinish, Color.parseColor("#00BD8C")))
                }

                if (roundSliceClose > 0) {
                    slices.add(PieChart.Slice(roundSliceFinish, Color.BLACK))
                }

                if (countNotRealization > 0) {
                    slices.add(PieChart.Slice(countNotRealization, Color.LTGRAY))
                }

                pieChart.slices = slices

                binding.tvTotalCtalkClient.text = (it?.data?.total ?: 0).toString()
                binding.tvWaitingCtalkClient.text = (it?.data?.menunggu ?: 0).toString()
                binding.tvOnprogressCtalkClient.text = (it?.data?.dikerjakan ?: 0).toString()
                binding.tvFinishCtalkClient.text = (it?.data?.selesai ?: 0).toString()
                binding.tvCloseCtalkClient.text = (it?.data?.tutup ?: 0).toString()

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }
    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }
}