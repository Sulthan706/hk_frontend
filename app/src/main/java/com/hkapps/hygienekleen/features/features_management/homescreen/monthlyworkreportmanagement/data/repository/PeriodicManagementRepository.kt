package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.data.repository

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

interface PeriodicManagementRepository {

    fun getHomeManagementPeriodic(
        projectCode: String
    ): Single<PeriodicHomeManagementResponseModel>

    fun getMonitorManagementPeriodic(
        projectCode:String,
        startDate: String,
        endDate: String
    ): Single<PeriodicMonitorManagementResponseModel>

    fun getCalendarManagementPeriodic(
        projectCode: String,
        date: String,
        page: Int,
        perPage:Int
    ): Single<PeriodicCalendarManagementResponseModel>

    fun getCalendarEventManagementPeriodic(
        projectCode: String,
        month: String,
        year: String
    ): Single<PeriodicCalendarEventResponseModel>

    fun getDetailManagamentPeriodic(
        idJobs: Int
    ): Single<PeriodicDetailResponseModel>

    fun getByStatusManagementPeriodic(
        projectCode: String,
        startDate: String,
        endDate: String,
        filterBy: String,
        page: Int,
        perPage: Int,
        locationId : Int
    ): Single<PeriodicByStatusResponseModel>

    fun putApproveJobsManagement(
        idJobs: Int,
        adminMasterId: Int
    ): Single<PeriodApproveJobMgmntResponseModel>

    fun putCreateBaManagement(
        idJobs: Int,
        adminMasterId: Int
    ): Single<PeriodCreateBaManagementResponseModel>

    fun getEscallationApproveManagement(
        projectCode: String
    ): Single<PeriodicEscallationResponseModel>

}