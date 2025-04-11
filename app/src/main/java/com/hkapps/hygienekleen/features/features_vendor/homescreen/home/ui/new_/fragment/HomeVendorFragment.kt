package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.location.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.databinding.FragmentHomeVendorBinding
import com.hkapps.hygienekleen.features.facerecog.model.statsregisface.StatsRegisFaceResponseModel
import com.hkapps.hygienekleen.features.facerecog.ui.RegisterFaceRecogActivity
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.ui.activity.ListBakMachineActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.AttendanceFixHistoryActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.new_.AttendanceLowGeoLocationOSM
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_.AttendanceMidGeoLocationOSMNew
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity.ListShiftChecklistActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.dacHome.DailyActHomeResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeNewsActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.MRActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.HomeDacAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.StatusAbsenViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.ui.activity.lowlevel.ScheduleActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.ui.activity.midlevel.ScheduleMidActivity
import com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.new_.activity.NotifMidActivity
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.new_.activity.DashboardComplaintVendorActivity
import com.hkapps.hygienekleen.features.grafik.ui.GrafikActivity
import com.hkapps.hygienekleen.features.grafik.ui.TurnOverActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import ru.nikartm.support.BadgePosition
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.hkapps.hygienekleen.R


class HomeVendorFragment : Fragment() {

    lateinit var binding: FragmentHomeVendorBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode: String = ""
    private var userName: String = ""
    private var userNuc: String = ""
    private var userPosition: String = ""
    private var levelPosition: String = ""
    private var userProject: String = ""
    private var versionApp: String = ""
    private lateinit var dailyHomeResponseModel: DailyActHomeResponseModel
    private lateinit var dailyActAdapterHome: HomeDacAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var rvSkeleton: Skeleton

    private val userLevelPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private var loadingDialog: Dialog? = null
    private var oneTimeShow: Boolean = false
    private var oneTimeGreeting: Boolean = false
    var pass: String = ""



    companion object {
        private const val REQUEST_WRITE_STORAGE = 1

        private val globalArrayList = ArrayList<String>()

        fun getGlobalArrayList(): ArrayList<String> {
            return globalArrayList
        }

        fun setGlobalArrayList(items: ArrayList<String>) {
            globalArrayList.clear()
            globalArrayList.addAll(items.distinct())
        }
    }
    var complaintType: String = ""

    private val profileViewModel: HomeViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(HomeViewModel::class.java)
    }
    private val statusAbsenViewModel: StatusAbsenViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(StatusAbsenViewModel::class.java)
    }
    private val faceRecogViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(FaceRecogViewModel::class.java)
    }
    var error = 0
    private var reqProfile =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.REQPROFILE, false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeVendorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rlMr.setOnClickListener {
            startActivity(Intent(requireContext(),MRActivity::class.java))
        }
        binding.rlBak.setOnClickListener {
            startActivity(Intent(requireContext(), ListBakMachineActivity::class.java))
        }
        binding.rlTimesheet.setOnClickListener {
            startActivity(Intent(requireContext(), GrafikActivity::class.java).also {
                it.putExtra("type","timesheet")
            })
        }

        binding.rlComplaint2.setOnClickListener {
            val i = Intent(context, ScheduleMidActivity::class.java)
            startActivity(i)
        }

        binding.rlRkb.setOnClickListener {
            startActivity(Intent(requireActivity(),ListShiftChecklistActivity::class.java))
        }

        binding.rlReliver.setOnClickListener {
            Toast.makeText(requireActivity(), "Coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.rlTurnOver.setOnClickListener {
            startActivity(Intent(requireContext(),TurnOverActivity::class.java))
        }


        // default layout
        binding.linearLayout6.visibility = View.GONE
        binding.textView4.visibility = View.GONE
        binding.llDacHome.visibility = View.GONE
        binding.tvUnknownUserLevel.visibility = View.GONE

        // get version app & set text version
        val manager = requireActivity().packageManager
        val info = manager.getPackageInfo(requireActivity().packageName, 0)
        versionApp = info.versionName
        binding.tvVersionHome.text = versionApp

        // refresh home
        binding.swipeHome.setOnRefreshListener(OnRefreshListener {
            Handler().postDelayed(
                {
                    binding.swipeHome.isRefreshing = false
                    val i = Intent(requireActivity(), HomeVendorActivity::class.java)
                    startActivity(i)
                    requireActivity().finishAffinity()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })

        // change status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(requireContext())
            activity?.window!!.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.white)
        }


//        binding.ivQuestionLocation.setOnClickListener {
//            BotSheetQuestionFragment().show(requireFragmentManager(), "sad")
//        }
//        //address latlong from activity
//        (activity as? HomeVendorActivity)?.liveLocationAddress?.observe(viewLifecycleOwner, Observer { value ->
//            binding.tvRealtimeLocation.text = value
//        })

        loadData()
        setObserver()
        setObserverDacHome()
        getBadgeNotif()
        loadDataComplaintValidate()


        if (checkPermission()) {
            createFunDirectory()
        } else {
            requestPermission()
        }

        //learn


        //call timeshift

        //oncreate
    }

    private fun loadDataComplaintValidate() {
        val items = getGlobalArrayList()
        items.clear()
        items.add("COMPLAINT_CLIENT")
        items.add("COMPLAINT_MANAGEMENT_CLIENT")
        items.add("COMPLAINT_VISITOR")
        complaintType = globalArrayList.joinToString(",")
        val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
        profileViewModel.getComplaintValidate(projectCode, items)

        profileViewModel.complaintValidateModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                when(it.message) {
                    "EXISTS" -> {
                        binding.rlComplaint.setBackgroundResource(R.drawable.bg_red)
                        binding.ivComplaint.setImageResource(R.drawable.complaint_white)
                    }
                    "NOT_EXISTS" -> {
                        binding.rlComplaint.setBackgroundResource(R.drawable.bg_white_card)
                        binding.ivComplaint.setImageResource(R.drawable.complain_new)
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Gagal mengambil data validasi cTalk",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkGps(){
        val builder = LocationSettingsRequest.Builder().addLocationRequest(LocationRequest())
        val task = LocationServices.getSettingsClient(requireContext()).checkLocationSettings(builder.build())

        task
            .addOnSuccessListener { response ->
                if (levelPosition == "Operator") {
                    val i = Intent(requireActivity(), AttendanceLowGeoLocationOSM::class.java)
                    startActivity(i)
                } else {
                    val i = Intent(requireActivity(), AttendanceMidGeoLocationOSMNew::class.java)
                    startActivity(i)
                }
            }
            .addOnFailureListener { e ->
                val statusCode = (e as ResolvableApiException).statusCode
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                    try {
                        e.startResolutionForResult(requireActivity(), 100)
                    } catch (sendEx: IntentSender.SendIntentException){ }
                }
            }
    }
    private fun checkPermission(): Boolean {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val granted = PackageManager.PERMISSION_GRANTED
        return ContextCompat.checkSelfPermission(requireContext(), permission) == granted
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_WRITE_STORAGE
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createFunDirectory()
            } else {
                Log.d("agri","dibuat")
            }
        }
    }


    private fun createFunDirectory() {
        val directoryName = "FunDirectory"
        val directory = File(Environment.getExternalStorageDirectory(), directoryName)

        if (directory.exists()) {
            Log.d("agri","dibuat")
        } else {
            val success = directory.mkdirs()
            if (success) {
                Log.d("agri","dibuat")

            } else {
                Log.d("agri","dibuat")

            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setHomeScreen() {
        when (levelPosition) {
            "Operator" -> {
                getStatusLowLevel()

                binding.tvDacOrChecklist.text = "DAC"
                profileViewModel.getDacCount(userId, projectCode)

                binding.llComplaint.visibility = View.GONE
                binding.llAbsentStaff.visibility = View.GONE

                // geo location attendance
                binding.llAttendance.setOnClickListener {
//                    checkAllPermission()
//                    checkLocation()
                    //face recog
                    Log.d("agri","$reqProfile")
                    checkFaceAndProfile(userId)
//                    checkUserFace(userNuc)
                }

                // menu jadwal
                binding.llScheduleOpr.visibility = View.VISIBLE
                binding.llComplaint2.visibility = View.GONE
                binding.linearLayout7.visibility = View.GONE
                binding.linearLayout8.visibility = View.GONE
                binding.rlScanBarcode.visibility = View.VISIBLE
                binding.textBarcode.visibility = View.VISIBLE
                binding.llScheduleOpr.setOnClickListener {
                    val i = Intent(context, ScheduleActivity::class.java)
                    startActivity(i)
                }

                // menu history absen
                binding.llHistory.visibility = View.VISIBLE
                binding.llHistory.setOnClickListener {
                    val i = Intent(context, AttendanceFixHistoryActivity::class.java)
                    requireActivity().startActivity(i)
                }

                // menu notifikasi
                binding.ivNotifHomeVendor.setOnClickListener {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.CLICK_FROM, "Home"
                    )
                    val i = Intent(requireContext(), NotifMidActivity::class.java)
                    startActivity(i)
                }
                //news
                binding.ivBtnHomeNews.setOnClickListener {
                    startActivity(Intent(requireActivity(), HomeNewsActivity::class.java))
                }
                //barcode
                binding.rlScanBarcode.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llScanBarcode.setOnClickListener {
//                    startActivity(Intent(requireActivity(), ScanBarcodeActivity::class.java))
                    Toast.makeText(
                        requireActivity(),
                        "Fitur dalam tahap pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            "Team Leader" -> {
                binding.llScheduleOpr.visibility = View.GONE
                binding.llHistory.visibility = View.GONE

                binding.tvDacOrChecklist.text = "Checklist"
                profileViewModel.getChecklistCount(projectCode)

                // geo location attendance
                binding.llAttendance.setOnClickListener {
//                    checkAllPermission()
//                    checkLocation()
                    //face recog
                    checkFaceAndProfile(userId)
//                    checkUserFace(userNuc)


                }

                // menu ctalk
                binding.llComplaint.visibility = View.VISIBLE
                binding.llComplaint.setOnClickListener {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.NOTIF_INTENT,
                        "home"
                    )
                    val i = Intent(requireActivity(), DashboardComplaintVendorActivity::class.java)
                    startActivity(i)
                }

                // menu absen staff
                binding.llAbsentStaff.visibility = View.VISIBLE
                binding.llAbsentStaff.setOnClickListener {
//                    val i = Intent(requireActivity(), NotAbsentOperatorActivity::class.java)
                    val i = Intent(requireActivity(), GrafikActivity::class.java)
                    startActivity(i)
                }

                // menu notifikasi
                binding.ivNotifHomeVendor.setOnClickListener {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.CLICK_FROM, "Home"
                    )
                    val i = Intent(requireActivity(), NotifMidActivity::class.java)
                    startActivity(i)
                }
                //news
                binding.ivBtnHomeNews.setOnClickListener {
                    startActivity(Intent(requireActivity(), HomeNewsActivity::class.java))
                }
                // barcode
                binding.rlScanBarcode.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llScanBarcode.setOnClickListener {
//                    startActivity(Intent(requireActivity(), ScanBarcodeActivity::class.java))
                    Toast.makeText(
                        requireActivity(),
                        "Fitur dalam tahap pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            "Supervisor" -> {
                binding.tvDacOrChecklist.text = "Checklist"
                profileViewModel.getChecklistCount(projectCode)

                binding.llScheduleOpr.visibility = View.GONE
                binding.llHistory.visibility = View.GONE

                // geo location attendance
                binding.llAttendance.setOnClickListener {
//                    checkAllPermission()
//                    checkLocation()
                    checkFaceAndProfile(userId)

                }

                // menu ctalk
                binding.llComplaint.visibility = View.VISIBLE
                binding.llComplaint.setOnClickListener {
                    val i = Intent(requireActivity(), DashboardComplaintVendorActivity::class.java)
                    startActivity(i)
                }

                // menu absensi staff
                binding.llAbsentStaff.visibility = View.VISIBLE
                binding.llAbsentStaff.setOnClickListener {
//                    val i = Intent(requireActivity(), NotAbsentOperatorActivity::class.java)
                    val i = Intent(requireActivity(), GrafikActivity::class.java)
                    startActivity(i)
                }

                // menu notifikasi
                binding.ivNotifHomeVendor.setOnClickListener {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.CLICK_FROM, "Home"
                    )
                    val i = Intent(requireActivity(), NotifMidActivity::class.java)
                    startActivity(i)
                }
                //news
                binding.ivBtnHomeNews.setOnClickListener {
                    startActivity(Intent(requireActivity(), HomeNewsActivity::class.java))
                }
                // barcode
                binding.rlScanBarcode.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llScanBarcode.setOnClickListener {
//                    startActivity(Intent(requireActivity(), ScanBarcodeActivity::class.java))
                    Toast.makeText(
                        requireActivity(),
                        "Fitur dalam tahap pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            "Chief Supervisor" -> {
                binding.tvDacOrChecklist.text = "Checklist"
                profileViewModel.getChecklistCount(projectCode)

                binding.llScheduleOpr.visibility = View.GONE
                binding.llHistory.visibility = View.GONE

                // geo location attendance
                binding.llAttendance.setOnClickListener {
//                    checkAllPermission()
//                    checkLocation()
                    checkFaceAndProfile(userId)

                }

                // menu ctalk
                binding.llComplaint.visibility = View.VISIBLE
                binding.llComplaint.setOnClickListener {
                    val i = Intent(requireActivity(), DashboardComplaintVendorActivity::class.java)
                    startActivity(i)
                }

                // menu absensi staff
                binding.llAbsentStaff.visibility = View.VISIBLE
                binding.llAbsentStaff.setOnClickListener {
//                    val i = Intent(requireActivity(), NotAbsentOperatorActivity::class.java)
                    val i = Intent(requireActivity(), GrafikActivity::class.java)
                    startActivity(i)
                }

                // menu notifikasi
                binding.ivNotifHomeVendor.setOnClickListener {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.CLICK_FROM, "Home"
                    )
                    val i = Intent(requireContext(), NotifMidActivity::class.java)
                    startActivity(i)
                }
                //news
                binding.ivBtnHomeNews.setOnClickListener {
                    startActivity(Intent(requireActivity(), HomeNewsActivity::class.java))
                }
                // barcode
                binding.rlScanBarcode.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llScanBarcode.setOnClickListener {
//                    startActivity(Intent(requireActivity(), ScanBarcodeActivity::class.java))
                    Toast.makeText(
                        requireActivity(),
                        "Fitur dalam tahap pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else -> {
                binding.linearLayout6.visibility = View.GONE
                binding.textView4.visibility = View.GONE
                binding.llDacHome.visibility = View.GONE
                binding.tvUnknownUserLevel.visibility = View.VISIBLE
            }

        }
    }


    private fun openDialogEditProfile() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_custom_edit_profile)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val button: AppCompatButton = dialog.findViewById(R.id.btnDialogEditProfile)

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
    private fun openDialogNewUpdate() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_custom_update_app)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val tvVersion = dialog.findViewById(R.id.tvDialogUpdateApp) as TextView
        val button = dialog.findViewById(R.id.btnDialogUpdateApp) as AppCompatButton

        profileViewModel.getNewVersionApp()
        profileViewModel.newVersionAppModel.observe(viewLifecycleOwner) {
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

    private fun checkFaceAndProfile(userId: Int) {
        // Trigger the profile check
        profileViewModel.getCheckProfile(userId)

        // Observe the profile response
        profileViewModel.cekEditProfileModel.observe(viewLifecycleOwner) { profileResponse ->
//            handleProfileResponse(profileResponse)

            // If profile is valid, trigger face recognition check
            if (profileResponse.code == 200) {
                faceRecogViewModel.getStatsRegisFaceRecog(userId)
            }
        }

        // Observe the face recognition response
        faceRecogViewModel.getStatsFaceRecogViewModel().observe(viewLifecycleOwner) { faceResponse ->
            handleFaceRecogResponse(faceResponse)
        }

    }

    private fun handleFaceRecogResponse(response: StatsRegisFaceResponseModel) {
        faceRecogViewModel.getStatsFaceRecogViewModel().removeObservers(viewLifecycleOwner)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.STATS_TYPE,
            response.message
        )

        if (response.code == 200) {
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_ALFABETA, false)
            checkGps()
        } else {
            when(response.errorCode) {
                "126" -> {
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_ALFABETA, true)
                    checkGps()
                }
                else -> {
                    val intent = Intent(requireContext(), RegisterFaceRecogActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            }
        }
    }



    private fun greetingAbsent() {
        if (!oneTimeGreeting){
            val view = View.inflate(requireContext(), R.layout.dialog_greeting_absent_out, null)
            val builder = android.app.AlertDialog.Builder(requireContext())
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBack)
            btnBack.setOnClickListener {
                dialog.dismiss()
            }
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        oneTimeGreeting = true
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setObserver() {
        profileViewModel.getTimeShiftEmployeeViewModel().observe(viewLifecycleOwner){
            val timeOut = ArrayList<String>()
            if (it.code == 200){
                for(i in 0 until it.data.size){
                    timeOut.add(it.data[i].endAt)
                }
                val timeOutString = timeOut.joinToString(",") // Combine elements with commas

                //timenow
                val sdf = SimpleDateFormat("HH:mm")
                val time = sdf.format(Date())

                if (time == timeOutString){
                    greetingAbsent()
                    createNotification(requireContext(),
                        "Jangan lupa absen pulang :D",
                        "Jangan lupa lakukan absen terlebih dahulu, sebelum meninggalkan area")
                }
                Log.d("agri","$time $timeOutString")
            }
        }
        profileViewModel.isLoading?.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan.", Toast.LENGTH_SHORT)
                        .show()
                    hideLoading()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerLayoutHomeVendor.stopShimmerAnimation()
                        binding.shimmerLayoutHomeVendor.visibility = View.GONE
                        binding.clHomeVendor.visibility = View.VISIBLE
                        binding.linearLayout6.visibility = View.VISIBLE
                        binding.textView4.visibility = View.VISIBLE
                        binding.llDacHome.visibility = View.VISIBLE
                    }, 500)
                    hideLoading()
                }
            }
        }
        profileViewModel.checkVersionAppModel.observe(viewLifecycleOwner) {
//            if (it.code == 200) {
//                profileViewModel.getCheckProfile(userId)
//            } else {
//                if (it.message == "Your app need to update") {
//                    openDialogNewUpdate()
//                } else {
//                    Toast.makeText(requireActivity(), "error check version app", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
            if (it.code != 200) {
                if (it.message == "Your app need to update") {
                    openDialogNewUpdate()
                } else {
                    Toast.makeText(requireActivity(), "error check version app", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        profileViewModel.getProfileModel().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                userName = it.data.employeeName
                userNuc = it.data.employeeNuc
                userPosition = it.data.jobName
                levelPosition = it.data.jobLevel
                userProject = it.data.project.projectName
                projectCode = it.data.project.projectCode

                CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_PROJECT, it.data.project.projectName)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_JABATAN, it.data.jobCode)


                loadProfileDefault(it.data.employeePhotoProfile ?: "")

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_NAME,
                    userName
                )

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_POSITION,
                    userPosition
                )

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_NUC,
                    userNuc
                )

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_PROJECT_CODE,
                    projectCode
                )

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_LEVEL_POSITION,
                    levelPosition
                )

                binding.tvUserNameHomeVendor.text = userName
                binding.tvUserNucHomeVendor.text = userNuc

                setHomeScreen()

                statusAbsenViewModel.getStatusAbsen(userId, projectCode)
                profileViewModel.getDailyActHome(userId, projectCode)
                statusAbsenViewModel.statusTIMEInOut(userId, projectCode)
                profileViewModel.checkVersion(versionApp)
                

                if (userLevelPosition == "Operator"){
                    profileViewModel.getBadgeNotificationOperator(userId, projectCode)
                } else {
                    profileViewModel.getBadgeNotification(userId, projectCode)
                }
//                profileViewModel.getBadgeNotificationOperator(userId, projectCode)

            } else {
                binding.tvUserNameHomeVendor.text = getString(R.string.user_name)
                binding.tvUserNucHomeVendor.text = getString(R.string.user_nuc)
                binding.tvWorkStatusHomeClient.text = getString(R.string.user_work_status)
            }
        }
        profileViewModel.getDacCount().observe(viewLifecycleOwner) {
            if (it.code == 200) {

                binding.tvTotalDacHomeVendor.text =
                    "${it.data.totalDACFinish} / ${it.data.totalDac}"
            } else {
                binding.tvTotalDacHomeVendor.text = "- / -"
                Toast.makeText(
                    requireActivity(),
                    "Gagal mengambil data total dac",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        profileViewModel.checklistCountModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                binding.tvTotalDacHomeVendor.text =
                    "${it.data.totalPlottingChecklist} / ${it.data.totalPlotting}"
            } else {
                binding.tvTotalDacHomeVendor.text = "- / -"
                Toast.makeText(
                    requireActivity(),
                    "Gagal mengambil data total checklist",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        statusAbsenViewModel.statusAttendanceTime().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if (it.message == "Your schedule not found or something wrong") {
                    binding.tvTimeCheckInAttendanceHome.text = "--:--"
                    binding.tvTimeCheckOutAttendanceHome.text = "--:--"
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_SCHEDULE, true)
                } else {
                    if (it.data.scanIn == "null" || it.data.scanIn == null || it.data.scanIn == "") {
                        binding.tvTimeCheckInAttendanceHome.text = "--:--"
                    } else {
                        val mStringDate = it.data.scanIn
                        val oldFormat = "dd-MM-yyyy HH:mm:ss"
                        val newFormat = "HH:mm"

                        var formattedDate = ""
                        val dateFormat = SimpleDateFormat(oldFormat)
                        var myDate: Date? = null
                        try {
                            myDate = dateFormat.parse(mStringDate)
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                        val timeFormat = SimpleDateFormat(newFormat)
                        formattedDate = timeFormat.format(myDate)

                        binding.tvTimeCheckInAttendanceHome.text = "" + formattedDate
                    }

                    if (it.data.scanOut == "null" || it.data.scanOut == null || it.data.scanOut == "") {
                        binding.tvTimeCheckOutAttendanceHome.text = "--:--"
                    } else {
                        val mStringDate = it.data.scanOut
                        val oldFormat = "dd-MM-yyyy HH:mm:ss"
                        val newFormat = "HH:mm"

                        var formattedDate = ""
                        val dateFormat = SimpleDateFormat(oldFormat)
                        var myDate: Date? = null
                        try {
                            myDate = dateFormat.parse(mStringDate)
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                        val timeFormat = SimpleDateFormat(newFormat)
                        formattedDate = timeFormat.format(myDate)

                        binding.tvTimeCheckOutAttendanceHome.text = "" + formattedDate
                    }
                }

            } else {
                binding.tvTimeCheckInAttendanceHome.text = "--:--"
                binding.tvTimeCheckOutAttendanceHome.text = "--:--"
                Toast.makeText(requireActivity(), "gagal mengambil data absen", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        //observer checknews
        profileViewModel.getCheckHomeNewsViewModel().observe(viewLifecycleOwner) {
            if (it.code == 200) {
//                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
               showCustomSnackbarAtTop(requireView(), it.message)
                binding.ivNewNews.visibility = View.VISIBLE
            } else {
                Log.d("AGRI", it.message)
            }
        }
    }




    //statusbar notif
    private fun createNotification(context: Context, title: String, content: String) {
        // Create a notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "attendance_channel_id"
            val channel = NotificationChannel(
                channelId,
                "Attendance Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val channelId = "attendance_channel_id"
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo_new_careops)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        with(NotificationManagerCompat.from(context)) {
            val notificationId = System.currentTimeMillis().toInt()
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.VIBRATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(notificationId, builder.build())
        }
    }



    private fun showCustomSnackbarAtTop(view: View, message: String) {
        if (!oneTimeShow) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Lihat") {
                    val intent = Intent(view.context, HomeNewsActivity::class.java)
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

    private fun getStatusHighLevel() {
        statusAbsenViewModel.statusAbsenModelMid().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                val statusAbsen = it.data.statusAttendance
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.STATUS_ABSEN_FIRST,
                    statusAbsen
                )

                when (statusAbsen) {
                    "Belum absen", "BELUM ABSEN" -> {
                        binding.tvWorkStatusHomeClient.text = statusAbsen
                    }
                    "Bertugas", "BERTUGAS" -> {
                        binding.tvWorkStatusHomeClient.text = statusAbsen
                    }
                    "Selesai", "SELESAI" -> {
                        binding.tvWorkStatusHomeClient.text = statusAbsen
                    }
                    "Off day", "Off Day", "OFF DAY" -> {
                        binding.tvWorkStatusHomeClient.text = statusAbsen
                    }
                }
            } else {
                val statusAbsenFirst = "Tidak ada jadwal"
                binding.tvWorkStatusHomeClient.text = statusAbsenFirst
            }
        }
    }

    private fun getStatusMidLevel() {
        statusAbsenViewModel.statusAbsenModelMid().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                val statusAbsen = it.data.statusAttendance
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.STATUS_ABSEN_FIRST,
                    statusAbsen
                )

                when (statusAbsen) {
                    "Belum absen", "BELUM ABSEN" -> {
                        binding.tvWorkStatusHomeClient.text = statusAbsen
                    }
                    "Bertugas", "BERTUGAS" -> {
                        binding.tvWorkStatusHomeClient.text = statusAbsen
                    }
                    "Selesai", "SELESAI" -> {
                        binding.tvWorkStatusHomeClient.text = statusAbsen
                    }
                    "Off day", "Off Day", "OFF DAY" -> {
                        binding.tvWorkStatusHomeClient.text = statusAbsen
                    }
                }
            } else {
                val statusAbsenFirst = "Tidak ada jadwal"
                binding.tvWorkStatusHomeClient.text = statusAbsenFirst
                CarefastOperationPref.saveString(CarefastOperationPrefConst.STATUS_ABSEN_FIRST, "")
            }
        }
    }

    private fun getStatusLowLevel() {
        statusAbsenViewModel.statusAbsenModel().observe(viewLifecycleOwner) {
            when (it.code) {
                200 -> {
                    val statusAbsenFirst = it.data.statusAttendanceFirst
                    val statusAbsenSecond = it.data.statusAttendanceSecond

                    if (statusAbsenFirst != null) {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.STATUS_ABSEN_FIRST,
                            statusAbsenFirst
                        )
                    }

                    if (statusAbsenSecond != null) {
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.STATUS_ABSEN_SECOND,
                            statusAbsenSecond
                        )
                    }
                }
                else -> {
                    val statusAbsenFirst = "Tidak ada jadwal"
                    binding.tvWorkStatusHomeClient.text = statusAbsenFirst
                }
            }
        }
    }


    private fun loadData() {
        profileViewModel.getTimeShiftEmployee(userId, projectCode)
        profileViewModel.getProfileEmployee(userId)
        profileViewModel.getCheckNews(userType = "Operator", userId)
    }

    //notifw
    private fun getBadgeNotif() {
        if (userLevelPosition == "Operator") {
            profileViewModel.getBadgeNotificationOperator(userId, projectCode)
            profileViewModel.getBadgeNotifOperator().observe(viewLifecycleOwner) {
                binding.badgeCountNotif.setBadgeValue(it.data)
                    .setBadgeOvalAfterFirst(true)
                    .setMaxBadgeValue(999)
                    .setBadgePosition(BadgePosition.BOTTOM_RIGHT)
                    .setBadgeTextStyle(Typeface.NORMAL)
                    .setShowCounter(true)
                    .setBadgeBackground(resources.getDrawable(R.drawable.bg_notification))
                    .setBadgePadding(5);
            }
        } else {
            profileViewModel.getBadgeNotification(userId, projectCode)
            profileViewModel.getBadgeNotifPengawas().observe(viewLifecycleOwner) {
                binding.badgeCountNotif.setBadgeValue(it.data)
                    .setBadgeOvalAfterFirst(true)
                    .setMaxBadgeValue(999)
                    .setBadgePosition(BadgePosition.BOTTOM_RIGHT)
                    .setBadgeTextStyle(Typeface.NORMAL)
                    .setShowCounter(true)
                    .setBadgeBackground(resources.getDrawable(R.drawable.bg_notification))
                    .setBadgePadding(5);
            }
        }
    }

    //INI GET DATA BUAT DITARO DI ADAPTERNYA
    //DAC
    @SuppressLint("SetTextI18n")
    private fun setObserverDacHome() {
        layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvDailyHome.layoutManager = layoutManager

        rvSkeleton = binding.rvDailyHome.applySkeleton(R.layout.item_shimmer_dac_home)
        rvSkeleton.showSkeleton()

        profileViewModel.dailyActHomeResponseModel.observe(viewLifecycleOwner) {
            Log.d("TAG", "setObserverDacHome: " + it.status)

            if (it.code == 200) {
                if (it.dailyActDataArrayHomeResponseModel.isNotEmpty()) {
                    binding.rvDailyHome.visibility = View.VISIBLE
                    dailyHomeResponseModel = it
                    dailyActAdapterHome = HomeDacAdapter(
                        it.dailyActDataArrayHomeResponseModel
                    )
                    binding.rvDailyHome.adapter = dailyActAdapterHome

                } else {
                    binding.tvEmptyDac.visibility = View.VISIBLE
                    binding.tvEmptyDac.text = "Anda tidak memiliki jadwal."
                    binding.rvDailyHome.visibility = View.GONE
                }
            } else {
                when (it.errorCode) {
                    "406" -> {
                        binding.tvEmptyDac.visibility = View.VISIBLE
                        binding.tvEmptyDac.text = it.message
                        binding.rvDailyHome.visibility = View.GONE
                    }
                    else -> {
                        binding.tvEmptyDac.visibility = View.VISIBLE
                        binding.tvEmptyDac.text = "gagal mengambil list dac"
                        binding.rvDailyHome.visibility = View.GONE
                    }
                }
            }
        }
    }








    //when permission is true run checklocation
    private fun checkLocation() {
        val lm =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
            AlertDialog.Builder(requireContext())
                .setMessage("GPS Tidak aktif")
                .setPositiveButton("Aktifkan",
                    DialogInterface.OnClickListener { _, _ ->
                        requireContext().startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    })
                .setNegativeButton("Batal", null)
                .show()
        } else {
            if (levelPosition == "Operator") {
                val i = Intent(requireActivity(), AttendanceLowGeoLocationOSM::class.java)
                startActivity(i)
            } else {
                val i = Intent(requireActivity(), AttendanceMidGeoLocationOSMNew::class.java)
                startActivity(i)
            }
        }
    }


    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(requireContext(), loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
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
            hideLoading()
            return true
        }
        return false
    }

    //Snack bar kesalahan data / data kosong
    private fun onSNACK(view: View, str: String) {
        val snackbar = Snackbar.make(
            view, str,
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Error", null)
        snackbar.setActionTextColor(resources.getColor(R.color.primary_color))
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(resources.getColor(R.color.primary_color))
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 14f
        snackbar.show()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }


}