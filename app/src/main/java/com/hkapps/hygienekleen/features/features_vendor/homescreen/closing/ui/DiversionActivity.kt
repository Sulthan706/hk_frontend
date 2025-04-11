package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui

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
import com.hkapps.hygienekleen.databinding.ActivityDiversionBinding
import com.hkapps.hygienekleen.databinding.BottomSheetAreaAssignmentBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.area.AreaAssignment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.Diversion
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter.ClosingAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter.ListChooseFilterAreaAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.viewmodel.ClosingViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity.PeriodicVendorDetailActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar

class DiversionActivity : AppCompatActivity(),ListChooseFilterAreaAdapter.OnItemSelectedCallBack {

    private lateinit var binding : ActivityDiversionBinding

    private lateinit var closingAdapter: ClosingAdapter

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val idDetailEmployeeProject  = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_DETAIL_EMPLOYEE_PROJECT,0)

    private val dataArea = mutableListOf<AreaAssignment>(AreaAssignment(0,"","Semua area penugasan"))

    private val closingViewModel by lazy {
        ViewModelProvider(this)[ClosingViewModel::class.java]
    }

    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.JOB_LEVEL,"")

    private var bottomSheet : BottomSheetDialog? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(intent.getBooleanExtra("isSuccess",false)){
                startActivity(Intent(this@DiversionActivity,PeriodicVendorDetailActivity::class.java))
                finish()
            }else{
                setResult(RESULT_OK)
                finish()
            }

        }
    }

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private var isLastPage = false

    private var page = 0

    private var locationId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiversionBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
       if(jobLevel.contains("chief",ignoreCase = true) || jobLevel.contains("supervisor",ignoreCase = true)){
           Handler().postDelayed({
               loadDataChief(0)
               loadDataArea()
           },1500)
       }else{
           Handler().postDelayed({
               loadData(0)
               loadDataArea()
           },1500)
       }

        if(intent.getBooleanExtra("isSuccess",false)){
            showDialog()
        }

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_DIVERTED,false)) {
                recreate()
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

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun loadDataChief(locationId: Int){
        closingViewModel.getDiversionChief(locationId,projectId,getYesterdayDate(),page,10)
        closingViewModel.listDiversionChief.observe(this){
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    isLastPage = it.data.last
                    if(page == 0){
                        rvDummyDiversion(it.data.content)
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

    private fun loadData(locationId : Int){
        closingViewModel.getListDailyDiversion(CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_DETAIL_EMPLOYEE_PROJECT_VALUE,0),locationId,page,10)
        closingViewModel.getListDiversionModel.observe(this){
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    isLastPage = it.data.last
                    if(page == 0){
                        rvDummyDiversion(it.data.content)
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

    private fun loadDataArea(){
        val category = if(jobLevel.contains("leader",ignoreCase = true)) "Team Leader" else "All"
        closingViewModel.getAreaAssignment(projectId,category,0,10)
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

    private fun rvDummyDiversion(data : List<Diversion>){
        closingAdapter = ClosingAdapter(data.toMutableList(), object : ClosingAdapter.OnClickDiversion{
            override fun onClickDetail(data: Diversion) {
                val i =  Intent(this@DiversionActivity,PeriodicVendorDetailActivity::class.java)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_JOB,data.idJob)
                resultLauncher.launch(i)
            }

            override fun onDetailDiversion(data: Diversion) {
                val i =  Intent(this@DiversionActivity,FormDiversionActivity::class.java)
                i.putExtra("idJob",data.idJob)
                resultLauncher.launch(i)
            }

            override fun showToast() {
                binding.tvInfo.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.tvInfo.visibility = View.INVISIBLE
                }, 2000)

            }
        })
        val rvLayoutManager = LinearLayoutManager(this@DiversionActivity)
        binding.rvDiversion.apply {
            adapter = closingAdapter
            layoutManager = rvLayoutManager
        }

        val scrollListener = object : EndlessScrollingRecyclerView(rvLayoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    if(jobLevel.contains("chief",ignoreCase = true)){
                        closingViewModel.getDiversionChief(locationId,projectId,getYesterdayDate(),page,10)
                    }else{
                        closingViewModel.getListDailyDiversion(idDetailEmployeeProject,locationId,page,10)
                    }

                }
            }
        }
        binding.rvDiversion.addOnScrollListener(scrollListener)
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

    private fun showBottomSheetFilter(data : List<AreaAssignment>){
        bottomSheet = BottomSheetDialog(this)
        val view = BottomSheetAreaAssignmentBinding.inflate(layoutInflater)
        bottomSheet?.apply {
            view.apply {
                setContentView(view.root)
                val layoutManager =
                    LinearLayoutManager(this@DiversionActivity, LinearLayoutManager.VERTICAL, false)
                rvFilterAreaAssignment.layoutManager = layoutManager
                rvFilterAreaAssignment.adapter = ListChooseFilterAreaAdapter(data).also {
                    it.setListener(this@DiversionActivity)
                }
            }
            show()
        }
    }

    override fun onItemSelected(areaAssignment: AreaAssignment) {
        bottomSheet?.dismiss()
        if(jobLevel.contains("chief",ignoreCase = true)){
            loadDataChief(areaAssignment.locationId)
        }else{
            loadData(areaAssignment.locationId)
        }

        locationId = areaAssignment.locationId
        binding.tvChooseFilter.text = areaAssignment.locationName
    }
}