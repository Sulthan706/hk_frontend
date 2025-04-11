package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment.SliderNewsPageFragment

class SliderAdapter(activity: FragmentActivity, private val data: List<String>): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = SliderNewsPageFragment()
        fragment.arguments = Bundle().apply {
            putString("image_url", data[position])
        }
        return fragment
    }
}