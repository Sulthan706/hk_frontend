package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentAttendanceBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.activity.*
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.adapter.AttendanceDailyAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.viewmodel.AttendanceViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback
import com.faltenreich.skeletonlayout.Skeleton
import com.google.android.material.snackbar.Snackbar

class AttendanceFragment : Fragment() {

    private var listener: NoInternetConnectionCallback? = null

    private lateinit var dailyActAdapter: AttendanceDailyAdapter
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var rvSkeleton: Skeleton
    private lateinit var dailyResponseModel: DailyActResponseModel
    private val attedanceViewModel: AttendanceViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceViewModel::class.java)
    }

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
        
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    fun setListener(listener: NoInternetConnectionCallback) {
        this.listener = listener
    }

    companion object {
        @JvmStatic
        fun newInstance(params: Int) =
            AttendanceFragment().apply {
                arguments = Bundle().apply {
//                    putInt(params)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttendanceBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
//            tipeTiket = it.getInt(TICKET_TYPE)
        }

//        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        binding.rvAttendanceDaily.layoutManager = layoutManager
//
//        rvSkeleton = binding.rvAttendanceDaily.applySkeleton(R.layout.item_attendance_daily)
//        rvSkeleton.showSkeleton()
//        attedanceViewModel.getDailyAct(employeeId, projectCode)
//        attedanceViewModel.getStatusAttendance(employeeId, projectCode)


//        binding.tvTodayTask.setOnClickListener {
//            val i = Intent(context, TestCamera::class.java)
//            startActivity(i)
////            requireActivity().finishAffinity()
//        }

        binding.llCheckOut.setOnClickListener {
            val i = Intent(context, AttendanceDailyCheckOutActivity::class.java)
            startActivity(i)
//            requireActivity().finishAffinity()
        }

        //Sementara
//        binding.tvSelfAttendance.setOnClickListener {
//            val i = Intent(context, QRCodeGenerator::class.java)
//            startActivity(i)
////            requireActivity().finishAffinity()
//        }

        getStatusAttendance()
    }

    @SuppressLint("SetTextI18n")
    private fun getStatusAttendance() {
        attedanceViewModel.attStatus.observe(requireActivity(), {
            if (it.code == 200) {
                //get status nya disini
                when (it.data.statusAttendance) {
                    "Selesai" -> {

//                        binding.tvSeeAllDailyAct.setOnClickListener {
//                            Toast.makeText(
//                                context,
//                                "Anda sudah Selesai mengerjakan.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//
//                        binding.rvAttendanceDaily.visibility = View.GONE
//                        binding.tvAttendanceDaily.text = "Tugas anda sudah selesai."
//                        binding.tvAttendanceDaily.visibility = View.VISIBLE
//                        binding.llCheckIn.setOnClickListener {
//                            Toast.makeText(
//                                context,
//                                "Anda sudah check in, status anda sudah selesai Bertugas.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                    "Bertugas" -> {
//
//                        setObserver()
//                        binding.tvSeeAllDailyAct.setOnClickListener {
//                            val i = Intent(context, AttendanceDailyActAllActivity::class.java)
//                            startActivity(i)
//                        }
//                        binding.rvAttendanceDaily.visibility = View.VISIBLE
//                        binding.tvAttendanceDaily.visibility = View.GONE
//                        binding.llCheckIn.setOnClickListener {
//                            Toast.makeText(
//                                context,
//                                "Anda sudah check in, status anda sedang Bertugas.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                    "Belum Absen" -> {
//
//                        binding.tvSeeAllDailyAct.setOnClickListener {
//                            Toast.makeText(
//                                context,
//                                "Anda belum Absen.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//
//                        binding.rvAttendanceDaily.visibility = View.GONE
//                        binding.tvAttendanceDaily.text = "Anda belum absen"
//                        binding.tvAttendanceDaily.visibility = View.VISIBLE
//
//                        binding.llCheckIn.setOnClickListener {
//                            val i = Intent(context, AttendanceDailyCheckInActivity::class.java)
//                            startActivity(i)
//                        }
                    }
                }
            }else{
                onSNACK(binding.root)
            }
        })
    }


    //INI GET DATA BUAT DITARO DI ADAPTERNYA
    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        attedanceViewModel.dailyActResponseModel.observe(requireActivity(), Observer {
            if (it.code == 200) {
                if (it.dailyActDataResponseModel.dailyActDataArrayResponseModel.isNotEmpty()) {
//                    binding.rvAttendanceDaily.visibility = View.VISIBLE
//                    binding.tvAttendanceDaily.visibility = View.GONE

                    dailyResponseModel = it
                    dailyActAdapter =
                        AttendanceDailyAdapter(
                            it.dailyActDataResponseModel.dailyActDataArrayResponseModel
                        )
//                    dailyResponseModel.dailyActDataResponseModel.addAll(
//                        it.dailyActDataResponseModel
//                    )
//                    dailyActAdapter.notifyItemRangeChanged(
//                        dailyResponseModel.dailyActDataResponseModel.size -
//                                it.dailyActDataResponseModel.size,
//                        dailyResponseModel.dailyActDataResponseModel.size
//                    )
//                    binding.rvAttendanceDaily.adapter = dailyActAdapter
//
//                    Log.d("TAG", "setObserver: " + it.dailyActDataResponseModel)
//                } else {
//                    binding.rvAttendanceDaily.visibility = View.GONE
////                    binding.tvAttendanceDaily.text = "Belum ada data"
                }
            } else {
                onSNACK(binding.root)
            }
        })
    }

    //Snack bar kesalahan
    private fun onSNACK(view: View) {
        val snackbar = Snackbar.make(
            view, "Terjadi kesalahan",
            Snackbar.LENGTH_LONG
        ).setAction("Error", null)
        snackbar.setActionTextColor(resources.getColor(R.color.primary_color))
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(resources.getColor(R.color.primary_color))
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 12f
        snackbar.show()
    }

}