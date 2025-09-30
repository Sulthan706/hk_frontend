package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListShiftChecklistBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listShift.Data
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter.ListShiftChecklistAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.ClosingAreaActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.HistoryClosingActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.viewmodel.ClosingViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.DailyTarget
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity.PeriodicVendorHomeActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.adapter.DailyTargetAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import ir.mahozad.android.PieChart
import java.lang.Float
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class ListShiftChecklistActivity : AppCompatActivity(),
    ListShiftChecklistAdapter.ListShiftChecklistCallBack {

    private lateinit var binding: ActivityListShiftChecklistBinding
    private lateinit var adapter: ListShiftChecklistAdapter

    private lateinit var dailyAdapter : DailyTargetAdapter

    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.JOB_LEVEL,"")

    private val isShowDialog = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.SUCCESS_CLOSING_DIALOG,false)

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    private var monthNow: String = ""
    private val viewModel: ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
    }
    private val viewModelRkb: MonthlyWorkReportViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkReportViewModel::class.java)
    }

    private val closingViewModel by lazy {
        ViewModelProviders.of(this).get(ClosingViewModel::class.java)
    }

    private var isDone = false


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListShiftChecklistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isDone = intent.getBooleanExtra("is_done",false)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        if(isDone){
            binding.appbarListShiftChecklist.ivAppbarBack.setOnClickListener {
                startActivity(Intent(this,HomeVendorActivity::class.java))
                finishAffinity()
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, "")
            }
        }else{
            binding.appbarListShiftChecklist.ivAppbarBack.setOnClickListener {
                finish()
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, "")
            }
        }

        // set app bar
        binding.appbarListShiftChecklist.tvAppbarTitle.text = "Konfirmasi Pekerjaan"

        if(isShowDialog){
            binding.appbarListShiftChecklist.ivAppbarBack.setOnClickListener {
                startActivity(Intent(this,HomeVendorActivity::class.java))
                finishAffinity()
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, "")
            }
            showDialogSuccessSubmit()
        }else{
            binding.appbarListShiftChecklist.ivAppbarBack.setOnClickListener {
                super.onBackPressed()
                finish()
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, "")
            }
        }

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK || CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_DIVERTED,false)) {
                recreate()
            }
        }

        // set shimmer effect
        binding.shimmerListShiftChecklist.startShimmerAnimation()
        binding.shimmerListShiftChecklist.visibility = View.VISIBLE
        binding.rvListShiftChecklist.visibility = View.GONE
        binding.flNoInternetListShiftChecklist.visibility = View.GONE
        binding.flNoDataListShiftChecklist.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }

        monthNow = getDateNow()
        binding.tvMonthNowListChecklist.text = monthNow
        binding.btnWorkDetail.setOnClickListener {
            startActivity(Intent(this, PeriodicVendorHomeActivity::class.java))
        }


        if(jobLevel.contains("chief",ignoreCase = true) || jobLevel.contains("supervisor",ignoreCase = true)){
            getDailyTarget()
        }else{
            getListDailyTarget()
        }

        //oncreate
    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }
    private fun getDailyTarget(){
        closingViewModel.getDailyTargetChief(projectId,getYesterdayDate(),userId)
        closingViewModel.detailDailyTargetChief.observe(this){
            if(it.code == 200){
                if(it.data.totalTarget != 0){
                    binding.linearTargetDaily.visibility = View.VISIBLE
                    binding.linearLoading.visibility = View.GONE
                    binding.linearChief.visibility = View.VISIBLE
                    binding.cardResultSend.visibility = View.VISIBLE
                    binding.rvDailyTarget.visibility = View.GONE
                    val text = "${it.data.targetsDone}/${it.data.totalTarget}"
                    binding.tvDateDailytTarget.text = it.data.date
                    val separatorIndex = text.indexOf("/")
                    val spannableString = SpannableString(text)

                    val greenColor = ForegroundColorSpan(Color.parseColor("#00C49A"))
                    spannableString.setSpan(greenColor, 0, separatorIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    val blackColor = ForegroundColorSpan(Color.BLACK)
                    spannableString.setSpan(blackColor, separatorIndex, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.tvFinish.text = spannableString
                    if (it.data.percentTargetsDone == 0.0) {
                        binding.tvFinishPercent.text = "0.00%"
                    } else if (it.data.percentTargetsDone == 100.0) {
                        binding.tvFinishPercent.text = "100%"
                    } else {
                        val rounded = "${String.format("%.2f", it.data.percentTargetsDone)}%"
                        binding.tvFinishPercent.text = rounded
                    }

                    val isClosed = it.data.closingStatus.equals("Closed", ignoreCase = true)

                    if(jobLevel.contains("chief",ignoreCase = true)){
                        binding.btnClosing.visibility = if (isClosed && it.data.fileGenerated && it.data.emailSent) View.GONE else View.VISIBLE
                        binding.btnFinishClosing.visibility = if (isClosed && it.data.fileGenerated && it.data.emailSent) View.VISIBLE else View.GONE
                        binding.btnClosing.setOnClickListener {
                            val i = Intent(this,ClosingAreaActivity::class.java)
                            resultLauncher.launch(i)
                        }
                    }else{
                       if(it.data.chiefNotExisted){
                           binding.btnClosing.visibility = if (isClosed && it.data.fileGenerated && it.data.emailSent) View.GONE else View.VISIBLE
                           binding.btnFinishClosing.visibility = if (isClosed && it.data.fileGenerated && it.data.emailSent) View.VISIBLE else View.GONE
                           binding.btnClosing.setOnClickListener {
                               val i = Intent(this,ClosingAreaActivity::class.java)
                               i.putExtra("no_chief",true)
                               resultLauncher.launch(i)
                           }
                       }else{
                           binding.btnClosing.visibility = if (isClosed) View.GONE else View.VISIBLE
                           binding.btnFinishClosing.visibility = if (isClosed) View.VISIBLE else View.GONE
                           binding.btnClosing.setOnClickListener {
                               val i = Intent(this,ClosingAreaActivity::class.java)
                               resultLauncher.launch(i)
                           }
                       }
                    }
                    binding.btnFinishClosing.setOnClickListener {
                        startActivity(Intent(this,HistoryClosingActivity::class.java))
                    }
                }else{
                    binding.linearTargetDaily.visibility = View.GONE
                    binding.linearLoading.visibility = View.GONE
                    binding.linearChief.visibility = View.GONE
                    binding.cardResultSend.visibility = View.GONE
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data closing", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getListDailyTarget(){
        viewModelRkb.getRkbListDailyTarget(userId,projectId)
        viewModelRkb.getListRkbDailyTargetModel.observe(this){
            if(it.code == 200){
                val datas = it.data.filter { it.totalTarget != 0 }
                if(datas.isNotEmpty()){
                    binding.linearTargetDaily.visibility = View.VISIBLE
                    binding.tvDateDailytTarget.text = it.data[it.data.lastIndex].date
                    binding.linearLoading.visibility = View.GONE
                    binding.rvDailyTarget.visibility = View.VISIBLE
                    val isChief = if(jobLevel.contains("chief",ignoreCase = true)) true else false
                    dailyAdapter = DailyTargetAdapter(datas,isChief,object : DailyTargetAdapter.OnClickDailyTarget{
                        override fun onClickDetail(data: DailyTarget) {
                            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_DIVERTED,false)
                            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_ADD_IMAGE_SHIFT_CHECKLIST,false)

                            val i = Intent(this@ListShiftChecklistActivity,ClosingAreaActivity::class.java).also{
                                it.putExtra("idSchedule",data.idDetailEmployeeProject)
                            }
                            resultLauncher.launch(i)
                        }

                        override fun isActualSchedule(isActualSchedule: Boolean,data : DailyTarget) {
                            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_DETAIL_EMPLOYEE_PROJECT,data.idDetailEmployeeProject)
                            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_ID_DETAIL_EMPLOYEE_PROJECT,isActualSchedule)
                        }

                        override fun showToast() {
                            binding.tvInfo.visibility = View.VISIBLE
                            val textInfo = "Anda belum bisa closing. Lakukan saat jam shift selesai, sebelum absen pulang."
                            binding.tvInfo.text = textInfo
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.tvInfo.visibility = View.INVISIBLE
                            }, 2000)
                        }

                        override fun onClickHistory() {
                            startActivity(Intent(this@ListShiftChecklistActivity,HistoryClosingActivity::class.java))
                        }
                    })
                    binding.rvDailyTarget.apply {
                        adapter = dailyAdapter
                        layoutManager = LinearLayoutManager(this@ListShiftChecklistActivity)
                    }
                }else{
                    binding.cardResultSend.visibility = View.GONE
                    binding.closingShift.visibility = View.GONE
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data ", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getDateNow(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) // Month is 0-based

        return formatDate(year, month)
    }

    private fun formatDate(year: Int, month: Int): String {
        val locale = Locale("id", "ID")
        val monthFormat = SimpleDateFormat("MMMM", locale)
        val monthName = monthFormat.format(Date(year - 1900, month, 1))

        return "$monthName $year"
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    viewIsOnline()
                    return true
                }
            }
        } else {
            noInternetState()
            return true
        }
        return false
    }

    private fun noInternetState() {
        binding.shimmerListShiftChecklist.visibility = View.GONE
        binding.rvListShiftChecklist.visibility = View.GONE
        binding.flNoDataListShiftChecklist.visibility = View.GONE
        binding.flNoInternetListShiftChecklist.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListShiftChecklistActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerListShiftChecklist.startShimmerAnimation()
        binding.shimmerListShiftChecklist.visibility = View.VISIBLE
        binding.rvListShiftChecklist.visibility = View.GONE
        binding.flNoInternetListShiftChecklist.visibility = View.GONE
        binding.flNoDataListShiftChecklist.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListShiftChecklist.layoutManager = layoutManager

        loadData()
        setObserver()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {

        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListShiftChecklist.stopShimmerAnimation()
                        binding.shimmerListShiftChecklist.visibility = View.GONE
                        binding.rvListShiftChecklist.visibility = View.VISIBLE
                        binding.llSummaryCardShiftChecklist.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listShiftResponseModel.observe(this) {
            if (it.code == 200) {
                adapter = ListShiftChecklistAdapter(
                    this,
                    it.data as ArrayList<Data>
                ).also { it.setListener(this) }
                binding.rvListShiftChecklist.adapter = adapter
            }
        }
        //rkb
        viewModelRkb.getHomeRkbViewModel().observe(this) {
            if (it.code == 200) {


                binding.tvTotalWorkMonthly.text =
                    if (it.data.totalTarget == 0) "-" else it.data.totalTarget.toString()

                val htmlContentDaily =
                    "<font color='#00BD8C'>${it.data.dailyDone}</font> / <font color='#2B5281'>${it.data.dailyTotal}</font>"
                binding.tvDailyWorkMonthly.text =
                    Html.fromHtml(htmlContentDaily, Html.FROM_HTML_MODE_COMPACT)
                val htmlContentWeekly =
                    "<font color='#00BD8C'>${it.data.weeklyDone}</font> / <font color='#2B5281'>${it.data.weeklyTotal}</font>"
                binding.tvWeeklyWorkMonthly.text =
                    Html.fromHtml(htmlContentWeekly, Html.FROM_HTML_MODE_COMPACT)
                val htmlContentMonthly =
                    "<font color='#00BD8C'>${it.data.monthlyDone}</font> / <font color='#2B5281'>${it.data.monthlyTotal}</font>"
                binding.tvMonthlyWorkMonthly.text =
                    Html.fromHtml(htmlContentMonthly, Html.FROM_HTML_MODE_COMPACT)

                //ba
                val baWeekly =
                    "<font color='#00BD8C'>${it.data.baWeeklyDone}</font> / <font color='#2B5281'>${it.data.baWeeklyTotal}</font>"
                binding.tvWeeklyBaMonthly.text =
                    Html.fromHtml(baWeekly, Html.FROM_HTML_MODE_COMPACT)

                val baMonthly =
                    "<font color='#00BD8C'>${it.data.baMonthlyDone}</font> / <font color='#2B5281'>${it.data.baMonthlyTotal}</font>"
                binding.tvMonthlyBaMonthly.text =
                    Html.fromHtml(baMonthly, Html.FROM_HTML_MODE_COMPACT)

                val totalRemaining = it.data.totalTarget.toFloat()

                val dailySliceValue = (it.data.dailyDone.toFloat() / totalRemaining) * 100f
                val weeklySliceValue = (it.data.weeklyDone.toFloat() / totalRemaining) * 100f
                val monthlySliceValue = (it.data.monthlyDone.toFloat() / totalRemaining) * 100f

                val roundSliceDaily = if (Float.isNaN(dailySliceValue)) 0f else dailySliceValue.roundToInt() / 100f
                val roundSliceWeekly = if (Float.isNaN(weeklySliceValue)) 0f else weeklySliceValue.roundToInt() / 100f
                val roundSliceMonthly = if (Float.isNaN(monthlySliceValue)) 0f else monthlySliceValue.roundToInt() / 100f
                binding.tvTotalPercentage.text = if (Float.isNaN(it.data.realizationInPercent.toFloat())) "0%" else "${it.data.realizationInPercent}%"

                val totalRealization = it.data.realizationInPercent.toFloat()
                val summaryNotRealization = 100f
                val resultSumarryNotRealization = summaryNotRealization - totalRealization
                val countNotRealization = if (Float.isNaN(resultSumarryNotRealization)) 0f else resultSumarryNotRealization.roundToInt() / 100f


                val pieChart = binding.pieChart
                pieChart.isAnimationEnabled = true
                pieChart.isLegendEnabled = false
                pieChart.holeRatio = 0.50f
//                pieChart.centerLabel = "${it.data.realizationInPercent}%"
                pieChart.centerLabelColor = Color.BLACK
                pieChart.isCenterLabelEnabled = false
                val slices = mutableListOf<PieChart.Slice>()

                if (roundSliceDaily > 0) {
                    slices.add(PieChart.Slice(roundSliceDaily, Color.parseColor("#2D9CDB")))
                }

                if (roundSliceWeekly > 0) {
                    slices.add(PieChart.Slice(roundSliceWeekly, Color.parseColor("#F47721")))
                }

                if (roundSliceMonthly > 0) {
                    slices.add(PieChart.Slice(roundSliceMonthly, Color.parseColor("#9B51E0")))
                }

                if (countNotRealization > 0) {
                    slices.add(PieChart.Slice(countNotRealization, Color.LTGRAY))
                }

                pieChart.slices = slices


            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModelRkb.getHomeRkb(userId, projectId)
        viewModel.getListShift(projectId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(isDone){
            startActivity(Intent(this,HomeVendorActivity::class.java))
            finishAffinity()
        }else{
            finish()
        }

        if(CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.SUCCESS_CLOSING_DIALOG,false)){
            startActivity(Intent(this,HomeVendorActivity::class.java))
            finishAffinity()
        }else{
            finish()
        }

        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, "")
    }

    override fun onClickShift(shiftId: Int, shiftName: String, shiftDesc: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, shiftId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, shiftName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_DESC, shiftDesc)
        val i = Intent(this, ListAreaOperationalChecklist::class.java)
        startActivity(i)
    }

    private fun showDialogSuccessSubmit() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.layout_dialog_success_generate_file)
        val btnOke = dialog.findViewById<AppCompatButton>(R.id.btn_next_send_email)
        val tv = dialog.findViewById<TextView>(R.id.text_success)
        tv.text = "Anda sudah melakukan closing hari ini. Silahkan kembali besok."
        btnOke.text = "Kembali"
        btnOke?.setOnClickListener {
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.SUCCESS_CLOSING_DIALOG,false)
            dialog.dismiss()
        }

        dialog.show()
    }

    companion object {
        var TAG = "agri"
    }

}
