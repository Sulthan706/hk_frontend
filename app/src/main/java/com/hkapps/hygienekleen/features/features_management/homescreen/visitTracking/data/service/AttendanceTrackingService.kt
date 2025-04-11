package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.service

import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.BranchNameManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.HistoryAttendanceNew.HistoryAttendanceNewResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.TotalProjectBranchResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listBranch.AllBranchVisitResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listCountProjectNew.ProjectCountDateNewResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listHistoryAttendance.AttendancesProjectManagementResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AttendanceTrackingService {

    @GET("/api/v1/employee/management/attendance/history/all")
    fun getAllAttendanceHistory(
        @Query("adminMasterId") userId: Int
    ): Single<AttendancesProjectManagementResponse>

    @GET("/api/v1/employee/management/attendance/history/date")
    fun getAttendanceHistoryDate(
        @Query("adminMasterId") userId: Int,
        @Query("startDate") startDate: String
    ): Single<AttendancesProjectManagementResponse>

    @GET("/api/v1/employee/management/attendance/count/total")
    fun getCountProjectDate(
        @Query("adminMasterId") userId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<ProjectCountDateNewResponse>

    @GET("/api/v1/project/branch/get-branch-name")
    fun getBranchManagement(
        @Query("employeeId") userId: Int
    ): Single<BranchNameManagementResponse>

    @GET("/api/v1/project/branch")
    fun getAllBranch(): Single<AllBranchVisitResponse>

    @GET("/api/v1/project/branch/count-project")
    fun getTotalProjectBranch(
        @Query("branchCode") branchCode: String
    ): Single<TotalProjectBranchResponse>

    @GET("/api/v1/employee/management/attendance/history/all/date")
    fun getHistoryAttendanceNew(
        @Query("adminMasterId") userId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Single<HistoryAttendanceNewResponse>

}