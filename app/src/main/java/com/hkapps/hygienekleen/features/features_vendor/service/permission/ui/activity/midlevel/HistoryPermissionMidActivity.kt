package com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHistoryPermissionBinding
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.ListHistoryPermission
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.customcalendar.setTextColorRes
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.adapter.ListChooserPermissionAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.adapter.lowlevel.HistoryPermissionAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.permission.viewmodel.PermissionViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryPermissionMidActivity : AppCompatActivity(),
    HistoryPermissionAdapter.HistoryPermissionCallBack,
    ListChooserPermissionAdapter.OnItemSelectedCallBack
{

    private lateinit var binding: ActivityHistoryPermissionBinding
    private lateinit var rvAdapter: HistoryPermissionAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var reloadNeeded = true
    private var selectedDateChooser = "Semua tanggal"
    private var selectedItem = ""
    private var message = ""
    private var startDate = ""
    private var startDateText = "Pilih tanggal"
    private var endDate = ""
    private var endDateText = "Pilih tanggal"

    private val viewModel: PermissionViewModel by lazy {
        ViewModelProviders.of(this).get(PermissionViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryPermissionBinding.inflate(layoutInflater)
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
        binding.appbarHistorypermission.tvAppbarTitle.text = "Riwayat Izin Saya"
        binding.appbarHistorypermission.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        binding.tvChooseDateHistoryPermissionMid.setOnClickListener {
            bottomSheetChooseDate()
        }

        // set shimmer effect
        binding.shimmerHistorypermission.startShimmerAnimation()
        binding.shimmerHistorypermission.visibility = View.VISIBLE
        binding.rvHistoryPermission.visibility = View.GONE
        binding.tvEmptyHistory.visibility = View.INVISIBLE

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryPermission.layoutManager = layoutManager

        binding.swipeHistoryPermission.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeHistoryPermission.isRefreshing = false
                    val i = Intent(this, HistoryPermissionMidActivity::class.java)
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
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.PERMISSION_CLICK_FROM, "historyPermission")
                    startActivityForResult(Intent(this, CreatePermissionMidFixActivity::class.java),
                        CREATE_CODE
                    )
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
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
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
                    binding.tvChooseDateHistoryPermissionMid.text = "$startDateText - $endDateText"
                    binding.tvChooseDateHistoryPermissionMid.setBackgroundResource(R.drawable.bg_spinner_blue)
                    binding.tvChooseDateHistoryPermissionMid.setTextColorRes(R.color.blueInfo)

                    // set default data
                    startDateText = "Pilih tanggal"
                    endDateText = "Pilih tanggal"

                    dialog.dismiss()
                    Handler(Looper.getMainLooper()).postDelayed( {
                        // set shimmer effect
                        binding.tvEmptyHistory.visibility = View.INVISIBLE
                        binding.shimmerHistorypermission.startShimmerAnimation()
                        binding.shimmerHistorypermission.visibility = View.VISIBLE
                        binding.rvHistoryPermission.visibility = View.GONE
                        binding.rvHistoryPermission.adapter = null

                        viewModel.getHistoryDateMid(userId, startDate, endDate)
                        setObserver()
                    }, 500)
                }
            } else {
                llChooseDate?.visibility = View.INVISIBLE
                binding.tvChooseDateHistoryPermissionMid.text = selectedItem
                binding.tvChooseDateHistoryPermissionMid.setBackgroundResource(R.drawable.bg_spinner_blue)
                binding.tvChooseDateHistoryPermissionMid.setTextColorRes(R.color.blueInfo)

                dialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed( {
                    // set shimmer effect
                    binding.tvEmptyHistory.visibility = View.INVISIBLE
                    binding.shimmerHistorypermission.startShimmerAnimation()
                    binding.shimmerHistorypermission.visibility = View.VISIBLE
                    binding.rvHistoryPermission.visibility = View.GONE
                    binding.rvHistoryPermission.adapter = null

                    viewModel.getHistoryPermissionMid(userId)
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

    private fun loadData() {
        if (selectedDateChooser == "Semua tanggal") {
            viewModel.getHistoryPermissionMid(userId)
        } else {
            viewModel.getHistoryDateMid(userId, startDate, endDate)
        }
        viewModel.getCheckPermission(userId)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerHistorypermission.stopShimmerAnimation()
                        binding.shimmerHistorypermission.visibility = View.GONE
                        binding.rvHistoryPermission.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.historyPermissionMidModel.observe(this) { it ->
            if (it.code == 200) {
                if (it.message != "Tidak Ada Izin") {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.tvEmptyHistory.visibility = View.GONE

                        // set rv adapter
                        rvAdapter = HistoryPermissionAdapter(
                            this,
                            it.data as ArrayList<ListHistoryPermission>
                        ).also { it.setListener(this) }
                        binding.rvHistoryPermission.adapter = rvAdapter
                    }, 1500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvEmptyHistory.visibility = View.VISIBLE
                        binding.tvEmptyHistory.text = "Tidak ada izin yang diajukan.\nUntuk mengajukan izin, silakan klik pada tombol di bawah."
                        binding.rvHistoryPermission.adapter = null
                    }, 1500)
                }
            } else {
                binding.rvHistoryPermission.adapter = null
                Toast.makeText(this, "Gagal mengambil data history", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.historyDateMidModel.observe(this) {
            if (it.code == 200) {
                if (it.message != "Tidak Ada Izin") {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.tvEmptyHistory.visibility = View.GONE

                        // set rv adapter
                        rvAdapter = HistoryPermissionAdapter(
                            this,
                            it.data as ArrayList<ListHistoryPermission>
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvHistoryPermission.adapter = rvAdapter
                    }, 1500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.tvEmptyHistory.visibility = View.VISIBLE
                        binding.tvEmptyHistory.text = "Tidak ada izin yang diajukan pada tanggal ini"
                        binding.rvHistoryPermission.adapter = null
                    }, 1500)
                }
            } else {
                binding.rvHistoryPermission.adapter = null
                Toast.makeText(this, "Gagal mengambil data history", Toast.LENGTH_SHORT).show()
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
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.PERMISSION_ID, permissionId)
        val i = Intent(this, DetailHistoryPermissionActivity::class.java)
        startActivity(i)
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
        if (this.reloadNeeded) {
            loadData()
            setObserver()
        }

        this.reloadNeeded = false
    }

    override fun onPause() {
        super.onPause()
        this.reloadNeeded = true
    }

}