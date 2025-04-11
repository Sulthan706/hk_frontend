package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.remote

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.service.VisitReportManagementService
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportBod.DailyVisitBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportManagement.DailyVisitManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportTeknisi.DailyVisitTeknisiResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.eventCalendar.EventCalendarManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.plannedVisitReport.PlannedVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.unplannedVisitsReport.UnplannedVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.visitReport.VisitReportManagementResponse
import io.reactivex.Single
import javax.inject.Inject

class VisitReportManagementDataSourceImpl @Inject constructor(private val service: VisitReportManagementService): VisitReportManagementDataSource {

    override fun getVisitReportBod(
        userId: Int,
        date: String,
        date2: String
    ): Single<VisitReportManagementResponse> {
        return service.getVisitReportBod(userId, date, date2)
    }

    override fun getVisitReportManagement(
        userId: Int,
        date: String,
        date2: String
    ): Single<VisitReportManagementResponse> {
        return service.getVisitReportManagement(userId, date, date2)
    }

    override fun getVisitReportTeknisi(
        userId: Int,
        date: String,
        date2: String
    ): Single<VisitReportManagementResponse> {
        return service.getVisitReportTeknisi(userId, date, date2)
    }

    override fun getPlannedVisitsBod(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<PlannedVisitsManagementResponse> {
        return service.getPlannedVisitsBod(userId, date, date2, page, perPage)
    }

    override fun getPlannedVisitsManagement(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<PlannedVisitsManagementResponse> {
        return service.getPlannedVisitsManagement(userId, date, date2, page, perPage)
    }

    override fun getPlannedVisitsTeknisi(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<PlannedVisitsManagementResponse> {
        return service.getPlannedVisitsTeknisi(userId, date, date2, page, perPage)
    }

    override fun getUnplannedVisitsBod(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<UnplannedVisitsManagementResponse> {
        return service.getUnplannedVisitsBod(userId, date, date2, page, perPage)
    }

    override fun getUnplannedVisitsManagement(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<UnplannedVisitsManagementResponse> {
        return service.getUnplannedVisitsManagement(userId, date, date2, page, perPage)
    }

    override fun getUnplannedVisitsTeknisi(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<UnplannedVisitsManagementResponse> {
        return service.getUnplannedVisitsTeknisi(userId, date, date2, page, perPage)
    }

    override fun getEventCalendarBod(
        userId: Int,
        month: Int,
        year: Int
    ): Single<EventCalendarManagementResponse> {
        return service.getEventCalendarBod(userId, month, year)
    }

    override fun getEventCalendarManagement(
        userId: Int,
        month: Int,
        year: Int
    ): Single<EventCalendarManagementResponse> {
        return service.getEventCalendarManagement(userId, month, year)
    }

    override fun getEventCalendarTeknisi(
        userId: Int,
        month: Int,
        year: Int
    ): Single<EventCalendarManagementResponse> {
        return service.getEventCalendarTeknisi(userId, month, year)
    }

    override fun getDailyVisitsBod(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<DailyVisitBodResponse> {
        return service.getDailyVisitsBod(userId, date, page, perPage)
    }

    override fun getDailyVisitsManagement(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<DailyVisitManagementResponse> {
        return service.getDailyVisitsManagement(userId, date, page, perPage)
    }

    override fun getDailyVisitsTeknisi(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<DailyVisitTeknisiResponse> {
        return service.getDailyVisitsTeknisi(userId, date, page, perPage)
    }

}