package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.mr


import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDashboardMractivityBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.MRDashboardData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.MRActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.MrAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.TableViewAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils


class DashboardMRActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDashboardMractivityBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private var page = 0
    private val size = 10
    private var isLastPage = false
    private var loadingDialog: Dialog? = null

    private val homeViewModel by viewModels<HomeViewModel>()
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE,"")

    private lateinit var tableViewAdapter: TableViewAdapter

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardMractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        next()
        showLoading(getString(R.string.loading_string_progress))
        previous()
        getData()
        setObserver()

    }

    private fun initView(){
        binding.apply {
            val appBar = "Material Request"
            appbarMr.tvAppbarTitle.text = appBar
            appbarMr.ivAppbarBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            onBackPressedDispatcher.addCallback(onBackPressedCallback)
            resultLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK || CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_DIVERTED,false)) {
                    getData()
                    dialogSuccess()
                }
            }
            appbarMr.llBtnUpload.setOnClickListener {
                val popupMenu: PopupMenu = PopupMenu(this@DashboardMRActivity,binding.appbarMr.llBtnUpload)
                popupMenu.menuInflater.inflate(R.menu.mr_add_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener({ menuItem ->
                    resultLauncher.launch(Intent(this@DashboardMRActivity, CreateMRActivity::class.java).also{
                        it.putExtra("name",menuItem.title)
                    })
                    true
                })
                popupMenu.show()
            }
        }
    }

    private fun getData(){
        homeViewModel.dashboardMR(projectId, page, size)
    }

    private fun setObserver(){
        homeViewModel.dashboardMR.observe(this) {
            if(it.code == 200){
                hideLoading()
                if(it.data.content.isNotEmpty()){
                    binding.tvEmptyData.visibility = View.GONE
                    binding.ivNextAbsenceReportManagement.visibility = View.VISIBLE
                    binding.ivPrevAbsenceReportManagement.visibility = View.VISIBLE
                    binding.tvPageHistoryClosing.visibility = View.VISIBLE
                    val pageStart = it.data.pageable.offset + 1
                    val pageEnd = it.data.pageable.offset + it.data.numberOfElements

                    val count = "Showing $pageStart-$pageEnd of ${it.data.size}"
                    binding.tvPageHistoryClosing.text = count
                    if (page == 0) {
                        tableViewAdapter = TableViewAdapter(it.data.content.toMutableList()){ month,year ->
                            startActivity(Intent(this, MRActivity::class.java).also{
                                it.putExtra("month",month)
                                it.putExtra("year",year)
                                it.putExtra("mr","mr")
                            })
                        }
                        binding.recyclerViewMovieList.adapter = tableViewAdapter
                        binding.recyclerViewMovieList.layoutManager = LinearLayoutManager(this)
                    } else {
                        tableViewAdapter.data.clear()
                        for (i in 0 until it.data.content.size) {
                            tableViewAdapter.data.add(it.data.content[i])
                        }
                        tableViewAdapter.notifyDataSetChanged()
                    }
                }else{
                    binding.tvEmptyData.visibility = View.VISIBLE
                    binding.ivNextAbsenceReportManagement.visibility = View.GONE
                    binding.ivPrevAbsenceReportManagement.visibility = View.GONE
                    binding.tvPageHistoryClosing.visibility = View.GONE
                }
            }else{
                hideLoading()
                Toast.makeText(this, "Failed get data MR", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun next(){
        binding.ivNextAbsenceReportManagement.setOnClickListener {
            loadingDialog = CommonUtils.showLoadingDialog(this, getString(R.string.loading_string_progress))
            Handler(Looper.getMainLooper()).postDelayed( {
                if (!isLastPage) {
                    page++
                    getData()
                } else {
                    Toast.makeText(this, "Last Page", Toast.LENGTH_SHORT).show()
                }
            }, 500)
        }
    }

    private fun previous(){
        binding.ivPrevAbsenceReportManagement.setOnClickListener {
            loadingDialog = CommonUtils.showLoadingDialog(this, getString(R.string.loading_string_progress))
            Handler(Looper.getMainLooper()).postDelayed( {
                if (page != 0 ) {
                    page--
                    getData()
                } else {
                    hideLoading()
                    Toast.makeText(this, "First Page", Toast.LENGTH_SHORT).show()
                }
            }, 500)
        }
    }

    private fun dialogSuccess(){
        val view = View.inflate(this, R.layout.dialog_success_resign, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)
        val tvTittle = dialog.findViewById<TextView>(R.id.tvSuccessRegisMekari)
        val tvBak = dialog.findViewById<TextView>(R.id.tvInfoBak)
        tvTittle.text = "Successfully Submit Request"
        tvBak.text = "This request will be processed by your Operation Admin.\n" +
                "Please wait."
        val btnBack = dialog.findViewById<MaterialButton>(R.id.btnBackBakVendor)
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }
}