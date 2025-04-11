package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.old.lowlevel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.fragment.old.lowlevel.AttendanceFixLowFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback

class ViewPagerFixLowLevelAdapter(fm: FragmentManager, type: Int, name: String, val context: Context) :
    FragmentPagerAdapter(fm) {
    private val type: Int = type
    val name: String = name

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        if (position == 0) {
            fragment = AttendanceFixLowFragment.newInstance(type)
                .also { it.setListener(context as NoInternetConnectionCallback) }
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
        }
        return title
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}