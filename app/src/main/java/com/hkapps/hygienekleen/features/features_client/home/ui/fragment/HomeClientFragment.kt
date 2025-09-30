package com.hkapps.hygienekleen.features.features_client.home.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentHomeClientBinding
import com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.activity.DashboardProjectClientActivity
import com.hkapps.hygienekleen.features.features_client.home.ui.activity.GrafikClientActivity
import com.hkapps.hygienekleen.features.features_client.home.ui.activity.HomeClientActivity
import com.hkapps.hygienekleen.features.features_client.home.ui.activity.ListMachineClientActivity
import com.hkapps.hygienekleen.features.features_client.home.ui.activity.MRActivity
import com.hkapps.hygienekleen.features.features_client.home.ui.activity.TurnOverClientActivity
import com.hkapps.hygienekleen.features.features_client.home.viewmodel.HomeClientViewModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.activity.MainViewPagerRkbActivity
import com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.activity.MyTeamClientsActivity
import com.hkapps.hygienekleen.features.features_client.notifcation.ui.activity.NotifClientActivity
import com.hkapps.hygienekleen.features.features_client.report.ui.new_.activity.ReportActivity
import com.hkapps.hygienekleen.features.features_client.training.ui.activity.TrainingClientActivity
import com.hkapps.hygienekleen.features.features_client.visitreport.ui.activity.MainVisitReportActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import ru.nikartm.support.BadgePosition
import java.text.SimpleDateFormat

class HomeClientFragment : Fragment() {

    private lateinit var binding: FragmentHomeClientBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")

    private var loadingDialog: Dialog? = null
    private var versionApp: String = ""

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    private val viewModel: HomeClientViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(HomeClientViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // new update app
        val manager = requireActivity().packageManager
        val info = manager.getPackageInfo(requireActivity().packageName, 0)
        versionApp = info.versionName ?: "unknown"
        binding.tvVersionHomeClient.text = versionApp

        homeViewModel.checkVersion(versionApp)
        homeViewModel.checkVersionAppModel.observe(viewLifecycleOwner) {
            if (it.message == "Your app need to update") {
                openDialogNewUpdate()
            }
        }

        binding.rlTurnOver.setOnClickListener {
            startActivity(Intent(requireContext(), TurnOverClientActivity::class.java))
        }

        // set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(requireActivity())
            activity?.window!!.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.white)
        }

        // menu home user client
        binding.ivNotifHomeVendor.setOnClickListener{
            startActivity(Intent(requireActivity(), NotifClientActivity::class.java))
        }
        binding.clCtalkHomeClient.setOnClickListener {

//            startActivity(Intent(requireActivity(), HistoryComplaintClientActivity::class.java))
            BotSheetComplaintFragment().show(requireActivity().supportFragmentManager, "bottomsheet")
        }
        binding.llCfteamHomeClient.setOnClickListener {
            startActivity(Intent(requireActivity(), MyTeamClientsActivity::class.java))
        }
        binding.clDashboardProjectHomeClient.setOnClickListener {
            startActivity(Intent(requireActivity(), DashboardProjectClientActivity::class.java))
        }
        binding.llTrainingHomeClient.setOnClickListener {
            startActivity(Intent(requireActivity(), TrainingClientActivity::class.java))
        }
        binding.llReportHomeClient.setOnClickListener {
            startActivity(Intent(requireActivity(),ReportActivity::class.java))
        }
        binding.llVisitReportHomeClient.setOnClickListener {
            startActivity(Intent(requireActivity(), MainVisitReportActivity::class.java))
        }
        binding.llMonthlyWorkReportHomeClient.setOnClickListener {
            startActivity(Intent(requireActivity(), MainViewPagerRkbActivity::class.java))
        }

        onClickNew()

        setObserver()
        loadData()
        fcmNotif("Complaint_Client_" + projectCode)
        fcmNotif("Complaint_Visitor_" + projectCode)

        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.CLIENT_PROJECT_CODE,
            projectCode
        )
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

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
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

    private fun loadData() {
        val complaintTypes = ArrayList<String>()
        complaintTypes.add("COMPLAINT_CLIENT")
        complaintTypes.add("COMPLAINT_MANAGEMENT_CLIENT")

        viewModel.getProfileClient(userId)
        viewModel.getInfoComplaintClient(projectCode, userId)
        viewModel.getInfoProject(projectCode)
        viewModel.getBadgeNotifClient(userId, projectCode)
//        viewModel.getCountChecklist(projectCode)
    }

    private fun onClickNew(){
        binding.rlMr.setOnClickListener {
            startActivity(Intent(requireActivity(), MRActivity::class.java))
        }
        binding.rlTimesheet.setOnClickListener {
            startActivity(Intent(requireActivity(),GrafikClientActivity::class.java).also {
                it.putExtra("type","timesheet")
            })
        }
        binding.rlBak.setOnClickListener {
            startActivity(Intent(requireActivity(),ListMachineClientActivity::class.java))
        }
        binding.rlAbsen.setOnClickListener {
            startActivity(Intent(requireActivity(),GrafikClientActivity::class.java))
        }

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(requireActivity(), "Terjadi kesalahan.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        viewModel.getProfileClientResponse().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                // set client name & project
                binding.tvUserNameHomeClient.text = it.data.clientName
                binding.tvUserProjectHomeClient.text = it.data.levelJabatan


                // set photo profile
                val url = getString(R.string.url) + "assets.admin_master/images/photo_profile/${it.data.photoProfile}"
                if (it.data.photoProfile == "null" || it.data.photoProfile == null || it.data.photoProfile == "") {
                    val uri =
                        "@drawable/profile_default" // where myresource (without the extension) is the file
                    val imageResource = resources.getIdentifier(uri, null, requireActivity().packageName)
                    val res = resources.getDrawable(imageResource)
                    binding.ivFotoUserHomeClient.setImageDrawable(res)
                } else {
                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true)

                    Glide.with(this)
                        .load(url)
                        .apply(requestOptions)
                        .into(binding.ivFotoUserHomeClient)
                }

                // refresh home
                binding.swipeHomeClient.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
                    Handler().postDelayed(
                        Runnable {
                            binding.swipeHomeClient.isRefreshing = false
                            val i = Intent(requireActivity(), HomeClientActivity::class.java)
                            startActivity(i)
                            requireActivity().finishAffinity()
                            requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                        }, 500
                    )
                })
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Gagal mengambil data profile",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.getInfoComplaintClientModel().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                binding.tvCountWaitingCtalkHomeClient.text = "${it.data.countStatusWaiting} menunggu"
                binding.tvCountOnprogressCtalkHomeClient.text = "${it.data.countStatusOnProgress} dikerjakan"
            } else {
                Toast.makeText(requireActivity(), "Gagal mengambil data CTalk", Toast.LENGTH_SHORT)
                    .show()
            }
        }
//        viewModel.countChecklistResponse.observe(requireActivity()) {
//            if (it.code == 200) {
//                binding.tvTotalChecklistHomeClient.text = "${it.data.totalPlottingChecklist} / ${it.data.totalPlotting}"
//            }
//        }
        viewModel.infoProjectResponse.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                binding.tvProjectNameHomeClient.text = it.data.projectName
                binding.tvCountManpowerHomeClient.text = "${it.data.countEmployee}"

                //save logo image
                if (it.data.projectLogo.isNullOrEmpty()){
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.LOGO_CLIENT, "")
                } else{
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.LOGO_CLIENT, it.data.projectLogo)
                }

                // convert date format
                val sdfBefore = SimpleDateFormat("yyyy-MM-dd")
                val dateStartParamBefore= sdfBefore.parse(it.data.projectStart)
                val dateEndParamBefore= sdfBefore.parse(it.data.projectEnd)
                val sdfAfter = SimpleDateFormat("dd MMM yyyy")
                val dateStartParamAfter = sdfAfter.format(dateStartParamBefore)
                val dateEndParamAfter = sdfAfter.format(dateEndParamBefore)
                binding.tvDateRangeHomeClient.text = "$dateStartParamAfter - $dateEndParamAfter"

                // set label status
                binding.tvLabelStatusPeriodeHomeClient.text = it.data.statusProject
                if (it.data.statusProject == "Active") {
                    binding.tvLabelStatusPeriodeHomeClient.setBackgroundResource(R.drawable.rounded_active_home_client)
                } else {
                    binding.tvLabelStatusPeriodeHomeClient.setBackgroundResource(R.drawable.rounded_near_expired_home_client)
                }
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Gagal mengambil data dashboard project",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.getBadgeNotifResponse().observe(viewLifecycleOwner, Observer {
            if (it.code == 200){
                binding.ibvIcon2.setBadgeValue(it.data)
                    .setBadgeOvalAfterFirst(true)
                    .setMaxBadgeValue(999)
                    .setBadgePosition(BadgePosition.TOP_RIGHT)
                    .setBadgeTextStyle(Typeface.NORMAL)
                    .setShowCounter(true)
                    .setBadgeBackground(resources.getDrawable(R.drawable.bg_notification))
                    .setBadgePadding(5);
            } else {
                Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
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
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${requireActivity().packageName}")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${requireActivity().packageName}")))
            }
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                dialog.dismiss()
            }, 1500)
        }

        dialog.show()
    }

    //Notif subscribtion
    fun fcmNotif(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener { task ->
            var msg = ""
            msg = if (!task.isSuccessful) {
                "failed"
            } else {
                "success"
            }
            Log.d("FCM", msg)
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.checkVersion(versionApp)
        homeViewModel.checkVersionAppModel.observe(viewLifecycleOwner) {
            if (it.message == "Your app need to update") {
                openDialogNewUpdate()
            }
        }
    }
}