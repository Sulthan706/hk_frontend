package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityAttendanceHistoryBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.activity.highLevel.AttendanceActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.adapter.AttendanceHistoryAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.adapter.AttendanceHistoryByDateAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.viewmodel.AttendanceViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.ConnectionTimeoutFragment
import com.hkapps.hygienekleen.utils.NoDataFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AttendanceHistoryActivity : AppCompatActivity(), NoInternetConnectionCallback {

    private lateinit var binding: ActivityAttendanceHistoryBinding
    private lateinit var rvSkeleton: Skeleton
    private var loadingDialog: Dialog? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    private lateinit var historyAdapter: AttendanceHistoryAdapter
    private lateinit var historyByDateAdapter: AttendanceHistoryByDateAdapter
    private lateinit var historyResponseModel: com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.HistoryResponseModel
    private lateinit var historyByDateResponseModel: com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.HistoryByDateResponseModel

    var dateParamsPref: String = ""
    var dateParamsSuf: String = ""

    var dateTextPref: String = "Pilih Tanggal"
    var dateTextSuf: String = "Pilih Tanggal"
    var dataNoInternet: String = "Internet"

    private val attedanceViewModel: AttendanceViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceViewModel::class.java)
    }


    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
        
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)

        binding.layoutAppbarAttendanceHistory.tvAppbarTitle.text = "History Absen"
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

//        //balik order by nya
//        (layoutManager as LinearLayoutManager).reverseLayout = true
//        (layoutManager as LinearLayoutManager).stackFromEnd = true

        binding.rvAttendanceHistory.layoutManager = layoutManager

        rvSkeleton =
            binding.rvAttendanceHistory.applySkeleton(R.layout.item_attendance_history_skeleton)
        rvSkeleton.showSkeleton()

        binding.layoutAppbarAttendanceHistory.ivAppbarBack.setOnClickListener {
            if (dataNoInternet == "noInternet") {
                val i = Intent(this, AttendanceActivity::class.java)
                startActivity(i)
                finishAffinity()
            } else {
                super.onBackPressed()
                finish()
            }
        }

        showLoading(getString(R.string.loading_string))

        //GET ALL DATA
        attedanceViewModel.getHistoryAttendance(employeeId, projectCode)

        //All data
        setObserver()
        //data by date
        setObserverByDate()

        binding.rvAttendanceHistory.visibility = View.GONE
        binding.flConnectionTimeoutHistory.visibility = View.VISIBLE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }

        //bottomsheets setting clicklistener
        binding.tvAttendanceHistory.setOnClickListener {
            showBottomSheetDialog()
        }

        Log.d("Internet", "onCreate: $dataNoInternet")
    }

    @SuppressLint("SimpleDateFormat")
    private fun showBottomSheetDialog() {

        val dialog = BottomSheetDialog(this)

        dialog.setContentView(R.layout.layout_bottom_sheets)
        val clChooseDatePref = dialog.findViewById<TextView>(R.id.tv_choose_date_prefix)
        val clChooseDateSuf = dialog.findViewById<TextView>(R.id.tv_choose_date_suffix)
        val btnApplied = dialog.findViewById<AppCompatButton>(R.id.btn_applied_history)
        val ivCancel = dialog.findViewById<ImageView>(R.id.iv_cancel_history)

        clChooseDatePref?.text = dateTextPref
        clChooseDateSuf?.text = dateTextSuf

        ivCancel?.setOnClickListener {  //handle click event
            dialog.dismiss()
        }

        //set calendar for choose by date
//        binding.tvAttendanceHistory.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
        var cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd/MM/yyyy" // mention the format you need
                val paramsFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val dateParams = SimpleDateFormat(paramsFormat, Locale.US)

                dateParamsPref = dateParams.format(cal.time)

                dateTextPref = sdf.format(cal.time)
                clChooseDatePref?.text = dateTextPref
            }

        val dateSetListenerSuffix =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd/MM/yyyy" // mention the format you need
                val paramsFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val dateParams = SimpleDateFormat(paramsFormat, Locale.US)

                dateParamsSuf = dateParams.format(cal.time)

                dateTextSuf = sdf.format(cal.time)
                clChooseDateSuf?.text = dateTextSuf
            }


        btnApplied?.setOnClickListener {  //handle click event


            if (clChooseDatePref?.text == this.getString(R.string.pilih_tanggal) || clChooseDateSuf?.text == this.getString(
                    R.string.pilih_tanggal
                )
            ) {

                Toast.makeText(
                    this,
                    "Harap isi tanggal.",
                    Toast.LENGTH_LONG
                ).show()

            } else {

                //get range of days
                //string date
                val dtStart = dateParamsPref.format(cal.time)
                val dtEnd = dateParamsSuf.format(cal.time)
                val format = SimpleDateFormat("yyyy-MM-dd")
                try {

                    //date
                    val startDate: Date? = format.parse(dtStart)
                    val endDate: Date? = format.parse(dtEnd)

                    //long
                    val startLong: Long = startDate!!.time
                    val endLong: Long = endDate!!.time

                    val msDiff = endLong - startLong
                    val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff)

                    if (daysDiff > 30) {
                        Toast.makeText(
                            this,
                            "Tidak dapat memilih lebih dari 30 hari.",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

//                GET API BASE ON DATE
                        attedanceViewModel.getHistoryAttendanceByDate(
                            employeeId,
                            projectCode,
                            dateParamsPref.format(cal.time),
                            dateParamsSuf.format(cal.time)
                        )
                        rvSkeleton.showSkeleton()
                        dialog.dismiss()
                        binding.llHeaderHistoryDate.visibility = View.VISIBLE
                    }

                } catch (e: ParseException) {
                    e.printStackTrace()
                }

//            Toast.makeText(this, "" + dateParamsSuf.format(cal.time) + " " + dateParamsSuf.format(cal.time), Toast.LENGTH_LONG).show()
            }
        }

        clChooseDatePref?.setOnClickListener {
            DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }

        clChooseDateSuf?.setOnClickListener {
            DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme, dateSetListenerSuffix,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }
        dialog.show()
    }


    //INI GET DATA BUAT DITARO DI ADAPTERNYA
    private fun setObserver() {
        attedanceViewModel.historyResponseModel.observe(this, Observer {
            if (it.code == 200) {
                if (it.historyDataArrayResponseModel.isNotEmpty()) {

                    //set timer cuma 1.1 detik buat loadingnya
                    val progressRunnable = Runnable {
                        hideLoading()
                        historyResponseModel = it
                        historyAdapter =
                            AttendanceHistoryAdapter(
                                it.historyDataArrayResponseModel
                            )

                        binding.rvAttendanceHistory.adapter = historyAdapter
                    }
                    val pdCanceller = Handler()
                    pdCanceller.postDelayed(progressRunnable, 1100)
                } else {
                    binding.rvAttendanceHistory.visibility = View.GONE
                    binding.flConnectionTimeoutHistory.visibility = View.VISIBLE
                    noDataState()
                    hideLoading()
                    onSNACK(binding.root, "Tidak ada data.")
                }
            } else {
                onSNACK(binding.root, "Terjadi kesalahan.")
            }
        })
    }


    //INI GET DATA BY DATE BUAT DITARO DI ADAPTERNYA
    private fun setObserverByDate() {
        attedanceViewModel.historyByDateResponseModel.observe(this, Observer {
            if (it.code == 200) {
                if (it.historyDataArrayByDateResponseModel.isNotEmpty()) {
                    //set timer cuma 1.1 detik buat loadingnya
                    val progressRunnable = Runnable {
                        historyByDateResponseModel = it
                        historyByDateAdapter =
                            AttendanceHistoryByDateAdapter(
                                it.historyDataArrayByDateResponseModel
                            )
                        binding.rvAttendanceHistory.adapter = historyByDateAdapter

                        hideLoading()
                        onSNACK(binding.root, "Berhasil menampilkan data.")
                    }

                    val pdCanceller = Handler()
                    pdCanceller.postDelayed(progressRunnable, 1100)

                    binding.tvAttendanceHistory.visibility = View.VISIBLE

                    binding.rvAttendanceHistory.visibility = View.VISIBLE
                    binding.flDateHistory.visibility = View.GONE

                } else {
                    binding.tvAttendanceHistory.visibility = View.VISIBLE

                    binding.rvAttendanceHistory.visibility = View.GONE
                    binding.flDateHistory.visibility = View.VISIBLE
                    hideLoading()
                    onSNACK(binding.root, "Tidak ada data.")
                }
            } else {
                onSNACK(binding.root, "Terjadi kesalahan.")
            }
        })
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    //check koneksi
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    binding.rvAttendanceHistory.visibility = View.VISIBLE
                    binding.flConnectionTimeoutHistory.visibility = View.GONE

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    binding.rvAttendanceHistory.visibility = View.VISIBLE
                    binding.flConnectionTimeoutHistory.visibility = View.GONE

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    binding.rvAttendanceHistory.visibility = View.VISIBLE
                    binding.flConnectionTimeoutHistory.visibility = View.GONE

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        } else {
            binding.rvAttendanceHistory.visibility = View.GONE
            binding.flConnectionTimeoutHistory.visibility = View.VISIBLE
            noInternetState()
            dataNoInternet = "noInternet"
            Log.i("Internet", "NO_INTERNET")
            return true
        }
        return false
    }

    override fun onRetry() {
        val i = Intent(this, AttendanceHistoryActivity::class.java)
        startActivity(i)
    }

    private fun noInternetState() {
        val noInternetState = ConnectionTimeoutFragment.newInstance().also {
            it.setListener(this)
        }
        hideLoading()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.flConnectionTimeoutHistory,
                noInternetState,
                "connectionTimeout"
            )
            .commit()
    }

    private fun noDataState() {
        val noInternetState = NoDataFragment.newInstance().also {
            it.setListener(this)
        }
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.flConnectionTimeoutHistory,
                noInternetState,
                "noDataState"
            )
            .commit()
    }


    //Snack bar kesalahan data / data kosong
    private fun onSNACK(view: View, str: String) {
        val snackbar = Snackbar.make(
            view, str,
            Snackbar.LENGTH_LONG
        ).setAction("Error", null)
        snackbar.setActionTextColor(resources.getColor(R.color.primary_color))
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(resources.getColor(R.color.primary_color))
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 12f
        snackbar.show()
    }

    override fun onBackPressed() {
        if (dataNoInternet == "noInternet") {
            val i = Intent(this, AttendanceActivity::class.java)
            startActivity(i)
            finishAffinity()
        } else {
            super.onBackPressed()
            finish()
        }
    }
}