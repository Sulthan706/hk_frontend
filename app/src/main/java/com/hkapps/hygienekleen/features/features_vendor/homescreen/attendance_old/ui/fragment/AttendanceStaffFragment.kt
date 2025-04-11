package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.FragmentAttendanceStaffBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.viewmodel.AttendanceViewModel
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback

class AttendanceStaffFragment : Fragment() {
    private var listener: NoInternetConnectionCallback? = null
    private lateinit var binding: FragmentAttendanceStaffBinding
    private lateinit var vm: AttendanceViewModel
    var name: String = ""

    fun setListeners(listener: NoInternetConnectionCallback, name: String) {
        this.listener = listener
        this.name = name
    }

    companion object {
        @JvmStatic
        fun newInstance(misaType: Int) =
            AttendanceStaffFragment().apply {
                arguments = Bundle().apply {
//                    putInt(type, misaType)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = activity?.run {
            ViewModelProviders.of(this)[AttendanceViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttendanceStaffBinding.inflate(layoutInflater)

//        binding.llAttendanceChoose.setOnClickListener {
////            Toast.makeText(context, "COOL",Toast.LENGTH_SHORT).show()
//            val i = Intent(context, AttendanceChooseStaffActivity::class.java)
//            startActivity(i)
//            activity?.finishAffinity()
//        }
//
//        binding.llCheckIn.setOnClickListener {
//            val i = Intent(context, AttendanceChooseStaffCheckInActivity::class.java)
//            startActivity(i)
//            activity?.finishAffinity()
//        }
//
//        binding.llCheckOut.setOnClickListener {
//            val i = Intent(context, AttendanceChooseStaffCheckOutActivity::class.java)
//            startActivity(i)
//            activity?.finishAffinity()
//        }
//
//        if (name == null || name.equals("null")) {
//            binding.tvYourStaff.text = "Pilih Staff Anda"
//        } else {
//            binding.tvYourStaff.text = name
//        }

        return binding.root
    }
}