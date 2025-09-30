package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityApprovalWorkClosingBinding
import com.hkapps.hygienekleen.databinding.BottomSheetApprovalWorkBinding
import com.hkapps.hygienekleen.databinding.BottomSheetAreaAssignmentBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.area.AreaAssignment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter.ApprovalWorkAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter.ListChooseFilterAreaAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.viewmodel.ClosingViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbystatus.ContentListStatusRkb
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity.PeriodicVendorDetailActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar

class ApprovalWorkClosingActivity : AppCompatActivity(),ListChooseFilterAreaAdapter.OnItemSelectedCallBack {

    private lateinit var binding : ActivityApprovalWorkClosingBinding

    private lateinit var approvalWorkAdapter : ApprovalWorkAdapter

    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.JOB_LEVEL,"")

    private val monthlyWorkViewModel by lazy {
        ViewModelProvider(this).get(MonthlyWorkReportViewModel::class.java)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setResult(RESULT_OK)
            finish()
        }
    }

    private val closingViewModel by lazy {
        ViewModelProvider(this)[ClosingViewModel::class.java]
    }

    private val dataArea = mutableListOf<AreaAssignment>(AreaAssignment(0,"","Semua area penugasan"))

    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private var startDate: String = ""

    private var locationId = 0

    private var isLastPage = false

    private var page = 0

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var flag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApprovalWorkClosingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
               initView()
            }
        }
        initView()

    }

    private fun initView(){
        val appBarName = "Approval Pekerjaan"
        binding.appBarApprovalWorkClosing.tvAppbarTitle.text = appBarName
        binding.appBarApprovalWorkClosing.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        binding.shimmerLayout.startShimmerAnimation()
        getDateNowApi()
        Handler().postDelayed({
            loadDataArea()
           if(jobLevel.contains("chief",ignoreCase = true) || jobLevel.contains("supervisor",ignoreCase = true)){
               loadData(true)
           }else{
               loadData(false)
           }
            setObserver()
        },1500)

    }

    private fun loadDataArea(){
        closingViewModel.getAreaAssignment(projectId,"All",0,10)
        closingViewModel.getListAreaAssignmentModel.observe(this){  areaAssignmentResponse ->
            if(areaAssignmentResponse.code == 200){
                dataArea.addAll(areaAssignmentResponse.data.content)
                binding.tvChooseFilter.setOnClickListener {
                    showBottomSheetFilterByArea(dataArea)
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRecyclerView(data : List<ContentListStatusRkb>){
        approvalWorkAdapter = ApprovalWorkAdapter(data.toMutableList(),object : ApprovalWorkAdapter.OnClickApprovalWork{
            override fun OnClickDetail(idJob : Int) {
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_JOB, idJob)
                val i = Intent(this@ApprovalWorkClosingActivity, PeriodicVendorDetailActivity::class.java)
                i.putExtra("from_closing",true)
                resultLauncher.launch(i)
            }

            override fun onClickApprove(content : ContentListStatusRkb) {
                showBottomSheetFilter(content)
            }
        })

        val rvLayoutManager = LinearLayoutManager(this)
        binding.rvApprovalWorkClosing.apply {
            adapter = approvalWorkAdapter
            layoutManager = rvLayoutManager
        }

        val scrollListener = object : EndlessScrollingRecyclerView(rvLayoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    if(jobLevel.contains("chief",ignoreCase = true) || jobLevel.contains("supervisor",ignoreCase = true)){
                        loadData(true)
                    }else{
                        loadData(false)
                    }
                }
            }
        }
        binding.rvApprovalWorkClosing.addOnScrollListener(scrollListener)
    }

    private fun getDateNowApi(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        startDate = formatDateNowApi(year, month, day)
        return formatDateNowApi(year, month, day)
    }
    private fun formatDateNowApi(year: Int, month: Int, day: Int): String {
        return "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun loadData(isChief : Boolean) {
        if(isChief){
            monthlyWorkViewModel.getListStatusMonthRkb(
                userId,
                projectCode,
                getYesterdayDate(),
                getYesterdayDate(),
                "notApproved",
                page,
                10,
                locationId
            )
        }else{
            monthlyWorkViewModel.getListStatusMonthRkb(
                userId,
                projectCode,
                startDate,
                startDate,
                "notApproved",
                page,
                10,
                locationId
            )
        }
    }

    private fun setObserver() {
        monthlyWorkViewModel.getListStatusMonthRkbViewModel().observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    isLastPage = false
                    if (page == 0) {
                        showRecyclerView(it.data.content)
                        val total = "Total : ${it.data.content.size} pekerjaan"
                        binding.tvTotalDiversion.text = total
                        binding.shimmerLayout.stopShimmerAnimation()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.rvApprovalWorkClosing.visibility = View.VISIBLE
                        binding.tvEmpty.visibility = View.GONE
                    }else{
                        approvalWorkAdapter.data.addAll(it.data.content)
                        approvalWorkAdapter.notifyItemRangeChanged(
                            approvalWorkAdapter.data.size - it.data.content.size,
                            approvalWorkAdapter.data.size
                        )
                    }

                } else {
                    val total = "Total : -"
                    binding.tvTotalDiversion.text = total
                    binding.shimmerLayout.stopShimmerAnimation()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvApprovalWorkClosing.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showBottomSheetFilter(content : ContentListStatusRkb){
        val bottomSheet = BottomSheetDialog(this)
        val view = BottomSheetApprovalWorkBinding.inflate(layoutInflater)
        bottomSheet.apply {
            view.apply {
                setContentView(view.root)
                tvDateWork.text = content.tanggalItem
                tvWork.text = content.detailJob
                tvArea.text = content.locationName
                tvShift.text = content.shift
                btnSubmit.setOnClickListener {
                    monthlyWorkViewModel.putApproveJob(content.idJob,userId)
                    setObserverApproval()
                    bottomSheet.dismiss()
                }
            }
            show()
        }
    }

    private fun setObserverApproval(){
        monthlyWorkViewModel.putApproveJobViewModel().observe(this) {
            if (it.code == 200) {
                if(flag == 0){
                    showDialog()
                }
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialog(){
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.layout_dialog_success_diversion)
        val btnOke = dialog.findViewById<AppCompatButton>(R.id.btn_dismiss_diversion)
        val tvDesc = dialog.findViewById<TextView>(R.id.tv_desc_finish)
        tvDesc.text = "Pekerjaan sudah di-approve"
        btnOke.setOnClickListener {
            dialog.dismiss()
            flag = 1
            recreate()
        }
        dialog.show()
    }

    private fun showBottomSheetFilterByArea(data : List<AreaAssignment>){
        val bottomSheet = BottomSheetDialog(this)
        val view = BottomSheetAreaAssignmentBinding.inflate(layoutInflater)
        bottomSheet.apply {
            view.apply {
                setContentView(view.root)
                val layoutManager =
                    LinearLayoutManager(this@ApprovalWorkClosingActivity, LinearLayoutManager.VERTICAL, false)
                rvFilterAreaAssignment.layoutManager = layoutManager
                rvFilterAreaAssignment.adapter = ListChooseFilterAreaAdapter(data).also {
                    it.setListener(this@ApprovalWorkClosingActivity)
                }
            }
            show()
        }
    }

    override fun onItemSelected(areaAssignment: AreaAssignment) {
        this.locationId = areaAssignment.locationId
        loadData(true)
        binding.tvChooseFilter.text = areaAssignment.locationName
    }
}