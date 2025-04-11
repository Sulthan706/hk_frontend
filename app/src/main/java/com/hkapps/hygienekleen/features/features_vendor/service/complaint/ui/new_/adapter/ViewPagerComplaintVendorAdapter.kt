package com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.new_.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.new_.fragment.*

class ViewPagerComplaintVendorAdapter(manager: FragmentManager, private val context: Context) :
    FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList: MutableList<Fragment> = ArrayList()

    init {
        // Add your fragments to the list
        fragmentList.add(AllComplaintVendorFragment())
        fragmentList.add(WaitingComplaintVendorFragment())
        fragmentList.add(OngoingComplaintVendorFragment())
        fragmentList.add(DoneComplaintVendorFragment())
        fragmentList.add(CloseComplaintVendorFragment())
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

//    override fun getItemPosition(`object`: Any): Int {
//        return POSITION_NONE
//    }

//    override fun notifyDataSetChanged() {
//        super.notifyDataSetChanged()
//        for (i in 0 until count) {
//            val fragment = getItem(i)
//            if (fragment is AllComplaintVendorFragment) {
//                fragment.refreshData()
//            }
//        }
//    }

}