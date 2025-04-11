package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity

import android.annotation.SuppressLint
import android.app.Dialog
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
import com.hkapps.hygienekleen.databinding.ActivityHomeNewsBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.ListNewsAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listhomenews.Content
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class HomeNewsActivity : AppCompatActivity(), ListNewsAdapter.ListNews {
    private lateinit var binding: ActivityHomeNewsBinding
    private val NewsViewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    //pref
    private val userLevelPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    //val
    var page: Int = 0
    private var isLastPage = false
    private var loadingDialog: Dialog? = null
    //adapter
    private lateinit var newsAdapter: ListNewsAdapter
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeNewsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.layoutAppbarHomeNews.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        binding.layoutAppbarHomeNews.tvAppbarTitle.text = "Pengumuman"

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHomeNews.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.rvHomeNews.addOnScrollListener(scrollListener)

        setObserver()
        loadData()
        showLoading("Loading..")
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun loadData() {
        NewsViewModel.getListHomeNews(page, userId, userType = "Operator")
    }

    private fun setObserver() {
        //list news observer
        NewsViewModel.getListNewsViewModel().observe(this){
            if (it.code == 200){
                binding.rvHomeNews.visibility = View.VISIBLE
                binding.tvEmptyStateNews.visibility = View.GONE
                isLastPage = false
                if (page == 0){
                    newsAdapter = ListNewsAdapter(it.data.content as ArrayList<Content>
                    ).also { it.setListener(this) }
                    binding.rvHomeNews.adapter = newsAdapter
                } else {
                    newsAdapter.homeNews.addAll(it.data.content as ArrayList<Content>)
                    newsAdapter.notifyItemRangeChanged(
                        newsAdapter.homeNews.size - it.data.size,
                        newsAdapter.homeNews.size
                    )
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
        //read news observer
        NewsViewModel.putReadNewsViewModel().observe(this){
            if (it.code == 200){
                Log.d("TAG","deleted")
            } else {
                Log.d("TAG","gagal hapus")
            }
        }
    }
    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onClickNews(newsId: Int, isRead: String) {
        Log.d("TAG","$newsId")
        if (isRead == "N"){
            NewsViewModel.putReadNews(userType = "Operator", userId, newsId)
            startActivity(Intent(this, DetailNewsActivity::class.java))
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_NEWS, newsId)
        } else {
            startActivity(Intent(this, DetailNewsActivity::class.java))
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_NEWS, newsId)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }


    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }

}
