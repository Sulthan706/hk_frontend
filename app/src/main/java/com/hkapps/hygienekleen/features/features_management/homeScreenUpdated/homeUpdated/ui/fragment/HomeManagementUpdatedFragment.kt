package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
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
import androidx.appcompat.widget.AppCompatButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentHomeManagementUpdatedBinding
import com.hkapps.hygienekleen.features.facerecog.model.statsregisface.StatsRegisFaceResponseModel
import com.hkapps.hygienekleen.features.facerecog.ui.RegisterFaceRecogManagementActivity
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.activity.ViewPagerBakManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity.AttendanceGeoManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity.ExtendVisitDurationActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.CftalkManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.ProjectsCftalkManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.EditProfileManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeNewsManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.ListProjectManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.ui.adapter.ListUnvisitedManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.ui.adapter.ListUnvisitedBodAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.ui.adapter.ListUnvisitedTeknisiAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.viewmodel.HomeManagementUpdatedViewModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.activity.BranchesHumanCapitalActivity
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.activity.ListHumanCapitalActivity
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.activity.BranchesProjectManagementActivity
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.activity.ProjectsNewManagementActivity
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.activity.ChangeScheduleManagementActivity
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.ui.activity.VisitReportManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.ListBranchProjectManagementActivity
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.activity.ScheduleManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity.AttendanceOutManagementLivenessActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.activity.DailyClosingManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.viewmodel.ClosingManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.ListAllProjectManagementActivity
import com.hkapps.hygienekleen.features.features_management.shareloc.ui.activity.ShareLocManagementActivity
import com.hkapps.hygienekleen.features.features_management.shareloc.ui.activity.bod.MainListShareLocManagementActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeManagementUpdatedFragment : Fragment() {

    private lateinit var binding: FragmentHomeManagementUpdatedBinding
    private lateinit var rvUnvisitedBodAdapter: ListUnvisitedBodAdapter
    private lateinit var rvUnvisitedManagementAdapter: ListUnvisitedManagementAdapter
    private lateinit var rvUnvisitedTeknisiAdapter: ListUnvisitedTeknisiAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private val userNuc = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, "")
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)
    private val dateBirth = CarefastOperationPref.loadString(CarefastOperationPrefConst.BIRTHDAY_MANAGEMENT, "")
    private val imageBirth = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_IMAGE, "")
    private val idRkbOperation = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, 0)
    private val visitDurationStatus = CarefastOperationPref.loadString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")

    private var versionApp = ""
    private var oneTimeShow: Boolean = false
    private var date = ""
    var isLastPage = false
    var page = 0
    var isDialogOpened = false
    private var newMemberAndDataNotCompleted = ""
    private var position: String = ""
    private var oneShowPopUp: Boolean = false

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val homeUpdatedViewModel: HomeManagementUpdatedViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementUpdatedViewModel::class.java)
    }
    private val homeManagementViewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }
    private val faceRecogViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(FaceRecogViewModel::class.java)
    }
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private val closingViewModel : ClosingManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ClosingManagementViewModel::class.java)
    }

    private var flag = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeManagementUpdatedBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set appbar color white
        activity?.window!!.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.white)
        isOnline(requireActivity())

        binding.lltimesheetBod.setOnClickListener {
            if (isVp) {
//                    Toast.makeText(requireContext(), "feature coming soon", Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(
                        requireActivity(),
                        ListAllProjectManagementActivity::class.java
                    ).also {
                        it.putExtra("type", "timesheet")
                    }
                )
            } else {
                startActivity(
                    Intent(
                        requireActivity(),
                        ListBranchProjectManagementActivity::class.java
                    ).also {
                        it.putExtra("type", "timesheet")
                    }
                )
            }
        }

        binding.llTurnoverManagement.setOnClickListener {
            if (isVp) {
//                    Toast.makeText(requireContext(), "feature coming soon", Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(
                        requireActivity(),
                        ListAllProjectManagementActivity::class.java
                    ).also {
                        it.putExtra("type", "turnover")
                    }
                )
            } else {
                startActivity(
                    Intent(
                        requireActivity(),
                        ListBranchProjectManagementActivity::class.java
                    ).also {
                        it.putExtra("type", "turnover")
                    }
                )
            }
        }

        // set version app text
        val manager = requireActivity().packageManager
        val info = manager.getPackageInfo(requireActivity().packageName, 0)
        versionApp = info.versionName
        binding.tvVersionHome.text = versionApp

        // set on click button news
        binding.ivBtnHomeNewsManagement.setOnClickListener {
            startActivity(Intent(requireContext(), HomeNewsManagementActivity::class.java))
        }

        // set on click button notification
        binding.ivNotifHomeManagement.setOnClickListener {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.CLICK_FROM, "Home"
            )
            //ke fitur vendor
            val i = Intent(requireActivity(), ListProjectManagementActivity::class.java)
            startActivity(i)
        }

        // swipe refresh home screen
        binding.swipeHomeManagementUpdated.setOnRefreshListener {
            Handler().postDelayed(
                {
                    binding.swipeHomeManagementUpdated.isRefreshing = false
                    val i = Intent(requireActivity(), HomeManagementActivity::class.java)
                    startActivity(i)
                    requireActivity().finishAffinity()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        }

        // get current date
        val currentDate = Calendar.getInstance().time
        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)

        // validate menu home management
        if (levelPosition == 20) {
            validateLayoutMenuTeknisi()
        } else {
            if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                validateLayoutBodCeo()
            } else {
                validateLayoutMenuManagement()
            }
        }

        // set on click menu visit report & my schedule
        binding.llMyReportHomeManagementUpdated.setOnClickListener {
            startActivity(Intent(requireContext(), VisitReportManagementActivity::class.java))
        }
        binding.llMyScheduleHomeManagementUpdated.setOnClickListener {
            startActivity(Intent(requireContext(), ScheduleManagementActivity::class.java))
        }

        // show popup notif from visit duration
        if (levelPosition != 20 || userLevel != "BOD" || userLevel == "CEO" || !isVp) {
            if (idRkbOperation != 0) {
                when (visitDurationStatus) {
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
        }

        // request location update
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requestLocationUpdates()

        homeManagementViewModel.getCheckProfile(userId)
        homeManagementViewModel.checkRenewalManagement(userId)
        checkShowDialogEditProfile()
        lifecycleScope.launch {
            delay(1000)
            loadData()
            setObserverFace()
            setObserver()
            checkGps()
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

    private fun checkShowDialogEditProfile() {
        homeManagementViewModel.checkEditProfileModel.observe(requireActivity()) {
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

    // request location update
    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest().apply {
            interval =
                10000 // Interval in milliseconds receive location updates
            fastestInterval = 5000 // Fastest interval in milliseconds
            priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                android.location.LocationRequest.QUALITY_HIGH_ACCURACY
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
//                val address = getAddressFromCoordinates(context!!, latitude!!, longitude!!)
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
            ActivityCompat.finishAffinity(requireActivity())
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun observeCheckRenewal(){
        var isShowDialogGreeting = true
        homeManagementViewModel.checkRenewalManagementModel.observe(requireActivity()){
            if(it.errorCode == "01"){
                openDialogLastEditProfile(false)
            }
            if(it.errorCode != "01" && isShowDialogGreeting){
                isShowDialogGreeting = false
                greetingDialog()
            }
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
                homeManagementViewModel.updateLastManagementProfile(userId)
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

    private fun greetingDialog() {
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date())

        val endTimePagi = "10:59"
        val endTimeSiang = "14:59"
        val endTimeSore = "17:59"
        val endTimeMalam = "05:00"

        if (time < endTimePagi) {
            greetingsDialog("pagi")
        } else if (time < endTimeSiang) {
            greetingsDialog("siang")
        } else if (time < endTimeSore) {
            greetingsDialog("sore")
        } else if (time > endTimeMalam) {
            greetingsDialog("malam")
        }

        //validasi ultah management
        val sdfBirthday = SimpleDateFormat("dd-MMMM-yyyy")
        val timeBirth = sdfBirthday.format(Date())

        if (dateBirth == timeBirth) {
            greetingBirthday()
        }
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

    private fun greetingsDialog(greeting: String) {
        val view = when(greeting) {
            "pagi" -> View.inflate(requireActivity(), R.layout.dialog_greeting_pagi, null)
            "siang" -> View.inflate(requireActivity(), R.layout.dialog_greeting_siang, null)
            "sore" -> View.inflate(requireActivity(), R.layout.dialog_greeting_sore, null)
            "malam" -> View.inflate(requireActivity(), R.layout.dialog_greeting_malam, null)
            else -> null
        }
        val builder = android.app.AlertDialog.Builder(requireActivity())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = when(greeting) {
            "pagi" -> dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingPagi)
            "siang" -> dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingSiang)
            "sore" -> dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingSore)
            "malam" -> dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingMalam)
            else -> null
        }
        btnBack?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun observerUpdateLastProfile() {
        homeManagementViewModel.updateLastManagementModel.observe(requireActivity()){
            if(it.errorCode != null){
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun setObserver() {
        homeManagementViewModel.getCheckNewsManagementViewModel().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                showCustomSnackbarAtTop(requireView(), it.message)
                binding.ivNewNewsManagement.visibility = View.VISIBLE
            } else {
                binding.ivNewNewsManagement.visibility = View.GONE
            }
        }
        homeManagementViewModel.getProfileResponseManagementModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                position = it.data.levelJabatan

                // validate popup update profile
                if(it.data.lastUpdatedProfile == null && !isDialogOpened && newMemberAndDataNotCompleted.isBlank()){
                    isDialogOpened = true
                    openDialogLastEditProfile(true)
                }

                if(it.data.lastUpdatedProfile != null && !isDialogOpened && newMemberAndDataNotCompleted.isBlank()){
                    isDialogOpened = true
                    observeCheckRenewal()
                }

                // set user profile picture, name & nuc
                binding.tvNameHomeManagementUpdated.text = userName
                binding.tvNucHomeManagementUpdated.text = userNuc
                loadProfileDefault(it.data.adminMasterImage)

                // save parameter management position, image, placeOfBirth
                CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_JABATAN_MANAGEMENT,it.data.adminMasterJabatan)

                if (it.data.adminMasterImage.isNullOrEmpty()){
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.USER_IMAGE_MANAGEMENT,
                        ""
                    )
                } else {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.USER_IMAGE_MANAGEMENT,
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

                homeManagementViewModel.getProject(userId)
                homeViewModel.checkVersion(versionApp)
            } else {
                binding.tvNameHomeManagementUpdated.text = getString(R.string.user_name)
                binding.tvNucHomeManagementUpdated.text = getString(R.string.user_nuc)
                binding.ivUserHomeManagementUpdated.setImageDrawable(resources.getDrawable(R.drawable.ic_error_image))
                Toast.makeText(
                    requireActivity(),
                    "Gagal mengambil data profile",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        homeViewModel.checkVersionAppModel.observe(viewLifecycleOwner) {
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
        homeManagementViewModel.getProjectModel().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                when (position) {
                    "FM" -> {
                        for (projectCode in 0 until it.data.listProject.size) {

                            if (it.data.listProject[projectCode].status == "Y") {
//                                fcmNotif("Complaint_FM_" + it.data.listProject[projectCode].projectCode)
//                                fcmNotif("Complaint_Management_Client_" + it.data.listProject[projectCode].projectCode)

                                fcmNotif("Permission_From_Operator_" + it.data.listProject[projectCode].projectCode)

//                                fcmNotif("Complaint_Visitor_" + it.data.listProject[projectCode].projectCode)
                                fcmNotif("Resign_Approval_" + userLevel + "_" + it.data.listProject[projectCode].projectCode)

                                fcmNotif("rkb_visit_$userId")

                            } else {
//                                fcmNotifUnsubs("Complaint_FM_" + it.data.listProject[projectCode].projectCode)
//                                fcmNotifUnsubs("Complaint_Management_Client_" + it.data.listProject[projectCode].projectCode)

                                fcmNotifUnsubs("Permission_From_Operator_" + it.data.listProject[projectCode].projectCode)
//                                fcmNotifUnsubs("Complaint_Visitor_" + it.data.listProject[projectCode].projectCode)

                                fcmNotifUnsubs("Resign_Approval_" + userLevel + "_" + it.data.listProject[projectCode].projectCode)

                                fcmNotifUnsubs("rkb_visit_$userId")

                            }
                        }
                    }

                    "OM" -> {
                        for (projectCode in 0 until it.data.listProject.size) {
                            if (it.data.listProject[projectCode].status == "Y") {
//                                fcmNotif("Complaint_OM_" + it.data.listProject[projectCode].projectCode)
//                                fcmNotif("Complaint_Management_Client_" + it.data.listProject[projectCode].projectCode)

//                                fcmNotif("Complaint_Visitor_" + it.data.listProject[projectCode].projectCode)
                                fcmNotif("Resign_Approval_" + userLevel + "_" + it.data.listProject[projectCode].projectCode)

                                fcmNotif("rkb_visit_$userId")
                            } else {
//                                fcmNotifUnsubs("Complaint_OM_" + it.data.listProject[projectCode].projectCode)
//                                fcmNotifUnsubs("Complaint_Management_Client_" + it.data.listProject[projectCode].projectCode)

//                                fcmNotifUnsubs("Complaint_Visitor_" + it.data.listProject[projectCode].projectCode)
                                fcmNotifUnsubs("Resign_Approval_" + userLevel + "_" + it.data.listProject[projectCode].projectCode)

                                fcmNotifUnsubs("rkb_visit_$userId")
                            }
                        }
                    }

                    "GM" -> {
                        for (projectCode in 0 until it.data.listProject.size) {
                            if (it.data.listProject[projectCode].status == "Y") {
//                                fcmNotif("Complaint_GM_" + it.data.listProject[projectCode].projectCode)
//                                fcmNotif("Complaint_Management_Client_" + it.data.listProject[projectCode].projectCode)
//
//                                fcmNotif("Complaint_Visitor_" + it.data.listProject[projectCode].projectCode)
                                fcmNotif("Resign_Approval_" + userLevel + "_" + it.data.listProject[projectCode].projectCode)

                                fcmNotif("rkb_visit_$userId")
                            } else {
//                                fcmNotifUnsubs("Complaint_GM_" + it.data.listProject[projectCode].projectCode)
//                                fcmNotifUnsubs("Complaint_Management_Client_" + it.data.listProject[projectCode].projectCode)
//
//                                fcmNotifUnsubs("Complaint_Visitor_" + it.data.listProject[projectCode].projectCode)
                                fcmNotifUnsubs("Resign_Approval_" + userLevel + "_" + it.data.listProject[projectCode].projectCode)

                                fcmNotifUnsubs("rkb_visit_$userId")
                            }
                            println("getproject " + it.data.listProject[projectCode].projectCode)
                        }
                    }

                    "BOD" -> {
//                        fcmNotif("Complaint_BOD-CEO")
                        fcmNotif("rkb_visit_$userId")
                    }

                    "CEO" -> {
//                        fcmNotif("Complaint_BOD-CEO")
                        fcmNotif("rkb_visit_$userId")
                    }

                }
                if (isVp) {
//                    fcmNotif("Complaint_BOD-CEO")
                    fcmNotif("rkb_visit_$userId")
                }
            }
        }
        homeManagementViewModel.attendanceFeatureAvailabilityModel.observe(viewLifecycleOwner) {
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

    private fun loadProfileDefault(img: String) {
        val url =
            getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

        Log.d("TESTED",url)

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imageResource = resources.getIdentifier(uri, null, requireActivity().packageName)
            val res = resources.getDrawable(imageResource)
            binding.ivUserHomeManagementUpdated.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(requireActivity())
                .load(url)
                .apply(requestOptions)
                .into(binding.ivUserHomeManagementUpdated)
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

    private fun loadData() {
        homeManagementViewModel.getProfileManagement(userId)
        homeManagementViewModel.getCheckNewsManagement(userType = "Management", userId)
    }

    @SuppressLint("SetTextI18n")
    private fun showBottomUnvisitedProject(managementLevel: String) {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.bottom_sheet_unvisited_project_management)
//        dialog.setCancelable(false)

        val tvDate = dialog.findViewById<TextView>(R.id.tvDateBottomUnvisitedProject)
        val tvRemainingProject = dialog.findViewById<TextView>(R.id.tvRemainingBottomUnvisitedProject)
        val rvProject = dialog.findViewById<RecyclerView>(R.id.rvBottomUnvisitedProject)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnBottomUnvisitedProject)

        // set on click button change plan
        button?.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(requireContext(), ChangeScheduleManagementActivity::class.java))
        }

        // set recycler view
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvProject?.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    when (managementLevel) {
                        "bodCeo" -> homeUpdatedViewModel.getListRemainingVisitBod(userId, date, page, 10)
                        "management" -> homeUpdatedViewModel.getListRemainingVisitManagement(userId, date, page, 10)
                        "teknisi" -> homeUpdatedViewModel.getListRemainingVisitTeknisi(userId, date, page, 10)
                    }
                }
            }
        }
        rvProject?.addOnScrollListener(scrollListener)

        // get current date
        val currentDate = Calendar.getInstance().time
        val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(currentDate)
        tvDate?.text = "Today, $date"

        when(managementLevel) {
            "bodCeo" -> {
                homeUpdatedViewModel.getListRemainingVisitBod(userId, this.date, 0, 10)
                homeUpdatedViewModel.listRemainingVisitBodResponse.observe(viewLifecycleOwner) {
                    if (it.code == 200) {
                        // set total remaining
                        tvRemainingProject?.text = "${it.data.totalRemaining} project"

                        // set list remaining
                        if (it.data.listScheduleBODDetail.content.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "Tidak ada data list unvisited project",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            isLastPage = it.data.listScheduleBODDetail.last
                            if (page == 0) {
                                rvUnvisitedBodAdapter = ListUnvisitedBodAdapter(
                                    it.data.listScheduleBODDetail.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitBod.Content>
                                )
                                rvProject?.adapter = rvUnvisitedBodAdapter
                            } else {
                                rvUnvisitedBodAdapter.listSchedule.addAll(it.data.listScheduleBODDetail.content)
                                rvUnvisitedBodAdapter.notifyItemRangeChanged(
                                    rvUnvisitedBodAdapter.listSchedule.size - it.data.listScheduleBODDetail.content.size,
                                    rvUnvisitedBodAdapter.listSchedule.size
                                )
                            }
                        }
                    } else {
                        tvRemainingProject?.text = "error"
                        rvProject?.adapter = null

                        Toast.makeText(
                            requireContext(),
                            "Gagal mengambil data list unvisited project",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            "management" -> {
                homeUpdatedViewModel.getListRemainingVisitManagement(userId, this.date, 0, 10)
                homeUpdatedViewModel.listRemainingVisitManagementResponse.observe(viewLifecycleOwner) {
                    if (it.code == 200) {
                        // set total remaining
                        tvRemainingProject?.text = "${it.data.totalRemaining}"

                        // set list remaining
                        if (it.data.listScheduleManagementDTO.content.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "Tidak ada data list unvisited project",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            isLastPage = it.data.listScheduleManagementDTO.last
                            if (page == 0) {
                                rvUnvisitedManagementAdapter = ListUnvisitedManagementAdapter(
                                    it.data.listScheduleManagementDTO.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitManagement.Content>
                                )
                                rvProject?.adapter = rvUnvisitedManagementAdapter
                            } else {
                                rvUnvisitedManagementAdapter.listSchedule.addAll(it.data.listScheduleManagementDTO.content)
                                rvUnvisitedManagementAdapter.notifyItemRangeChanged(
                                    rvUnvisitedManagementAdapter.listSchedule.size - it.data.listScheduleManagementDTO.content.size,
                                    rvUnvisitedManagementAdapter.listSchedule.size
                                )
                            }
                        }
                    } else {
                        tvRemainingProject?.text = "error"
                        rvProject?.adapter = null

                        Toast.makeText(
                            requireContext(),
                            "Gagal mengambil data list unvisited project",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            "teknisi" -> {
                homeUpdatedViewModel.getListRemainingVisitTeknisi(userId, this.date, 0, 10)
                homeUpdatedViewModel.listRemainingVisitTeknisiResponse.observe(viewLifecycleOwner) {
                    if (it.code == 200) {
                        // set total remaining
                        tvRemainingProject?.text = "${it.data.totalRemaining}"

                        // set list remaining
                        if (it.data.listScheduleTeknisiVisit.content.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "Tidak ada data list unvisited project",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            isLastPage = it.data.listScheduleTeknisiVisit.last
                            if (page == 0) {
                                rvUnvisitedTeknisiAdapter = ListUnvisitedTeknisiAdapter(
                                    it.data.listScheduleTeknisiVisit.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitTeknisi.Content>
                                )
                                rvProject?.adapter = rvUnvisitedTeknisiAdapter
                            } else {
                                rvUnvisitedTeknisiAdapter.listSchedule.addAll(it.data.listScheduleTeknisiVisit.content)
                                rvUnvisitedTeknisiAdapter.notifyItemRangeChanged(
                                    rvUnvisitedTeknisiAdapter.listSchedule.size - it.data.listScheduleTeknisiVisit.content.size,
                                    rvUnvisitedTeknisiAdapter.listSchedule.size
                                )
                            }
                        }
                    } else {
                        tvRemainingProject?.text = "error"
                        rvProject?.adapter = null

                        Toast.makeText(
                            requireContext(),
                            "Gagal mengambil data list unvisited project",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


        dialog.show()
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

    private fun cameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 101)
        }
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
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
                startActivity(Intent(requireContext(), AttendanceOutManagementLivenessActivity::class.java))
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun validateLayoutMenuManagement() {
        loadDataManagement()
        setObserverManagement()

        binding.clMenuBodCeoHomeManagementUpdated.visibility = View.GONE
        binding.llMenuManagementHomeUpdated.visibility = View.VISIBLE
        binding.llMenuTeknisiHomeManagementUpdated.visibility = View.GONE
        binding.linearAlatMr.visibility = View.VISIBLE

        // set on click menu
        binding.llAttendanceManagement.setOnClickListener {
            // check version feature attendance api
            homeManagementViewModel.getAttendanceFeature(userId)
            // face recog
//            checkFaceAndProfile(userId)
        }
        binding.llProjectManagement.setOnClickListener {
            startActivity(Intent(requireActivity(), ProjectsNewManagementActivity::class.java))
        }
        binding.llHumanCapitalManagement.setOnClickListener {
            startActivity(Intent(requireActivity(), ListHumanCapitalActivity::class.java))
        }
        binding.llCftalkManagement.setOnClickListener {
            startActivity(Intent(requireActivity(), CftalkManagementActivity::class.java))
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setObserverManagement() {
        homeUpdatedViewModel.rkbHomeManagementResponse.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if (it.data.planningTarget == 0) {
                    binding.clDailyVisitHomeManagementUpdated.visibility = View.GONE
                    binding.clLastVisitHomeManagementUpdated.visibility = View.GONE

                    binding.tvInfoVisitHomeManagementUpdated.text = "You have no visits planned for today"
                } else {
                    binding.clDailyVisitHomeManagementUpdated.visibility = View.VISIBLE
                    binding.clLastVisitHomeManagementUpdated.visibility = View.VISIBLE

                    // set data visit planned
                    binding.tvInfoVisitHomeManagementUpdated.text = "You have visits planned for today"

                    binding.tvTargetHomeManagementUpdated.text = "Daily Target: ${it.data.dailyTarget} visits"
                    binding.tvPlanningHomeManagementUpdated.text = "Planning: ${it.data.totalJadwal} visits"
                    binding.tvTotRealizationHomeManagementUpdated.text = "${it.data.realisasiTercapai}"
                    binding.tvTotPlanningnHomeManagementUpdated.text = "/${it.data.totalJadwal}"
                    binding.tvPercentageHomeManagementUpdated.text = if (it.data.realizationInPercent == 0.0) {
                        "0%"
                    } else {
                        "${it.data.realizationInPercent}%"
                    }
                    binding.progressBarHomeManagementUpdated.setProgressPercentage(it.data.realizationInPercent)

                    binding.tvUnvisitedPlanHomeManagementUpdated.setOnClickListener {
                        showBottomUnvisitedProject("management")
                    }
                }
            } else {
                binding.clDailyVisitHomeManagementUpdated.visibility = View.GONE
                binding.clLastVisitHomeManagementUpdated.visibility = View.GONE

                binding.tvInfoVisitHomeManagementUpdated.text = "Error"

                Toast.makeText(
                    requireContext(),
                    "Gagal mengambil data visit planned",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        homeUpdatedViewModel.lastVisitHomeManagementResponse.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                binding.tvProjectHomeManagementUpdated.text = it.data.lastVisit.projectName ?: "No visit yet"
                binding.tvCheckInHomeManagementUpdated.text = it.data.lastVisit.checkIn ?: "--:--"
                binding.tvCheckOutHomeManagementUpdated.text = it.data.lastVisit.checkOut ?: "--:--"
            } else {
                binding.tvProjectHomeManagementUpdated.text = "error"
                binding.tvCheckInHomeManagementUpdated.text = "error"
                binding.tvCheckOutHomeManagementUpdated.text = "error"

                Toast.makeText(requireContext(), "Gagal mengambil data last visit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadDataManagement() {
        homeUpdatedViewModel.getRkbManagementHome(userId, date)
        homeUpdatedViewModel.getLastVisitManagement(userId, date)
    }

    private fun validateLayoutBodCeo() {
        loadDataBodCeo()
        setObserverBodCeo()

        binding.clMenuBodCeoHomeManagementUpdated.visibility = View.VISIBLE
        binding.llMenuManagementHomeUpdated.visibility = View.GONE
        binding.llMenuTeknisiHomeManagementUpdated.visibility = View.GONE

        // validate layout menu pak zul
        if (userId == 70) {
            binding.llTimesheetBodCeo.visibility = View.VISIBLE
            binding.llMenu3BodCeo.visibility = View.VISIBLE

            // set on click menu


        } else {
            binding.llTimesheetBodCeo.visibility = View.INVISIBLE
            binding.llMenu3BodCeo.visibility = View.VISIBLE

            binding.llTimesheetBodCeo.setOnClickListener {
                if (isVp) {
//                    Toast.makeText(requireContext(), "feature coming soon", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(
                            requireActivity(),
                            ListAllProjectManagementActivity::class.java
                        ).also {
                            it.putExtra("type", "timesheet")
                        }
                    )
                } else {
                    startActivity(
                        Intent(
                            requireActivity(),
                            ListBranchProjectManagementActivity::class.java
                        ).also {
                            it.putExtra("type", "timesheet")
                        }
                    )
                }
            }
            binding.llTurnoverBodCeo.setOnClickListener {
                if (isVp) {
//                    Toast.makeText(requireContext(), "feature coming soon", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(
                            requireActivity(),
                            ListAllProjectManagementActivity::class.java
                        ).also {
                            it.putExtra("type", "turnover")
                        }
                    )
                } else {
                    startActivity(
                        Intent(
                            requireActivity(),
                            ListBranchProjectManagementActivity::class.java
                        ).also {
                            it.putExtra("type", "turnover")
                        }
                    )
                }
            }
            binding.llAlatBod.setOnClickListener {
                if (isVp) {
//                    Toast.makeText(requireContext(), "feature coming soon", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(
                            requireActivity(),
                            ListAllProjectManagementActivity::class.java
                        ).also {
                            it.putExtra("type", "machine")
                        }
                    )
                } else {
                    startActivity(
                        Intent(
                            requireActivity(),
                            ListBranchProjectManagementActivity::class.java
                        ).also {
                            it.putExtra("type", "machine")
                        }
                    )
                }
            }
            binding.llMrBod.setOnClickListener {
                if (isVp) {
//                    Toast.makeText(requireContext(), "feature coming soon", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(
                            requireActivity(),
                            ListAllProjectManagementActivity::class.java
                        ).also {
                            it.putExtra("type", "mr")
                        }
                    )
                } else {
                    startActivity(
                        Intent(
                            requireActivity(),
                            ListBranchProjectManagementActivity::class.java
                        ).also {
                            it.putExtra("type", "mr")
                        }
                    )
                }
            }
        }

        // set on click menu
        binding.llAttendanceBodCeo.setOnClickListener {
            // face recog
            checkFaceAndProfile(userId)
        }
        binding.llProjectBodCeo.setOnClickListener {
            if (isVp) {
                startActivity(Intent(requireActivity(), ProjectsNewManagementActivity::class.java))
            } else {
                startActivity(Intent(requireActivity(), BranchesProjectManagementActivity::class.java))
            }
        }
        binding.llHumanCapitalBodCeo.setOnClickListener {
            // coming soon updated layout
            if (isVp) {
                startActivity(Intent(requireActivity(), ListHumanCapitalActivity::class.java))
            } else {
                startActivity(Intent(requireActivity(), BranchesHumanCapitalActivity::class.java))
            }
        }
        binding.llManagementBodCeo.setOnClickListener {
            // coming soon updated layout
            Toast.makeText(requireContext(), "feature coming soon", Toast.LENGTH_SHORT).show()
        }
        binding.llCftalkBodCeo.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    ProjectsCftalkManagementActivity::class.java
                )
            )
        }
        binding.llBranchBodCeo.setOnClickListener {
            // coming soon updated layout
            Toast.makeText(requireContext(), "feature coming soon", Toast.LENGTH_SHORT).show()
        }
        binding.llRealtimeAttendBodCeo.setOnClickListener {
//            if (isVp) {
//                Toast.makeText(requireContext(), "feature coming soon", Toast.LENGTH_SHORT).show()
//            } else {
//                checkGpsShareLoc()
//            }
            checkGpsShareLoc()
        }

    }

    private fun checkGpsShareLoc() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(LocationRequest())
        val task = LocationServices.getSettingsClient(requireContext())
            .checkLocationSettings(builder.build())

        task
            .addOnSuccessListener { _ ->
                if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
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

    private fun checkFaceAndProfile(userId: Int) {
        // Trigger the profile check
        homeManagementViewModel.getCheckProfile(userId)
        // Observe the profile response
        homeManagementViewModel.checkEditProfileModel.observe(viewLifecycleOwner) { profileResponse ->
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


    @SuppressLint("SetTextI18n")
    private fun setObserverBodCeo() {
        homeUpdatedViewModel.rkbHomeBodResponse.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if (it.data.planningTarget == 0) {
                    binding.clDailyVisitHomeManagementUpdated.visibility = View.GONE
                    binding.clLastVisitHomeManagementUpdated.visibility = View.GONE

                    binding.tvInfoVisitHomeManagementUpdated.text = "You have no visits planned for today"
                } else {
                    binding.clDailyVisitHomeManagementUpdated.visibility = View.VISIBLE
                    binding.clLastVisitHomeManagementUpdated.visibility = View.VISIBLE

                    // set data visit planned
                    binding.tvInfoVisitHomeManagementUpdated.text = "You have visits planned for today"

                    binding.tvTargetHomeManagementUpdated.text = "Daily Target: ${it.data.dailyTarget} visits"
                    binding.tvPlanningHomeManagementUpdated.text = "Planning: ${it.data.totalJadwal} visits"
                    binding.tvTotRealizationHomeManagementUpdated.text = "${it.data.realisasiTercapai}"
                    binding.tvTotPlanningnHomeManagementUpdated.text = "/${it.data.totalJadwal}"
                    binding.tvPercentageHomeManagementUpdated.text = if (it.data.realizationInPercent == 0.0) {
                        "0%"
                    } else {
                        "${it.data.realizationInPercent}%"
                    }
                    binding.progressBarHomeManagementUpdated.setProgressPercentage(it.data.realizationInPercent)

                    binding.tvUnvisitedPlanHomeManagementUpdated.setOnClickListener {
                        showBottomUnvisitedProject("bodCeo")
                    }
                }
            } else {
                binding.clDailyVisitHomeManagementUpdated.visibility = View.GONE
                binding.clLastVisitHomeManagementUpdated.visibility = View.GONE

                binding.tvInfoVisitHomeManagementUpdated.text = "Error"

                Toast.makeText(
                    requireContext(),
                    "Gagal mengambil data visit planned",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        homeUpdatedViewModel.lastVisitHomeBodResponse.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                binding.tvProjectHomeManagementUpdated.text = it.data.lastVisit.projectName ?: "No visit yet"
                binding.tvCheckInHomeManagementUpdated.text = it.data.lastVisit.checkIn ?: "--:--"
                binding.tvCheckOutHomeManagementUpdated.text = it.data.lastVisit.checkOut ?: "--:--"
            } else {
                binding.tvProjectHomeManagementUpdated.text = "error"
                binding.tvCheckInHomeManagementUpdated.text = "error"
                binding.tvCheckOutHomeManagementUpdated.text = "error"

                Toast.makeText(requireContext(), "Gagal mengambil data last visit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadDataBodCeo() {
        homeUpdatedViewModel.getRkbBodHome(userId, date)
        homeUpdatedViewModel.getLastVisitBod(userId, date)
    }

    private fun validateLayoutMenuTeknisi() {
        loadDataTeknisi()
        setObserverTeknisi()

        binding.clMenuBodCeoHomeManagementUpdated.visibility = View.GONE
        binding.llMenuManagementHomeUpdated.visibility = View.GONE
        binding.llMenuTeknisiHomeManagementUpdated.visibility = View.VISIBLE

        // set on click menu
        binding.llAttendanceTeknisi.setOnClickListener {
            // check version feature attendance api
            homeManagementViewModel.getAttendanceFeature(userId)
            // face recog
//            checkFaceAndProfile(userId)
        }
        binding.llProjectTeknisi.setOnClickListener {
            startActivity(Intent(requireActivity(), ProjectsNewManagementActivity::class.java))
        }
        binding.llBakMesinTeknisi.setOnClickListener {
            startActivity(Intent(requireContext(), ViewPagerBakManagementActivity::class.java))
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setObserverTeknisi() {
        homeUpdatedViewModel.rkbHomeTeknisiResponse.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if (it.data.planningTarget == 0) {
                    binding.clDailyVisitHomeManagementUpdated.visibility = View.GONE
                    binding.clLastVisitHomeManagementUpdated.visibility = View.GONE

                    binding.tvInfoVisitHomeManagementUpdated.text = "You have no visits planned for today"
                } else {
                    binding.clDailyVisitHomeManagementUpdated.visibility = View.VISIBLE
                    binding.clLastVisitHomeManagementUpdated.visibility = View.VISIBLE

                    // set data visit planned
                    binding.tvInfoVisitHomeManagementUpdated.text = "You have visits planned for today"

                    binding.tvTargetHomeManagementUpdated.text = "Daily Target: ${it.data.dailyTarget} visits"
                    binding.tvPlanningHomeManagementUpdated.text = "Planning: ${it.data.totalJadwal} visits"
                    binding.tvTotRealizationHomeManagementUpdated.text = "${it.data.realisasiTercapai}"
                    binding.tvTotPlanningnHomeManagementUpdated.text = "/${it.data.totalJadwal}"
                    binding.tvPercentageHomeManagementUpdated.text = if (it.data.realizationInPercent == 0.0) {
                        "0%"
                    } else {
                        "${it.data.realizationInPercent}%"
                    }
                    binding.progressBarHomeManagementUpdated.setProgressPercentage(it.data.realizationInPercent)

                    binding.tvUnvisitedPlanHomeManagementUpdated.setOnClickListener {
                        showBottomUnvisitedProject("teknisi")
                    }
                }
            } else {
                binding.clDailyVisitHomeManagementUpdated.visibility = View.GONE
                binding.clLastVisitHomeManagementUpdated.visibility = View.GONE

                binding.tvInfoVisitHomeManagementUpdated.text = "Error"

                Toast.makeText(
                    requireContext(),
                    "Gagal mengambil data visit planned",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        homeUpdatedViewModel.lastVisitHomeTeknisiResponse.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                binding.tvProjectHomeManagementUpdated.text = it.data.lastVisit.projectName ?: "No visit yet"
                binding.tvCheckInHomeManagementUpdated.text = it.data.lastVisit.checkIn ?: "--:--"
                binding.tvCheckOutHomeManagementUpdated.text = it.data.lastVisit.checkOut ?: "--:--"
            } else {
                binding.tvProjectHomeManagementUpdated.text = "error"
                binding.tvCheckInHomeManagementUpdated.text = "error"
                binding.tvCheckOutHomeManagementUpdated.text = "error"

                Toast.makeText(requireContext(), "Gagal mengambil data last visit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadDataTeknisi() {
        homeUpdatedViewModel.getRkbTeknisiHome(userId, date)
        homeUpdatedViewModel.getLastVisitTeknisi(userId, date)
    }

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
            onSNACK(binding.root, "No internet connection")
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

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        loadData()
        setObserver()
        requestLocationUpdates()

        // refresh data visit project
        if (levelPosition == 20) {
            loadDataTeknisi()
            setObserverTeknisi()
        } else {
            if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                loadDataBodCeo()
                setObserverBodCeo()
            } else {
                loadDataManagement()
                setObserverManagement()
            }
        }
    }

}