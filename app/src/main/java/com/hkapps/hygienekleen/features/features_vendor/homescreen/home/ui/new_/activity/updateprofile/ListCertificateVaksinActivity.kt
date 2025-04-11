package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityUpdateCertificateVaksinBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listvaccine.ContentVaccine
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.ListVaccineAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment.BotSheetUploadVaccineFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class ListCertificateVaksinActivity : AppCompatActivity(), ListVaccineAdapter.ListVaccineClick {
    private lateinit var binding: ActivityUpdateCertificateVaksinBinding
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private lateinit var adapters: ListVaccineAdapter
    //val
    val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    var page: Int = 0
    private var isLastPage = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateCertificateVaksinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutAppbarHomeNews.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutAppbarHomeNews.tvAppbarTitle.text = "Sertifikat Vaksin"

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvVaccine.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.rvVaccine.addOnScrollListener(scrollListener)
        //open fragment
        binding.layoutAppbarHomeNews.llBtnUpload.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER, "")
            BotSheetUploadVaccineFragment().show(supportFragmentManager, "bottomsheet")
        }

        loadData()
        setObserver()
        //oncreate
    }

    private fun loadData() {
        viewModel.getListVaccine(employeeId, page)

    }

    private fun setObserver() {
        viewModel.getListVaccineViewModel().observe(this){
            if (it.code == 200){

                if (it.data.content.isEmpty()){
                    binding.llEmptyStateVaccine.visibility = View.VISIBLE
                } else {
//                    val commaSeperatedString = it.data.content.joinToString (separator = ", ") { it -> "\'${it}\'" }

                    binding.llEmptyStateVaccine.visibility = View.GONE
                    binding.rvVaccine.visibility = View.VISIBLE

                    isLastPage = false
                    if (page == 0){
                        adapters = ListVaccineAdapter(this, it.data.content as ArrayList<ContentVaccine>
                        ).also { it.setListener(this) }
                        binding.rvVaccine.adapter = adapters
                    } else {
                        adapters.listVaccine.addAll(it.data.content as ArrayList<ContentVaccine>)
                        adapters.notifyItemRangeChanged(
                            adapters.listVaccine.size - it.data.size,
                            adapters.listVaccine.size
                        )
                    }
                }


            } else {
                Toast.makeText(this, "gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClickVaccine(vaccineCertificate: String, idVaccine: Int, vaccineType: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CHANGE_VACCINE,"")
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_VACCINE, idVaccine)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.VACCINE_NAME, vaccineType)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.VACCINE_CERTIFICATE, vaccineCertificate)
        startActivity(Intent(this, ChangeCertificateVaksinActivity::class.java))

    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
    //fun
}