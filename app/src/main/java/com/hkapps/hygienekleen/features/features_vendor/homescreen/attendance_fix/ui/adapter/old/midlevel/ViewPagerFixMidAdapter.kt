package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.old.midlevel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.fragment.old.midlevel.AttendanceFixMidFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.fragment.old.midlevel.AttendanceFixMidStaffFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback

class ViewPagerFixMidAdapter(fm: FragmentManager, type: Int, name: String, val context: Context) :
    FragmentPagerAdapter(fm) {
    private val type: Int = type
    val name: String = name

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        if (position == 0) {
            fragment = AttendanceFixMidFragment.newInstance(type)
                .also { it.setListener(context as NoInternetConnectionCallback) }
        } else if (position == 1) {
            fragment = AttendanceFixMidStaffFragment.newInstance(type)
                .also { it.setListeners(context as NoInternetConnectionCallback, name) }
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = "Absen"
        } else if (position == 1) {
            title = "Absen Staff"
        }
        return title
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}