package com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.activity

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMyTeamClientScheduleBinding
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamscheduleteam.Operator
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamscheduleteam.Pengawas
import com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.adapter.MyTeamListOperatorAdapter
import com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.adapter.MyTeamListPengawasAdapter
import com.hkapps.hygienekleen.features.features_client.myteam.viewmodel.MyTeamClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MyTeamClientScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyTeamClientScheduleBinding
    private val viewModel: MyTeamClientViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamClientViewModel::class.java)
    }
    //pref
    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    //adapter
    //val
    private var loadingDialog: Dialog? = null
    private var shiftId: Int = 0
    private var msg: String = ""
    private val shiftValue = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMyTeamClientScheduleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        binding.layoutAppbarCfteam.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutAppbarCfteam.tvAppbarTitle.text = "Schedule Team"

        var sdf = SimpleDateFormat("yyyy-MM-dd")
        msg = sdf.format(Date())

        val datePicker = findViewById<DatePicker>(R.id.llCalendarCfTeam)
        val today = Calendar.getInstance()
        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            var month = month + 1
            var monthreal: String = month.toString()
            var dayDate: String = day.toString()
            var yearReal: String = year.toString()

            //validasi 0
            if (month <= 10) {
                monthreal = "0" + month
            }
            if (day < 10) {
                dayDate = "0" + dayDate
            }
            shiftId = 0
            binding.spinnerShiftAreaSchedule.clearSelection()
            defaultLayout()
            msg = "$year-$monthreal-$dayDate"


        }
        viewModel.getListShiftCfteam(projectId)
        viewModel.getListShiftCfteamViewModel().observe(this){
            if (it.code == 200){
                val sizeShift = it.data.size
                for (i in 0 until sizeShift){
                    shiftValue.add(it.data[i].shiftDescription)
                }
                binding.spinnerShiftAreaSchedule.adapter = ArrayAdapter(this, R.layout.spinner_item, shiftValue)
                binding.spinnerShiftAreaSchedule.onItemSelectedListener =object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        shiftId = it.data[position].shiftId
                        viewModel.getScheduleCfteam(projectId, msg, it.data[position].shiftId)
                        showLoading("Loading text..")
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }

        //set recyclerview


        val layoutManagerPengawas = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPengawasCalendarTeamArea.layoutManager = layoutManagerPengawas

        val layoutManagerOperator = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvOperatorCalendarTeamArea.layoutManager = layoutManagerOperator
        //oncreate

        setObserver()
    }

    private fun defaultLayout(){
        binding.rvPengawasCalendarTeamArea.visibility = View.GONE
        binding.rvOperatorCalendarTeamArea.visibility = View.GONE
        binding.tvDefaultOperatorCalendarTeamArea.visibility = View.VISIBLE
        binding.tvDefaultPengawasCalendarTeamArea.visibility = View.VISIBLE

    }
    
    @SuppressLint("SetTextI18n")
    private fun setObserver(){
        viewModel.getScheduleCfteamViewModel().observe(this, Observer { 
            if (it.code == 200) {
                binding.rvPengawasCalendarTeamArea.visibility = View.VISIBLE
                binding.rvOperatorCalendarTeamArea.visibility = View.VISIBLE

                binding.tvJamKerjaReport.text = "${it.data.shiftStartAt}-${it.data.shiftEndAt}"
                //validasi empty pengawas
                if (it.data.listPengawas.isEmpty()){
                    binding.tvDefaultPengawasCalendarTeamArea.visibility = View.VISIBLE
                    binding.rvPengawasCalendarTeamArea.visibility = View.GONE
                    binding.tvDefaultPengawasCalendarTeamArea.text = "Tidak ada cfteam yang bertugas"
                } else {
                    binding.tvDefaultPengawasCalendarTeamArea.visibility = View.GONE
                    binding.rvPengawasCalendarTeamArea.adapter = MyTeamListPengawasAdapter(this, it.data.listPengawas as ArrayList<Pengawas>)
                }
                //validasi empty operator
                if(it.data.listOperator.isEmpty()){
                    binding.tvDefaultOperatorCalendarTeamArea.visibility = View.VISIBLE
                    binding.rvOperatorCalendarTeamArea.visibility = View.GONE
                    binding.tvDefaultOperatorCalendarTeamArea.text = "Tidak ada cfteam yang bertugas"
                } else {
                    binding.tvDefaultOperatorCalendarTeamArea.visibility = View.GONE
                    binding.rvOperatorCalendarTeamArea.adapter = MyTeamListOperatorAdapter(this, it.data.listOperator as ArrayList<Operator>)
                }

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        })
    }
    //fun

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
}