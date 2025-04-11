package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.fragment

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentHomeNewBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.old.AttendanceFixLowLevelActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.new_.AttendanceLowGeoLocationOSM
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.old.AttendanceFixMidActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.activity.highLevel.AttendanceActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.activity.PenilaianKondisiObjectActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.activity.DacActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.activity.ListProjectActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.StatusAbsenViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.ui.activity.PlottingActivity
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.activity.MyteamSpvActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeNewBinding

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userProjectFM =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PRO_PROJECT_NAME, "")

    private var projectCode: String = ""
    private var userPhone: String = ""
    private var userName: String = ""
    private var userNuc: String = ""
    private var userPosition: String = ""
    private var levelPosition: String = ""
    private var userProject: String = ""

    private val profileViewModel: HomeViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(HomeViewModel::class.java)
    }
    private val statusAbsenViewModel: StatusAbsenViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(StatusAbsenViewModel::class.java)
    }

    private var loadingDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for requireContext() fragment
        binding = FragmentHomeNewBinding.inflate(inflater, container, false)
        binding.shimmerLayoutHome.startShimmerAnimation()
        binding.shimmerLayoutHomeProjectStatus.startShimmerAnimation()
        //showLoading(getString(R.string.loading_string))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        setObserver()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(requireContext())
        }
    }

    private fun setHomeScreen() {
        Log.d("tagHomeFragment", "setHomeScreen: userName: $userName")
        Log.d("tagHomeFragment", "setHomeScreen: userNuc: $userNuc")
        Log.d("tagHomeFragment", "setHomeScreen: userPosition: $userPosition")
        Log.d("tagHomeFragment", "setHomeScreen: projectCode: $projectCode")
        Log.d("tagHomeFragment", "setHomeScreen: userProjectFM: $userProjectFM")

        //semuabisaaksesye
        getPlottingScreen()

//        getTimkuScreen()
//        getChecklistScreen()
//        binding.tvProjectUserHome.visibility = View.VISIBLE
//        binding.tvProjectHome.visibility = View.GONE
//        binding.rlAbsenIzinMenu.visibility = View.VISIBLE
//        binding.rlPlottingLemburJadkar.visibility = View.VISIBLE
//        binding.rlTimkuChecklistMenu.visibility = View.VISIBLE

        when (levelPosition) {
            "Operator" -> {
                // set view
                binding.llIzin.visibility = View.VISIBLE
                binding.tvProjectUserHome.visibility = View.VISIBLE
                binding.tvProjectHome.visibility = View.GONE
                binding.rlPlottingLemburJadkar.visibility = View.GONE
                binding.rlTimkuChecklistMenu.visibility = View.GONE

                //sementara buat ke geo lokasi
                binding.llAbsen.setOnClickListener {
                    checkAllPermission()
                }

//                getAttendanceScreenLowLevel()
                getDacLowLevel()
            }
            "Team Leader" -> {
                // set view
                binding.llIzin.visibility = View.GONE
                binding.tvProjectUserHome.visibility = View.VISIBLE
                binding.tvProjectHome.visibility = View.GONE
                binding.rlPlottingLemburJadkar.visibility = View.GONE

                //sementara buat ke geo lokasi
                binding.llAbsen.setOnClickListener {
                    checkAllPermission()
                }

                // get menu direct screen
                getChecklistScreen()
                getTimkuScreen()
//                getAttendanceScreenMidLevel()
            }
            "Supervisor" -> {
                // set view
                binding.tvProjectUserHome.visibility = View.VISIBLE
                binding.tvProjectHome.visibility = View.GONE
                // get menu direct screen
                getChecklistScreen()
                getTimkuScreen()
                getAttendanceScreenHighLevel()
            }
            "Chief Supervisor" -> {
                // get menu direct screen
                getChecklistScreen()
                getAttendanceScreenHighLevel()
                // set view
                binding.tvProjectUserHome.visibility = View.VISIBLE
                binding.tvProjectHome.visibility = View.GONE
//                if (userPosition == "Chief Supervisor") {
//                    binding.tvProjectUserHome.visibility = View.VISIBLE
//                    binding.tvProjectHome.visibility = View.GONE
//                } else {
//                    binding.tvProjectUserHome.visibility = View.GONE
//                    getListProjectScreen()
//                    if (userProjectFM == "") {
//                        binding.tvProjectHome.text = getString(R.string.pilih_project)
//                    } else {
//                        binding.tvProjectHome.text = userProjectFM
//                    }
//                }
            }
            // "Field Manager"
            else -> {
                binding.tvProjectUserHome.visibility = View.GONE
                getListProjectScreen()
                if (userProjectFM == "") {
                    binding.tvProjectHome.text = getString(R.string.pilih_project)
                } else {
                    binding.tvProjectHome.text = userProjectFM
                }
            }

        }
    }

    private fun getDacLowLevel() {
        binding.llIzin.setOnClickListener {
            val i = Intent(context, DacActivity::class.java)
            startActivity(i)
        }
    }


    private fun setObserver() {
        profileViewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan.", Toast.LENGTH_SHORT)
                        .show()
                    Log.d(
                        "Hide", "true"
                    )
                    hideLoading()

                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerLayoutHome.stopShimmerAnimation()
                        binding.shimmerLayoutHomeProjectStatus.stopShimmerAnimation()

                        binding.shimmerLayoutHome.visibility = View.GONE
                        binding.shimmerLayoutHomeProjectStatus.visibility = View.GONE

                        binding.clHomeProfile.visibility = View.VISIBLE
                        binding.linearLayout6.visibility = View.VISIBLE

                        binding.rlMenuHome.visibility = View.VISIBLE
                        binding
                    }, 33)
                    hideLoading()
                }
            }
        })

        profileViewModel.getProfileModel().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                userName = it.data.employeeName
                userNuc = it.data.employeeNuc
                userPosition = it.data.jobName
                levelPosition = it.data.jobLevel
                userProject = it.data.project.projectName
                projectCode = it.data.project.projectCode
                userPhone = when {
                    it.data.employeePhoneNumber != null -> it.data.employeePhoneNumber.toString()
                    else -> ""
                }

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_PHONE,
                    userPhone
                )

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_NAME,
                    userName
                )

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_POSITION,
                    userProject
                )

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_NUC,
                    userNuc
                )

                Toast.makeText(requireContext(), projectCode, Toast.LENGTH_SHORT).show()

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_PROJECT_CODE,
                    projectCode
                )

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_LEVEL_POSITION,
                    levelPosition
                )


                setDataProfile()
                setHomeScreen()

                statusAbsenViewModel.getStatusAbsen(userId, projectCode)

            }
            Log.d("tagHomeFragment", "setObserver: running")
        }

//        statusAbsenViewModel.statusAbsenModel().observe(viewLifecycleOwner) {
//            if (it.code == 200) {
//                val statusAbsen = it.data.status
//                CarefastOperationPref.saveString(
//                    CarefastOperationPrefConst.STATUS_ABSEN,
//                    statusAbsen
//                )
//                when (statusAbsen) {
//                    "Belum Absen" -> {
//                        binding.tvStatusHome.text = statusAbsen
//                        binding.tvInfoHome.visibility = View.VISIBLE
//                        binding.tvInfoHome.text = getString(R.string.home_info_belum_absen)
//                    }
//                    "Bertugas" -> {
//                        binding.tvStatusHome.text = statusAbsen
//                        binding.tvInfoHome.visibility = View.VISIBLE
//                        binding.tvInfoHome.text = getString(R.string.home_info_belum_absen)
//                    }
//                    "Selesai" -> {
//                        binding.tvStatusHome.text = statusAbsen
//                        binding.tvInfoHome.visibility = View.GONE
//                    }
//                    "Off" -> {
//                        binding.tvStatusHome.text = statusAbsen
//                        binding.tvInfoHome.visibility = View.GONE
//                        binding.llAbsen.setOnClickListener {
//                            Toast.makeText(
//                                context,
//                                "Anda tidak memiliki jadwal hari ini",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                    else ->
//                        binding.tvInfoHome.visibility = View.GONE
//                }
//                Log.d("tagHomeFragment", "setObserver: status absen = $statusAbsen")
//            }
//        }

//        statusAbsenViewModel.statusAbsenModelFail().observe(viewLifecycleOwner) {
//            CarefastOperationPref.saveString(
//                CarefastOperationPrefConst.STATUS_ABSEN,
//                "Belum Absen"
//            )
//            binding.tvStatusHome.text = "Belum Absen"
//            binding.tvInfoHome.visibility = View.VISIBLE
//            binding.tvInfoHome.text = getString(R.string.home_info_belum_absen)
//        }


        //refresh home
        binding.swipeHome.setOnRefreshListener(OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeHome.isRefreshing = false
                    val i = Intent(requireContext(), HomeVendorActivity::class.java)
                    context?.startActivity(i)
                    requireActivity().finishAffinity()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })
    }

    private fun loadData() {
        profileViewModel.getProfileEmployee(userId)
        Log.d("tagHomeFragment", "loadData: running")
    }

    private fun setDataProfile() {
        binding.tvNucHome.text = userNuc
        binding.tvUserNameHome.text = userName
        binding.tvUserJobHome.text = userPosition
        binding.tvProjectUserHome.text = userProject
    }

    private fun getChecklistScreen() {
        binding.ivChecklistHome.setOnClickListener {
            val checklist = Intent(context, PenilaianKondisiObjectActivity::class.java)
            startActivity(checklist)
        }
//        binding.ivChecklistHome.setOnClickListener {
//            when (levelPosition) {
//                "Team Leader" -> {
//                    val i = Intent(context, ChecklistTeamleadActivity::class.java)
//                    startActivity(i)
//                }
//                "Supervisor" -> {
//                    val i = Intent(context, ChecklistSpvActivity::class.java)
//                    startActivity(i)
//                }
//            }
//        }
    }

    companion object {
        private const val MULTIPLE_PERMISSION_REQUEST_CODE = 4
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

        val camStatePermissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        if (camStatePermissionCheck == PackageManager.PERMISSION_GRANTED && internetPermissionCheck == PackageManager.PERMISSION_GRANTED && networkStatePermissionCheck == PackageManager.PERMISSION_GRANTED && writeExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED && coarseLocationPermissionCheck == PackageManager.PERMISSION_GRANTED && fineLocationPermissionCheck == PackageManager.PERMISSION_GRANTED && wifiStatePermissionCheck == PackageManager.PERMISSION_GRANTED) {
            checkLocationOnly()
        } else {
            Toast.makeText(requireContext(), "Please allow permission first.", Toast.LENGTH_LONG)
                .show()

            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_WIFI_STATE
                ),
                MULTIPLE_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkLocationOnly() {
        val lm =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {

        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {

        }
        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder(requireContext())
                .setMessage("GPS Tidak aktif")
                .setPositiveButton("Aktifkan",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        requireContext().startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    })
                .setNegativeButton("Batal", null)
                .show()
        } else {
            val i = Intent(context, AttendanceLowGeoLocationOSM::class.java)
            startActivity(i)
        }
    }

    private fun getAttendanceScreenHighLevel() {
        binding.llAbsen.setOnClickListener {
            val intent = Intent(context, AttendanceActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAttendanceScreenMidLevel() {
        binding.textView.setOnClickListener {
            val intent = Intent(context, AttendanceFixMidActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAttendanceScreenLowLevel() {
        binding.textView.setOnClickListener {
            val intent = Intent(context, AttendanceFixLowLevelActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getPlottingScreen() {
        binding.linearLayout4.setOnClickListener {
            val intent = Intent(context, PlottingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getListProjectScreen() {
        binding.tvProjectHome.setOnClickListener {
            val intent = Intent(context, ListProjectActivity::class.java)
            startActivity(intent)
        }
//        if (userPosition == "Field Manager") {
//            binding.tvProjectHome.setOnClickListener {
//                val intent = Intent(context, ListProjectActivity::class.java)
//                startActivity(intent)
//            }
//
//        } else {
//            binding.tvProjectHome.setOnClickListener {
//                val intent = Intent(context, ListProjectPro::class.java)
//                startActivity(intent)
//            }
//
//        }
    }

    private fun getTimkuScreen() {
        binding.ivTimKuHome.setOnClickListener {
            val intent = Intent(context, MyteamSpvActivity::class.java)
            startActivity(intent)
        }
//        when (levelPosition) {
//            "Team Leader" -> {
//                binding.ivTimKuHome.setOnClickListener {
//                    val intent = Intent(context, MyteamTeamleadActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//            "Supervisor" -> {
//                binding.ivTimKuHome.setOnClickListener {
//                    val intent = Intent(context, MyteamSpvActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//        }
    }

    private fun showLoading(loadingText: String) {
        Log.d("HomeFrag", "showLoading: ")
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

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        } else {
            onSNACK(binding.root, "Tidak ada koneksi.")
            hideLoading()
            Log.i("Internet", "NO_INTERNET")
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
}