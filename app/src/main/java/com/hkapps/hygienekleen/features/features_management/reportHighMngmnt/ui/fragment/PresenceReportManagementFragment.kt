package com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentPresenceReportManagementBinding
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.attendanceReportManagement.Content
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.ui.adapter.PresenceReportAdapter
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.viewmodel.ReportHighViewModel
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class PresenceReportManagementFragment : Fragment() {

    private lateinit var binding: FragmentPresenceReportManagementBinding
    private lateinit var rvAdapter: PresenceReportAdapter

    private var branchCode = ""
    private var projectCode = ""
    private var startAt = ""
    private var endAt = ""
    private var startAtTxt = ""
    private var endAtTxt = ""
    private var order = "DESC"
    private var whatToOrder = "HADIR"
    private var page = 0
    private val size = 10
    private var isLastPage = false
    private var countReport = 0
    private var loadingDialog: Dialog? = null
    val projectValue = ArrayList<String>()

    private val viewModel: ReportHighViewModel by lazy {
        ViewModelProviders.of(this).get(ReportHighViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPresenceReportManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set default period
        val dateFormatFirst = SimpleDateFormat("yyyy-MM-dd")
        val dateTxtFormatFirst = SimpleDateFormat("dd MMM yyyy")
        val calFirst = Calendar.getInstance()
        calFirst.set(Calendar.DAY_OF_MONTH, 1)
        startAt = dateFormatFirst.format(calFirst.time)
        startAtTxt = dateTxtFormatFirst.format(calFirst.time)

        val currentDate = dateFormatFirst.format(Date())
        val currentDateTxt = dateTxtFormatFirst.format(Date())
        val dateFormatYesterday = SimpleDateFormat("yyyy-MM-dd")
        val dateTxtFormatYesterday = SimpleDateFormat("dd MMM yyyy")
        val calYesterday = Calendar.getInstance()
        calYesterday.add(Calendar.DATE, -1)
        endAt = if (currentDate == startAt) {
            startAt
        } else {
            dateFormatYesterday.format(calYesterday.time)
        }
        endAtTxt = if (currentDateTxt == startAtTxt) {
            startAtTxt
        } else {
            dateTxtFormatYesterday.format(calYesterday.time)
        }

        binding.tvPeriodPresenceReportManagement.text = "$startAtTxt - $endAtTxt"

        // choose range date
        val picker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(
            MaterialDatePicker.thisMonthInUtcMilliseconds(),
            MaterialDatePicker.todayInUtcMilliseconds()
        )).build()
        binding.rlPeriodPresenceReportManagement.setOnClickListener {
            picker.show(parentFragmentManager, "rangeDatePickerTag")
            picker.addOnPositiveButtonClickListener {
                val calendarFirst = Calendar.getInstance()
                calendarFirst.timeInMillis = it.first!!
                val calendarSecond = Calendar.getInstance()
                calendarSecond.timeInMillis = it.second!!

                val sdf = SimpleDateFormat("yyyy-MM-dd")
                startAt = sdf.format(calendarFirst.time)
                endAt = sdf.format(calendarSecond.time)

                page = 0
                isLastPage = false
                showLoading(getString(R.string.loading_string_progress))

                val firstDate = android.text.format.DateFormat.format("dd MMM yyyy", calendarFirst) as String
                val secondDate = android.text.format.DateFormat.format("dd MMM yyyy", calendarSecond) as String
                val selectedRangeDate = "$firstDate - $secondDate"

                binding.tvPeriodPresenceReportManagement.text = if (firstDate == secondDate) {
                    firstDate
                } else {
                    selectedRangeDate
                }
            }
        }

        // spinner list branch
        val branchValue = ArrayList<String>()
        branchValue.add("All Branch")
        val adapterBranch = ArrayAdapter(requireContext(), R.layout.spinner_item, branchValue)
        adapterBranch.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerBranchPresenceReportManagement.adapter = adapterBranch

        viewModel.getListBranchAttendanceReport()
        viewModel.branchesAttendanceReportResponse.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                val length = it.data.size
                for (i in 0 until length) {
                    branchValue.add(it.data[i].branchName)
                }

                binding.spinnerBranchPresenceReportManagement.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        projectValue.clear()
                        projectValue.add("All Project")

                        if (position == 0) {
                            branchCode = ""
                            projectCode = ""
                            binding.spinnerProjectPresenceReportManagement.setSelection(0)
                        } else {
                            branchCode = it.data[position-1].branchCode
                            projectCode = ""
                            binding.spinnerProjectPresenceReportManagement.setSelection(0)
                            viewModel.getListProjectAttendanceReport(it.data[position-1].branchCode)
                        }
                        page = 0
                        isLastPage = false
                        showLoading(getString(R.string.loading_string_progress))
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            }
        }

        // spinner list project
        projectValue.add("All Project")
        val adapterProject = ArrayAdapter(requireContext(), R.layout.spinner_item, projectValue)
        adapterProject.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerProjectPresenceReportManagement.adapter = adapterProject
        viewModel.projectAttendanceReportResponse.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                val length = it.data.size
                for (i in 0 until length) {
                    projectValue.add(it.data[i].projectName)
                }

                binding.spinnerProjectPresenceReportManagement.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        projectCode = if (position == 0) {
                            ""
                        } else {
                            it.data[position-1].projectCode
                        }
                        page = 0
                        isLastPage = false
                        showLoading(getString(R.string.loading_string_progress))
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            }
        }

        // spinner sort by
        val sortByValue = resources.getStringArray(R.array.sortBy)
        val spinnerAdapterSort = ArrayAdapter(requireContext(), R.layout.spinner_item, sortByValue)
        spinnerAdapterSort.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerSortPresenceReportManagement.adapter = spinnerAdapterSort
        binding.spinnerSortPresenceReportManagement.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                order = when (position) {
                    0 -> "DESC"
                    1 -> "ASC"
                    else -> ""
                }
                page = 0
                isLastPage = false
                showLoading(getString(R.string.loading_string_progress))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        // set recycler view
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvTablePresenceReportManagement.layoutManager = layoutManager

        // set button next & prev page
        binding.ivNextPresenceReportManagement.setOnClickListener {
            loadingDialog = CommonUtils.showLoadingDialog(requireContext(), getString(R.string.loading_string_progress))
            Handler(Looper.getMainLooper()).postDelayed( {
                if (!isLastPage) {
                    page++
                    loadData()
                } else {
                    Toast.makeText(context, "Last Page", Toast.LENGTH_SHORT).show()
                }
            }, 500)
        }
        binding.ivPrevPresenceReportManagement.setOnClickListener {
            loadingDialog = CommonUtils.showLoadingDialog(requireContext(), getString(R.string.loading_string_progress))
            Handler(Looper.getMainLooper()).postDelayed( {
                if (page != 0 ) {
                    page--
                    loadData()
                } else {
                    Toast.makeText(context, "First Page", Toast.LENGTH_SHORT).show()
                }
            }, 500)
        }

        // load data
        loadData()
        setObserver()
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(requireContext(), loadingText)
        loadData()
    }

    private fun loadData() {
        viewModel.getListAttendanceReportHigh(branchCode, projectCode, startAt, endAt, order, whatToOrder, page, size)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun setObserver() {
        viewModel.attendanceReportHighResponse.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                hideLoading()

                // set pagable
                val pageStart = it.data.pageable.offset + 1
                val pageEnd = it.data.pageable.offset + it.data.numberOfElements

//                binding.tvPagePresenceReportManagement.text =
//                    "Showing ${it.data.pageable.offset + 1}-${it.data.pageable.offset + it.data.numberOfElements} of $countReport"

                viewModel.getCountAttendanceReportHigh(branchCode, projectCode, startAt, endAt)
                viewModel.countAttendanceReportResponse.observe(viewLifecycleOwner) {
                    if (it.code == 200) {
                        countReport = it.data
                        binding.tvPagePresenceReportManagement.text = "Showing $pageStart-$pageEnd of ${it.data}"
                    } else {
                        countReport = 0
                        binding.tvPagePresenceReportManagement.text = "Showing $pageStart-$pageEnd of 0"
                    }
                }

                isLastPage = it.data.last
                if (page == 0) {
                    rvAdapter = PresenceReportAdapter(
                        it.data.content as ArrayList<Content>,
                        it.data.pageable.offset
                    )
                    binding.rvTablePresenceReportManagement.adapter = rvAdapter
                } else {
                    rvAdapter.listPresence.clear()
                    for (i in 0 until it.data.content.size) {
                        rvAdapter.listPresence.add(it.data.content[i])
                    }
                    rvAdapter.notifyDataSetChanged()
                }
            } else {
                hideLoading()
                Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

}