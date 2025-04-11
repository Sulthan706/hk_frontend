package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentServiceManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.activity.OvertimeManagementActivity
import com.hkapps.hygienekleen.features.features_management.service.permission.ui.activity.PermissionsApprovalManagementActivity
import com.hkapps.hygienekleen.features.features_vendor.service.approval.ui.activity.ApprovalLeaderActivity
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.viewmodel.MekariViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.util.*


class ServiceManagementFragment : Fragment() {

    private lateinit var binding: FragmentServiceManagementBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var monthss: String = ""
    private val statusRegis = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATUS_REGIS_MEKARI, false)

    private val viewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }

    private val mekariViewModel: MekariViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(MekariViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentServiceManagementBinding.inflate(inflater, container, false)
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
            binding.clServiceManagement.setBackgroundColor(R.color.white)
        }

        // validate button mekari
//        mekariViewModel.getCheckMekari(userId)
//        mekariViewModel.submitRegisMekariResponse.observe(viewLifecycleOwner) {
//            if (it.code == 200 || it.code == 201) {
//                binding.llMekari.visibility = View.VISIBLE
//            } else {
//                binding.llMekari.visibility = View.GONE
//            }
//        }

        // menu mekari
//        binding.llMekari.setOnClickListener {
//            if (statusRegis) {
//                val launchMekari = requireContext().packageManager.getLaunchIntentForPackage("com.mekari.flex")
//                if (launchMekari != null) {
//                    startActivity(launchMekari)
//                } else {
//                    startActivity(
//                        Intent(
//                            Intent.ACTION_VIEW,
//                            Uri.parse("https://play.google.com/store/apps/details?id=com.mekari.flex")
//                        )
//                    )
//                }
//            } else {
//                startActivity(Intent(requireContext(), RegisterMekariActivity::class.java))
//            }
//        }

        if (userLevel == "BOD" || userLevel == "CEO") {
            binding.clServiceBod.visibility = View.VISIBLE
            binding.clServiceManagement.visibility = View.GONE

            binding.clApprovalIzinServiceBod.setOnClickListener {
                startActivity(Intent(requireActivity(), PermissionsApprovalManagementActivity::class.java))
            }

            // menu training
            binding.llTrainingServiceBod.setOnClickListener {
                val intent = Intent(requireActivity(), Class.forName("com.digimaster.academy.features.authentication.splashscreen.IntroTrainingActivity"))
                intent.putExtra("userNuc", CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, ""))
                startActivity(intent)
            }
        } else {
            binding.clServiceBod.visibility = View.GONE
            binding.clServiceManagement.visibility = View.VISIBLE

            // set current month year
            val calendar: Calendar = Calendar.getInstance()
            val month = android.text.format.DateFormat.format("MMMM", calendar) as String
            val year = android.text.format.DateFormat.format("yyyy", calendar) as String
            val currentYear: Int = calendar.get(Calendar.YEAR)
            val currentMonth: Int = calendar.get(Calendar.MONTH)
            setCurrentMonthYear(month, year)

            // set progress status permissions
            viewModel.getCountProgressPermission(userId)
            viewModel.countProgressPermissionModel.observe(viewLifecycleOwner) {
                if (it.code == 200) {
                    // progress cuti
                    binding.progressBarCuti.progress = it.data.cutiPersentase
                    val textCuti = "<font color=#2B5281>${it.data.countCuti}</font> <font color=#1D1D1D> / </font> <font color=#F47721> 0 </font>"
                    binding.tvProgressBarCuti.text = Html.fromHtml(textCuti)

                    // progress izin
                    binding.progressBarIzin.progress = it.data.izinPersentase
                    val textIzin = "<font color=#2B5281>${it.data.countIzin}</font> <font color=#1D1D1D> / </font> <font color=#F47721> 5 </font>"
                    binding.tvProgressBarIzin.text = Html.fromHtml(textIzin)

                    // approval izin
                    binding.tvApprovalIzinService.text = it.data.countIzinMenunggu.toString()
                } else {
                    Toast.makeText(context, "Gagal mengambil data progress izin", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            when(userLevel) {
                "OM", "GM" -> {
                    binding.llLemburServiceManagement.setBackgroundResource(R.drawable.rounded_disable_new)
                    binding.llCutiServiceManagement.setBackgroundResource(R.drawable.rounded_disable_new)

                    // menu lembur
                    binding.llLemburServiceManagement.setOnClickListener {
                        Toast.makeText(
                            requireActivity(),
                            "Fitur dalam tahap pengembangan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    // menu cuti
                    binding.llCutiServiceManagement.setOnClickListener {
                        Toast.makeText(
                            requireActivity(),
                            "Fitur dalam tahap pengembangan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                "FM" -> {
                    binding.llLemburServiceManagement.setBackgroundResource(R.drawable.rounded_white)
                    binding.llCutiServiceManagement.setBackgroundResource(R.drawable.rounded_disable_new)

                    // menu lembur
                    binding.llLemburServiceManagement.setOnClickListener {
                        startActivity(Intent(requireActivity(), OvertimeManagementActivity::class.java))
                    }
                    // menu cuti
                    binding.llCutiServiceManagement.setOnClickListener {
                        Toast.makeText(
                            requireActivity(),
                            "Fitur dalam tahap pengembangan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }

            // menu izin
            binding.llIzinServiceManagement.setOnClickListener {
                startActivity(Intent(requireActivity(), PermissionsApprovalManagementActivity::class.java))
            }
            // menu approval
            binding.llApprovalServiceManagement.setOnClickListener {
                CarefastOperationPref.saveString(CarefastOperationPrefConst.APPROVAL_CLICK_FROM, "management")
                startActivity(Intent(requireActivity(), ApprovalLeaderActivity::class.java))
            }
            // menu training
//            binding.llTrainingServiceManagement.setOnClickListener {
//                val intent = Intent(requireActivity(), Class.forName("com.digimaster.academy.features.authentication.splashscreen.IntroTrainingActivity"))
//                intent.putExtra("userNuc", CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, ""))
//                startActivity(intent)
//            }

            // menu mekari
//            binding.llMekari.setOnClickListener {
//                startActivity(Intent(requireActivity(), RegisterMekariActivity::class.java))
//            }
        }

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
            monthss = "Nopember"
        } else if (month == "December" || month == "Desember") {
            monthss = "Desember"
        }

        binding.tvCurrentMonthDateService.text = "$monthss $year"
    }

}