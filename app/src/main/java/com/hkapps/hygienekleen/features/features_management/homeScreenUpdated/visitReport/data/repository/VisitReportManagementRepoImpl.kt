package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.repository

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.remote.VisitReportManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportBod.DailyVisitBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportManagement.DailyVisitManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportTeknisi.DailyVisitTeknisiResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.eventCalendar.EventCalendarManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.plannedVisitReport.PlannedVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.unplannedVisitsReport.UnplannedVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.visitReport.VisitReportManagementResponse
import io.reactivex.Single
import javax.inject.Inject

class VisitReportManagementRepoImpl @Inject constructor(private val dataSource: VisitReportManagementDataSource) : VisitReportManagementRepository {

    override fun getVisitReportBod(
        userId: Int,
        date: String,
        date2: String
    ): Single<VisitReportManagementResponse> {
        return dataSource.getVisitReportBod(userId, date, date2)
    }

    override fun getVisitReportManagement(
        userId: Int,
        date: String,
        date2: String
    ): Single<VisitReportManagementResponse> {
        return dataSource.getVisitReportManagement(userId, date, date2)
    }

    override fun getVisitReportTeknisi(
        userId: Int,
        date: String,
        date2: String
    ): Single<VisitReportManagementResponse> {
        return dataSource.getVisitReportTeknisi(userId, date, date2)
    }

    override fun getPlannedVisitsBod(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<PlannedVisitsManagementResponse> {
        return dataSource.getPlannedVisitsBod(userId, date, date2, page, perPage)
    }

    override fun getPlannedVisitsManagement(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<PlannedVisitsManagementResponse> {
        return dataSource.getPlannedVisitsManagement(userId, date, date2, page, perPage)
    }

    override fun getPlannedVisitsTeknisi(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<PlannedVisitsManagementResponse> {
        return dataSource.getPlannedVisitsTeknisi(userId, date, date2, page, perPage)
    }

    override fun getUnplannedVisitsBod(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<UnplannedVisitsManagementResponse> {
        return dataSource.getUnplannedVisitsBod(userId, date, date2, page, perPage)
    }

    override fun getUnplannedVisitsManagement(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<UnplannedVisitsManagementResponse> {
        return dataSource.getUnplannedVisitsManagement(userId, date, date2, page, perPage)
    }

    override fun getUnplannedVisitsTeknisi(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ): Single<UnplannedVisitsManagementResponse> {
        return dataSource.getUnplannedVisitsTeknisi(userId, date, date2, page, perPage)
    }

    override fun getEventCalendarBod(
        userId: Int,
        month: Int,
        year: Int
    ): Single<EventCalendarManagementResponse> {
        return dataSource.getEventCalendarBod(userId, month, year)
    }

    override fun getEventCalendarManagement(
        userId: Int,
        month: Int,
        year: Int
    ): Single<EventCalendarManagementResponse> {
        return dataSource.getEventCalendarManagement(userId, month, year)
    }

    override fun getEventCalendarTeknisi(
        userId: Int,
        month: Int,
        year: Int
    ): Single<EventCalendarManagementResponse> {
        return dataSource.getEventCalendarTeknisi(userId, month, year)
    }

    override fun getDailyVisitsBod(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<DailyVisitBodResponse> {
        return dataSource.getDailyVisitsBod(userId, date, page, perPage)
    }

    override fun getDailyVisitsManagement(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<DailyVisitManagementResponse> {
        return dataSource.getDailyVisitsManagement(userId, date, page, perPage)
    }

    override fun getDailyVisitsTeknisi(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<DailyVisitTeknisiResponse> {
        return dataSource.getDailyVisitsTeknisi(userId, date, page, perPage)
    }

}