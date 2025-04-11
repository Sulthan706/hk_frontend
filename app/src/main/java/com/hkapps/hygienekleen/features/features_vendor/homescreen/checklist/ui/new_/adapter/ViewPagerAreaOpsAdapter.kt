package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.fragment.ListAreaChecklistFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.fragment.ListOperationalChecklistFragment

class ViewPagerAreaOpsAdapter(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = ListAreaChecklistFragment()
            1 -> fragment = ListOperationalChecklistFragment()
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when(position) {
            0 -> title = "Area"
            1 -> title = "Operational"
        }
        return title
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

}