package com.hkapps.hygienekleen.features.features_vendor.damagereport.ui.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListBakMachineBinding
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachine
import com.hkapps.hygienekleen.features.features_vendor.damagereport.ui.adapter.ListBakMachineAdapter
import com.hkapps.hygienekleen.features.features_vendor.damagereport.viewmodel.DamageReportVendorViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ListBakMachineActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListBakMachineBinding

    private lateinit var bakMachineAdapter: ListBakMachineAdapter

    private var page = 0

    private var isLastPage = false

    private var size = 10

    private val bakMachineViewModel by lazy {
        ViewModelProviders.of(this)[DamageReportVendorViewModel::class.java]
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBakMachineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        initView()
    }

    private fun initView(){
        val appBar = "Daftar Mesin Bulan ${getCurrentMonthYear()}"
        binding.appBarBakMachine.tvAppbarTitle.text = appBar
        binding.appBarBakMachine.tvAppbarTitle.textSize = 16.0f
        binding.appBarBakMachine.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        binding.shimmerLayout.startShimmerAnimation()
        Handler().postDelayed({
            binding.shimmerLayout.visibility = View.GONE
            binding.rvBakMachine.visibility = View.VISIBLE
            loadData()
        },1000)

    }

    private fun getCurrentMonthYear() : String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("id", "ID"))
        return  dateFormat.format(calendar.time)
    }

    private fun loadData(){
        bakMachineViewModel.getListBakMachine(projectId,page,size)
        bakMachineViewModel.getListBakMachine.observe(this){
            if(it.code == 200){
                binding.shimmerLayout.visibility = View.GONE
                binding.tvEmpty.visibility = View.GONE
                binding.tvTotal.text = "Jumlah mesin : ${it.data.listMesin.size}"
                binding.tvRusak.text = "Mesin rusak : ${it.data.totalMesinRusak}"
                binding.tvBaik.text = "Mesin baik : ${it.data.totalMesinBaik}"
                if(it.data.listMesin.content.isNotEmpty()){
                    isLastPage = it.data.listMesin.last
                    binding.rvBakMachine.visibility = View.VISIBLE
                    if(page == 0){
                        showRecycler(it.data.listMesin.content)
                    }else{
                        bakMachineAdapter.data.addAll(it.data.listMesin.content)
                        bakMachineAdapter.notifyItemRangeChanged(
                            bakMachineAdapter.data.size - it.data.listMesin.content.size,
                            bakMachineAdapter.data.size
                        )
                    }
                }else{
                    binding.rvBakMachine.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRecycler(data : List<BakMachine>){
        bakMachineAdapter = ListBakMachineAdapter(this,data.sortedBy{ it.machineStatus == "Y" }.toMutableList(),object : ListBakMachineAdapter.BakMachineOnClick{
            override fun onDetailImageFront(image: String) {
                showPopup(image)
            }

            override fun onDetailImageSide(image: String) {
               showPopup(image)
            }

        })

        val rvLayoutManager = LinearLayoutManager(this@ListBakMachineActivity)
        binding.rvBakMachine.apply {
            adapter = bakMachineAdapter
            layoutManager = rvLayoutManager
        }

        val scrollListener = object : EndlessScrollingRecyclerView(rvLayoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    bakMachineViewModel.getListBakMachine(projectId,page,size)
                }
            }
        }
        binding.rvBakMachine.addOnScrollListener(scrollListener)
    }

    private fun showPopup(image : String){
        val dialog = Dialog(this)
        val url = getString(R.string.url) + "assets.admin_master/mesin/$image"
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_image_slide)
        val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
        val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView
        Glide.with(ivZoom.context).load(url).into(ivZoom)

        close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}