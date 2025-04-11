package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.fragment.old.midlevel

import android.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.FragmentAttendanceStaffBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.AttendanceListStaffNotAttendanceResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_not_absent.EmployeeNotAttendance
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.old.midlevel.AttendanceFixMidListStaffNotAttendanceAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton


class AttendanceFixMidStaffFragment : Fragment() {
    private var listener: NoInternetConnectionCallback? = null
    private lateinit var binding: FragmentAttendanceStaffBinding
    private lateinit var vm: AttendanceFixViewModel
    var name: String = ""

    private lateinit var notAttendanceAdapter: AttendanceFixMidListStaffNotAttendanceAdapter
    private lateinit var responseModel: AttendanceListStaffNotAttendanceResponseModel

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private val attedanceViewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }

    private lateinit var rvSkeleton: Skeleton
    private var loadingDialog: Dialog? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    fun setListeners(listener: NoInternetConnectionCallback, name: String) {
        this.listener = listener
        this.name = name
    }

    companion object {
        @JvmStatic
        fun newInstance(misaType: Int) =
            AttendanceFixMidStaffFragment().apply {
                arguments = Bundle().apply {
//                    putInt(type, misaType)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = activity?.run {
            ViewModelProviders.of(this)[AttendanceFixViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAttendanceStaffBinding.inflate(layoutInflater)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {

        }
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvStaffNotAttendance.layoutManager = layoutManager

//        binding.tvNotAttendance.text = "0"
        binding.tvCountTotalAttendanceIn.text = " /0"

        attedanceViewModel.getShift(projectCode)
        setObserver()
    }

    //INI GET DATA BUAT DITARO DI ADAPTERNYA
    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        attedanceViewModel.shift.observe(requireActivity(), Observer {
//            val values : Array<String> = arrayOf("USD", "UAH", "GBD", "EUR", "BIT", "RUB")
            var data = ArrayList<String>()
//            data.add(it.data[0].shift.shiftDescription)
//            data.add(it.data[1].shift.shiftDescription)
//            data.add(it.data[2].shift.shiftDescription)

            val length = it.data.size

            Log.d("TAG", "length: $length")
            for (i in 0 until length) {
                data.add(it.data[i].shift.shiftDescription)
            }

            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                requireContext(), R.layout.simple_spinner_item,
                data
            )

            adapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
            binding.spinnerChooseShift.adapter = adapter

            binding.spinnerChooseShift.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    attedanceViewModel.getListStaffNotAttendance(
                        userId,
                        projectCode,
                        it.data[position].shift.shiftId
                    )
                    setObserverListStaff()
                    rvSkeleton =
                        binding.rvStaffNotAttendance.applySkeleton(com.hkapps.hygienekleen.R.layout.item_shimmer)
                    rvSkeleton.showSkeleton()
                    showLoading(getString(com.hkapps.hygienekleen.R.string.loading_string))
                    binding.llListAttendanceLoadingState.visibility = View.GONE
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setObserverListStaff() {
        attedanceViewModel.notAttendanceResponseModel.observe(requireActivity(), Observer {
            binding.tvNotAttendanceStaff.text = "" + it.data.countListEmployeeBelumAbsen
            binding.tvCountTotalAttendanceIn.text = "" + it.data.countStaff

            if(it.data.countStaff >= 0){
                binding.llListAttendanceLoadingState.visibility = View.GONE
            }

//            responseModel = it
            notAttendanceAdapter =
                AttendanceFixMidListStaffNotAttendanceAdapter(
                    it.data.listEmployee as ArrayList<EmployeeNotAttendance>
                )
            binding.rvStaffNotAttendance.adapter = notAttendanceAdapter

//            if (it.data.countListEmployeeBelumAbsen.isNotEmpty()) {
//                //set timer cuma 1.1 detik buat loadingnya
//                val progressRunnable = Runnable {
//                    binding.rvStaffNotAttendance.visibility = View.VISIBLE
//                    hideLoading()
//                }
//                val pdCanceller = Handler()
//                pdCanceller.postDelayed(progressRunnable, 1500)
//            } else {
//                binding.rvStaffNotAttendance.visibility = View.VISIBLE
//                binding.llListAttendanceLoadingState.visibility = View.VISIBLE
////                binding.flConnectionTimeoutHistory.visibility = View.VISIBLE
////                noDataState()
//                hideLoading()
////                onSNACK(binding.root, "Tidak ada data.")
//            }
            hideLoading()
        })
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(requireContext(), loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
}