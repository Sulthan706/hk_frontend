package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.repository

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportBod.DailyVisitBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportManagement.DailyVisitManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportTeknisi.DailyVisitTeknisiResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.eventCalendar.EventCalendarManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.plannedVisitReport.PlannedVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.unplannedVisitsReport.UnplannedVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.visitReport.VisitReportManagementResponse
import io.reactivex.Single

interface VisitReportManagementRepository {

    fun getVisitReportBod(
        userId: Int,
        date: String,
        date2: String
    ): Single<VisitReportManagementResponse>

    fun getVisitReportManagement(
        userId: Int,
        date: String,
        date2: String
    ): Single<VisitReportManagementResponse>

    fun getVisitReportTeknisi(
        userId: Int,
        date: String,
        date2: String
    ): Single<VisitReportManagementResponse>

    fun getPlannedVisitsBod(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<PlannedVisitsManagementResponse>

    fun getPlannedVisitsManagement(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<PlannedVisitsManagementResponse>

    fun getPlannedVisitsTeknisi(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<PlannedVisitsManagementResponse>

    fun getUnplannedVisitsBod(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<UnplannedVisitsManagementResponse>

    fun getUnplannedVisitsManagement(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<UnplannedVisitsManagementResponse>

    fun getUnplannedVisitsTeknisi(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<UnplannedVisitsManagementResponse>


    fun getEventCalendarBod(
        userId: Int,
        month: Int,
        year: Int
    ): Single<EventCalendarManagementResponse>

    fun getEventCalendarManagement(
        userId: Int,
        month: Int,
        year: Int
    ): Single<EventCalendarManagementResponse>

    fun getEventCalendarTeknisi(
        userId: Int,
        month: Int,
        year: Int
    ): Single<EventCalendarManagementResponse>


    fun getDailyVisitsBod(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<DailyVisitBodResponse>

    fun getDailyVisitsManagement(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<DailyVisitManagementResponse>

    fun getDailyVisitsTeknisi(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<DailyVisitTeknisiResponse>
}