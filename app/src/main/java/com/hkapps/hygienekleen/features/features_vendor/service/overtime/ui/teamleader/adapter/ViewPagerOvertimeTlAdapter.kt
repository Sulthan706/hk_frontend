package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.fragment.OvertimeChangeTlFragment
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.fragment.OvertimeRequestTlFragment

class ViewPagerOvertimeTlAdapter(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {var fragment: Fragment? = null
        when(position) {
            0 -> fragment = OvertimeChangeTlFragment()
            1 -> fragment = OvertimeRequestTlFragment()
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when(position) {
            0 -> title = "Lembur Ganti"
            1 -> title = "Lembur Tagih"
        }
        return title
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}