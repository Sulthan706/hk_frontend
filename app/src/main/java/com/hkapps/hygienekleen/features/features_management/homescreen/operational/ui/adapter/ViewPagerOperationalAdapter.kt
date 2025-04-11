package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment.ListAllManagementFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment.ListAllOperationalFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment.newoperational.BranchOperationalEmployeeFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment.newoperational.BranchOperationalManagementFragment

class ViewPagerOperationalAdapter(fm: FragmentManager, val context: Context,val  userLevel : String): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment{
        var fragment: Fragment? = null
        when(position) {
            0 -> {
                fragment = if(userLevel == "BOD" || userLevel == "CEO"){
                    BranchOperationalEmployeeFragment()
                }else{
                    ListAllOperationalFragment()
                }
            }
            1 -> {
                fragment = if(userLevel == "BOD" || userLevel == "CEO"){
                    BranchOperationalManagementFragment()
                }else{
                    ListAllManagementFragment()
                }

            }

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
        return FragmentPagerAdapter.POSITION_NONE
    }
}