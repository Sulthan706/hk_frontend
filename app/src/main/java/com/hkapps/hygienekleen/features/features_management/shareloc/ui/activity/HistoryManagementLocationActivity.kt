package com.hkapps.hygienekleen.features.features_management.shareloc.ui.activity

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHistoryManagementLocationBinding
import com.hkapps.hygienekleen.features.features_management.shareloc.viewmodel.ShareLocManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HistoryManagementLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryManagementLocationBinding
    private val viewModel: ShareLocManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ShareLocManagementViewModel::class.java)
    }
    private var managementId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var managementName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME,"")
    private var month: String = ""
    private var year: String = ""
    private var datesApi: String = ""
    private var loadingDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryManagementLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        binding.appBarHistoryLocManagementBod.tvAppbarTitle.text = "Riwayat Lokasi"
        binding.appBarHistoryLocManagementBod.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        //setup event
        //calender
        val calenderRkb = binding.calenderHistorLocManagementBod
        calenderRkb.setFirstDayOfWeek(Calendar.MONDAY)
        calenderRkb.setUseThreeLetterAbbreviation(true)


        // Sample event data (date, details)

        binding.calenderHistorLocManagementBod.invalidate()
        // Get the current date as the initial month
        val initialMonth = Calendar.getInstance().time


        val initialMonthYearText = SimpleDateFormat("MMMM yyyy", Locale("id","ID")).format(initialMonth)
        binding.tvGetMonthNow.text = initialMonthYearText

        month = SimpleDateFormat("MM", Locale.getDefault()).format(initialMonth)
        year = SimpleDateFormat("yyyy", Locale.getDefault()).format(initialMonth)
        datesApi = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(initialMonth)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_FROM_MONTH_RKB, datesApi)


        val calendarListener = object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                // Handle day click if needed
                val calendar = Calendar.getInstance()
                calendar.time = dateClicked ?: Date()
                val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                datesApi = dateNow

                viewModel.getDetailManagementShareLoc(managementId, datesApi)
                showLoading("Loading..")



//                val itemDate = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(calendar.time)
//                val outputDate = convertDate(itemDate)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_FROM_MONTH_RKB, dateNow)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                // Update the TextView with the new month and year
                val calendar = Calendar.getInstance()
                calendar.time = firstDayOfNewMonth ?: Date()
                val monthYearText = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)

                val months = SimpleDateFormat("MM", Locale.getDefault()).format(calendar.time)
                val years = SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.time)

                viewModel.getEventCalenderManagement(managementId, months, years)


                binding.tvGetMonthNow.text = monthYearText
            }
        }
        binding.calenderHistorLocManagementBod.setListener(calendarListener)

        //name management
        binding.tvNameManagementAllLocationBod.text = managementName.ifEmpty { "" }
        showLoading(getString(R.string.loading_string2))
        loadDataCalendar()
        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun loadDataCalendar() {
        viewModel.getEventCalenderManagement(managementId, month, year)
    }

    private fun loadData() {
        viewModel.getDetailManagementShareLoc(managementId, datesApi)
    }

    private fun setObserver() {
        val processedDailyEventDates = mutableSetOf<String>()

        viewModel.getEventCalendarManagementViewModel().observe(this) { response ->
            if (response.code == 200) {
                val sampleEvents = response.data

                for (events in sampleEvents) {
                    val date = events.timestamp
                    val eventType = events.type
                    val eventTimestamp = convertDateToTimestamp(date, "yyyy-MM-dd")

                    val eventDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(eventTimestamp)

                    if (eventType == "Catatan Lokasi Terakhir" && !processedDailyEventDates.contains(eventDate)) {
                        val eventDateObj =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventDateObj != null) {
                            binding.calenderHistorLocManagementBod.addEvent(
                                com.github.sundeepk.compactcalendarview.domain.Event(
                                    Color.parseColor("#00BD8C"),
                                    eventDateObj.time // Event timestamp
                                )
                            )
                        }
                        processedDailyEventDates.add(eventDate)
                    }
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()

        }
        viewModel.getDetailManagementShareLocViewModel().observe(this){
            if (it.code == 200){
                binding.llSelectedManagement.visibility = View.VISIBLE
                binding.tvEmptyLocationShareLoc.visibility = View.GONE

                setPhotoProfile(it.data.managementImage, binding.ivImageUserManagement)
                binding.tvDateManagementAllLocation.text = it.data.updated_at.ifEmpty { "-" }
                binding.tvAddressAllManagement.text = it.data.address.ifEmpty { "Data not found" }
            } else {
                binding.llSelectedManagement.visibility = View.GONE
                binding.tvEmptyLocationShareLoc.visibility = View.VISIBLE
            }
            hideLoading()
        }
    }
    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun convertDateToTimestamp(dateString: String, dateFormat: String): Long {
        try {
            val sdf = SimpleDateFormat(dateFormat)
            val date = sdf.parse(dateString)
            return date?.time ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    private fun setPhotoProfile(img: String?, imageView: ImageView) {
        val url = getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                resources.getIdentifier(uri, null, packageName)
            val res = resources.getDrawable(imaResource)
            imageView.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(imageView)
        }
    }
    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }


}