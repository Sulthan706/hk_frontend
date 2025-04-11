package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.service

import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.dacAbsentOpr.DacOperationalAbsentMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsent.DetailAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsentByStatus.DetailAbsentByStatusResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listBranch.ListBranchAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listOperational.ListCountAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByBranch.ListProjectByBranchAbsentOprResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByUser.ListProjectByUserAbsentOprResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AbsentOprMgmntService {

    @GET("/api/v1/project/branch")
    fun getListBranch(): Single<ListBranchAbsentOprMgmntResponse>

    @GET("/api/v1/project/branch/branch-code")
    fun getListProjectByBranch(
        @Query("branchCode") branchCode: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ListProjectByBranchAbsentOprResponse>

    @GET("/api/v1/management/project/v2/{id}")
    fun getListProjectByUser(
        @Path("id") employeeId: Int
    ): Single<ListProjectByUserAbsentOprResponse>

    @GET("/api/v1/attendance/project/info/per-month")
    fun getListAbsentCountOpr(
        @Query("projectCode") projectCode: String,
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Query("jobRole") jobRole: String
    ): Single<ListCountAbsentOprMgmntResponse>

    @GET("/api/v1/attendance/employee/info")
    fun getDetailAbsentOpr(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Single<DetailAbsentOprMgmntResponse>

    @GET("/api/v1/project/search")
    fun searchProject(
        @Query("page") page: Int,
        @Query("branchCode") branchCode: String,
        @Query("keywords") keywords: String,
        @Query("perPage") perPage: Int
    ): Single<ListProjectByBranchAbsentOprResponse>

    @GET("/api/v1/employee/schedule/status")
    fun getDetailAbsentByStatusOpr(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectCode: String,
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Query("scheduleType") scheduleType: String,
        @Query("statusAttendance") statusAttendance: String
    ): Single<DetailAbsentByStatusResponseModel>

    @GET("/api/v1/employee/dac")
    fun getDacOperational(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectId: String,
        @Query("idDetailEmployeeProject") idDetailEmployeeProject: Int
    ): Single<DacOperationalAbsentMgmntResponse>
}