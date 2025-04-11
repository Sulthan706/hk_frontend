package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.fragment.MyProjectCftalkFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.fragment.OtherProjectCftalkFragment

class CftalkManagementAdapter(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = MyProjectCftalkFragment()
            1 -> fragment = OtherProjectCftalkFragment()
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when(position) {
            0 -> title = "Project Saya"
            1 -> title = "Project Lain"
        }
        return title
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}