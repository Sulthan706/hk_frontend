package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityProfileOperationalBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.activity.DetailAbsentOprMgmntActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.PhoneNumberAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment.RatingOperationalFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDateTime
import java.time.temporal.ChronoField

class ProfileOperationalActivity : AppCompatActivity(), PhoneNumberAdapter.ListPhoneNumberCallBack {

    private lateinit var binding : ActivityProfileOperationalBinding
    private lateinit var rvPhoneAdapter : PhoneNumberAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, 0)
    private var projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.OPERATIONAL_OPS_PROJECT_CODE, "")
    private var month: Int = 0
    private var year: Int = 0
    private val phoneNumbers = ArrayList<String>()

    companion object {
        const val CALL_REQ = 101
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val currentTime = LocalDateTime.now()

    private val viewModel : OperationalManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OperationalManagementViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileOperationalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // first state loading
        binding.shimmerFotoProfile.startShimmerAnimation()
        binding.shimmerJobProfile.startShimmerAnimation()
        binding.shimmerNameProfile.startShimmerAnimation()
        binding.shimmerNucProfile.startShimmerAnimation()
        binding.shimmerProjectName.startShimmerAnimation()
        binding.clShimmer.visibility = View.VISIBLE
        binding.clProfile.visibility = View.GONE

        // set layout client or management
        if (userLevel == "CLIENT") {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

            // icon call
            binding.ivAppbarCall.visibility = View.GONE

            binding.clProfileOperational.setBackgroundResource(R.drawable.bg_profile_operational_secondary)
        } else {
            // icon call
            binding.ivAppbarCall.visibility = View.VISIBLE

            binding.clProfileOperational.setBackgroundResource(R.drawable.bg_profile_operational_primary)
        }

        binding.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        binding.ivAppbarCall.setOnClickListener {
            setupPermissions()
        }

        binding.llAbsen.setOnClickListener {
            val selectedMonth = currentTime.get(ChronoField.MONTH_OF_YEAR)
            val selectedYear = currentTime.get(ChronoField.YEAR)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_OPR_MANAGEMENT, selectedMonth)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_OPR_MANAGEMENT, selectedYear)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ABSENT_OPR_MANAGEMENT, projectCode)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.EMPLOYEE_ID_ABSENT_OPR_MANAGEMENT, employeeId)
            startActivity(Intent(this, DetailAbsentOprMgmntActivity::class.java))
        }

        binding.llRiwayat.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, employeeId)
            startActivity(Intent(this, HistoryAttendanceOperationalActivity::class.java))
        }
        // button get rating
        binding.llRating.setOnClickListener{
            showBottomDialog()
        }


        // get current month & year
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            month = currentTime.get(ChronoField.MONTH_OF_YEAR)
            year = currentTime.get(ChronoField.YEAR)
        }

        viewModel.getDetailOperationalAttendance(projectCode, employeeId, month, year)
        viewModel.getDetailOperational(employeeId)
        setObserver()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            openDialogPhoneNumber()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CALL_PHONE),
            CALL_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALL_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the call phone permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun openPhoneCall(phoneNumber: String) {
        val call = Intent(Intent.ACTION_CALL)
        call.data = Uri.parse("tel:$phoneNumber")
        startActivity(call)
    }

    private fun openDialogPhoneNumber() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom_phone_number)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val recyclerView = dialog.findViewById(R.id.rvDialogPhoneNumber) as RecyclerView
        val btnClose = dialog.findViewById(R.id.ivCloseDialogPhoneNumber) as ImageView
        val tvEmpty = dialog.findViewById<TextView>(R.id.tvEmptyDialogPhoneNumber)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        // validate no phone number
        when (phoneNumbers.size) {
            1 -> {
                if (phoneNumbers[0] == "") {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
            2 -> {
                if (phoneNumbers[0] == "" && phoneNumbers[1] == "") {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
            3 -> {
                if (phoneNumbers[0] == "" && phoneNumbers[1] == "" && phoneNumbers[2] == "") {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // set rv adapter
//        rvPhoneAdapter = PhoneNumberAdapter(
//            this,
//            phoneNumbers
//        ).also { it.setListener(this) }
//        recyclerView.adapter = rvPhoneAdapter

        dialog.show()
    }

    private fun showBottomDialog(){
        val myBottomSheet: BottomSheetDialogFragment = RatingOperationalFragment()
        val bundle = Bundle()
        bundle.putInt("employeeId", employeeId)
        myBottomSheet.arguments = bundle
        myBottomSheet.show(supportFragmentManager, myBottomSheet.tag)
        Log.d("bottomsheet", "$bundle")

    }


    @SuppressLint("SetTextI18n")
    private fun setObserver(){
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerFotoProfile.stopShimmerAnimation()
                        binding.shimmerJobProfile.stopShimmerAnimation()
                        binding.shimmerNameProfile.stopShimmerAnimation()
                        binding.shimmerNucProfile.stopShimmerAnimation()
                        binding.shimmerProjectName.stopShimmerAnimation()
                        binding.clShimmer.visibility = View.GONE
                        binding.clProfile.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getDetailOperationalResponse().observe(this) {
            if (it.code == 200) {
                binding.tvNameUserProfile.text = it.data.employeeName
                binding.tvJobUser.text = it.data.jobName
                binding.tvNucProfile.text = it.data.employeeNuc
                binding.tvProjectProfile.text = it.data.project.projectName

                val length = it.data.employeePhoneNumber.size
                for (i in 0 until length) {
                    if (it.data.employeePhoneNumber[i].employeePhone == null ||
                        it.data.employeePhoneNumber[i].employeePhone == "null" ||
                        it.data.employeePhoneNumber[i].employeePhone == "") {
                        phoneNumbers.add("")
                    } else {
                        phoneNumbers.add(it.data.employeePhoneNumber[i].employeePhone)
                    }
                }

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
                    this.binding.ivProfile.setImageDrawable(res)
                } else {
                    val requestOptions = com.bumptech.glide.request.RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true)
                        .error(R.drawable.ic_error_image)

                    Glide.with(this)
                        .load(url)
                        .apply(requestOptions)
                        .into(this.binding.ivProfile)
                }


            }
        }
        viewModel.getDetailOperationalAttendanceResponse().observe(this){

            val alfaCountData = it.data.lupaAbsenCount + it.data.tidakHadirCount
            binding.tvAbsenHadir.text = it.data.hadirCount.toString()
            binding.tvAbsenAlfa.text = alfaCountData.toString()
            binding.tvAbsenIzin.text = it.data.izinCount.toString()
            binding.tvAbsenLemburganti.text = it.data.lemburGantiCount.toString()
            binding.tvAbsenLemburtagih.text = it.data.tidakHadirCount.toString()
            binding.tvProgressBarOperational.text = "${it.data.totalAttendanceInPercent}%"
            binding.progressBarOperational.progress = it.data.totalAttendanceInPercent
        }
    }

    override fun onClickPhone(phoneNumber: String) {
        openPhoneCall(phoneNumber)
    }

}