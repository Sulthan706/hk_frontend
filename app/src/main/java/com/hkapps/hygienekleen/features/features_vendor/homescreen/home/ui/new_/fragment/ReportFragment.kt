package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentReportBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listSlipGaji.Data
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.PdfViewSalaryActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.ListSlipGajiAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeReportViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.ui.activity.DetailAttendanceReportActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.ui.activity.DetailLateReportActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ReportFragment : Fragment(), ListSlipGajiAdapter.ListSlipGajiCallBack {

    private lateinit var binding: FragmentReportBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var monthText = ""
    private var month = 0
    private var year = 0
    private var monthYearText = ""
    private var linkMonth = 0
    private var linkYear = 0
    private var projectCode = ""
    private var employeeId = 0

    private val viewModel: HomeReportViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(HomeReportViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        // change status bar color
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            activity?.window!!.statusBarColor =
//                ContextCompat.getColor(requireActivity(), R.color.white)
//        }

        // set current month year text
        val currentTime = Calendar.getInstance().time
        monthYearText = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentTime)
        binding.tvRecentMonthReport.text = monthYearText

        // get current month & year
        val calendar: Calendar = Calendar.getInstance()
        month = calendar.get(Calendar.MONTH)+1
        year = calendar.get(Calendar.YEAR)
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        // set on click select month year
        binding.tvRecentMonthReport.setOnClickListener {
            val dialog = MonthYearPickerDialog.Builder(
                requireActivity(),
                R.style.Style_MonthYearPickerDialog_Orange,
                { selectedYear, selectedMonth ->

                    monthToText(selectedMonth+1)
                monthYearText = "$monthText $selectedYear"
                month = selectedMonth+1
                year = selectedYear

                binding.tvRecentMonthReport.text = monthYearText
                viewModel.getReportAttendance(userId, selectedMonth+1, selectedYear)
                },
                currentYear,
                currentMonth
            )
                .setNegativeButton("Batal")
                .setPositiveButton("Oke")
                .setMonthFormat("MMM")
                .build()

            dialog.setTitle("Pilih Bulan dan Tahun")
            dialog.show()
//            val builder: MonthPickerDialog.Builder = MonthPickerDialog.Builder(requireContext(), MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->
//                monthToText(selectedMonth+1)
//                monthYearText = "$monthText $selectedYear"
//                month = selectedMonth+1
//                year = selectedYear
//
//                binding.tvRecentMonthReport.text = monthYearText
//                viewModel.getReportAttendance(userId, selectedMonth+1, selectedYear)
//
//            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
//
//            builder.setActivatedMonth(calendar.get(Calendar.MONTH))
//                .setMinYear(2000)
//                .setActivatedYear(calendar.get(Calendar.YEAR))
//                .setMaxYear(calendar.get(Calendar.YEAR))
//                .setTitle("Pilih Bulan dan Tahun")
//                .build().show()
        }

        // set on click detail attendance report
        binding.rlDetailAttendanceReport.setOnClickListener {
            startActivity(Intent(requireActivity(), DetailAttendanceReportActivity::class.java))
        }

        // set on click detail late report
        binding.tvDetailLateReport.setOnClickListener {
            // save selected month year
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_HOME_REPORT, month)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_HOME_REPORT, year)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MONTH_YEAR_HOME_REPORT, monthYearText)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.EMPLOYEE_ID_HOME_REPORT, userId)

            startActivity(Intent(requireActivity(), DetailLateReportActivity::class.java))
        }

        // load data
        viewModel.getReportAttendance(userId, month, year)
        setObserver()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setObserver() {
        viewModel.isLoading?.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        viewModel.reportAttendanceModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                // set image user
                loadProfilePhoto(it.data.employeePhotoProfile)

                // change date format
                val dateString = it.data.startingFrom
                if (dateString == "-") {
                    binding.tvWorkStartReport.text = "mulai dari $dateString"
                } else {
                    val sdfBefore = SimpleDateFormat("dd-MM-yyyy")
                    val dateParamBefore = sdfBefore.parse(dateString)
                    val sdfAfter = SimpleDateFormat("dd MMM yyyy")
                    val dateStart = sdfAfter.format(dateParamBefore)
                    binding.tvWorkStartReport.text = "mulai dari $dateStart"
                }

                // set user data
                binding.tvUserNameReport.text = it.data.employeeName
                binding.tvJobPositionReport.text = it.data.jobName
                binding.tvProjectReport.text = it.data.projectName
                binding.tvUserLevelReport.text = it.data.levelJabatan

                if (it.data.isSeePayroll == "N") {
                    binding.tvNoSlipGajiReport.visibility = View.VISIBLE
                    binding.tvMonthYearSlipGajiReport.visibility = View.GONE
                } else {
                    binding.tvMonthYearSlipGajiReport.visibility = View.VISIBLE
                    binding.tvNoSlipGajiReport.visibility = View.GONE
                    binding.tvMonthYearSlipGajiReport.text = it.data.previousMonth

                    binding.layout6.setOnClickListener {
                        bottomSheetSlipGaji()
                    }
                }

                // validate badge by job level
                when(it.data.jobRole) {
                    "Operator" -> binding.ivBadgeLevelReport.setImageResource(R.drawable.badge_operator)
                    "Team Leader"-> binding.ivBadgeLevelReport.setImageResource(R.drawable.badge_leader)
                    "Supervisor" -> binding.ivBadgeLevelReport.setImageResource(R.drawable.badge_spv)
                    "Chief Supervisor" -> binding.ivBadgeLevelReport.setImageResource(R.drawable.badge_chief)
                }

                // validate message by attendance percentage
                when (it.data.persentaseKehadiran) {
                    in 0.0..60.00 -> {
                        binding.tvMessageReport.text = "Yuk bisa yuk!"
                        binding.tvSubMessageReport.text = "Tingkatkan terus kehadiranmu."
                    }
                    in 60.00..75.00 -> {
                        binding.tvMessageReport.text = "Kerja bagus!"
                        binding.tvSubMessageReport.text = "Rekap kehadiranmu bulan ini semakin bagus."
                    }
                    in 75.00..90.00 -> {
                        binding.tvMessageReport.text = "Mantab!"
                        binding.tvSubMessageReport.text = "Rekap kehadiranmu bulan ini semakin bagus."
                    }
                    in 90.00..99.99 -> {
                        binding.tvMessageReport.text = "Luar Biasa!"
                        binding.tvSubMessageReport.text = "Rekap kehadiranmu bulan ini semakin bagus."
                    }
                    100.00 -> {
                        binding.tvMessageReport.text = "Luar Biasa!"
                        binding.tvSubMessageReport.text = "Rekap kehadiranmu bulan ini memuaskan."
                    }
                }

                // set data attandance
                binding.tvDaysWorkReport.text = if (it.data.totalHari == 0 || it.data.totalHari == null) {
                    "Hari Kerja: - hari"
                } else {
                    "Hari Kerja: ${it.data.totalHari} hari"
                }
                binding.tvTimeWorkReport.text = if (it.data.totalJamKerja == 0 || it.data.totalJamKerja == null) {
                    "Total Jam Kerja: - jam"
                } else {
                    "Total Jam Kerja: ${it.data.totalJamKerja} jam"
                }
                binding.progressBarReport.setProgressPercentage(it.data.persentaseKehadiran)
                binding.tvPercentageReport.text = if (it.data.persentaseKehadiran == null) {
                    "-%"
                } else {
                    "${String.format("%.2f", it.data.persentaseKehadiran)}%"
                }
                binding.tvPresentReport.text = if (it.data.hadirCount == 0 || it.data.hadirCount == null) {
                    "-"
                } else {
                    "${it.data.hadirCount}"
                }
                binding.tvPermissionReport.text = if (it.data.izinCount == 0 || it.data.izinCount == null) {
                    "-"
                } else {
                    "${it.data.izinCount}"
                }
                binding.tvAbsentReport.text = if (it.data.alpaCount == 0 || it.data.alpaCount == null) {
                    "-"
                } else {
                    "${it.data.alpaCount}"
                }
                binding.tvLateReport.text = if (it.data.terlambatCount == 0 || it.data.terlambatCount == null) {
                    "- kali"
                } else {
                    "${it.data.terlambatCount} kali"
                }
                binding.tvTotLemburGantiReport.text = if (it.data.lemburGantiCount == 0 || it.data.lemburGantiCount == null) {
                    "- hari"
                } else {
                    "${it.data.lemburGantiCount} hari"
                }
                binding.tvTotLemburTagihReport.text = "- hari"

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bottomSheetSlipGaji() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_default_choose)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomDefaultChoose)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitleBottomDefaultChoose)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfoBottomDefaultChoose)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvBottomDefaultChoose)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnAppliedBottomDefaultChoose)
        val tvError = dialog.findViewById<TextView>(R.id.tvErrorBottomDefaultChoose)
        val tvInfoError = dialog.findViewById<TextView>(R.id.tvInfoErrorBottomDefaultChoose)

        // set rv layout
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager

        // set data
        tvTitle?.text = "Daftar Slip Gaji"
        tvInfo?.text = "Pilih slip gaji bulan lalu yang ingin Anda lihat"
        tvInfoError?.text = "silahkan cek kembali bulan depan"

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }

        // load api
        viewModel.getListSlipGaji(userId, month-1, year)
        viewModel.listSlipGajiModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if(it.data.isEmpty()) {
                    // set layout
                    tvError?.text = "Maaf slip gaji bulan lalu tidak tersedia"
                    tvError?.visibility = View.VISIBLE
                    tvInfoError?.visibility = View.VISIBLE
                    tvInfo?.visibility = View.GONE
                    recyclerView?.visibility = View.GONE
                    recyclerView?.adapter = null
                    button?.visibility = View.GONE
                } else {
                    // set layout
                    tvError?.visibility = View.GONE
                    tvInfoError?.visibility = View.GONE
                    tvInfo?.visibility = View.VISIBLE
                    recyclerView?.visibility = View.VISIBLE
                    button?.visibility = View.VISIBLE

                    recyclerView?.adapter = ListSlipGajiAdapter(
                        it.data as ArrayList<Data>
                    ).also { it.setListener(this) }
                }
            } else {
                // set layout
                tvError?.text = "User not found"
                tvError?.visibility = View.VISIBLE
                tvInfoError?.visibility = View.GONE
                tvInfo?.visibility = View.GONE
                recyclerView?.visibility = View.GONE
                recyclerView?.adapter = null
                button?.visibility = View.GONE
            }
        }

        button?.setOnClickListener {
            dialog.dismiss()
            val url = "https://ops.carefast.id/carefast/Payrollgenerate/dataPayrollShow/$projectCode/$employeeId/$linkMonth/$linkYear"
            CarefastOperationPref.saveString(CarefastOperationPrefConst.URL_SALARY, url)

            startActivity(Intent(context, PdfViewSalaryActivity::class.java))
        }

        dialog.show()
    }

    @SuppressLint("UseCompatLoadingForDrawables", "DiscouragedApi")
    private fun loadProfilePhoto(img: String) {
        val url =
            getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imageResource = resources.getIdentifier(uri, null, requireActivity().packageName)
            val res = resources.getDrawable(imageResource)
            binding.ivUserReport.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(requireActivity())
                .load(url)
                .apply(requestOptions)
                .into(binding.ivUserReport)
        }
    }

    private fun monthToText(monthInt: Int) {
        monthText = when(monthInt) {
            1 -> "Januari"
            2 -> "Februari"
            3 -> "Maret"
            4 -> "April"
            5 -> "Mei"
            6 -> "Juni"
            7 -> "Juli"
            8 -> "Agustus"
            9 -> "September"
            10 -> "Oktober"
            11 -> "November"
            12 -> "Desember"
            else -> "error"
        }
    }

    override fun onClickSlipGaji(projectCode: String, employeeId: Int, month: Int, year: Int) {
        this.projectCode = projectCode
        this.employeeId = employeeId
        linkMonth = month
        linkYear = year
    }
}