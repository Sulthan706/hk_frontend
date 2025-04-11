package com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMyTeamClientHistoryAttendanceBinding
import com.hkapps.hygienekleen.features.features_client.myteam.viewmodel.MyTeamClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import java.text.SimpleDateFormat
import java.util.*

class MyTeamClientHistoryAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyTeamClientHistoryAttendanceBinding
    //viewmodel
    private val viewModel: MyTeamClientViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamClientViewModel::class.java)
    }
    //pref
    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    //val
    private var loadingDialog: Dialog? = null
    //adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMyTeamClientHistoryAttendanceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.layoutAppbarCfteam.tvAppbarTitle.text = "Riwayat Kehadiran"
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
        //oncreate
        binding.layoutAppbarCfteam.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        var sdf = SimpleDateFormat("yyyy-MM-dd")
        var currentDate = sdf.format(Date())
        var localDate = currentDate

        viewModel.getHistoryAttendanceCfteam(projectId, localDate)

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


           viewModel.getHistoryAttendanceCfteam(projectId, msg)
        }

        setObserver()
        showLoading("Loading text...")
        //oncreate
    }
    //fun

    private fun setObserver(){
        viewModel.getHistoryDetailAttedanceViewModel().observe(this, Observer {
            if (it.code == 200){
                //empty state lembur
                if (it.data.countLemburGanti == 0){
                    binding.llLemburCountAll.visibility = View.GONE
                    binding.llEmptyLembur.visibility = View.VISIBLE
                } else {
                    binding.llEmptyLembur.visibility = View.GONE
                    binding.llLemburCountAll.visibility = View.VISIBLE
                }
                //emptystate Kehadiran
                if (it.data.inPercent == 0){
                    binding.llCountKehadiranAll.visibility = View.GONE
                    binding.tvEmptyStateKehadiranAll.visibility = View.VISIBLE
                } else {
                    binding.llCountKehadiranAll.visibility = View.VISIBLE
                    binding.tvEmptyStateKehadiranAll.visibility = View.GONE
                }



                binding.tvScheduleOffCount.text = if (it.data.countJadwalLibur == null){
                    "--"
                } else {
                    "${it.data.countJadwalLibur} Orang"
                }
                binding.tvScheduleInCount.text = if (it.data.countJadwalMasuk == null){
                    "--"
                } else {
                    "${it.data.countJadwalMasuk} Orang"
                }
                binding.roundedProgressBar.setProgressPercentage(it.data.inPercent.toDouble())
                binding.tvCountHadirPercent.text = if(it.data.countHadir == null ){
                    "--"
                } else {
                    "${it.data.countHadir} Orang"
                }
                binding.tvCountTidakHadirPercent.text = if (it.data.countTidakHadir == null) {
                    "--"
                } else {
                    "${it.data.countTidakHadir} Orang"
                }
                binding.tvCountLemburgantiRiwayat.text = if (it.data.countLemburGanti == null){
                    "--"
                } else {
                    "${it.data.countLemburGanti} Orang"
                }

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

}