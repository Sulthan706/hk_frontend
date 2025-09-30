package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.*
import android.location.LocationRequest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.databinding.ActivityHomeVendorBinding
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity.ListShiftChecklistActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.activity.DacList
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment.HomeVendorFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment.ReportFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment.ServiceFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.fragment.ProfileFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.features.splash.ui.activity.SplashActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.button.MaterialButton
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.hkapps.hygienekleen.R


class HomeVendorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeVendorBinding
    private var dateBirth =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.BIRTHDAY_MANAGEMENT, "")
    private var imageBirth =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_IMAGE, "")
    private var userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userNus = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, "")
    private var jobLevel: String = ""
    private var doubleBackToExit = false

    private val PERMISSION_REQUEST_CODE = 100
    private val PREF_PERMISSION_VALIDATION = "pref_permission_validation"
    private var isPermissionValidationShown = false
    private var adsId: String = ""

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var lastLatitude: Double = 0.0
    private var lastLongitude: Double = 0.0
    private var lastAddress: String = ""
    private var fakeGpsValidator: Boolean = false

    private var loadingDialog: Dialog? = null
    private val REQUEST_CODE_GPS = 100

    private var oneShowPopUp: Boolean = false

    private val locationManager: LocationManager by lazy {
        this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    val liveLocationAddress =  MutableLiveData<String>()

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private val faceRecogViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(this).get(FaceRecogViewModel::class.java)
    }

    private val isLastUpdateProfileClicked = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.LAST_UPDATE,false)

    private var newMemberAndDataNotCompleted = ""
    var CAMERA_PERMISSION_REQUEST_CODE = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView) { view, insets ->
            val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = 0
            view.layoutParams = layoutParams
            WindowInsetsCompat.CONSUMED
        }


        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.SAVE_DATE_TEMPORER_START,
            ""
        )

        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.SAVE_DATE_TEMPORER_END,
            ""
        )

        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.ATTENDANCE_STR,
            ""
        )

        CarefastOperationPref.saveInt(
            CarefastOperationPrefConst.ATTENDANCE_ID,
            0
        )

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        if (intent.getBooleanExtra("navigateToProfile", false)) {
            navigateToProfileFragment()
        } else {
            navigateDefaultFragment()
        }

        var isDialogOpened = false

        setObsDialog()
        lifecycleScope.launch {
            delay(1000)
            viewModel.getProfileEmployee(userId)
            viewModel.checkRenewalProfile(userId)
        }

        viewModel.getProfileModel().observe(this) {
            when (it.code) {
                200 -> {
                    if (it.data.isActive == "N") {
                        Toast.makeText(
                            this,
                            "Maaf, Anda sudah tidak terdaftar sebagai karyawan Carefast",
                            Toast.LENGTH_SHORT
                        ).show()
                        CarefastOperationPref.logout()
                        val i = Intent(this, SplashActivity::class.java)
                        startActivity(i)
                    }

                    if(it.data.lastUpdatedProfile == null && !isDialogOpened && newMemberAndDataNotCompleted.isBlank()){
                        isDialogOpened = true
                        openDialogLastEditProfile(true)
                    }

                    if(it.data.lastUpdatedProfile != null && !isDialogOpened && newMemberAndDataNotCompleted.isBlank()){
                        isDialogOpened = true
                        observeCheckRenewal()
                    }

                    if (it.data.jobLevel == "Operator") {
                        fcmNotif("Operator")
                        fcmNotif("Permission_Operator_$userId")
                        fcmNotif("Overtime_Operator_$userId")
                        fcmNotif("RESIGN_APPROVAL_EMPLOYEE_$userNus")
                        Log.d("kece","$userNus")

                    } else if (it.data.jobLevel == "Team Leader") {
//                        fcmNotif("Complaint_Leader_" + it.data.project.projectCode)
//                        fcmNotif("Complaint_Management_Client_" + it.data.project.projectCode)
                        fcmNotif("Permission_TeamLeader_$userId")
                        fcmNotif("Overtime_TeamLeader_$userId")
                        fcmNotif("Permission_From_Operator_" + it.data.project.projectCode)

//                        fcmNotif("Complaint_Visitor_" + it.data.project.projectCode)
                        fcmNotif("RESIGN_APPROVAL_EMPLOYEE_$userNus")
                        Log.d("kece","$userNus")


                    } else if (it.data.jobLevel == "Supervisor") {
//                        fcmNotif("Complaint_Supervisor_" + it.data.project.projectCode)
//                        fcmNotif("Complaint_Management_Client_" + it.data.project.projectCode)
                        fcmNotif("Permission_Supervisor_$userId")
                        fcmNotif("Overtime_Supervisor_$userId")

                        fcmNotif("Permission_From_Operator_" + it.data.project.projectCode)
                        fcmNotif("Permission_From_TeamLeader_" + it.data.project.projectCode)

//                        fcmNotif("Complaint_Visitor_" + it.data.project.projectCode)
                        fcmNotif("RESIGN_APPROVAL_EMPLOYEE_$userNus")


                    } else if (it.data.jobLevel == "Chief Supervisor") {
//                        fcmNotif("Complaint_Chief_Supervisor_" + it.data.project.projectCode)
//                        fcmNotif("Complaint_Management_Client_" + it.data.project.projectCode)
                        fcmNotif("Permission_ChiefSupervisor_$userId")
                        fcmNotif("Overtime_ChiefSupervisor_$userId")

                        fcmNotif("Permission_From_Operator_" + it.data.project.projectCode)
                        fcmNotif("Permission_From_TeamLeader_" + it.data.project.projectCode)
                        fcmNotif("Permission_From_Supervisor_" + it.data.project.projectCode)

//                        fcmNotif("Complaint_Visitor_" + it.data.project.projectCode)
                        fcmNotif("RESIGN_APPROVAL_EMPLOYEE_$userNus")


                    }

                    if(it.data.levelJabatan == "Pengawas"){
                        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_PENGAWAS,true)
                    }
                    jobLevel = it.data.jobLevel
                    when (jobLevel) {
                        "CHIEF SUPERVISOR", "Chief Supervisor",
                        "SUPERVISOR", "Supervisor", "TEAM LEADER", "Team Leader" -> {
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.JOB_LEVEL,jobLevel)
                            binding.fab.setOnClickListener {
                                val i = Intent(this, ListShiftChecklistActivity::class.java)
                                startActivity(i)
//                                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
                            }
                        }


//                        "TEAM LEADER", "Team Leader" ->
//                            binding.fab.setOnClickListener {
//                                val i = Intent(this, ChecklistTeamleadActivity::class.java)
//                                startActivity(i)
//                            }

                        "OPERATOR", "Operator" -> {
                            binding.img.setImageResource(R.drawable.done)
                            binding.fab.setOnClickListener {
                                val i = Intent(this, DacList::class.java)
                                startActivity(i)
                            }
                        }
                        else -> binding.fab.setOnClickListener {
                            Toast.makeText(this, "User role tidak diketahui", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                    Log.d("employename home", it.data.employeeName)
                    Log.d("tagMainActivity", "setObserver: running")
                }
                else -> {
                    when (it.status) {
                        "403" -> {
                            CarefastOperationPref.logout()
                            val i = Intent(this, SplashActivity::class.java)
                            startActivity(i)
                            Toast.makeText(
                                this,
                                "Your session is expired.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        "500" -> {
                            Toast.makeText(
                                this,
                                "Anda belum memiliki jadwal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuHome -> {
                    if (binding.bottomNavigationView.selectedItemId == menuItem.itemId) {
                        //doNothing
                    } else {
                        isDialogOpened = false
                        replaceFragment(HomeVendorFragment())

                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuService -> {
                    if (binding.bottomNavigationView.selectedItemId == menuItem.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(ServiceFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuReport -> {
                    if (binding.bottomNavigationView.selectedItemId == menuItem.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(ReportFragment())
                    }
                    return@setOnNavigationItemSelectedListener true

                }
                R.id.menuProfile -> {
                    if (binding.bottomNavigationView.selectedItemId == menuItem.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(ProfileFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }

                else -> return@setOnNavigationItemSelectedListener false
            }

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            checkAllPermissionDefault()
        } else {
            checkAllPermissionUnder()
        }



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationUpdates()

        loadData()
        setObserver()
        checkGpsHome()
        //validasi 15 menit hit api latlong
//        timerFifteen()
        viewModel.getCheckProfile(userId)

    }


    private fun loadData(){
        viewModel.getCheckAttendance(userId)
    }
    private fun checkGpsHome(){
        val builder = LocationSettingsRequest.Builder().addLocationRequest(com.google.android.gms.location.LocationRequest())
        val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        task
            .addOnSuccessListener { response ->
            }
            .addOnFailureListener { e ->
                val statusCode = (e as ResolvableApiException).statusCode
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                    try {
                        e.startResolutionForResult(this, 100)
                    } catch (sendEx: IntentSender.SendIntentException){ }
                }
            }
    }

    private fun dialogFakeGpsInstalled(){
        oneShowPopUp = true
        val view = View.inflate(this, R.layout.dialog_fake_gps, null)
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingSore)
        btnBack.setOnClickListener{
            dialog.dismiss()
            finishAffinity()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }


    // Request location updates
    private fun requestLocationUpdates() {
        val locationRequest = com.google.android.gms.location.LocationRequest().apply {
            interval =
                10000 // Interval in milliseconds at which you want to receive location updates
            fastestInterval = 2000 // Fastest interval in milliseconds
            priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                LocationRequest.QUALITY_HIGH_ACCURACY
            } else {
                com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
            }
        }

        if (ContextCompat.checkSelfPermission(
                this,
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
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION
            )
        }

    }


    // Handle location updates
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation.let { location ->
                lastLatitude = location.latitude
                lastLongitude = location.longitude
                //use lat long for get address
                lastAddress = getAddressFromCoordinates(this@HomeVendorActivity, lastLatitude, lastLongitude)
                shortAddress(lastAddress)
                Log.d("agri", "$lastLongitude")

                if (location.isFromMockProvider) {
                    // The location is from a mock provider
                    // Handle the case of a fake location

                    if (!oneShowPopUp){
                        dialogFakeGpsInstalled()
                    } else {
                        Log.d("agri","emtpy")
                    }

                } else {
                    // The location is from a genuine provider
                    // Proceed with processing the location
                    Log.d("AGRI", "Safe")
                }

            }
        }

    }

    private fun timerFifteen() {
        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                //logic
                viewModel.putLatLongArea(
                    userId,
                    lastLatitude.toString(),
                    lastLongitude.toString(),
                    lastAddress
                )
            }
        }

        // Schedule the task to run after a delay of 1 minute and repeat every 15 minutes
        val delay = 2000L // Delay of 2 seconds
        val period = 5 * 60 * 1000L // Repeat every 5 minutes (5 * 60 seconds * 1000 milliseconds)
        timer.schedule(task, delay, period)
    }

    //convert to  ... after 90 character
    private fun shortAddress(text: String) {

        if (text != null) {
            val maxLength = 90
            val ellipsis = "..."

            val validatedAddress = when {
                text.length > maxLength -> text.substring(0, maxLength) + ellipsis
                else -> text
            }

            liveLocationAddress.value = validatedAddress

        }
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


    private fun checkAllPermissionUnder() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest, PERMISSION_REQUEST_CODE)
        }
    }

    private fun checkAllPermissionDefault() {
        val permissions = arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.VIBRATE,
            Manifest.permission.POST_NOTIFICATIONS
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest, PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            var allPermissionsGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }
        }
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            }
        }
    }


    private fun setPermissionValidationShown() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(PREF_PERMISSION_VALIDATION, true)
        editor.apply()
    }

    private fun showPermissionValidation() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("Please allow all the required permissions in the app settings.")
            .setPositiveButton("Settings") { dialog, _ ->
                // Open app settings
                val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                settingsIntent.data = uri
                startActivity(settingsIntent)
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()

        setPermissionValidationShown()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Check the permission status again after returning from app settings
            checkAllPermissionDefault()
        }
    }

    private fun showPermissionStoragePopup() {
        isPermissionValidationShown = true
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("Please allow all the required permissions in the app settings.")
            .setPositiveButton("Settings") { dialog, _ ->
                // Open app settings
                val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                settingsIntent.data = uri
                startActivityForResult(settingsIntent, PERMISSION_REQUEST_CODE)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                checkAllPermissionDefault()
            }
            .setCancelable(false)
            .show()

        setPermissionValidationShown()
    }



    private fun greetingDialog() {
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date())

        val endTimePagi = "10:59"
        val endTimeSiang = "14:59"
        val endTimeSore = "17:59"
        val endTimeMalam = "05:00"
//        val startnight = "18:00"
//        val startmorning = "05:00"

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
        val view = View.inflate(this, R.layout.dialog_greeting_pagi, null)
        val builder = AlertDialog.Builder(this)
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
        val view = View.inflate(this, R.layout.dialog_greeting_siang, null)
        val builder = AlertDialog.Builder(this)
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
        val view = View.inflate(this, R.layout.dialog_greeting_sore, null)
        val builder = AlertDialog.Builder(this)
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
        val view = View.inflate(this, R.layout.dialog_greeting_malam, null)
        val builder = AlertDialog.Builder(this)
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
        val view = View.inflate(this, R.layout.dialog_greeting_happybirthday, null)
        val builder = AlertDialog.Builder(this)
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
            val imageResource = resources.getIdentifier(uri, null, this.packageName)
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

    private fun navigateDefaultFragment() {
        val profileFragment = HomeVendorFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_homeVendor, profileFragment)
            .commit()
    }

    fun navigateToProfileFragment() {
        val profileFragment = ProfileFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_homeVendor, profileFragment)
            .commit()

        // Set the selected item in bottom navigation
        binding.bottomNavigationView.selectedItemId = R.id.menuProfile
    }

    private fun setObserver() {
        //face recog
        faceRecogViewModel.getStatsFaceRecogViewModel().observe(this){
            if (it.code == 200){
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATUSFACERECOG, true)
            } else {
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATUSFACERECOG, false)
            }
        }
        //update latlong
//        viewModel.putLatLongAreaModel.observe(this){
//            if (it.code == 200){
//                Log.d("agri","latlong sent")
//            } else {
//                Log.d("agri","latlong not sent")
//            }
//        }

//        //dialog melengkapi profile

        viewModel.getCheckAttendanceViewModel().observe(this){
            if (it.code == 200){
                if (it.message == "Y"){
                    dialogNotAttendance()
                }
            }
        }
    }

    private fun setObsDialog(){
        viewModel.cekEditProfileModel.observe(this) {
            if (it.code != 200) {
                when (it.errorCode) {
//                      "01" -> GreetingCompletingProfileLowFragment().show(supportFragmentManager,"greeting")
                    "01" -> {
                        newMemberAndDataNotCompleted = "01"
                        openDialogEditProfile()
                    }
                    "03" -> {
                        newMemberAndDataNotCompleted = "03"
                        openMonthEditProfile()
                    }
                    else -> {
                        newMemberAndDataNotCompleted = ""
                        Toast.makeText(
                            this,
                            "error check profile data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.REQPROFILE, false)
            } else {
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.REQPROFILE, true)
            }
            Log.d("agri","${it.errorCode}")
        }
    }


    private fun openMonthEditProfile() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_monthly_update_profile)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val button: AppCompatButton = dialog.findViewById(R.id.btnDialogEditProfile)

        button.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.bottomNavigationView.selectedItemId = R.id.menuProfile
            }, 200)
            dialog.dismiss()
        }
        dialog.show()
    }



    private fun dialogNotAttendance() {
        val view = View.inflate(this, R.layout.dialog_late_attendance, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<MaterialButton>(R.id.btnBackGreetingMalam)
        val tvCaption = dialog.findViewById<TextView>(R.id.tvCaptionNotAttendance)
        tvCaption.text = Html.fromHtml("Admin pantau Anda sudah 3 hari berturut-turut <font color='#FF5656'><b>TIDAK HADIR<b></font> tanpa kabar. Yuk, tingkatkan terus kehadiranmu. Jangan sampai mempengaruhi performa kerjamu bulan ini ya.")
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun openDialogEditProfile() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom_edit_profile)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val button: AppCompatButton = dialog.findViewById(R.id.btnDialogEditProfile)

        button.setOnClickListener {
            dialog.dismiss()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.bottomNavigationView.selectedItemId = R.id.menuProfile
            }, 200)
        }
        dialog.show()
    }

    private fun openDialogLastEditProfile(isLastUpdate: Boolean) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_dialog_last_update_profile)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val tvTittle = dialog.findViewById<TextView>(R.id.tv_tittle_dialog_last_update)
        val tvDesc = dialog.findViewById<TextView>(R.id.tv_desc_dialog_last_update)
        val btnSave: AppCompatButton = dialog.findViewById(R.id.btn_save_complete)
        val btnUpdateData: AppCompatButton = dialog.findViewById(R.id.btn_update_profile)

        if(isLastUpdate){
            btnSave.setOnClickListener {
                viewModel.updateLastProfile(userId)
                observerUpdateLastProfile()
                dialog.dismiss()
                greetingDialog()

            }
            btnUpdateData.setOnClickListener {
                dialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.bottomNavigationView.selectedItemId = R.id.menuProfile
                }, 200)
            }
        }else{
            tvTittle.text = resources.getString(R.string.text_tittle_update_profile)
            tvDesc.text = resources.getString(R.string.text_desc_update_profile)
            btnSave.text = resources.getString(R.string.text_update_profile)
            btnSave.setOnClickListener {
                dialog.dismiss()
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.LAST_UPDATE,true)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.bottomNavigationView.selectedItemId = R.id.menuProfile
                }, 200)
                greetingDialog()
            }
            btnUpdateData.visibility = View.GONE
        }
        dialog.show()
    }

    private fun observerUpdateLastProfile(){
        viewModel.updateLastModel.observe(this){
            if(it.errorCode != null){
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeCheckRenewal(){
        var isShowDialogGreeting = true
        viewModel.checkRenewalModel.observe(this){
            if(it.errorCode == "01"){
                openDialogLastEditProfile(false)
            }

            if(it.errorCode != "01" && isShowDialogGreeting){
                isShowDialogGreeting = false
                greetingDialog()
            }
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        if(fragment is HomeVendorFragment && CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.LAST_UPDATE,false)){
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.LAST_UPDATE,false)
            val intent = Intent(this, HomeVendorActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_homeVendor, fragment)
                .commit()

        }

    }

    //Notif subscribtion
    fun fcmNotif(topic: String) {
        Log.d("FCM", "fcm")
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener { task ->
            var msg = ""
            msg = if (!task.isSuccessful) {
                "failed"
            } else {
                "success"
            }
            Log.d("FCM", msg)
            //                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
            finishAffinity()
        }
        doubleBackToExit = true
        Toast.makeText(this, "press BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExit = false }, 2000)
    }

}