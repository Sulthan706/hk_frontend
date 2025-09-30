package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityClosingAreaBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity.ListShiftChecklistActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.viewmodel.ClosingViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar

class ClosingAreaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityClosingAreaBinding

    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.JOB_LEVEL,"")

    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private val userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val viewModelRkb: MonthlyWorkReportViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkReportViewModel::class.java)
    }

    private val closingViewModel by lazy {
        ViewModelProviders.of(this).get(ClosingViewModel::class.java)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setResult(RESULT_OK)
            finish()
        }
    }

    private var isApprove = false

    private var isNoChief = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClosingAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        initView()

    }

    private fun initView(){
        binding.apply {
            val appBar = "Closing Area"
            appBarClosingArea.tvAppbarTitle.text = appBar

            appBarClosingArea.ivAppbarBack.setOnClickListener {
                setResult(RESULT_OK)
                onBackPressedCallback.handleOnBackPressed()
            }
            onBackPressedDispatcher.addCallback(onBackPressedCallback)
            resultLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK || CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_DIVERTED,false)) {
                    recreate()
                }
            }

            isNoChief = intent.getBooleanExtra("no_chief",false)
            val idDetailEmployeeProject = intent.getIntExtra("idSchedule",0)
            if(idDetailEmployeeProject != 0){
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_DETAIL_EMPLOYEE_PROJECT_VALUE,idDetailEmployeeProject)
            }
            appBarClosingArea.ivAppbarHistory.setOnClickListener {
                startActivity(Intent(this@ClosingAreaActivity,HistoryClosingActivity::class.java))
            }
            btnDiversion.setOnClickListener {
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_ADD_IMAGE_SHIFT_CHECKLIST,false)
                val i = Intent(this@ClosingAreaActivity,DiversionActivity::class.java)
                resultLauncher.launch(i)
            }
            binding.btnTlSeeClosing.setOnClickListener {
                startActivity(Intent(this@ClosingAreaActivity,ListPendingClosingActivity::class.java))
            }
            binding.btnApprove.setOnClickListener {
                val i = Intent(this@ClosingAreaActivity,ApprovalWorkClosingActivity::class.java)
                resultLauncher.launch(i)
            }
            binding.btnSeeClosing.setOnClickListener {
                startActivity(Intent(this@ClosingAreaActivity,ListPendingClosingActivity::class.java).also{
                    it.putExtra("chief",true)
                })
            }

            if(jobLevel.contains("chief",ignoreCase = true)){
                linearTlWasClosing.visibility = View.VISIBLE
                linearApprove.visibility = View.VISIBLE
                closingSpv.visibility = View.VISIBLE
                linearSpv.visibility = View.VISIBLE
                shimmerClosingArea.startShimmerAnimation()
                Handler().postDelayed({
                    getDailyTargetChief()
                },1500)
                binding.btnClosing.setOnClickListener {
                    submitClosingChief()

                }
            }else{
                if(jobLevel.contains("supervisor",ignoreCase = true)){
                    shimmerClosingArea.startShimmerAnimation()
                    linearApprove.visibility = View.VISIBLE
                    Handler().postDelayed({
                        getDailyTargetChief()
                    },1500)
                    binding.btnClosing.setOnClickListener {
                        submitClosingChief()
                    }
                }else{
                    if(idDetailEmployeeProject != 0 && jobLevel.contains("leader",ignoreCase = true)){
                        shimmerClosingArea.startShimmerAnimation()
                        Handler().postDelayed({
                            getDailyTarget(idDetailEmployeeProject)
                        },1500)
                        binding.btnClosing.setOnClickListener {
                            submitClosing(idDetailEmployeeProject)
                        }
                    }else{
                        Toast.makeText(this@ClosingAreaActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                    }
                }
            }


            if(jobLevel.contains("supervisor",ignoreCase = true)){
                closingSpv.visibility = View.VISIBLE
                linearTlWasClosing.visibility = View.VISIBLE
            }


            if(jobLevel.contains("leader",ignoreCase = true) ){
                btnClosing.setOnClickListener {
                    submitClosing(idDetailEmployeeProject)
                }
            }
        }
    }


    private fun submitClosingChief(){
        closingViewModel.submitClosingChief(projectId,getYesterdayDate(),userId)
        closingViewModel.submitClosingChief.observe(this){
            if(it.code == 200){
                if(isNoChief || jobLevel.contains("chief",ignoreCase = true)){
                    startActivity(Intent(this,ClosingSpvActivity::class.java))
                }else{
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.SUCCESS_CLOSING_DIALOG,true)
                    startActivity(Intent(this,ListShiftChecklistActivity::class.java).also{
                        it.putExtra("is_done",true)
                    })
                    finish()
                }
            }else{
                if(it.errorCode != null && it.errorCode == "01"){
                    binding.tvInfo.visibility = View.VISIBLE
                    val textInfo = "Closing belum tersedia"
                    binding.tvInfo.text = textInfo
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvInfo.visibility = View.INVISIBLE
                    }, 2000)
                }else if(it.errorCode != null && it.errorCode == "02"){
                    binding.tvInfo.visibility = View.VISIBLE
                    val textInfo = "Alihkan pekerjaan yang belum dikerjakan atau belum selesai terlebih dahulu"
                    val textInfoNotApprove = "Approve pekerjaan terlebih dahulu"
                    binding.tvInfo.text = if(isApprove) textInfoNotApprove else textInfo
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvInfo.visibility = View.INVISIBLE
                    }, 2000)
                }else if(it.errorCode != null && it.errorCode == "03"){
                    Toast.makeText(this, it.message ?: "Terjadi kesalahan closing", Toast.LENGTH_SHORT).show()
                }else if(it.errorCode != null && it.errorCode == "04"){
                    Toast.makeText(this, "Minta pengawas yang belum closing agar closing terlebih dahulu", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Terjadi kesahalan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun submitClosing(idDetailEmployeeProject : Int){
        closingViewModel.submitClosing(idDetailEmployeeProject,userId)
        closingViewModel.submitClosingModel.observe(this){
            if(it.code == 200){
                if(jobLevel.contains("leader",ignoreCase = true)){
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.SUCCESS_CLOSING_DIALOG,true)
                    startActivity(Intent(this,ListShiftChecklistActivity::class.java).also{
                        it.putExtra("is_done",true)
                    })
                    finish()
                }
                if(jobLevel.contains("chief supervisor",ignoreCase = true)){
                    startActivity(Intent(this,ClosingSpvActivity::class.java))
                }else{
                    if(jobLevel.contains("supervisor",ignoreCase = true)){
                        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.SUCCESS_CLOSING_DIALOG,true)
                        startActivity(Intent(this,ListShiftChecklistActivity::class.java).also{
                            it.putExtra("is_done",true)
                        })
                        finish()
                    }
                }
            }else{
                if(it.errorCode != null && it.errorCode == "01"){
                    binding.tvInfo.visibility = View.VISIBLE
                    val textInfo = "Closing belum tersedia"
                    binding.tvInfo.text = textInfo
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvInfo.visibility = View.INVISIBLE
                    }, 2000)
                }else if(it.errorCode != null && it.errorCode == "02"){
                    binding.tvInfo.visibility = View.VISIBLE
                    val textInfo = "Alihkan pekerjaan yang belum dikerjakan atau belum selesai terlebih dahulu"
                    binding.tvInfo.text = textInfo
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.tvInfo.visibility = View.INVISIBLE
                    }, 2000)
                }else{
                    Toast.makeText(this, it.message ?: "Terjadi kesalahan closing", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun getDailyTargetChief(){
        closingViewModel.getDailyTargetChief(projectId,getYesterdayDate(),userId)
        closingViewModel.detailDailyTargetChief.observe(this){ response ->
            if (response.code == 200) {
                binding.shimmerClosingArea.stopShimmerAnimation()
                binding.shimmerClosingArea.visibility = View.GONE
                binding.linearContent.visibility = View.VISIBLE
                val data = response.data
                val totalTarget = data.totalTarget

                val date = " : ${response.data.date}"
                if(response.data.shift != null){
                    val shift = " : ${response.data.shift.uppercase()}"
                    binding.tvShiftPengawas.text = shift
                }else{
                    binding.tvShiftPengawas.text = ": -"
                }

                binding.tvDate.text = date
                binding.tvPengawas.text = ": $userName"
                val totalSettlement = "$totalTarget Pekerjaan"
                binding.tvTotalSettlement.text = totalSettlement
                if (data.percentTargetsNotFulfilled == 0.0) {
                    binding.tvPercentNotFinish.text = "0.00%"
                } else if (data.percentTargetsNotFulfilled == 100.0) {
                    binding.tvPercentNotFinish.text = "100%"
                } else {
                    val rounded = "${String.format("%.2f", data.percentTargetsNotFulfilled)}%"
                    binding.tvPercentNotFinish.text = rounded
                }
                binding.tvCountNotFinish.text = createSpannable("${data.targetsNotFulfilled}/$totalTarget", "#FF6347")
                binding.tvTotalDiversion.text = if(response.data.totalDiverted != 0) response.data.totalDiverted.toString() else "0"

                if(data.targetsNotFulfilled == 0 && data.targetsNotApproved != 0){
                    isApprove = true
                }

                binding.tvSpvClosing.text = createSpannable("${data.supervisorDoneClosing}/${data.supervisorTotalClosing}", "#00C49A")
                binding.tvTlClosing.text = createSpannable("${data.teamLeadDoneClosing}/${data.teamLeadTotalClosing}", "#00C49A")
                binding.tvDone.text = "Realization"
                if (data.percentTargetsNotApproved == 0.0) {
                    binding.tvPercentNotApprove.text = "0.00%"
                } else if (data.percentTargetsNotApproved == 100.0) {
                    binding.tvPercentNotApprove.text = "100%"
                } else {
                    val rounded = "${String.format("%.2f", data.percentTargetsNotApproved)}%"
                    binding.tvPercentNotApprove.text = rounded
                }

                if (data.percentTargetsRealization == 0.0) {
                    binding.tvPercentFinish.text = "0.00%"
                } else if (data.percentTargetsRealization == 100.0) {
                    binding.tvPercentFinish.text = "100%"
                } else {
                    val rounded = "${String.format("%.2f", data.percentTargetsRealization)}%"
                    binding.tvPercentFinish.text = rounded
                }

                binding.tvCountFinish.text = createSpannable("${data.targetsRealization}/$totalTarget", "#FF6347")
                binding.tvCountNotApprove.text = createSpannable("${data.targetsNotApproved}/$totalTarget", "#FF6347")
            }else{
                binding.shimmerClosingArea.visibility = View.VISIBLE
                binding.linearContent.visibility = View.GONE
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDailyTarget(idDetailEmployeeProject : Int) {
        viewModelRkb.getDetailRkbDailyTarget(idDetailEmployeeProject,userId)
        viewModelRkb.getDetailRkbDailyTargetModel.observe(this) { response ->
            if (response.code == 200) {
                binding.shimmerClosingArea.stopShimmerAnimation()
                binding.shimmerClosingArea.visibility = View.GONE
                binding.linearContent.visibility = View.VISIBLE
                val data = response.data
                val totalTarget = data.totalTarget

                val date = " : ${response.data.date}"
                val pengawas = " : ${response.data.name}"
                if(response.data.shift != null){
                    val shift = " : ${response.data.shift.uppercase()}"
                    binding.tvShiftPengawas.text = shift
                }

                binding.tvDate.text = date
                binding.tvPengawas.text = pengawas
                val totalSettlement = "$totalTarget Pekerjaan"
                binding.tvTotalSettlement.text = totalSettlement
                if (data.percentTargetsNotFulfilled == 0.0) {
                    binding.tvPercentNotFinish.text = "0.00%"
                } else if (data.percentTargetsNotFulfilled == 100.0) {
                    binding.tvPercentNotFinish.text = "100%"
                } else {
                    val rounded = "${String.format("%.2f", data.percentTargetsNotFulfilled)}%"
                    binding.tvPercentNotFinish.text = rounded
                }
                binding.tvCountNotFinish.text = createSpannable("${data.targetsNotFulfilled}/$totalTarget", "#FF6347")
                binding.tvTotalDiversion.text = if(response.data.totalDiverted != 0) response.data.totalDiverted.toString() else "0"

                binding.tvSpvClosing.text = createSpannable("${data.supervisorDoneClosing}/${data.supervisorTotalClosing}", "#00C49A")
                binding.tvTlClosing.text = createSpannable("${data.teamLeadDoneClosing}/${data.teamLeadTotalClosing}", "#00C49A")
                if(jobLevel == "CHIEF SUPERVISOR" || jobLevel == "Chief Supervisor" ){
                    binding.tvDone.text = "Realization"
                    if (data.percentTargetsNotApproved == 0.0) {
                        binding.tvPercentNotApprove.text = "0.00%"
                    } else if (data.percentTargetsNotApproved == 100.0) {
                        binding.tvPercentNotApprove.text = "100%"
                    } else {
                        val rounded = "${String.format("%.2f", data.percentTargetsNotApproved)}%"
                        binding.tvPercentNotApprove.text = rounded
                    }

                    if (data.percentTargetsRealization == 0.0) {
                        binding.tvPercentFinish.text = "0.00%"
                    } else if (data.percentTargetsRealization == 100.0) {
                        binding.tvPercentFinish.text = "100%"
                    } else {
                        val rounded = "${String.format("%.2f", data.percentTargetsRealization)}%"
                        binding.tvPercentFinish.text = rounded
                    }

                    binding.tvCountFinish.text = createSpannable("${data.targetsRealization}/$totalTarget", "#FF6347")
                    binding.tvCountNotApprove.text = createSpannable("${data.targetsNotApproved}/$totalTarget", "#FF6347")

                }else{
                    if(jobLevel == "Supervisor" || jobLevel == "supervisor"){
                        if (data.percentTargetsNotApproved == 0.0) {
                            binding.tvPercentNotApprove.text = "0.00%"
                        } else if (data.percentTargetsNotApproved == 100.0) {
                            binding.tvPercentNotApprove.text = "100%"
                        } else {
                            val rounded = "${String.format("%.2f", data.percentTargetsNotApproved)}%"
                            binding.tvPercentNotApprove.text = rounded
                        }

                        binding.tvCountNotApprove.text = createSpannable("${data.targetsNotApproved}/$totalTarget", "#FF6347")
                    }
                    binding.tvDone.text = "Selesai"
                    if (data.percentTargetsDone == 0.0) {
                        binding.tvPercentFinish.text = "0.00%"
                    } else if (data.percentTargetsDone == 100.0) {
                        binding.tvPercentFinish.text = "100%"
                    } else {
                        val rounded = "${String.format("%.2f", data.percentTargetsDone)}%"
                        binding.tvPercentFinish.text = rounded
                    }
                    binding.tvCountFinish.text = createSpannable("${data.targetsDone}/$totalTarget", "#00C49A")
                }
            }else{
                binding.shimmerClosingArea.visibility = View.VISIBLE
                binding.linearContent.visibility = View.GONE
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createSpannable(text: String, firstPartColor: String): SpannableString {
        val separatorIndex = text.indexOf("/")
        val firstPart = text.substring(0, separatorIndex)
        val secondPart = text.substring(separatorIndex + 1)
        val displayFirstPart = if (firstPart == "0") "0" else firstPart
        val formattedText = "$displayFirstPart/$secondPart"

        val spannableString = SpannableString(formattedText)
        val firstColor = ForegroundColorSpan(Color.parseColor(firstPartColor))
        spannableString.setSpan(firstColor, 0, displayFirstPart.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val blackColor = ForegroundColorSpan(Color.BLACK)
        spannableString.setSpan(blackColor, displayFirstPart.length, formattedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableString
    }

}