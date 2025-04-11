package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewpagerRkbAdapter(fm: FragmentManager, private val fragments: List<Fragment>):
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
       return fragments.size
    }

    override fun getItem(position: Int): Fragment {
         return fragments[position]
    }
}