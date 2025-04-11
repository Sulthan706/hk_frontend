package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityZoomNewsBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.adapter.ZoomSliderAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ZoomNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityZoomNewsBinding
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private val newsId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_NEWS, 0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZoomNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val viewPager = binding.zoomViewPager
//        val images = listOf(imageUrl)
//        val adapter = ZoomSliderAdapter(this, images)
//        viewPager.adapter = adapter

        loadData()
        setObserver()

    }

    private fun loadData() {
        viewModel.getDetailNews(newsId)
    }

    private fun setObserver() {
        viewModel.getDetailNewsViewModel().observe(this){
            if (it.code == 200){
                val viewPager = binding.zoomViewPager
                val images = listOf(it.data.newsImage)
                val adapter = ZoomSliderAdapter(this, images)
                viewPager.adapter = adapter
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }


}