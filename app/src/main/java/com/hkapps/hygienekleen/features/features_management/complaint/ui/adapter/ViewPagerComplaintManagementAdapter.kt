package com.hkapps.hygienekleen.features.features_management.complaint.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hkapps.hygienekleen.features.features_management.complaint.ui.fragment.*

class ViewPagerComplaintManagementAdapter(manager: FragmentManager, private val context: Context) :
    FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList: MutableList<Fragment> = ArrayList()
    init {
        // Add your fragments to the list
        fragmentList.add(AllComplaintManagementFragment())
        fragmentList.add(WaitingComplaintManagementFragment())
        fragmentList.add(OngoingComplaintManagementFragment())
        fragmentList.add(DoneComplaintManagementFragment())
        fragmentList.add(CloseComplaintManagementFragment())
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when(position) {
            0 -> title = "Semua"
            1 -> title = "Menunggu"
            2 -> title = "Dikerjakan"
            3 -> title = "Selesai"
            4 -> title = "Tutup"
        }
        return title
    }

}