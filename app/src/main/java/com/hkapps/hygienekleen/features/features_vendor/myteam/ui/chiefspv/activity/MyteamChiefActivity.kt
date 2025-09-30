package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.chiefspv.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMyteamChiefBinding
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listSpvModel.SupervisorData
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.chiefspv.adapter.ListSupervisorAdapter
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.activity.MyteamSpvActivity
import com.hkapps.hygienekleen.features.features_vendor.myteam.viewmodel.ShiftTimkuViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyteamChiefActivity : AppCompatActivity(), ListSupervisorAdapter.ListSpvCallback {

    private lateinit var binding: ActivityMyteamChiefBinding
    private lateinit var adapter: ListSupervisorAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val projectChief = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
    private val chiefId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MY_TEAM_CHIEF_ID, 0)
    private val chiefName = CarefastOperationPref.loadString(CarefastOperationPrefConst.MY_TEAM_CHIEF_NAME, "")
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private var dateParams: String = ""
    private var dateText: String = "Pilih Tanggal"
    private var dtDate: String = ""
    private var employeeId: Int = 0
    private var projectCode: String = ""

    private val viewModel: ShiftTimkuViewModel by lazy {
        ViewModelProviders.of(this).get(ShiftTimkuViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyteamChiefBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        employeeId = when(clickFrom) {
            "Myteam Management" -> chiefId
            else -> userId
        }
        projectCode = when(clickFrom) {
            "Myteam Management" -> projectChief
            else -> projectId
        }

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
            return true
        }
        return false
    }

    private fun noInternetState() {
        binding.shimmerMyTeamChief.visibility = View.GONE
        binding.rvMyTeamChief.visibility = View.GONE
        binding.flNoInternetMyTeamChief.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, MyteamChiefActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun viewIsOnline() {
        // set appbar
        binding.appbarMyTeamChief.tvAppbarTitle.text = when(clickFrom) {
            "Myteam Management" -> chiefName
            else -> "Tim Ku"
        }
        binding.appbarMyTeamChief.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
            dtDate = ""

            CarefastOperationPref.saveInt(CarefastOperationPrefConst.MY_TEAM_CHIEF_ID, 0)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MY_TEAM_CHIEF_NAME, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
        }
        binding.appbarMyTeamChief.ivAppbarDate.visibility = View.GONE
//        binding.appbarMyTeamChief.ivAppbarDate.setOnClickListener {
//            showBottomSheetDialog()
//        }

        // set shimmer effect
        binding.shimmerMyTeamChief.startShimmerAnimation()
        binding.shimmerMyTeamChief.visibility = View.VISIBLE
        binding.rvMyTeamChief.visibility = View.GONE
        binding.flNoInternetMyTeamChief.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvMyTeamChief.layoutManager = layoutManager

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerMyTeamChief.stopShimmerAnimation()
                        binding.shimmerMyTeamChief.visibility = View.GONE
                        binding.rvMyTeamChief.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getListSpvResponseModel().observe(this) {
            if (it.code == 200) {
                adapter = ListSupervisorAdapter(this, it.data as ArrayList<SupervisorData>, viewModel, this, projectCode).also { it.setListener(this) }
                binding.rvMyTeamChief.adapter = adapter
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListSupervisor(projectCode)
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(this)

        dialog.setContentView(R.layout.layout_bottom_sheets_myteam)
        val tvChooseDate = dialog.findViewById<TextView>(R.id.tv_choose_date_myteam)
        val btnApplied = dialog.findViewById<AppCompatButton>(R.id.btn_applied_myteam)
        val ivClose = dialog.findViewById<ImageView>(R.id.iv_close_date_myteam)

        tvChooseDate?.text = dateText

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }

        // set calendar choose date
        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy"
            val paramsFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val dateParam = SimpleDateFormat(paramsFormat, Locale.US)

            dateParams = dateParam.format(cal.time)

            dateText = sdf.format(cal.time)
            tvChooseDate?.text = dateText
            btnApplied?.setBackgroundResource(R.drawable.btn_green)
        }

        btnApplied?.setOnClickListener {
            if (tvChooseDate?.text == this.getString(R.string.pilih_tanggal)) {
                Toast.makeText(this, "Harap isi tanggal", Toast.LENGTH_LONG).show()
            } else {
                dtDate = dateParams.format(cal.time)
                Toast.makeText(this, "Mengambil data pada tanggal: $dtDate", Toast.LENGTH_LONG).show()

                val i = Intent(this, MyteamSpvActivity::class.java)
                startActivity(i)
                finish()
            }
        }

        tvChooseDate?.setOnClickListener {
            DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        dtDate = ""

        CarefastOperationPref.saveInt(CarefastOperationPrefConst.MY_TEAM_CHIEF_ID, 0)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.MY_TEAM_CHIEF_NAME, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")

    }

    override fun onClickSpv(spvId: Int, spvName: String, shiftId: Int, projectCode: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.MY_TEAM_SPV_ID, spvId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.MY_TEAM_SPV_NAME, spvName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "Myteam Spv")

        val i = Intent(this, MyteamSpvActivity::class.java)
        i.putExtra("projectIdMyteamSpv", projectCode)
        startActivity(i)
    }
}