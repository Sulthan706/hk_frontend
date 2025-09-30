package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListRoutineVisitedBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listRoutine.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.adapter.ListRoutineReportAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.viewmodel.RoutineViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ListRoutineVisitedActivity : AppCompatActivity(), ListRoutineReportAdapter.ListRoutineCallBack {

    private lateinit var binding: ActivityListRoutineVisitedBinding
    private lateinit var rvAdapter: ListRoutineReportAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val mClickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.M_CLICK_FROM, "")
    private var projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_ROUTINE, "")
    private var date = ""
    private var page = 0
    private var isLastPage = false
    private var reloadNeeded = true

    private val viewModel: RoutineViewModel by lazy {
        ViewModelProviders.of(this).get(RoutineViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRoutineVisitedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set app bar
        binding.appbarListRoutineVisited.tvAppbarTitle.text = "Kunjungan Rutin"
        binding.appbarListRoutineVisited.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set current date
        val today = Calendar.getInstance()
        val day = android.text.format.DateFormat.format("dd", today) as String
        val month = android.text.format.DateFormat.format("MMM", today) as String
        val months = android.text.format.DateFormat.format("MM", today) as String
        val year = android.text.format.DateFormat.format("yyyy", today) as String
        binding.tvDateListRoutineVisited.text = "$day $month $year"
        date = "$year-$months-$day"

        // open dialog choose date
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd MMM yyyy" // mention the format you need
                val paramsFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val dateParam = SimpleDateFormat(paramsFormat, Locale.US)

                date = dateParam.format(cal.time)
                binding.tvDateListRoutineVisited.text = sdf.format(cal.time)

                // set shimmer layout
                binding.shimmerListRoutineVisited.startShimmerAnimation()
                binding.shimmerListRoutineVisited.visibility = View.VISIBLE
                binding.rvListRoutineVisited.visibility = View.GONE
                binding.tvEmptyListRoutineVisited.visibility = View.GONE

                Handler(Looper.getMainLooper()).postDelayed({
                    loadData()
                    setObserver()
                }, 1500)
            }

        binding.tvDateListRoutineVisited.setOnClickListener {
            DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListRoutineVisited.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvListRoutineVisited.addOnScrollListener(scrollListener)

        // set on click create meeting
        binding.ivCreateListRoutineVisited.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "listRoutine")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.M_CLICK_FROM, "listRoutine")
            startActivityForResult(Intent(this, FormRoutineReportActivity::class.java), CREATE_CODE)
            finish()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            loadData()
            setObserver()
        }, 1500)
    }

    private fun loadData() {
        viewModel.getListRoutine(userId, projectCode, date, page)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    binding.shimmerListRoutineVisited.stopShimmerAnimation()
                    binding.shimmerListRoutineVisited.visibility = View.GONE
                    binding.rvListRoutineVisited.visibility = View.VISIBLE
                }
            }
        }
        viewModel.listRoutineResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.rvListRoutineVisited.adapter = null
                        binding.tvEmptyListRoutineVisited.visibility = View.VISIBLE
                    }, 1500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvEmptyListRoutineVisited.visibility = View.GONE
                        isLastPage = it.data.last
                        if (page == 0) {
                            rvAdapter = ListRoutineReportAdapter(
                                this,
                                it.data.content as ArrayList<Content>
                            ).also { it1 -> it1.setListener(this) }
                            binding.rvListRoutineVisited.adapter = rvAdapter
                        } else {
                            rvAdapter.listRoutine.addAll(it.data.content)
                            rvAdapter.notifyItemRangeChanged(
                                rvAdapter.listRoutine.size - it.data.content.size,
                                rvAdapter.listRoutine.size
                            )
                        }
                    }, 1500)
                }
            } else {
                binding.rvListRoutineVisited.adapter = null
                binding.tvEmptyListRoutineVisited.visibility = View.VISIBLE
                Toast.makeText(this, "Gagal mengambil list laporan rutin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val CREATE_CODE = 31
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                this.reloadNeeded = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (this.reloadNeeded)
            loadData()

        this.reloadNeeded = false
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickItem(idRoutine: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ROUTINE_ID_INSPEKSI, idRoutine)
        startActivity(Intent(this, DetailRoutineReportActivity::class.java))
    }
}