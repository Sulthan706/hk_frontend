package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.fragment.*

class ViewPagerTeamSpvAdapter(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 6
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = MorningTeamSpvFragment()
            1 -> fragment = DayTeamSpvFragment()
            2 -> fragment = NightTeamSpvFragment()
            3 -> fragment = DayOffTeamSpvFragment()
            4 -> fragment = MiddleTeamSpvFragment()
            5 -> fragment = WeekdaysTeamSpvFragment()
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when(position) {
            0 -> title = "Pagi"
            1 -> title = "Siang"
            2 -> title = "Malam"
            3 -> title = "Libur"
            4 -> title = "Middle"
            5 -> title = "Weekdays"
        }
        return title
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}