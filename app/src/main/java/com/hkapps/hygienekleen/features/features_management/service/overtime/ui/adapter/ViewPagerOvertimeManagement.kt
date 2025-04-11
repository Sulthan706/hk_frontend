package com.hkapps.hygienekleen.features.features_management.service.overtime.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.fragment.OvertimeChangeManagementFragment
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.fragment.OvertimeRequestManagementFragment

class ViewPagerOvertimeManagement(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = OvertimeChangeManagementFragment()
            1 -> fragment = OvertimeRequestManagementFragment()
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when(position) {
            0 -> title = "Lembur ganti"
            1 -> title = "Lembur tagih"
        }
        return title
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}