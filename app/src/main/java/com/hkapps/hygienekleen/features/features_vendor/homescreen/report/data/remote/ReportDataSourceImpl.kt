package com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.remote

import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.service.ReportService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.dailyAttendanceReport.DailyAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listEventCalendar.ListCalendarReportResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listLateReport.ListLateReportResponse
import io.reactivex.Single
import javax.inject.Inject

class ReportDataSourceImpl @Inject constructor(private val service: ReportService) : ReportDataSource {

    override fun getDailyAttendanceReport(
        userId: Int,
        date: String
    ): Single<DailyAttendanceReportResponse> {
        return service.getDailyAttendanceReport(userId, date)
    }

    override fun getListLateReport(
        userId: Int,
        month: Int,
        year: Int
    ): Single<ListLateReportResponse> {
        return service.getListLateReport(userId, month, year)
    }

    override fun getListCalendarReport(
        userId: Int,
        month: Int,
        year: Int
    ): Single<ListCalendarReportResponse> {
        return service.getListCalendarReport(userId, month, year)
    }

}