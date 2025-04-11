package com.hkapps.hygienekleen.features.features_management.service.permission.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityPermissionsApprovalManagementBinding
import com.hkapps.hygienekleen.features.features_management.service.permission.model.approvalPermissionManagement.Content
import com.hkapps.hygienekleen.features.features_management.service.permission.ui.adapter.ApprovalPermissionAdapter
import com.hkapps.hygienekleen.features.features_management.service.permission.viewmodel.PermissionManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.customcalendar.setTextColorRes
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.adapter.ListChooserPermissionAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

class PermissionsApprovalManagementActivity : AppCompatActivity(), ListChooserPermissionAdapter.OnItemSelectedCallBack{

    private lateinit var binding: ActivityPermissionsApprovalManagementBinding
    private lateinit var rvAdapter: ApprovalPermissionAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var message: String = ""

    private var selectedPosition = ""
    private var jabatan = "Operator"
    private var isLastPage = false
    private var page = 0
    private var size = 10
    private var reloadNeeded = true

    private val viewModel: PermissionManagementViewModel by lazy {
        ViewModelProviders.of(this).get(PermissionManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionsApprovalManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarPermissionManagement.tvAppbarTitle.text = "Izin"
        binding.appbarPermissionManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }
        binding.appbarPermissionManagement.ivAppbarHistory.setOnClickListener {
            startActivity(Intent(this, HistoryPermissionManagementActivity::class.java))
        }

        // set validate layout by user level
        if (userLevel == "BOD" || userLevel == "CEO") {
            binding.appbarPermissionManagement.ivAppbarHistory.visibility = View.GONE
            binding.tvPositionPermissionManagement.visibility = View.GONE
            binding.ivCreatePermissionManagement.visibility = View.GONE
        } else {
            binding.appbarPermissionManagement.ivAppbarHistory.visibility = View.VISIBLE
            binding.tvPositionPermissionManagement.visibility = View.VISIBLE
            binding.ivCreatePermissionManagement.visibility = View.VISIBLE

            binding.tvPositionPermissionManagement.setOnClickListener {
                bottomSheetChoosePosition()
            }
        }

        // set shimmer effect
        binding.shimmerPermissionManagement.startShimmerAnimation()
        binding.shimmerPermissionManagement.visibility = View.VISIBLE
        binding.rvPermissionManagement.visibility = View.GONE
        binding.tvEmptyPermissionManagement.visibility = View.GONE

        // swipe refresh
        binding.swipePermissionManagement.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipePermissionManagement.isRefreshing = false
                val i = Intent(
                    this@PermissionsApprovalManagementActivity,
                    PermissionsApprovalManagementActivity::class.java
                )
                startActivity(i)
                finish()
                overridePendingTransition(R.anim.nothing, R.anim.nothing)
            }, 500)
        }

        // set layout manager recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPermissionManagement.layoutManager = layoutManager

        // scroll recycler view
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    viewModel.getApprovalPermission(userId, jabatan, page,size)
                }
            }
        }
        binding.rvPermissionManagement.addOnScrollListener(scrollListener)

        // set validation create izin
        binding.ivCreatePermissionManagement.setOnClickListener {
            when(message) {
                "VALID" -> {
//                    startActivity(Intent(this, CreatePermissionManagementActivity::class.java))
                    startActivityForResult(Intent(this, CreatePermissionManagementActivity::class.java), CREATE_CODE)
                    binding.tvInfoValidationCreatePermissionManagement.visibility = View.INVISIBLE
                }
                "INVALID" -> {
                    binding.tvInfoValidationCreatePermissionManagement.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvInfoValidationCreatePermissionManagement.visibility = View.INVISIBLE
                    }, 2000)
                }
                else -> {
                    Toast.makeText(this, "Error validasi izin", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerPermissionManagement.stopShimmerAnimation()
                        binding.shimmerPermissionManagement.visibility = View.GONE
                        binding.rvPermissionManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        }
        viewModel.approvalPermissionModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvEmptyPermissionManagement.visibility = View.GONE
                        isLastPage = it.data.last
                        if (page == 0) {
                            rvAdapter = ApprovalPermissionAdapter(
                                this,
                                viewModel,
                                it.data.content as ArrayList<Content>
                            )
                            binding.rvPermissionManagement.adapter = rvAdapter
                        } else {
                            rvAdapter.listPermission.addAll(it.data.content)
                            rvAdapter.notifyItemRangeChanged(
                                rvAdapter.listPermission.size - it.data.content.size,
                                rvAdapter.listPermission.size
                            )
                        }
                    }, 1500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.rvPermissionManagement.adapter = null
                        binding.tvEmptyPermissionManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            } else {
                binding.rvPermissionManagement.adapter = null
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.permissionValidateModel.observe(this) {
            message = it.message
        }
    }

    private fun loadData() {
        viewModel.getApprovalPermission(userId, jabatan, page,size)
        viewModel.getPermissionValidateManagement(userId)
    }

    private fun bottomSheetChoosePosition() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_choose_position)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomChoosePosition)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvBottomChoosePosition)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnAppliedBottomChoosePosition)

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager

        ivClose?.setOnClickListener {
            selectedPosition = ""
            dialog.dismiss()
        }

        button?.setOnClickListener {
            if (selectedPosition == "") {
                Toast.makeText(this, "Silahkan pilih posisi", Toast.LENGTH_SHORT).show()
            } else {
                dialog.dismiss()
                binding.tvPositionPermissionManagement.text = selectedPosition
                binding.tvPositionPermissionManagement.setBackgroundResource(R.drawable.bg_spinner_blue)
                binding.tvPositionPermissionManagement.setTextColorRes(R.color.blueInfo)
                selectedPosition = ""
                page = 0
                isLastPage = false

                // set shimmer effect
                binding.shimmerPermissionManagement.startShimmerAnimation()
                binding.shimmerPermissionManagement.visibility = View.VISIBLE
                binding.rvPermissionManagement.visibility = View.INVISIBLE
                binding.tvEmptyPermissionManagement.visibility = View.GONE

                loadData()
                setObserver()
            }
        }

        val listChoosePosition = ArrayList<String>()
        when(userLevel) {
            "FM" -> {
                listChoosePosition.add("Operator")
                listChoosePosition.add("Team Leader")
                listChoosePosition.add("Supervisor")
                listChoosePosition.add("Chief Supervisor")
            }
            "OM" -> {
                listChoosePosition.add("Operator")
                listChoosePosition.add("Team Leader")
                listChoosePosition.add("Supervisor")
                listChoosePosition.add("Chief Supervisor")
                listChoosePosition.add("Field Manager")
            }
            "GM" -> {
                listChoosePosition.add("Operator")
                listChoosePosition.add("Team Leader")
                listChoosePosition.add("Supervisor")
                listChoosePosition.add("Chief Supervisor")
                listChoosePosition.add("Field Manager")
                listChoosePosition.add("Operational Manager")
            }
        }
        val selectedItemAdapter: Int = when(selectedPosition) {
            "Operator" -> 0
            "Team Leader" -> 1
            "Supervisor" -> 2
            "Chief Supervisor" -> 3
            "Field Manager" -> 4
            "Operational Manager" -> 5
            else -> -1
        }
        recyclerView?.adapter = ListChooserPermissionAdapter(listChoosePosition, selectedItemAdapter).also { it.setListener(this) }

        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onItemSelected(item: String) {
        selectedPosition = item
        jabatan = when(item) {
            "Field Manager" -> "FM"
            "Operational Manager" -> "OM"
            else -> item
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                this.reloadNeeded = true
            }
        }
    }

    companion object {
        private const val CREATE_CODE = 31
    }

    override fun onResume() {
        super.onResume()
        if (this.reloadNeeded)
            loadData()

        this.reloadNeeded = false
    }
}