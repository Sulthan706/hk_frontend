package com.hkapps.hygienekleen.features.features_client.report.ui.new_.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityKondisiAreaBinding
import com.hkapps.hygienekleen.features.features_client.report.model.listkondisiarea.ContentKondisiArea
import com.hkapps.hygienekleen.features.features_client.report.ui.new_.adapter.ListKondisiAreaAdapter
import com.hkapps.hygienekleen.features.features_client.report.viewmodel.ReportClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class KondisiAreaActivity : AppCompatActivity(), ListKondisiAreaAdapter.ListKondisiAreaCallBack {
    private lateinit var binding: ActivityKondisiAreaBinding
    private val viewModel: ReportClientViewModel by lazy {
        ViewModelProviders.of(this).get(ReportClientViewModel::class.java)
    }

    //pref
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")

    //adapter
    private lateinit var adapterKondisiArea: ListKondisiAreaAdapter

    //val
    var page: Int = 0
    private var isLastPage = false
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityKondisiAreaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        binding.appbarKondisiAreaReport.tvAppbarTitle.text = "Kondisi Area"
        binding.appbarKondisiAreaReport.ivAppbarBack.setOnClickListener {
            onBackPressed()
            finish()
        }
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListKondisiArea.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.rvListKondisiArea.addOnScrollListener(scrollListener)
        loadData()
        setObserver()
        showLoading("Loading..")
//oncreate
    }

    //fun
    private fun loadData() {
        viewModel.getListKondisiArea(projectCode, page)
    }

    private fun setObserver() {
        viewModel.getListKondisiAreaViewModel().observe(this) {
            if (it.code == 200) {
                isLastPage = it.data.last
                if (page == 0) {
                    adapterKondisiArea = ListKondisiAreaAdapter(
                        this, it.data.content as ArrayList<ContentKondisiArea>
                    ).also { it.setListener(this) }
                    binding.rvListKondisiArea.adapter = adapterKondisiArea
                } else {
                    adapterKondisiArea.listKondisiArea.addAll(it.data.content as ArrayList<ContentKondisiArea>)
                    adapterKondisiArea.notifyItemRangeChanged(
                        adapterKondisiArea.listKondisiArea.size - it.data.size,
                        adapterKondisiArea.listKondisiArea.size
                    )
                }

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }

    override fun onClickNotification(locationId: Int, locationName: String) {
        val i = Intent(this, DetailKondisiAreaActivity::class.java)
        startActivity(i)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.LOCATION_ID, locationId)
    }
    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }


}