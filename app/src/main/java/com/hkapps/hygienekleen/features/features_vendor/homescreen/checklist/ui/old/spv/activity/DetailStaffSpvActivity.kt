package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.activity

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
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailStaffBertugasSpvBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.dailyAct.DailyActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.dailyAct.DailyActivityResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.adapter.DailyActivitySpvAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.fragment.ImageChecklistDialog
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.activity.ListStaffTlActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.utils.ConnectionTimeoutFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback

class DetailStaffSpvActivity : AppCompatActivity(), NoInternetConnectionCallback, DailyActivitySpvAdapter.DailyActCallBack {

    private lateinit var binding: ActivityDetailStaffBertugasSpvBinding
    private lateinit var adapter: DailyActivitySpvAdapter
    private lateinit var dailyActivityResponseModel: DailyActivityResponseModel
//    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
//    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val projectCode: String = "CFHO"
    private val employeeId: Int = 6744
    private var employeeName: String = ""
    private var employeeNuc: String = ""
    private var pengawasName: String = ""
    private val pengawasNuc: String = "CF12345"
    private var date: String = ""
    private var shift: String = ""
    private var plottingCode: String = ""
    private var location: String = ""
    private var subLocation: String = ""
    private var statusPenilaian: String = ""

    private var dataNoInternet: String = "Internet"

    private val viewModel: ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStaffBertugasSpvBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        binding.rvDailyActChecklist.layoutManager = layoutManager
//
//        // set view appbar
//        binding.appbarPenilaianSpv.tvAppbarTitle.text = "Detail Daily Activity"
//        binding.appbarPenilaianSpv.ivAppbarBack.setOnClickListener {
//            if (dataNoInternet == "noInternet") {
//                val intent = Intent(this, ListStaffTlActivity::class.java)
//                startActivity(intent)
//                finishAffinity()
//            } else {
//                super.onBackPressed()
//                finish()
//            }
//        }
//
//
//        // set view layout
//        binding.linearLayout7.visibility = View.GONE
//        binding.rvDailyActChecklist.visibility = View.GONE
//        binding.shimmerDacCheckSpv.visibility = View.GONE
//        binding.flConnectionTimeoutPenilaianSpv.visibility = View.VISIBLE
//
//        setObserver()
//        loadData()

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
            dataNoInternet = "noInternet"
            return true
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun viewIsOnline() {
        // set appbar layout
        binding.appbarPenilaianSpv.tvAppbarTitle.text = "Detail Daily Activity"
        binding.appbarPenilaianSpv.ivAppbarBack.setOnClickListener {
            if (dataNoInternet == "noInternet") {
                val i = Intent(this, HomeVendorActivity::class.java)
                startActivity(i)
                finishAffinity()
            } else {
                super.onBackPressed()
                finish()
            }
        }

        // set button berikan persetujuan
        binding.btnPersetujuanChecklistSpv.isEnabled = false
        binding.btnPersetujuanChecklistSpv.setOnClickListener {
            // belum ada tampilannya
            // setting disini dialog notifikasi
        }

        // set shimmer effect
        binding.shimmerDetailStaffSpv.startShimmerAnimation()
        binding.shimmerDacCheckSpv.startShimmerAnimation()
        binding.shimmerDetailStaffSpv.visibility = View.VISIBLE
        binding.linearLayout7.visibility = View.GONE
        binding.flConnectionTimeoutPenilaianSpv.visibility = View.GONE
        binding.rvDailyActChecklist.visibility = View.GONE
        binding.btnPersetujuanChecklistSpv.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDailyActChecklist.layoutManager = layoutManager

        setObserver()
        loadData()
    }

    private fun noInternetState() {
        val noInternetState = ConnectionTimeoutFragment.newInstance().also {
            it.setListener(this)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.flConnectionTimeoutPenilaianSpv, noInternetState, "connectionTimeout")
            .commit()
    }

    private fun loadData() {
        viewModel.getDailyAct(employeeId, projectCode)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerDacCheckSpv.stopShimmerAnimation()
                        binding.shimmerDetailStaffSpv.stopShimmerAnimation()

                        binding.shimmerDetailStaffSpv.visibility = View.GONE
                        binding.shimmerDacCheckSpv.visibility = View.GONE

                        binding.flConnectionTimeoutPenilaianSpv.visibility = View.GONE
                        binding.linearLayout7.visibility = View.VISIBLE
                        binding.rvDailyActChecklist.visibility = View.VISIBLE
                        binding.btnPersetujuanChecklistSpv.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.dailyActResponseModel().observe(this, { it ->
            if (it.code == 200) {
                pengawasName = it.data.plotting.employeePengawasName
                employeeName = it.data.employee.employeeName
                employeeNuc = it.data.employee.employeeCode
                date = "Error date"
                shift = it.data.plotting.shiftDescription
                plottingCode = it.data.plotting.codePlottingArea
                location = it.data.plotting.locationName
                subLocation = it.data.plotting.subLocationName

                adapter = DailyActivitySpvAdapter(this, it.data.plotting.dailyActivities as ArrayList<DailyActivity>, viewModel).also { it.setListener(this) }
                binding.rvDailyActChecklist.adapter = adapter

                binding.tvPengawasNamePenilaianSpv.text = "$pengawasName ($pengawasNuc)"
                binding.tvOperationalNamePenilaianSpv.text = "$employeeName ($employeeNuc)"
                binding.tvTanggalPenilaianSpv.text = date
                binding.tvShiftPenilaianSpv.text = shift
                binding.tvPlottingPenilaianSpv.text = plottingCode
                binding.tvAreaPenilaianSpv.text = location
                binding.tvSubAreaPenilaianSpv.text = subLocation

            }
        })

    }

    override fun onBackPressed() {
        if (dataNoInternet == "noInternet") {
            val intent = Intent(this, ListStaffTlActivity::class.java)
            startActivity(intent)
            finishAffinity()
        } else {
            super.onBackPressed()
            finish()
        }
    }

    override fun onRetry() {
        val i = Intent(this, DetailStaffSpvActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onClickedImage(activityId: String) {
        popupImageDialog()
    }

    private fun popupImageDialog() {
        val popupImage = ImageChecklistDialog()
        popupImage.show(supportFragmentManager, "SuccessRegisDialog")
    }

}