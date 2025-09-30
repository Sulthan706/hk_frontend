package com.hkapps.hygienekleen.features.features_client.complaint.ui.activity.visitor

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDashboardComplaintVisitorClientBinding
import com.hkapps.hygienekleen.features.features_client.complaint.model.dashboardctalkvisitorclient.Area
import com.hkapps.hygienekleen.features.features_client.complaint.model.dashboardctalkvisitorclient.Object
import com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter.visitor.AreaClientVisitorAdapter
import com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter.visitor.ObjectClientVisitorAdapter
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

class DashboardComplaintVisitorClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardComplaintVisitorClientBinding
    private val viewModel: ClientComplaintViewModel by lazy {
        ViewModelProviders.of(this).get(ClientComplaintViewModel::class.java)
    }
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private var beginDate: String = ""
    private var endDate: String = ""
    private var loadingDialog: Dialog? = null
    private lateinit var adapterArea: AreaClientVisitorAdapter
    private lateinit var objectArea: ObjectClientVisitorAdapter

    private var originalList = ArrayList<Area>()
    private var expanded = false
    private var expandeds = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardComplaintVisitorClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this,R.color.secondary_color)


        val fabColorNormal =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.fabBackButton.backgroundTintList = fabColorNormal
        binding.fabBackButton.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        //setup rv
        val layoutManagers =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAreaCtalkVisitor.layoutManager = layoutManagers

        //setup rv
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvObjectCtalkVisitor.layoutManager = layoutManager

        binding.btnDetailCtalkVisitor.setOnClickListener {
            startActivity(Intent(this, ListComplaintVisitorClientActivity::class.java))
        }

        binding.tvCalenderCtalkVisitor.setOnClickListener {
            showDateRangePicker()
        }

        binding.swipeCtalkVisitorClient.setOnRefreshListener {
            loadData()
            startActivity(Intent(this, DashboardComplaintVisitorClientActivity::class.java))
            finish()
            binding.swipeCtalkVisitorClient.isRefreshing = false
        }







        binding.tvCalenderCtalkVisitor.setText(getDateNow())
        beginDate = getDateNowApi()
        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        showLoading(getString(R.string.loading_string2))
    }

    private fun updateButtonState() {
        val buttonText = if (expanded) "Show Less" else "Show More"
        binding.btnShowMoreArea.text = buttonText
    }
    private fun updateButtonStates() {
        val buttonText = if (expandeds) "Show Less" else "Show More"
        binding.btnShowMoreObject.text = buttonText
    }


    private fun loadData() {
        viewModel.getDashboardCtalkVisitorClient(projectCode, beginDate, beginDate)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getDasboardCtalkVisitorClientViewModel().observe(this){
            if (it.code == 200){

                val totalRemaining = it.data.total.toFloat()

                val waitingSliceValue = (it.data.menunggu.toFloat() / totalRemaining) * 100f
                val onprogressSliceValue = (it.data.dikerjakan.toFloat() / totalRemaining) * 100f
                val finishSliceValue = (it.data.tutup.toFloat() / totalRemaining) * 100f


                val roundSliceWaiting = if (Float.isNaN(waitingSliceValue)) 0f else waitingSliceValue.roundToInt() / 100f
                val roundSliceOnprogress = if (Float.isNaN(onprogressSliceValue)) 0f else onprogressSliceValue.roundToInt() / 100f
                val roundSliceFinish = if (Float.isNaN(finishSliceValue)) 0f else finishSliceValue.roundToInt() / 100f


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

                if (countNotRealization > 0) {
                    slices.add(PieChart.Slice(countNotRealization, Color.LTGRAY))
                }

                pieChart.slices = slices

                binding.tvTotalCtalk.text = (it?.data?.total ?: "-").toString()

                binding.tvWaitingCtalk.text = (it?.data?.menunggu ?: "-").toString()
                binding.tvOnprogressCtalk.text = (it?.data?.dikerjakan ?: "-").toString()
                binding.tvFinishCtalk.text = (it?.data?.tutup ?: "-").toString()

                //adapter
                if (it.data.area.isEmpty()){
                    binding.llEmtpyAreaCtalkVisitor.visibility = View.VISIBLE
                } else {
                    binding.llEmtpyAreaCtalkVisitor.visibility = View.GONE
                    binding.rvAreaCtalkVisitor.visibility = View.VISIBLE

                    val areaList = it.data.area as ArrayList<Area>
                    adapterArea = AreaClientVisitorAdapter(areaList)

                    if (areaList.size <= 3 ) {
                        binding.btnShowMoreArea.visibility = View.GONE
                    } else {
                        binding.btnShowMoreArea.visibility = View.VISIBLE
                        binding.btnShowMoreArea.setOnClickListener {
                            expanded = !expanded
                            adapterArea.toggleExpansion()
                            updateButtonState()
                            Log.d("agri", "btn show $expanded")
                        }
                    }
                    binding.rvAreaCtalkVisitor.adapter = adapterArea
                }


                if (it.data.objectArea.isEmpty()) {
                    binding.llEmtpyObjectCtalkVisitor.visibility = View.VISIBLE
                } else {
                    binding.llEmtpyObjectCtalkVisitor.visibility = View.GONE
                    binding.rvObjectCtalkVisitor.visibility = View.VISIBLE

                    val objectList = it.data.objectArea as ArrayList<Object>
                    objectArea = ObjectClientVisitorAdapter(objectList)

                    if (objectList.size <= 3) {
                        // Hide the "Show More" button if the number of objects is 3 or less
                        binding.btnShowMoreObject.visibility = View.GONE
                    } else {
                        // Show the "Show More" button if the number of objects is more than 3
                        binding.btnShowMoreObject.visibility = View.VISIBLE
                        binding.btnShowMoreObject.setOnClickListener {
                            expandeds = !expandeds
                            objectArea.toggleExpansion()
                            updateButtonStates()
                            Log.d("agri", "btn show $expandeds")
                        }
                    }
                    binding.rvObjectCtalkVisitor.adapter = objectArea
                }





            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
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

            if (startDateMillis > endDateMillis) {
                // Show error message or handle the case where the start date is after the end date
                Toast.makeText(
                    this@DashboardComplaintVisitorClientActivity,
                    "Please select a valid date range",
                    Toast.LENGTH_SHORT
                ).show()
                return@addOnPositiveButtonClickListener
            }

            val formattedDateRange = formatDateForUI(startDateMillis, endDateMillis)
            binding.tvCalenderCtalkVisitor.setText(formattedDateRange)

            val startDateApi = formatDateForApi(startDateMillis)
            val endDateApi = formatDateForApi(endDateMillis)


            beginDate = startDateApi
            endDate = endDateApi
            viewModel.getDashboardCtalkVisitorClient(projectCode, beginDate, endDate)
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
        val endMonth = monthFormat.format(endCalendar.time)



        val year = startCalendar.get(Calendar.YEAR)
        return if (startDay == endDay) {
            "$startDay $startMonth $year"
        } else {
            "$startDay $startMonth - $endDay $endMonth $year"
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