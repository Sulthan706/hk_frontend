package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.data.service

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
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface PeriodicManagementService {

    @GET("api/v1/rkb/management/home")
    fun getHomeManagementPeriodic(
        @Query("projectCode") projectCode: String
    ): Single<PeriodicHomeManagementResponseModel>

    @GET("api/v1/rkb/management/dates")
    fun getMonitorManagementPeriodic(
        @Query("projectCode") projectCode:String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<PeriodicMonitorManagementResponseModel>

    @GET("api/v1/rkb/management/calendar")
    fun getCalendarManagementPeriodic(
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("perPage") perPage:Int
    ): Single<PeriodicCalendarManagementResponseModel>

    @GET("api/v1/rkb/management/calendar/events")
    fun getCalendarEventManagementPeriodic(
        @Query("projectCode") projectCode: String,
        @Query("month") month: String,
        @Query("year") year: String
    ): Single<PeriodicCalendarEventResponseModel>

    @GET("api/v1/rkb/management/fetch")
    fun getDetailManagamentPeriodic(
        @Query("idJobs") idJobs: Int
    ): Single<PeriodicDetailResponseModel>

    @GET("api/v1/rkb/management/status")
    fun getByStatusManagementPeriodic(
        @Query("projectCode") projectCode: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("filterBy") filterBy: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("locationId") locationId : Int
    ): Single<PeriodicByStatusResponseModel>

    @PUT("api/v1/rkb/management/approve")
    fun putApproveJobsManagement(
        @Query("idJobs") idJobs: Int,
        @Query("adminMasterId") adminMasterId: Int
    ): Single<PeriodApproveJobMgmntResponseModel>

    @PUT("api/v1/rkb/management/diverted/approve")
    fun putCreateBaManagement(
        @Query("idJobs") idJobs: Int,
        @Query("adminMasterId") adminMasterId: Int
    ): Single<PeriodCreateBaManagementResponseModel>

    @GET("api/v1/rkb/management/escalation")
    fun getEscallationApproveManagement(
        @Query("projectCode") projectCode: String
    ): Single<PeriodicEscallationResponseModel>


}