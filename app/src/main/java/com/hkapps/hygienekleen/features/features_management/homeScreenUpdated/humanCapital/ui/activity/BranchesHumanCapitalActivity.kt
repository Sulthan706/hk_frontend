package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.activity

import android.annotation.SuppressLint
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
import com.hkapps.hygienekleen.databinding.ActivityBranchesHumanCapitalBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listBranch.Content
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.adapter.BranchesHumanCapitalAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.viewmodel.HumanCapitalViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import java.text.SimpleDateFormat
import java.util.Date

class BranchesHumanCapitalActivity : AppCompatActivity(),
    BranchesHumanCapitalAdapter.BranchesHumanCapitalCallBack {

    private lateinit var binding: ActivityBranchesHumanCapitalBinding
    private lateinit var rvAdapter: BranchesHumanCapitalAdapter

    var isLastPage = false
    var page = 0
    val perPage = 10
    private var currentDate = ""

    private val viewModel: HumanCapitalViewModel by lazy {
        ViewModelProviders.of(this)[HumanCapitalViewModel::class.java]
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBranchesHumanCapitalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarBranchesHumanCapital.tvAppbarTitle.text = "Human Capital"
        binding.appbarBranchesHumanCapital.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvBranchesHumanCapital.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadDataBranch()
                }
            }

        }
        binding.rvBranchesHumanCapital.addOnScrollListener(scrollListener)

        // swipe refresh layout
        binding.swipeRefreshBranchesHumanCapital.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshBranchesHumanCapital.isRefreshing = false
                loadingLayout()
                loadDataBranch()
                overridePendingTransition(R.anim.nothing, R.anim.nothing)
            }, 200)
        }

        // get current date
        val sdf = SimpleDateFormat("dd MMM yyyy")
        currentDate = sdf.format(Date())

        loadingLayout()
        loadDataBranch()
    }

    private fun loadingLayout() {
        page = 0
        isLastPage = false
        binding.shimmerBranchesHumanCapital.startShimmerAnimation()
        binding.shimmerBranchesHumanCapital.visibility = View.VISIBLE
        binding.clDashboardBranchesHumanCapital.visibility = View.GONE
        binding.rvBranchesHumanCapital.visibility = View.GONE
        binding.tvErrorBranchesHumanCapital.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun loadDataBranch() {
        viewModel.getBranchesHumanCapital(page, perPage)
        viewModel.isLoading?.observe(this) {
            if (it != null) {
                if (it) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerBranchesHumanCapital.stopShimmerAnimation()
                        binding.shimmerBranchesHumanCapital.visibility = View.GONE
                        binding.clDashboardBranchesHumanCapital.visibility = View.VISIBLE
                        binding.rvBranchesHumanCapital.visibility = View.VISIBLE
                    }, 500)
                }
            }
        }
        viewModel.branchesHumanCapitalResponse.observe(this) {
            if (it.code == 200) {
                // set dashboard data
                binding.tvDateBranchesHumanCapital.text = "Today, $currentDate"
                val formattedNumber = "%,d".format(it.data.totalEmployeeAktif)
                binding.tvTotMpBranchesHumanCapital.text = "Total MP : $formattedNumber"
                binding.tvTotResignBranchesHumanCapital.text = "${it.data.totalResign}"
                binding.tvTotNewcomerBranchesHumanCapital.text = "${it.data.totalNew}"
                if (it.data.totalResign > it.data.totalNew) {
                    binding.tvTotTurnoverBranchesHumanCapital.setTextColor(resources.getColor(R.color.red4))
                    binding.tvTotTurnoverBranchesHumanCapital.text = "-${it.data.totalTurnover}"
                } else if (it.data.totalResign < it.data.totalNew) {
                    binding.tvTotTurnoverBranchesHumanCapital.setTextColor(resources.getColor(R.color.blue5))
                    binding.tvTotTurnoverBranchesHumanCapital.text = "+${it.data.totalTurnover}"
                } else if (it.data.totalResign == it.data.totalNew) {
                    binding.tvTotTurnoverBranchesHumanCapital.setTextColor(resources.getColor(R.color.green2))
                    binding.tvTotTurnoverBranchesHumanCapital.text = "="
                }

                // set rv adapter
                isLastPage = it.data.listProjectPerBranch.last
                if (page == 0) {
                    rvAdapter = BranchesHumanCapitalAdapter(
                        it.data.listProjectPerBranch.content as ArrayList<Content>
                    ).also { it1 -> it1.setListener(this) }
                    binding.rvBranchesHumanCapital.adapter = rvAdapter
                } else {
                    rvAdapter.listBranch.addAll(it.data.listProjectPerBranch.content)
                    rvAdapter.notifyItemRangeChanged(
                        rvAdapter.listBranch.size - it.data.listProjectPerBranch.content.size,
                        rvAdapter.listBranch.size
                    )
                }
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.tvErrorBranchesHumanCapital.visibility = View.VISIBLE
                    Toast.makeText(this, "${it.errorCode} ${it.message}", Toast.LENGTH_SHORT).show()
                }, 500)
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onRestart() {
        super.onRestart()
        loadingLayout()
        loadDataBranch()
    }

    override fun onClickBranch(branchCode: String, branchName: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, branchCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_NAME_PROJECT_MANAGEMENT, branchName)
        startActivity(Intent(this, ListHumanCapitalActivity::class.java))
    }
}