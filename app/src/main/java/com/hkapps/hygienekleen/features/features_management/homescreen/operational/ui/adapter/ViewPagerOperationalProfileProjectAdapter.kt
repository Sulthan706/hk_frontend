package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment.ListChiefSpvProfileProjectFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment.ListOperationalProfileProjectFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment.ListSpvProfileProjectFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment.ListTeamLeaderProfileProjectFragment

class ViewPagerOperationalProfileProjectAdapter(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {


    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = ListOperationalProfileProjectFragment()
            1 -> fragment = ListTeamLeaderProfileProjectFragment()
            2 -> fragment = ListSpvProfileProjectFragment()
            3 -> fragment = ListChiefSpvProfileProjectFragment()
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when(position) {
            0 -> title = "Operasional"
            1 -> title = "Team Leader"
            2 -> title = "SPV"
            3 -> title = "C-SPV"
        }
        return title
    }

    override fun getItemPosition(`object`: Any): Int {
        return FragmentPagerAdapter.POSITION_NONE
    }
}