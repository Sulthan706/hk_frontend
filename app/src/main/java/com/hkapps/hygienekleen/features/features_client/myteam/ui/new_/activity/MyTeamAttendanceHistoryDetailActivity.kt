package com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.activity

import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMyTeamAttendanceHistoryDetailBinding
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamdetailhistoryperson.TipeJadwal
import com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.adapter.MyTeamAbsentAdapter
import com.hkapps.hygienekleen.features.features_client.myteam.viewmodel.MyTeamClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.text.SimpleDateFormat
import java.util.*

class MyTeamAttendanceHistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyTeamAttendanceHistoryDetailBinding

    //viewmodel
    private val viewModel: MyTeamClientViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamClientViewModel::class.java)
    }
    //adapter
    private lateinit var adapterAbsent: MyTeamAbsentAdapter

    // pref
    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_EMPLOYEE_CFTEAM, 0)
    private val jobCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.JOBCODE_CFTEAM, "")

    //val


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMyTeamAttendanceHistoryDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        binding.layoutAppbarCfteam.tvAppbarTitle.text = "Riwayat Kehadiran Individu"
        binding.layoutAppbarCfteam.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        //datepicker auto from click btn
        var sdf = SimpleDateFormat("yyyy-MM-dd")
        var currentDate = sdf.format(Date())
        var localDate = currentDate

        var monthString = DateFormat.format("MM", Date()) as String // Jun
        var yearString = DateFormat.format("yyyy", Date()) as String // 2013
        viewModel.getDetailHistoryPersonCfteam(
            projectId,
            employeeId,
            localDate,
            monthString,
            yearString,
            jobCode
        )


        //datepicker selected
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

            val msg = "$year-$monthreal-$dayDate"
            var monthTitle: String = ""
            when(month){

                1 -> {
                    monthTitle = "Januari"
                }
                2 -> {
                    monthTitle = "Februari"
                }
                3 -> {
                    monthTitle = "Maret"
                }
                4 -> {
                    monthTitle = "April"
                }
                5 -> {
                    monthTitle = "Mei"
                }
                6 -> {
                    monthTitle = "Juni"
                }
                7 -> {
                    monthTitle = "Juli"
                }
                8 -> {
                    monthTitle = "Agustus"
                }
                9 -> {
                    monthTitle = "September"
                }
                10 -> {
                    monthTitle = "Oktober"
                }
                11 -> {
                    monthTitle = "November"
                }
                12 -> {
                    monthTitle = "Desember"
                }
            }
            binding.tvMonthTitle.text = "$monthTitle $yearReal"

            viewModel.getDetailHistoryPersonCfteam(
                projectId,
                employeeId,
                msg,
                monthreal,
                yearReal,
                jobCode
            )
        }
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCfteamAbsen.layoutManager = layoutManager

//        val layoutManagerLembur = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        binding.rvCfteamLembur.layoutManager = layoutManagerLembur

        setObserver()
        //oncreate
    }

    private fun setObserver() {
        viewModel.getDetailHistoryPersonViewModel().observe(this, Observer {
            if (it.code == 200) {
                binding.tvCountHadirResult.text = if (it.data.scheduleHadir == null) {
                    "-"
                } else {
                    it.data.scheduleHadir.toString()
                }
                binding.tvCountAlpaResult.text = if (it.data.scheduleAlpha == null) {
                    "-"
                } else {
                    it.data.scheduleAlpha.toString()
                }
                binding.tvCountIzinResult.text = if (it.data.scheduleIzin == null) {
                    "-"
                } else {
                    it.data.scheduleIzin.toString()
                }
                binding.tvCountLemburGantiResult.text = if (it.data.scheduleLemburGanti == null) {
                    "-"
                } else {
                    it.data.scheduleLemburGanti.toString()
                }

                binding.tvDateScheduleResult.text = it.data.tglSchedule

                binding.tvTimeScheduleResult.text =
                    if (it.data.shiftMulai == null || it.data.shiftAkhir == null) {
                        "--"
                    } else {
                        "${it.data.shiftMulai}-${it.data.shiftAkhir}"
                    }
                if (it.data.tipeJadwal.isNullOrEmpty()){
                    binding.llEmtpyCountHarian.visibility = View.VISIBLE
                    binding.llReportToday.visibility = View.GONE
                } else {
                    binding.llEmtpyCountHarian.visibility = View.GONE
                    binding.llReportToday.visibility = View.VISIBLE
                }


                binding.tvScheduleStatusResult.text = if (it.data.statusSchedule.isNullOrEmpty()) {
                    "--"
                } else {
                    it.data.statusSchedule
                }
                if (it.data.statusSchedule.isNullOrEmpty()) {
                    "--"
                } else {
                    val textview = binding.tvScheduleStatusResult
                    when (it.data.statusSchedule) {
                        "LUPA_ABSEN" -> {
                            textview.text = "Lupa Absen"
                            textview.setTextColor(Color.parseColor("#FF5656"))
                        }
                        "TIDAK_HADIR" -> {
                            textview.text = "Tidak Hadir"
                            textview.setTextColor(Color.parseColor("#FF5656"))
                        }
                        "HADIR" -> {
                            textview.text = "Hadir"
                            textview.setTextColor(Color.parseColor("#00BD8C"))
                        }
                        "SEDANG_BEKERJA" -> {
                            textview.text = "Sedang Bekerja"
                            textview.setTextColor(Color.parseColor("#167FFC"))
                        }
                        "LIBUR" -> {
                            textview.text = "Libur"
                            textview.setTextColor(Color.parseColor("#607080"))
                        }


                    }
                }
                adapterAbsent = MyTeamAbsentAdapter(
                    it.data.tipeJadwal as List<TipeJadwal>
                )
                binding.rvCfteamAbsen.adapter = adapterAbsent



            }
        })
    }
    //fun
}