package com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
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
import com.hkapps.hygienekleen.databinding.ActivityDiversionManagementBinding
import com.hkapps.hygienekleen.databinding.BottomSheetAreaAssignmentBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.viewmodel.ClosingManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.ui.activity.PeriodicManagementDetailActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.area.AreaAssignment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.Diversion
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter.ClosingAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter.ListChooseFilterAreaAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.viewmodel.ClosingViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar

class DiversionManagementActivity : AppCompatActivity(),ListChooseFilterAreaAdapter.OnItemSelectedCallBack {

    private lateinit var binding : ActivityDiversionManagementBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var closingAdapter: ClosingAdapter

    private val dataArea = mutableListOf<AreaAssignment>(AreaAssignment(0,"","Semua area penugasan"))

    private val closingManagementViewModel by lazy {
        ViewModelProvider(this)[ClosingManagementViewModel::class.java]
    }

    private val closingViewModel by lazy {
        ViewModelProvider(this)[ClosingViewModel::class.java]
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setResult(RESULT_OK)
            finish()
        }
    }

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")

    private var isLastPage = false

    private var page = 0

    private var locationId = 0

    private var i : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiversionManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        i = intent.getBooleanExtra("isYesterday",false)

        initView()

    }

    private fun initView(){
        val appBarName = "Pengalihan Pekerjaan"
        binding.appBarDiversion.tvAppbarTitle.text = appBarName
        binding.appBarDiversion.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        binding.shimmerLayout.startShimmerAnimation()
        Handler().postDelayed({
            loadDataArea()
            loadData(0)
        },1500)

        if(intent.getBooleanExtra("isSuccess",false)){
            showDialog()
        }

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_DIVERTED,false)) {
                showDialog()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if(CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_ADD_IMAGE_SHIFT_CHECKLIST,true)){
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_ADD_IMAGE_SHIFT_CHECKLIST,false)
            recreate()
        }
    }

    private fun showRecycler(data : List<Diversion>){
        closingAdapter = ClosingAdapter(data.toMutableList(), object : ClosingAdapter.OnClickDiversion{
            override fun onClickDetail(data: Diversion) {
                val i =  Intent(this@DiversionManagementActivity, PeriodicManagementDetailActivity::class.java)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_JOB,data.idJob)
                resultLauncher.launch(i)
            }

            override fun onDetailDiversion(data: Diversion) {
                val i =  Intent(this@DiversionManagementActivity, FormDiversionManagementActivity::class.java)
                i.putExtra("idJob",data.idJob)
                i.putExtra("is_management",true)
                resultLauncher.launch(i)
            }

            override fun showToast() {
                binding.tvInfo.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.tvInfo.visibility = View.INVISIBLE
                }, 2000)

            }
        })
        val rvLayoutManager = LinearLayoutManager(this@DiversionManagementActivity)
        binding.rvDiversion.apply {
            adapter = closingAdapter
            layoutManager = rvLayoutManager
        }

        val scrollListener = object : EndlessScrollingRecyclerView(rvLayoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    closingManagementViewModel.listDiversion(locationId,projectId,if(i) getYesterdayDate() else getTwoYesterdayDate(),page,10)
                }
            }
        }
        binding.rvDiversion.addOnScrollListener(scrollListener)
    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun loadData(locationId : Int){
        closingManagementViewModel.listDiversion(locationId,projectId,if(i) getYesterdayDate() else getTwoYesterdayDate(),page,10)
        closingManagementViewModel.listDiversionModel.observe(this){
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    isLastPage = it.data.last
                    if(page == 0){
                        showRecycler(it.data.content)
                        val total = "Total : ${it.data.content.size} pekerjaan"
                        binding.tvTotalDiversion.text = total
                        binding.shimmerLayout.stopShimmerAnimation()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.rvDiversion.visibility = View.VISIBLE
                        binding.tvEmpty.visibility = View.GONE
                    }else{
                        closingAdapter.data.addAll(it.data.content)
                        closingAdapter.notifyItemRangeChanged(
                            closingAdapter.data.size - it.data.content.size,
                            closingAdapter.data.size
                        )
                    }

                }else{
                    val total = "Total : -"
                    binding.tvTotalDiversion.text = total
                    binding.shimmerLayout.stopShimmerAnimation()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvDiversion.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data pengalihan", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun showDialog(){
        loadData(0)
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.layout_dialog_success_diversion)
        val btnOke = dialog.findViewById<AppCompatButton>(R.id.btn_dismiss_diversion)
        btnOke.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun loadDataArea(){
        closingViewModel.getAreaAssignment(projectId,"All",0,10)
        closingViewModel.getListAreaAssignmentModel.observe(this){  areaAssignmentResponse ->
            if(areaAssignmentResponse.code == 200){
                dataArea.addAll(areaAssignmentResponse.data.content)
                binding.tvChooseFilter.setOnClickListener {
                    showBottomSheetFilter(dataArea)
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showBottomSheetFilter(data : List<AreaAssignment>){
        val bottomSheet = BottomSheetDialog(this)
        val view = BottomSheetAreaAssignmentBinding.inflate(layoutInflater)
        bottomSheet.apply {
            view.apply {
                setContentView(view.root)
                val layoutManager =
                    LinearLayoutManager(this@DiversionManagementActivity, LinearLayoutManager.VERTICAL, false)
                rvFilterAreaAssignment.layoutManager = layoutManager
                rvFilterAreaAssignment.adapter = ListChooseFilterAreaAdapter(data).also {
                    it.setListener(this@DiversionManagementActivity)
                }
            }
            show()
        }
    }

    override fun onItemSelected(areaAssignment: AreaAssignment) {
        loadData(areaAssignment.locationId)
        locationId = areaAssignment.locationId
        binding.tvChooseFilter.text = areaAssignment.locationName
    }

    private fun getTwoYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -2)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

}