package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentOvertimeChangeTlBinding
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.listOvertimeNew.Content
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.activity.CreateOvertimeChangeMidActivity
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.activity.OvertimeTeamleadActivity
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter.*
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.viewmodel.OvertimeViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.customcalendar.setTextColorRes
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.adapter.ListChooserPermissionAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OvertimeChangeTlFragment : Fragment(),
    ListChooserPermissionAdapter.OnItemSelectedCallBack,
    JobPositionChooserAdapter.OnJobPositionCallBack
{

    private lateinit var binding: FragmentOvertimeChangeTlBinding
    private lateinit var rvAdapter: ListOvertimeChangeTlAdapter
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private var selectedPosition = ""
    private var jabatan = "Semua Posisi"
    private var selectedItem = ""
    private var startDate = ""
    private var startDateText = "Pilih tanggal"
    private var endDate = ""
    private var endDateText = "Pilih tanggal"
    private var page = 0
    private var isLastPage = false

    private val viewModel: OvertimeViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOvertimeChangeTlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (jobLevel == "Team Leader") {
            binding.tvPositionOvertimeChangeMid.visibility = View.INVISIBLE
        } else {
            binding.tvPositionOvertimeChangeMid.visibility = View.VISIBLE
        }

        // set on click filter
        binding.tvDateOvertimeChangeMid.setOnClickListener {
            bottomSheetChooseDate()
        }
        binding.tvPositionOvertimeChangeMid.setOnClickListener {
            bottomSheetChoosePosition()
        }

        // set on click create overtime change
        binding.ivCreateOvertimeChangeTl.setOnClickListener {
            startActivity(Intent(requireActivity(), CreateOvertimeChangeMidActivity::class.java))
        }

        // set shimmer effect
        binding.shimmerOvertimeChangeTl.startShimmerAnimation()
        binding.shimmerOvertimeChangeTl.visibility = View.VISIBLE
        binding.rvOvertimeChangeTl.visibility = View.GONE
        binding.tvEmptyValidationCreateOvertimeChange.visibility = View.GONE

        // set layout manager recycler view
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvOvertimeChangeTl.layoutManager = layoutManager

        // scroll recycler view
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvOvertimeChangeTl.addOnScrollListener(scrollListener)

        // refresh layout
        binding.swipeOvertimeChangeTl.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                val i = Intent(requireActivity(), OvertimeTeamleadActivity::class.java)
                startActivity(i)
                requireActivity().finish()
                requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
            }, 500)
        }

        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getFilterOvertimeChange(employeeId, projectCode, jabatan, startDate, endDate, page)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity()) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(context, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    binding.shimmerOvertimeChangeTl.stopShimmerAnimation()
                    binding.shimmerOvertimeChangeTl.visibility = View.GONE
                    binding.rvOvertimeChangeTl.visibility = View.VISIBLE
                }
            }
        }
        viewModel.filterOvertimeChangeResponse.observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvEmptyValidationCreateOvertimeChange.visibility = View.GONE
                        isLastPage = it.data.last
                        if (page == 0) {
                            rvAdapter = ListOvertimeChangeTlAdapter(
                                requireActivity(),
                                it.data.content as ArrayList<Content>
                            )
                            binding.rvOvertimeChangeTl.adapter = rvAdapter
                        } else {
                            rvAdapter.listOvertime.addAll(it.data.content)
                            rvAdapter.notifyItemRangeChanged(
                                rvAdapter.listOvertime.size - it.data.content.size,
                                rvAdapter.listOvertime.size
                            )
                        }
                    }, 1500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvEmptyValidationCreateOvertimeChange.visibility = View.VISIBLE
                        binding.rvOvertimeChangeTl.visibility = View.GONE
                        binding.rvOvertimeChangeTl.adapter = null
                    }, 1500)
                }
            } else {
                Toast.makeText(context, "Gagal mengambil data lembur ganti", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun bottomSheetChoosePosition() {
        val dialog = BottomSheetDialog(requireActivity())
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_choose_position)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomChoosePosition)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvBottomChoosePosition)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnAppliedBottomChoosePosition)

        // set rv layout
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager

        ivClose?.setOnClickListener {
//            selectedPosition = ""
            dialog.dismiss()
        }

        button?.setOnClickListener {
            if (selectedPosition == "") {
                Toast.makeText(context, "Silahkan pilih posisi", Toast.LENGTH_SHORT).show()
            } else {
                dialog.dismiss()
                binding.tvPositionOvertimeChangeMid.text = selectedPosition
                binding.tvPositionOvertimeChangeMid.setBackgroundResource(R.drawable.bg_spinner_blue)
                binding.tvPositionOvertimeChangeMid.setTextColorRes(R.color.blueInfo)
//                selectedPosition = ""
                page = 0
                isLastPage = false

                // set shimmer effect
                binding.shimmerOvertimeChangeTl.startShimmerAnimation()
                binding.shimmerOvertimeChangeTl.visibility = View.VISIBLE
                binding.rvOvertimeChangeTl.visibility = View.INVISIBLE
                binding.tvEmptyValidationCreateOvertimeChange.visibility = View.GONE

                loadData()
                setObserver()
            }
        }

        val listChoosePosition = ArrayList<String>()
        when(jobLevel) {
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
        recyclerView?.adapter = JobPositionChooserAdapter(listChoosePosition, selectedItemAdapter).also { it.setListener(this) }

        dialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun bottomSheetDatePicker(string: String) {
        val dialog = BottomSheetDialog(requireActivity())
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
        val dialog = BottomSheetDialog(requireActivity())
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
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
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
                    Toast.makeText(requireActivity(), "Pilih tanggal terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    binding.tvDateOvertimeChangeMid.text = "$startDateText - $endDateText"
                    binding.tvDateOvertimeChangeMid.setBackgroundResource(R.drawable.bg_spinner_blue)
                    binding.tvDateOvertimeChangeMid.setTextColorRes(R.color.blueInfo)

                    // set default data
//                    selectedItem = ""
                    startDateText = "Pilih tanggal"
                    endDateText = "Pilih tanggal"
                    page = 0
                    isLastPage = false

                    dialog.dismiss()

                    Handler(Looper.getMainLooper()).postDelayed( {
                        // set shimmer effect
                        binding.shimmerOvertimeChangeTl.startShimmerAnimation()
                        binding.shimmerOvertimeChangeTl.visibility = View.VISIBLE
                        binding.rvOvertimeChangeTl.visibility = View.GONE
                        binding.tvEmptyValidationCreateOvertimeChange.visibility = View.GONE

                        loadData()
                        setObserver()
                    }, 500)
                }
            } else {
                llChooseDate?.visibility = View.INVISIBLE
                binding.tvDateOvertimeChangeMid.text = selectedItem
                binding.tvDateOvertimeChangeMid.setBackgroundResource(R.drawable.bg_spinner_blue)
                binding.tvDateOvertimeChangeMid.setTextColorRes(R.color.blueInfo)
//                selectedItem = ""
                page = 0
                isLastPage = false

                dialog.dismiss()

                Handler(Looper.getMainLooper()).postDelayed( {
                    // set shimmer effect
                    binding.shimmerOvertimeChangeTl.startShimmerAnimation()
                    binding.shimmerOvertimeChangeTl.visibility = View.VISIBLE
                    binding.rvOvertimeChangeTl.visibility = View.GONE
                    binding.tvEmptyValidationCreateOvertimeChange.visibility = View.GONE

                    viewModel.getFilterOvertimeChange(employeeId, projectCode, jabatan, "", "", page)
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

    override fun onItemSelected(item: String) {
        selectedItem = item
    }

    override fun onJobPositionSelected(item: String) {
        selectedPosition = item
        jabatan = item
    }

//    override fun onResume() {
//        super.onResume()
//        setDefaultDataLayout()
//        loadData()
//        setObserver()
//    }

    private fun setDefaultDataLayout() {
        // set default data
        selectedPosition = ""
        jabatan = "Semua Posisi"
        selectedItem = ""
        startDate = ""
        startDateText = "Pilih tanggal"
        endDate = ""
        endDateText = "Pilih tanggal"
        page = 0
        isLastPage = false

        // set default filter layout
        binding.tvPositionOvertimeChangeMid.setBackgroundResource(R.drawable.bg_spinner)
        binding.tvPositionOvertimeChangeMid.setTextColorRes(R.color.black2)

        binding.tvDateOvertimeChangeMid.setBackgroundResource(R.drawable.bg_spinner)
        binding.tvDateOvertimeChangeMid.setTextColorRes(R.color.black2)

        // set shimmer effect
        binding.shimmerOvertimeChangeTl.startShimmerAnimation()
        binding.shimmerOvertimeChangeTl.visibility = View.VISIBLE
        binding.rvOvertimeChangeTl.visibility = View.GONE
        binding.tvEmptyValidationCreateOvertimeChange.visibility = View.GONE
    }

}