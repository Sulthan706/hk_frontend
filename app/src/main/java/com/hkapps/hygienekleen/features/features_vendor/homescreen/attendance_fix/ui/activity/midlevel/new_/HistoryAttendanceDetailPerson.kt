package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_



import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityHistoryAttendanceDetailPersonBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.util.*
import kotlin.collections.ArrayList


class HistoryAttendanceDetailPerson : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryAttendanceDetailPersonBinding
    private val attedanceViewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }
    //pref

    //val
    lateinit var employeeName: String
    lateinit var employeeNuc: String
    lateinit var employeePhotoProfile: String
    lateinit var employeeJobName: String
    var employeeId: Int = 0
    var month: Int = 0
    var year: Int = 0
//    @SuppressLint("NewApi")
//    var year = Year.now().value

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHistoryAttendanceDetailPersonBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val onClickListener =
            binding.layoutAppbarHistoryAttendancePerson.ivAppbarBack.setOnClickListener {
                onBackPressed()
            }
        binding.layoutAppbarHistoryAttendancePerson.tvAppbarTitle.text = "Riwayat Absen Individual"

        employeeId = intent.getIntExtra("employeeId", 0)
        employeeName = intent.getStringExtra("employeeName").toString()
        employeeNuc = intent.getStringExtra("employeeNuc").toString()
        employeePhotoProfile = intent.getStringExtra("employeePhotoProfile").toString()
        employeeJobName = intent.getStringExtra("employeeJobName").toString()

//        binding.rvDateDetailPerson.setOnClickListener {
//            setMonthNow()
//        }
        binding.tvBtnSubmit.setOnClickListener {
            val i = Intent(this, PersonHistoryActivity::class.java)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_RESULT, month)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_RESULT, year)
            CarefastOperationPref.saveInt(
                CarefastOperationPrefConst.EMPLOYEE_HISTORY_RESULT,
                employeeId
            )
            startActivity(i)
        }

        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance()[Calendar.YEAR]
        for (i in thisYear downTo 2012) {
            years.add(Integer.toString(i))
        }
        val adapterss =
            ArrayAdapter(this, com.hkapps.hygienekleen.R.layout.spinner_item_white, years)
        adapterss.setDropDownViewResource(com.hkapps.hygienekleen.R.layout.simple_spinner_custom_new)
        binding.spinnerFabDetail.adapter = adapterss
        binding.spinnerFabDetail.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    year = years[position].toInt()
                    Log.d("test", "$year")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }


        val months = ArrayList<String>()
        months.add("Januari")
        months.add("Februari")
        months.add("Maret")
        months.add("April")
        months.add("Mei")
        months.add("Juni")
        months.add("Juli")
        months.add("Agustus")
        months.add("September")
        months.add("Oktober")
        months.add("November")
        months.add("Desember")
        var adapters =
            ArrayAdapter(this, com.hkapps.hygienekleen.R.layout.spinner_item_white, months)
        adapters.setDropDownViewResource(com.hkapps.hygienekleen.R.layout.simple_spinner_custom_new)
        binding.spinnerFabDetailMonth.adapter = adapters
        binding.spinnerFabDetailMonth.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    month = position + 1
                    Log.d("months", "$month")
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_RESULT, month)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        Log.d("yearnow", "$year")

        //oncreate ^
        setObserver()
    }

    //fun
//    private fun setMonthNow() {
//        MonthYearPickerDialog.show(context = this, listener = object : PickerListener {
//            override fun onSetResult(calendar: Calendar) {
//                val date: String = PickerUtils.getMonthYearDisplay(
//                    this@HistoryAttendanceDetailPerson,
//                    calendar,
//                    PickerUtils.Format.SHORT
//                )
//
//                Log.d("time", "$date")
//
//                // Uncomment this line to set the selected date in your UI
//                // binding.tvMonthDetailPerson.text = date
//            }
//
//            override fun writeToParcell(p0: Parcel, p1: Int) {
//
//            }
//
//
//        })
//    }





    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        binding.tvEmployeeNameHistoryP.text = employeeName
        binding.tvJobNucEmployeeHistoryP.text = "$employeeNuc | $employeeJobName"
    }
}




