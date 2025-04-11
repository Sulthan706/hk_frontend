package com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.service

import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.dailyAttendanceReport.DailyAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listEventCalendar.ListCalendarReportResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listLateReport.ListLateReportResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportService {

    @GET("api/v1/attendance/employee/report-detail")
    fun getDailyAttendanceReport(
        @Query("employeeId") userId: Int,
        @Query("date") date: String
    ): Single<DailyAttendanceReportResponse>

    @GET("api/v1/attendance/employee/terlambat")
    fun getListLateReport(
        @Query("employeeId") userId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Single<ListLateReportResponse>

    @GET("api/v1/attendance/employee/event-calendar")
    fun getListCalendarReport(
        @Query("employeeId") userId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Single<ListCalendarReportResponse>

}