package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.data.service.PeriodicManagementService
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicapprovejobmanagement.PeriodApproveJobMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicbystatusmanagement.PeriodicByStatusResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodiccalendarevent.PeriodicCalendarEventResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodiccalendarmanagement.PeriodicCalendarManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodiccreatebamanagement.PeriodCreateBaManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicdetailmanagement.PeriodicDetailResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicescallationmanagement.PeriodicEscallationResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodichomemanagement.PeriodicHomeManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicmonitormanagement.PeriodicMonitorManagementResponseModel
import io.reactivex.Single
import javax.inject.Inject

class PeriodicManagementDataSourceImpl @Inject constructor(private val service: PeriodicManagementService):
PeriodicManagementDataSource {

    override fun getHomeManagementPeriodic(projectCode: String): Single<PeriodicHomeManagementResponseModel> {
        return service.getHomeManagementPeriodic(projectCode)
    }

    override fun getMonitorManagementPeriodic(
        projectCode: String,
        startDate: String,
        endDate: String
    ): Single<PeriodicMonitorManagementResponseModel> {
        return service.getMonitorManagementPeriodic(projectCode, startDate, endDate)
    }

    override fun getCalendarManagementPeriodic(
        projectCode: String,
        date: String,
        page: Int,
        perPage: Int
    ): Single<PeriodicCalendarManagementResponseModel> {
        return service.getCalendarManagementPeriodic(projectCode, date, page, perPage)
    }

    override fun getCalendarEventManagementPeriodic(
        projectCode: String,
        month: String,
        year: String
    ): Single<PeriodicCalendarEventResponseModel> {
        return service.getCalendarEventManagementPeriodic(projectCode, month, year)
    }

    override fun getDetailManagamentPeriodic(idJobs: Int): Single<PeriodicDetailResponseModel> {
        return service.getDetailManagamentPeriodic(idJobs)
    }

    override fun getByStatusManagementPeriodic(
        projectCode: String,
        startDate: String,
        endDate: String,
        filterBy: String,
        page: Int,
        perPage: Int,
        locationId : Int
    ): Single<PeriodicByStatusResponseModel> {
        return service.getByStatusManagementPeriodic(projectCode, startDate, endDate, filterBy, page, perPage,locationId)
    }

    override fun putApproveJobsManagement(
        idJobs: Int,
        adminMasterId: Int
    ): Single<PeriodApproveJobMgmntResponseModel> {
        return service.putApproveJobsManagement(idJobs, adminMasterId)
    }

    override fun putCreateBaManagement(
        idJobs: Int,
        adminMasterId: Int
    ): Single<PeriodCreateBaManagementResponseModel> {
        return service.putCreateBaManagement(idJobs, adminMasterId)
    }

    override fun getEscallationApproveManagement(projectCode: String): Single<PeriodicEscallationResponseModel> {
        return service.getEscallationApproveManagement(projectCode)
    }


}