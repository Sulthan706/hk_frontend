package com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.remote.ReportDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.dailyAttendanceReport.DailyAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listEventCalendar.ListCalendarReportResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listLateReport.ListLateReportResponse
import io.reactivex.Single
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(private val dataSource: ReportDataSource) : ReportRepository {

    override fun getDailyAttendanceReport(
        userId: Int,
        date: String
    ): Single<DailyAttendanceReportResponse> {
        return dataSource.getDailyAttendanceReport(userId, date)
    }

    override fun getListLateReport(
        userId: Int,
        month: Int,
        year: Int
    ): Single<ListLateReportResponse> {
        return dataSource.getListLateReport(userId, month, year)
    }

    override fun getListCalendarReport(
        userId: Int,
        month: Int,
        year: Int
    ): Single<ListCalendarReportResponse> {
        return dataSource.getListCalendarReport(userId, month, year)
    }

}