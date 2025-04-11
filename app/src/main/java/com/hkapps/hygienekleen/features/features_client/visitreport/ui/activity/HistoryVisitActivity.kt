package com.hkapps.hygienekleen.features.features_client.visitreport.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHistoryVisitBinding
import com.hkapps.hygienekleen.features.features_client.visitreport.model.mainvisitreport.Content
import com.hkapps.hygienekleen.features.features_client.visitreport.ui.adapter.ListHistoryVisitAdapter
import com.hkapps.hygienekleen.features.features_client.visitreport.viewmodel.VisitReportViewModel
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryVisitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryVisitBinding
    private val viewModel: VisitReportViewModel by lazy {
        ViewModelProviders.of(this).get(VisitReportViewModel::class.java)
    }

    //adapter
    private lateinit var adapterHistoryVisitReport: ListHistoryVisitAdapter

    //pref
    private val projectCode = "CF_HO"
//        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    var date: String = ""
    var dateNow: String = ""

    //val
    var page: Int = 0
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHistoryVisitBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        binding.appbarHistoryVisit.tvAppbarTitle.text = "Riwayat Kunjungan"
        binding.appbarHistoryVisit.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListHistoryVisit.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvListHistoryVisit.addOnScrollListener(scrollListener)
        //datenow
        var sdfDate = SimpleDateFormat("yyyy-MM-dd")
        date = sdfDate.format(Date())

        var sdfDateNow = SimpleDateFormat("yyyy-MM-dd")
        dateNow = sdfDateNow.format(Date())
        
        
        //set datepicker
        val datePicker = findViewById<DatePicker>(R.id.calendarHistoryVisit)
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

            date = "$year-$monthreal-$dayDate"
            
//            if (date < dateNow){
//                loadData()
//                binding.tvEmptyDataKunjungan.text = "Data Kunjungan belum tersedia"
//            } else {
//                binding.tvEmptyDataKunjungan.text = "Tidak ada data kunjungan pada tanggal ini"
//            }
            defaultLayout()
            loadData()

        }


        loadData()
        setObserver()
        //oncreate
    }
    //fun
    private fun defaultLayout(){
        binding.rvListHistoryVisit.visibility = View.GONE
    }
    private fun loadData() {
        viewModel.getMainVisitReport(projectCode, date, page)
    }

    private fun setObserver() {
        viewModel.getMainVisitReportViewModel().observe(this) {
            if (it.code == 200) {
                binding.rvListHistoryVisit.visibility = View.VISIBLE

                if(it.data.content.isNotEmpty()){
                    binding.tvEmptyDataKunjungan.visibility = View.GONE

                    isLastPage = it.data.last
                    if (page == 0) {
                        adapterHistoryVisitReport = ListHistoryVisitAdapter(this, it.data.content as ArrayList<Content>)
                        binding.rvListHistoryVisit.adapter = adapterHistoryVisitReport
                    } else {
                        adapterHistoryVisitReport.listHistoryVisit.addAll(it.data.content as ArrayList<Content>)
                        adapterHistoryVisitReport.notifyItemRangeChanged(
                            adapterHistoryVisitReport.listHistoryVisit.size - it.data.size,
                            adapterHistoryVisitReport.listHistoryVisit.size
                        )
                    }
                } else {

                    binding.tvEmptyDataKunjungan.visibility = View.VISIBLE
                    binding.rvListHistoryVisit.visibility = View.GONE
                }

            }
        }
    }
}