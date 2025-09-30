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
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityApproveMractivityBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.MRActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.mr.DashboardMRActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.MrAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.TableViewAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.util.Calendar
import kotlin.getValue

class ApproveMRActivity : AppCompatActivity() {
    private lateinit var binding : ActivityApproveMractivityBinding

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
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID,0)

    private lateinit var tableViewAdapter: MrAdapter

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApproveMractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root, binding.statusBarBackground)
        initView()
        next()
        showLoading(getString(R.string.loading_string_progress))
        previous()
        getData()
        setObserver()
    }

    private fun initView(){
        binding.apply {
            val appBar = "Checklist MR"
            appBarMr.tvAppbarTitle.text = appBar
            appBarMr.ivAppbarBack.setOnClickListener {
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
        }
    }

    private fun getData(){
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        if(intent.getStringExtra("mr") != null){
            homeViewModel.getDataMr(CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE,""),currentMonth,currentYear,page,size)
        }else{
            homeViewModel.getDataMr(CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE,""),currentMonth,currentYear,page,size)
        }
    }

    private fun setObserver(){
        homeViewModel.getDataMr.observe(this) {
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
                        tableViewAdapter = MrAdapter(it.data.content.toMutableList(),false,true){ id ->
                            approveMr(id)
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

    private fun approveMr(idMaterial : Int,){
        showLoading("Please wait")
        homeViewModel.approveMr(userId,idMaterial)
        homeViewModel.approveMR.observe(this) {
            if(it.code == 200){
                hideLoading()
                dialogSuccess()
            }else{
                hideLoading()
                Toast.makeText(this, "Failed approve MR", Toast.LENGTH_SHORT).show()
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