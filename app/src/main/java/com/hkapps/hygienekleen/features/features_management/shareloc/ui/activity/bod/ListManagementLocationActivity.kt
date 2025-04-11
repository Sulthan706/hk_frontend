package com.hkapps.hygienekleen.features.features_management.shareloc.ui.activity.bod

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListManagementLocationBinding
import com.hkapps.hygienekleen.features.features_management.shareloc.model.allsharelocmanagement.Management
import com.hkapps.hygienekleen.features.features_management.shareloc.ui.adapter.ListAllManagementLocationAdapter
import com.hkapps.hygienekleen.features.features_management.shareloc.ui.fragment.BotSheetSearchManagementFragment
import com.hkapps.hygienekleen.features.features_management.shareloc.viewmodel.ShareLocManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListManagementLocationActivity : AppCompatActivity(),
    ListAllManagementLocationAdapter.ClickManagement {
    private lateinit var binding: ActivityListManagementLocationBinding
    private val viewModel: ShareLocManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ShareLocManagementViewModel::class.java)
    }


    private lateinit var adapters: ListAllManagementLocationAdapter

    private var page:Int = 0
    private var isLastPage:Boolean = false
    private var keywords: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListManagementLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //appbar
        binding.appbarShareLocListMgmnt.tvAppbarTitle.text = "List Manajemen"
        binding.appbarShareLocListMgmnt.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        binding.appbarShareLocListMgmnt.ivAppbarHistory.setOnClickListener {
            BotSheetSearchManagementFragment().show(supportFragmentManager, "botsheetsearchmanagement")
        }

        //setup rv
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvListAllShareLocMgmnt.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvListAllShareLocMgmnt.addOnScrollListener(scrollListener)

        //date
        val timeNow = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale("id","ID"))
        val sdf = timeNow.format(Calendar.getInstance().time)
        binding.tvTimeNowShareBod.text = sdf

        //swipe refresh
        binding.swipeLayoutListManagement.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed( {
                binding.swipeLayoutListManagement.isRefreshing = false
                val i = Intent(this, ListManagementLocationActivity::class.java)
                startActivity(i)
                finish()
                overridePendingTransition(R.anim.nothing, R.anim.nothing)
            }, 500
            )
        })

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun loadData() {
        viewModel.getAllShareLocManagement()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getAllShareLocManagementViewModel().observe(this){
            if (it.code == 200){
                if (it.data.listManagement.isNotEmpty()){
                    binding.tvEmptyStateListManagement.visibility = View.GONE
                    binding.rvListAllShareLocMgmnt.visibility = View.VISIBLE
                    isLastPage = false
                    if (page == 0){
                        adapters = ListAllManagementLocationAdapter(
                            this, it.data.listManagement as ArrayList<Management>
                        ).also { it.setListener(this) }
                        binding.rvListAllShareLocMgmnt.adapter = adapters
                    } else {
                        adapters.listALlManagement.addAll(it.data.listManagement as ArrayList<Management>)
                        adapters.notifyItemRangeChanged(
                            adapters.listALlManagement.size - it.data.listManagement.size,
                            adapters.listALlManagement.size
                        )
                    }
                } else {
                    isLastPage = true
                    binding.rvListAllShareLocMgmnt.adapter = null
                }
                binding.tvCountAllShareLoc.text = "${it.data.countAll} Orang"

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }

    override fun onClickManagement(managementId: Int, managementName: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_NAME, managementName)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_EMPLOYEE_MANAGEMENT, managementId)
        startActivity(Intent(this, DetailManagementLocationActivity::class.java))
    }


}