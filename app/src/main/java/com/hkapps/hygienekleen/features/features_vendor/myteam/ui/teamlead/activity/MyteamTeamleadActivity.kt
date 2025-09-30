package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.teamlead.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMyteamTeamleadBinding
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.teamlead.adapter.ViewPagerMyteamAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.ConnectionTimeoutFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*

class MyteamTeamleadActivity : AppCompatActivity(), NoInternetConnectionCallback {

    private lateinit var binding: ActivityMyteamTeamleadBinding
    private lateinit var viewPagerAdapter: ViewPagerMyteamAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val teamleadName = CarefastOperationPref.loadString(CarefastOperationPrefConst.MY_TEAM_TL_NAME, "")
    private var dataNoInternet: String = "Internet"
    private var dateParams: String = ""
    private var dateText: String = "Pilih Tanggal"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyteamTeamleadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set layout client
        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarTimkuTeamlead.rlAppbar.setBackgroundResource(R.color.secondary_color)

            binding.abl.setBackgroundResource(R.color.secondary_color)

            // set tab layout & view pager
            binding.tabLayoutClientTimku.visibility = View.VISIBLE
            binding.tabLayoutTimku.visibility = View.GONE

            // set tablayout & viewpager view
            viewPagerAdapter = ViewPagerMyteamAdapter(supportFragmentManager, this)
            this.binding.viewPagerTimku.adapter = viewPagerAdapter
            this.binding.tabLayoutClientTimku.setupWithViewPager(this.binding.viewPagerTimku)
            binding.tabLayoutClientTimku.tabMode = TabLayout.MODE_SCROLLABLE
            binding.viewPagerTimku.currentItem = 0
        } else {
            binding.appbarTimkuTeamlead.rlAppbar.setBackgroundResource(R.color.primary_color)

            // set tab layout & view pager
            binding.tabLayoutTimku.visibility = View.VISIBLE
            binding.tabLayoutClientTimku.visibility = View.GONE

            // set tablayout & viewpager view
            viewPagerAdapter = ViewPagerMyteamAdapter(supportFragmentManager, this)
            this.binding.viewPagerTimku.adapter = viewPagerAdapter
            this.binding.tabLayoutTimku.setupWithViewPager(this.binding.viewPagerTimku)
            binding.tabLayoutTimku.tabMode = TabLayout.MODE_SCROLLABLE
            binding.viewPagerTimku.currentItem = 0
        }


        // set appbar view
        binding.appbarTimkuTeamlead.tvAppbarTitle.text = when(clickFrom) {
            "Service" -> "Tim Ku"
            "Myteam Teamlead" -> teamleadName
            else -> "Error"
        }
        binding.appbarTimkuTeamlead.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_PARAMS_MYTEAM, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")


        }
        binding.appbarTimkuTeamlead.ivAppbarSearch.visibility = View.INVISIBLE
        binding.appbarTimkuTeamlead.ivAppbarSearch.setOnClickListener {
            val search = Intent(this, SearchStaffMyteamTlActivity::class.java)
            startActivity(search)
        }
        binding.appbarTimkuTeamlead.ivAppbarDate.visibility = View.INVISIBLE
        binding.appbarTimkuTeamlead.ivAppbarDate.setOnClickListener {
            showBottomSheetDialog()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
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
                val dtDate = dateParams.format(cal.time)
                Toast.makeText(this, "Mengambil data pada tanggal: $dtDate", Toast.LENGTH_LONG).show()

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.DATE_PARAMS_MYTEAM,
                    dateParams.format(cal.time))

                val i = Intent(this, MyteamTeamleadActivity::class.java)
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
        CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_PARAMS_MYTEAM, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
//        if (dataNoInternet == "noInternet") {
//            val i = Intent(this, MainActivity::class.java)
//            startActivity(i)
//            finishAffinity()
//        } else {
//            super.onBackPressed()
//            finish()
//        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
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
//            noInternetState()
//            binding.viewPagerTimku.visibility = View.GONE
//            binding.flConnectionTimeoutTimku.visibility = View.VISIBLE
            dataNoInternet = "noInternet"
            return true
        }
        return false
    }

    private fun viewIsOnline() {
//        binding.viewPagerTimku.visibility = View.VISIBLE
//        binding.flConnectionTimeoutTimku.visibility = View.GONE
        dataNoInternet = "Internet"
    }

    private fun noInternetState() {
        val noInternetState = ConnectionTimeoutFragment.newInstance().also {
            it.setListener(this)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.flConnectionTimeoutTimku, noInternetState, "connectionTimeout")
            .commit()
    }

    override fun onRetry() {
        val i = Intent(this, MyteamTeamleadActivity::class.java)
        startActivity(i)
        finish()
    }


}