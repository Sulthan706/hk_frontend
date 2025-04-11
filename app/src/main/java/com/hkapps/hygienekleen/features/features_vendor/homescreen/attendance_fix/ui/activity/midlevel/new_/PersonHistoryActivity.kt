package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityPersonHistoryBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_history_result.Attendance
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.fab.FabHistoryResultVerticalAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton

class PersonHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonHistoryBinding
    private val attedanceViewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }

    //pref
    private val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.EMPLOYEE_HISTORY_RESULT, 0)
    private val month =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.MONTH_RESULT, 0)
    private val year =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.YEAR_RESULT, 0)

    //val
    private lateinit var rvSkeleton: Skeleton
    private var loadingDialog: Dialog? = null
    lateinit var adapterVertical: FabHistoryResultVerticalAdapter
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPersonHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.layoutAppbarHistoryAttendancePerson.tvAppbarTitle.text = "Riwayat Absen Individual"

        binding.layoutAppbarHistoryAttendancePerson.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        rvSkeleton =
            binding.rvHistoryPersonResult.applySkeleton(com.hkapps.hygienekleen.R.layout.item_shimmer)
        rvSkeleton.showSkeleton()
        showLoading(getString(com.hkapps.hygienekleen.R.string.loading_string))
        //oncreate
        setObserver()
        loadData()
    }


    private fun setObserver() {
        attedanceViewModel.getHistoryResultPerson().observe(this, Observer {
            if (it.code == 200) {
                if (it.data.listAttendance.isEmpty()) {
                    val view = View.inflate(this, R.layout.dialog_custom_fab_list_empty, null)
                    val builder = AlertDialog.Builder(this)
                    builder.setView(view)
                    val dialog = builder.create()
                    dialog.show()
                    dialog.setCancelable(false)
                    val btnBack = dialog.findViewById<Button>(R.id.btnBack)
                    btnBack.setOnClickListener {
                        onBackPressed()
                    }
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


                }
                binding.tvEmployeeNameHistoryP.text = it.data.employeeName
                binding.tvMonthPersonResult.text = it.data.date
                binding.tvJobNucEmployeeHistoryP.text =
                    "${it.data.employeeCode} | ${it.data.jobCode}"

                binding.tvHadirCountResult.text = if (it.data.hadirCount == null) {
                    "-"
                } else {
                    it.data.hadirCount.toString()
                }
                binding.tvLiburCountResult.text = if (it.data.liburCount == null) {
                    "-"
                } else {
                    it.data.liburCount.toString()
                }
                binding.tvAlpaCountResult.text = if (it.data.alphaCount == null) {
                    "-"
                } else {
                    it.data.alphaCount.toString()
                }
                binding.tvLupaAbsenCountResult.text = if (it.data.lupaAbsenCount == null) {
                    "-"
                } else {
                    it.data.lupaAbsenCount.toString()
                }
                binding.tvIzinCountResult.text = if (it.data.izinCount == null) {
                    "-"
                } else {
                    it.data.izinCount.toString()
                }
                binding.tvLemburGantiCountResult.text = if (it.data.lemburGantiCount == null) {
                    "-"
                } else {
                    it.data.lemburGantiCount.toString()
                }


                //adaper
                layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.rvHistoryPersonResult.layoutManager = layoutManager

                adapterVertical =
                    FabHistoryResultVerticalAdapter(it.data.listAttendance as ArrayList<Attendance>)
                binding.rvHistoryPersonResult.adapter = adapterVertical
            } else {
//                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                val view = View.inflate(this, R.layout.dialog_custom_fab_list_empty, null)
                val builder = AlertDialog.Builder(this)
                builder.setView(view)
                val dialog = builder.create()
                dialog.show()
                dialog.setCancelable(false)
                val btnBack = dialog.findViewById<Button>(R.id.btnBack)
                btnBack.setOnClickListener {
                    onBackPressed()
                }
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            }
            hideLoading()
        })
    }

    private fun loadData() {
        attedanceViewModel.getHistoryResult(employeeId, month, year)
    }

    //loading state
    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
}