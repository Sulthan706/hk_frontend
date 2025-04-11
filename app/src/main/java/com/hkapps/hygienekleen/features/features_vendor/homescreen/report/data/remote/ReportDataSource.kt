package com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.remote

import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.dailyAttendanceReport.DailyAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listEventCalendar.ListCalendarReportResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listLateReport.ListLateReportResponse
import io.reactivex.Single

interface ReportDataSource {

    fun getDailyAttendanceReport(
        userId: Int,
        date: String
    ): Single<DailyAttendanceReportResponse>

    fun getListLateReport(
        userId: Int,
        month: Int,
        year: Int
    ): Single<ListLateReportResponse>

    fun getListCalendarReport(
        userId: Int,
        month: Int,
        year: Int
    ): Single<ListCalendarReportResponse>

}