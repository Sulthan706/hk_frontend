package com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.remote

import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.attendanceReportManagement.AttendanceReportHighResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.countAttendanceReport.CountAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.listBranch.BranchesAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.listProject.ProjectsAttendanceReportResponse
import io.reactivex.Single

interface ReportHighDataSource {

    fun getListAttendanceReportHigh(
        branchCode: String,
        projectCode: String,
        startAt: String,
        endAt: String,
        order: String,
        whatToOrder: String,
        page: Int,
        size: Int
    ): Single<AttendanceReportHighResponse>

    fun getListBranchAttendanceReport(): Single<BranchesAttendanceReportResponse>

    fun getCountAttendanceReportHigh(
        branchCode: String,
        projectCode: String,
        startAt: String,
        endAt: String,
    ): Single<CountAttendanceReportResponse>

    fun getListProjectAttendanceReport(
        branchCode: String
    ): Single<ProjectsAttendanceReportResponse>

}