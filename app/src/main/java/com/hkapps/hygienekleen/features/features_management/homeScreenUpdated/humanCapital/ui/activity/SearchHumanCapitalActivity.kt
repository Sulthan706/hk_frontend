package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivitySearchHumanCapitalBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerBod.Content
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.adapter.HumanCapitalBodAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.adapter.HumanCapitalManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.viewmodel.HumanCapitalViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class SearchHumanCapitalActivity : AppCompatActivity(),
    HumanCapitalBodAdapter.HumanCapitalBodCallback,
    HumanCapitalManagementAdapter.HumanCapitalManagementCallback {

    private lateinit var binding: ActivitySearchHumanCapitalBinding
    private lateinit var rvManagement: HumanCapitalManagementAdapter
    private lateinit var rvBod: HumanCapitalBodAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, "")

    private var keywords = ""
    private var filter = "All"
    private var isLastPage = false
    private var page = 0
    private val perPage = 10

    private val viewModel: HumanCapitalViewModel by lazy {
        ViewModelProviders.of(this)[HumanCapitalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchHumanCapitalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set app bar
        binding.appbarSearchHumanCapital.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set default layout
        binding.shimmerSearchHumanCapital.visibility = View.GONE
        binding.rvSearchHumanCapital.visibility = View.GONE
        binding.llEmptySearchHumanCapital.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchHumanCapital.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    keywords = ""
                    loadData()
                }
            }
        }
        binding.rvSearchHumanCapital.addOnScrollListener(scrollListener)

        // set appbar search
        binding.appbarSearchHumanCapital.svAppbarSearch.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.shimmerSearchHumanCapital.startShimmerAnimation()
                binding.shimmerSearchHumanCapital.visibility = View.VISIBLE
                binding.rvSearchHumanCapital.visibility = View.GONE
                binding.rvSearchHumanCapital.adapter = null
                binding.llEmptySearchHumanCapital.visibility = View.GONE

                page = 0
                keywords = query ?: ""
                loadData()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.shimmerSearchHumanCapital.startShimmerAnimation()
                binding.shimmerSearchHumanCapital.visibility = View.VISIBLE
                binding.rvSearchHumanCapital.visibility = View.GONE
                binding.rvSearchHumanCapital.adapter = null
                binding.llEmptySearchHumanCapital.visibility = View.GONE

                page = 0
                keywords = newText ?: ""
                loadData()
                return true
            }

        })

        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) {
            if (it != null) {
                if (it) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerSearchHumanCapital.stopShimmerAnimation()
                        binding.shimmerSearchHumanCapital.visibility = View.GONE
                    }, 500)
                }
            }
        }
        viewModel.manPowerBodResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.listEmployeeDetail.content.isEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.llEmptySearchHumanCapital.visibility = View.VISIBLE
                        binding.rvSearchHumanCapital.visibility = View.GONE
                        binding.rvSearchHumanCapital.adapter = null
                    }, 500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.llEmptySearchHumanCapital.visibility = View.GONE
                        binding.rvSearchHumanCapital.visibility = View.VISIBLE

                        isLastPage = it.data.listEmployeeDetail.last
                        if (page == 0) {
                            rvBod = HumanCapitalBodAdapter(
                                this,
                                it.data.listEmployeeDetail.content as ArrayList<Content>
                            ).also { it1 -> it1.setListener(this) }
                            binding.rvSearchHumanCapital.adapter = rvBod
                        } else {
                            rvBod.listManPower.addAll(it.data.listEmployeeDetail.content)
                            rvBod.notifyItemRangeChanged(
                                rvBod.listManPower.size - it.data.listEmployeeDetail.content.size,
                                rvBod.listManPower.size
                            )
                        }
                    }, 500)
                }
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.llEmptySearchHumanCapital.visibility = View.VISIBLE
                    binding.rvSearchHumanCapital.visibility = View.GONE
                    binding.rvSearchHumanCapital.adapter = null
                    Toast.makeText(this, "${it.errorCode} ${it.message}", Toast.LENGTH_SHORT).show()
                }, 500)
            }
        }
        viewModel.manPowerManagementResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.listEmployeeDetail.content.isEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.llEmptySearchHumanCapital.visibility = View.VISIBLE
                        binding.rvSearchHumanCapital.visibility = View.GONE
                        binding.rvSearchHumanCapital.adapter = null
                    }, 500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.llEmptySearchHumanCapital.visibility = View.GONE
                        binding.rvSearchHumanCapital.visibility = View.VISIBLE

                        isLastPage = it.data.listEmployeeDetail.last
                        if (page == 0) {
                            rvManagement = HumanCapitalManagementAdapter(
                                this,
                                it.data.listEmployeeDetail.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerManagement.Content>
                            ).also { it1 -> it1.setListener(this) }
                            binding.rvSearchHumanCapital.adapter = rvManagement
                        } else {
                            rvManagement.listManPower.addAll(it.data.listEmployeeDetail.content)
                            rvManagement.notifyItemRangeChanged(
                                rvManagement.listManPower.size - it.data.listEmployeeDetail.content.size,
                                rvManagement.listManPower.size
                            )
                        }
                    }, 500)
                }
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.llEmptySearchHumanCapital.visibility = View.VISIBLE
                    binding.rvSearchHumanCapital.visibility = View.GONE
                    binding.rvSearchHumanCapital.adapter = null
                    Toast.makeText(this, "${it.errorCode} ${it.message}", Toast.LENGTH_SHORT).show()
                }, 500)
            }
        }
    }

    private fun loadData() {
        if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
            viewModel.getManPowerBod(branchCode, keywords, filter, page, perPage)
        } else {
            viewModel.getManPowerManagement(userId, keywords, filter, page, perPage)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickMpBod(userId: Int, projectCode: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, userId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.OPERATIONAL_OPS_PROJECT_CODE, projectCode)
        startActivity(Intent(this, ProfileEmployeeActivity::class.java))
    }

    override fun onClickMpManagement(userId: Int, projectCode: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, userId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.OPERATIONAL_OPS_PROJECT_CODE, projectCode)
        startActivity(Intent(this, ProfileEmployeeActivity::class.java))
    }
}