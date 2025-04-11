package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ClosingSpvAdapter(data : ArrayList<Fragment>, fm : FragmentManager, lifecycle: Lifecycle):  FragmentStateAdapter(fm,lifecycle) {
    private val listData = data
    override fun getItemCount(): Int  = listData.size

    override fun createFragment(position: Int): Fragment = listData[position]
}