package com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.customcalendar

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.CalendarCustomBinding
import com.hkapps.hygienekleen.databinding.CalendarCustomFragmentBinding
import com.hkapps.hygienekleen.databinding.CalendarCustomHeaderBinding
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.lowlevel.CreatePermissionFixActivity
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel.CreatePermissionMidFixActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.snackbar.Snackbar
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class TestCalendar : BaseFragment(R.layout.calendar_custom_fragment), HasToolbar, HasBackButton {

//    override val toolbar: Toolbar?
//        get() = binding.exFourToolbar
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    override val titleRes: Int? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now()

    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private val headerDateFormatter = DateTimeFormatter.ofPattern("EEE'\n'd MMM")

    private val startBackground: GradientDrawable by lazy {
        requireContext().getDrawableCompat(R.drawable.example_4_continuous_selected_bg_start) as GradientDrawable
    }

    private val endBackground: GradientDrawable by lazy {
        requireContext().getDrawableCompat(R.drawable.example_4_continuous_selected_bg_end) as GradientDrawable
    }

    private val title =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PERMISSION_TITLE, "")
    private val reason =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PERMISSION_REASON, "")

    private lateinit var binding: CalendarCustomFragmentBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = CalendarCustomFragmentBinding.bind(view)
        // We set the radius of the continuous selection background drawable dynamically
        // since the view size is `match parent` hence we cannot determine the appropriate
        // radius value which would equal half of the view's size beforehand.
        binding.exFourCalendar.post {
            val radius = ((binding.exFourCalendar.width / 7) / 2).toFloat()
            startBackground.setCornerRadius(topLeft = radius, bottomLeft = radius)
            endBackground.setCornerRadius(topRight = radius, bottomRight = radius)
        }

        // Set the First day of week depending on Locale
        val daysOfWeek = daysOfWeekFromLocale()
        binding.legendLayout.root.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                setTextColorRes(R.color.black)
            }
        }

        val currentMonth = YearMonth.now()
        binding.exFourCalendar.setup(currentMonth, currentMonth.plusMonths(12), daysOfWeek.first())
        binding.exFourCalendar.scrollToMonth(currentMonth)

        @SuppressLint("SimpleDateFormat")
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = CalendarCustomBinding.bind(view)

            init {
                view.setOnClickListener {
//                    if (day.owner == DayOwner.THIS_MONTH && (day.date == today || day.date.isAfter(
//                            today
//                        ))
//                    )
                    if (day.owner == DayOwner.THIS_MONTH && (day.date == today || day.date.isAfter(
                            today
                        ))
                    ) {
                        val date = day.date
                        if (startDate != null) {
                            if (date < startDate || endDate != null) {
                                startDate = date
                                endDate = null
                            } else if (date != startDate) {
                                val formatter = SimpleDateFormat("yyyy-MM-dd")
                                val dateInString = "2022-00-00"
                                val formatterOut = SimpleDateFormat("yyyy-MM-dd")
                                try {
                                    val datea = formatter.parse(dateInString)
                                    val dateb = formatterOut.format(datea)
                                    if (date.toString() == dateb){
//                                        Toast.makeText(
//                                            context,
//                                            "Ada hari libur.",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
                                        clearData()
                                    }else{
                                        endDate = date
                                    }
                                } catch (e: ParseException) {
                                    e.printStackTrace()
                                }
                            } else if (date == startDate) {
                                endDate = date
                            }
                        } else {
                            //belum select sama sekali atau udah di clear calendarnya
                            val formatter = SimpleDateFormat("yyyy-MM-dd")
                            val dateInString = "2022-00-00"
                            val formatterOut = SimpleDateFormat("yyyy-MM-dd")
                            try {
                                val datea = formatter.parse(dateInString)
                                val dateb = formatterOut.format(datea)

                                if (date.toString() == dateb) {
                                    Toast.makeText(
                                        context,
                                        "Anda libur hari ini.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    startDate = date
                                    Toast.makeText(context, "today $today", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }
                        }
                        this@TestCalendar.binding.exFourCalendar.notifyCalendarChanged()
                        bindSummaryViews()
                    }
                }
            }
        }


        binding.clearSelectedCal.setOnClickListener {
            clearData()
        }

        binding.exFourCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            @SuppressLint("SimpleDateFormat")
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.exFourDayText
                val roundBgView = container.binding.exFourRoundBgView

                textView.text = null
                textView.background = null
                roundBgView.makeInVisible()

                val startDate = startDate
                val endDate = endDate

                when (day.owner) {
                    DayOwner.THIS_MONTH -> {
                        textView.text = day.day.toString()
                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                        val dateInString = "2022-00-00"
                        val formatterOut = SimpleDateFormat("yyyy-MM-dd")

                        try {
                            val datea = formatter.parse(dateInString)
                            val dateb = formatterOut.format(datea)

                            when {
                                day.date.isBefore(today) -> {
                                    textView.setTextColorRes(R.color.grayLine)
                                }
                                day.date.toString() == dateb -> {
                                    textView.setTextColorRes(R.color.grayLine)
                                }
                                else -> {
                                    when {
                                        startDate == day.date && endDate == null -> {
                                            textView.setTextColorRes(R.color.white)
                                            roundBgView.makeVisible()
                                            roundBgView.setBackgroundResource(R.drawable.example_4_single_selected_bg)
                                        }
                                        day.date == startDate && day.date == endDate -> {
                                            textView.setTextColorRes(R.color.white)
                                            textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                                        }
                                        startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                                            textView.setTextColorRes(R.color.white)
                                            textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                                        }
                                        day.date == startDate -> {
                                            textView.setTextColorRes(R.color.white)
                                            textView.background = startBackground
                                        }
                                        day.date == endDate -> {
                                            when {
                                                day.date.toString() == dateb -> {
                                                    textView.setTextColorRes(R.color.grayLine)
                                                }
                                                day.date.toString() != dateb -> {
                                                    textView.setTextColorRes(R.color.white)
                                                    textView.background = endBackground
                                                }
                                            }
                                        }
                                        day.date == today -> {
                                            textView.setTextColorRes(R.color.secondary_color)
                                            roundBgView.makeVisible()
                                            roundBgView.setBackgroundResource(R.drawable.example_4_today_bg)
                                        }
                                        else -> textView.setTextColorRes(R.color.mdtp_transparent_black)
                                    }
                                }
                            }

                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    }
                    // Make the coloured selection background continuous on the invisible in and out dates across various months.
                    DayOwner.PREVIOUS_MONTH ->
                        if (startDate != null && endDate != null && isInDateBetween(
                                day.date,
                                startDate,
                                endDate
                            )
                        ) {
                            textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                        }
                    DayOwner.NEXT_MONTH ->
                        if (startDate != null && endDate != null && isOutDateBetween(
                                day.date,
                                startDate,
                                endDate
                            )
                        ) {
                            textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                        }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = CalendarCustomHeaderBinding.bind(view).exFourHeaderText
        }
        binding.exFourCalendar.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    val monthTitle =
                        "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
                    container.textView.text = monthTitle
                }
            }

        binding.exFourSaveButton.setOnClickListener click@{
            val startDate = startDate
            val endDate = endDate
            if (startDate != null && endDate != null) {
                val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
                val text = "Selected: ${formatter.format(startDate)} - ${formatter.format(endDate)}"
                Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.SAVE_DATE_TEMPORER_START,
                    startDate.toString()
                )

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.SAVE_DATE_TEMPORER_END,
                    endDate.toString()
                )

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.PERMISSION_TITLE,
                    title
                )

                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.PERMISSION_REASON,
                    reason
                )

                if (userLevel == "Operator") {
                    val i = Intent(context, CreatePermissionFixActivity::class.java)
                    requireActivity().startActivity(i)
                    requireActivity().supportFinishAfterTransition()
                } else {
                    val i = Intent(context, CreatePermissionMidFixActivity::class.java)
                    requireActivity().startActivity(i)
                    requireActivity().supportFinishAfterTransition()
                }
            } else {
                Snackbar.make(requireView(), "Pilih tanggal.", Snackbar.LENGTH_LONG)
                    .show()
            }
            fragmentManager?.popBackStack()
        }
        bindSummaryViews()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindSummaryViews() {
        binding.exFourStartDateText.apply {
            if (startDate != null) {
                text = headerDateFormatter.format(startDate)
                setTextColorRes(R.color.black)
            } else {
                text = "Dari"
                setTextColor(Color.BLACK)
            }
        }

        binding.exFourEndDateText.apply {
            if (endDate != null) {
                text = headerDateFormatter.format(endDate)
                setTextColorRes(R.color.black)
            } else {
                text = "Sampai"
                setTextColor(Color.BLACK)
            }
        }

        // Enable save button if a range is selected or no date is selected at all, Airbnb style.
//        binding.exFourSaveButton.isEnabled = endDate != null || (startDate == null && endDate == null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun clearData() {
        startDate = null
        endDate = null
        binding.exFourCalendar.notifyCalendarChanged()
        bindSummaryViews()
        true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isInDateBetween(
        inDate: LocalDate,
        startDate: LocalDate,
        endDate: LocalDate
    ): Boolean {
        if (startDate.yearMonth == endDate.yearMonth) return false
        if (inDate.yearMonth == startDate.yearMonth) return true
        val firstDateInThisMonth = inDate.plusMonths(1).yearMonth.atDay(1)
        return firstDateInThisMonth >= startDate && firstDateInThisMonth <= endDate && startDate != firstDateInThisMonth
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isOutDateBetween(
        outDate: LocalDate,
        startDate: LocalDate,
        endDate: LocalDate
    ): Boolean {
        if (startDate.yearMonth == endDate.yearMonth) return false
        if (outDate.yearMonth == endDate.yearMonth) return true
        val lastDateInThisMonth = outDate.minusMonths(1).yearMonth.atEndOfMonth()
        return lastDateInThisMonth >= startDate && lastDateInThisMonth <= endDate && endDate != lastDateInThisMonth
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.example_4_menu, menu)
//        binding.exFourToolbar.post {
//            // Configure menu text to match what is in the Airbnb app.
//            binding.exFourToolbar.findViewById<TextView>(R.id.menuItemClear).apply {
//                setTextColor(requireContext().getColorCompat(R.color.secondary_color))
//                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//                isAllCaps = false
//            }
//        }
//        menu.findItem(R.id.menuItemClear).setOnMenuItemClickListener {
//            startDate = null
//            endDate = null
//            binding.exFourCalendar.notifyCalendarChanged()
//            bindSummaryViews()
//            true
//        }

    }

    override fun onStart() {
        super.onStart()
        val closeIndicator = requireContext().getDrawableCompat(R.drawable.ic_clear)?.apply {
            setColorFilter(
                requireContext().getColorCompat(R.color.secondary_color),
                PorterDuff.Mode.SRC_ATOP
            )
        }
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(closeIndicator)
        requireActivity().window.apply {
            // Update statusbar color to match toolbar color.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarColor = requireContext().getColorCompat(R.color.white)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                statusBarColor = Color.GRAY
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.apply {
            // Reset statusbar color.
            statusBarColor = requireContext().getColorCompat(R.color.black)
            decorView.systemUiVisibility = 0
        }
    }

    override val toolbar: Toolbar?
        get() = TODO("Not yet implemented")
}
