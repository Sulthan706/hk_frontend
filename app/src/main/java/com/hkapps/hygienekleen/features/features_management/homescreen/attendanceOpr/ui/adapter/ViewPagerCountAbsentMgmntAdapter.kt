package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.fragment.ListCspvAbsentMgmntFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.fragment.ListOperationalAbsentMgmntFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.fragment.ListSpvAbsentMgmntFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.fragment.ListTeamLeadAbsentMgmntFragment

class ViewPagerCountAbsentMgmntAdapter(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return  4
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = ListOperationalAbsentMgmntFragment()
            1 -> fragment = ListTeamLeadAbsentMgmntFragment()
            2 -> fragment = ListSpvAbsentMgmntFragment()
            3 -> fragment = ListCspvAbsentMgmntFragment()
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
        return POSITION_NONE
    }
}