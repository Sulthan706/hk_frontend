package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentMyProjectCftalkBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listAllProject.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listProjectByUserId.Project
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.CftalkManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.CftalksByProjectActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter.ProjectsAllCftalkAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter.ProjectsEmployeeIdCftalkAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.viewmodel.CftalkManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView


class MyProjectCftalkFragment : Fragment(),
    ProjectsEmployeeIdCftalkAdapter.ProjectsEmployeeCallBack,
    ProjectsAllCftalkAdapter.ProjectsAllCallBack {

    private lateinit var binding: FragmentMyProjectCftalkBinding
    private lateinit var rvAdapter: ProjectsEmployeeIdCftalkAdapter
    private lateinit var adapter: ProjectsAllCftalkAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var page = 0
    private var isLastPage = false
    private val size = 10

    private val viewModel: CftalkManagementViewModel by lazy {
        ViewModelProviders.of(this).get(CftalkManagementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyProjectCftalkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set shimmer effect
        binding.shimmerMyProjectCftalk.startShimmerAnimation()
        binding.shimmerMyProjectCftalk.visibility = View.VISIBLE
        binding.rvMyProjectCftalk.visibility = View.GONE
        binding.flMyProjectCftalkNoData.visibility = View.GONE
        binding.flMyProjectCftalkNoInternet.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(requireActivity())
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
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
            return true
        }
        return false
    }

    private fun noInternetState(){
        binding.shimmerMyProjectCftalk.visibility = View.GONE
        binding.rvMyProjectCftalk.visibility = View.GONE
        binding.flMyProjectCftalkNoData.visibility = View.GONE
        binding.flMyProjectCftalkNoInternet.visibility = View.VISIBLE
//        binding.flMyProjectCftalkNoInternet.layoutConnectionTimeout.btnRetry.setOnClickListener {
//            startActivity(Intent(context, CftalkManagementActivity::class.java))
//            requireActivity().finish()
//        }
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            startActivity(Intent(context, CftalkManagementActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun noDataState() {
        binding.shimmerMyProjectCftalk.visibility = View.GONE
        binding.rvMyProjectCftalk.visibility = View.GONE
        binding.flMyProjectCftalkNoInternet.visibility = View.GONE
        binding.flMyProjectCftalkNoData.visibility = View.VISIBLE
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerMyProjectCftalk.startShimmerAnimation()
        binding.shimmerMyProjectCftalk.visibility = View.VISIBLE
        binding.rvMyProjectCftalk.visibility = View.GONE
        binding.flMyProjectCftalkNoData.visibility = View.GONE
        binding.flMyProjectCftalkNoInternet.visibility = View.GONE

        // set recycler view list operational
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvMyProjectCftalk.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager){
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if(!isLastPage){
                    page++
                    loadData()
                }
            }

        }

        binding.swipeRefreshLayoutMyProjectCftalk.setColorSchemeResources(R.color.red)
        binding.swipeRefreshLayoutMyProjectCftalk.setOnRefreshListener {
            page = 0
            loadData()
        }

        binding.swipeRefreshLayoutMyProjectCftalk.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                {
                    binding.swipeRefreshLayoutMyProjectCftalk.isRefreshing = false
                    startActivity(Intent(requireActivity(), CftalkManagementActivity::class.java))
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500)
        })

        binding.rvMyProjectCftalk.addOnScrollListener(scrollListener)
        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerMyProjectCftalk.stopShimmerAnimation()
                        binding.shimmerMyProjectCftalk.visibility = View.GONE
                        binding.rvMyProjectCftalk.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.projectsByEmployeeIdModel.observe(requireActivity()) {
            if (it.code == 200) {
                if (page == 0) {
                    if (it.data.listProject.isNotEmpty()) {
                        rvAdapter = ProjectsEmployeeIdCftalkAdapter(
                            requireContext(),
                            it.data.listProject as ArrayList<Project>
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvMyProjectCftalk.adapter = rvAdapter
                    } else {
                        binding.rvMyProjectCftalk.adapter = null
                        noDataState()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil list project", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.allProjectsCftalkModel.observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    isLastPage = it.data.last
                    if (page == 0) {
                        adapter = ProjectsAllCftalkAdapter(
                            requireContext(),
                            it.data.content as ArrayList<Content>
                        ).also { it.setListener(this) }
                        binding.rvMyProjectCftalk.adapter = adapter
                    } else {
                        adapter.listAllProject.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listAllProject.size - it.data.content.size,
                            adapter.listAllProject.size
                        )
                    }
                } else {
                    noDataState()
                }
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil data list project", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        if (levelJabatan == "FM" || levelJabatan == "OM" || levelJabatan == "GM") {
            viewModel.getProjectsByEmployeeId(userId)
        } else {
            viewModel.getAllProjects(page, size)
        }
    }

    override fun onClickProjectEmployee(projectName: String, projectCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_CFTALK_MANAGEMENT, projectName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_CFTALK_MANAGEMENT, projectCode)
        startActivity(Intent(requireContext(), CftalksByProjectActivity::class.java))
    }

    override fun onClickProject(projectName: String, projectCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_CFTALK_MANAGEMENT, projectName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_CFTALK_MANAGEMENT, projectCode)
        startActivity(Intent(requireContext(), CftalksByProjectActivity::class.java))
    }

}