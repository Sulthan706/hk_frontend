package com.hkapps.hygienekleen.features.features_management.myteam.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_management.myteam.ui.fragment.MyTeamManagementFragment
import com.hkapps.hygienekleen.features.features_management.myteam.ui.fragment.MyTeamOperationalFragment

class ViewPagerMyTeamManagementAdapter(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = MyTeamOperationalFragment()
            1 -> fragment = MyTeamManagementFragment()
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when(position) {
            0 -> title = "Operasional"
            1 -> title = "Manajemen"
        }
        return title
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}