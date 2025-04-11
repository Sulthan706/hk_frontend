package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentListWorkBinding
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.calendarrkbclient.ContentListItemRkbClient
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.activity.DetailClientRkbActivity
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.adapter.ListRkbClientAdapter
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.viewmodel.MonthlyWorkClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ListWorkFragment : Fragment(),
    ListRkbClientAdapter.ClickRkbClient {
    private lateinit var binding: FragmentListWorkBinding
    private val viewModel: MonthlyWorkClientViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkClientViewModel::class.java)
    }
    private var month: String = ""
    private var year: String = ""
    private lateinit var adapter: ListRkbClientAdapter
    private var loadingDialog: Dialog? = null
    private var perPage = 10
    private var page = 0
    private var isLastPage = false
    private var datesApi = ""
    private var clientId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCodeClient =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListWorkBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setup event
        //calender
        val calenderRkb = binding.calenderRkbEvent
        calenderRkb.setFirstDayOfWeek(Calendar.MONDAY)
        calenderRkb.setUseThreeLetterAbbreviation(true)


        binding.calenderRkbEvent.invalidate()
        // Get the current date as the initial month
        val initialMonth = Calendar.getInstance().time

        // Update the TextView with the initial month
        val initialMonthYearText =
            SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(initialMonth)
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
                val dateNows =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                datesApi = dateNows
                viewModel.getCalendarRkbClient(clientId, projectCodeClient, dateNows, page = 0, perPage)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_FROM_MONTH_RKB, dateNows)

                refreshRecyclerView(ArrayList())
                showLoading("Loading..")
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                // Update the TextView with the new month and year
                val calendar = Calendar.getInstance()
                calendar.time = firstDayOfNewMonth ?: Date()
                val monthYearText =
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)

                val months = SimpleDateFormat("MM", Locale.getDefault()).format(calendar.time)
                val years = SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.time)

//                viewModel.getCalendarEvent(projectCode, userId, months, years)
                viewModel.getEventCalendarRkbClient(projectCodeClient, clientId, months, years)

                binding.tvGetMonthNow.text = monthYearText
            }
        }
        binding.calenderRkbEvent.setListener(calendarListener)

        // set recycler view list operational
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvPeriodic.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(datesApi)
                }
            }

        }
        binding.rvPeriodic.addOnScrollListener(scrollListener)

        //fab croll to top
        val fabColorNormal = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.light_orange_2))
        binding.fabscrolltotop.backgroundTintList = fabColorNormal

        val recyclerView = binding.rvPeriodic
        val fabScrollToTop = binding.fabscrolltotop

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy < 0 && fabScrollToTop.visibility == View.VISIBLE) {
                    fabScrollToTop.hide()
                } else if (dy > 0 && fabScrollToTop.visibility != View.VISIBLE) {
                    fabScrollToTop.show()
                }
            }
        })

        binding.fabscrolltotop.setOnClickListener { _ ->
            val recyclerView = binding.rvPeriodic
            if (recyclerView.layoutManager != null) {
                recyclerView.smoothScrollToPosition(0)
            }
        }

        loadDataEvent()
        loadData(datesApi)
        setObserver()
    }

    private fun loadDataEvent() {
        viewModel.getEventCalendarRkbClient(projectCodeClient, clientId, month, year)
    }

    private fun loadData(datesApi: String) {
        viewModel.getCalendarRkbClient(clientId, projectCodeClient, datesApi, page, perPage)
    }

    private fun setObserver() {
        val processedDailyEventDates = mutableSetOf<String>()
        val processedMonthlyEventDates = mutableSetOf<String>()

        viewModel.getEventCalendarRkbClientViewModel().observe(requireActivity()) { response ->
            if (response.code == 200) {
                val sampleEvents = response.data

                for (events in sampleEvents) {
                    val date = events.timestamp
                    val eventType = events.typeJob

                    val eventTimestamp = convertDateToTimestamp(date, "yyyy-MM-dd")

                    val eventDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(eventTimestamp)

                    if (eventType == "WEEKLY" && !processedDailyEventDates.contains(eventDate)) {
                        val eventDateObj =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventDateObj != null) {
                            binding.calenderRkbEvent.addEvent(
                                com.github.sundeepk.compactcalendarview.domain.Event(
                                    Color.parseColor("#F47721"),
                                    eventDateObj.time // Event timestamp
                                )
                            )
                        }
                        processedDailyEventDates.add(eventDate)
                    }

                    if (eventType == "MONTHLY" && !processedMonthlyEventDates.contains(eventDate)) {
                        val eventDateObj =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventDateObj != null) {
                            binding.calenderRkbEvent.addEvent(
                                com.github.sundeepk.compactcalendarview.domain.Event(
                                    Color.parseColor("#9B51E0"),
                                    eventDateObj.time
                                )
                            )
                        }
                        processedMonthlyEventDates.add(eventDate)
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
        viewModel.getCalendarRkbClientViewModel().observe(requireActivity()) {
            if (it.code == 200) {
                binding.tvDailySummaryCalendar.text = if (it.data.totalDaily == 0) {
                    "-"
                } else it.data.totalDaily.toString()
                binding.tvWeeklySummaryCalendar.text = if (it.data.totalWeekly == 0) {
                    "-"
                } else it.data.totalWeekly.toString()
                if (it.data.totalMonthly == 0 || it.data.totalMonthly == null) {
                    binding.tvMonthlySummaryCalendar.text = "-"
                } else {
                    binding.tvMonthlySummaryCalendar.text = it.data.totalMonthly.toString()
                }

                if (it.data.listJobs.content.isNotEmpty()) {
                    binding.rvPeriodic.visibility = View.VISIBLE
                    isLastPage = false
                    if (page == 0) {
                        adapter = ListRkbClientAdapter(
                            it.data.listJobs.content as ArrayList<ContentListItemRkbClient>
                        ).also { it.setListener(this) }
                        binding.rvPeriodic.adapter = adapter
                    } else {
                        adapter.listRkbClient.addAll(it.data.listJobs.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listRkbClient.size - it.data.listJobs.content.size,
                            adapter.listRkbClient.size
                        )
                    }
                    binding.tvEmtpyStateListJobsPeriodic.visibility = View.GONE
                } else {
                    isLastPage = true
                }

            } else {
                Toast.makeText(requireActivity(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
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

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(requireActivity(), loadingText)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshRecyclerView(data: List<ContentListItemRkbClient>) {
        adapter.listRkbClient.clear()
        adapter.listRkbClient.addAll(data)
        page = 0
        adapter.notifyDataSetChanged()
    }

    override fun onClickRkb(idJob: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_JOB, idJob)
        startActivity(Intent(requireActivity(), DetailClientRkbActivity::class.java))
    }
}




