package com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter.visitor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hkapps.hygienekleen.features.features_client.complaint.ui.fragment.visitor.AllComplaintVisitorClientFragment
import com.hkapps.hygienekleen.features.features_client.complaint.ui.fragment.visitor.CloseComplaintVisitorClientFragment
import com.hkapps.hygienekleen.features.features_client.complaint.ui.fragment.visitor.DoneComplaintVisitorClientFragment
import com.hkapps.hygienekleen.features.features_client.complaint.ui.fragment.visitor.OngoingComplaintVisitorClientFragment
import com.hkapps.hygienekleen.features.features_client.complaint.ui.fragment.visitor.WaitingComplaintVisitorClientFragment

class VpComplaintVisitorClientAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllComplaintVisitorClientFragment()
            1 -> WaitingComplaintVisitorClientFragment()
            2 -> OngoingComplaintVisitorClientFragment()
            3 -> DoneComplaintVisitorClientFragment()
            4 -> CloseComplaintVisitorClientFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}