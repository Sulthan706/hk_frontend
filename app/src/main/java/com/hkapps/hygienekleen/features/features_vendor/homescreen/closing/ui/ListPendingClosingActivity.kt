package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityListPendingClosingBinding
import com.hkapps.hygienekleen.databinding.BottomSheetFilterClosingBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ListChooseInspeksiAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ClosingModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter.ClosingPendingAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.viewmodel.ClosingViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ListPendingClosingActivity : AppCompatActivity(),ListChooseInspeksiAdapter.OnItemSelectedCallBack {

    private lateinit var binding  : ActivityListPendingClosingBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }


    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val jobLevel =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.JOB_LEVEL, "")

    private lateinit var closingPendingAdapter : ClosingPendingAdapter

    private val closingViewModel by lazy {
        ViewModelProvider(this)[ClosingViewModel::class.java]
    }

    private var page = 0

    private var isLastPage = false

    private var filter = ""

    private var isChief = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPendingClosingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        val appBarName = "Closing Pengawas"
        binding.appBarClosingPending.tvAppbarTitle.text = appBarName
        binding.appBarClosingPending.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        binding.tvChooseFilter.setOnClickListener {
            showBottomSheetFilter()
        }
        isChief = intent.getBooleanExtra("chief",false)
        binding.shimmerLayout.startShimmerAnimation()
        Handler().postDelayed({
            getData()
        },1500)
    }

    private fun getData(){
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        val currentDate = Date()
        val date = dateFormat.format(currentDate)
        if(jobLevel.contains("chief",ignoreCase = true) || jobLevel.contains("supervisor",ignoreCase = true)){
            if(isChief){
                closingViewModel.listClosing(projectId,getYesterdayDate(),getYesterdayDate(),"Supervisor","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
            }else{
                closingViewModel.listClosing(projectId,getYesterdayDate(),getYesterdayDate(),"Team Leader","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
            }
        }else{
            if(isChief){
                closingViewModel.listClosing(projectId,date,date,"Supervisor","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
            }else{
                closingViewModel.listClosing(projectId,date,date,"Team Leader","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
            }
        }

//        if(jobLevel == "CHIEF SUPERVISOR" || jobLevel == "Chief Supervisor"){
//            closingViewModel.listClosing(projectId,date,date,"Team Leader","Supervisor",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
//        }else{
//            closingViewModel.listClosing(projectId,date,date,"Team Leader","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
//        }
        closingViewModel.listClosingModel.observe(this){
            if(it.code == 200){
                if(it.data.models.content != null){
                    if(it.data.models.content.isNotEmpty()){
                        binding.tvEmpty.visibility = View.GONE
                        isLastPage = it.data.models.last
                        if(page == 0){
                            val total = "Total : ${it.data.models.content.size} pekerjaan"
                            binding.tvTotalClosing.text = total
                            binding.shimmerLayout.visibility = View.GONE
                            binding.rvClosingPending.visibility = View.VISIBLE
                            showRecycler(it.data.models.content.sortedBy { it.status == "BELUM CLOSING" })
                        }else{
                            closingPendingAdapter.data.addAll(it.data.models.content)
                            closingPendingAdapter.notifyItemRangeChanged(
                                closingPendingAdapter.data.size - it.data.models.content.size,
                                closingPendingAdapter.data.size
                            )
                        }
                    }else{
                        val total = "Total : -"
                        binding.tvTotalClosing.text = total
                        binding.shimmerLayout.stopShimmerAnimation()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.rvClosingPending.visibility = View.GONE
                        binding.tvEmpty.visibility = View.VISIBLE
                    }
                }else{
                    val total = "Total : -"
                    binding.tvTotalClosing.text = total
                    binding.shimmerLayout.stopShimmerAnimation()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvClosingPending.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showRecycler(data : List<ClosingModel>){
        closingPendingAdapter = ClosingPendingAdapter(data.toMutableList())
        binding.rvClosingPending.apply {
            adapter = closingPendingAdapter
            layoutManager = LinearLayoutManager(this@ListPendingClosingActivity)
        }
        val scrollListener = object : EndlessScrollingRecyclerView(LinearLayoutManager(this@ListPendingClosingActivity)) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
                    val currentDate = Date()
                    val date = dateFormat.format(currentDate)
                    if(jobLevel.contains("chief",ignoreCase = true) || jobLevel.contains("supervisor",ignoreCase = true)){
                        if(isChief){
                            closingViewModel.listClosing(projectId,getYesterdayDate(),getYesterdayDate(),"Supervisor","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
                        }else{
                            closingViewModel.listClosing(projectId,getYesterdayDate(),getYesterdayDate(),"Team Leader","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
                        }
                    }else{
                        if(isChief){
                            closingViewModel.listClosing(projectId,date,date,"Supervisor","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
                        }else{
                            closingViewModel.listClosing(projectId,date,date,"Team Leader","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
                        }
                    }
//                    if(jobLevel == "CHIEF SUPERVISOR" || jobLevel == "Chief Supervisor"){
//                        closingViewModel.listClosing(projectId,date,date,"Team Leader","Supervisor",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
//                    }else{
//
//                    }

                }
            }
        }
        binding.rvClosingPending.addOnScrollListener(scrollListener)
    }

    private fun showBottomSheetFilter(){
        val bottomSheet = BottomSheetDialog(this)
        val view = BottomSheetFilterClosingBinding.inflate(layoutInflater)
        bottomSheet.apply {
            view.apply {
                setContentView(view.root)
                val layoutManager =
                    LinearLayoutManager(this@ListPendingClosingActivity, LinearLayoutManager.VERTICAL, false)
                rvChooseShift.layoutManager = layoutManager
                val listChooseInspeksi = ArrayList<String>()
                listChooseInspeksi.add("Semua Status")
                listChooseInspeksi.add("BELUM CLOSING")
                listChooseInspeksi.add("SUDAH CLOSING")
                rvChooseShift.adapter = ListChooseInspeksiAdapter(listChooseInspeksi).also {
                    it.setListener(this@ListPendingClosingActivity)
                }
            }
            show()
        }
    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    override fun onItemSelected(item: String) {
        binding.tvChooseFilter.text = item
        filter = item
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        val currentDate = Date()
        val date = dateFormat.format(currentDate)
        if(jobLevel.contains("chief",ignoreCase = true) || jobLevel.contains("supervisor",ignoreCase = true)){
            if(isChief){
                closingViewModel.listClosing(projectId,getYesterdayDate(),getYesterdayDate(),"Supervisor","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
            }else{
                closingViewModel.listClosing(projectId,getYesterdayDate(),getYesterdayDate(),"Team Leader","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
            }
        }else{
            if(isChief){
                closingViewModel.listClosing(projectId,date,date,"Supervisor","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
            }else{
                closingViewModel.listClosing(projectId,date,date,"Team Leader","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
            }
        }
//        if(jobLevel == "CHIEF SUPERVISOR" || jobLevel == "Chief Supervisor"){
//            closingViewModel.listClosing(projectId,date,date,"Team Leader","Supervisor",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
//        }else{
//            closingViewModel.listClosing(projectId,date,date,"Team Leader","",if(filter.contains("semua",ignoreCase = true)) "" else filter,page,10)
//        }
    }
}