package com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDailyClosingManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.DailyTargetManagement
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ListDailyTargetClosing
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.adapter.DailyClosingManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.viewmodel.ClosingManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeManagementActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DailyClosingManagementActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDailyClosingManagementBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(intent.getBooleanExtra("done",false)){
                startActivity(Intent(this@DailyClosingManagementActivity,HomeManagementActivity::class.java))
                finishAffinity()
            }else if(intent.getBooleanExtra("from_home",false)){
                startActivity(Intent(this@DailyClosingManagementActivity,HomeManagementActivity::class.java))
                finishAffinity()
            }else{
                finish()
            }
        }
    }

    private lateinit var dailyClosingManagementAdapter : DailyClosingManagementAdapter

    private val closingManagementViewModel by lazy {
        ViewModelProvider(this)[ClosingManagementViewModel::class.java]
    }

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var flag = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyClosingManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }


    private fun initView(){
        val appBarName = "Closing Harian Project"
        binding.appBarClosing.tvAppbarTitle.text = appBarName
        binding.appBarClosing.ivAppbarBack.setOnClickListener {
            if(intent.getStringExtra("done") != null){
                finish()
            }else if(intent.getBooleanExtra("from_home",false)){
                startActivity(Intent(this,HomeManagementActivity::class.java))
                finishAffinity()
            }
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)


       binding.shimmerLayout.startShimmerAnimation()
        Handler().postDelayed({
            startLiveDateTime()
            getDataClosingManagement()
        },1000)

    }

    private fun  startLiveDateTime(){

        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale("id", "ID"))

        val timeZone = TimeZone.getDefault()
        dateFormat.timeZone = timeZone

        val timeZoneAbbreviation = when (timeZone.id) {
            "Asia/Jakarta" -> "WIB"
            "Asia/Makassar", "Asia/Ujung_Pandang" -> "WITA"
            "Asia/Jayapura" -> "WIT"
            else -> timeZone.displayName
        }

        lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                val dateTime = "${dateFormat.format(Date())} $timeZoneAbbreviation"
                binding.tvDateTime.text = dateTime
                delay(1000)
            }
        }
    }

    private fun getDataClosingManagement(){
        closingManagementViewModel.getListDalyTarget(userId,getTodayDate())
        closingManagementViewModel.listDailyTargetModel.observe(this){
            if(it.code == 200){
                binding.shimmerLayout.stopShimmerAnimation()
                binding.shimmerLayout.visibility = View.GONE

                if(it.data.isNotEmpty()){
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvClosing.visibility = View.VISIBLE
                    showRecycler(it.data)
                }
            }else{
                binding.shimmerLayout.stopShimmerAnimation()
                binding.shimmerLayout.visibility = View.GONE
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvClosing.visibility = View.GONE
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRecycler(data : List<ListDailyTargetClosing>){
        dailyClosingManagementAdapter = DailyClosingManagementAdapter(this,data.toMutableList(),object : DailyClosingManagementAdapter.OnClickDailyClosingManagement{
            override fun onCLickDetail(data: DailyTargetManagement,isYesterday : Boolean) {
                checkClosing(data.projectCode,data.date,isYesterday)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.INTENT_FIRST,data.projectName)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT,data.projectCode)
            }

            override fun onClickDetailYesterday(data: DailyTargetManagement) {
                checkClosing(data.projectCode,data.date,false)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT,data.projectCode)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.INTENT_FIRST,data.projectName)
            }

            override fun onClickHistory(data: DailyTargetManagement) {
                CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT,data.projectCode)
                startActivity(Intent(this@DailyClosingManagementActivity,HistoryClosingManagementActivity::class.java))
            }
        })

        val rvLayoutManager = LinearLayoutManager(this@DailyClosingManagementActivity)
        binding.rvClosing.apply {
            adapter = dailyClosingManagementAdapter
            layoutManager = rvLayoutManager
        }
    }

    private fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun checkClosing(projectCode : String,date : String,isYesterday : Boolean){
        closingManagementViewModel.checkClosingChief(projectCode)
        closingManagementViewModel.checkClosingChiefModel.observe(this){
            if(it != null){
                if(it.code == 200 && it.message.equals("VALID",ignoreCase = true)){
                    startActivity(Intent(this@DailyClosingManagementActivity,ClosingAreaManagementActivity::class.java).also{
                        it.putExtra("projectCode",projectCode)
                        it.putExtra("date",date)
                        it.putExtra("isYesterday",isYesterday)
                    })
                    finish()
                }else{
                    if(it.code == 400 && it.message.equals("INVALID",ignoreCase = true)){
                        if(flag == 0){
                            showDialogNotClosing()
                        }
                        flag = 1

                    }else{
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                closingManagementViewModel.checkClosingChiefModel.value = null
            }
        }
    }

    private fun showDialogNotClosing() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_cant_closing)
        val yesBtn = dialog.findViewById<AppCompatButton>(R.id.btn_check_and_closing_cant_closing)
        val tv = dialog.findViewById<TextView>(R.id.tv_project_name_cant_closing)
        tv.text = CarefastOperationPref.loadString(CarefastOperationPrefConst.INTENT_FIRST,"")
        yesBtn.setOnClickListener {
            flag = 0
            dialog.dismiss()
        }
        dialog.show()
    }
}