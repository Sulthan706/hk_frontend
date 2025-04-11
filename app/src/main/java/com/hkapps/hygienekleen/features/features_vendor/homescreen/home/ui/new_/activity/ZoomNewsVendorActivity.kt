package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityZoomNewsLowBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.adapter.ZoomSliderAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ZoomNewsVendorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityZoomNewsLowBinding
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    //preef
    val newsId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_NEWS, 0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZoomNewsLowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getDetailNews(newsId)
    }

    private fun setObserver() {
        viewModel.getDetailNewsViewModel().observe(this){
            if (it.code == 200){
                val viewPager = binding.zoomLowViewPager
                val images = listOf(it.data.newsImage)
                val adapter = ZoomSliderAdapter(this, images)
                viewPager.adapter = adapter
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }


}