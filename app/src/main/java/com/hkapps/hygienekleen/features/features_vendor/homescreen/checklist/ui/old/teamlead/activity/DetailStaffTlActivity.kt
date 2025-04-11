package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailStaffTlBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.dailyAct.DailyActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.dailyAct.DailyActivityResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.adapter.DailyActivityTlAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.ConnectionTimeoutFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback

class DetailStaffTlActivity : AppCompatActivity(), NoInternetConnectionCallback, DailyActivityTlAdapter.DailyActCallBack {

    private lateinit var binding: ActivityDetailStaffTlBinding
    private lateinit var tlAdapter: DailyActivityTlAdapter
    private lateinit var dacResponseModel: DailyActivityResponseModel
//    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
//    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.EMPLOYEE_ON_CHECKLIST, 0)
    private val projectCode: String = "CFHO"
    private var employeeId: Int = 6744
    private var dataNoInternet: String = "Internet"

    private var staffId: Int = 0
    private var plottingId: Int = 0
    private var employeeName: String = ""
    private var employeeJob: String = ""
    private var kodePlotting: String = ""
    private var location: String = ""
    private var subLoc: String = ""
    private var shift: String = ""
    private var statusPenilaian: String = ""
    private var statusDac: String = ""

    private val viewModel: ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStaffTlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get employee id
//        employeeId = intent.getIntExtra("employeeId", 0)

//        // set appbar layout
//        binding.appbarDetailStaff.tvAppbarTitle.text = "Detail Daily Activity"
//        binding.appbarDetailStaff.ivAppbarBack.setOnClickListener {
//            if (dataNoInternet == "noInternet") {
//                val i = Intent(this, ChecklistTeamleadActivity::class.java)
//                startActivity(i)
//                finishAffinity()
//            } else {
//                super.onBackPressed()
//                finish()
//            }
//        }
//
//        // set shimmer effect
//        binding.shimmerDetailStaffTl.startShimmerAnimation()
//        binding.shimmerDetailStaffTl.visibility = View.VISIBLE
//        binding.ivStaffImgDetailTl.visibility = View.GONE
//        binding.llDetailStaffTl1.visibility = View.GONE
//        binding.llDetailStaffTl2.visibility = View.GONE
//        binding.flChecklistDetailTl.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    viewIsOnline()
                    return true
                }
            }
        } else {
            noInternetState()
            binding.flChecklistDetailTl.visibility = View.VISIBLE
            dataNoInternet = "noInternet"
            return true
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun viewIsOnline() {
        // set appbar layout
        binding.appbarDetailStaff.tvAppbarTitle.text = "Detail Daily Activity"
        binding.appbarDetailStaff.ivAppbarBack.setOnClickListener {
            if (dataNoInternet == "noInternet") {
                val i = Intent(this, HomeVendorActivity::class.java)
                startActivity(i)
                finishAffinity()
            } else {
                super.onBackPressed()
                finish()
            }
        }

        // set button kirim penilaian dac
        binding.btnKirimDacChecklist.isEnabled = false
        binding.btnKirimDacChecklist.setOnClickListener {
            // belum ada tampilannya
            // setting disini dialognya notifikasi
        }

        // set shimmer effect
        binding.shimmerDetailStaffTl.startShimmerAnimation()
        binding.shimmerDacDetailStaffTl.startShimmerAnimation()
        binding.shimmerDetailStaffTl.visibility = View.VISIBLE
        binding.ivStaffImgDetailTl.visibility = View.GONE
        binding.llDetailStaffTl1.visibility = View.GONE
        binding.llDetailStaffTl2.visibility = View.GONE
        binding.flChecklistDetailTl.visibility = View.GONE
        binding.btnKirimDacChecklist.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDacDetailStaffTl.layoutManager = layoutManager

        setObserver()
        loadData()
    }

    private fun loadData() {
        viewModel.getDailyAct(employeeId, projectCode)
        viewModel.getAttendanceStatus(employeeId, projectCode)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerDetailStaffTl.stopShimmerAnimation()
                        binding.shimmerDacDetailStaffTl.stopShimmerAnimation()

                        binding.shimmerDetailStaffTl.visibility = View.GONE
                        binding.shimmerDacDetailStaffTl.visibility = View.GONE

                        binding.rvDacDetailStaffTl.visibility = View.VISIBLE
                        binding.btnKirimDacChecklist.visibility = View.VISIBLE
                        binding.ivStaffImgDetailTl.visibility = View.VISIBLE
                        binding.llDetailStaffTl1.visibility = View.VISIBLE
                        binding.llDetailStaffTl2.visibility = View.VISIBLE
                        binding.flChecklistDetailTl.visibility = View.GONE
                    }, 1500)
                }
            }
        })
        viewModel.dailyActResponseModel().observe(this, {
            if (it.code == 200) {
                dacResponseModel = it

                employeeName = it.data.employee.employeeName
                employeeJob = it.data.employee.jobName
                kodePlotting = it.data.plotting.codePlottingArea
                location = it.data.plotting.locationName
                subLoc = it.data.plotting.subLocationName
                shift = it.data.plotting.shiftDescription
                plottingId = it.data.plotting.plottingId
                staffId = it.data.employee.employeeId

                // save id plotting
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.PLOTTING_ON_CHECKLIST, plottingId)

                // set detail staff data
                binding.tvStaffNameDetailTl.text = employeeName
                binding.tvStaffPositionDetailTl.text = employeeJob
                binding.tvPlottingDetailTl.text = kodePlotting
                binding.tvAreaDetailTl.text = location
                binding.tvSubAreaDetailTl.text = subLoc
                binding.tvShiftDetailTl.text = shift

                // set rv adapter
                tlAdapter = DailyActivityTlAdapter(
                    this,
                    it.data.plotting.dailyActivities as ArrayList<DailyActivity>, viewModel
                ).also { it.setListener(this) }
                binding.rvDacDetailStaffTl.adapter = tlAdapter
            }
        })
        viewModel.attendanceStatusResponse().observe(this, {
            if (it.code == 200) {
                statusPenilaian = it.data.statusAttendance
                binding.tvStatusPenilaianDetailTl.text = statusPenilaian

                val image = it.data.attendanceInfo.employeeImgSelfieIn
                val url = getString(R.string.url) +"assets.admin_master/images/attendance_photo_selfie/$image"
                this.let {
                    Glide.with(it)
                        .load(url)
                        .apply(RequestOptions.centerCropTransform())
                        .into(binding.ivStaffImgDetailTl)
                }
            }
        })
    }

    private fun noInternetState() {
        val noInternetState = ConnectionTimeoutFragment.newInstance().also {
            it.setListener(this)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_checklistDetailTl, noInternetState, "connectionTimeout")
            .commit()
    }

    override fun onBackPressed() {
        if (dataNoInternet == "noInternet") {
            val i = Intent(this, ChecklistTeamleadActivity::class.java)
            startActivity(i)
            finishAffinity()
        } else {
            super.onBackPressed()
            finish()
        }
    }

    override fun onRetry() {
        val i = Intent(this, DetailStaffTlActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onClickedDac(activityId: Int) {
        val i = Intent(this, PenilaianKondisiObjectActivity::class.java)
        i.putExtra("activityId", activityId)
//        i.putExtra("staffId", staffId)
//        i.putExtra("plottingId", plottingId)
        startActivity(i)
    }


}