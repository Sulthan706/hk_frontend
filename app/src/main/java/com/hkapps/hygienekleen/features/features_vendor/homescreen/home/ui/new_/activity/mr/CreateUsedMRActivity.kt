package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.mr

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityCreateUsedMractivityBinding
import com.hkapps.hygienekleen.databinding.BottomsheetChooseFilterBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMRData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.ChooserMRAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class CreateUsedMRActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateUsedMractivityBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private val homeViewModel : HomeViewModel by viewModels<HomeViewModel>()

    private var projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE,"")
    private var userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID,0)

    private var idItemReq = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUsedMractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        binding.apply {
            setupEdgeToEdge(binding.root, binding.statusBarBackground)
            appBarCreateMP.tvAppbarTitle.text = "Form Used MR"
            appBarCreateMP.ivAppbarBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }
            onBackPressedDispatcher.addCallback(onBackPressedCallback)
        }
        binding.tvItem.setOnClickListener {
            bottomSheetPosition { namaItem, unitCode, idItem,quantity  ->
                binding.tvItem.setText(namaItem)
                binding.tvUnit.setText(unitCode)
                binding.tvType.setText(quantity.toString())
                idItemReq = idItem
            }
        }
        binding.btnSubmitCreateScheduleManagement.setOnClickListener {
            homeViewModel.createUsed(projectCode,idItemReq,binding.tvType.text.toString().toInt(),binding.tvUnit.text.toString(),userId)
            homeViewModel.createUsed.observe(this) {
                if(it.code == 200){
                    Toast.makeText(this, "Success insert", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }else{
                    Toast.makeText(this, "Failed insert", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bottomSheetPosition(onItemSelected: (String,String,Int,Int) -> Unit){
        val bottomSheet = BottomSheetDialog(this)
        val view = BottomsheetChooseFilterBinding.inflate(layoutInflater)

        bottomSheet.apply {
            view.apply {
                setContentView(view.root)
                tittle.text = "Choose Item"
                textInputLayout.visibility = View.VISIBLE
                add.visibility = View.GONE
                menuProject.visibility = View.GONE

                var jobCoder = ""
                var jobNamer = ""
                var idItems = 0
                var quantities = 0
                val adapterChooser = ChooserMRAdapter(mutableListOf(),"",object : ChooserMRAdapter.OnItemRequestMRClickListener{

                    override fun onItemRequestMRClick(
                        idItem: Int,
                        namaItem: String,
                        unitCode: String,
                        quantity : Int,
                    ) {
                        idItems = idItem
                        jobCoder = namaItem
                        jobNamer = unitCode
                        quantities = quantity
                        btnSave.isEnabled = true
                    }
                })

                homeViewModel.getListStockMR(projectCode)
                homeViewModel.getMRStock.observe(this@CreateUsedMRActivity){
                    if (it.code == 200) {
                        val datas = mutableListOf<ItemMRData>()
                        it.data.map {
                            datas.add(ItemMRData(it.itemId,it.itemCode,it.itemName,it.unitCode,it.quantity))
                        }
                        adapterChooser.updateData(datas)
                    } else {
                        Toast.makeText(
                            this@CreateUsedMRActivity,
                            "Cannot get list position",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                btnSave.setOnClickListener {
                    onItemSelected(jobCoder,jobNamer,idItems,quantities)
                    dismiss()
                }

                etSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val query = s.toString().trim()
                        homeViewModel.getListStockMR(query)
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })

                rvRequestMp.apply {
                    adapter = adapterChooser
                    layoutManager = LinearLayoutManager(this@CreateUsedMRActivity)
                }
            }
            show()
        }

    }
}