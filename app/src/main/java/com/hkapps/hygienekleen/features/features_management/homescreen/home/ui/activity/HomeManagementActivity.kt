package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHomeManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment.*
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.ui.fragment.HomeManagementUpdatedFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.activity.InspeksiMainActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HomeManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeManagementBinding
    private var doubleBackToExit = false
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    
    private val viewModel: HomeManagementViewModel by lazy { 
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }
    private var dateBirth =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.BIRTHDAY_MANAGEMENT, "")
    private var imageBirth =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_IMAGE_MANAGEMENT, "")
    private var userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private var adminId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val PERMISSION_REQUEST_CODE = 100
    private val PREF_PERMISSION_VALIDATION = "pref_permission_validation"
    private var isPermissionValidationShown = false


    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        if (intent.getBooleanExtra("navigateToProfile", false)) {
            navigateToProfileFragment()
        } else {
            navigateDefaultFragment()
        }



        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuHome -> {

                    if (binding.bottomNavigationView.selectedItemId == menuItem.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(HomeManagementUpdatedFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuService -> {
                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        view.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_color))
                        view.setPadding(0, systemBars.top, 0, 0)
                        insets
                    }
                    if (binding.bottomNavigationView.selectedItemId == menuItem.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(ServiceManagementFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuReport -> {
                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        view.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        view.setPadding(0, systemBars.top, 0, 0)
                        insets
                    }
                    if (binding.bottomNavigationView.selectedItemId == menuItem.itemId) {
                        //doNothing
                    } else {
//                        replaceFragment(ReportManagementWebFragment())
//                        replaceFragment(ReportManagementFragment())

                        if (userLevel == "BOD" || userLevel == "CEO") {
                            replaceFragment(ReportHighManagementFragment())
                        } else {
                            replaceFragment(ReportComingsoonFragment())
                        }
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuProfile -> {
                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        view.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_color))
                        view.setPadding(0, systemBars.top, 0, 0)
                        insets
                    }
                    if (binding.bottomNavigationView.selectedItemId == menuItem.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(ProfileManagementFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(this, InspeksiMainActivity::class.java))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            checkAllPermissionDefault()
        } else {
            checkAllPermissionUnder()
        }


        Thread(Runnable {
            try {
                val advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(applicationContext)
                val advertisingId = advertisingIdInfo.id

                // Log or use the advertisingId as needed
                Log.d("Advertising ID", advertisingId)


            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            }
        }).start()
//
//        setObserver()
        loadData()

    }



    private fun checkAllPermissionDefault() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest, PERMISSION_REQUEST_CODE)
        }
    }

    private fun loadData(){
        viewModel.getCheckProfile(adminId)
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

    private fun setObserver() {
        viewModel.checkEditProfileModel.observe(this) {
            if (it.code == 200) {
                greetingDialog()
            } else {
                when (it.errorCode) {
//                      "01" -> GreetingCompletingProfileLowFragment().show(supportFragmentManager,"greeting")
                    "01" -> openDialogEditProfile()
                    "03" -> openDialogMonthProfile()
                    else -> Toast.makeText(
                        this,
                        "error check profile data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            Log.d("agri","${it.errorCode}")
        }
    }

    private fun openDialogMonthProfile() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_monthly_update_profile)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val button: AppCompatButton = dialog.findViewById(R.id.btnDialogEditProfile)



        button.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, EditProfileManagementActivity::class.java))
        }
        dialog.show()
    }

    private fun greetingDialog(){
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date())

        val endTimePagi = "10:59"
        val endTimeSiang = "14:59"
        val endTimeSore = "17:59"
        val endTimeMalam = "05:00"
//        val startnight = "18:00"
//        val startmorning = "05:00"

        if (time < endTimePagi){
            greetingPagi()
        } else if (time < endTimeSiang){
            greetingSiang()
        } else if(time < endTimeSore){
            greetingSore()
        } else if(time > endTimeMalam){
            greetingMalam()
        }

        //validasi ultah management
        val sdfBirthday = SimpleDateFormat("dd-MMMM-yyyy")
        val timeBirth = sdfBirthday.format(Date())


        if (dateBirth == timeBirth){
            greetingBirthday()
        }
    }

    private fun openDialogEditProfile() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom_edit_profile)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val button = dialog.findViewById(R.id.btnDialogEditProfile) as AppCompatButton

        button.setOnClickListener {
            dialog.dismiss()

//            val fragment = ProfileFragment()
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fl_homeVendor, fragment)
//                .commit()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.bottomNavigationView.selectedItemId = R.id.menuProfile
            }, 200)
        }
        dialog.show()
    }

    private fun navigateDefaultFragment() {
        val profileFragmentManagement = HomeManagementUpdatedFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_homeVendor, profileFragmentManagement)
            .commit()
    }

    private fun navigateToProfileFragment() {
        val profileFragmentManagement = ProfileManagementFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_homeVendor, profileFragmentManagement)
            .commit()

        // Set the selected item in bottom navigation
        binding.bottomNavigationView.selectedItemId = R.id.menuProfile
    }

    private fun greetingPagi(){
        val view = View.inflate(this, R.layout.dialog_greeting_pagi, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingPagi)
        btnBack.setOnClickListener{
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
    private fun greetingSiang(){
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
    private fun greetingSore(){
        val view = View.inflate(this, R.layout.dialog_greeting_sore, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingSore)
        btnBack.setOnClickListener{
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
    private fun greetingMalam(){
        val view = View.inflate(this, R.layout.dialog_greeting_malam, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingMalam)
        btnBack.setOnClickListener{
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun greetingBirthday(){
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

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_homeVendor, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
            finishAffinity()
        }
        doubleBackToExit = true
        Toast.makeText(this, "press BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed( { doubleBackToExit = false }, 2000)
    }

}