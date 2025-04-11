package com.hkapps.hygienekleen.features.features_management.service.overtime.ui.fragment

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
import com.hkapps.hygienekleen.databinding.FragmentOvertimeChangeManagementBinding
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.listOvertimeChange.Content
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.activity.CreateOvertimeChangeManagementsActivity
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.activity.OvertimeManagementActivity
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.adapter.OvertimeChangeManagementAdapter
import com.hkapps.hygienekleen.features.features_management.service.overtime.viewModel.OvertimeManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter.JobPositionChooserAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.customcalendar.setTextColorRes
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.adapter.ListChooserPermissionAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OvertimeChangeManagementFragment : Fragment(),
    ListChooserPermissionAdapter.OnItemSelectedCallBack,
    JobPositionChooserAdapter.OnJobPositionCallBack
{

    private lateinit var binding: FragmentOvertimeChangeManagementBinding
    private lateinit var rvAdapter: OvertimeChangeManagementAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private var selectedPosition = ""
    private var jabatan = "Operator"
    private var selectedItem = ""
    private var startDate = ""
    private var startDateText = "Pilih tanggal"
    private var endDate = ""
    private var endDateText = "Pilih tanggal"
    private var page = 0
    private var isLastPage = false

    private val viewModel: OvertimeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeManagementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOvertimeChangeManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (jobLevel == "FM") {
//            binding.tvPositionOvertimeChangeManagement.visibility = View.INVISIBLE
//        } else {
//            binding.tvPositionOvertimeChangeManagement.visibility = View.VISIBLE
//        }

        // set on click filter
        binding.tvDateOvertimeChangeManagement.setOnClickListener {
            bottomSheetChooseDate()
        }

        binding.tvPositionOvertimeChangeManagement.setOnClickListener {
            bottomSheetChoosePosition()
        }

        // set on click create overtime change
        binding.ivCreateOvertimeChangeManagement.setOnClickListener {
//            startActivity(Intent(requireActivity(), CreateOvertimeChangeManagementActivity::class.java))
            startActivity(Intent(requireActivity(), CreateOvertimeChangeManagementsActivity::class.java))
        }

        // set shimmer effect
        binding.shimmerOvertimeChangeManagement.startShimmerAnimation()
        binding.shimmerOvertimeChangeManagement.visibility = View.VISIBLE
        binding.rvOvertimeChangeManagement.visibility = View.GONE
        binding.tvEmptyValidationCreateOvertimeChangeManagement.visibility = View.GONE

        // set layout manager recycler view
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvOvertimeChangeManagement.layoutManager = layoutManager

        // scroll recycler view
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvOvertimeChangeManagement.addOnScrollListener(scrollListener)

        // refresh layout
        binding.swipeOvertimeChangeManagement.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                val i = Intent(requireActivity(), OvertimeManagementActivity::class.java)
                startActivity(i)
                requireActivity().finish()
                requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
            }, 500)
        }

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(requireActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerOvertimeChangeManagement.stopShimmerAnimation()
                        binding.shimmerOvertimeChangeManagement.visibility = View.GONE
//                        binding.rvOvertimeChangeManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        }
        viewModel.listOvertimeChangeResponse.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.rvOvertimeChangeManagement.visibility = View.VISIBLE
                        binding.tvEmptyValidationCreateOvertimeChangeManagement.visibility = View.GONE
                        isLastPage = it.data.last
                        if (page == 0) {
                            rvAdapter = OvertimeChangeManagementAdapter(
                                requireActivity(),
                                it.data.content as ArrayList<Content>
                            )
                            binding.rvOvertimeChangeManagement.adapter = rvAdapter
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
                        binding.rvOvertimeChangeManagement.adapter = null
                        binding.rvOvertimeChangeManagement.visibility = View.GONE
                        binding.tvEmptyValidationCreateOvertimeChangeManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            } else {
                binding.tvEmptyValidationCreateOvertimeChangeManagement.visibility = View.VISIBLE
                binding.rvOvertimeChangeManagement.adapter = null
                Toast.makeText(
                    requireActivity(),
                    "Gagal mengambil data lembur ganti",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListOvertimeChange(userId, jabatan, startDate, endDate, page)
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

        val listChoosePosition = ArrayList<String>()
        listChoosePosition.add("Operator")
        listChoosePosition.add("Team Leader")
        listChoosePosition.add("Supervisor")
        listChoosePosition.add("Chief Supervisor")

        val selectedItemAdapter: Int = when(selectedPosition) {
            "Operator" -> 0
            "Team Leader" -> 1
            "Supervisor" -> 2
            "Chief Supervisor" -> 3
            else -> -1
        }
        recyclerView?.adapter = JobPositionChooserAdapter(listChoosePosition, selectedItemAdapter).also { it.setListener(this) }

        button?.setOnClickListener {
            if (selectedPosition == "") {
                Toast.makeText(context, "Silahkan pilih posisi", Toast.LENGTH_SHORT).show()
            } else {
                binding.tvPositionOvertimeChangeManagement.text = selectedPosition
                binding.tvPositionOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_spinner_blue)
                binding.tvPositionOvertimeChangeManagement.setTextColorRes(R.color.blueInfo)
//                selectedPosition = ""
                page = 0
                isLastPage = false

                // set shimmer effect
                binding.shimmerOvertimeChangeManagement.startShimmerAnimation()
                binding.shimmerOvertimeChangeManagement.visibility = View.VISIBLE
                binding.rvOvertimeChangeManagement.visibility = View.INVISIBLE
                binding.tvEmptyValidationCreateOvertimeChangeManagement.visibility = View.GONE

                viewModel.getListOvertimeChange(userId, jabatan, startDate, endDate, 0)
                setObserver()

                dialog.dismiss()
            }
        }

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
                    binding.tvDateOvertimeChangeManagement.text = "$startDateText - $endDateText"
                    binding.tvDateOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_spinner_blue)
                    binding.tvDateOvertimeChangeManagement.setTextColorRes(R.color.blueInfo)

                    // set default data
//                    selectedItem = ""
                    startDateText = "Pilih tanggal"
                    endDateText = "Pilih tanggal"
                    page = 0
                    isLastPage = false

                    dialog.dismiss()
                    Handler(Looper.getMainLooper()).postDelayed( {
                        // set shimmer effect
                        binding.shimmerOvertimeChangeManagement.startShimmerAnimation()
                        binding.shimmerOvertimeChangeManagement.visibility = View.VISIBLE
                        binding.rvOvertimeChangeManagement.visibility = View.GONE
                        binding.tvEmptyValidationCreateOvertimeChangeManagement.visibility = View.GONE

                        viewModel.getListOvertimeChange(userId, jabatan, startDate, endDate, 0)
                        setObserver()
                    }, 500)
                }
            } else {
                llChooseDate?.visibility = View.INVISIBLE
                binding.tvDateOvertimeChangeManagement.text = selectedItem
                binding.tvDateOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_spinner_blue)
                binding.tvDateOvertimeChangeManagement.setTextColorRes(R.color.blueInfo)
//                selectedItem = ""
                page = 0
                isLastPage = false

                dialog.dismiss()

                Handler(Looper.getMainLooper()).postDelayed( {
                    // set shimmer effect
                    binding.shimmerOvertimeChangeManagement.startShimmerAnimation()
                    binding.shimmerOvertimeChangeManagement.visibility = View.VISIBLE
                    binding.rvOvertimeChangeManagement.visibility = View.GONE
                    binding.tvEmptyValidationCreateOvertimeChangeManagement.visibility = View.GONE

                    viewModel.getListOvertimeChange(userId, jabatan, "", "", 0)
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

    private fun setDefaultDataLayout() {
        // set default data
        selectedPosition = ""
        jabatan = "Operator"
        selectedItem = ""
        startDate = ""
        startDateText = "Pilih tanggal"
        endDate = ""
        endDateText = "Pilih tanggal"
        page = 0
        isLastPage = false

        // set default filter layout
        binding.tvPositionOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_spinner)
        binding.tvPositionOvertimeChangeManagement.setTextColorRes(R.color.black2)

        binding.tvDateOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_spinner)
        binding.tvDateOvertimeChangeManagement.setTextColorRes(R.color.black2)

        // set shimmer effect
        binding.shimmerOvertimeChangeManagement.startShimmerAnimation()
        binding.shimmerOvertimeChangeManagement.visibility = View.VISIBLE
        binding.rvOvertimeChangeManagement.visibility = View.GONE
        binding.tvEmptyValidationCreateOvertimeChangeManagement.visibility = View.GONE
    }

}