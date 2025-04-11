package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityDetailsKondisiAreaBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.viewmodel.AuditViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listLaporanKondisiArea.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ListLaporanAreaAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class DetailsKondisiAreaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsKondisiAreaBinding
    private lateinit var rvAdapter: ListLaporanAreaAdapter
    private val idAuditKualitas = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_AUDIT_KUALITAS, 0)
    private var page = 0
    private var isLastPage = false

    private val viewModel: AuditViewModel by lazy {
        ViewModelProviders.of(this).get(AuditViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsKondisiAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarDetailsKondisiArea.tvAppbarTitle.text = "Laporan Kondisi Area"
        binding.appbarDetailsKondisiArea.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // set rv layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDetailsKondisiArea.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvDetailsKondisiArea.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getListReportHasilKerja(idAuditKualitas, page)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.listReportHasilKerjaResponse.observe(this) {
            if (it.code == 200) {
                isLastPage = it.data.last
                if (page == 0) {
                    rvAdapter = ListLaporanAreaAdapter(
                        this,
                        it.data.content as ArrayList<Content>,
                        "detailKondisiArea",
                        viewModel,
                        this
                    )
                    binding.rvDetailsKondisiArea.adapter = rvAdapter
                } else {
                    rvAdapter.listLaporanArea.addAll(it.data.content)
                    rvAdapter.notifyItemRangeChanged(
                        rvAdapter.listLaporanArea.size - it.data.content.size,
                        rvAdapter.listLaporanArea.size
                    )
                }
            } else {
                binding.rvDetailsKondisiArea.adapter = null
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}