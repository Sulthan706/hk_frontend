package com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMyTeamClientsBinding
import com.hkapps.hygienekleen.features.features_client.myteam.viewmodel.MyTeamClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import java.text.SimpleDateFormat
import java.util.*


class MyTeamClientsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMyTeamClientsBinding
    //viewmodel
    private val viewModel: MyTeamClientViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamClientViewModel::class.java)
    }
    private var loadingDialog: Dialog? = null
    //val
    private var date = null
    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMyTeamClientsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        binding.rlBtnBackMyTeamClients.setOnClickListener {
            onBackPressed()
            finish()
        }
        val fabColorNormal =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))

        binding.rlBtnBackMyTeamClients.apply {
            backgroundTintList = fabColorNormal
            setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN)
        }

        binding.rlBtnPengawasDetail.setOnClickListener {
            val i = Intent(this, MyTeamClientsListActivity::class.java)
            startActivity(i)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.ROLE_CLIENT, "Pengawas")
        }
        binding.rlBtnOperatorDetail.setOnClickListener {
            val i = Intent(this, MyTeamClientsListActivity::class.java)
            startActivity(i)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.ROLE_CLIENT, "Operator")
        }
//        binding.rlBtnCFmanajemenDetail.setOnClickListener {
//            val i = Intent(this, MyTeamClientsListActivity::class.java)
//            startActivity(i)
//            CarefastOperationPref.saveString(CarefastOperationPrefConst.ROLE_CLIENT, "Management")
//        }
        binding.rlBtnHistoryMyTeamClients.setOnClickListener {
            val i = Intent(this, MyTeamClientHistoryAttendanceActivity::class.java)
            startActivity(i)
        }
        binding.rlBtnScheduleCfteam.setOnClickListener {
            val i = Intent(this, MyTeamClientScheduleActivity::class.java)
            startActivity(i)
        }

        setObserver()
        showLoading("Loading...")
        //oncreate
    }
    //fun
    @SuppressLint("SimpleDateFormat")
    private fun setObserver(){
        viewModel.getListCountCfTeam(projectId)
        viewModel.getCountCfTeamViewModel().observe(this, Observer {
            if (it.code == 200){
                binding.tvCountTotalCfTeam.text = if (it.data.countAllPeople == 0){
                    "-"
                } else {
                    "${it.data.countAllPeople} Orang"
                }
                binding.tvCountPengawasTitle.text = if (it.data.countPengawas == 0){
                    "-"
                } else {
                    "${it.data.countPengawas} Orang"
                }
                binding.tvCountOperatorTitle.text = if (it.data.countOperator == 0){
                    "-"
                } else {
                    "${it.data.countOperator} Orang"
                }
                binding.tvCountCFManajemenTitle.text = if (it.data.countCfManagement == 0){
                    "-"
                } else {
                    "${it.data.countCfManagement} Orang"
                }

                val timeZone = TimeZone.getDefault().getOffset(Date().time) / 3600000.0
                val tz = when(timeZone.toString()){
                    "7.0" -> " WIB"
                    "8.0" -> " WITA"
                    "9.0" -> " WIT"
                    else -> ""
                }
                val sdf = SimpleDateFormat("dd MMM yyyy, kk:mm" )
                val currentDate = sdf.format(Date()) + tz

                binding.tvDateNowCfTeam.text = currentDate

                //count libur dll
                binding.tvCountCfTeamLibur.text = if (it.data.countLibur == 0 ){
                    "-"
                } else {
                    "${it.data.countLibur}"
                }
                binding.tvCountCfTeamBelumAbsen.text = if (it.data.countBelumabsen == 0 ){
                    "-"
                } else {
                    "${it.data.countBelumabsen}"
                }
                binding.tvCountCfTeamSedangBekerja.text = if (it.data.countSedangbekerja == 0 ){
                    "-"
                } else {
                    "${it.data.countSedangbekerja}"
                }
                binding.tvCountCfTeamSelesaiBekerja.text = if (it.data.countSelesaibekerja == 0 ){
                    "-"
                } else {
                    "${it.data.countSelesaibekerja}"
                }
                hideLoading()




            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
}