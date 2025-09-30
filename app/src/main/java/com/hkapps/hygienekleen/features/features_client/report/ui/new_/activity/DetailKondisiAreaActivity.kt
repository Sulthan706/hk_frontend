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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailKondisiAreaBinding
import com.hkapps.hygienekleen.features.features_client.report.viewmodel.ReportClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailKondisiAreaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKondisiAreaBinding
    private val viewModel: ReportClientViewModel by lazy {
        ViewModelProviders.of(this).get(ReportClientViewModel::class.java)
    }
    //pref
    private val locationId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.LOCATION_ID, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    //val
    private val subLocationValue = ArrayList<String>()
    private var sublocationId = 0

    private val shiftValue = ArrayList<String>()
    private var shiftId = 0

    private var msg: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailKondisiAreaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        binding.appbarKondisiAreaReport.tvAppbarTitle.text = "All Area"
        binding.appbarKondisiAreaReport.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        //spinner sublocation
        viewModel.getListSublocation(projectCode, locationId)
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
                        //set shift valu 0
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
                binding.spinnerSubAreaCalendarReport.adapter = ArrayAdapter(this, R.layout.spinner_item, defaultSubLocData)
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
//                        loadData()


                            viewModel.getDetailKondisiArea(projectCode, locationId, sublocationId, shiftId, msg)


                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            } else {
                val defaultSubLocData = resources.getStringArray(R.array.noChoose)
                binding.spinnerShiftCalendarReport.adapter = ArrayAdapter(this, R.layout.spinner_item, defaultSubLocData)
                Toast.makeText(this, "Gagal mengambil data sub area", Toast.LENGTH_SHORT).show()
            }
        }
        //get dateNow
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        msg = sdf.format(Date())

        //datepicker selected
        val datePicker = findViewById<DatePicker>(R.id.calendarDetailKondisiArea)
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
            if (month < 10) {
                monthreal = "0" + month
            }
            if (day < 10) {
                dayDate = "0" + dayDate
            }

             msg = "$year-$monthreal-$dayDate"
            emptyData()
            emptySelection()
            shiftId = 0
            shiftValue.clear()

        }
        setObserver()

        //oncreate
    }


    private fun emptySelection(){
        binding.spinnerShiftCalendarReport.clearSelection()
        binding.spinnerSubAreaCalendarReport.clearSelection()
    }
    private fun emptyData(){
        binding.llItemDetailKondisi.visibility = View.GONE
        binding.tvWorkTimeCalendarReport.text = "-"
        binding.tvEmptyStateDetailKondisi.text = "Pilih sub area dan shift terlebih dahulu"
    }
    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getDetailKondisiAreaViewModel().observe(this){
            if (it.code == 200 ){
                binding.tvWorkTimeCalendarReport.text = "${it.data.shiftStartAt} - ${it.data.shiftEndAt}"
                if (it.data.kondisiAreaDTO.checkPengawasName == null){
                    binding.llItemDetailKondisi.visibility = View.GONE
                    binding.tvEmptyStateDetailKondisi.visibility = View.VISIBLE
                    binding.tvEmptyStateDetailKondisi.text = "Data tidak ditemukan"
                } else {
                    binding.llItemDetailKondisi.visibility = View.VISIBLE
                    binding.tvEmptyStateDetailKondisi.visibility = View.GONE
                }
                binding.tvDicekKondisiArea.text = it.data.kondisiAreaDTO.checkDate
                binding.tvDicekOlehKondisiArea.text = it.data.kondisiAreaDTO.checkPengawasName
                binding.tvCatatanPengawasKondisiArea.text = it.data.kondisiAreaDTO.description
            }
        }
    }
    //fun
}