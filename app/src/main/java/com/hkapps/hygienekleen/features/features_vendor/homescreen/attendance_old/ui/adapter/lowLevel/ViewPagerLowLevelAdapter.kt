package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.adapter.lowLevel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.fragment.AttendanceFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.fragment.AttendanceStaffFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback

class ViewPagerLowLevelAdapter(fm: FragmentManager, type: Int, name: String, val context: Context) :
    FragmentPagerAdapter(fm) {
    private val type: Int = type
    val name: String = name

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        if (position == 0) {
            fragment = AttendanceFragment.newInstance(type)
                .also { it.setListener(context as NoInternetConnectionCallback) }
        } else if (position == 1) {
            fragment = AttendanceStaffFragment.newInstance(type)
                .also { it.setListeners(context as NoInternetConnectionCallback, name) }
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return 1
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