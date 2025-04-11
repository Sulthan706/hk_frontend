package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.fragment.ChecklistSpvDayFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.fragment.ChecklistSpvMorningFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.fragment.ChecklistSpvNightFragment

class ViewPagerChecklistSpvAdapter(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = ChecklistSpvMorningFragment()
            1 -> fragment = ChecklistSpvDayFragment()
            2 -> fragment = ChecklistSpvNightFragment()
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when(position) {
            0 -> title = "Pagi"
            1 -> title = "Siang"
            2 -> title = "Malam"
        }
        return title
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}