package com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMyTeamClientHistoryDetailBinding
import com.hkapps.hygienekleen.features.features_client.myteam.viewmodel.MyTeamClientViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.ProfileOperationalActivity.Companion.CALL_REQ
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.PhoneNumberAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyTeamClientEmployeeDetail : AppCompatActivity(), PhoneNumberAdapter.ListPhoneNumberCallBack {
    private lateinit var binding: ActivityMyTeamClientHistoryDetailBinding
    //viewmodel
    private val viewModel: MyTeamClientViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamClientViewModel::class.java)
    }
    //pref
    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private val employeeIdCfteam =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_EMPLOYEE_CFTEAM, 0)

    //val
    private val phoneNumber = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMyTeamClientHistoryDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        binding.imgTelp.setOnClickListener {
            permissionCall()
        }

        val sdf = SimpleDateFormat("MMMM")
        val currentDate = sdf.format(Date())
        binding.tvDateMonthDetailEmployee.text = currentDate
        //oncreate
        setObserver()
    }

    private fun permissionCall() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            openDialogPhoneNumber()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CALL_PHONE),
            CALL_REQ
        )
    }

    //fun
    private fun setObserver(){
        viewModel.getDetailListCfteam(projectId, employeeIdCfteam)
        viewModel.getDetailCfteamViewModel().observe(this, Observer {
            val i = it.data.employeeId
            val jobCode = it.data.employeeJob
            if (it.code == 200){
                binding.llBtnHistoryAttendanceEmployee.setOnClickListener {
                    val a = Intent(this,MyTeamAttendanceHistoryDetailActivity::class.java)
                    startActivity(a)
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_EMPLOYEE_CFTEAM, i)
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.JOBCODE_CFTEAM, jobCode)
                }
                binding.tvCountDayScheduleOff.text = "${it.data.scheduleOff} Hari"
                binding.tvCountDayScheduleIn.text = "${it.data.scheduleHadir} Hari"
                binding.tvHadirCountProfile.text = if (it.data.scheduleHadir == null){
                    "-"
                } else {
                    "${it.data.scheduleHadir}"
                }
                binding.tvMngmntNameCfteam.text = if (it.data.employeeName == null){
                    "-"
                } else {
                    it.data.employeeName
                }
                binding.tvMngmntJobNameCfteam.text = if ( it.data.employeeJob == null ){
                    "-"
                } else {
                    it.data.employeeJob
                }
                binding.tvMngmntJobCodeCfteam.text = if (it.data.employeeCode == null){
                    "-"
                } else {
                    it.data.employeeCode
                }
                binding.tvCountDayScheduleAlpha.text = if (it.data.scheduleAlpha == null){
                    "-"
                } else {
                    it.data.scheduleAlpha.toString()
                }
                binding.tvCountDaySchedulePermission.text = if (it.data.scheduleIzin == null) {
                    "-"
                } else {
                    it.data.scheduleIzin.toString()
                }
                //circullar progress
                binding.progressBarDetailCfteam.progress = 60
                val textCuti = "<font color=#1D1D1D>${it.data.totalPercentage}%</font><font color=#2B5281>"
                binding.tvProgressBarDetailCfteam.text = Html.fromHtml(textCuti)
                //telp
                Log.d("TAG", "hasil persentase = ${it.data.totalPercentage}")


                val length = it.data.employeePhoneNumber.size
                for (i in 0 until length) {
                    if (it.data.employeePhoneNumber[i].employeePhone == null ||
                        it.data.employeePhoneNumber[i].employeePhone == "null" ||
                        it.data.employeePhoneNumber[i].employeePhone == "") {
                        phoneNumber.add("")
                    } else {
                        phoneNumber.add(it.data.employeePhoneNumber[i].employeePhone)
                    }
                }

            }
        })
    }
    @SuppressLint("SetTextI18n")
    private fun openDialogPhoneNumber() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom_phone_number)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val recyclerView = dialog.findViewById(R.id.rvDialogPhoneNumber) as RecyclerView
        val btnClose = dialog.findViewById(R.id.ivCloseDialogPhoneNumber) as ImageView
        val tvEmpty = dialog.findViewById<TextView>(R.id.tvEmptyDialogPhoneNumber)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        // validate no phone number
        when (phoneNumber.size) {
            1 -> {
                if (phoneNumber[0] == "") {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
            2 -> {
                if (phoneNumber[0] == "" && phoneNumber[1] == "") {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
            3 -> {
                if (phoneNumber[0] == "" && phoneNumber[1] == "" && phoneNumber[2] == "") {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager


        // set rv adapter
        val rvPhoneAdapter = PhoneNumberAdapter(
            this,
            phoneNumber
        ).also { it.setListener(this) }
        recyclerView.adapter = rvPhoneAdapter

        dialog.show()
        }

    override fun onClickPhone(phoneNumber: String) {
        val call = Intent(Intent.ACTION_CALL)
        call.data = Uri.parse("tel:$phoneNumber")
        startActivity(call)
    }


}