package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.adapter.DacAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.viewmodel.DacViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar
import net.cachapa.expandablelayout.ExpandableLayout


class DacActivity : AppCompatActivity(), ExpandableLayout.OnExpansionUpdateListener {
    private lateinit var binding: ActivityDacBinding

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var rvSkeleton: Skeleton

    private val dacViewModel: DacViewModel by lazy {
        ViewModelProviders.of(this).get(DacViewModel::class.java)
    }
    private lateinit var dailyActAdapter: DacAdapter
    private lateinit var dailyResponseModel: DailyActResponseModel


    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDacBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDac.layoutManager = layoutManager

        rvSkeleton = binding.rvDac.applySkeleton(R.layout.item_shimmer_dac)
        rvSkeleton.showSkeleton()

        binding.shimmerPengawas.startShimmerAnimation()
        binding.shimmerArea.startShimmerAnimation()
        binding.shimmerDate.startShimmerAnimation()
        binding.shimmerPlotting.startShimmerAnimation()
        binding.shimmerShift.startShimmerAnimation()
        binding.shimmerSub.startShimmerAnimation()

        dacViewModel.getDailyAct(employeeId, projectCode)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            setObserver()
        }, 1000)

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


    //INI GET DATA BUAT DITARO DI ADAPTERNYA
    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        dacViewModel.dailyActResponseModel.observe(this, Observer {
            if (it.code == 200) {

                binding.shimmerPengawas.stopShimmerAnimation()
                binding.shimmerPengawas.visibility = View.GONE
                binding.tvPengawas.visibility = View.VISIBLE
                binding.tvPengawas.text =
                    ": " + it.dailyActDataResponseModel.employeePengawasName


                binding.shimmerDate.stopShimmerAnimation()
                binding.shimmerDate.visibility = View.GONE
                binding.tvDate.visibility = View.VISIBLE
                binding.tvDate.text = ": " + it.dailyActDataResponseModel.date


                binding.shimmerShift.stopShimmerAnimation()
                binding.shimmerShift.visibility = View.GONE
                binding.tvShift.visibility = View.VISIBLE
                binding.tvShift.text = ": " + it.dailyActDataResponseModel.shiftDescription

                binding.shimmerPlotting.stopShimmerAnimation()
                binding.shimmerPlotting.visibility = View.GONE
                binding.tvPlottingCode.visibility = View.VISIBLE
                binding.tvPlottingCode.text = ": " + it.dailyActDataResponseModel.codePlottingArea

                binding.shimmerArea.stopShimmerAnimation()
                binding.shimmerArea.visibility = View.GONE
                binding.tvArea.visibility = View.VISIBLE
                binding.tvArea.text = ": " + it.dailyActDataResponseModel.locationName

                binding.shimmerSub.stopShimmerAnimation()
                binding.shimmerSub.visibility = View.GONE
                binding.tvSubArea.visibility = View.VISIBLE
                binding.tvSubArea.text = ": " + it.dailyActDataResponseModel.subLocationName

                if (it.dailyActDataResponseModel.dailyActDataArrayResponseModel.isNotEmpty()) {
                    binding.rvDac.visibility = View.VISIBLE
//                    binding.tvAttendanceDaily.visibility = View.GONE

                    dailyResponseModel = it
                    dailyActAdapter = DacAdapter(
                        it.dailyActDataResponseModel.dailyActDataArrayResponseModel
                    )
                    binding.rvDac.adapter = dailyActAdapter

                    Log.d("TAG", "setObserver: " + it.dailyActDataResponseModel)
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