package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment


import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentServiceBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.ui.activity.midlevel.ScheduleMidActivity
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.chiefspv.activity.MyteamChiefActivity
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.activity.MyteamSpvActivity
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.teamlead.activity.MyteamTeamleadActivity
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.activity.ListComplaintInternalVendorActivity
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.operator.activity.OvertimeOperatorActivity
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.activity.OvertimeTeamleadActivity
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel.ListPermission
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.util.*
import android.text.Html
import android.util.Log
import com.hkapps.hygienekleen.features.features_vendor.damagereport.ui.activity.ListDamageReportVendorActivity
import com.hkapps.hygienekleen.features.features_vendor.damagereport.viewmodel.DamageReportVendorViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.ui.activity.RegisterMekariActivity
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.viewmodel.MekariViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.lowlevel.HistoryPermissionActivity
import com.hkapps.hygienekleen.features.features_vendor.service.resign.ui.activity.ListReqResignVendorActivity


class ServiceFragment : Fragment() {

    private lateinit var binding: FragmentServiceBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val userLevel =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val userJabatan =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_JABATAN, "")
    private var monthss = ""
    private val statusRegis =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATUS_REGIS_MEKARI, false)

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(HomeViewModel::class.java)
    }
    private val mekariViewModel: MekariViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(MekariViewModel::class.java)
    }
    private val bakViewMoel: DamageReportVendorViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(DamageReportVendorViewModel::class.java)
    }
//    private var statsBak: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.window!!.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.primary_color)
        } else {
            binding.textView.setTextColor(R.color.primary_color)
            binding.clService.setBackgroundColor(R.color.white)
        }

        // set current month & year
        val calendar: Calendar = Calendar.getInstance()
        val month = android.text.format.DateFormat.format("MMMM", calendar) as String
        val year = android.text.format.DateFormat.format("yyyy", calendar) as String
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH)
        setCurrentMonthYear(month, year)


        when (userLevel) {
            "Operator" -> {

                binding.llApprovalIzinService.visibility = View.GONE
                binding.llOperatorMenuService.visibility = View.VISIBLE
                binding.llLeaderMenuService.visibility = View.GONE

                //validasi bak
                if(userJabatan == "CS_SENIOR_CLEANER"){
                    //operator
                    binding.llBak.visibility = View.VISIBLE
                    binding.tvBak.visibility = View.VISIBLE
                }else{
                    binding.llBak.visibility = View.GONE
                    binding.tvBak.visibility = View.GONE
                }

                binding.llBak.setOnClickListener {
                    startActivity(Intent(requireContext(), ListDamageReportVendorActivity::class.java))
                }


                binding.llIzinOperator.setOnClickListener {
                    val i = Intent(context, HistoryPermissionActivity::class.java)
                    startActivity(i)
                }
                binding.llLemburOperator.setOnClickListener {
                    val i = Intent(context, OvertimeOperatorActivity::class.java)
                    startActivity(i)
                }

                binding.llMekariOperator.setOnClickListener {
                    if (statusRegis) {
                        mekariViewModel.getTokenMekari(userId)
                        getTokenMekariObserver()
                    } else {
                        startActivity(Intent(requireContext(), RegisterMekariActivity::class.java))
                    }
                }
                binding.llTukerJadwalOperator.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llTukerJadwalOperator.setOnClickListener {
                    Toast.makeText(
                        context,
                        "Fitur ini dalam pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.llTrainingOperator.setOnClickListener {

                    val intent = Intent(requireActivity(), Class.forName("com.digimaster.academy.features.authentication.splashscreen.IntroTrainingActivity"))
                    intent.putExtra("userNuc", CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, ""))
                    startActivity(intent)

                }
            }

            "Team Leader" -> {
                binding.llApprovalIzinService.visibility = View.VISIBLE
                binding.llOperatorMenuService.visibility = View.GONE
                binding.llLeaderMenuService.visibility = View.VISIBLE

                // menu mekari
                binding.llMekari.setOnClickListener {
                    if (statusRegis) {
                        mekariViewModel.getTokenMekari(userId)
                        getTokenMekariObserver()
                    } else {
                        startActivity(Intent(requireContext(), RegisterMekariActivity::class.java))
                    }
                }

                // menu training
                binding.llTrainingLeaders.setOnClickListener {
                    val intent = Intent(requireActivity(), Class.forName("com.digimaster.academy.features.authentication.splashscreen.IntroTrainingActivity"))
                    intent.putExtra("userNuc", CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, ""))
                    startActivity(intent)
                }

                binding.llIzinService.setOnClickListener {
                    val i = Intent(context, ListPermission::class.java)
                    startActivity(i)
                }
                binding.llLemburService.setOnClickListener {
                    val i = Intent(context, OvertimeTeamleadActivity::class.java)
                    startActivity(i)
                }
                binding.llMyTeam.setOnClickListener {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.CLICK_FROM,
                        "Service"
                    )
                    val i = Intent(context, MyteamTeamleadActivity::class.java)
                    startActivity(i)
                }
                binding.llSchedule.setOnClickListener {
                    val i = Intent(context, ScheduleMidActivity::class.java)
                    startActivity(i)
                }
                binding.llBakLeader.setOnClickListener {
                    startActivity(Intent(requireContext(), ListDamageReportVendorActivity::class.java))
                }
                binding.llComplaintInternal.setOnClickListener {
//                            CarefastOperationPref.saveString(
//                                CarefastOperationPrefConst.CLICK_FROM, "Service"
//                            )
//                            val i = Intent(requireContext(), NotifVendorActivity::class.java)
//                            startActivity(i)
                    val i =
                        Intent(requireContext(), ListComplaintInternalVendorActivity::class.java)
                    startActivity(i)
                }
                binding.llApprovalService.setOnClickListener {
//                    CarefastOperationPref.saveString(CarefastOperationPrefConst.APPROVAL_CLICK_FROM, "leaders")
//                    val i = Intent(requireContext(), ApprovalLeaderActivity::class.java)
//                    startActivity(i)
                    Toast.makeText(
                        context,
                        "Fitur ini dalam pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.llTurnoverService.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llTurnoverService.setOnClickListener {
                    Toast.makeText(
                        context,
                        "Fitur ini dalam pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.llTukerJadwalService.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llTukerJadwalService.setOnClickListener {
//                            val i = Intent(context, ChangeScheduleTeamleadActivity::class.java)
//                            startActivity(i)
                    Toast.makeText(
                        context,
                        "Fitur ini dalam pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            "Supervisor" -> {
                binding.llApprovalIzinService.visibility = View.VISIBLE
                binding.llOperatorMenuService.visibility = View.GONE
                binding.llLeaderMenuService.visibility = View.VISIBLE

                // menu mekari
                binding.llMekari.setOnClickListener {
                    if (statusRegis) {
                        mekariViewModel.getTokenMekari(userId)
                        getTokenMekariObserver()
                    } else {
                        startActivity(Intent(requireContext(), RegisterMekariActivity::class.java))
                    }
                }
                binding.llBakLeader.setOnClickListener {
                    startActivity(Intent(requireContext(), ListDamageReportVendorActivity::class.java))
                }

                // menu training
                binding.llTrainingLeaders.setOnClickListener {
                    val intent = Intent(requireActivity(), Class.forName("com.digimaster.academy.features.authentication.splashscreen.IntroTrainingActivity"))
                    intent.putExtra("userNuc", CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, ""))
                    startActivity(intent)
                }

                binding.llIzinService.setOnClickListener {
                    val i = Intent(context, ListPermission::class.java)
                    startActivity(i)
                }
                binding.llLemburService.setOnClickListener {
                    val i = Intent(context, OvertimeTeamleadActivity::class.java)
                    startActivity(i)
                }
                binding.llMyTeam.setOnClickListener {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.CLICK_FROM,
                        "Service"
                    )
                    val i = Intent(requireContext(), MyteamSpvActivity::class.java)
                    startActivity(i)
                }
                binding.llSchedule.setOnClickListener {
                    val i = Intent(context, ScheduleMidActivity::class.java)
                    startActivity(i)
                }
                binding.llComplaintInternal.setOnClickListener {
//                            CarefastOperationPref.saveString(
//                                CarefastOperationPrefConst.CLICK_FROM, "Service"
//                            )
//                            val i = Intent(requireContext(), NotifVendorActivity::class.java)
//                            startActivity(i)
                    val i =
                        Intent(requireContext(), ListComplaintInternalVendorActivity::class.java)
                    startActivity(i)
                }
                binding.llApprovalService.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llApprovalService.setOnClickListener {
//                    CarefastOperationPref.saveString(CarefastOperationPrefConst.APPROVAL_CLICK_FROM, "leaders")
//                    val i = Intent(requireContext(), ApprovalLeaderActivity::class.java)
//                    startActivity(i)
                    Toast.makeText(
                        context,
                        "Fitur ini dalam pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.llTurnoverService.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llTurnoverService.setOnClickListener {
                    Toast.makeText(
                        context,
                        "Fitur ini dalam pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.llTukerJadwalService.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llTukerJadwalService.setOnClickListener {
                    Toast.makeText(
                        context,
                        "Fitur ini dalam pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            "Chief Supervisor" -> {
                binding.llApprovalIzinService.visibility = View.VISIBLE
                binding.llOperatorMenuService.visibility = View.GONE
                binding.llLeaderMenuService.visibility = View.VISIBLE

                binding.llBakLeader.setOnClickListener {
                    startActivity(Intent(requireContext(), ListDamageReportVendorActivity::class.java))
                }

                // menu mekari
                binding.llMekari.setOnClickListener {
                    if (statusRegis) {
                        mekariViewModel.getTokenMekari(userId)
                        getTokenMekariObserver()
                    } else {
                        startActivity(Intent(requireContext(), RegisterMekariActivity::class.java))
                    }
                }

                // menu training
                binding.llTrainingLeaders.setOnClickListener {
                    val intent = Intent(requireActivity(), Class.forName("com.digimaster.academy.features.authentication.splashscreen.IntroTrainingActivity"))
                    intent.putExtra("userNuc", CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, ""))
                    startActivity(intent)
                }

                binding.llIzinService.setOnClickListener {
                    val i = Intent(context, ListPermission::class.java)
                    startActivity(i)
                }
                binding.llLemburService.setOnClickListener {
                    val i = Intent(context, OvertimeTeamleadActivity::class.java)
                    startActivity(i)
                }
                binding.llMyTeam.setOnClickListener {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.CLICK_FROM,
                        "Service"
                    )
                    val i = Intent(requireContext(), MyteamChiefActivity::class.java)
                    startActivity(i)
                }
                binding.llSchedule.setOnClickListener {
                    val i = Intent(context, ScheduleMidActivity::class.java)
                    startActivity(i)
                }
                binding.llComplaintInternal.setOnClickListener {
//                            CarefastOperationPref.saveString(
//                                CarefastOperationPrefConst.CLICK_FROM, "Service"
//                            )
//                            val i = Intent(requireContext(), NotifVendorActivity::class.java)
//                            startActivity(i)
                    val i =
                        Intent(requireContext(), ListComplaintInternalVendorActivity::class.java)
                    startActivity(i)
                }
                binding.llApprovalService.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llApprovalService.setOnClickListener {
//                    CarefastOperationPref.saveString(CarefastOperationPrefConst.APPROVAL_CLICK_FROM, "leaders")
//                    val i = Intent(requireContext(), ApprovalLeaderActivity::class.java)
//                    startActivity(i)
                    Toast.makeText(
                        context,
                        "Fitur ini dalam pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.llTurnoverService.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llTurnoverService.setOnClickListener {
                    Toast.makeText(
                        context,
                        "Fitur ini dalam pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.llTukerJadwalService.setBackgroundResource(R.drawable.rounded_disable_new)
                binding.llTukerJadwalService.setOnClickListener {
                    Toast.makeText(
                        context,
                        "Fitur ini dalam pengembangan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        binding.llResignOperator.setOnClickListener {
            startActivity(Intent(requireContext(), ListReqResignVendorActivity::class.java))
        }
        //leader
        binding.llResign.setOnClickListener {
            startActivity(Intent(requireContext(), ListReqResignVendorActivity::class.java))
        }

        loadTrialMekari()
        setObsTrialMekari()
        loadData()
        countPermissionObserver()
//        setObserver()
    }

    private fun setObsTrialMekari() {
        mekariViewModel.getTrialMekariViewModel().observe(viewLifecycleOwner) {
            if (it.code == 200){
                binding.llMekari.visibility = View.GONE
                binding.llMekariOperator.visibility = View.GONE
                binding.tvMekariLeader.visibility = View.GONE
                binding.tvMekariOperator.visibility = View.GONE
            } else {
                binding.llMekari.visibility = View.GONE
                binding.llMekariOperator.visibility = View.GONE
                binding.tvMekariLeader.visibility = View.GONE
                binding.tvMekariOperator.visibility = View.GONE
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        bakViewMoel.getCheckUserVendorViewModel().observe(viewLifecycleOwner) {
            if (it.code == 200){
                if (it.message == "Y"){
                   if(userJabatan == "CS_SENIOR_CLEANER"){
                       //operator
                       binding.llBak.visibility = View.VISIBLE
                       binding.tvBak.visibility = View.VISIBLE
                       //pengawas
                       binding.llBakLeader.visibility = View.VISIBLE
                       binding.tvBakLeader.visibility = View.VISIBLE
                   }else{
                       binding.llBak.visibility = View.GONE
                       binding.tvBak.visibility = View.GONE

                       binding.llBakLeader.visibility = View.GONE
                       binding.tvBakLeader.visibility = View.GONE
                   }

                } else {
                    binding.llBak.visibility = View.GONE
                    binding.tvBak.visibility = View.GONE

                    binding.llBakLeader.visibility = View.GONE
                    binding.tvBakLeader.visibility = View.GONE
                }
                binding.llBak.setOnClickListener {
                    startActivity(Intent(requireContext(), ListDamageReportVendorActivity::class.java))
                }

                binding.llBakLeader.setOnClickListener {
                    startActivity(Intent(requireContext(), ListDamageReportVendorActivity::class.java))
                }
            }
        }

        viewModel.countPermissionMidModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                // progress cuti
                binding.progressBarCuti.progress = it.data.cutiPersentase
                val textCuti =
                    "<font color=#2B5281>${it.data.countCuti}</font> <font color=#1D1D1D> / </font> <font color=#F47721> 0 </font>"
                binding.tvProgressBarCuti.text = Html.fromHtml(textCuti)

                // progress izin
                binding.progressBarIzin.progress = it.data.izinPersentase
                val textIzin =
                    "<font color=#2B5281>${it.data.countIzin}</font> <font color=#1D1D1D> / </font> <font color=#F47721> 5 </font>"
                binding.tvProgressBarIzin.text = Html.fromHtml(textIzin)

                // progress alfa
                binding.progressBarAlfa.progress = it.data.alphaPersentase
                val textAlfa =
                    "<font color=#2B5281>${it.data.countAlpha}</font> <font color=#1D1D1D> / </font> <font color=#F47721> 3 </font>"
                binding.tvProgressBarAlfa.text = Html.fromHtml(textAlfa)

                // approval izin tim
                binding.tvCountApprovalIzinService.text = "${it.data.countIzinMenunggu} Menunggu"
            } else {
                Toast.makeText(context, "Gagal mengambil data progress izin", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun countPermissionObserver(){
        viewModel.countPermissionMidModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                // progress cuti
                binding.progressBarCuti.progress = it.data.cutiPersentase
                val textCuti =
                    "<font color=#2B5281>${it.data.countCuti}</font> <font color=#1D1D1D> / </font> <font color=#F47721> 0 </font>"
                binding.tvProgressBarCuti.text = Html.fromHtml(textCuti)

                // progress izin
                binding.progressBarIzin.progress = it.data.izinPersentase
                val textIzin =
                    "<font color=#2B5281>${it.data.countIzin}</font> <font color=#1D1D1D> / </font> <font color=#F47721> 5 </font>"
                binding.tvProgressBarIzin.text = Html.fromHtml(textIzin)

                // progress alfa
                binding.progressBarAlfa.progress = it.data.alphaPersentase
                val textAlfa =
                    "<font color=#2B5281>${it.data.countAlpha}</font> <font color=#1D1D1D> / </font> <font color=#F47721> 3 </font>"
                binding.tvProgressBarAlfa.text = Html.fromHtml(textAlfa)

                // approval izin tim
                binding.tvCountApprovalIzinService.text = "${it.data.countIzinMenunggu} Menunggu"
            } else {
                Toast.makeText(context, "Gagal mengambil data progress izin", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getTokenMekariObserver(){
        mekariViewModel.checkMekariResponse.observe(viewLifecycleOwner) {
            if (it.errorCode == 400) {
                binding.llMekari.setBackgroundResource(R.drawable.btn_disable)
                binding.llMekariOperator.setBackgroundResource(R.drawable.btn_disable)

                binding.llMekari.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        "Sementara fitur ini tidak tersedia di project Anda",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.llMekariOperator.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        "Sementara fitur ini tidak tersedia di project Anda",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        mekariViewModel.getTokenMekariViewModel().observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if (it.data.data.attributes.access_token.isEmpty()) {
                    Toast.makeText(requireContext(), "Failed to retrieve token", Toast.LENGTH_SHORT)
                        .show()
                } else {
//                    val PackageName = "com.mekari.flex.dev"
                    val PackageName = "com.mekari.flex" //prod

                    val packageManager: PackageManager = requireContext().packageManager
                    val appIntent = packageManager.getLaunchIntentForPackage(PackageName)
                    val isAppInstalled = appIntent != null

                    if (isAppInstalled) {
                        // is installed
                        val DEEP_LINK_URL =
                            "mekariflex://external_token/${it.data.data.attributes.access_token}"
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(DEEP_LINK_URL)
                        try {
                            requireContext().startActivity(intent)
                            Log.d("validate", "pindah")
                        } catch (e: ActivityNotFoundException) {
                            Log.d("validate", "no activity handle that")
                        }
                    } else {
                        //  not installed
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.mekari.flex")
                            )
                        )
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadTrialMekari() {
        mekariViewModel.getTrialMekari(projectCode, userId)
    }

    private fun loadData() {
        bakViewMoel.getCheckUserTechnician(userId)
        viewModel.getCountPermissionMid(userId, projectCode)
        mekariViewModel.getCheckMekari(userId)
    }

    @SuppressLint("SetTextI18n")
    private fun setCurrentMonthYear(month: String, year: String) {
        if (month == "January" || month == "Januari") {
            monthss = "Januari"
        } else if (month == "February" || month == "Februari") {
            monthss = "Februari"
        } else if (month == "March" || month == "Maret") {
            monthss = "Maret"
        } else if (month == "April" || month == "April") {
            monthss = "April"
        } else if (month == "May" || month == "Mei") {
            monthss = "Mei"
        } else if (month == "June" || month == "Juni") {
            monthss = "Juni"
        } else if (month == "July" || month == "Juli") {
            monthss = "Juli"
        } else if (month == "August" || month == "Agustus") {
            monthss = "Agustus"
        } else if (month == "September" || month == "September") {
            monthss = "September"
        } else if (month == "October" || month == "Oktober") {
            monthss = "Oktober"
        } else if (month == "November" || month == "Nopember") {
            monthss = "November"
        } else if (month == "December" || month == "Desember") {
            monthss = "Desember"
        }

        binding.tvCurrentMonthDateService.text = "$monthss $year"
    }
}