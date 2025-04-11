package com.hkapps.hygienekleen.features.features_management.damagereport.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.FragmentAllBakManagementBinding
import com.hkapps.hygienekleen.features.features_management.damagereport.model.listdamagereport.ContentDamageReportManagement
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.adapter.ListDamageReportManagementAdapter
import com.hkapps.hygienekleen.features.features_management.damagereport.viewmodel.DamageReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView


class AllBakManagementFragment : Fragment() {
    private lateinit var binding: FragmentAllBakManagementBinding
    private lateinit var viewModel: DamageReportManagementViewModel
    private lateinit var adapters: ListDamageReportManagementAdapter
    var projectCode : String =""
    var page = 0
    var isLastPage: Boolean = false
    var dates: String = ""
    var filter: String = "SEMUA"

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllBakManagementBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[DamageReportManagementViewModel::class.java]

        // set shimmer effect
        binding.shimmerListBak.startShimmerAnimation()
        binding.shimmerListBak.visibility = View.VISIBLE
        binding.rvDamageReportManagement.visibility = View.GONE

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvDamageReportManagement.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvDamageReportManagement.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getListDamageReportMgmnt(userId,projectCode, dates, filter, page)
    }

    private fun setObserver() {
        viewModel.getListDamageReportMgmntViewModel().observe(requireActivity()){
            if (it.code == 200){
                if (it.data.content.isNotEmpty()){
                    binding.tvEmptyListBakManagement.visibility = View.GONE
                    binding.shimmerListBak.stopShimmerAnimation()
                    binding.shimmerListBak.visibility = View.GONE
                    binding.rvDamageReportManagement.visibility = View.VISIBLE
                    binding.tvEmptyListBakManagement.visibility = View.GONE

                    isLastPage = it.data.last
                    if (page == 0){
                        adapters = ListDamageReportManagementAdapter(requireContext(), it.data.content as ArrayList<ContentDamageReportManagement>)
                        binding.rvDamageReportManagement.adapter = adapters
                    }else {
                        adapters.listDamageReport.addAll(it.data.content)
                        adapters.notifyItemRangeChanged(
                            adapters.listDamageReport.size - it.data.content.size,
                            adapters.listDamageReport.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerListBak.stopShimmerAnimation()
                        binding.shimmerListBak.visibility = View.GONE
                        binding.tvEmptyListBakManagement.visibility = View.VISIBLE
                        binding.rvDamageReportManagement.visibility = View.GONE
                    }, 1500)
                }


            } else {
                Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateDate(date: String){
        page = 0
        this.dates = date
        loadData()
    }

    fun updateProject(projectCode: String){
        page = 0
        this.projectCode = projectCode
        loadData()
    }



}