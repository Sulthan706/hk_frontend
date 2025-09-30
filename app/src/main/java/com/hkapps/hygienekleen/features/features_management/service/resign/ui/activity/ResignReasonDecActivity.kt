package com.hkapps.hygienekleen.features.features_management.service.resign.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.ActivityResignReasonDecBinding
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listreasonresign.ContentReasonResign
import com.hkapps.hygienekleen.features.features_management.service.resign.ui.adapter.ListReasonDecAdapter
import com.hkapps.hygienekleen.features.features_management.service.resign.viewmodel.ResignManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ResignReasonDecActivity : AppCompatActivity(), ListReasonDecAdapter.ReasonResignsCallback {
    private lateinit var binding: ActivityResignReasonDecBinding
    private val viewModel: ResignManagementViewModel by lazy {
        ViewModelProviders.of(this)[ResignManagementViewModel::class.java]
    }
    private lateinit var adapter: ListReasonDecAdapter
    private var type: String = "REJECTED"
    private var page: Int = 0
    private var size: Int = 10
    private var userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME,"")
    private var userNuc =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC,"")
    private var userPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_POSITION,"")
    private var idTurnOver: Int =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_TURN_OVER,0)
    private var adminMasterId: Int =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID,0)
    private var approval: String = "REJECTED"
    private var reasonId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResignReasonDecBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)

        binding.btnCloseResignReasonDec.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        binding.tvUserProfile.text = if (userName.isNullOrEmpty()) "" else userName
        binding.tvUserNucAndPosition.text = if (userNuc.isNullOrEmpty() && userPosition.isNullOrEmpty()) "" else "$userNuc | $userPosition"

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDecReasonResign.layoutManager = layoutManager

        binding.btnDecReasonResign.setOnClickListener {
            viewModel.submitResignManagement(idTurnOver, adminMasterId, reasonId, approval)
        }

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun loadData() {
        viewModel.getListReasonResign(type, page, size)
    }

    private fun setObserver() {
        viewModel.getListReasonResignManagementViewModel().observe(this) { it ->
            if (it.code == 200){
                adapter = ListReasonDecAdapter(it.data.content as ArrayList<ContentReasonResign>).also {
                    it.setListener(this)
                }
                binding.rvDecReasonResign.adapter = adapter
            }
        }
        viewModel.submitResignManagementViewModel().observe(this){
            if (it.code == 200){
                Toast.makeText(this, it.status, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, it.status, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClickReason(reasonId: Int) {
        this.reasonId = reasonId
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}