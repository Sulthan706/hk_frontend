package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.service

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportBod.DailyVisitBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportManagement.DailyVisitManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportTeknisi.DailyVisitTeknisiResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.eventCalendar.EventCalendarManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.plannedVisitReport.PlannedVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.unplannedVisitsReport.UnplannedVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.visitReport.VisitReportManagementResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface VisitReportManagementService {

    @GET("/api/v1/rkb/management/bod/visit/report")
    fun getVisitReportBod(
        @Query("bodId") userId: Int,
        @Query("date") date: String,
        @Query("date2") date2: String
    ): Single<VisitReportManagementResponse>

    @GET("/api/v1/rkb/management/visit/report")
    fun getVisitReportManagement(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("date2") date2: String
    ): Single<VisitReportManagementResponse>

    @GET("/api/v1/rkb/management/visit/report/teknisi")
    fun getVisitReportTeknisi(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("date2") date2: String
    ): Single<VisitReportManagementResponse>

    @GET("/api/v1/rkb/management/bod/list/planned-visit")
    fun getPlannedVisitsBod(
        @Query("bodId") userId: Int,
        @Query("date") date: String,
        @Query("date2") date2: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<PlannedVisitsManagementResponse>

    @GET("/api/v1/rkb/management/list/planned-visit")
    fun getPlannedVisitsManagement(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("date2") date2: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<PlannedVisitsManagementResponse>

    @GET("/api/v1/rkb/management/teknisi/list/planned-visit")
    fun getPlannedVisitsTeknisi(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("date2") date2: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<PlannedVisitsManagementResponse>

    @GET("/api/v1/rkb/management/bod/list/unplanned-visit")
    fun getUnplannedVisitsBod(
        @Query("bodId") userId: Int,
        @Query("date") date: String,
        @Query("date2") date2: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<UnplannedVisitsManagementResponse>

    @GET("/api/v1/rkb/management/list/unplanned-visit")
    fun getUnplannedVisitsManagement(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("date2") date2: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<UnplannedVisitsManagementResponse>

    @GET("/api/v1/rkb/management/teknisi/list/unplanned-visit")
    fun getUnplannedVisitsTeknisi(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("date2") date2: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<UnplannedVisitsManagementResponse>

    @GET("/api/v1/rkb/management/bod/calendar/events")
    fun getEventCalendarBod(
        @Query("bodId") userId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Single<EventCalendarManagementResponse>

    @GET("/api/v1/rkb/management/calendar/events-schedule")
    fun getEventCalendarManagement(
        @Query("adminMasterId") userId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Single<EventCalendarManagementResponse>

    @GET("/api/v1/rkb/management/teknisi/calendar/events-schedule")
    fun getEventCalendarTeknisi(
        @Query("adminMasterId") userId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Single<EventCalendarManagementResponse>

    @GET("/api/v1/rkb/management/bod/daily-target")
    fun getDailyVisitsBod(
        @Query("bodId") userId: Int,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<DailyVisitBodResponse>

    @GET("/api/v1/rkb/management/daily-target")
    fun getDailyVisitsManagement(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<DailyVisitManagementResponse>

    @GET("/api/v1/rkb/management/teknisi/daily-target")
    fun getDailyVisitsTeknisi(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<DailyVisitTeknisiResponse>

}