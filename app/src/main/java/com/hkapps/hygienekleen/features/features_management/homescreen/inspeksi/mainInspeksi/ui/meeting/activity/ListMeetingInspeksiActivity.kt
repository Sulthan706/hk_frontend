package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListMeetingInspeksiBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listMeeting.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.adapter.ListMeetingInspeksiAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.viewmodel.InspeksiViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListMeetingInspeksiActivity : AppCompatActivity(), ListMeetingInspeksiAdapter.ListMeetingCallBack {

    private lateinit var binding: ActivityListMeetingInspeksiBinding
    private lateinit var rvAdapter: ListMeetingInspeksiAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val mClickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.M_CLICK_FROM, "")
    private var projectCode = ""
    private var date = ""
    private var page = 0
    private var isLastPage = false
    private var reloadNeeded = true

    private val viewModel: InspeksiViewModel by lazy {
        ViewModelProviders.of(this).get(InspeksiViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMeetingInspeksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // validate project code by click from
        projectCode = if (clickFrom == "mainInspeksi" || mClickFrom == "mainInspeksi") {
            CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
        } else if (clickFrom == "listMeeting" || mClickFrom == "listMeeting") {
            CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_MEETING, "")
        } else {
            ""
        }
        Log.d("ListMeetingAct", "onCreate: project code = $projectCode")
        Log.d("ListMeetingAct", "onCreate: clickFrom = $clickFrom nClickFrom = $mClickFrom")

        // set app bar
        binding.appbarListMeetingInspeksi.tvAppbarTitle.text = "Meeting"
        binding.appbarListMeetingInspeksi.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // set current date
        val today = Calendar.getInstance()
        val day = android.text.format.DateFormat.format("dd", today) as String
        val month = android.text.format.DateFormat.format("MMM", today) as String
        val months = android.text.format.DateFormat.format("MM", today) as String
        val year = android.text.format.DateFormat.format("yyyy", today) as String
        binding.tvDateListMeetingInspeksi.text = "$day $month $year"
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
                binding.tvDateListMeetingInspeksi.text = sdf.format(cal.time)

                // set shimmer layout
                binding.shimmerListMeetingInspeksi.startShimmerAnimation()
                binding.shimmerListMeetingInspeksi.visibility = View.VISIBLE
                binding.rvListMeetingInspeksi.visibility = View.GONE
                binding.tvEmptyListMeetingInspeksi.visibility = View.GONE

                Handler(Looper.getMainLooper()).postDelayed({
                    loadData()
                    setObserver()
                }, 1500)
            }

        binding.tvDateListMeetingInspeksi.setOnClickListener {
            DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // set shimmer layout
        binding.shimmerListMeetingInspeksi.startShimmerAnimation()
        binding.shimmerListMeetingInspeksi.visibility = View.VISIBLE
        binding.rvListMeetingInspeksi.visibility = View.GONE
        binding.tvEmptyListMeetingInspeksi.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListMeetingInspeksi.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvListMeetingInspeksi.addOnScrollListener(scrollListener)

        // set on click create meeting
        binding.ivCreateListMeetingInspeksi.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "listMeeting")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.M_CLICK_FROM, "listMeeting")
            startActivityForResult(Intent(this, FormLaporanMeetingActivity::class.java), CREATE_CODE)
            finish()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            loadData()
            setObserver()
        }, 1500)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    binding.shimmerListMeetingInspeksi.stopShimmerAnimation()
                    binding.shimmerListMeetingInspeksi.visibility = View.GONE
                    binding.rvListMeetingInspeksi.visibility = View.VISIBLE
                }
            }
        }
        viewModel.listMeetingResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.rvListMeetingInspeksi.adapter = null
                        binding.tvEmptyListMeetingInspeksi.visibility = View.VISIBLE
                    }, 1500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvEmptyListMeetingInspeksi.visibility = View.GONE
                        isLastPage = it.data.last
                        if (page == 0) {
                            rvAdapter = ListMeetingInspeksiAdapter(
                                this,
                                it.data.content as ArrayList<Content>
                            ).also { it1 -> it1.setListener(this) }
                            binding.rvListMeetingInspeksi.adapter = rvAdapter
                        } else {
                            rvAdapter.listMeeting.addAll(it.data.content)
                            rvAdapter.notifyItemRangeChanged(
                                rvAdapter.listMeeting.size - it.data.content.size,
                                rvAdapter.listMeeting.size
                            )
                        }
                    }, 1500)
                }
            } else {
                binding.rvListMeetingInspeksi.adapter = null
                binding.tvEmptyListMeetingInspeksi.visibility = View.VISIBLE
                Toast.makeText(this, "Gagal mengambil list meeting", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListMeeting(userId, projectCode, date, page)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                this.reloadNeeded = true
            }
        }
    }

    companion object {
        private const val CREATE_CODE = 31
    }

    override fun onResume() {
        super.onResume()
        if (this.reloadNeeded)
            loadData()

        this.reloadNeeded = false
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
        super.onBackPressed()
        finish()
    }

    override fun onClickItem(idMeeting: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.MEETING_ID_INSPEKSI, idMeeting)
        startActivity(Intent(this, DetailMeetingInspeksiActivity::class.java))
    }
}