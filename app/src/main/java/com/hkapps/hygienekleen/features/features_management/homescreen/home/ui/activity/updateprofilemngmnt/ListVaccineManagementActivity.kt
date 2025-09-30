package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityListVaccineManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.listvaccinemanagement.ContentVaccineMgmnt
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.adapter.ListVaccineManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment.BotSheetVaccineManagementFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ListVaccineManagementActivity : AppCompatActivity(), ListVaccineManagementAdapter.ListVaccineMgmntClick {
    private lateinit var binding: ActivityListVaccineManagementBinding
    private val homeManagementViewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }

    var page: Int = 0
    private var isLastPage = false
    val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private lateinit var adapters: ListVaccineManagementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityListVaccineManagementBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.layoutAppbarHomeNews.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutAppbarHomeNews.tvAppbarTitle.text = "Sertifikat Vaksin"
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvVaccine.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.rvVaccine.addOnScrollListener(scrollListener)
        //open fragment
        binding.layoutAppbarHomeNews.llBtnUpload.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER, "")
            BotSheetVaccineManagementFragment().show(supportFragmentManager, "bottomsheet")
        }

        loadData()
        setObserver()
        //oncreate
    }

    private fun setObserver() {
        homeManagementViewModel.getListVaccineManagementViewModel().observe(this){
            if (it.code == 200){
                if (it.data.content.isNullOrEmpty()){
                    binding.llEmptyStateVaccine.visibility = View.VISIBLE
                } else {
                    binding.llEmptyStateVaccine.visibility = View.GONE
                    binding.rvVaccine.visibility = View.VISIBLE

                    isLastPage = false
                    if (page == 0){
                        adapters = ListVaccineManagementAdapter(this, it.data.content as ArrayList<ContentVaccineMgmnt>
                        ).also { it.setListener(this) }
                        binding.rvVaccine.adapter = adapters
                    } else {
                        adapters.listVaccineManagement.addAll(it.data.content as ArrayList<ContentVaccineMgmnt>)
                        adapters.notifyItemRangeChanged(
                            adapters.listVaccineManagement.size - it.data.content.size,
                            adapters.listVaccineManagement.size
                        )
                    }
                }

            }
        }
    }

    private fun loadData() {
        homeManagementViewModel.getListVaccineManagement(employeeId, page)
    }

    override fun onClickVaccineMgmnt(
        vaccineCertificate: String,
        idVaccine: Int,
        vaccineType: String
    ) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CHANGE_VACCINE,"")
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_VACCINE, idVaccine)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.VACCINE_NAME, vaccineType)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.VACCINE_CERTIFICATE, vaccineCertificate)
        startActivity(Intent(this, ChangeVaccineManagementActivity::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    //fun
}