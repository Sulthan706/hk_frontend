package com.hkapps.hygienekleen.features.features_client.visitreport.ui.activity

import android.content.Intent
import java.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMainVisitReportBinding
import com.hkapps.hygienekleen.features.features_client.visitreport.model.mainvisitreport.Content
import com.hkapps.hygienekleen.features.features_client.visitreport.ui.adapter.ListVisitReportAdapter
import com.hkapps.hygienekleen.features.features_client.visitreport.viewmodel.VisitReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.util.*
import kotlin.collections.ArrayList

class MainVisitReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainVisitReportBinding
    private val viewModel: VisitReportViewModel by lazy {
        ViewModelProviders.of(this).get(VisitReportViewModel::class.java)
    }
    //adapter
    private lateinit var adapterVisitReport: ListVisitReportAdapter
    //pref
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    var date: String = ""
    //val
    var page: Int = 0
    private var isLastPage = false


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainVisitReportBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,null)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
        //oncreate
        //datenow for layour=t
        var sdf = SimpleDateFormat("dd MMMM yyyy")
        var currentDate = sdf.format(Date())

        binding.tvTitleTime.text = currentDate

        var sdfmonth = SimpleDateFormat("MMMM yyyy")
        var currentDates = sdfmonth.format(Date())

        binding.tvMonthMainReport.text = currentDates

        var sdfDate = SimpleDateFormat("yyyy-MM-dd")
        date = sdfDate.format(Date())


        binding.fabArrowLeftBack.setOnClickListener {
            onBackPressed()
        }

        binding.ivBtnHistoryVisit.setOnClickListener{
            startActivity(Intent(this, HistoryVisitActivity::class.java))
        }


        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvVisitReport.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvVisitReport.addOnScrollListener(scrollListener)
        loadData()
        setObserver()
        //oncreate
    }

    private fun loadData() {
        viewModel.getMainVisitReport(projectCode, date, page)
    }

    private fun setObserver() {
        viewModel.getMainVisitReportViewModel().observe(this){
            if (it.code == 200){
                binding.tvEmtpyStateVisitReport.visibility = View.GONE
                binding.rvVisitReport.visibility = View.VISIBLE

                if (it.data.content.isEmpty()){
                    binding.tvEmtpyStateVisitReport.visibility = View.VISIBLE
                } else {
                    isLastPage = it.data.last
                    if (page == 0) {
                        adapterVisitReport = ListVisitReportAdapter(this, it.data.content as ArrayList<Content>)
                        binding.rvVisitReport.adapter = adapterVisitReport
                    } else {
                        adapterVisitReport.listVisitReport.addAll(it.data.content as ArrayList<Content>)
                        adapterVisitReport.notifyItemRangeChanged(
                            adapterVisitReport.listVisitReport.size - it.data.size,
                            adapterVisitReport.listVisitReport.size
                        )
                    }
                }


            }
        }
    }

}