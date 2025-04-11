package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListLaporanAuditBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listLaporanAudit.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.adapter.ListLaporanAuditAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.viewmodel.AuditViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.DefaultChooseInspeksiAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ListChooseInspeksiAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

class ListLaporanAuditActivity : AppCompatActivity(),
    ListChooseInspeksiAdapter.OnItemSelectedCallBack,
    ListLaporanAuditAdapter.LaporanAuditCallBack,
    DefaultChooseInspeksiAdapter.OnItemDefaultSelectedCallBack
{

    private lateinit var binding: ActivityListLaporanAuditBinding
    private lateinit var rvAdapter: ListLaporanAuditAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.AUDIT_CLICK_FROM, "")
    private var projectCode = ""
    private var periode = "Semua Periode"
    private var page = 0
    private var isLastPage = false
    private var positionPeriode = 0
    private var reloadedNeeded = true
    private var statusPeriode = ""

    companion object {
        private const val CREATE_CODE = 31
    }

    private val viewModel: AuditViewModel by lazy {
        ViewModelProviders.of(this).get(AuditViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListLaporanAuditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // validate project data
        when(clickFrom) {
            "listAudit" -> {
                projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_AUDIT, "")
                binding.tvProjectListLaporanAudit.text = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_AUDIT, "")
                binding.ivCreateListLaporanAudit.visibility = View.GONE
            }
            "mainInspeksi" -> {
                projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
                binding.tvProjectListLaporanAudit.text = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")
                binding.ivCreateListLaporanAudit.visibility = View.VISIBLE
            }
            else -> {
                projectCode = ""
                binding.tvProjectListLaporanAudit.text = ""
            }
        }

        // set app bar
        binding.appbarListLaporanAudit.tvAppbarTitle.text = "Audit"
        binding.appbarListLaporanAudit.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // set shimmer
        binding.shimmerListLaporanAudit.startShimmerAnimation()
        binding.shimmerListLaporanAudit.visibility = View.VISIBLE
        binding.rvListLaporanAudit.visibility = View.GONE
        binding.tvEmptyListLaporanAudit.visibility = View.GONE

        // set on click periode
        binding.tvPeriodeListLaporanAudit.setOnClickListener {
            bottomSheetChoosePeriode("text")
        }

        // set on click create form audit
        binding.ivCreateListLaporanAudit.setOnClickListener {
            bottomSheetChoosePeriode("image")
        }

        // set recyclerview
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListLaporanAudit.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvListLaporanAudit.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getListLaporanAudit(userId, projectCode, periode, page)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    binding.shimmerListLaporanAudit.stopShimmerAnimation()
                    binding.shimmerListLaporanAudit.visibility = View.GONE
                    binding.rvListLaporanAudit.visibility = View.VISIBLE
                }
            }
        }
        viewModel.listLaporanAuditResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.rvListLaporanAudit.adapter = null
                        binding.tvEmptyListLaporanAudit.visibility = View.VISIBLE
                    }, 1500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvEmptyListLaporanAudit.visibility = View.GONE
                        isLastPage = it.data.last
                        if (page == 0) {
                            rvAdapter = ListLaporanAuditAdapter(
                                this,
                                it.data.content as ArrayList<Content>
                            ).also { it1 -> it1.setListener(this) }
                            binding.rvListLaporanAudit.adapter = rvAdapter
                        } else {
                            rvAdapter.listAudit.addAll(it.data.content)
                            rvAdapter.notifyItemRangeChanged(
                                rvAdapter.listAudit.size - it.data.content.size,
                                rvAdapter.listAudit.size
                            )
                        }
                    }, 1500)
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data list audit", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.rvListLaporanAudit.adapter = null
                    binding.tvEmptyListLaporanAudit.visibility = View.VISIBLE
                }, 1500)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bottomSheetChoosePeriode(clickFrom: String) {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_default_choose)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomDefaultChoose)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitleBottomDefaultChoose)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfoBottomDefaultChoose)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvBottomDefaultChoose)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnAppliedBottomDefaultChoose)

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }

        tvTitle?.text = "Periode Audit"
        tvInfo?.text = "Pilih periode laporan"

        val listChooseInspeksi = ArrayList<String>()
        when (clickFrom) {
            "text" -> {
                listChooseInspeksi.add("Semua Periode")
                listChooseInspeksi.add("Rutin Bulanan")
                listChooseInspeksi.add("3/4 Bulan")
                listChooseInspeksi.add("6 Bulan")
                listChooseInspeksi.add("Tahunan")
                listChooseInspeksi.add("Lainnya")
                recyclerView?.adapter = DefaultChooseInspeksiAdapter(listChooseInspeksi, positionPeriode).also { it.setListener(this) }
            }
            "image" -> {
                listChooseInspeksi.add("Rutin Bulanan")
                listChooseInspeksi.add("3/4 Bulan")
                listChooseInspeksi.add("6 Bulan")
                listChooseInspeksi.add("Tahunan")
                listChooseInspeksi.add("Lainnya")
                recyclerView?.adapter = ListChooseInspeksiAdapter(listChooseInspeksi).also { it.setListener(this) }
            }
        }

        button?.setOnClickListener {
            when(clickFrom) {
                "text" -> {
                    page = 0
                    binding.tvPeriodeListLaporanAudit.text = periode
                    binding.tvPeriodeListLaporanAudit.setTextColor(resources.getColor(R.color.grey2_client))

                    binding.shimmerListLaporanAudit.startShimmerAnimation()
                    binding.shimmerListLaporanAudit.visibility = View.VISIBLE
                    binding.rvListLaporanAudit.adapter = null
                    binding.rvListLaporanAudit.visibility = View.GONE
                    binding.tvEmptyListLaporanAudit.visibility = View.GONE

                    dialog.dismiss()
                    Handler(Looper.getMainLooper()).postDelayed({
                        reloadedNeeded = true
                        onResume()
                    }, 1500)
                }
                "image" -> {
                    Log.d("ListLapAuditAct", "bottomSheetChoosePeriode: periode = $periode")
                    Log.d("ListLapAuditAct", "bottomSheetChoosePeriode: statusPeriode = $statusPeriode")
                    if (statusPeriode == "Available") {
                        dialog.dismiss()
                        binding.tvInfoListLaporanAudit.visibility = View.INVISIBLE
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.M_CLICK_AUDIT, "createAudit")
                        startActivityForResult(
                            Intent(
                                this,
                                FormAuditActivity::class.java
                            ), CREATE_CODE
                        )
                    } else {
                        dialog.dismiss()
                        binding.tvInfoListLaporanAudit.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.tvInfoListLaporanAudit.visibility = View.INVISIBLE
                            periode = "Semua Periode"
                        }, 1500)
                    }
                }
            }

        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                binding.tvPeriodeListLaporanAudit.text = "Semua Periode"
                periode = "Semua Periode"
                positionPeriode = 0
                this.reloadedNeeded = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (this.reloadedNeeded) {
            loadData()
            page = 0
            periode = "Semua Periode"
            isLastPage = false
            positionPeriode = 0
        }
        this.reloadedNeeded = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onItemSelected(item: String) {
        if (item != "Semua Periode") {
            viewModel.getCekStatusAudit(userId, projectCode, item)
            viewModel.cekStatusAuditResponse.observe(this) {
                statusPeriode = it.message
            }
        }
        periode = item
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PERIOD_AUDIT, item)
    }

    override fun onClickAudit(idReport: Int) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.M_CLICK_AUDIT, "detailAudit")
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_REPORT_AUDIT, idReport)
        startActivity(Intent(this, FormAuditActivity::class.java))
    }

    override fun onItemDefaultSelected(item: String, position: Int) {
        periode = item
        positionPeriode = position
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PERIOD_AUDIT, item)
    }
}