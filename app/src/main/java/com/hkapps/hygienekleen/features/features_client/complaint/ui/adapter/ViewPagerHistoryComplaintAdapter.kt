package com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hkapps.hygienekleen.features.features_client.complaint.ui.fragment.*

class ViewPagerHistoryComplaintAdapter(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = AllDetailHistoryComplaintClientFragment()
            1 -> fragment = WaitingDetailHistoryComplaintClientFragment()
            2 -> fragment = OngoingDetailHistoryComplaintClientFragment()
            3 -> fragment = DoneDetailHistoryComplaintClientFragment()
            4 -> fragment = CloseDetailHistoryComplaintClientFragment()
        }
        return fragment!!
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

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}