package com.hkapps.hygienekleen.features.features_management.damagereport.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.fragment.AllBakManagementFragment
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.fragment.FinishBakManagementFragment
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.fragment.NotFinishBakManagementFragment

class VwBakManagementAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    private val fragmentArray = SparseArrayCompat<Fragment>()

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> AllBakManagementFragment()
            1 -> NotFinishBakManagementFragment()
            2 -> FinishBakManagementFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
        fragmentArray.put(position, fragment)
        return fragment
    }

    fun getFragment(position: Int): Fragment? {
        return fragmentArray[position]
    }





}