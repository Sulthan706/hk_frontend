package com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.service

import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.attendanceReportManagement.AttendanceReportHighResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.countAttendanceReport.CountAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.listBranch.BranchesAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.listProject.ProjectsAttendanceReportResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportHighService {

    @GET("/api/v1/bod/report/get/employee")
    fun getListAttendanceReportHigh(
        @Query("branchCode") branchCode: String,
        @Query("projectCode") projectCode: String,
        @Query("startAt") startAt: String,
        @Query("endAt") endAt: String,
        @Query("order") order: String,
        @Query("whatToOrder") whatToOrder: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<AttendanceReportHighResponse>

    @GET("/api/v1/project/branch")
    fun getListBranchAttendanceReport(): Single<BranchesAttendanceReportResponse>

    @GET("/api/v1/bod/report/get/employee/count")
    fun getCountAttendanceReportHigh(
        @Query("branchCode") branchCode: String,
        @Query("projectCode") projectCode: String,
        @Query("startAt") startAt: String,
        @Query("endAt") endAt: String,
    ): Single<CountAttendanceReportResponse>

    @GET("api/v1/project/list-projects-by-branch")
    fun getListProjectAttendanceReport(
        @Query("branchCode") branchCode: String
    ): Single<ProjectsAttendanceReportResponse>

}