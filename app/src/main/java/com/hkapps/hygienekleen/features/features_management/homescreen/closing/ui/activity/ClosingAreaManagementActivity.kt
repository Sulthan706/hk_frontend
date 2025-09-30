package com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.activity

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
import androidx.lifecycle.ViewModelProvider
import com.hkapps.hygienekleen.databinding.ActivityClosingAreaManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.viewmodel.ClosingManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar

class ClosingAreaManagementActivity : AppCompatActivity() {

    private lateinit var binding : ActivityClosingAreaManagementBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setResult(RESULT_OK)
            startActivity(Intent(this@ClosingAreaManagementActivity,DailyClosingManagementActivity::class.java).also{
                finish()
            })
        }
    }

    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")

    private val closingManagementViewModel by lazy{
        ViewModelProvider(this)[ClosingManagementViewModel::class.java]
    }

    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")


    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClosingAreaManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        initView()

    }

    private fun initView(){

        binding.apply {
            val appBar = "Closing Project"
            appBarClosingArea.tvAppbarTitle.text = appBar

            appBarClosingArea.ivAppbarBack.setOnClickListener {
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

            appBarClosingArea.ivAppbarHistory.setOnClickListener {
                startActivity(Intent(this@ClosingAreaManagementActivity, HistoryClosingManagementActivity::class.java))
            }

            binding.shimmerClosingArea.startShimmerAnimation()
            val i = intent.getBooleanExtra("isYesterday",false)
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_YESTERDAY,i)
            Handler().postDelayed({
                getDataClosing(i)
            },1000)

            btnDiversion.setOnClickListener {
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_ADD_IMAGE_SHIFT_CHECKLIST,false)
                val x = Intent(this@ClosingAreaManagementActivity, DiversionManagementActivity::class.java)
                x.putExtra("isYesterday",i)
                resultLauncher.launch(x)
            }
            binding.btnTlSeeClosing.setOnClickListener {
                startActivity(Intent(this@ClosingAreaManagementActivity, ListClosingProjectManagementActivity::class.java).also{
                    it.putExtra("isYesterday",i)
                    it.putExtra("team_leader",true)
                })
            }
            binding.btnSeeClosing.setOnClickListener {
                startActivity(Intent(this@ClosingAreaManagementActivity,ListClosingProjectManagementActivity::class.java).also{
                    it.putExtra("isYesterday",i)
                })

            }

            binding.btnClosing.setOnClickListener {
                submitClosing(i)
            }
        }
    }

    private fun getDataClosing(isYesterday : Boolean){
        if(isYesterday){
            closingManagementViewModel.getDetailDailyTarget(projectId,getYesterdayDate())
        }else{
            closingManagementViewModel.getDetailDailyTarget(projectId,getTwoYesterdayDate())
        }
        closingManagementViewModel.detailDailyTargetModel.observe(this){ response ->
            if(response.code == 200){
                binding.shimmerClosingArea.stopShimmerAnimation()
                binding.linearContent.visibility = View.VISIBLE
                binding.shimmerClosingArea.visibility = View.GONE
                val data = response.data
                val totalTarget = data.totalTarget

                binding.btnApprove.setOnClickListener {
                    val i = Intent(this,ApprovalWorkManagementActivity::class.java)
                    i.putExtra("isYesterday",isYesterday)
                    resultLauncher.launch(i)
                }

                val date = " : ${response.data.date}"
                val pengawas = " : $userName"
                if(response.data.shift != null){
                    val shift = " : ${response.data.shift.uppercase()}"
                    binding.tvShiftPengawas.text = shift
                }else{
                    binding.tvShiftPengawas.text = ": - "
                }

                binding.tvDate.text = date
                binding.tvPengawas.text = pengawas
                val totalSettlement = "$totalTarget Pekerjaan"

                if (data.percentTargetsNotFulfilled == 0.0) {
                    binding.tvPercentNotFinish.text = "0.00%"
                } else if (data.percentTargetsNotFulfilled == 100.0) {
                    binding.tvPercentNotFinish.text = "100%"
                } else {
                    val rounded = "${String.format("%.2f", data.percentTargetsNotFulfilled)}%"
                    binding.tvPercentNotFinish.text = rounded
                }
                binding.tvTotalSettlement.text = totalSettlement
                binding.tvCountNotFinish.text = createSpannable("${data.targetsNotFulfilled}/$totalTarget", "#FF6347")
                binding.tvTotalDiversion.text = if(response.data.totalDiverted != 0) response.data.totalDiverted.toString() else "0"

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
                binding.tvCountNotApprove.text = createSpannable("${data.targetsNotApproved}/${data.totalTarget}", "#FF6347")
                binding.tvCountFinish.text = createSpannable("${data.targetsRealization}/$totalTarget", "#FF6347")

            }else{
                binding.linearContent.visibility = View.GONE
                binding.shimmerClosingArea.visibility = View.VISIBLE
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitClosing(isYesterday: Boolean){
        if(isYesterday){
            closingManagementViewModel.submitClosing(projectId,getYesterdayDate(),userId)
        }else{
            closingManagementViewModel.submitClosing(projectId,getTwoYesterdayDate(),userId)
        }
        closingManagementViewModel.submitClosingModel.observe(this){
            if(it.code == 200){
                startActivity(Intent(this,ClosingManagementActivity::class.java))
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
                    val textInfo = "Alihkan pekerjaan yang belum dikerjakan atau approve pekerjaan terlebih dahulu"
                    binding.tvInfo.text = textInfo
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

    private fun createSpannable(text: String, firstPartColor: String): SpannableString {
        val separatorIndex = text.indexOf("/")
        val firstPart = text.substring(0, separatorIndex)
        val secondPart = text.substring(separatorIndex + 1)
        val displayFirstPart = if (firstPart == "0") "-" else firstPart
        val formattedText = "$displayFirstPart/$secondPart"

        val spannableString = SpannableString(formattedText)
        val firstColor = ForegroundColorSpan(Color.parseColor(firstPartColor))
        spannableString.setSpan(firstColor, 0, displayFirstPart.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val blackColor = ForegroundColorSpan(Color.BLACK)
        spannableString.setSpan(blackColor, displayFirstPart.length, formattedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableString
    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun getTwoYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -2)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }
}