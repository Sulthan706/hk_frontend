package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDacBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.DailyActNewResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.adapter.DacNewAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.fragment.ConfirmDacChecklistDialog
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.viewmodel.DacViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import net.cachapa.expandablelayout.ExpandableLayout
import java.text.SimpleDateFormat
import java.util.*


class DacNewActivity : AppCompatActivity(), ExpandableLayout.OnExpansionUpdateListener {
    private lateinit var binding: ActivityDacBinding

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var rvSkeleton: Skeleton

    private val dacViewModel: DacViewModel by lazy {
        ViewModelProviders.of(this).get(DacViewModel::class.java)
    }
    private lateinit var dailyActAdapter: DacNewAdapter
    private lateinit var dailyNewResponseModel: DailyActNewResponseModel
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val dateSch =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_SCH, "")

    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val idDetailProject =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_DETAIL_PROJECT, 0)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDacBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDac.layoutManager = layoutManager

        rvSkeleton = binding.rvDac.applySkeleton(R.layout.item_shimmer_dac)
        rvSkeleton.showSkeleton()
        binding.rvDac.setHasFixedSize(true)

        binding.btnSendDacChecklist.setBackgroundColor(resources.getColor(R.color.grayLine))

        binding.shimmerPengawas.startShimmerAnimation()
        binding.shimmerArea.startShimmerAnimation()
        binding.shimmerDate.startShimmerAnimation()
        binding.shimmerPlotting.startShimmerAnimation()
        binding.shimmerShift.startShimmerAnimation()
        binding.shimmerSub.startShimmerAnimation()

        dacViewModel.getDailyNewAct(employeeId, projectCode, idDetailProject)

        setObservers()
        setObserver()
//        Handler(Looper.getMainLooper()).postDelayed(Runnable {
//        }, 1000)

        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutAppbar.tvAppbarTitle.text = "Lihat Daily Activity"
    }

    override fun onExpansionUpdate(expansionFraction: Float, state: Int) {
        if (state == ExpandableLayout.State.EXPANDING) {
//            recyclerView?.smoothScrollToPosition(getAdapterPosition());
        }
    }

    //INI GET DATA DARI ADAPTERNYA UNTUK BUTTON KIRIM
    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        dacViewModel.checkResponseModel.observe(this) {
            if (it.code == 200) {
//                dacViewModel.getDailyNewAct(employeeId, projectCode, idDetailProject)
                dacViewModel.getCountAct(employeeId, projectCode, idDetailProject)
                dacViewModel.dACCheckResponseModel.observe(this) { it1 ->
                    if (it1.data.plottingDataResponseModel.checklistByEmployee == "Y") {
                        binding.btnSendDacChecklist.visibility = View.GONE
                    } else if (it1.data.plottingDataResponseModel.checklistByEmployee == "N") {
                        if (it1.data.plottingDataResponseModel.countChecklistDACByEmployee >= it1.data.plottingDataResponseModel.countDailyActivities) {
//                        if (it1.data.plottingDataResponseModel.isDone == "Y"){
                            binding.btnSendDacChecklist.setBackgroundColor(resources.getColor(R.color.primary_color))
                            binding.btnSendDacChecklist.setOnClickListener {
                                openDialog()
                            }
                            binding.btnSendDacChecklist.isEnabled = true
                        } else {
                            binding.btnSendDacChecklist.setBackgroundColor(resources.getColor(R.color.grayLine))
                            binding.btnSendDacChecklist.isEnabled = false
                        }
                    }
                }
            }
        }
    }

    private fun openDialog() {
        val dialog = ConfirmDacChecklistDialog()
        dialog.show(supportFragmentManager, "ConfirmDacChecklistDialog")
    }

    //INI GET DATA BUAT DITARO DI ADAPTERNYA
    @SuppressLint("SetTextI18n")
    fun setObserver() {
        dacViewModel.dailyActNewResponseModel.observe(this, Observer {
            if (it.code == 200) {
                if (it.dailyActDataNewResponseModel.plottingDataResponseModel.checklistByEmployee == "Y") {
                    binding.btnSendDacChecklist.visibility = View.GONE
                } else if (it.dailyActDataNewResponseModel.plottingDataResponseModel.checklistByEmployee == "N") {
//                    if (it.dailyActDataNewResponseModel.plottingDataResponseModel.isDone == "Y"){
                    if (it.dailyActDataNewResponseModel.plottingDataResponseModel.countChecklistDACByEmployee >= it.dailyActDataNewResponseModel.plottingDataResponseModel.countDailyActivities) {
                        binding.btnSendDacChecklist.setBackgroundColor(resources.getColor(R.color.primary_color))
                        binding.btnSendDacChecklist.setOnClickListener {
                            openDialog()
                        }
                        binding.btnSendDacChecklist.isEnabled = true
                    } else {
                        binding.btnSendDacChecklist.setBackgroundColor(resources.getColor(R.color.grayLine))
                        binding.btnSendDacChecklist.isEnabled = false
                    }
                }
                binding.shimmerPengawas.stopShimmerAnimation()
                binding.shimmerPengawas.visibility = View.GONE
                binding.tvPengawas.visibility = View.VISIBLE
                binding.tvPengawas.text =
                    ": " + it.dailyActDataNewResponseModel.plottingDataResponseModel.employeePengawasName

                val currentDate: String =
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())

                binding.shimmerDate.stopShimmerAnimation()
                binding.shimmerDate.visibility = View.GONE
                binding.tvDate.visibility = View.VISIBLE
                binding.tvDate.text = ": " + dateSch.substring(0, 2) + " " + currentDate


                binding.shimmerShift.stopShimmerAnimation()
                binding.shimmerShift.visibility = View.GONE
                binding.tvShift.visibility = View.VISIBLE
                binding.tvShift.text =
                    ": " + it.dailyActDataNewResponseModel.plottingDataResponseModel.shiftDescription

                binding.shimmerPlotting.stopShimmerAnimation()
                binding.shimmerPlotting.visibility = View.GONE
                binding.tvPlottingCode.visibility = View.VISIBLE
                binding.tvPlottingCode.text =
                    ": " + it.dailyActDataNewResponseModel.plottingDataResponseModel.codePlottingArea

                binding.shimmerArea.stopShimmerAnimation()
                binding.shimmerArea.visibility = View.GONE
                binding.tvArea.visibility = View.VISIBLE
                binding.tvArea.text =
                    ": " + it.dailyActDataNewResponseModel.plottingDataResponseModel.locationName

                binding.shimmerSub.stopShimmerAnimation()
                binding.shimmerSub.visibility = View.GONE
                binding.tvSubArea.visibility = View.VISIBLE
                binding.tvSubArea.text =
                    ": " + it.dailyActDataNewResponseModel.plottingDataResponseModel.subLocationName

                if (it.dailyActDataNewResponseModel.plottingDataResponseModel.dailyActDataArrayResponseModel.isNotEmpty()) {
                    binding.rvDac.visibility = View.VISIBLE
//                    binding.tvAttendanceDaily.visibility = View.GONE

                    dailyNewResponseModel = it
                    dailyActAdapter = DacNewAdapter(
                        it.dailyActDataNewResponseModel.plottingDataResponseModel.dailyActDataArrayResponseModel,
                        it.dailyActDataNewResponseModel.plottingDataResponseModel,
                        this,
                        dacViewModel,
                        this
                    )
                    binding.rvDac.adapter = dailyActAdapter

                    Log.d("TAG", "setObserver: " + it.dailyActDataNewResponseModel)
                } else {
                    binding.rvDac.visibility = View.GONE
//                    binding.tvAttendanceDaily.text = "Belum ada data"
                }
            } else {
                onSNACK(binding.root,it.code)
            }
        })
    }

    //Snack bar kesalahan
    private fun onSNACK(view: View,code : Int) {
        val snackbar = Snackbar.make(
            view, if(code.toString().contains("4")) "Pengawas belum ditambahkan" else "Terjadi kesalahan",
            Snackbar.LENGTH_LONG
        ).setAction("Error", null)
        snackbar.setActionTextColor(resources.getColor(R.color.primary_color))
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(resources.getColor(R.color.primary_color))
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 12f
        snackbar.show()
    }

}