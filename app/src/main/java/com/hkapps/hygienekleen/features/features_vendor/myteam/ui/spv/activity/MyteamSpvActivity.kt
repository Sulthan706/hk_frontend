package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMyteamSpvBinding
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.adapter.ViewPagerTeamSpvAdapter
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.teamlead.activity.SearchStaffMyteamTlActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*

class MyteamSpvActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyteamSpvBinding
    private lateinit var viewpagerAdapter: ViewPagerTeamSpvAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private var spvName = CarefastOperationPref.loadString(CarefastOperationPrefConst.MY_TEAM_SPV_NAME, "")
    private var dateParams: String = ""
    private var dateText: String = "Pilih Tanggal"
    private var dtDate: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyteamSpvBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set layout client
        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarTimkuSpv.rlAppbar.setBackgroundResource(R.color.secondary_color)

            binding.abl.setBackgroundResource(R.color.secondary_color)

            // set tab layout & view pager
            binding.tabLayoutClientTimkuSpv.visibility = View.VISIBLE
            binding.tabLayoutTimkuSpv.visibility = View.GONE

            // set tablayout & viewpager
            viewpagerAdapter = ViewPagerTeamSpvAdapter(supportFragmentManager, this)
            this.binding.viewPagerTimkuSpv.adapter = viewpagerAdapter
            this.binding.tabLayoutClientTimkuSpv.setupWithViewPager(this.binding.viewPagerTimkuSpv)
            binding.tabLayoutClientTimkuSpv.tabMode = TabLayout.MODE_SCROLLABLE
            binding.viewPagerTimkuSpv.currentItem = 0
        } else {
            binding.appbarTimkuSpv.rlAppbar.setBackgroundResource(R.color.primary_color)

            // set tab layout & view pager
            binding.tabLayoutTimkuSpv.visibility = View.VISIBLE
            binding.tabLayoutClientTimkuSpv.visibility = View.GONE

            // set tablayout & viewpager
            viewpagerAdapter = ViewPagerTeamSpvAdapter(supportFragmentManager, this)
            this.binding.viewPagerTimkuSpv.adapter = viewpagerAdapter
            this.binding.tabLayoutTimkuSpv.setupWithViewPager(this.binding.viewPagerTimkuSpv)
            binding.tabLayoutTimkuSpv.tabMode = TabLayout.MODE_SCROLLABLE
            binding.viewPagerTimkuSpv.currentItem = 0
        }

        // set appbar layout
        binding.appbarTimkuSpv.tvAppbarTitle.text = when(clickFrom) {
            "Myteam Spv" -> spvName
            "Service" -> "Tim Ku"
            else -> "Error"
        }
        binding.appbarTimkuSpv.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
            dtDate = ""
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.MY_TEAM_TL_ID, 0)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MY_TEAM_TL_NAME, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
        }
        binding.appbarTimkuSpv.ivAppbarDate.visibility = View.GONE
//        binding.appbarTimkuSpv.ivAppbarDate.setOnClickListener {
//            showBottomSheetDialog()
//        }
        binding.appbarTimkuSpv.ivAppbarSearch.visibility = View.GONE
        binding.appbarTimkuSpv.ivAppbarSearch.setOnClickListener {
            val i = Intent(this, SearchStaffMyteamTlActivity::class.java)
            startActivity(i)
        }

    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(this)

        dialog.setContentView(R.layout.layout_bottom_sheets_myteam)
        val tvChooseDate = dialog.findViewById<TextView>(R.id.tv_choose_date_myteam)
        val btnApplied = dialog.findViewById<AppCompatButton>(R.id.btn_applied_myteam)
        val ivClose = dialog.findViewById<ImageView>(R.id.iv_close_date_myteam)

        tvChooseDate?.text = dateText

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }

        // set calendar choose date
        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy"
            val paramsFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val dateParam = SimpleDateFormat(paramsFormat, Locale.US)

            dateParams = dateParam.format(cal.time)

            dateText = sdf.format(cal.time)
            tvChooseDate?.text = dateText
            btnApplied?.setBackgroundResource(R.drawable.btn_green)
        }

        btnApplied?.setOnClickListener {
            if (tvChooseDate?.text == this.getString(R.string.pilih_tanggal)) {
                Toast.makeText(this, "Harap isi tanggal", Toast.LENGTH_LONG).show()
            } else {
                dtDate = dateParams.format(cal.time)
                Toast.makeText(this, "Mengambil data pada tanggal: $dtDate", Toast.LENGTH_LONG).show()

                val i = Intent(this, MyteamSpvActivity::class.java)
                startActivity(i)
                finish()
            }
        }

        tvChooseDate?.setOnClickListener {
            DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        dtDate = ""
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.MY_TEAM_TL_ID, 0)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.MY_TEAM_TL_NAME, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
    }


}