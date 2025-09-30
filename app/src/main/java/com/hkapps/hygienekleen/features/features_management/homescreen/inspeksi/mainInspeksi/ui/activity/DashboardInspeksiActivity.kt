package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDashboardInspeksiBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ListChooseInspeksiAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.activity.FormLaporanMeetingActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.activity.ListMeetingInspeksiActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class DashboardInspeksiActivity : AppCompatActivity(), ListChooseInspeksiAdapter.OnItemSelectedCallBack {

    private lateinit var binding: ActivityDashboardInspeksiBinding
    private val projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
    private var selectedInspeksi = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardInspeksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,null)

        // set on click button back
        binding.ivBackDashboardInspeksi.setOnClickListener {
            onBackPressed()
        }

        // set project name
        binding.tvProjectDashboardInspeksi.text = if (projectName == "") {
            "-"
        } else {
            projectName
        }

        binding.tvKontrolAreaDashboardInspeksi.setOnClickListener {
            startActivity(Intent(this, ListKontrolAreaActivity::class.java))
        }

        binding.tvMeetingDashboardInspeksi.setOnClickListener {
            startActivity(Intent(this, ListMeetingInspeksiActivity::class.java))
        }

        binding.ivCreateDashboardInspeksi.setOnClickListener {
            bottomSheetChooseInspeksi()
        }
    }

    private fun bottomSheetChooseInspeksi() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_choose_inspeksi)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomChooseInspeksi)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvBottomChooseInspeksi)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnAppliedBottomChooseInspeksi)

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager

        ivClose?.setOnClickListener {
            selectedInspeksi = ""
            dialog.dismiss()
        }

        button?.setOnClickListener {
            if (selectedInspeksi == "") {
                Toast.makeText(this, "Silahkan pilih posisi", Toast.LENGTH_SHORT).show()
            } else {
                when (selectedInspeksi) {
                    "Kontrol Area" -> {
                        dialog.dismiss()
                        startActivity(Intent(this, FormKontrolAreaActivity::class.java))
                    }
                    "Meeting" -> {
                        dialog.dismiss()
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "dashboardMeeting")
                        startActivity(Intent(this, FormLaporanMeetingActivity::class.java))
                    }
                }
            }
        }

        val listChooseInspeksi = ArrayList<String>()
        listChooseInspeksi.add("Kontrol Area")
        listChooseInspeksi.add("Meeting")
        recyclerView?.adapter = ListChooseInspeksiAdapter(listChooseInspeksi).also { it.setListener(this) }

        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
        finish()
    }

    override fun onItemSelected(item: String) {
        selectedInspeksi = item
    }
}