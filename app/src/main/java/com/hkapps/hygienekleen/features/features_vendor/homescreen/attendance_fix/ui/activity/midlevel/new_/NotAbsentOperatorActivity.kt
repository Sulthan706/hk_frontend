package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_

import android.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityAttendanceListOperatorBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.PhoneNumberAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.*
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_not_absent.EmployeeNotAttendance
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_not_absent.EmployeePhoneNumberNotAttendance
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.new_.midlevel.AttendanceFixMidListStaffNotAttendanceAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton

class NotAbsentOperatorActivity : AppCompatActivity(),
    AttendanceFixMidListStaffNotAttendanceAdapter.PhoneCallback,
    PhoneNumberAdapter.ListPhoneNumberCallBack {

    private var listener: NoInternetConnectionCallback? = null
    private lateinit var binding: ActivityAttendanceListOperatorBinding
    private lateinit var vm: AttendanceFixViewModel
    private var name: String = ""
    private var selectedShift: Int = 0
    private lateinit var notAttendanceAdapter: AttendanceFixMidListStaffNotAttendanceAdapter
    private lateinit var responseModel: AttendanceListStaffNotAttendanceResponseModel
    private lateinit var responseModelSPV: AttendanceListStaffNotAttendanceSPVResponseModel
    private lateinit var responseModelCSPV: AttendanceListStaffNotAttendanceCSPVResponseModel
    companion object {
        const val CALL_REQ = 101
    }
    private val attendanceId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ATTENDANCE_ID, 0)

    private val attendanceSTR =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ATTENDANCE_STR, "")

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


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceListOperatorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.layoutAppbarAttendanceListOperator.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        binding.layoutAppbarAttendanceListOperator.tvAppbarTitle.text = "Absensi Staff Hari Ini"


        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvStaffNotAttendance.layoutManager = layoutManager


        //fab
        binding.btnHistoryAttendanceTim.setOnClickListener {
            val i = Intent(this, FabHistoryAttendanceTeamActivity::class.java)
            startActivity(i)
        }
        binding.btnHistoryAttendancePerson.setOnClickListener {
            val i = Intent(this, FabHistoryAttendancePersonActivity::class.java)
            startActivity(i)
        }

        //floating action
        val maddFab = binding.addFab

        val maddHistoryTeamFab = binding.btnHistoryAttendanceTim
        val maddHistoryPersonFab = binding.btnHistoryAttendancePerson
        val maadHistoryCancelFab = binding.addFabCancel

        maadHistoryCancelFab.visibility = View.INVISIBLE
        maddHistoryTeamFab.visibility = View.GONE
        maddHistoryPersonFab.visibility = View.GONE


        var isAllVisibleFalse = false

        maddFab.setOnClickListener {


            if (!isAllVisibleFalse!!) {
                maddHistoryPersonFab.show()
                maddHistoryTeamFab.show()
                maddFab.setImageDrawable(getDrawable(R.drawable.ic_menu_close_clear_cancel))
                isAllVisibleFalse = true
            } else {
                maddFab.setImageDrawable(getDrawable(com.hkapps.hygienekleen.R.drawable.ic_history_attendance_floating))
                maddHistoryTeamFab.hide()
                maddHistoryPersonFab.hide()
                isAllVisibleFalse = false
            }
        }
        if (selectedShift == 0) {
            binding.llBtnAttendanceOut.setOnClickListener {
                Toast.makeText(this, "Pilih shift dahulu", Toast.LENGTH_SHORT).show()
            }
        } else {
            binding.llBtnAttendanceOut.setOnClickListener {
                val i = Intent(this, AlreadyAbsentOperatorActivity::class.java)
                startActivity(i)
            }
        }


        setObserver()
        setObserverListStaff()
        attedanceViewModel.getShift(projectCode)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        attedanceViewModel.shift.observe(this, Observer {
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
                this, R.layout.simple_spinner_item,
                data
            )

            adapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
            binding.spinnerChooseShift.adapter = adapter

            binding.spinnerChooseShift.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d("TAG", "onItemSelected: $attendanceSTR")

                    CarefastOperationPref.saveInt(
                        CarefastOperationPrefConst.SHIFT_ID,
                        it.data[position].shift.shiftId
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.SHIFT_NAME,
                        it.data[position].shift.shiftDescription
                    )
                    val shiftSelected = it.data[position].shift.shiftId
                    selectedShift = it.data[position].shift.shiftId
                    attedanceViewModel.getListAttendanceNotAbsent(
                        userId,
                        projectCode,
                        shiftSelected
                    )
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

        attedanceViewModel.listAttendanceNotAbsent().observe(this, Observer {
            if (it.code == 200) {

                binding.tvNotAttendanceStaff.text = if (it.data.countListEmployeeBelumAbsen == null){
                    "0"
                } else {
                    it.data.countListEmployeeBelumAbsen.toString()
                }

                binding.tvCountTotalAttendanceIn.text = if (it.data.countStaff == null){
                    "0"
                } else {
                    it.data.countStaff.toString()
                }


                if (it.data.listEmployee.isNullOrEmpty()) {
                    binding.llListAttendanceLoadingState.visibility = View.GONE
                    binding.tvEmptyStateNotAbsent.visibility = View.VISIBLE
                }

                if (it.data.countListEmployeeBelumAbsen == null) {
                    binding.llBtnAttendanceOut.setOnClickListener {
                        Toast.makeText(this, "Pilih shift dahulu", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    binding.llBtnAttendanceOut.setOnClickListener {
                        val i = Intent(this, AlreadyAbsentOperatorActivity::class.java)
                        startActivity(i)
                    }
                }

//            responseModel = it
                notAttendanceAdapter =
                    AttendanceFixMidListStaffNotAttendanceAdapter(
                        it.data.listEmployee as ArrayList<EmployeeNotAttendance>
                    ).also { it.setListener(this) }
                binding.rvStaffNotAttendance.adapter = notAttendanceAdapter


            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        })
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }


    override fun onBackPressed() {
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.ATTENDANCE_STR,
            ""
        )

        CarefastOperationPref.saveInt(
            CarefastOperationPrefConst.ATTENDANCE_ID,
            0
        )

        super.onBackPressed()
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun openDialogPhoneNumber(phoneNumbers: List<EmployeePhoneNumberNotAttendance>) {
        val dialog = Dialog(this)
        dialog.setContentView(com.hkapps.hygienekleen.R.layout.dialog_custom_phone_number_operator)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val recyclerView =
            dialog.findViewById(com.hkapps.hygienekleen.R.id.rvDialogPhoneNumber) as RecyclerView
        val btnClose =
            dialog.findViewById(com.hkapps.hygienekleen.R.id.ivCloseDialogPhoneNumber) as ImageView
        val tvEmpty =
            dialog.findViewById<TextView>(com.hkapps.hygienekleen.R.id.tvEmptyDialogPhoneNumber)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

//        val phonee = phoneNumbers[0].employeePhone

        val phoneNumber = ArrayList<String>()
        val length = phoneNumbers.size
        for (i in 0 until length) {
            if (phoneNumbers[i].employeePhone == null || phoneNumbers[i].employeePhone == "null" || phoneNumbers[i].employeePhone == "") {
                phoneNumber.add("")
            } else {
                phoneNumber.add(phoneNumbers[i].employeePhone)
                Log.d("telp", "${phoneNumbers[i].employeePhone}")
            }
        }


//         set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

//         set rv adapter
        val rvPhoneAdapter = PhoneNumberAdapter(
            this,
            phoneNumber
        ).also { it.setListener(this) }
        recyclerView.adapter = rvPhoneAdapter
        Log.d("test", "$phoneNumber")
        dialog.show()
    }

    override fun onClickOperator(employeePhoneNumber: List<EmployeePhoneNumberNotAttendance>) {
        openDialogPhoneNumber(employeePhoneNumber)
        Log.d("dialog", "$employeePhoneNumber")
    }

    override fun onClickPhone(phoneNumber: String) {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + phoneNumber)
            startActivity(callIntent)
        }
    }
    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CALL_PHONE),CALL_REQ
        )
    }

}
