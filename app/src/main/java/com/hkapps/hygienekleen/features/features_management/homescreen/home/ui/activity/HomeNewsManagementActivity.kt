package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityHomeNewsManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.listnewsmanagement.ContentListNewsManagement
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.adapter.ListNewsManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class HomeNewsManagementActivity : AppCompatActivity(), ListNewsManagementAdapter.ListNewsManagementCallback {
    private lateinit var binding : ActivityHomeNewsManagementBinding
    private val viewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }
    private lateinit var adapter: ListNewsManagementAdapter
    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var page:Int = 0
    private var isLastPage: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeNewsManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appbarNewsManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        binding.appbarNewsManagement.tvAppbarTitle.text = "Pengumuman"

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNewsManagement.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.rvNewsManagement.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun loadData() {
        viewModel.getListNewsManagement(page, userId, userType = "Management")
    }

    private fun setObserver() {
        viewModel.getListHomeNewsViewModel().observe(this){
            if (it.code == 200){
                binding.rvNewsManagement.visibility = View.VISIBLE
                binding.tvEmtpyStateNewsManagement.visibility = View.GONE
                isLastPage = it.data.last
                if (page == 0){
                    adapter = ListNewsManagementAdapter(it.data.content as ArrayList<ContentListNewsManagement>
                    ).also { it.setListener(this) }
                    binding.rvNewsManagement.adapter = adapter
                } else {
                    adapter.listNewsManagement.addAll(it.data.content as ArrayList<ContentListNewsManagement>)
                    adapter.notifyItemRangeChanged(
                        adapter.listNewsManagement.size - it.data.size,
                        adapter.listNewsManagement.size
                    )
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.putReadNewsManagementViewModel().observe(this){
            if (it.code == 200){
                Log.d("TAG","deleted")
            } else {
                Log.d("TAG","gagal hapus")
            }
        }
    }

    override fun onClickNews(newsId: Int, isRead: String) {
        if (isRead == "N"){
            viewModel.readNewsManagement(userType = "Management", userId, newsId)
            startActivity(Intent(this, DetailNewsManagementActivity::class.java))
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_NEWS, newsId)
        } else {
            startActivity(Intent(this, DetailNewsManagementActivity::class.java))
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_NEWS, newsId)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

}