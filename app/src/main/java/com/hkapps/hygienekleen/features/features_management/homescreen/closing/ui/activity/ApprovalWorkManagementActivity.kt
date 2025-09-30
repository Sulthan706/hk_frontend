package com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.activity

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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityApprovalWorkManagementBinding
import com.hkapps.hygienekleen.databinding.BottomSheetApprovalWorkBinding
import com.hkapps.hygienekleen.databinding.BottomSheetAreaAssignmentBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.adapter.ApprovalWorkManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicbystatusmanagement.ContentListByStatsPeriodic
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.viewmodel.PeriodicManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.area.AreaAssignment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter.ListChooseFilterAreaAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.viewmodel.ClosingViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity.PeriodicVendorDetailActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar

class ApprovalWorkManagementActivity : AppCompatActivity(),ListChooseFilterAreaAdapter.OnItemSelectedCallBack {

    private lateinit var binding : ActivityApprovalWorkManagementBinding

    private lateinit var approvalWorkAdapter : ApprovalWorkManagementAdapter

    private val monthlyWorkViewModel by lazy {
        ViewModelProvider(this)[PeriodicManagementViewModel::class.java]
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setResult(RESULT_OK)
            finish()
        }
    }

    private val dataArea = mutableListOf<AreaAssignment>(AreaAssignment(0,"","Semua area penugasan"))

    private val closingViewModel by lazy {
        ViewModelProvider(this)[ClosingViewModel::class.java]
    }

    private var flag = 0

    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")

    private var startDate: String = ""

    private var locationId = 0

    private var isLastPage = false

    private var page = 0

    private var i = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApprovalWorkManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        i = intent.getBooleanExtra("isYesterday",false)
        initView(i)
    }

    private fun initView(isYesterday : Boolean){
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
            loadData(isYesterday)
            setObserver()
        },1500)

    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun loadDataArea(){
        closingViewModel.getAreaAssignment(projectCode,"All",0,10)
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

    private fun showRecyclerView(data : List<ContentListByStatsPeriodic>){
        approvalWorkAdapter = ApprovalWorkManagementAdapter(data.toMutableList(),object : ApprovalWorkManagementAdapter.OnClickApprovalWork{
            override fun OnClickDetail(idJob : Int) {
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_JOB, idJob)
                startActivity(Intent(this@ApprovalWorkManagementActivity, PeriodicVendorDetailActivity::class.java))
            }

            override fun onClickApprove(content : ContentListByStatsPeriodic) {
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
                    loadData(i)
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

    private fun loadData(isYesterday: Boolean) {
        monthlyWorkViewModel.getByStatusPeriodicManagement(
            projectCode,
            if(isYesterday) getYesterdayDate() else getTwoYesterdayDate() ,
            if(isYesterday) getYesterdayDate() else getTwoYesterdayDate(),
            "notApproved",
            page,
            10,
            locationId
        )

    }

    private fun setObserver() {
        monthlyWorkViewModel.getByStatusPeriodicManagementModel.observe(this) {
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

    private fun showBottomSheetFilter(content : ContentListByStatsPeriodic){
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
                    monthlyWorkViewModel.putApproveJobManagements(content.idJob,userId)
                    setObserverApproval()
                    bottomSheet.dismiss()
                }
            }
            show()
        }
    }

    private fun setObserverApproval(){
        monthlyWorkViewModel.putApproveJobsManagementModel.observe(this) {
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
                    LinearLayoutManager(this@ApprovalWorkManagementActivity, LinearLayoutManager.VERTICAL, false)
                rvFilterAreaAssignment.layoutManager = layoutManager
                rvFilterAreaAssignment.adapter = ListChooseFilterAreaAdapter(data).also {
                    it.setListener(this@ApprovalWorkManagementActivity)
                }
            }
            show()
        }
    }

    override fun onItemSelected(areaAssignment: AreaAssignment) {
        this.locationId = areaAssignment.locationId
        loadData(i)
        binding.tvChooseFilter.text = areaAssignment.locationName
    }

    private fun getTwoYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -2)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }


}