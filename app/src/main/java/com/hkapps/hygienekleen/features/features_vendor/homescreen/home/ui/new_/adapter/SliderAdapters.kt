package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment.SliderNewsPagesLowFragment

class SliderAdapters(activity: FragmentActivity, private val data : List<String>): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = SliderNewsPagesLowFragment()
        fragment.arguments = Bundle().apply {
            putString("image_url", data[position])
        }
        return fragment
    }
}