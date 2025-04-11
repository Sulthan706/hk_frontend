package com.hkapps.hygienekleen.features.features_client.dashboardProject.data.service

import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.DetailDashboardProjectResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.historyAttendance.AttendanceProjectClientResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listEmployee.EmployeeProjectTeamResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listShift.ShiftProjectClientResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listSubLoc.SubLocProjectClientResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DashboardProjectService {

    @GET("/api/v1/client/detail-project")
    fun getDetailDashboardProject(
        @Query("projectCode") projectCode: String
    ): Single<DetailDashboardProjectResponse>

    @GET("/api/v1/client/list-sub-location")
    fun getSubLocProjectClient(
        @Query("projectCode") projectCode: String,
        @Query("locationId") locationId: Int
    ): Single<SubLocProjectClientResponse>

    @GET("/api/v1/client/list-shift")
    fun getShiftProjectClient(
        @Query("projectCode") projectCode: String,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int
    ): Single<ShiftProjectClientResponse>

    @GET("/api/v1/client/work-page-team")
    fun getListTeamEmployeeClient(
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int
    ): Single<EmployeeProjectTeamResponse>

    @GET("/api/v1/client/attendance-history-page")
    fun getHistoryAttendanceProject(
        @Query("projectCode") projectCode: String,
        @Query("date") date: String
    ): Single<AttendanceProjectClientResponse>

}