package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.bodClient

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentHomeBodClientBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.activity.ListProjectAbsentOprMgmntActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.ListBranchProjectManagementActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.features.grafik.ui.TurnOverActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class HomeBodClientFragment : Fragment() {

    private lateinit var binding: FragmentHomeBodClientBinding
    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private val userJob = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_POSITION, "")
    private var versionApp: String = ""

    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBodClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // new update app
        val manager = requireActivity().packageManager
        val info = manager.getPackageInfo(requireActivity().packageName, 0)
        versionApp = info.versionName
        binding.tvVersionHomeBodClient.text = versionApp

        homeViewModel.checkVersion(versionApp)
        homeViewModel.checkVersionAppModel.observe(viewLifecycleOwner) {
            if (it.message == "Your app need to update") {
                openDialogNewUpdate()
            }
        }

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.window!!.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.white)
        }

        // set user data
        binding.tvUserNameHomeBodClient.text = userName
        binding.tvUserProjectHomeBodClient.text = userJob

        // set menu attendance
        binding.rlAttendance.setOnClickListener {
            val i = Intent(requireContext(), ListProjectAbsentOprMgmntActivity::class.java)
            startActivity(i)
        }
        
        // set menu operational
        binding.rlOperational.setOnClickListener {
            val i = Intent(requireContext(), ListBranchProjectManagementActivity::class.java)
            startActivity(i)
        }

        // set menu project
        binding.rlProject.setOnClickListener {
            startActivity(Intent(requireContext(), ListBranchProjectManagementActivity::class.java))
        }

        binding.linearMr.setOnClickListener {
            val i = Intent(requireActivity(), ListBranchProjectManagementActivity::class.java).also {
                it.putExtra("type","mr")
            }
            startActivity(i)
        }

        binding.rlTurnOver.setOnClickListener {
            startActivity(Intent(requireContext(), TurnOverActivity::class.java))
        }

        binding.linearTimesheet.setOnClickListener {
            val i = Intent(requireActivity(), ListBranchProjectManagementActivity::class.java).also {
                it.putExtra("type","timesheet")
            }
            startActivity(i)
        }

        binding.linearMesin.setOnClickListener {
            val i = Intent(requireActivity(), ListBranchProjectManagementActivity::class.java).also {
                it.putExtra("type","machine")
            }
            startActivity(i)
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
        homeViewModel.newVersionAppModel.observe(viewLifecycleOwner) {
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