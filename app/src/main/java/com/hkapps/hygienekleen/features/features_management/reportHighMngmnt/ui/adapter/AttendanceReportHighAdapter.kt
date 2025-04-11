package com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.ui.fragment.AbsenceReportManagementFragment
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.ui.fragment.PresenceReportManagementFragment

class AttendanceReportHighAdapter(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = AbsenceReportManagementFragment()
            1 -> fragment = PresenceReportManagementFragment()
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when(position) {
            0 -> title = "Absence"
            1 -> title = "Presence"
        }
        return title
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}