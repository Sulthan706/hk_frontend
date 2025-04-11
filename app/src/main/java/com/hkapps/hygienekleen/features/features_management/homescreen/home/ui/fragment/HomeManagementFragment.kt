package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.location.LocationRequest
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.databinding.FragmentHomeManagementBinding
import com.hkapps.hygienekleen.features.facerecog.model.statsregisface.StatsRegisFaceResponseModel
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.ListProjectManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity.AttendanceGeoManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.CftalkManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.ProjectsCftalkManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.ListOperationalManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.ListAllProjectManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.ListBranchProjectManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.ui.activity.VisitTrackingManagementActivity
import com.hkapps.hygienekleen.features.facerecog.ui.RegisterFaceRecogManagementActivity
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.activity.ViewPagerBakManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity.AttendanceOutManagementLivenessActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity.ExtendVisitDurationActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.activity.DailyClosingManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.viewmodel.ClosingManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.EditProfileManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeNewsManagementActivity
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.activity.ScheduleManagementActivity
import com.hkapps.hygienekleen.features.features_management.project.viewmodel.ProjectManagementViewModel
import com.hkapps.hygienekleen.features.features_management.shareloc.ui.activity.ShareLocManagementActivity
import com.hkapps.hygienekleen.features.features_management.shareloc.ui.activity.bod.MainListShareLocManagementActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import com.hkapps.hygienekleen.R


class HomeManagementFragment : Fragment() {

    private lateinit var binding: FragmentHomeManagementBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private var imageBirth =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_IMAGE, "")
    private val userNuc = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, "")
    private var position: String = ""
    private var versionApp: String = ""
    private var dateBirth =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.BIRTHDAY_MANAGEMENT, "")

    private val viewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }
    private val projectViewModel: ProjectManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ProjectManagementViewModel::class.java)
    }
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private val faceRecogViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(FaceRecogViewModel::class.java)
    }

    private val closingViewModel : ClosingManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ClosingManagementViewModel::class.java)
    }

    private var oneShowPopUp: Boolean = false

    private var flag = 0

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var newMemberAndDataNotCompleted = ""
    private var userLevel =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val visitDurationStatus = CarefastOperationPref.loadString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
    private val branchName = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_NAME_PROJECT_MANAGEMENT, "")
    private val totalProject = CarefastOperationPref.loadInt(CarefastOperationPrefConst.BRANCH_NAME_PROJECT_MANAGEMENT_TOTAL_PROJECT, 0)
    private val nuc = CarefastOperationPref.loadString(CarefastOperationPrefConst.LOGIN_MANAGEMENT_NUC, "")
    private val idRkbOperation = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, 0)
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)

    companion object {
        private const val MULTIPLE_PERMISSION_REQUEST_CODES = 4
    }

    private var oneTimeShow: Boolean = false

    var isDialogOpened = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        startActivity(Intent(requireActivity(),LivenessActivity::class.java).also{
//            it.putExtra("is_register",true)
//            it.putExtra("is_management",true)
//        })


        // set default layout
        binding.linearLayout6.visibility = View.GONE
        binding.linearLayout7.visibility = View.GONE
        binding.linearLayout8.visibility = View.GONE
        binding.linearLayout9.visibility = View.GONE


        if(nuc.equals("CF0029",ignoreCase = true)){
            binding.linearMrBod.visibility = View.VISIBLE
            binding.linearMesinBod.visibility = View.VISIBLE
            binding.rlTurnOverBod.visibility - View.VISIBLE
            binding.linearTimesheetBod.visibility = View.VISIBLE
        }else{
            binding.linearMrBod.visibility = View.INVISIBLE
            binding.linearMesinBod.visibility = View.INVISIBLE
            binding.rlTurnOverBod.visibility - View.INVISIBLE
            binding.linearTimesheetBod.visibility = View.INVISIBLE
            binding.linearTurnoverBod.visibility = View.INVISIBLE
        }


        if(isVp){
            binding.totalProject.text = totalProject.toString()
            binding.branchName.text = "Total project cabang $branchName"
            binding.clVp.visibility = View.VISIBLE
            binding.clTrackingVisitActivity.visibility = View.GONE
            binding.linearLayoutVp.visibility = View.VISIBLE
            binding.linearLayout6.visibility = View.GONE
        }else{
            binding.clVp.visibility = View.GONE
            binding.clTrackingVisitActivity.visibility = View.VISIBLE
            binding.linearLayoutVp.visibility = View.GONE
            binding.linearLayout6.visibility = View.VISIBLE
        }


        // new update app
        val manager = requireActivity().packageManager
        val info = manager.getPackageInfo(requireActivity().packageName, 0)
        versionApp = info.versionName
        binding.tvVersionHome.text = versionApp

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(requireActivity())
            activity?.window!!.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.white)
        }

        binding.swipeHome.setOnRefreshListener {
            Handler().postDelayed(
                {
                    binding.swipeHome.isRefreshing = false
                    val i = Intent(requireActivity(), HomeManagementActivity::class.java)
                    startActivity(i)
                    requireActivity().finishAffinity()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requestLocationUpdates()

        // validate layout level management
        if (userLevel == "BOD" || userLevel == "CEO") {
            binding.llShareLocManagement.visibility = View.VISIBLE
            binding.llSchedule.visibility = View.GONE
            binding.llCfTalkBod.visibility = View.VISIBLE
            binding.linearLayout8.visibility = View.GONE
            binding.linearLayout9.visibility = View.GONE
        } else {
            binding.llShareLocManagement.visibility = View.GONE
            binding.llSchedule.visibility = View.VISIBLE
            binding.llCfTalkBod.visibility = View.GONE
            binding.linearLayout8.visibility = View.VISIBLE
            binding.linearLayout9.visibility = View.VISIBLE
        }
        Log.d("agri","$userId, $userNuc, $userName, $userLevel")

        // validate layout teknisi
        if (levelPosition == 20) {
            binding.linearTeknisi.visibility = View.VISIBLE
            binding.linearLayout8.visibility = View.GONE
            binding.linearLayout9.visibility = View.GONE
            binding.bakTeknisi.setOnClickListener { startActivity(Intent(requireContext(), ViewPagerBakManagementActivity::class.java)) }

            binding.cftalkTeknisi.isEnabled = false
            binding.llOperational.isEnabled = false
            binding.cftalk.setBackgroundResource(R.drawable.bg_disable)
            binding.rlOperational.setBackgroundResource(R.drawable.bg_disable)
        } else{
            binding.linearTeknisi.visibility = View.GONE
        }
//        if (jobPositions == "TEKNISI" || jobPositions == "TEKNISI_HO"){
//            CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_JABATAN, jobPositions)
//            binding.cftalkTeknisi.isEnabled = false
//            binding.llOperational.isEnabled = false
//            binding.cftalk.setBackgroundResource(R.drawable.bg_disable)
//            binding.rlOperational.setBackgroundResource(R.drawable.bg_disable)
//        }

        binding.llShareLocManagement.setOnClickListener {
            if (userLevel == "BOD" || userLevel == "CEO") {
                checkGpsShareLoc()
            } else {
                binding.llShareLocManagement.visibility = View.GONE
            }
        }

        binding.llbakmesin.setOnClickListener {
            startActivity(Intent(requireContext(), ViewPagerBakManagementActivity::class.java))
        }


        binding.tvShareLocHome.text = if (userLevel == "BOD" || userLevel == "CEO") "Report Lokasi"
        else "Share Lokasi"

        if (userLevel == "BOD" || userLevel == "CEO"){
            binding.linearLayoutBod.visibility = View.VISIBLE
        }

        binding.ivBtnHomeNewsManagement.setOnClickListener {
            startActivity(Intent(requireContext(), HomeNewsManagementActivity::class.java))
        }

        // show popup visit duration
        if (idRkbOperation != 0) {
            when(visitDurationStatus) {
                "Extended Exceeded" -> {
                    showDialogExtendedVisitExceeded()
                    fcmNotif("rkb_visit_$userId")
                    fcmNotifUnsubs("rkb_visit_$userId")
                }
                "Visit Exceeded" -> {
                    showDialogVisitDurationExceeded()
                    fcmNotif("rkb_visit_$userId")
                    fcmNotifUnsubs("rkb_visit_$userId")
                }
            }
        }

        viewModel.getCheckProfile(userId)
        viewModel.checkRenewalManagement(userId)
        checkShowDialogEditProfile()
        lifecycleScope.launch {
            delay(1000)
            loadData()
            setObserverFace()
            setObserver()
            checkGps()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDialogVisitDurationExceeded() {
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout_management_visit_exceeded)

        val tvProject = dialog.findViewById<TextView>(R.id.tvProjectVisitDurationExceeded)
        val btnCheckOut = dialog.findViewById<AppCompatButton>(R.id.btnAttendanceOut)
        val btnExtendVisit = dialog.findViewById<AppCompatButton>(R.id.btnExtendVisitDuration)

        tvProject.text = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")
        btnCheckOut.setOnClickListener {
            if (!cameraPermission()){
                cameraPermission()
                Toast.makeText(requireActivity(), "Please allow all permission", Toast.LENGTH_SHORT).show()
            } else {
//                startActivity(Intent(requireContext(), AttendanceOutManagementActivity::class.java))
                startActivity(Intent(requireContext(), AttendanceOutManagementLivenessActivity::class.java))
                dialog.dismiss()
            }
        }
        btnExtendVisit.setOnClickListener {
            val currentTime = LocalTime.now()
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TIME_LAST_VISIT, currentTime.toString())

            startActivity(Intent(requireContext(), ExtendVisitDurationActivity::class.java))
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDialogExtendedVisitExceeded() {
        val dialog = Dialog(requireActivity())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout_management_extended_visit_exceeded)

        val btnCheckOut = dialog.findViewById<AppCompatButton>(R.id.btnAttendanceOutExtendedVisitExceeded)

        btnCheckOut.setOnClickListener {
            if (!cameraPermission()){
                cameraPermission()
                Toast.makeText(requireActivity(), "Please allow all permission", Toast.LENGTH_SHORT).show()
            } else {
//                startActivity(Intent(requireContext(), AttendanceOutManagementActivity::class.java))
                startActivity(Intent(requireContext(), AttendanceOutManagementLivenessActivity::class.java))
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun cameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 101)
        }
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkFaceAndProfile(userId: Int) {
        // Trigger the profile check
        viewModel.getCheckProfile(userId)

        // Observe the profile response
        viewModel.checkEditProfileModel.observe(viewLifecycleOwner) { profileResponse ->
//            handleProfileResponse(profileResponse)

            // If profile is valid, trigger face recognition check
            if (profileResponse.code == 200) {
                faceRecogViewModel.getStatsRegisManagementFaceRecog(userId)
            } else {
                openDialogEditProfile()
            }
        }

        // Observe the face recognition response
        faceRecogViewModel.getStatsManagementFaceRecogViewModel().observe(viewLifecycleOwner) { faceResponse ->
            handleFaceRecogResponse(faceResponse)
        }

    }

    private fun handleFaceRecogResponse(response: StatsRegisFaceResponseModel) {
        faceRecogViewModel.getStatsManagementFaceRecogViewModel().removeObservers(viewLifecycleOwner)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.STATS_TYPE,response.message)
        if (response.code == 200) {
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_ALFABETA, false)
            checkGPSAttendance()
        } else {
            when(response.errorCode) {
                "126" -> {
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_ALFABETA, true)
                    checkGPSAttendance()
                }
                else -> {
                    val intent = Intent(requireContext(), RegisterFaceRecogManagementActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            }
        }
    }


    private fun checkGpsShareLoc() {

        val builder = LocationSettingsRequest.Builder().addLocationRequest(LocationRequest())
        val task = LocationServices.getSettingsClient(requireContext())
            .checkLocationSettings(builder.build())

        task
            .addOnSuccessListener { _ ->
                if (userLevel == "BOD" || userLevel == "CEO") {
                    startActivity(
                        Intent(
                            requireContext(),
                            MainListShareLocManagementActivity::class.java
                        )
                    )
                } else {
                    startActivity(Intent(requireContext(), ShareLocManagementActivity::class.java))
                }
//            startActivity(Intent(requireContext(), MainListShareLocManagementActivity::class.java))
            }
            .addOnFailureListener { e ->
                val statusCode = (e as ResolvableApiException).statusCode
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        e.startResolutionForResult(requireActivity(), 100)
                    } catch (sendEx: IntentSender.SendIntentException) {
                    }
                }
            }
    }

    private fun checkGps() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(LocationRequest())
        val task = LocationServices.getSettingsClient(requireContext())
            .checkLocationSettings(builder.build())

        task
            .addOnSuccessListener { response ->

            }
            .addOnFailureListener { e ->
                if (e is ResolvableApiException) {
                    val statusCode = e.statusCode
                    if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            e.startResolutionForResult(requireActivity(), 100)
                        } catch (sendEx: IntentSender.SendIntentException) {
                            // Handle exception
                        }
                    }
                } else {
                    // Handle other types of ApiException here
                }
            }
    }

    // Request location updates
    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest().apply {
            interval =
                10000 // Interval in milliseconds receive location updates
            fastestInterval = 5000 // Fastest interval in milliseconds
            priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                LocationRequest.QUALITY_HIGH_ACCURACY
            } else {
                com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
            }
        }

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION
            )
        }
    }

    // Handle location updates
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation.let { location ->
                val latitude = location?.latitude
                val longitude = location?.longitude
//fake gps
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (location.isMock) {
                        if (!oneShowPopUp) {
                            dialogFakeGpsInstalled()
                        }
                    }
                } else {
                    if (location.isFromMockProvider) {
                        if (!oneShowPopUp) {
                            dialogFakeGpsInstalled()
                        }
                    }
                }
                //use lat long for get address
                val address = getAddressFromCoordinates(context!!, latitude!!, longitude!!)
//                main(address)
//                api
//                viewModel.putLatLongManagement(userId, latitude.toString(), longitude.toString(), address)
            }
        }
    }

    private fun dialogFakeGpsInstalled() {
        oneShowPopUp = true
        val view = View.inflate(requireContext(), R.layout.dialog_fake_gps, null)
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingSore)
        btnBack.setOnClickListener {
            dialog.dismiss()
            finishAffinity(requireActivity())
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun showDialogNotClosing(isChiefSpv : Boolean = false) {
        val dialog = Dialog(requireActivity())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_closing)

        val yesBtn = dialog.findViewById<AppCompatButton>(R.id.btn_check_and_closing)
        val tvDesc = dialog.findViewById<TextView>(R.id.tv_not_closing_desc)
        if(isChiefSpv){
            yesBtn.text = "Closing & Kirim Laporan Pekerjaan"
            tvDesc.text = "Anda tidak dapat melakukan absen masuk"
        }
        yesBtn.setOnClickListener {
            flag = 0
            startActivity(Intent(requireActivity(), DailyClosingManagementActivity::class.java).also {
                it.putExtra("from_home",true)
            })
            dialog.dismiss()
        }
        dialog.show()
    }

//    private fun main(text: String) {
//
//        if (text != null) {
//            val maxLength = 90
//            val ellipsis = "..."
//
//            val validatedAddress = when {
//                text.length > maxLength -> text.substring(0, maxLength) + ellipsis
//                else -> text
//            }
//
////            binding.tvRealtimeLocation.text = validatedAddress
//
//        }
//    }

    // Handle permission request result

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun getAddressFromCoordinates(
        context: Context,
        latitude: Double,
        longitude: Double
    ): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addressText = ""

        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                val stringBuilder = StringBuilder()

                for (i in 0..address.maxAddressLineIndex) {
                    stringBuilder.append(address.getAddressLine(i)).append("\n")
                }

                addressText = stringBuilder.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return addressText
    }


    private fun loadData() {
        viewModel.getProfileManagement(userId)
        viewModel.getCheckNewsManagement(userType = "Management", userId)
    }

    private fun setObserverFace() {
        faceRecogViewModel.getStatsManagementFaceRecogViewModel().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_ALFABETA, false)
                checkGPSAttendance()
            } else {
                when (it.errorCode) {
                    "126" -> {
                        CarefastOperationPref.saveBoolean(
                            CarefastOperationPrefConst.STATS_ALFABETA,
                            true
                        )
                        checkGPSAttendance()
                    }

                    else -> {
                        startActivity(
                            Intent(
                                requireContext(),
                                RegisterFaceRecogManagementActivity::class.java
                            )
                        )
                    }
                }
            }
        }
    }

    private fun showCustomSnackbarAtTop(view: View, message: String) {
        if (!oneTimeShow) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Lihat") {
                    val intent = Intent(view.context, HomeNewsManagementActivity::class.java)
                    view.context.startActivity(intent)
                }

            // Set the snackbar's gravity to show it at the top
            val snackbarView = snackbar.view
            val params = snackbarView.layoutParams

            if (params is CoordinatorLayout.LayoutParams) {
                params.gravity = Gravity.TOP
            } else if (params is FrameLayout.LayoutParams) {
                params.gravity = Gravity.TOP
            }

            snackbarView.layoutParams = params
            snackbar.show()
        }
        oneTimeShow = true
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun setObserver() {
        viewModel.getCheckNewsManagementViewModel().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                showCustomSnackbarAtTop(requireView(), it.message)
                binding.ivNewNewsManagement.visibility = View.VISIBLE
            } else {
                binding.ivNewNewsManagement.visibility = View.GONE
            }
        }
        viewModel.getProjectModel().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                when (position) {
                    "FM" -> {
                        for (projectCode in 0 until it.data.listProject.size) {

                            if (it.data.listProject[projectCode].status == "Y") {
                                fcmNotif("Complaint_FM_" + it.data.listProject[projectCode].projectCode)
                                fcmNotif("Complaint_Management_Client_" + it.data.listProject[projectCode].projectCode)

                                fcmNotif("Permission_From_Operator_" + it.data.listProject[projectCode].projectCode)

                                fcmNotif("Complaint_Visitor_" + it.data.listProject[projectCode].projectCode)
                                fcmNotif("Resign_Approval_" + userLevel + "_" + it.data.listProject[projectCode].projectCode)

                                fcmNotif("rkb_visit_$userId")
                                fcmNotif("closing_FM_$userId")

                            } else {
                                fcmNotifUnsubs("Complaint_FM_" + it.data.listProject[projectCode].projectCode)
                                fcmNotifUnsubs("Complaint_Management_Client_" + it.data.listProject[projectCode].projectCode)

                                fcmNotifUnsubs("Permission_From_Operator_" + it.data.listProject[projectCode].projectCode)
                                fcmNotifUnsubs("Complaint_Visitor_" + it.data.listProject[projectCode].projectCode)

                                fcmNotifUnsubs("Resign_Approval_" + userLevel + "_" + it.data.listProject[projectCode].projectCode)

                                fcmNotifUnsubs("rkb_visit_$userId")
                                fcmNotifUnsubs("closing_FM_$userId")

                            }
                        }
                    }

                    "OM" -> {
                        for (projectCode in 0 until it.data.listProject.size) {
                            if (it.data.listProject[projectCode].status == "Y") {
                                fcmNotif("Complaint_OM_" + it.data.listProject[projectCode].projectCode)
                                fcmNotif("Complaint_Management_Client_" + it.data.listProject[projectCode].projectCode)

                                fcmNotif("Complaint_Visitor_" + it.data.listProject[projectCode].projectCode)
                                fcmNotif("Resign_Approval_" + userLevel + "_" + it.data.listProject[projectCode].projectCode)

                                fcmNotif("rkb_visit_$userId")
                            } else {
                                fcmNotifUnsubs("Complaint_OM_" + it.data.listProject[projectCode].projectCode)
                                fcmNotifUnsubs("Complaint_Management_Client_" + it.data.listProject[projectCode].projectCode)

                                fcmNotifUnsubs("Complaint_Visitor_" + it.data.listProject[projectCode].projectCode)
                                fcmNotifUnsubs("Resign_Approval_" + userLevel + "_" + it.data.listProject[projectCode].projectCode)

                                fcmNotifUnsubs("rkb_visit_$userId")
                            }
                        }
                    }

                    "GM" -> {
                        for (projectCode in 0 until it.data.listProject.size) {
                            if (it.data.listProject[projectCode].status == "Y") {
                                fcmNotif("Complaint_GM_" + it.data.listProject[projectCode].projectCode)
                                fcmNotif("Complaint_Management_Client_" + it.data.listProject[projectCode].projectCode)

                                fcmNotif("Complaint_Visitor_" + it.data.listProject[projectCode].projectCode)
                                fcmNotif("Resign_Approval_" + userLevel + "_" + it.data.listProject[projectCode].projectCode)

                                fcmNotif("rkb_visit_$userId")
                            } else {
                                fcmNotifUnsubs("Complaint_GM_" + it.data.listProject[projectCode].projectCode)
                                fcmNotifUnsubs("Complaint_Management_Client_" + it.data.listProject[projectCode].projectCode)

                                fcmNotifUnsubs("Complaint_Visitor_" + it.data.listProject[projectCode].projectCode)
                                fcmNotifUnsubs("Resign_Approval_" + userLevel + "_" + it.data.listProject[projectCode].projectCode)

                                fcmNotifUnsubs("rkb_visit_$userId")
                            }
                            println("getproject " + it.data.listProject[projectCode].projectCode)
                        }
                    }

                    "BOD" -> {
                        fcmNotif("Complaint_BOD-CEO")
                        fcmNotif("rkb_visit_$userId")
                    }

                    "CEO" -> {
                        fcmNotif("Complaint_BOD-CEO")
                        fcmNotif("rkb_visit_$userId")
                    }
                }
            }
        }
        viewModel.attendanceActivityModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.TOTAL_PROJECT,
                    it.data.totalProject.toString()
                )
                binding.tvTotalProjectHomeManagement.text = "" + it.data.totalProject

                if (it.data.todayProgress == 0) {
                    binding.tvProgressProjectHomeManagement.text = "-"
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.TOTAL_PROJECT_TODAY,
                        ""
                    )
                } else {
                    binding.tvProgressProjectHomeManagement.text = "${it.data.todayProgress}"
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.TOTAL_PROJECT_TODAY,
                        it.data.todayProgress.toString()
                    )
                }

                binding.tvYesterdayProjectHomeManagement.text =
                    if (it.data.yesterdayProgress == 0) {
                        "-"
                    } else {
                        "${it.data.yesterdayProgress}"
                    }

                binding.tvThisMonthProjectHomeManagement.text = if (it.data.monthlyProgress == 0) {
                    "-"
                } else {
                    "${it.data.monthlyProgress}"
                }

                binding.tvLastProjectHomeManagement.text =
                    if (it.data.lastVisit.projectName == "" ||
                        it.data.lastVisit.projectName == "null" || it.data.lastVisit.projectName == null
                    ) {
                        "Belum ada data kunjungan"
                    } else {
                        it.data.lastVisit.projectName
                    }

                binding.tvDateHomeManagement.text = if (it.data.lastVisit.date == "" ||
                    it.data.lastVisit.date == "null" || it.data.lastVisit.date == null
                ) {
                    "-"
                } else {
                    it.data.lastVisit.date
                }

                binding.tvTimeInHomeManagement.text = if (it.data.lastVisit.checkIn == "" ||
                    it.data.lastVisit.checkIn == "null" || it.data.lastVisit.checkIn == null
                ) {
                    "--:--"
                } else {
                    it.data.lastVisit.checkIn
                }

                binding.tvTimeOutHomeManagement.text = if (it.data.lastVisit.checkOut == "" ||
                    it.data.lastVisit.checkOut == "null" || it.data.lastVisit.checkOut == null
                ) {
                    "--:--"
                } else {
                    it.data.lastVisit.checkOut
                }

            } else {
                Toast.makeText(
                    requireActivity(),
                    "Gagal mengambil data aktivitas kunjungan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        homeViewModel.checkVersionAppModel.observe(viewLifecycleOwner) {
//            if (it.code == 200) {
//                if (position == "FM" || position == "GM" || position == "OM") {
//                    viewModel.getCheckProfile(userId)
//                }
//            } else {
//                when(it.message) {
//                    "Your app need to update" -> openDialogNewUpdate()
//                    else -> Toast.makeText(requireActivity(), "error check version app", Toast.LENGTH_SHORT).show()
//                }
//            }
            if (it.code != 200) {
                when (it.message) {
                    "Your app need to update" -> openDialogNewUpdate()
                    else -> Toast.makeText(
                        requireActivity(),
                        "error check version app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.getProfileResponseManagementModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                position = it.data.levelJabatan

                if(it.data.lastUpdatedProfile == null && !isDialogOpened && newMemberAndDataNotCompleted.isBlank()){
                    isDialogOpened = true
                    openDialogLastEditProfile(true)
                }

                if(it.data.lastUpdatedProfile != null && !isDialogOpened && newMemberAndDataNotCompleted.isBlank()){
                    isDialogOpened = true
                    observeCheckRenewal()
                }
                //get job position
                val jobPositions = it.data.adminMasterJabatan
                CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_JABATAN_MANAGEMENT,it.data.adminMasterJabatan)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_IMAGE_MANAGEMENT,it.data.adminMasterImage ?: "")


                if (jobPositions == "TEKNISI" || jobPositions == "TEKNISI_HO"){
                    binding.linearTeknisi.visibility = View.VISIBLE
                    binding.linearBak.visibility = View.VISIBLE
                    binding.linearLayout8.visibility = View.GONE
                    binding.linearLayout9.visibility = View.GONE
                    binding.linearTimesheet.visibility = View.GONE
                    binding.linearMr.visibility = View.GONE
                    binding.linearTurnOverTeknisi.visibility = View.VISIBLE
                    binding.rlTurnOverTeknisi.setOnClickListener {
                        val i = Intent(requireActivity(), ListAllProjectManagementActivity::class.java).also {
                            it.putExtra("type","turnover")
                        }
                        startActivity(i)
                    }
                    binding.bakTeknisi.setOnClickListener { startActivity(Intent(requireContext(), ViewPagerBakManagementActivity::class.java)) }
                    binding.rlTeknisi.setOnClickListener { startActivity(Intent(requireContext(), ViewPagerBakManagementActivity::class.java)) }

                }else{
                    binding.linearBak.visibility = View.GONE
                    binding.linearTeknisi.visibility = View.GONE
                }

                if (jobPositions == "TEKNISI" || jobPositions == "TEKNISI_HO"){
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_JABATAN, jobPositions)
                }

                binding.tvUserNameHomeVendor.text = it.data.adminMasterName
                binding.tvUserNucHomeVendor.text = it.data.adminMasterNUC
                loadProfileDefault(it.data.adminMasterImage ?: "")

                setHomeLayout()
                if (it.data.adminMasterImage.isNullOrEmpty()){
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.IMAGE_MANAGEMENT,
                        ""
                    )
                } else {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.IMAGE_MANAGEMENT,
                        it.data.adminMasterImage
                    )
                }
                if (it.data.adminMasterPlaceOfBirth.isNullOrEmpty()){
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.BIRTHDAY_MANAGEMENT,
                        ""
                    )
                } else {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.BIRTHDAY_MANAGEMENT,
                        it.data.adminMasterPlaceOfBirth
                    )
                }


                viewModel.getProject(userId)
                viewModel.getAttendanceActivity(userId)
                homeViewModel.checkVersion(versionApp)
            } else {
                binding.tvUserNameHomeVendor.text = getString(R.string.user_name)
                binding.tvUserNucHomeVendor.text = getString(R.string.user_nuc)
                binding.ivFotoUserHomeVendor.setImageDrawable(resources.getDrawable(R.drawable.ic_error_image))
                Toast.makeText(
                    requireActivity(),
                    "Gagal mengambil data profile",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        //updatte latlong
//        viewModel.putLatLongManagementViewModel().observe(viewLifecycleOwner){
//            if (it.code == 200) {
//                Log.d("Agri","management sukses")
//            } else {
//                Log.d("Agri","management gagal")
//            }
//        }
        viewModel.attendanceFeatureAvailabilityModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                CarefastOperationPref.saveString(CarefastOperationPrefConst.ATTENDANCE_FEATURE_ACCESS_AVAILABILITY, it.message)
                if(it.message == "V2"){
                    if(levelPosition != 20){
                        checkClosing()
                    }else{
                        checkFaceAndProfile(userId)
                    }
                }else{
                    checkFaceAndProfile(userId)
                }
            } else {
                CarefastOperationPref.saveString(CarefastOperationPrefConst.ATTENDANCE_FEATURE_ACCESS_AVAILABILITY, "")
            }
        }
    }

    private fun greetingDialog() {
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date())

        val endTimePagi = "10:59"
        val endTimeSiang = "14:59"
        val endTimeSore = "17:59"
        val endTimeMalam = "05:00"

        if (time < endTimePagi) {
            greetingPagi()
        } else if (time < endTimeSiang) {
            greetingSiang()
        } else if (time < endTimeSore) {
            greetingSore()
        } else if (time > endTimeMalam) {
            greetingMalam()
        }

        //validasi ultah management
        val sdfBirthday = SimpleDateFormat("dd-MMMM-yyyy")
        val timeBirth = sdfBirthday.format(Date())

        if (dateBirth == timeBirth) {
            greetingBirthday()
        }
    }

    private fun greetingPagi() {
        val view = View.inflate(requireActivity(), R.layout.dialog_greeting_pagi, null)
        val builder = android.app.AlertDialog.Builder(requireActivity())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingPagi)
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun greetingSiang() {
        val view = View.inflate(requireActivity(), R.layout.dialog_greeting_siang, null)
        val builder = android.app.AlertDialog.Builder(requireActivity())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingSiang)
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun greetingSore() {
        val view = View.inflate(requireActivity(), R.layout.dialog_greeting_sore, null)
        val builder = android.app.AlertDialog.Builder(requireActivity())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingSore)
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun greetingMalam() {
        val view = View.inflate(requireActivity(), R.layout.dialog_greeting_malam, null)
        val builder = android.app.AlertDialog.Builder(requireActivity())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingMalam)
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun greetingBirthday() {
        val view = View.inflate(requireActivity(), R.layout.dialog_greeting_happybirthday, null)
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setView(view)
        val dialog = builder.create()
        val ivManagement = view.findViewById<ImageView>(R.id.ivImageManagement)
        val tvName = view.findViewById<TextView>(R.id.tvNameBirthday)

        tvName.text = userName
        val url =
            getString(R.string.url) + "assets.admin_master/images/photo_profile/$imageBirth"

        if (imageBirth == "null" || imageBirth == null || imageBirth == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imageResource = resources.getIdentifier(uri, null, requireActivity().packageName)
            val res = resources.getDrawable(imageResource)
            ivManagement.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(ivManagement)
        }

        dialog.show()
//        val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingMalam)
//        btnBack.setOnClickListener{
//            dialog.dismiss()
//        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun openDialogMonthProfile() {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_monthly_update_profile)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)

            val button: AppCompatButton = dialog.findViewById(R.id.btnDialogEditProfile)



            button.setOnClickListener {
                dialog.dismiss()
                startActivity(Intent(requireContext(), EditProfileManagementActivity::class.java))
            }
            dialog.show()
    }

    private fun openDialogEditProfile() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_custom_edit_profile)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val button: AppCompatButton = dialog.findViewById(R.id.btnDialogEditProfile)
//        val bottomNavigationView: BottomNavigationView = dialog.findViewById(R.id.bottomNavigationView)


        button.setOnClickListener {
            dialog.dismiss()
            Handler(Looper.getMainLooper()).postDelayed({
                val bottomNavigationView: BottomNavigationView? = activity?.findViewById(R.id.bottomNavigationView)
                bottomNavigationView?.selectedItemId = R.id.menuProfile
            }, 200)
        }
        dialog.show()
    }

    private fun checkShowDialogEditProfile() {
        viewModel.checkEditProfileModel.observe(requireActivity()) {
            if (it.code != 200) {
                when (it.errorCode) {
//                      "01" -> GreetingCompletingProfileLowFragment().show(supportFragmentManager,"greeting")
                    "01" -> {
                        newMemberAndDataNotCompleted = "01"
                        openDialogEditProfile()
                    }
                    "03" -> {
                        newMemberAndDataNotCompleted = "03"
                        openDialogMonthProfile()
                    }
                    else -> {
                        newMemberAndDataNotCompleted = ""
                        Toast.makeText(
                            requireContext(),
                            "error check profile data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            Log.d("agri","${it.errorCode}")
        }
    }

    private fun openDialogLastEditProfile(isLastUpdate: Boolean) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.layout_dialog_last_update_profile)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val tvTittle = dialog.findViewById<TextView>(R.id.tv_tittle_dialog_last_update)
        val tvDesc = dialog.findViewById<TextView>(R.id.tv_desc_dialog_last_update)
        val btnSave: AppCompatButton = dialog.findViewById(R.id.btn_save_complete)
        val btnUpdateData: AppCompatButton = dialog.findViewById(R.id.btn_update_profile)

        if(isLastUpdate){
            btnSave.setOnClickListener {
                viewModel.updateLastManagementProfile(userId)
                observerUpdateLastProfile()
                dialog.dismiss()
                greetingDialog()

            }
            btnUpdateData.setOnClickListener {
                dialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed({
                    val bottomNavigationView: BottomNavigationView? = activity?.findViewById(R.id.bottomNavigationView)
                    bottomNavigationView?.selectedItemId = R.id.menuProfile
                }, 200)
            }
        }else{
            tvTittle.text = resources.getString(R.string.text_tittle_update_profile)
            tvDesc.text = resources.getString(R.string.text_desc_update_profile)
            btnSave.text = resources.getString(R.string.text_update_profile)
            btnSave.setOnClickListener {
                dialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed({
                    val bottomNavigationView: BottomNavigationView? = activity?.findViewById(R.id.bottomNavigationView)
                    bottomNavigationView?.selectedItemId = R.id.menuProfile
                }, 200)
                greetingDialog()
            }
            btnUpdateData.visibility = View.GONE
        }
        dialog.show()
    }

    private fun observerUpdateLastProfile(){
        viewModel.updateLastManagementModel.observe(requireActivity()){
            if(it.errorCode != null){
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeCheckRenewal(){
        var isShowDialogGreeting = true
        viewModel.checkRenewalManagementModel.observe(requireActivity()){
            if(it.errorCode == "01"){
                openDialogLastEditProfile(false)
            }
            if(it.errorCode != "01" && isShowDialogGreeting){
                isShowDialogGreeting = false
                greetingDialog()
            }
        }
    }

    private fun checkClosing(){
        closingViewModel.checkClosingPopupHome(userId)
        closingViewModel.checkClosingStatusModel.observe(requireActivity()){
            when(it.message){
                "Daily closing not finished yet" -> {
                   if(flag == 0){
                       showDialogNotClosing(true)
                   }
                }

                "Daily closing not finished yet, include off" -> {
                   if(flag == 0){
                       showDialogNotClosing(true)
                   }
                }
                else -> {
                    checkFaceAndProfile(userId)
                }
            }
            flag = 1
        }
    }


    private fun setHomeLayout() {
        if(isVp){
            binding.linearLayout6.visibility = View.GONE
        }else{
            binding.linearLayout6.visibility = View.VISIBLE
        }


        // tracking visit project
        binding.clTrackingVisitActivity.setOnClickListener {
            startActivity(Intent(requireActivity(), VisitTrackingManagementActivity::class.java))
        }

        // menu notifikasi
        binding.ivNotifHomeVendor.setOnClickListener {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.CLICK_FROM, "Home"
            )
            //ke fitur vendor
            val i = Intent(requireActivity(), ListProjectManagementActivity::class.java)
            startActivity(i)
        }

        // geo location attendance
        binding.llAttendance.setOnClickListener {
            // check feature attendance api
            viewModel.getAttendanceFeature(userId)
//            checkFaceAndProfile(userId)

        }

        // menu jadwal
        binding.llSchedule.setOnClickListener {
            startActivity(Intent(requireActivity(), ScheduleManagementActivity::class.java))
        }

        // menu operational
        binding.llOperational.setOnClickListener {
            val i = Intent(requireActivity(), ListOperationalManagementActivity::class.java)
            startActivity(i)
        }

        binding.rlOperationalAbsen.setOnClickListener {
            val i = Intent(requireActivity(), ListAllProjectManagementActivity::class.java).also {
                it.putExtra("type","absen_operational")
            }

            startActivity(i)
        }

        binding.llCtalk.setOnClickListener {
            val i = Intent(requireActivity(), ListAllProjectManagementActivity::class.java).also {
                it.putExtra("type","ctalk")
            }
            startActivity(i)
        }

        binding.linearDailyAbsen.setOnClickListener {
            val i = Intent(requireActivity(), ListAllProjectManagementActivity::class.java).also {
                it.putExtra("type","absen")
            }
            startActivity(i)
        }

        if(position == "BOD" || position == "CEO"){
            binding.linearLayout7.visibility = View.VISIBLE
        }else{
            binding.linearLayout7.visibility = View.GONE
        }

        binding.llProjectVp.setOnClickListener {
            if (position == "BOD" || position == "CEO") {
                startActivity(
                    Intent(
                        requireActivity(),
                        ListBranchProjectManagementActivity::class.java
                    )
                )
            } else {
                val i = Intent(requireActivity(), ListAllProjectManagementActivity::class.java)
                startActivity(i)
            }
        }


        binding.linearMr.setOnClickListener {
            if (position == "BOD" || position == "CEO") {
                startActivity(
                    Intent(
                        requireActivity(),
                        ListBranchProjectManagementActivity::class.java
                    ).also {
                        it.putExtra("type","mr")
                    }
                )
            } else {
                val i = Intent(requireActivity(), ListAllProjectManagementActivity::class.java).also {
                    it.putExtra("type","mr")
                }
                startActivity(i)
            }
        }

        binding.rlTurnOver.setOnClickListener {
            if (position == "BOD" || position == "CEO") {
                startActivity(
                    Intent(
                        requireActivity(),
                        ListBranchProjectManagementActivity::class.java
                    ).also {
                        it.putExtra("type","turnover")
                    }
                )
            } else {
                val i = Intent(requireActivity(), ListAllProjectManagementActivity::class.java).also {
                    it.putExtra("type","turnover")
                }
                startActivity(i)
            }
        }

        binding.linearTimesheet.setOnClickListener {
            if (position == "BOD" || position == "CEO") {
                startActivity(
                    Intent(
                        requireActivity(),
                        ListBranchProjectManagementActivity::class.java
                    ).also {
                        it.putExtra("type","timesheet")
                    }
                )
            } else {
                val i = Intent(requireActivity(), ListAllProjectManagementActivity::class.java).also {
                    it.putExtra("type","timesheet")
                }
                startActivity(i)
            }
        }

        binding.linearTimesheetBod.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    ListBranchProjectManagementActivity::class.java
                ).also {
                    it.putExtra("type","timesheet")
                }
            )
        }

        binding.linearMrBod.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    ListBranchProjectManagementActivity::class.java
                ).also {
                    it.putExtra("type","mr")
                }
            )
        }

        binding.linearMesinBod.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    ListBranchProjectManagementActivity::class.java
                ).also {
                    it.putExtra("type","machine")
                }
            )
        }

        binding.rlTurnOverBod.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    ListBranchProjectManagementActivity::class.java
                ).also {
                    it.putExtra("type","turnover")
                }
            )
        }

        binding.linearMesin.setOnClickListener {
            if (position == "BOD" || position == "CEO") {
                startActivity(
                    Intent(
                        requireActivity(),
                        ListBranchProjectManagementActivity::class.java
                    ).also {
                        it.putExtra("type","machine")
                    }
                )
            } else {
                val i = Intent(requireActivity(), ListAllProjectManagementActivity::class.java).also {
                    it.putExtra("type","machine")
                }
                startActivity(i)
            }
        }


        // menu project
        binding.llProject.setOnClickListener {
            if (position == "BOD" || position == "CEO") {
                startActivity(
                    Intent(
                        requireActivity(),
                        ListBranchProjectManagementActivity::class.java
                    )
                )
            } else {
                val i = Intent(requireActivity(), ListAllProjectManagementActivity::class.java)
                startActivity(i)
            }
        }

        // menu CFTalk
        binding.llCfTalk.setOnClickListener {
            startActivity(Intent(requireActivity(), CftalkManagementActivity::class.java))
        }
        binding.llCfTalkBod.setOnClickListener {
            startActivity(Intent(requireActivity(), ProjectsCftalkManagementActivity::class.java))
        }


    }

    private fun loadProfileDefault(img: String) {
        val url =
            getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imageResource = resources.getIdentifier(uri, null, requireActivity().packageName)
            val res = resources.getDrawable(imageResource)
            binding.ivFotoUserHomeVendor.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(requireActivity())
                .load(url)
                .apply(requestOptions)
                .into(binding.ivFotoUserHomeVendor)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun openDialogNewUpdate() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_custom_update_app)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val tvVersion = dialog.findViewById(R.id.tvDialogUpdateApp) as TextView
        val button = dialog.findViewById(R.id.btnDialogUpdateApp) as AppCompatButton

        homeViewModel.getNewVersionApp()
        homeViewModel.newVersionAppModel.observe(requireActivity()) {
            if (it.code == 200) {
                tvVersion.text = "versi terbaru ${it.data.versionRelease}"
            }
        }

        button.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=${requireActivity().packageName}")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${requireActivity().packageName}")
                    )
                )
            }
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                dialog.dismiss()
            }, 1500)
        }

        dialog.show()

    }


    private fun checkAllPermission() {
        val internetPermissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.INTERNET
        )
        val networkStatePermissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_NETWORK_STATE
        )
        val writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val coarseLocationPermissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val fineLocationPermissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val wifiStatePermissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_WIFI_STATE
        )
        val cameraPermissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        val notificationPermissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.POST_NOTIFICATIONS
        )

        if (cameraPermissionCheck == PackageManager.PERMISSION_GRANTED &&
            internetPermissionCheck == PackageManager.PERMISSION_GRANTED &&
            networkStatePermissionCheck == PackageManager.PERMISSION_GRANTED &&
            writeExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED &&
            coarseLocationPermissionCheck == PackageManager.PERMISSION_GRANTED &&
            fineLocationPermissionCheck == PackageManager.PERMISSION_GRANTED &&
            wifiStatePermissionCheck == PackageManager.PERMISSION_GRANTED &&
            notificationPermissionCheck == PackageManager.PERMISSION_GRANTED
        ) {
            checkLocationOnly()
        } else {
            Toast.makeText(requireActivity(), "Please allow permission first.", Toast.LENGTH_LONG)
                .show()

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.POST_NOTIFICATIONS
                ),
                MULTIPLE_PERMISSION_REQUEST_CODES
            )
        }
    }


    private fun checkLocationOnly() {
        val lm = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {

        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {

        }
        if (!gpsEnabled && !networkEnabled) {
            // notify user
            AlertDialog.Builder(requireActivity())
                .setMessage("GPS Tidak aktif")
                .setPositiveButton("Aktifkan",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        requireActivity().startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    })
                .setNegativeButton("Batal", null)
                .show()
        } else {
            val i = Intent(requireActivity(), AttendanceGeoManagementActivity::class.java)
            startActivity(i)
        }
    }



    //check gps
    private fun checkGPSAttendance() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(LocationRequest())
        val task = LocationServices.getSettingsClient(requireContext())
            .checkLocationSettings(builder.build())
        

        task
            .addOnSuccessListener { _ ->
                startActivity(Intent(requireContext(), AttendanceGeoManagementActivity::class.java))
            }
            .addOnFailureListener { e ->
                val statusCode = (e as ResolvableApiException).statusCode
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        e.startResolutionForResult(requireActivity(), 100)
                    } catch (sendEx: IntentSender.SendIntentException) {
                    }
                }
            }
    }


    //Notif subscribtion
    private fun fcmNotif(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener { task ->
            val msg = if (!task.isSuccessful) {
                "failed $topic"
            } else {
                "success $topic"
            }
            Log.d("fcmSubsManagementTag", msg)
        }
    }

    private fun fcmNotifUnsubs(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener { task ->
            val msg = if (!task.isSuccessful) {
                "failed $topic"
            } else {
                "success $topic"
            }
            Log.d("fcmUnsubsManagementTag", msg)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        } else {
            onSNACK(binding.root, "Tidak ada koneksi.")
            return true
        }
        return false
    }

    private fun onSNACK(view: View, str: String) {
        val snack = Snackbar.make(
            view, str,
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Error", null)
        snack.setActionTextColor(resources.getColor(R.color.primary_color))
        val snackView = snack.view
        snackView.setBackgroundColor(resources.getColor(R.color.primary_color))
        val textView =
            snackView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 14f
        snack.show()
    }

    override fun onResume() {
        super.onResume()
        loadData()
        setObserver()
        requestLocationUpdates()
    }

}