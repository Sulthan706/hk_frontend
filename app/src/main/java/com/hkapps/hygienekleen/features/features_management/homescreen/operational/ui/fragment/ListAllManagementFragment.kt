package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.FragmentListAllManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.ListAllManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listceomanagement.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.ProfileManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.ListGmOmFmAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listgmfmom.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.TabMenuJobRoleAdapter


class ListAllManagementFragment : Fragment(),
    ListAllManagementAdapter.ListAllManagementCallback,
    ListGmOmFmAdapter.ListGmOmFmCallback,
    TabMenuJobRoleAdapter.ListJobRoleCallBack {

    private lateinit var binding: FragmentListAllManagementBinding
    private lateinit var adapter: ListAllManagementAdapter
    private lateinit var rvAdapter: ListGmOmFmAdapter
    private lateinit var jobRoleAdapter: TabMenuJobRoleAdapter
    private val userPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var dataNoInternet: String = "Internet"
    private var page = 0
    private var isLastPage = false
    private var jobRole = "Semua"

    private val viewModel: OperationalManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OperationalManagementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListAllManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(requireActivity())
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    viewIsOnline()
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    viewIsOnline()
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    viewIsOnline()
                    return true
                }
            }
        } else {
            noInternetState()
            dataNoInternet = "noInternet"
            return true
        }
        return false
    }

    private fun viewIsOnline() {
        //set shimmer effect
        binding.shimmerListAllManagement.startShimmerAnimation()
        binding.shimmerListAllManagement.visibility = View.VISIBLE
        binding.rvListAllManagement.visibility = View.GONE
        binding.flListAllManagementNoInternet.visibility = View.GONE
        binding.flListAllManagementNoData.visibility = View.GONE

        // set recycler view job role
        val layoutManager1 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListJobRole.layoutManager = layoutManager1

        val listJobRole = ArrayList<String>()
        when (userPosition) {
            "CEO" -> {
                listJobRole.add("Semua")
                listJobRole.add("FM")
                listJobRole.add("GM")
                listJobRole.add("OM")
                listJobRole.add("BOD")
                listJobRole.add("CLIENT")
            }

            else -> {
                listJobRole.add("Semua")
                listJobRole.add("FM")
                listJobRole.add("GM")
                listJobRole.add("OM")
            }
        }

        jobRoleAdapter =
            TabMenuJobRoleAdapter(requireContext(), listJobRole).also { it.setListener(this) }
        binding.rvListJobRole.adapter = jobRoleAdapter

        // set recycler view
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvListAllManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    when (userPosition) {
                        "CEO" -> {
                            viewModel.getListCEO(page)
                        }

                        "BOD" -> {
                            viewModel.getListBOD(page)
                        }
                    }
                }
            }

        }
        binding.rvListAllManagement.addOnScrollListener(scrollListener)
        loadData()
        setObserver()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(context, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListAllManagement.stopShimmerAnimation()
                        binding.shimmerListAllManagement.visibility = View.GONE
                        binding.rvListAllManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getListCEOResponse().observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.flListAllManagementNoData.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        adapter = ListAllManagementAdapter(
                            requireContext(),
                            it.data.content as ArrayList<Content>,
                            viewModel,
                            this
                        ).also { it.setListener(this) }
                        binding.rvListAllManagement.adapter = adapter
                    } else {
                        adapter.listManagement.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listManagement.size - it.data.content.size,
                            adapter.listManagement.size
                        )
                    }
                } else {
                    binding.rvListAllManagement.adapter = null
                    noDataState()
                }
            } else {
                Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getListBODResponse().observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.flListAllManagementNoData.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        adapter = ListAllManagementAdapter(
                            requireContext(),
                            it.data.content as ArrayList<Content>,
                            viewModel,
                            this
                        ).also { it.setListener(this) }
                        binding.rvListAllManagement.adapter = adapter
                    } else {
                        adapter.listManagement.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listManagement.size - it.data.content.size,
                            adapter.listManagement.size
                        )
                    }

                } else {
                    binding.rvListAllManagement.adapter = null
                    noDataState()
                }
            } else {
                Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getListGmOmFmResponse().observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    binding.flListAllManagementNoData.visibility = View.GONE
                    rvAdapter = ListGmOmFmAdapter(
                        requireContext(),
                        it.data as ArrayList<Data>,
                        viewModel,
                        this
                    ).also { it.setListener(this) }
                    binding.rvListAllManagement.adapter = rvAdapter
                } else {
                    binding.rvListAllManagement.adapter = null
                    noDataState()
                }
            } else {
                Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.listManagementByRoleCeoBodModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    binding.flListAllManagementNoData.visibility = View.GONE
                    rvAdapter = ListGmOmFmAdapter(
                        requireContext(),
                        it.data as ArrayList<Data>,
                        viewModel,
                        this
                    ).also { it.setListener(this) }
                    binding.rvListAllManagement.adapter = rvAdapter
                } else {
                    binding.rvListAllManagement.adapter = null
                    noDataState()
                }
            } else {
                Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.listManagementByRoleFmGMOmModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    binding.flListAllManagementNoData.visibility = View.GONE
                    rvAdapter = ListGmOmFmAdapter(
                        requireContext(),
                        it.data as ArrayList<Data>,
                        viewModel,
                        this
                    ).also { it.setListener(this) }
                    binding.rvListAllManagement.adapter = rvAdapter
                } else {
                    binding.rvListAllManagement.adapter = null
                    noDataState()
                }
            } else {
                Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loadData() {
        when (userPosition) {
            "CEO" -> {
                if (jobRole == "Semua") {
                    viewModel.getListCEO(page)
                } else {
                    viewModel.getListManagementByRoleCeoBod(jobRole)
                }
            }

            "BOD" -> {
                if (jobRole == "Semua") {
                    viewModel.getListBOD(page)
                } else {
                    viewModel.getListManagementByRoleCeoBod(jobRole)
                }
            }

            "GM", "OM", "FM", "CLIENT" -> {
                if (jobRole == "Semua") {
                    viewModel.getGmOmFm(employeeId)
                } else {
                    viewModel.getListManagementByRoleFmGmOm(employeeId, jobRole)
                }
            }
        }

    }

    private fun noInternetState() {
        binding.shimmerListAllManagement.visibility = View.GONE
        binding.rvListAllManagement.visibility = View.GONE
        binding.flListAllManagementNoData.visibility = View.GONE
        binding.flListAllManagementNoInternet.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            startActivity(Intent(context, ListAllManagementFragment::class.java))
            requireActivity().finish()
        }
    }

    private fun noDataState() {
        binding.shimmerListAllManagement.visibility = View.GONE
        binding.rvListAllManagement.visibility = View.GONE
        binding.flListAllManagementNoInternet.visibility = View.GONE
        binding.flListAllManagementNoData.visibility = View.VISIBLE
    }


    override fun onClickManagement(adminMasterId: Int, adminMastername: String) {
        CarefastOperationPref.saveInt(
            CarefastOperationPrefConst.OPERATIONAL_MANAGEMENT_ADMIN_MASTER_ID,
            adminMasterId
        )
        startActivity(Intent(requireActivity(), ProfileManagementActivity::class.java))
    }

    override fun onClickGmOmFm(adminMasterId: Int, adminMasterName: String) {
        CarefastOperationPref.saveInt(
            CarefastOperationPrefConst.OPERATIONAL_MANAGEMENT_ADMIN_MASTER_ID,
            adminMasterId
        )
        startActivity(Intent(requireActivity(), ProfileManagementActivity::class.java))
    }

    override fun onClickJobRole(jobRole: String) {
        this.jobRole = jobRole

        //set shimmer effect
        binding.shimmerListAllManagement.startShimmerAnimation()
        binding.shimmerListAllManagement.visibility = View.VISIBLE
        binding.rvListAllManagement.visibility = View.GONE
        binding.flListAllManagementNoInternet.visibility = View.GONE
        binding.flListAllManagementNoData.visibility = View.GONE
        binding.rvListAllManagement.adapter = null
        page = 0

        loadData()
        setObserver()
    }

}