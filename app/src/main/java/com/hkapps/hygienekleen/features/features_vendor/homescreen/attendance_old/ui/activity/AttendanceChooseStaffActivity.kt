package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityAttendanceChooseStaffBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.ChooseStaffDataResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.activity.highLevel.AttendanceActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.adapter.AttendanceChooseStaffAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.viewmodel.AttendanceViewModel
import com.hkapps.hygienekleen.utils.CommonUtils
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton


class AttendanceChooseStaffActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityAttendanceChooseStaffBinding
    private lateinit var rvSkeleton: Skeleton
    private var loadingDialog: Dialog? = null
    private var layoutManager: RecyclerView.LayoutManager? = null


    private lateinit var chooseAdapterSearch: AttendanceChooseStaffAdapter

    private val attedanceViewModel: AttendanceViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceViewModel::class.java)
    }
    private var params: String = "TheAdventuresOfTomSawyer_201303"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceChooseStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutAppbar.tvAppbarTitle.setText("Absen")
        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            val i = Intent(this, AttendanceActivity::class.java)
            startActivity(i)
            finishAffinity()
        }

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAttendanceChooseStaff.layoutManager = layoutManager
        rvSkeleton =
            binding.rvAttendanceChooseStaff.applySkeleton(R.layout.item_attendance_choose_staff)
        rvSkeleton.showSkeleton()
        attedanceViewModel.getChooseAttendanceStaff(params)


        showLoading(getString(R.string.loading_string))
        setUpViews()
        setObserver()
    }

    //SETUP ADAPTERNYA
    private fun setUpViews() {
        binding.searchAttendanceChoose.setOnQueryTextListener(this)
        binding.rvAttendanceChooseStaff.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        chooseAdapterSearch = AttendanceChooseStaffAdapter()
        binding.rvAttendanceChooseStaff.adapter = chooseAdapterSearch
    }

    //INI GET DATA BUAT DITARO DI ADAPTERNYA
    private fun setObserver() {
        attedanceViewModel.chooseStaffResponseModel.observe(this, Observer {
            if (it.created != 0) {
                if (it.chooseStaffDataResponseModel.isNotEmpty()) {
                    renderData(it.chooseStaffDataResponseModel)
                    Log.d("TAG", "setObserver: " + it.chooseStaffDataResponseModel)
                    hideLoading()
                } else {
                    hideLoading()
                }
            }
        })
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }


    //BUAT NAMPILIN DATA
    private fun renderData(chooseStaffList: List<ChooseStaffDataResponseModel>) {
        chooseAdapterSearch.initData(chooseStaffList)
        chooseAdapterSearch.notifyDataSetChanged()

    }

    //INI SEARCH NYA
    override fun onQueryTextSubmit(query: String?): Boolean {
        chooseAdapterSearch.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        chooseAdapterSearch.filter.filter(newText)
        return false
    }

    override fun onBackPressed() {
        val i = Intent(this, AttendanceActivity::class.java)
        startActivity(i)
        finishAffinity()
    }
}