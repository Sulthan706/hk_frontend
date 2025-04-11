package com.hkapps.hygienekleen.features.features_vendor.service.approval.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_management.service.resign.ui.fragment.ListResignManagementFragment
import com.hkapps.hygienekleen.features.features_vendor.service.approval.ui.fragment.AttendanceApprovalFragment

class ViewPagerApprovalAdapter(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = ListResignManagementFragment()
            1 -> fragment = AttendanceApprovalFragment()
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when(position) {
            0 -> title = "Turnover"
            1 -> title = "Absensi"
        }
        return title
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

}