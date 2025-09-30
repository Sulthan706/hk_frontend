package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityProfileEmployeeBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.fragment.SubmitRatingFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.HistoryAttendanceOperationalActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeReportViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.ui.activity.DetailLateReportActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEmployeeBinding

    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, 0)

    private var month = 0
    private var year = 0
    private var monthYearText = ""

    private val operationalViewModel : OperationalManagementViewModel by lazy {
        ViewModelProviders.of(this)[OperationalManagementViewModel::class.java]
    }
    private val homeReportViewModel: HomeReportViewModel by lazy {
        ViewModelProviders.of(this)[HomeReportViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,null)

        // set on click button back
        binding.ivBackProfileEmployee.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // first state loading
        binding.shimmerFotoProfile.startShimmerAnimation()
        binding.shimmerJobProfile.startShimmerAnimation()
        binding.shimmerNameProfile.startShimmerAnimation()
        binding.shimmerNucProfile.startShimmerAnimation()
        binding.shimmerProjectName.startShimmerAnimation()
        binding.clShimmerProfileEmployee.visibility = View.VISIBLE
        binding.clProfileEmployee.visibility = View.GONE

        // set current month year text
        val currentTime = Calendar.getInstance().time
        monthYearText = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentTime)
        binding.tvRecentMonthProfileEmployee.text = monthYearText

        // get current month & year
        val calendar: Calendar = Calendar.getInstance()
        month = calendar.get(Calendar.MONTH)+1
        year = calendar.get(Calendar.YEAR)
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        // set on click select month year
        binding.tvRecentMonthProfileEmployee.setOnClickListener {
            val dialog = MonthYearPickerDialog.Builder(
                this,
                R.style.Style_MonthYearPickerDialog_Orange,
                { selectedYear, selectedMonth ->

                    monthYearText = "${monthToText(selectedMonth+1)} $selectedYear"
                    month = selectedMonth+1
                    year = selectedYear

                    binding.tvRecentMonthProfileEmployee.text = monthYearText
                    homeReportViewModel.getReportAttendance(employeeId, selectedMonth+1, selectedYear)
                },
                currentYear,
                currentMonth
            )
                .setNegativeButton("Cancel")
                .setPositiveButton("Oke")
                .setMonthFormat("MMM")
                .build()

            dialog.setTitle("Select month and year")
            dialog.show()
            dialog.getButton(android.app.DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.black2))
            dialog.getButton(android.app.DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.primary_color))
        }

        // set on click detail late report
        binding.tvDetailLateProfileEmployee.setOnClickListener {
            // save selected month year
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_HOME_REPORT, month)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_HOME_REPORT, year)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MONTH_YEAR_HOME_REPORT, monthYearText)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.EMPLOYEE_ID_HOME_REPORT, employeeId)

            startActivity(Intent(this, DetailLateReportActivity::class.java))
        }

        binding.llHistoryProfileEmployee.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, employeeId)
            startActivity(Intent(this, HistoryAttendanceOperationalActivity::class.java))
        }
        binding.llRatingProfileEmployee.setOnClickListener{
            showBottomDialog()
        }

        loadData()
        setObserver()
    }

    private fun showBottomDialog() {
        val myBottomSheet: BottomSheetDialogFragment = SubmitRatingFragment()
        myBottomSheet.show(supportFragmentManager, myBottomSheet.tag)
    }

    private fun monthToText(monthInt: Int): String {
        val monthText = when(monthInt) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "error"
        }
        return monthText
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        operationalViewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerFotoProfile.stopShimmerAnimation()
                        binding.shimmerJobProfile.stopShimmerAnimation()
                        binding.shimmerNameProfile.stopShimmerAnimation()
                        binding.shimmerNucProfile.stopShimmerAnimation()
                        binding.shimmerProjectName.stopShimmerAnimation()
                        binding.clShimmerProfileEmployee.visibility = View.GONE
                        binding.clProfileEmployee.visibility = View.VISIBLE
                    }, 500)
                }
            }
        }
        operationalViewModel.getDetailOperationalResponse().observe(this) {
            if (it.code == 200) {
                binding.tvNameProfileEmployee.text = it.data.employeeName
                binding.tvJobProfileEmployee.text = it.data.jobName
                binding.tvNucProfileEmployee.text = it.data.employeeNuc
                CarefastOperationPref.saveString(CarefastOperationPrefConst.OPERATIONAL_OPS_JOB_CODE, it.data.jobCode)

                //set user image
                val img = it.data.employeePhotoProfile
                val url =
                    this.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

                if (img == "null" || img == null || img == "") {
                    val uri =
                        "@drawable/profile_default" // where myresource (without the extension) is the file
                    val imaResource =
                        this.resources.getIdentifier(uri, null, this.packageName)
                    val res = this.resources.getDrawable(imaResource)
                    this.binding.ivProfileEmployee.setImageDrawable(res)
                } else {
                    val requestOptions = com.bumptech.glide.request.RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true)
                        .error(R.drawable.ic_error_image)

                    Glide.with(this)
                        .load(url)
                        .apply(requestOptions)
                        .into(this.binding.ivProfileEmployee)
                }


            }
        }
        homeReportViewModel.isLoading?.observe(this){ isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
        homeReportViewModel.reportAttendanceModel.observe(this) {
            if (it.code == 200) {
                val totDays = it.data.totalHari ?: "-"
                val totTimeWork = it.data.totalJamKerja ?: "-"
                binding.tvDaysTimeWorkProfileEmployee.text = "Working $totDays days | $totTimeWork hours"

                binding.progressBarProfileEmployee.setProgressPercentage(it.data.persentaseKehadiran)
                val percentProgress = it.data.persentaseKehadiran ?: "0"
                binding.tvPercentageProfileEmployee.text = "${"%.2f".format(percentProgress)}%"

                binding.tvPresentProfileEmployee.text = "${it.data.hadirCount ?: "-"}"
                binding.tvPermissionProfileEmployee.text = "${it.data.izinCount ?: "-"}"
                binding.tvAbsentProfileEmployee.text = "${it.data.alpaCount ?: "-"}"
                binding.tvLateProfileEmployee.text = "${it.data.terlambatCount ?: "-"} times"

                binding.tvTotBackupProfileEmployee.text = "${it.data.lemburGantiCount ?: "0"} days"
                binding.tvTotRequestedProfileEmployee.text = "0 days"
            }
        }
     }

    private fun loadData() {
        operationalViewModel.getDetailOperational(employeeId)
        homeReportViewModel.getReportAttendance(employeeId, month, year)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

}