package com.hkapps.hygienekleen.features.features_client.report.ui.new_.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityReportCalenderBinding
import com.hkapps.hygienekleen.features.features_client.report.model.jadwalkerja.Operator
import com.hkapps.hygienekleen.features.features_client.report.model.jadwalkerja.Pekerjaan
import com.hkapps.hygienekleen.features.features_client.report.model.jadwalkerja.Pengawas
import com.hkapps.hygienekleen.features.features_client.report.ui.new_.adapter.ListDacCalenderAdapter
import com.hkapps.hygienekleen.features.features_client.report.ui.new_.adapter.ListOperatorReportAdapter
import com.hkapps.hygienekleen.features.features_client.report.ui.new_.adapter.ListPengawasReportAdapter
import com.hkapps.hygienekleen.features.features_client.report.viewmodel.ReportClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.util.*
import kotlin.collections.ArrayList

class ReportCalenderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportCalenderBinding
    private val viewModel: ReportClientViewModel by lazy {
        ViewModelProviders.of(this).get(ReportClientViewModel::class.java)
    }
    //pref
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")

    //val
    private var areaId = 0
    private var locationId = 0
    private val LocationValue = ArrayList<String>()

    private val subLocationValue = ArrayList<String>()
    private var sublocationId = 0

    private val shiftValue = ArrayList<String>()
    private var shiftId = 0
    var date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReportCalenderBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        binding.appbarCalendarReport.tvAppbarTitle.text = "Jadwal kerja"
        binding.appbarCalendarReport.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        //spinner area
        viewModel.getListLocation(projectCode)
        viewModel.getListLocationViewModel().observe(this){
            if (it.code == 200){
                val sizeLoc = it.data.size
                for(i in 0 until sizeLoc){
                    LocationValue.add(it.data[i].locationName)
                }
                binding.spinnerAreaLocCalendarReport.adapter = ArrayAdapter(this, R.layout.spinner_item, LocationValue)
                binding.spinnerAreaLocCalendarReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        locationId = it.data[position].locationId
                        //setdefaultspinner
                        sublocationId = 0
                        subLocationValue.clear()
                        binding.spinnerSubAreaCalendarReport.clearSelection()

                        viewModel.getListSublocation(
                            projectCode,
                            it.data[position].locationId)


                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            } else {
                val defaultSubLocData = resources.getStringArray(R.array.noChoose)
                binding.spinnerAreaLocCalendarReport.adapter = ArrayAdapter(this, R.layout.spinner_item, defaultSubLocData)
                Toast.makeText(this, "Gagal mengambil data sub area", Toast.LENGTH_SHORT).show()
            }
        }

        //spinner sublocation
        viewModel.getListSublocationViewModel().observe(this){
            if (it.code == 200){
                val sizeSubloc = it.data.size
                for (i in 0 until  sizeSubloc){
                    subLocationValue.add(it.data[i].subLocationName)
                }
                binding.spinnerSubAreaCalendarReport.adapter = ArrayAdapter(this, R.layout.spinner_item, subLocationValue)
                binding.spinnerSubAreaCalendarReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        sublocationId = it.data[position].subLocationId
                        //defaultspinner
                        shiftId = 0
                        shiftValue.clear()
                        binding.spinnerShiftCalendarReport.clearSelection()

                        viewModel.getListShiftReports(projectCode, locationId, it.data[position].subLocationId)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            } else {
                val defaultSubLocData = resources.getStringArray(R.array.noChoose)
                binding.spinnerAreaLocCalendarReport.adapter = ArrayAdapter(this, R.layout.spinner_item, defaultSubLocData)
                Toast.makeText(this, "Gagal mengambil data sub area", Toast.LENGTH_SHORT).show()
            }
        }
        //spinner shift
        viewModel.getListShiftReportViewModel().observe(this){
            if (it.code == 200){
                val sizeShift = it.data.size
                for (i in 0 until  sizeShift){
                    shiftValue.add(it.data[i].shiftDescription)
                }
                binding.spinnerShiftCalendarReport.adapter = ArrayAdapter(this, R.layout.spinner_item, shiftValue)
                binding.spinnerShiftCalendarReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        shiftId = it.data[position].shiftId

//                        viewModel.getWorkHour(projectCode, date, shiftId, locationId, sublocationId)
                        loadData()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            } else {
                val defaultSubLocData = resources.getStringArray(R.array.noChoose)
                binding.spinnerAreaLocCalendarReport.adapter = ArrayAdapter(this, R.layout.spinner_item, defaultSubLocData)
                Toast.makeText(this, "Gagal mengambil data sub area", Toast.LENGTH_SHORT).show()
            }
        }
        //set datepicker
        val datePicker = findViewById<DatePicker>(R.id.calendarTeamArea)
        val today = Calendar.getInstance()
        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->

            var month = month + 1
            var monthreal: String = month.toString()
            var dayDate: String = day.toString()

            //validasi 0
            if (month <= 10) {
                monthreal = "0" + month
            }
            if (day < 10) {
                dayDate = "0" + dayDate
            }
            clearDropdown()
            emptyLayout()
            shiftId = 0
            shiftValue.clear()
            sublocationId = 0
            subLocationValue.clear()
            date = "$year-$monthreal-$dayDate"

        }

        //set recyclerview
        val layoutManagerDac = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDacCalendarReport.layoutManager = layoutManagerDac

        val layoutManagerPengawas = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPengawasCalendarTeamArea.layoutManager = layoutManagerPengawas

        val layoutManagerOperator = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvOperatorCalendarTeamArea.layoutManager = layoutManagerOperator

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //oncreate
    }
    private fun clearDropdown(){
        binding.spinnerAreaLocCalendarReport.clearSelection()
        binding.spinnerSubAreaCalendarReport.clearSelection()
        binding.spinnerShiftCalendarReport.clearSelection()

    }
    private fun emptyLayout(){
        binding.rvDacCalendarReport.visibility = View.GONE
        binding.rvPengawasCalendarTeamArea.visibility = View.GONE
        binding.rvOperatorCalendarTeamArea.visibility = View.GONE
        binding.tvEmptyOperatorCalendarTeamArea.visibility = View.GONE
        binding.tvEmptyPengawasCalendarTeamArea.visibility = View.GONE
        binding.tvDefaultOperatorCalendarTeamArea.visibility = View.VISIBLE
        binding.tvDefaultPengawasCalendarTeamArea.visibility = View.VISIBLE
        binding.tvWorkTimeCalendarReport.text = "-"
    }
    private fun loadData(){
        viewModel.getWorkHour(projectCode, date, shiftId, locationId, sublocationId)

    }

    @SuppressLint("SetTextI18n")
    private fun setObserver(){
        viewModel.getWorkHoutViewModel().observe(this){
            if (it.code == 200){
                binding.tvDefaultPengawasCalendarTeamArea.visibility = View.GONE
                binding.tvDefaultOperatorCalendarTeamArea.visibility = View.GONE
                binding.rvDacCalendarReport.visibility = View.VISIBLE
                binding.rvPengawasCalendarTeamArea.visibility = View.VISIBLE
                binding.rvOperatorCalendarTeamArea.visibility = View.VISIBLE

                binding.tvWorkTimeCalendarReport.text = "${it.data.shiftStartAt}-${it.data.shiftEndAt}"
                binding.rvDacCalendarReport.adapter = ListDacCalenderAdapter(this, it.data.listPekerjaan as ArrayList<Pekerjaan>)

                //validasi pengawas
                if (it.data.listPengawas.isNotEmpty()){
                    binding.tvEmptyPengawasCalendarTeamArea.visibility = View.GONE
                    binding.rvPengawasCalendarTeamArea.adapter = ListPengawasReportAdapter(this, it.data.listPengawas as ArrayList<Pengawas>)
                } else {
                    binding.tvEmptyPengawasCalendarTeamArea.visibility = View.VISIBLE
                    binding.rvPengawasCalendarTeamArea.adapter = null
                }
             


                //validasi operator
                if (it.data.listOperator.isNotEmpty()){
                    binding.tvEmptyOperatorCalendarTeamArea.visibility = View.GONE
                    binding.rvOperatorCalendarTeamArea.adapter = ListOperatorReportAdapter(this, it.data.listOperator as ArrayList<Operator>)
                } else {
                    binding.tvEmptyOperatorCalendarTeamArea.visibility = View.VISIBLE
                    binding.rvOperatorCalendarTeamArea.adapter = null
                }

            } else {
                Toast.makeText(this, "Silahkan mengisis data yg sesuai", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //fun

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}