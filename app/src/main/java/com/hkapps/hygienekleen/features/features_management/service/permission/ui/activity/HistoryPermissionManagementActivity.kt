package com.hkapps.hygienekleen.features.features_management.service.permission.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHistoryPermissionManagementBinding
import com.hkapps.hygienekleen.features.features_management.service.permission.model.historyPermissionManagement.Content
import com.hkapps.hygienekleen.features.features_management.service.permission.ui.adapter.HistoryPermissionManagementAdapter
import com.hkapps.hygienekleen.features.features_management.service.permission.viewmodel.PermissionManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.customcalendar.setTextColorRes
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.adapter.ListChooserPermissionAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryPermissionManagementActivity : AppCompatActivity(),
    ListChooserPermissionAdapter.OnItemSelectedCallBack,
    HistoryPermissionManagementAdapter.HistoryPermissionCallBack
{

    private lateinit var binding: ActivityHistoryPermissionManagementBinding
    private lateinit var rvAdapter: HistoryPermissionManagementAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var message = ""
    private var selectedDateChooser = "Semua tanggal"
    private var selectedItem = ""
    private var startDate = ""
    private var startDateText = "Pilih tanggal"
    private var endDate = ""
    private var endDateText = "Pilih tanggal"
    private var page = 0
    private var isLastPage = false
    private var reloadNeeded = true

    private val viewModel: PermissionManagementViewModel by lazy {
        ViewModelProviders.of(this).get(PermissionManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryPermissionManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarHistoryPermissionManagement.tvAppbarTitle.text = "Riwayat Izin Saya"
        binding.appbarHistoryPermissionManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // choose date
        binding.tvChooseDateHistoryPermissionManagement.setOnClickListener {
            bottomSheetChooseDate()
        }

        // set shimmer effect
        binding.shimmerHistoryPermissionManagement.startShimmerAnimation()
        binding.shimmerHistoryPermissionManagement.visibility = View.VISIBLE
        binding.rvHistoryPermissionManagement.visibility = View.GONE
        binding.tvEmptyHistoryPermissionManagement.visibility = View.GONE

        // set layout manager recyclerview
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryPermissionManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvHistoryPermissionManagement.addOnScrollListener(scrollListener)

        // swipe refresh
        binding.swipeHistoryPermissionManagement.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeHistoryPermissionManagement.isRefreshing = false
                onRefreshLayout()
                overridePendingTransition(R.anim.nothing, R.anim.nothing)
            }, 500)
        }

        // set validation create izin
        binding.ivCreatePermissionHistoryManagement.setOnClickListener {
            when(message) {
                "VALID" -> {
//                    startActivity(Intent(this, CreatePermissionManagementActivity::class.java))
                    startActivityForResult(Intent(this, CreatePermissionManagementActivity::class.java),
                        CREATE_CODE
                    )
                    binding.tvInfoValidationCreatePermissionHistoryManagement.visibility = View.INVISIBLE
                }
                "INVALID" -> {
                    binding.tvInfoValidationCreatePermissionHistoryManagement.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvInfoValidationCreatePermissionHistoryManagement.visibility = View.INVISIBLE
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

    private fun onRefreshLayout() {
        selectedDateChooser = "Semua tanggal"
        selectedItem = ""
        startDate = ""
        startDateText = "Pilih tanggal"
        endDate = ""
        endDateText = "Pilih tanggal"
        page = 0
        isLastPage = false
        reloadNeeded = true

        // set shimmer effect
        binding.shimmerHistoryPermissionManagement.startShimmerAnimation()
        binding.shimmerHistoryPermissionManagement.visibility = View.VISIBLE
        binding.rvHistoryPermissionManagement.visibility = View.GONE
        binding.tvEmptyHistoryPermissionManagement.visibility = View.GONE

        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getHistoryPermissionManagement(userId, page)
        viewModel.getPermissionValidateManagement(userId)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerHistoryPermissionManagement.stopShimmerAnimation()
                        binding.shimmerHistoryPermissionManagement.visibility = View.GONE
                        binding.rvHistoryPermissionManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        }
        viewModel.historyPermissionModel.observe(this) {
            if (it.code == 200) {
                if (it.message != "Tidak Ada Izin") {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvEmptyHistoryPermissionManagement.visibility = View.GONE
                        isLastPage = it.data.last
                        if (page == 0) {
                            rvAdapter = HistoryPermissionManagementAdapter(
                                this,
                                it.data.content as ArrayList<Content>
                            ).also { it1 -> it1.setListener(this) }
                            binding.rvHistoryPermissionManagement.adapter = rvAdapter
                        } else {
                            rvAdapter.listHistoryPermission.addAll(it.data.content)
                            rvAdapter.notifyItemRangeChanged(
                                rvAdapter.listHistoryPermission.size - it.data.content.size,
                                rvAdapter.listHistoryPermission.size
                            )
                        }
                    }, 1500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.rvHistoryPermissionManagement.adapter = null
                        binding.tvEmptyHistoryPermissionManagement.visibility = View.VISIBLE
                        binding.tvEmptyHistoryPermissionManagement.text = "Tidak ada izin yang diajukan.\nUntuk mengajukan izin, silakan klik pada tombol di bawah."
                    }, 1500)
                }
            } else {
                binding.rvHistoryPermissionManagement.adapter = null
                Toast.makeText(this, "Gagal mengambil data list history izin", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.filterPermissionModel.observe(this) {
            if (it.code == 200) {
                if (it.message != "Tidak Ada Izin") {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvEmptyHistoryPermissionManagement.visibility = View.GONE
                        isLastPage = it.data.last
                        if (page == 0) {
                            rvAdapter = HistoryPermissionManagementAdapter(
                                this,
                                it.data.content as ArrayList<Content>
                            ).also { it1 -> it1.setListener(this) }
                            binding.rvHistoryPermissionManagement.adapter = rvAdapter
                        } else {
                            rvAdapter.listHistoryPermission.addAll(it.data.content)
                            rvAdapter.notifyItemRangeChanged(
                                rvAdapter.listHistoryPermission.size - it.data.content.size,
                                rvAdapter.listHistoryPermission.size
                            )
                        }
                    }, 1500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.rvHistoryPermissionManagement.adapter = null
                        binding.tvEmptyHistoryPermissionManagement.visibility = View.VISIBLE
                        binding.tvEmptyHistoryPermissionManagement.text = "Tidak ada izin yang diajukan pada tanggal ini"
                    }, 1500)
                }
            } else {
                binding.rvHistoryPermissionManagement.adapter = null
                Toast.makeText(this, "Gagal mengambil data list history izin", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.permissionValidateModel.observe(this) {
            message = it.message
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun bottomSheetDatePicker(string: String) {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_date_picker)
        val close = dialog.findViewById<ImageView>(R.id.ivCloseBottomDatePicker)
        val text = dialog.findViewById<TextView>(R.id.tv1BottomDatePicker)
        val btnChoose = dialog.findViewById<AppCompatButton>(R.id.btnBottomDatePicker)
        val dateTv = dialog.findViewById<NumberPicker>(R.id.dateTv)
        val monthTv = dialog.findViewById<NumberPicker>(R.id.monthTv)
        val yearTv = dialog.findViewById<NumberPicker>(R.id.yearTv)

        close?.setOnClickListener {
            dialog.dismiss()
        }

        text?.text = string

        val calendar: Calendar = Calendar.getInstance()
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH)
        val currentDate: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val months = arrayOf(
            "Januari",
            "Februari",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agustus",
            "September",
            "Oktober",
            "November",
            "Desember"
        )

        dateTv?.minValue = 1
        dateTv?.maxValue = 31
        dateTv?.value = currentDate
        dateTv?.wrapSelectorWheel = true

        monthTv?.displayedValues = months
        monthTv?.minValue = 1
        monthTv?.maxValue = 12
        monthTv?.value = currentMonth+1
        monthTv?.wrapSelectorWheel = true

        yearTv?.minValue = 2000
        yearTv?.maxValue = currentYear
        yearTv?.value = currentYear
        yearTv?.wrapSelectorWheel = true

        var daySelected = currentDate
        var monthSelected = currentMonth+1
        var yearSelected = currentYear
        var startDateSelected: String
        var endDateSelected: String

        dateTv?.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(p0: NumberPicker?, oldValue: Int, newValue: Int) {
                daySelected = newValue
            }
        })

        monthTv?.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(p0: NumberPicker?, oldValue: Int, newValue: Int) {
                monthSelected = newValue
            }
        })

        yearTv?.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(p0: NumberPicker?, oldValue: Int, newValue: Int) {
                yearSelected = newValue
            }

        })

        btnChoose?.setOnClickListener {
            when(string) {
                "Mulai dari" -> {
                    startDateSelected = "$daySelected-$monthSelected-$yearSelected"
                    val sdfBefore = SimpleDateFormat("dd-MM-yyyy")
                    val dateParamBefore = sdfBefore.parse(startDateSelected)
                    val sdfAfter = SimpleDateFormat("dd MMM yyyy")
                    startDateText = sdfAfter.format(dateParamBefore)
                    val sdfAfterApi = SimpleDateFormat("yyyy-MM-dd")
                    startDate = sdfAfterApi.format(dateParamBefore)

                    bottomSheetChooseDate()
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 500)
                }
                "Sampai" -> {
                    endDateSelected = "$daySelected-$monthSelected-$yearSelected"
                    val sdfBefore = SimpleDateFormat("dd-MM-yyyy")
                    val dateParamBefore = sdfBefore.parse(endDateSelected)
                    val sdfAfter = SimpleDateFormat("dd MMM yyyy")
                    endDateText = sdfAfter.format(dateParamBefore)
                    val sdfAfterApi = SimpleDateFormat("yyyy-MM-dd")
                    endDate = sdfAfterApi.format(dateParamBefore)

                    bottomSheetChooseDate()
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 500)
                }
            }
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun bottomSheetChooseDate() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_choose_date)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomChooseDate)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvBottomChooseDate)
        val llChooseDate = dialog.findViewById<LinearLayout>(R.id.llChooseDateBottomChooseDate)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnAppliedBottomChooseDate)
        val start = dialog.findViewById<LinearLayout>(R.id.llStartDateChooseDate)
        val end = dialog.findViewById<LinearLayout>(R.id.llEndDateChooseDate)
        val tvStartDate = dialog.findViewById<TextView>(R.id.tvStartDateChooseDate)
        val tvEndDate = dialog.findViewById<TextView>(R.id.tvEndDateChooseDate)

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager

        ivClose?.setOnClickListener {
            selectedItem = binding.tvChooseDateHistoryPermissionManagement.text.toString()
            dialog.dismiss()
        }

        start?.setOnClickListener {
            bottomSheetDatePicker("Mulai dari")
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 500)
        }

        end?.setOnClickListener {
            bottomSheetDatePicker("Sampai")
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 500)
        }

        tvStartDate?.text = startDateText
        tvEndDate?.text = endDateText

        if (startDateText != "Pilih tanggal" || endDateText != "Pilih tanggal") {
            llChooseDate?.visibility = View.VISIBLE
        }

        if (selectedItem == "Semua tanggal" || selectedItem == "") {
            llChooseDate?.visibility = View.INVISIBLE
        } else {
            llChooseDate?.visibility = View.VISIBLE
        }

        button?.setOnClickListener {
            if (selectedItem == "Pilih tanggal sendiri") {
                llChooseDate?.visibility = View.VISIBLE
                if (startDateText == "Pilih tanggal" || endDateText == "Pilih tanggal") {
                    Toast.makeText(this, "Pilih tanggal terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    binding.tvChooseDateHistoryPermissionManagement.text = "$startDateText - $endDateText"
                    binding.tvChooseDateHistoryPermissionManagement.setBackgroundResource(R.drawable.bg_spinner_blue)
                    binding.tvChooseDateHistoryPermissionManagement.setTextColorRes(R.color.blueInfo)

                    // set default data
                    page = 0
                    isLastPage = false

                    dialog.dismiss()
                    Handler(Looper.getMainLooper()).postDelayed( {
                        // set shimmer effect
                        binding.shimmerHistoryPermissionManagement.startShimmerAnimation()
                        binding.shimmerHistoryPermissionManagement.visibility = View.VISIBLE
                        binding.tvEmptyHistoryPermissionManagement.visibility = View.GONE
                        binding.rvHistoryPermissionManagement.visibility = View.GONE
                        binding.rvHistoryPermissionManagement.adapter = null

                        viewModel.getFilterHistoryManagement(userId, startDate, endDate, page)
                        setObserver()
                    }, 500)
                }
            } else {
                llChooseDate?.visibility = View.INVISIBLE
                binding.tvChooseDateHistoryPermissionManagement.text = selectedItem
                binding.tvChooseDateHistoryPermissionManagement.setBackgroundResource(R.drawable.bg_spinner_blue)
                binding.tvChooseDateHistoryPermissionManagement.setTextColorRes(R.color.blueInfo)

                page = 0
                isLastPage = false
                startDateText = "Pilih tanggal"
                endDateText = "Pilih tanggal"

                dialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed( {
                    // set shimmer effect
                    binding.shimmerHistoryPermissionManagement.startShimmerAnimation()
                    binding.shimmerHistoryPermissionManagement.visibility = View.VISIBLE
                    binding.tvEmptyHistoryPermissionManagement.visibility = View.GONE
                    binding.rvHistoryPermissionManagement.visibility = View.GONE
                    binding.rvHistoryPermissionManagement.adapter = null

                    viewModel.getHistoryPermissionManagement(userId, page)
                    setObserver()
                }, 500)
            }
        }

        val listChoosePosition = ArrayList<String>()
        listChoosePosition.add("Semua tanggal")
        listChoosePosition.add("Pilih tanggal sendiri")
        val selectedItemAdapter: Int = when(selectedItem) {
            "Semua tanggal" -> 0
            "Pilih tanggal sendiri" -> 1
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
        selectedItem = item
        selectedDateChooser = item
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

    override fun onClickPermission(permissionId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.PERMISSION_ID, permissionId)
        startActivity(Intent(this, DetailHistoryPermissionManagementActivity::class.java))
    }
}