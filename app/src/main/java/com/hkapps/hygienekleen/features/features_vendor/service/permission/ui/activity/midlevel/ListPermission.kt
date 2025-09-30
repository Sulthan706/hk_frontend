package com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListPermissionBinding
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.ListPermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.PutDenialPermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.PutPermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.customcalendar.setTextColorRes
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.adapter.ListChooserPermissionAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.adapter.midlevel.ListPermissionAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.permission.viewmodel.PermissionViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ListPermission : AppCompatActivity(),
    ListPermissionAdapter.ListPermissionCallBack,
    ListChooserPermissionAdapter.OnItemSelectedCallBack
{

    private lateinit var binding: ActivityListPermissionBinding
    private lateinit var rvAdapter: ListPermissionAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private var page = 0
    private var isLastPage = false
    private var selectedPosition = ""
    private var jabatan = "Semua Posisi"
    private var message = ""

    private val viewModel: PermissionViewModel by lazy {
        ViewModelProviders.of(this).get(PermissionViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)

        // set appbar layout
        binding.appbarPermission.tvAppbarTitle.text = "Izin"
        binding.appbarPermission.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        binding.appbarPermission.ivAppbarHistory.setOnClickListener {
            val i = Intent(this, HistoryPermissionMidActivity::class.java)
            startActivity(i)
        }

        when(userLevel) {
            "Supervisor", "Chief Supervisor", "FM" -> {
                binding.tvPositionListPermission.visibility = View.VISIBLE
            }
            else -> binding.tvPositionListPermission.visibility = View.GONE
        }
        binding.tvPositionListPermission.setOnClickListener {
            bottomSheetChoosePosition()
        }

        // set shimmer effect
        binding.shimmerPermission.startShimmerAnimation()
        binding.shimmerPermission.visibility = View.VISIBLE
        binding.rvPermission.visibility = View.INVISIBLE

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPermission.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvPermission.addOnScrollListener(scrollListener)

//        binding.swipeNotification.setOnRefreshListener {
//            page = 0
//            loadData()
//        }

        binding.swipePermission.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed( {
                    binding.swipePermission.isRefreshing = false
                    val i = Intent(this, ListPermission::class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })

        binding.ivCreateComplaint.setOnClickListener {
            when(message) {
                "VALID" -> {
                    binding.tvInfoValidationCreateComplaint.visibility = View.INVISIBLE
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.PERMISSION_CLICK_FROM, "listPermission")
                    val i = Intent(this, CreatePermissionMidFixActivity::class.java)
                    startActivity(i)
                }
                "INVALID" -> {
                    binding.tvInfoValidationCreateComplaint.visibility = View.VISIBLE
                    Handler().postDelayed({
                        binding.tvInfoValidationCreateComplaint.visibility = View.INVISIBLE
                    }, 2000)
                }
                else -> {
                    Toast.makeText(this, "Error validasi izin", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loadData()
        setObserver()
        setObserverListPermission()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
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
                binding.tvPositionListPermission.text = selectedPosition
                binding.tvPositionListPermission.setBackgroundResource(R.drawable.bg_spinner_blue)
                binding.tvPositionListPermission.setTextColorRes(R.color.blueInfo)
                selectedPosition = ""
                page = 0
                isLastPage = false

                // set shimmer effect
                binding.shimmerPermission.startShimmerAnimation()
                binding.shimmerPermission.visibility = View.VISIBLE
                binding.rvPermission.visibility = View.INVISIBLE
                binding.tvEmptyPermission.visibility = View.GONE

                loadData()
                setObserver()
            }
        }

        val listChoosePosition = ArrayList<String>()
        when(userLevel) {
            "Supervisor" -> {
                listChoosePosition.add("Semua Posisi")
                listChoosePosition.add("Operator")
                listChoosePosition.add("Team Leader")
            }
            "Chief Supervisor", "FM" -> {
                listChoosePosition.add("Semua Posisi")
                listChoosePosition.add("Operator")
                listChoosePosition.add("Team Leader")
                listChoosePosition.add("Supervisor")
            }
        }

        val selectedItemAdapter: Int = when(selectedPosition) {
            "Semua Posisi" -> 0
            "Operator" -> 1
            "Team Leader" -> 2
            "Supervisor" -> 3
            else -> -1
        }
        recyclerView?.adapter = ListChooserPermissionAdapter(listChoosePosition, selectedItemAdapter).also { it.setListener(this) }

        dialog.show()
    }

    private fun setObserverListPermission() {
        viewModel.putOperatorModelNew().observe(this, Observer<PutPermissionResponseModel> {
            if (it.code == 200) {
                Toast.makeText(this, "Berhasil mengizinkan.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Silahkan coba lagi.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
        viewModel.putDenialOperatorModelNew().observe(this, Observer<PutDenialPermissionResponseModel> {
            if (it.code == 200) {
                Toast.makeText(this, "Berhasil menolak.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Silahkan coba lagi.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun loadData() {
        viewModel.getPermission(projectId, userId, jabatan, page)
        viewModel.getCheckPermission(userId)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerPermission.stopShimmerAnimation()
                        binding.shimmerPermission.visibility = View.GONE
                        binding.rvPermission.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.permissionResponseModel.observe(this) { it ->
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.tvEmptyPermission.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        // set rv adapter
                        rvAdapter = ListPermissionAdapter(
                            this,
                            viewModel,
                            it.data.content as ArrayList<ListPermissionResponseModel>
                        ).also { it.setListener(this) }
                        binding.rvPermission.adapter = rvAdapter
                    } else {
                        rvAdapter.listPermission.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listPermission.size - it.data.content.size,
                            rvAdapter.listPermission.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.tvEmptyPermission.visibility = View.VISIBLE
                    }, 1500)
                    binding.rvPermission.adapter = null
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.checkCreatePermissionModel.observe(this) {
            message = it.message
        }
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickedPermission(permissionId: Int) {
//        val i = Intent(this, DetailPermissionActivity::class.java)
//        i.putExtra("permissionId", permissionId)
//        startActivity(i)
    }

    override fun onItemSelected(item: String) {
        selectedPosition = item
        jabatan = item
    }
}